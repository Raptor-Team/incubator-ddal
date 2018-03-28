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

package studio.raptor.ddal.core.engine.plan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import studio.raptor.ddal.common.util.StringUtil;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.AbstractPlanNode;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;
import studio.raptor.ddal.core.engine.plan.node.LinkedPlanNode;
import studio.raptor.ddal.core.engine.plan.node.PlanNode;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;

/**
 * 执行计划执行链
 *
 * @author Sam
 * @since 3.0.0
 */
public class PlanNodeChain {

  private volatile static PlanNodeChain template = null;
  private PlanNode headNode;
  private PlanNode currentNode;

  private static final Object LOCK = new Object();

  private PlanNodeChain() {
    // do nothing
  }

  /**
   * 获取执行计划模版。
   * 运行期只有一个执行计划模版。
   *
   * @return 执行计划模版
   */
  public static PlanNodeChain getInsTemplate() {
    if (null == template) {
      synchronized (LOCK) {
        if (null == template) {
          template = sourcePlanConfiguration();
        }
      }
    }
    return template;
  }

  /**
   * 使用配置文件构造执行计划链。
   * 配置文件：classpath:/plan.properties
   */
  private static PlanNodeChain sourcePlanConfiguration() {
    PlanNodeChain template;
    try {
      Properties properties = new Properties();
      properties.load(PlanNodeChain.class.getResourceAsStream("/plan.properties"));
      Map<String, PlanConfigItem> planConfig = loadPropToMap(properties);
      // set the node which has no pre as template head
      PlanConfigItem headNode = null;

      boolean headNodeFound = false;
      for (Map.Entry<String, PlanConfigItem> entry : planConfig.entrySet()) {
        if (StringUtil.isEmpty(entry.getValue().pre)) {
          headNode = entry.getValue();
          // check whether more than one head node exists.
          if (headNodeFound) {
            throw new IllegalArgumentException("Execute plan contains more than one head node.");
          }
          headNodeFound = true;
        }
      }
      if (null == headNode) {
        throw new RuntimeException(
            "No head plan node found on the template, please check template configuration");
      }
      template = new PlanNodeChain();
      template.headNode = headNode.instance;
      instanceTemplate(headNode, template.headNode, planConfig);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load plan configuration from plan.properties", e);
    }
    return template;
  }

  /**
   * 实例化执行计划节点。
   * classname -> instance
   *
   * @param planConfigItem 节点配置项
   * @param planNode 当前节点实例
   * @param allPlanConfig 所有节点的配置
   */
  private static void instanceTemplate(
      PlanConfigItem planConfigItem,
      PlanNode planNode, Map<String, PlanConfigItem> allPlanConfig) {

    if (!StringUtil.isEmpty(planConfigItem.next)) {
      // current is a process node
      PlanConfigItem nextPlanConfigItem = allPlanConfig.get(planConfigItem.next);
      PlanNode nextNode = nextPlanConfigItem.instance;
      ((ProcessNode) planNode).setNext(nextNode);
      instanceTemplate(nextPlanConfigItem, nextNode, allPlanConfig);
    } else if (!StringUtil.isEmpty(planConfigItem.forking)) {
      // current is a forking node
      for (String fork : planConfigItem.forkingArray) {
        PlanConfigItem nextPlanConfigItem = allPlanConfig.get(fork);
        if (((ForkingNode) planNode).getForkingList().size() == 2) {
          continue;
        }
        PlanNode forkNode = nextPlanConfigItem.instance;
        ((ForkingNode) planNode).addForking(forkNode);
        instanceTemplate(nextPlanConfigItem, forkNode, allPlanConfig);
      }
    }
  }

  /**
   * 将Properties文件转换成Map，Key是节点名称，Value是自定义的{@link PlanConfigItem}，
   * 其包含了该节点的大部分信息。其中instance是该节点对应的PlanNode实例。
   *
   * @param prop plan.properties
   * @return Map
   */
  private static Map<String, PlanConfigItem> loadPropToMap(Properties prop) {
    Set<Object> keys = prop.keySet();
    Map<String, PlanConfigItem> planConfig = new HashMap<>();

    for (Object keyObj : keys) {
      String key = String.valueOf(keyObj), propValue = prop.getProperty(key);
      if (!key.contains(".")) {
        throw new RuntimeException("Illegal plan node key " + key);
      }
      String[] ka = key.split(Pattern.quote("."));
      PlanConfigItem item = planConfig.get(ka[0]);
      if (null == item) {
        item = new PlanConfigItem(ka[0]);
        planConfig.put(ka[0], item);
      }
      switch (ka[1]) {
        case "class":
          item.setClass(propValue);
          break;
        case "next":
          item.next = propValue;
          break;
        case "forking":
          item.setForking(propValue);
          break;
        default:
          throw new IllegalArgumentException("Unsupported plan property " + ka[1]);
      }
    }

    // 设置节点的pre属性
    for (Map.Entry<String, PlanConfigItem> entry : planConfig.entrySet()) {
      if (!StringUtil.isEmpty(entry.getValue().next)) {
        if (!planConfig.containsKey(entry.getValue().next)) {
          throw new IllegalArgumentException("Unknown plan node " + entry.getValue().next);
        }
        planConfig.get(entry.getValue().next).pre = entry.getValue().nodeId;
      }
      if (!StringUtil.isEmpty(entry.getValue().forking)) {
        for (String fork : entry.getValue().forkingArray) {
          if (planConfig.containsKey(fork)) {
            planConfig.get(fork).pre = entry.getValue().nodeId;
          } else {
            throw new IllegalArgumentException("Unknown forking node " + fork);
          }
        }
      }
    }
    return planConfig;
  }

  /**
   * 构造一个新的，没有任何数据的执行链。执行链上的节点在模版执行时不断填充。
   *
   * @return 空的执行链
   */
  public static PlanNodeChain createInstance() {
    return new PlanNodeChain();
  }

  /**
   * 向执行计划实例上添加执行节点。
   *
   * 现在只支持LinkedNode加入。
   *
   * @param node 执行节点
   */
  public void appendInsNode(PlanNode node) {
    if (headNode == null) {
      headNode = node;
    } else {
      ((LinkedPlanNode) currentNode).setNext(node);
    }
    currentNode = node;
  }

  public void runFromStart(ProcessContext context) {
    runFrom(0, context);
  }

  private void runFrom(int nodeIdx, ProcessContext context) {
    if (nodeIdx != 0) {
      throw new RuntimeException("Random execute is not supported for now.");
    }
    headNode.execute0(context);
  }

  private static class PlanConfigItem {

    private String nodeId;
    private String nodeClass;
    private PlanNode instance;
    private String next;
    private String pre;
    private String forking;
    private String[] forkingArray;

    private PlanConfigItem(String nodeId) {
      this.nodeId = nodeId;
    }

    private void setForking(String forking) {
      if (StringUtil.isEmpty(forking)
          || (this.forkingArray = forking.split(Pattern.quote(","))).length < 2) {
        throw new IllegalArgumentException(
            String.format("Forking node [%s] doesn't have two nodes", this.nodeId));
      }
      this.forking = forking;
    }

    private void setClass(String nodeClass) {
      try {
        this.nodeClass = nodeClass;
        this.instance = (PlanNode) Class.forName(this.nodeClass).newInstance();
        ((AbstractPlanNode) this.instance).setNodeId(this.nodeId);
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        throw new RuntimeException("Load plan template failed", e);
      }
    }
  }
}
