package studio.raptor.ddal.core.sequence;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import studio.raptor.ddal.common.exception.SequenceException;
import studio.raptor.ddal.common.exception.SequenceException.Code;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.config.SystemProperties;
import studio.raptor.ddal.config.model.shard.Sequences;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.config.model.shard.VirtualDbs;
import studio.raptor.gid.Sequencer;
import studio.raptor.gid.def.SequenceDef;

/**
 * 默认序列生成器
 *
 * @author Charley
 * @since 1.0
 */
public class DefaultIdGenerator implements IdGenerator{

  public static DefaultIdGenerator getInstance(){
    return DefaultIdGeneratorHolder.INSTANCE;
  }

  private static class DefaultIdGeneratorHolder {
    private static final DefaultIdGenerator INSTANCE = new DefaultIdGenerator();
  }

  private static final String EMPTY_SYSTEM_ID = "";
  private Sequencer sequenceServer;

  private DefaultIdGenerator(){
    init();
  }

  private void init(){
    createSequencer();
    loadSequences();
  }

  private void createSequencer() {
    try {
      SystemProperties systemProperties = SystemProperties.getInstance();
      if (!systemProperties.isLoaded()) {
        throw SequenceException
            .create(Code.CREATE_SEQUENCE_SERVER_ERROR, "The system.properties file not found!");
      }
      String zkAddress = checkAndGetArgument(systemProperties, "sequence.zk.address");
      String namespace = checkAndGetArgument(systemProperties, "sequence.zk.namespace");
      //可选配置，若仅使用Breadcrumb是可不指定，值为空字符串；若使用Snowflake，Ticktock时必须指定此值
      String sysId = systemProperties.get("sequence.sysId");
      if(Strings.isNullOrEmpty(sysId)){
        sysId = EMPTY_SYSTEM_ID;
      }
      sequenceServer = new Sequencer(zkAddress, namespace, sysId);
      sequenceServer.startupWithoutLoad();
    }catch (Exception e){
      e.printStackTrace();
      throw SequenceException.create(Code.CREATE_SEQUENCE_SERVER_ERROR, e.getMessage());
    }
  }

  private void loadSequences(){
    try {
      VirtualDbs virtualDbs = ShardConfig.getInstance().getVirtualDbs();
      for (VirtualDb virtualDb : virtualDbs) {
        Sequences sequences = virtualDb.getSequences();
        for (Object def : sequences) {
          sequenceServer.add((SequenceDef) def);
        }
      }
    } catch (Exception e) {
      throw SequenceException.create(Code.LOAD_SEQUENCE_ERROR, e.getMessage());
    }
  }

  private String checkAndGetArgument(SystemProperties properties, String key){
    String value = properties.get(key);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "The argument of %s can not been null!", key);
    return value;
  }

  /**
   * 根据序列名称获取下一个ID
   */
  @Override
  public Long nextId(String sequenceName) throws SequenceException{
    try{
      return sequenceServer.get(sequenceName).nextId();
    }catch (Exception e){
      throw SequenceException.create(Code.FETCH_SEQUENCE_ERROR, e.getMessage());
    }
  }
}
