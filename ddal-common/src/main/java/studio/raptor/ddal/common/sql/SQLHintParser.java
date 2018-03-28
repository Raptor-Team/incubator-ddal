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

package studio.raptor.ddal.common.sql;

import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.List;

/**
 * sql 注释解析器。
 * 支持注释类型:
 * <ul>
 * <li>datasource(string) 指定SQL执行数据源</li>
 * <li>limit(offset=number, count=number) 结果集条数限制</li>
 * <li>shard(column=value) 隐藏字段分片</li>
 * <li>readonly 只读数据源选择标记</li>
 * </ul>
 *
 * 如果datasource(dsName)的dsName为空，解析器会忽略该无效注释。
 *
 * @author Sam
 * @since 3.0.0
 */
public class SQLHintParser {

  static Joiner commaJoiner = Joiner.on(',');

  /**
   * SQL注释器唯一向外暴露的API。
   *
   * @param sql 带注释的SQL语句
   * @return 注释对象
   */
  public static SQLHint parse(String sql) {
    List<Token> tokens = new HintLexer(sql).lex();
    if (null != tokens && !tokens.isEmpty()) {
      SQLHint sqlHint = new SQLHint();
      for (Token token : tokens) {
        if (token instanceof Datasource) {
          sqlHint.datasource = (Datasource) token;
        }
        if (token instanceof Page) {
          sqlHint.page = (Page) token;
        }

        if (token instanceof Shard) {
          sqlHint.shard = (Shard) token;
        }

        if (token instanceof ReadOnly) {
          sqlHint.readonly = (ReadOnly) token;
        }
      }
      return sqlHint;
    }
    return null;
  }

  private static class HintLexer {

    private static String start = "/*!hint";
    private static int startLen = start.length();
    private static String end = "*/";
    private static int endLen = end.length();

    private String sql;
    private int sqlLen;
    private char ch;
    private int mark = 0;
    private int pos = 0;

    HintLexer(String sql) {
      this.sql = sql;
      this.sqlLen = sql.length();
    }

    private boolean isBlank(final CharSequence cs) {
      int strLen;
      if (cs == null || (strLen = cs.length()) == 0) {
        return true;
      }
      for (int i = 0; i < strLen; i++) {
        if (!Character.isWhitespace(cs.charAt(i))) {
          return false;
        }
      }
      return true;
    }

    private List<Token> lex() {
      // 输入有效性快速校验
      if (!fastCheck()) {
        return null;
      }
      String stringVal;
      List<Token> result = new ArrayList<>();
      boolean reachedEnd = false;
      for (; ; ) {
        if (pos >= sqlLen - 1) {
          break;
        }
        readChar();
        switch (ch) {
          case '/':
            mark = pos;
            if ('*' == readChar() && '!' == readChar()) {
              stringVal = readNext(4);
              if ("hint".equals(stringVal)) {
                result.add(new PlainText(start, Type.START));
              } else {
                pos = mark;
                break;
              }
            } else {
              pos = mark;
              continue;
            }
            break;
          case '*':
            mark = pos;
            if ('/' == readChar()) {
              result.add(new PlainText(end, Type.END));
              reachedEnd = true;
            } else {
              pos = mark;
            }
            break;
          case 'd':
            mark = pos;
            stringVal = readNext(9);
            if ("atasource".equals(stringVal)) {
              stringVal = readUtil('(');
              if (!isBlank(stringVal)) {
                throw new IllegalArgumentException(
                    "Illegal hint exception: '(' is expected after datasource");
              }
              stringVal = readUtil(')').trim();
              if (!isBlank(stringVal)) {
                result.add(new Datasource(stringVal));
              }
            } else {
              pos = mark;
            }
            break;
          case 'p':
            mark = pos;
            stringVal = readUtil('(').trim();
            if ("age".equals(stringVal)) {
              Page page = new Page();
              stringVal = readUtil('=').trim();
              if ("offset".equals(stringVal)) {
                stringVal = readUtil(',').trim();
                try {
                  page.offset = Integer.parseInt(stringVal);
                } catch (Exception ignore) {

                }
              }
              if ("count".equals(stringVal)) {
                stringVal = readUtil(',').trim();
                try {
                  page.count = Integer.valueOf(stringVal);
                } catch (Exception ignore) {

                }
              }
              stringVal = readUtil('=').trim();
              if ("offset".equals(stringVal)) {
                stringVal = readUtil(',').trim();
                try {
                  page.offset = Integer.valueOf(stringVal);
                } catch (Exception ignore) {

                }
              }
              if ("count".equals(stringVal)) {
                stringVal = readUtil(')').trim();
                try {
                  page.count = Integer.valueOf(stringVal);
                } catch (Exception ignore) {

                }
              }
              result.add(page);
            } else {
              pos = mark;
            }
          case 's':
            mark = pos;
            stringVal = readUtil('(').trim();
            if ("hard".equals(stringVal)) {
              Shard shard = new Shard();
              shard.column = readUtil('=').trim();
              shard.value = readUtil(')').trim();
              result.add(shard);
            } else {
              pos = mark;
              break;
            }

          case 'r':
            mark = pos;
            stringVal = readNext(7);
            if ("eadonly".equals(stringVal)) {
              readChar();
              while (ch == 0x20) {
                readChar();
              }
              if ('(' == ch) {
                stringVal = readUtil(')');
                if (isBlank(stringVal)) {
                  result.add(new ReadOnly());
                } else {
                  String[] seq = stringVal.trim().split(",");
                  ReadOnly readOnly = new ReadOnly();
                  for (String s : seq) {
                    try {
                      readOnly.addSeq(Integer.parseInt(s));
                    } catch (Exception e) {
                      throw new IllegalArgumentException(
                          "Illegal hint exception: invalid readonly seq number.");
                    }
                  }
                  result.add(readOnly);
                }
              } else {
                result.add(new ReadOnly());
              }
            } else {
              pos = mark;
              break;
            }
          default:
            break;
        }
        if (reachedEnd) {
          break;
        }
      }
      return result;
    }

