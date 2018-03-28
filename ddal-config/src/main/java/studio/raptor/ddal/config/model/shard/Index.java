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

package studio.raptor.ddal.config.model.shard;

import java.util.List;

/**
 * 索引
 *
 * @author Charley
 * @since 1.0
 */
public class Index extends Table{
  //索引类型，单字段或联合索引
  private String type;
  private String refColumn;
  private List<String> columns;
  private boolean isCopy = false;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRefColumn() {
    return refColumn;
  }

  public void setRefColumn(String refColumn) {
    this.refColumn = refColumn;
  }

  public List<String> getColumns() {
    return columns;
  }

  public void setColumns(List<String> columns) {
    this.columns = columns;
  }

  public boolean isCopy() {
    return isCopy;
  }

  public void setCopy(boolean copy) {
    isCopy = copy;
  }

  @Override
  public String toString() {
    return "Index{" +
        "type='" + type + '\'' +
        ", refColumn='" + refColumn + '\'' +
        ", columns=" + columns +
        ", isCopy=" + isCopy +
        "} " + super.toString();
  }
}
