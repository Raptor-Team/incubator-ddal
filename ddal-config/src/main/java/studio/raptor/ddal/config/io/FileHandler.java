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
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Sam
 * @since 3.0.0
 */
public class FileHandler {

  /**
   * A reference to the current {@code FileLocator} object.
   */
  private final AtomicReference<FileLocator> fileLocator;


  /**
   * Creates a new instance of {@code FileHandler} and sets the managed
   * {@code FileBased} object.
   *
   * @param locator the file-based object to manage
   */
  public FileHandler(FileLocator locator) {
    fileLocator = new AtomicReference<>(locator);
  }

  /**
   * Creates a {@code File} object from the content of the given
   * {@code FileLocator} object. If the locator is not defined, result is
   * <b>null</b>.
   *
   * @param loc the {@code FileLocator}
   * @return a {@code File} object pointing to the associated file
   */
  private static File createFile(FileLocator loc) {
    if (loc.getFileName() == null && loc.getSourceURL() == null) {
      return null;
    } else if (loc.getSourceURL() != null) {
      return FileLocatorUtils.fileFromURL(loc.getSourceURL());
    } else {
      return FileLocatorUtils.getFile(loc.getBasePath(),
          loc.getFileName());
    }
  }

  /**
   * Returns the location of the associated file as a URL. If a URL is set,
   * it is directly returned. Otherwise, an attempt to locate the referenced
   * file is made.
   *
   * @return a URL to the associated file; can be <b>null</b> if the location is unspecified
   */
  public URL getURL() {
    FileLocator locator = getFileLocator();
    return (locator.getSourceURL() != null) ? locator.getSourceURL()
        : FileLocatorUtils.locate(locator);
  }

  /**
   * Returns the location of the associated file as a {@code File} object. If
   * the base path is a URL with a protocol different than &quot;file&quot;,
   * or the file is within a compressed archive, the return value will not
   * point to a valid file object.
   *
   * @return the location as {@code File} object; this can be <b>null</b>
   */
  public File getFile() {
    return createFile(getFileLocator());
  }

  /**
   * Returns a {@code FileLocator} object with the specification of the file
   * stored by this {@code FileHandler}. Note that this method returns the
   * internal data managed by this {@code FileHandler} as it was defined.
   * This is not necessarily the same as the data returned by the single
   * access methods like {@code getFileName()} or {@code getURL()}: These
   * methods try to derive missing data from other values that have been set.
   *
   * @return a {@code FileLocator} with the referenced file
   */
  public FileLocator getFileLocator() {
    return fileLocator.get();
  }
}
