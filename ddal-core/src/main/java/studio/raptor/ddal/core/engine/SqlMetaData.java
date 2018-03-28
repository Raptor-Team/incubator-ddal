package studio.raptor.ddal.core.engine;

import java.io.Serializable;

/**
 * Sql元信息
 *
 * @author Charley
 * @since 1.0
 */
public class SqlMetaData implements Serializable {
  boolean isReplaced = false;
  String originFingerprint;
  String originSql;
  String replaceSql;

  public SqlMetaData(String originSql) {
    this.originSql = originSql;
  }
  public boolean isReplaced() {
    return isReplaced;
  }

  public void setReplaced(boolean replaced) {
    isReplaced = replaced;
  }

  public String getOriginFingerprint() {
    return originFingerprint;
  }

  public void setOriginFingerprint(String originFingerprint) {
    this.originFingerprint = originFingerprint;
  }

  public String getOriginSql() {
    return originSql;
  }

  public void setOriginSql(String originSql) {
    this.originSql = originSql;
  }

  public String getReplaceSql() {
    return replaceSql;
  }

  public void setReplaceSql(String replaceSql) {
    this.replaceSql = replaceSql;
  }

  @Override
  public String toString() {
    return "SqlMetaData{" +
        "isReplaced=" + isReplaced +
        ", originFingerprint='" + originFingerprint + '\'' +
        ", originSql='" + originSql + '\'' +
        ", replaceSql='" + replaceSql + '\'' +
        '}';
  }
}
