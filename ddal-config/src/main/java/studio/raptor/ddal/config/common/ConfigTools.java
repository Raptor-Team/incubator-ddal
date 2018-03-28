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

import com.google.common.io.Files;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.exception.ConfigException;
import studio.raptor.ddal.common.exception.ConfigException.Code;
import studio.raptor.ddal.common.util.Base64;
import studio.raptor.ddal.common.util.RuntimeUtil;
import studio.raptor.ddal.common.util.StringUtil;
import studio.raptor.ddal.config.config.SystemProperties;

public class ConfigTools {

  private static Logger log = LoggerFactory.getLogger(ConfigTools.class);
  private static final String DEFAULT_PRIVATE_KEY_STRING = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMDpGS6K+TCVTltK8jCerGJ/GIHNP0QmLijipGapxHczWeyit3hq/qUpC3I0ARF6BwlQiqJdjEnUYri0qou7x8/5fEg+yM2Jm+8O3cjiIaJU4TzOMq2pjNT84PoQmrIfV5fInWzW0mDRAz5ShfZWmx5ja/B57MzEifBbHjXIJlMO7zglwHNc35Db0rAXmMvUkHRf3HBHl5DJP5Cind17TeuFr/n0chWFdxVlPSAaS+lMtpmWnoZpOsztZUrjGUeB07bqO9MAQns9u/WUAoEaWFIAVvQD8op6leF/INLOig0ijBrynvHuMjfOhOhnn9yYrx/c2y/E5QWWtAGSheyJR1AgMBAAECggEAG3ATNsuc8dpHJoUiNmQP+WgD2l2DbHvA8Y9WWtsx6JF6dLQ4lb7uIaKxNR+TwoV1afjYbLTjZYlGIOu14Z59y6Mq8VXC9sjMPernPmkd5y9dv2VJL7EJdFdpEAjFYyBQ/c7XFhzCgEzY2sHPCflNHovyN7RfeNGgfH7aHaeW6AnGUDh8JA0g9xkPNHOSkfkg4Su5JuF8unZ3PeGg69tsEZrVrEezUaEB2wdpoYJl3ZZekYmyTUBqGKOVSiESSBDjo55/7/3SmVROthMOgFwCFfV++tNEAmkOYrdE6lIDSu3uuV8yfTejzA2xxjnlWwyig+ryiR3tm7hfcGAgE8oLYQKBgQDWmckmgjr6hOU15MBphlihi+vuh2v2a3DpKE2KCghh0h2MXnET9rKtYFqUSCMPjQz4IkDXLcPzo9A0evc2KEqY1iBRAlgtR6udiSzPP81VJOqPHDqN4sa0T5nWQFjW99hPAZqboMUkIj8ZLfgYLy4n1L6POFis0JxyV8Oy1rJlGQKBgQCnE2G/kW+6vpdOCeHumI3KtWIHYVwu/YNjG187mmDDREwFij4t4EmD8RheKCWKAgK6S/JZC7vcfWgIUvAt/Frnn4u0e3Drzu0a2gn5e5uURQUhdNqv0uhg89IeuCw6ZagWLhtMUZ43moEv7w248VJHU+5hoXOfZHYvTo0UoOiZvQKBgGyzQPxMgutaXW/LCttovYQ2h68u0TTkfzDaxUvigRRrhaQQQl7GWwMPB1KMp5W88JDPaOCVweerVk7+6jF4fzCjZYMjGN72qqsFTLk9rBgwts7+kXpFIDk9CMNJnLZmnB6IxXOHH9SJtERg0IYPf+WVEWEiPWhr0pLoeawgrAQhAoGBAJ4CoZaImI+XwwcmJ4v8sVT+GDj8pi82ybTIZVe8WDk0dWVj6gx/K2fic3ZRJ7eO0T+Zphj7FO+hlusQbWBAY0TdykWl3RTXPb0AayCM+4XNhN2wY0aYA/f1gEpXSlTsYXygnGagZi8WK4nG30aCzJry0Od6xxv+6hsxghH5YCJNAoGAUNThyMG94qxZwAM3TPLvZRrs8QswR5HjxvncVAtJHbEygxkQ2SAnpVSGS8Wnqwo+AVrCINXokmGY7DAkjAz35krvZaKNuoANiYLokSftfUlrwkVYdClwRgSg6fkkFlq51xOBToqON4/GMz1ctZMoyy85th8Fai6QvzrUDD+ExVk=";
  public static final String DEFAULT_PUBLIC_KEY_STRING = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjA6RkuivkwlU5bSvIwnqxifxiBzT9EJi4o4qRmqcR3M1nsord4av6lKQtyNAERegcJUIqiXYxJ1GK4tKqLu8fP+XxIPsjNiZvvDt3I4iGiVOE8zjKtqYzU/OD6EJqyH1eXyJ1s1tJg0QM+UoX2VpseY2vweezMxInwWx41yCZTDu84JcBzXN+Q29KwF5jL1JB0X9xwR5eQyT+Qop3de03rha/59HIVhXcVZT0gGkvpTLaZlp6GaTrM7WVK4xlHgdO26jvTAEJ7Pbv1lAKBGlhSAFb0A/KKepXhfyDSzooNIowa8p7x7jI3zoToZ5/cmK8f3NsvxOUFlrQBkoXsiUdQIDAQAB";
  private static String sysDecryptKey;
  private static final Object LOAD_LOCK = new Object();

