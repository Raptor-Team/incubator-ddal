package studio.raptor.ddal.core.sequence;

import studio.raptor.ddal.common.exception.SequenceException;

/**
 * ID生成器接口类
 *
 * @author Charley
 * @since 1.0
 */
public interface IdGenerator {

  /**
   * 根据序列名称获取下一个ID
   *
   * @param sequenceName
   * @return
   */
  Long nextId(String sequenceName) throws SequenceException;
}
