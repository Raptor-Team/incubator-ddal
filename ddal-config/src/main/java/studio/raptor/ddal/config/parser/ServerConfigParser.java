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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.exception.ConfigErrCodes;
import studio.raptor.ddal.config.config.ServerConfig;
import studio.raptor.ddal.config.model.server.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ServerConfigParser {

    private static final XPath XPATH = XPathFactory.newInstance().newXPath();

    public static void parse(ServerConfig serverConfig, String xml) throws GenericException {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            InputStream is = new ByteArrayInputStream(xml.getBytes());
            Document document = builder.parse(is);
            Element root = document.getDocumentElement();

            createParams(serverConfig, root);
            createUsers(serverConfig, root);
            createGrants(serverConfig, root);
        } catch (Exception e) {
            throw new GenericException(ConfigErrCodes.CONFIG_103, e.getMessage());
        }
    }

    private static void createGrants(ServerConfig serverConfig, Element root) throws XPathExpressionException {
        NodeList grantsDom = (NodeList) XPATH.evaluate("grants/grant", root, XPathConstants.NODESET);
        Grants grants = new Grants();
        for (int i = 0; i < grantsDom.getLength(); i++) {
            Node grantDom = grantsDom.item(i);

            Grant grant = new Grant();
            grant.setUser(XPATH.evaluate("@user", grantDom));
          grant.setVdbName(XPATH.evaluate("@vdbname", grantDom));
            grant.setRules(createRules(grantDom));
            grants.add(grant);
        }
        serverConfig.setGrants(grants);
    }

    private static Map<String, Grant.Rule> createRules(Node grantDom) throws XPathExpressionException {
        NodeList rulesDom = (NodeList) XPATH.evaluate("rule", grantDom, XPathConstants.NODESET);
        Map<String, Grant.Rule> rules = new HashMap<>(rulesDom.getLength());

        for (int i = 0; i < rulesDom.getLength(); i++) {
            Node ruleDom = rulesDom.item(i);
            Grant.Rule rule = new Grant.Rule();
            rule.setType(XPATH.evaluate("type", ruleDom));
            rule.setObj(XPATH.evaluate("object", ruleDom));
            rule.setPrivilege(XPATH.evaluate("privilege", ruleDom));
            rules.put(rule.getType(), rule);
        }
        return rules;
    }

    private static void createUsers(ServerConfig serverConfig, Element root) throws XPathExpressionException {
        NodeList usersDom = (NodeList) XPATH.evaluate("users/user", root, XPathConstants.NODESET);
        Users users = new Users();

        for (int i = 0; i < usersDom.getLength(); i++) {
            Node userDom = usersDom.item(i);

            User user = new User();
            user.setName(XPATH.evaluate("@name", userDom));
            user.setPwd(XPATH.evaluate("@pwd", userDom));
            User.AccessPolicy acceptAp = createAccessPolicy(userDom);
            user.setAccessPolicy(acceptAp);
            users.add(user);
        }
        serverConfig.setUsers(users);
    }

    private static User.AccessPolicy createAccessPolicy(Node userDom) throws XPathExpressionException {
        Node accessPolicyDom = (Node) XPATH.evaluate("accessPolicy", userDom, XPathConstants.NODE);
        if (null != accessPolicyDom) {
            NodeList ipsDom = (NodeList) XPATH.evaluate("ip", accessPolicyDom, XPathConstants.NODESET);
            String[] ips = new String[ipsDom.getLength()];
            for (int i = 0; i < ipsDom.getLength(); i++) {
                Node ipDom = ipsDom.item(i);
                ips[i] = ipDom.getTextContent();
            }
            User.AccessPolicy ap = new User.AccessPolicy(XPATH.evaluate("@type", accessPolicyDom), ips);
            return ap;
        }
        return null;
    }

    private static void createParams(ServerConfig serverConfig, Element root) throws XPathExpressionException {
        NodeList paramsDom = (NodeList) XPATH.evaluate("params/param", root, XPathConstants.NODESET);
        Params params = new Params();

        for (int i = 0; i < paramsDom.getLength(); i++) {
            Node paramDom = paramsDom.item(i);
            params.put(XPATH.evaluate("@name", paramDom), paramDom.getTextContent());
        }
        serverConfig.setParams(params);
    }

}
