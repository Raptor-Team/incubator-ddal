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

package studio.raptor.ddal.core.parser.result.merger;

import java.util.List;
import studio.raptor.ddal.core.parser.result.ParameterGenerator;

/**
 * 分页对象.
 */
public class Limit {

  private final Object offset;
  private final Object rowCount;

  public Limit(Object offset, Object rowCount) {
    this.offset = offset;
    this.rowCount = rowCount;
  }

  public int parseOffset(List<Object> parameters) {
    Object offsetObj = ParameterGenerator.parameterize(parameters, offset);
    return (int) offsetObj;
  }

  public int parseRowCount(List<Object> parameters) {
    Object countObj = ParameterGenerator.parameterize(parameters, rowCount);
    return (int) countObj;
  }

  public int getOffset(){
    return (int) offset;
  }

  public int getRowCount(){
    return (int) rowCount;
  }

  @Override
  public String toString() {
    return "Limit{" +
        "offset=" + offset +
        ", rowCount=" + rowCount +
        '}';
  }
}
