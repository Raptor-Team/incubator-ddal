/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studio.raptor.ddal.core.connection.jdbc.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DriverDataSource implements DataSource {

  private static final Logger LOGGER = LoggerFactory.getLogger(DriverDataSource.class);

  private final String jdbcUrl;
  private final Properties driverProperties;
  private Driver driver;

  public DriverDataSource(String jdbcUrl, String driverClassName, Properties properties,
      String username, String password) {
    this.jdbcUrl = jdbcUrl;
    this.driverProperties = new Properties();

    for (Entry<Object, Object> entry : properties.entrySet()) {
      driverProperties.setProperty(entry.getKey().toString(), entry.getValue().toString());
    }

    if (username != null) {
      driverProperties.put("user", driverProperties.getProperty("user", username));
    }
    if (password != null) {
      driverProperties.put("password", driverProperties.getProperty("password", password));
    }

    if (driverClassName != null) {
      Enumeration<Driver> drivers = DriverManager.getDrivers();
      while (drivers.hasMoreElements()) {
        Driver d = drivers.nextElement();
        if (d.getClass().getName().equals(driverClassName)) {
          driver = d;
          break;
        }
      }

      if (driver == null) {
        LOGGER.warn(
            "Registered driver with driverClassName={} was not found, trying direct instantiation.",
            driverClassName);
        Class<?> driverClass = null;
        try {
          driverClass = this.getClass().getClassLoader().loadClass(driverClassName);
          LOGGER.debug("Driver class found in the HikariConfig class classloader {}",
              this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
          ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
          if (threadContextClassLoader != null && threadContextClassLoader != this.getClass()
              .getClassLoader()) {
            try {
              driverClass = threadContextClassLoader.loadClass(driverClassName);
              LOGGER.debug("Driver class found in Thread context class loader {}",
                  threadContextClassLoader);
            } catch (ClassNotFoundException e1) {
              LOGGER.error(
                  "Failed to load class of driverClassName {} in either of HikariConfig class classloader {} or Thread context classloader {}",
                  driverClassName, this.getClass().getClassLoader(), threadContextClassLoader);
            }
          } else {
            LOGGER.error(
                "Failed to load class of driverClassName {} in HikariConfig class classloader {}",
                driverClassName, this.getClass().getClassLoader());
          }
        }
        if (driverClass != null) {
          try {
            driver = (Driver) driverClass.newInstance();
          } catch (Exception e) {
            LOGGER.warn("Failed to create instance of driver class {}, trying jdbcUrl resolution",
                driverClassName, e);
          }
        }
      }
    }

    try {
      if (driver == null) {
        driver = DriverManager.getDriver(jdbcUrl);
      } else if (!driver.acceptsURL(jdbcUrl)) {
        throw new RuntimeException(
            "Driver " + driverClassName + " claims to not accept jdbcUrl, " + jdbcUrl);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to get driver instance for jdbcUrl=" + jdbcUrl, e);
    }
  }

  @Override
  public Connection getConnection() throws SQLException {
    return driver.connect(jdbcUrl, driverProperties);
  }

  @Override
  public Connection getConnection(String username, String password) throws SQLException {
    return getConnection();
  }

  @Override
  public PrintWriter getLogWriter() throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public void setLogWriter(PrintWriter logWriter) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public int getLoginTimeout() throws SQLException {
    return DriverManager.getLoginTimeout();
  }

  @Override
  public void setLoginTimeout(int seconds) throws SQLException {
    DriverManager.setLoginTimeout(seconds);
  }

  @Override
  public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
    return driver.getParentLogger();
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException();
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }
}
