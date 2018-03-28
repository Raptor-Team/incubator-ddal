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

package studio.raptor.ddal.config.parser;

import com.google.common.base.Strings;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import studio.raptor.ddal.common.algorithm.ShardAlgorithm;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.config.RuleConfig;
import studio.raptor.ddal.config.exception.ConfigErrCodes;
import studio.raptor.ddal.config.model.rule.ShardRule;
import studio.raptor.ddal.config.model.rule.ShardRules;

public class RuleConfigParser {

  private static final XPath XPATH = XPathFactory.newInstance().newXPath();

  public static void parse(RuleConfig ruleConfig, String xml) throws GenericException {
    try {
      DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = domFactory.newDocumentBuilder();
      InputStream is = new ByteArrayInputStream(xml.getBytes());
      Document document = builder.parse(is);
      Element root = document.getDocumentElement();

      createShardRules(ruleConfig, root);
    } catch (Exception e) {
      throw new GenericException(ConfigErrCodes.CONFIG_103, e.getMessage());
    }
  }

  private static void createShardRules(RuleConfig ruleConfig, Element root)
      throws XPathExpressionException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    NodeList shardRulesDom = (NodeList) XPATH
        .evaluate("shardRules/shardRule", root, XPathConstants.NODESET);
    ShardRules shardRules = new ShardRules();
    for (int i = 0; i < shardRulesDom.getLength(); i++) {
      Node shardRuleDom = shardRulesDom.item(i);

      ShardRule shardRule = new ShardRule();
      shardRule.setName(XPATH.evaluate("@name", shardRuleDom));

      String shardColumnString = XPATH.evaluate("@shardColumn", shardRuleDom);
      if (Strings.isNullOrEmpty(shardColumnString)) {
        throw new XPathExpressionException(
            "Shard rule must have shardColumn, ruleName=" + shardRule.getName());
      }
      shardRule.setShardColumns(shardColumnString.trim().toUpperCase().split(","));

      String algorithmClass = XPATH.evaluate("@algorithm", shardRuleDom);
      if (Strings.isNullOrEmpty(algorithmClass)) {
        throw new XPathExpressionException(
            "Shard rule must have algorithmClass, ruleName=" + shardRule.getName());
      }
      Class<?> clazz = Class.forName(algorithmClass);
      //判断是否继承ShardAlgorithm
      if (!ShardAlgorithm.class.isAssignableFrom(clazz)) {
        throw new XPathExpressionException("rule function must implements "
            + ShardAlgorithm.class.getName() + ", rule=" + shardRule.getName());
      }

      ShardAlgorithm shardAlgorithm;
      String param = XPATH.evaluate("@param", shardRuleDom);
      if (Strings.isNullOrEmpty(param)) {
        shardAlgorithm = (ShardAlgorithm) clazz.newInstance();
      } else {
        Constructor<?> constructor = clazz.getConstructor(String.class);
        shardAlgorithm = (ShardAlgorithm) constructor.newInstance(param);
      }
      shardRule.setAlgorithm(shardAlgorithm);

      shardRules.add(shardRule);
    }

    ruleConfig.setShardRules(shardRules);
  }

}
