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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import studio.raptor.ddal.common.exception.GenericException;

/**
 * Abstract layer to allow various types of file systems.
 *
 * @author Sam
 * @version 3.0.0
 */
public abstract class FileSystem {

  /**
   * FileSystem options provider
   */
  private volatile FileOptionsProvider optionsProvider;

  public FileOptionsProvider getFileOptionsProvider() {
    return this.optionsProvider;
  }

  /**
   * Set the FileOptionsProvider
   *
   * @param provider The FileOptionsProvider
   */
  public void setFileOptionsProvider(FileOptionsProvider provider) {
    this.optionsProvider = provider;
  }

  public abstract InputStream getInputStream(URL url) throws GenericException;

  public abstract OutputStream getOutputStream(URL url) throws GenericException;

  public abstract OutputStream getOutputStream(File file) throws GenericException;

  public abstract String getPath(File file, URL url, String basePath, String fileName);

  public abstract String getBasePath(String path);

  public abstract String getFileName(String path);

  public abstract URL locateFromURL(String basePath, String fileName);

  public abstract URL getURL(String basePath, String fileName) throws MalformedURLException;
}
