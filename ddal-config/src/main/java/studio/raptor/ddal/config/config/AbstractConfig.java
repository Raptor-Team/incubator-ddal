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

import java.io.ByteArrayInputStream;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.exception.ConfigErrCodes;
import studio.raptor.ddal.config.fetcher.ConfigFetcher;
import studio.raptor.ddal.config.fetcher.FetcherHolder;

/**
 * 配置抽象类
 *
 * @author Charley
 * @since 1.0
 */
public abstract class AbstractConfig {

  protected ConfigFetcher configFetcher;

  public AbstractConfig() {
    configFetcher = FetcherHolder.get();
  }

  /**
   * XML合法性验证
   *
   * @param xml //配置文件串
   * @param xsd //校验文件串
   * @return //校验是否通过
   * @throws GenericException //受检异常
   */
  protected boolean validate(String xml, String xsd) throws GenericException {
    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Source schemaSrc = new StreamSource(new ByteArrayInputStream(xsd.getBytes("UTF-8")));
      Schema schema = schemaFactory.newSchema(schemaSrc);
      Validator validator = schema.newValidator();
      Source source = new StreamSource(new ByteArrayInputStream(xml.getBytes("UTF-8")));
      validator.validate(source);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      throw new GenericException(ConfigErrCodes.CONFIG_102, e, "", xml);
    }
  }

  /**
   * 获取文件字符串
   */
  protected String getFileString(String filePath) throws GenericException {
    return configFetcher.getFileString(filePath);
  }

  /**
   * 获取属性Map
   */
  protected Map<String, String> getPropsMap(String filePath) throws Exception {
    return configFetcher.getProperties(filePath);
  }

  /**
   * 配置支持重新加载。
   */
  public abstract void reload();
}
