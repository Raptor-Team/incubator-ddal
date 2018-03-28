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

package studio.raptor.ddal.core.constants;

/**
 * @author Sam
 * @since 3.0.0
 */
public enum DataSourceAccessLevel {
  RW(0b001, "RW"), R(0b010, "R"), BLOCK(0b100, "BLOCK");

  private int level;
  private String texture;

  DataSourceAccessLevel(int level, String texture) {
    this.level = level;
    this.texture = texture;
  }

  public static final int MASK = 0b111;

  public int getLevel() {
    return level;
  }

  public String getTexture() {
    return texture;
  }

  public static void main(String[] args) {
    System.out.println(DataSourceAccessLevel.RW.getLevel());
    System.out.println((DataSourceAccessLevel.BLOCK.getLevel() & 0b111) == BLOCK.getLevel());
  }

  public static DataSourceAccessLevel textureOf(String texture) {
    for(DataSourceAccessLevel accessLevel : DataSourceAccessLevel.values()) {
      if(accessLevel.getTexture().equalsIgnoreCase(texture)) {
        return accessLevel;
      }
    }
    throw new IllegalArgumentException("Unsupported access level:" + texture);
  }
}
