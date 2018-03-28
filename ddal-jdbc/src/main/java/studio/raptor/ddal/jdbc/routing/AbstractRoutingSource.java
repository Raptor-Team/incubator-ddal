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

package studio.raptor.ddal.jdbc.routing;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import studio.raptor.ddal.jdbc.routing.lookup.SourceLookup;

/**
 * 抽象路由资源
 *
 * @author Charley
 * @since 1.0
 */
public abstract class AbstractRoutingSource<T> {

  private Map<String, String> targetSources;
  private Map<String, T> originSources;
  private SourceLookup sourceLookup;
  private Map<Object, T> resolvedSources;

  protected void setTargetSources(Map<String, String> targetSources) {
    this.targetSources = targetSources;
  }

  protected void setOriginSources(Map<String, T> originSources) {
    this.originSources = originSources;
  }

  protected void afterPropertiesSet() {
    if (this.targetSources == null || this.originSources == null) {
      throw new IllegalArgumentException("Property \'targetSources\' is required");
    } else {
      this.sourceLookup = new SourceLookup(originSources);

      this.resolvedSources = new HashMap(this.targetSources.size());
      Iterator it = this.targetSources.entrySet().iterator();

      while (it.hasNext()) {
        Map.Entry<String, String> entry = (Map.Entry) it.next();
        String lookupKey = this.resolveSpecifiedLookupKey(entry.getKey());
        T source = this.resolveSpecifiedSource(entry.getValue());
        this.resolvedSources.put(lookupKey, source);
      }
    }
  }

  private String resolveSpecifiedLookupKey(String lookupKey) {
    return lookupKey;
  }

  private T resolveSpecifiedSource(String source) throws IllegalArgumentException {
    return (T) this.sourceLookup.getSource(source);
  }

  protected T determineTargetSource() {
    Preconditions.checkNotNull(this.resolvedSources, "Source router not initialized");
    String lookupKey = this.determineCurrentLookupKey();
    T source = (T) this.resolvedSources.get(lookupKey);

    if (source == null) {
      throw new IllegalStateException(
          "Cannot determine target Source for lookup key [" + lookupKey + "]");
    } else {
      return source;
    }
  }

  protected abstract String determineCurrentLookupKey();

//  public void close() throws IOException {
//    for(T source: resolvedSources.values()){
//      source.close();
//    }
//  }

}