  public static void main(String[] args) throws Exception {
    String password = args[0];
    System.out.println(encrypt(password));
  }

  /**
   * 加载解密密钥。 如果system.properties中已配置config.decrypt.key.file属性，则
   * 优先尝试读取配置指定的解密密钥文件。当读取指定的密钥文件失败，则尝试从DDAL默认的
   * 密钥文件读取。
   */
  public static void loadSysDecryptKey() {
    if (StringUtil.isEmpty(sysDecryptKey)) {
      synchronized (LOAD_LOCK) {
        if (StringUtil.isEmpty(sysDecryptKey)) {
          String decryptKey = StringUtil.EMPTY;
          String decryptKeyString = SystemProperties.getInstance()
              .get(ConfigConstant.PROP_KEY_CONFIG_DECRYPT_KEY);
          String decryptKeyFile = SystemProperties.getInstance()
              .get(ConfigConstant.PROP_KEY_CONFIG_DECRYPT_KEY_FILE);
          if (!StringUtil.isEmpty(decryptKeyString)) {
            decryptKey = decryptKeyString;
          } else if (!StringUtil.isEmpty(decryptKeyFile)) {

            // read from user decrypt file
            try {
              decryptKey = Files
                  .toString(new File(decryptKeyFile),
                      Charset.forName(ConfigConstant.DEFAULT_CHARSET));
              log.debug("Successfully read decrypt key from {}", decryptKeyFile);
            } catch (IOException ioe) {
              if (log.isErrorEnabled()) {
                log.error(String.format(
                    "Failed to read decrypt key file %s, prepare to read from default decrypt key file.",
                    decryptKeyFile), ioe);
              }
            }
          } else {
            //read from default decrypt file
            decryptKeyFile = String.format("%s/%s/%s", RuntimeUtil.getSysUserHome(),
                ConfigConstant.CONFIG_DEFAULT_DECRYPT_KEY_FILE_DIR,
                ConfigConstant.CONFIG_DEFAULT_DECRYPT_KEY_FILE_NAME);
            try {
              decryptKey = Files
                  .toString(new File(decryptKeyFile),
                      Charset.forName(ConfigConstant.DEFAULT_CHARSET));
            } catch (IOException ioe) {
              if (log.isErrorEnabled()) {
                log.error("Failed to read default decrypt key file", ioe);
              }
              throw ConfigException.create(Code.READ_DECRYPT_KEY_ERROR);
            }
          }
          if (StringUtil.isEmpty(sysDecryptKey = decryptKey.trim())) {
            throw ConfigException.create(Code.READ_DECRYPT_KEY_ERROR);
          }
        }
      }
    }
  }

  public static String getSysDecryptKey() {
    return sysDecryptKey;
  }

  public static String decrypt(String cipherText) throws Exception {
    return decrypt((String) null, cipherText);
  }

  public static String decrypt(String publicKeyText, String cipherText)
      throws Exception {
    PublicKey publicKey = getPublicKey(publicKeyText);
    return decrypt(publicKey, cipherText);
  }

  public static PublicKey getPublicKeyByX509(String x509File) {
    if (x509File == null || x509File.length() == 0) {
      return ConfigTools.getPublicKey(null);
    }

    try(FileInputStream in = new FileInputStream(x509File);) {
      CertificateFactory factory = CertificateFactory
          .getInstance("X.509");
      Certificate cer = factory.generateCertificate(in);
      return cer.getPublicKey();
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to get public key", e);
    }
  }

