package studio.raptor.ddal.dashboard.service.interfaces;

import studio.raptor.ddal.dashboard.repository.Sequence;
import java.util.List;
import java.util.Map;

/**
 * Created by liujy on 2017/11/27.
 * raptor-gid : BreadCrumb序列全局展示与修改服务
 */
public interface SequenceService {
  /**
   * 获取所有中心序列命名空间
   * @return
   */
  List<String> getCenters() throws Exception;

  /**
   * 获取指定中心namespace下的全部序列
   * @param namespace
   * @return
   */
  List<Sequence> getCenterSequences(String namespace) throws Exception;

  /**
   * 获取具体的序列信息
   * @param namespace
   * @param sequenceName
   * @return
   */
  Sequence getSequence(String namespace,String sequenceName) throws Exception;

  /**
   * 修改给定的序列
   * @param namespace
   * @param sequence
   * @return
   */
  boolean reset(String namespace, Sequence sequence) throws Exception;

  /**
   * 修改给定的序列列表
   * @param namespace
   * @param sequenceList
   * @return
   */
  Boolean[] resetAll(String namespace,List<Sequence> sequenceList) throws Exception;

  /**
   * 修改给定的序列列表，读取excel文件中序列列表
   * @param sequenceList
   * @return
   */
  String resetAll(List<Sequence> sequenceList) throws Exception;

  /**
   * 修改zookeeper地址
   * @param zkAddress zookeeper连接串地址
   * @return
   */
  boolean modifyZkAddress(String zkAddress);

  /**
   * 停止zookeeper链接
   * @return
   */
  boolean stopZookeeper();

  /**
   * 获取当前链接的Zk信息
   * key: zkConnectString 连接串,
   * startTime 链接开始时间,
   * state 链接状态:注销,连接,停止,休眠
   * duration 链接时长,单位：秒
   * @return
   */
  Map<String,String> getZkInfo();

}