    /**
     * 读取指定个数字符。
     *
     * @param len 字符个数
     * @return 字符组成的字符串
     */
    private String readNext(int len) {
      StringBuilder builder = new StringBuilder(len);
      for (int i = 0; i < len; i++) {
        try {
          builder.append(readChar());
        } catch (Exception ignore) {
          break;
        }
      }
      return builder.toString();
    }

    /**
     * 往后读取字符串，直到stop字符为止。
     *
     * @param stop 休止符
     * @return 字符串
     */
    private String readUtil(char stop) {
      StringBuilder builder = new StringBuilder();
      for (; ; ) {
        try {
          readChar();
        } catch (Exception readException) {
          break;
        }
        if (stop == this.ch) {
          break;
        }
        builder.append(this.ch);
      }
      return builder.toString();
    }

    private boolean fastCheck() {
      return sqlLen > (startLen + endLen);
    }

    private char readChar() {
      return ch = sql.charAt(pos++);
    }
  }

  public static class SQLHint {

    private Datasource datasource;
    private Page page;
    private Shard shard;
    private ReadOnly readonly;

    public Page getPage() {
      return page;
    }

    public Shard getShard() {
      return shard;
    }

    public ReadOnly getReadonly() {
      return readonly;
    }

    public Datasource getDatasource() {
      return datasource;
    }

    /**
     * 注解规格，不包含具体变量值的字符串。在生成SQL指纹时使用。
     *
     * @return 注释规格。
     */
    public String toSpec() {
      StringBuilder specBuilder = new StringBuilder("DDALHintSpec");
      if (null != datasource) {
        specBuilder.append(";");
        specBuilder.append("datasource");
      }
      if (null != page) {
        specBuilder.append(";");
        specBuilder.append("page");
      }
      if (null != shard) {
        specBuilder.append(";");
        specBuilder.append("shard").append("+").append(shard.getColumn());
      }

      if (null != readonly) {
        specBuilder.append(";");
        specBuilder.append("readonly");
      }
      return specBuilder.toString();
    }

    @Override
    public String toString() {
      return HintLexer.start + " "
          + (null == datasource ? "" : datasource.toString() + "; ")
          + (null == page ? "" : page.toString() + "; ")
          + (null == shard ? "" : shard.toString() + "; ")
          + (null == readonly ? "" : readonly.toString() + "; ")
          + HintLexer.end;
    }
  }

  private enum Type {
    START, END, METHOD, COMMA, SEMI,
  }


  public static abstract class Token {

    private String value;
    private Type type;

    Token(String value, Type type) {
      this.value = value;
      this.type = type;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "Token{" +
          "value='" + value + '\'' +
          ", type=" + type +
          '}';
    }
  }

  static class PlainText extends Token {

    PlainText(String value, Type type) {
      super(value, type);
    }

    @Override
    public String toString() {
      return super.value;
    }
  }

  /**
   * 只读注释对象。
   */
  public static class ReadOnly extends Token {

    ReadOnly() {
      super("readonly", Type.METHOD);
      seq = new ArrayList<>();
    }

    /**
     * 只读数据库序号。
     * 因为支持故障转移，所以会有多个序号。
     */
    private List<Integer> seq;

    public List<Integer> getSeq() {
      return seq;
    }

    public void addSeq(Integer seq) {
      this.seq.add(seq);
    }

    @Override
    public String toString() {
      if (this.seq.isEmpty()) {
        return "readonly";
      } else {
        return "readonly(" + commaJoiner.join(this.seq) + ")";
      }
    }
  }

  public static class Page extends Token {

    Page() {
      super("page", Type.METHOD);
    }

    private int offset;
    private int count;

    public int getOffset() {
      return offset;
    }

    public int getCount() {
      return count;
    }

    @Override
    public String toString() {
      return "page(" +
          "offset=" + offset +
          ", count=" + count +
          ")";
    }
  }

  public static class Shard extends Token {

    Shard() {
      super("shard", Type.METHOD);
    }

    private String column;
    private String value;

    public String getColumn() {
      return column;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return "shard(" + column + "=" + value + ")";
    }
  }

  public static class Datasource extends Token {

    private String dsName;

    Datasource(String datasource) {
      super("datasource", Type.METHOD);
      this.dsName = datasource;
    }

    public String getDsName() {
      return dsName;
    }

    @Override
    public String toString() {
      return "datasource(" + dsName + ")";
    }
  }
}
