package studio.raptor.ddal.core.engine;

import java.util.Collection;

/**
 * 执行计划MBean接口
 *
 * @author Charley
 * @since 1.0
 */
public interface MemoryObjectsBasedOnSQLMBean {

  Collection<String> getSqlList();

  SqlMetaData getSqlByFingerprint(String fingerprint);

  String getSqlByOriginSql(String originSql);

  SqlMetaData getSqlByReplaceSql(String replaceSql);

  boolean replaceSql(String fingerprint, String newSql);

  void cancelReplace(String fingerprint);
}