  public static PublicKey getPublicKey(String publicKeyText) {
    if (publicKeyText == null || publicKeyText.length() == 0) {
      publicKeyText = ConfigTools.DEFAULT_PUBLIC_KEY_STRING;
    }

    try {
      byte[] publicKeyBytes = Base64.base64ToByteArray(publicKeyText);
      X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
          publicKeyBytes);

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return keyFactory.generatePublic(x509KeySpec);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to get public key", e);
    }
  }

  public static PublicKey getPublicKeyByPublicKeyFile(String publicKeyFile) {
    if (publicKeyFile == null || publicKeyFile.length() == 0) {
      return ConfigTools.getPublicKey(null);
    }

    try(FileInputStream in = new FileInputStream(publicKeyFile);) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int len = 0;
      byte[] b = new byte[512 / 8];
      while ((len = in.read(b)) != -1) {
        out.write(b, 0, len);
      }

      byte[] publicKeyBytes = out.toByteArray();
      X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
      KeyFactory factory = KeyFactory.getInstance("RSA");
      return factory.generatePublic(spec);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to get public key", e);
    }
  }

  public static String decrypt(PublicKey publicKey, String cipherText)
      throws Exception {
    Cipher cipher = Cipher.getInstance("RSA");
    try {
      cipher.init(Cipher.DECRYPT_MODE, publicKey);
    } catch (InvalidKeyException e) {
      // 因为 IBM JDK 不支持私钥加密, 公钥解密, 所以要反转公私钥
      // 也就是说对于解密, 可以通过公钥的参数伪造一个私钥对象欺骗 IBM JDK
      RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
      RSAPrivateKeySpec spec = new RSAPrivateKeySpec(rsaPublicKey.getModulus(),
          rsaPublicKey.getPublicExponent());
      Key fakePrivateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);
      cipher = Cipher.getInstance("RSA"); //It is a stateful object. so we need to get new one.
      cipher.init(Cipher.DECRYPT_MODE, fakePrivateKey);
    }

    if (cipherText == null || cipherText.length() == 0) {
      return cipherText;
    }

    byte[] cipherBytes = Base64.base64ToByteArray(cipherText);
    byte[] plainBytes = cipher.doFinal(cipherBytes);

    return new String(plainBytes);
  }

  public static String encrypt(String plainText) throws Exception {
    return encrypt((String) null, plainText);
  }

  public static String encrypt(String key, String plainText) throws Exception {
    if (key == null) {
      key = DEFAULT_PRIVATE_KEY_STRING;
    }

    byte[] keyBytes = Base64.base64ToByteArray(key);
    return encrypt(keyBytes, plainText);
  }

  public static String encrypt(byte[] keyBytes, String plainText)
      throws Exception {
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory factory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = factory.generatePrivate(spec);
    Cipher cipher = Cipher.getInstance("RSA");
    try {
      cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    } catch (InvalidKeyException e) {
      //For IBM JDK, 原因请看解密方法中的说明
      RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
      RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(),
          rsaPrivateKey.getPrivateExponent());
      Key fakePublicKey = KeyFactory.getInstance("RSA").generatePublic(publicKeySpec);
      cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, fakePublicKey);
    }

    byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
    String encryptedString = Base64.byteArrayToBase64(encryptedBytes);

    return encryptedString;
  }

  public static byte[][] genKeyPairBytes(int keySize)
      throws NoSuchAlgorithmException {
    byte[][] keyPairBytes = new byte[2][];

    KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
    gen.initialize(keySize, new SecureRandom());
    KeyPair pair = gen.generateKeyPair();

    keyPairBytes[0] = pair.getPrivate().getEncoded();
    keyPairBytes[1] = pair.getPublic().getEncoded();

    return keyPairBytes;
  }

  public static String[] genKeyPair(int keySize)
      throws NoSuchAlgorithmException {
    byte[][] keyPairBytes = genKeyPairBytes(keySize);
    String[] keyPairs = new String[2];
    keyPairs[0] = Base64.byteArrayToBase64(keyPairBytes[0]);
    keyPairs[1] = Base64.byteArrayToBase64(keyPairBytes[1]);
    return keyPairs;
  }
}
