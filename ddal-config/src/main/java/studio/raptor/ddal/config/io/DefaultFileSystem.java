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
package studio.raptor.ddal.config.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.exception.ConfigErrCodes;

/**
 * FileSystem that uses java.io.File or HttpClient
 *
 * @author Sam
 * @since 3.0.0
 */
public class DefaultFileSystem extends FileSystem {

  private static Logger logger = LoggerFactory.getLogger(DefaultFileSystem.class);

  @Override
  public InputStream getInputStream(URL url) throws GenericException {
    // throw an exception if the target URL is a directory
    File file = FileLocatorUtils.fileFromURL(url);
    if (file != null && file.isDirectory()) {
      throw new GenericException(ConfigErrCodes.CONFIG_107);
    }
    try {
      return url.openStream();
    } catch (Exception e) {
      throw new GenericException(ConfigErrCodes.CONFIG_108, e);
    }
  }

  @Override
  public OutputStream getOutputStream(URL url) throws GenericException {
    throw new RuntimeException("Unsupported operation getOutputStream");
  }

  @Override
  public OutputStream getOutputStream(File file) throws GenericException {
    throw new RuntimeException("Unsupported operation getOutputStream");
  }

  @Override
  public String getPath(File file, URL url, String basePath, String fileName) {
    String path = null;
    // if resource was loaded from jar file may be null
    if (file != null) {
      path = file.getAbsolutePath();
    }
    // try to see if file was loaded from a jar
    if (path == null) {
      if (url != null) {
        path = url.getPath();
      } else {
        try {
          path = getURL(basePath, fileName).getPath();
        } catch (Exception e) {
          // simply ignore it and return null
          logger.debug(String.format("Could not determine URL for "
                  + "basePath = %s, fileName = %s: %s", basePath,
              fileName, e));
        }
      }
    }
    return path;
  }

  @Override
  public String getBasePath(String path) {
    URL url;
    try {
      url = getURL(null, path);
      return FileLocatorUtils.getBasePath(url);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public String getFileName(String path) {
    URL url;
    try {
      url = getURL(null, path);
      return FileLocatorUtils.getFileName(url);
    } catch (Exception e) {
      return null;
    }
  }


  @Override
  public URL getURL(String basePath, String file) throws MalformedURLException {
    File f = new File(file);
    if (f.isAbsolute()) // already absolute?
    {
      return FileLocatorUtils.toURL(f);
    }

    try {
      if (basePath == null) {
        return new URL(file);
      } else {
        URL base = new URL(basePath);
        return new URL(base, file);
      }
    } catch (MalformedURLException uex) {
      return FileLocatorUtils.toURL(FileLocatorUtils.constructFile(basePath, file));
    }
  }


  @Override
  public URL locateFromURL(String basePath, String fileName) {
    try {
      URL url;
      if (basePath == null) {
        return new URL(fileName);
        //url = new URL(name);
      } else {
        URL baseURL = new URL(basePath);
        url = new URL(baseURL, fileName);

        // check if the file exists
        InputStream in = null;
        try {
          in = url.openStream();
        } finally {
          if (in != null) {
            in.close();
          }
        }
        return url;
      }
    } catch (IOException e) {
      logger.debug("Could not locate file " + fileName + " at " + basePath + ": " + e.getMessage());
      return null;
    }
  }

}
