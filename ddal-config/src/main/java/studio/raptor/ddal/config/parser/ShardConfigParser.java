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

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.exception.ConfigErrCodes;
import studio.raptor.ddal.config.parser.shard.DataSourceParser;
import studio.raptor.ddal.config.parser.shard.PhysicalDbParser;
import studio.raptor.ddal.config.parser.shard.ShardGroupParser;
import studio.raptor.ddal.config.parser.shard.virtualDb.VirtualDbParser;

public class ShardConfigParser {

  public static void parse(ShardConfig shardConfig, String xml) throws GenericException {
    try {
      DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = domFactory.newDocumentBuilder();
      InputSource is = new InputSource(new StringReader(xml));
      Document document = builder.parse(is);
      Element root = document.getDocumentElement();

      ShardGroupParser.parse(shardConfig, root);
      VirtualDbParser.parse(shardConfig, root);
      PhysicalDbParser.parse(shardConfig, root);
      DataSourceParser.parse(shardConfig, root);
    } catch (Exception e) {
      e.printStackTrace();
      throw new GenericException(ConfigErrCodes.CONFIG_103, e.getMessage());
    }
  }
}
