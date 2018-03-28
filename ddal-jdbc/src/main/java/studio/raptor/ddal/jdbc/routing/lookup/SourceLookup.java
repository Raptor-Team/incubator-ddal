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

package studio.raptor.ddal.jdbc.routing.lookup;

import com.google.common.base.Preconditions;
import java.util.Map;

/**
 * 资源查找
 *
 * @author Charley
 * @since 1.0
 */
public class SourceLookup<T> {

  private final Map<String, T> sources;

  public SourceLookup(Map<String, T> sources) {
    this.sources = sources;
  }

  public void setSources(Map<String, T> sources) {
    if (sources != null) {
      this.sources.putAll(sources);
    }

  }

  public void addSource(String sourceName, T source) {
    Preconditions.checkNotNull(sourceName, "Source name must not be null");
    Preconditions.checkNotNull(source, "Source must not be null");
    this.sources.put(sourceName, source);
  }

  public Map<String, T> getSources() {
    return sources;
  }

  public T getSource(String sourceName) throws RuntimeException {
    Preconditions.checkNotNull(sourceName, "Source name must not be null");
    T source = this.sources.get(sourceName);
    if (source == null) {
      throw new RuntimeException("No Source with name \'" + sourceName + "\' registered");
    } else {
      return source;
    }
  }

}
