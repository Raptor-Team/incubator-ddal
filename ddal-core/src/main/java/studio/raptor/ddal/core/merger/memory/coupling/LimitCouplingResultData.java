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

package studio.raptor.ddal.core.merger.memory.coupling;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.executor.resultset.RowData;


/**
 * 分页限制条件的连接结果集.
 *
 * @author jack
 * @since 3.0.0
 */
public class LimitCouplingResultData implements CouplingResultData {

  private ResultData resultData;
  private final int originOffset;
  private final int originRowCount;

  public LimitCouplingResultData(ResultData resultData, final int originOffset, final int originRowCount) {
    this.resultData = resultData;
    this.originOffset = originOffset;
    this.originRowCount = originRowCount;
  }

  @Override
  public ResultData couple() {
    List<RowData> rowData = new ArrayList<>();
    long endIdx;
    long rowCount = (endIdx = originRowCount + originOffset) > resultData.getRowCount()
        ? resultData.getRowCount() : endIdx;
    for (int i = originOffset; i < rowCount; i++) {
      rowData.add(resultData.getRows().get(i));
    }
    resultData.clearAndAddRows(rowData);
    return resultData;
  }
}
