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

package studio.raptor.ddal.config.common;

import java.security.NoSuchAlgorithmException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.config.config.SystemProperties;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ConfigToolsTest {

  @Test
  public void testGenKeyPair() throws NoSuchAlgorithmException {
    String[] keyPairs = ConfigTools.genKeyPair(2048);
    System.out.println("PrivateKey:" + keyPairs[0]);
    System.out.println("PublicKey:" + keyPairs[1]);
  }

  /**
   *  sa
   *  LGK1MqOM+84RbgXqo61WtsBFH8g94ME3IgrGaOfvdeIj3r+KfAtk/WCKSyMBoeVOkXicRfLarmDQMBS+090FHT3NPhqlgd1KsMfYoYQJ5v4h/d1GLJ//Fyj2gJ0D6EruKs4olqlsUI2/KyBjAacXCzV4Vzn5AqHwlqjWsXH4MDIVOcbzc48+wAeIWnDdFjW7RmV7mArQfVeAGHWFyvYR2+NCUOM/9u3IjhcP2TNuWoFDugXrg6H5m2fwFZAWvCuenpyP2K+iMPKSZzbeUKtmZ4JSA7yTaCz+2+gFfVQroCSJ5reR3X5Op87ZTs9sdFmzFYSzpUqILV0yj10lJmuXrQ==
   * @throws Exception
   */
  @Test
  public void testGenPassword() throws Exception {
    System.out.println(ConfigTools.encrypt(SystemProperties.getInstance().getMapper().get("config.encrypt.key"), "crm123"));
  }

  @Test
  public void testDecrypt() throws Exception {
    Assert.assertEquals("sa", ConfigTools.decrypt(SystemProperties.getInstance().getMapper().get("config.decrypt.key"),
        "LGK1MqOM+84RbgXqo61WtsBFH8g94ME3IgrGaOfvdeIj3r+KfAtk/WCKSyMBoeVOkXicRfLarmDQMBS+090FHT3NPhqlgd1KsMfYoYQJ5v4h/d1GLJ//Fyj2gJ0D6EruKs4olqlsUI2/KyBjAacXCzV4Vzn5AqHwlqjWsXH4MDIVOcbzc48+wAeIWnDdFjW7RmV7mArQfVeAGHWFyvYR2+NCUOM/9u3IjhcP2TNuWoFDugXrg6H5m2fwFZAWvCuenpyP2K+iMPKSZzbeUKtmZ4JSA7yTaCz+2+gFfVQroCSJ5reR3X5Op87ZTs9sdFmzFYSzpUqILV0yj10lJmuXrQ=="
    ));
  }
}
