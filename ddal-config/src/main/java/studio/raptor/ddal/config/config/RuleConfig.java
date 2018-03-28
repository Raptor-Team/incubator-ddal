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

package studio.raptor.ddal.config.config;

import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.common.FileLoader;
import studio.raptor.ddal.config.model.rule.ShardRules;
import studio.raptor.ddal.config.parser.RuleConfigParser;

public class RuleConfig extends AbstractConfig {

  private static final String RULE_CONFIG_XSD = "rule-config.xsd";

  private ShardRules shardRules;

  private static RuleConfig instance = new RuleConfig();

  public static RuleConfig getInstance() {
    return instance;
  }

  private RuleConfig() throws GenericException {
    String xmlContent = this.getFileString(this.configFetcher.getRuleConfigPath());
    this.validate(xmlContent, FileLoader.readLocalFile(RULE_CONFIG_XSD));
    RuleConfigParser.parse(this, xmlContent);
  }

  @Override
  public void reload() {
    throw new UnsupportedOperationException("RuleConfig reloading is not supported.");
  }

  public ShardRules getShardRules() {
    return shardRules;
  }

  public void setShardRules(ShardRules shardRules) {
    this.shardRules = shardRules;
  }

  @Override
  public String toString() {
    return "RuleConfig{" +
        "shardRules=" + shardRules +
        "} " + super.toString();
  }
}
