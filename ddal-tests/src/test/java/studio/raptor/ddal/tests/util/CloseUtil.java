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

package studio.raptor.ddal.tests.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Sam
 * @since 3.0.0
 */
public class CloseUtil {


    public static void close(Statement statement, Connection connection) {
        try {
            if (null != statement) {
                statement.close();
            }
            if (null != connection) {
                connection.close();
            }
        } catch (SQLException ignore) {
        }
    }

    public static void close(List<? extends AutoCloseable> resources) {
        try {
            for (AutoCloseable resource : resources) {
                if (null != resource) {
                    resource.close();
                }
            }
        } catch (Exception ignore) {
        }
    }
}
