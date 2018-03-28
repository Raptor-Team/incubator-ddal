/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studio.raptor.sqlparser.ast.statement;

import java.util.List;
import studio.raptor.sqlparser.ast.SQLCommentHint;
import studio.raptor.sqlparser.ast.SQLDataTypeImpl;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLCharacterDataType extends SQLDataTypeImpl {

  public final static String CHAR_TYPE_BYTE = "BYTE";
  public final static String CHAR_TYPE_CHAR = "CHAR";
  public List<SQLCommentHint> hints;
  private String charSetName;
  private String collate;
  private String charType;
  private boolean hasBinary;

  public SQLCharacterDataType(String name) {
    super(name);
  }

  public String getCharSetName() {
    return charSetName;
  }

  public void setCharSetName(String charSetName) {
    this.charSetName = charSetName;
  }

  public boolean isHasBinary() {
    return hasBinary;
  }

  public void setHasBinary(boolean hasBinary) {
    this.hasBinary = hasBinary;
  }

  public String getCollate() {
    return collate;
  }

  public void setCollate(String collate) {
    this.collate = collate;
  }

  public String getCharType() {
    return charType;
  }

  public void setCharType(String charType) {
    this.charType = charType;
  }

  public List<SQLCommentHint> getHints() {
    return hints;
  }

  public void setHints(List<SQLCommentHint> hints) {
    this.hints = hints;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.arguments);
    }

    visitor.endVisit(this);
  }
}
