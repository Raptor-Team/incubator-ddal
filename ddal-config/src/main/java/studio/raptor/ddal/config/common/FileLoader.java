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

package studio.raptor.ddal.config.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.exception.ConfigErrCodes;
import studio.raptor.ddal.config.exception.ConfigNotFoundException;

public class FileLoader {

    public static String readLocalFile(String filename) throws GenericException {
        try {
            return FileLoader.readFile(filename);
        } catch (IOException e) {
            throw new GenericException(ConfigErrCodes.CONFIG_101, e, "", filename);
        }
    }

    public static Properties loadLocalProps(String filePath) throws Exception {
        return FileLoader.loadProps(filePath);
    }

    private static String readFile(String filename) throws IOException {
        String content = null;
        InputStream in = null;
        BufferedReader br = null;
        try {
            StringBuilder sb = new StringBuilder();
            in = FileLoader.class.getClassLoader().getResourceAsStream(filename);
            if (null == in) {
                throw new IOException(filename + " is not found");
            }
            br = new BufferedReader(new InputStreamReader(in, Charset.forName("utf-8")));
            while ((content = br.readLine()) != null) {
                sb.append(content).append("\n");
            }
            content = sb.toString();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private static Properties loadProps(String filePath) throws Exception {
        Properties props = new Properties();
        InputStream ips = FileLoader.class.getClassLoader().getResourceAsStream(filePath);
        if(null == ips){
            throw new ConfigNotFoundException();
        }
        try {
            props.load(ips);
        } finally {
            try {
                ips.close();
            } catch (Exception e) {
            }
        }
        return props;
    }
}
