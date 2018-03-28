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

package studio.raptor.ddal.core.engine.plan.node.impl.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import studio.raptor.ddal.common.sql.SQLHintParser.Page;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.parser.result.merger.Limit;

/**
 * 处理分页线索。
 *
 * @author Sam
 * @since 3.0.0
 */
public class HandlePageHint extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    Page page = context.getSqlHint().getPage();
    if (page.getCount() <= 0) {
      throw new RuntimeException(String.format(
          "Paging comment [%s] is ignored, because the value of paging parameter [count] is less than or equal to 0",
          context.getSqlHint().getPage().toString()));
    }
    Limit limit = new Limit(page.getOffset(), page.getCount());
    context.getParseResult().setLimit(limit);
  }


  /**
   * 解析注释中的分页信息。 当offset和count两个变量都设置，并且count>0
   * 才会返回limit对象。
   *
   * @param comment sql注释
   * @return 分页信息
   */
  private Limit parseLimitHint(String comment) {
    Limit limit = null;
    String pageComment = findPageComment(comment);

    if (null != pageComment) {
      String[] commentSplit = pageComment.split(",");
      if (commentSplit.length == 2) {
        int offset = 0, count = 0;
        for (String limitItem : commentSplit) {
          String[] limitSplit = limitItem.split("=");
          // 如果注释格式配置不合理，抛出异常提示。
          if (limitSplit.length != 2) {
            throw new IllegalArgumentException(
                String.format("Invalid hint config of page [%s]", pageComment));
          }
          if (limitSplit[0].trim().toUpperCase().equals("OFFSET")) {
            offset = parseHintParam(limitSplit[1], "OFFSET");
          } else if (limitSplit[0].trim().toUpperCase().equals("COUNT")) {
            count = parseHintParam(limitSplit[1], "COUNT");
          }
        }
        if (count > 0) {
          limit = new Limit(offset, count);
        } else {
          throw new RuntimeException(String.format(
              "Paging comment [%s] is ignored, because the value of paging parameter [count] is less than or equal to 0",
              comment));
        }
      } else {
        throw new RuntimeException(String.format("Invalid hint config of page [%s]", pageComment));
      }
    }
    return limit;
  }

  private String findPageComment(String comments) {
    Pattern pattern = Pattern.compile("page\\([\\s\\S]*?\\)");
    Matcher matcher = pattern.matcher(comments);

    boolean singleMatch = false;
    String pageContent = null;
    while (matcher.find()) {
      if (singleMatch) {
        throw new RuntimeException(String.format("Duplicate hint config of %s", "page"));
      }
      singleMatch = true;
      String mGrp = matcher.group();
      pageContent = mGrp.substring(5, mGrp.length() - 1).trim();
      if ("".equals(pageContent)) {
        throw new RuntimeException(String.format("Empty hint config content of %s", "page"));
      }
    }
    return pageContent;
  }

  private Integer parseHintParam(String val, String exceptionAdvice) {
    if (null == val || !(val = val.trim()).matches("\\d+")) {
      throw new IllegalArgumentException(
          String.format("Illegal sql comment value [%s] for parameter [%s]", val, exceptionAdvice));
    }
    return Integer.parseInt(val);
  }
}
