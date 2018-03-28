package studio.raptor.ddal.dashboard.curator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.KeeperException.NoAuthException;
import studio.raptor.ddal.dashboard.repository.Sequence;


/**
 * 只返回带全局序列的中心
 * Created by Administrator on 2017/11/27.
 */
public class CuratorUtil {
    private static Logger log = LoggerFactory.getLogger(CuratorUtil.class);
    public static final String SEQUENCE_NODE = "raptor-sequences";
    private static CuratorFramework client=null;

    /*static{
        try{
            Properties properties = new Properties();
            properties.load(CuratorUtil.class.getResourceAsStream("/system.properties"));
            url = (String) properties.get("sequence.zk.address");
            sessionTimeout = Integer.parseInt((String)properties.get("sequence.zk.session_timeout"));
            connectionTimeout = Integer.parseInt((String)properties.get("sequence.zk.connection_timeout"));
        } catch (IOException ioe) {
            log.error("load system.properties failed.", ioe);
        }
    }*/

    /**
     * 创建zookeeper客户端
     *
     * @return zk客户端对象
     */
    public static CuratorFramework newClient(String connectString,int sessionTimeout,int connectionTimeout) {
        try {
            if(client == null) {
                client=newClient(connectString, sessionTimeout, connectionTimeout, null);
            }
            return client;
        }catch(Exception e){
            log.error("zk config error",e.getMessage());
            return null;
        }
    }


    /**
     * 创建zookeeper客户端
     *
     * @param connectString 连接串 ip1:port1,ip2:port2......
     * @param sessionTimeout 会话超时时间（毫秒）
     * @param connectTimeout 连接超时时间（毫秒）
     * @param listener 监听器
     * @return zk客户端对象
     */
    private static CuratorFramework newClient(String connectString, int sessionTimeout,
                                             int connectTimeout,ConnectionStateListener listener) {
        client = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectTimeout)
                .retryPolicy(new RetryNTimes(3, 1000))
                .build();

        if (client.getState() == CuratorFrameworkState.LATENT) {
            client.start();
        }

        if (null != listener) {
            client.getConnectionStateListenable().addListener(listener);
        }

        return client;
    }

    /**
     * 销毁zookeeper客户端
     *
     *
     */
    public static void destory() {
        if (null != client) {
            CloseableUtils.closeQuietly(client);
            client=null;
        } else {
            log.warn("zookeeper zkClient is null");
        }
    }

    /**
     * 检查路径是否存在
     *
     * @param client zk客户端
     * @param path 路径
     * @return 存在返回true,反之返回false
     * @throws Exception zk errors
     */
    public static boolean checkExists(CuratorFramework client, String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        if (null != stat) {// path exists
            return true;
        } else {
            return false;
        }
    }

    public static List getSingleSeqVal(String path) throws Exception{
        List<Sequence> sequencesList=new ArrayList<Sequence>(  );
        if(client!=null) {
            DistributedAtomicLong seq = new DistributedAtomicLong(client, path, new RetryNTimes(3, 200));
            AtomicValue<Long> value = seq.get();
            if (value.succeeded()) {
                long preval = value.preValue();
                Sequence sequence=new Sequence();
                sequence.setId( preval );
                String nodeName=path.substring( path.lastIndexOf( "/" )+ 1 );
                sequence.setName(nodeName );
                sequence.setPath( path.substring(0,path.lastIndexOf( "/" )));
                sequencesList.add( sequence );
            }
        }
        return sequencesList;
    }


    public static List getValueOfSeq(String path,List nodes) throws Exception{
        List<Sequence> sequencesList=new ArrayList<Sequence>(  );
        if(client!=null && nodes!=null && nodes.size()>0){
            for(String node:(List<String>)nodes){
                DistributedAtomicLong seq = new DistributedAtomicLong(client, path+"/"+node,  new RetryNTimes(3, 200));
                AtomicValue<Long> value = seq.get();
                if(value.succeeded()){
                    //如果操作成功， preValue()代表操作前的值， postValue()代表操作后的值。
                    Sequence sequence=new Sequence();
                    long preval=value.preValue();
                    sequence.setId( preval );
                    sequence.setName(node  );
                    sequence.setPath( path );
                    sequencesList.add( sequence );
                }
            }
        }
        return sequencesList;
    }

    public static void main(String[] args){
        try {
            String url = "192.168.199.24:2181";
            int sessionTimeout =10000;  //会话超时(毫秒)
            int connectionTimeout = 10000;   //连接超时(毫秒)
            client=newClient(url,sessionTimeout,connectionTimeout);
            //List list = (List) getChildPath(client,"/test");
            setSequenceValue("/crm/raptor-sequences/TEST_1","12320");
        }catch (Exception e){
            log.error("get zookeeper data failed",e.getMessage());
        }finally{
            if(client!=null){
                CuratorUtil.destory();
            }
        }
    }

    public static List getChildPath(String path) throws Exception{
        if(!path.startsWith("/") || (path.endsWith("/") && !path.equals("/"))){
            log.info("path is invalid: path="+path);
            return null;
        }
        List nodes=null;
        if(client!=null) {
            if(path.equals("/")){
                return getAllSequenceRootNodes();
            }

                int count=substrOccured(path,"/");
                switch (count) {
                    case 1:
                        try {
                            List allChildren = client.getChildren().forPath(path);
                            for (String node : (List<String>) allChildren) {
                                if (node.equals(SEQUENCE_NODE)) {
                                    nodes.add(node);
                                    break;
                                }
                            }
                        }catch(NoAuthException e){
                            log.error("auth node traverse failed,node name="+path,e.getMessage());
                        }
                        break;
                    case 2:
                        List seqNodes = client.getChildren().forPath(path);
                        nodes = getValueOfSeq(path,seqNodes);
                        break;
                    case 3:
                        nodes = getSingleSeqVal(path);
                        break;
                    default:
                }
        }
        return nodes;
    }

    public  static int substrOccured(String str,String substr)
    {
        int index=0;
        int count=0;
        int fromindex=0;
        while((index=str.indexOf(substr,fromindex))!=-1) {
            fromindex=index+substr.length();
            count++;
        }
        return count;
    }

    public  static List<String> getAllSequenceRootNodes() throws Exception{
        List<String> oneNodes=null;
        List<String> twoNodes=null;
        List<String> sequenceNodes=new ArrayList();
        if(client!=null) {
            oneNodes = client.getChildren().forPath("/");
            if(oneNodes!=null && oneNodes.size()>0){
                for(String node1:oneNodes){
                    try {
                        twoNodes = client.getChildren().forPath("/" + node1);
                        if (twoNodes != null && twoNodes.size() > 0) {
                            for (String node2 : twoNodes) {
                                if (node2.equals(SEQUENCE_NODE)) {
                                    sequenceNodes.add(node1);
                                    break;
                                }
                            }
                        }
                    }catch(NoAuthException e){
                        log.error("auth node traverse failed,node name=/"+node1,e.getMessage());
                    }
                }
            }
        }
        return sequenceNodes;
    }

    /**
     * description:设置序列值
     * @param path
     * @param value
     * @return
     * @throws Exception
     */
    public static boolean setSequenceValue(String path,String value) throws Exception{
        if(!path.startsWith("/") || (path.endsWith("/") && !path.equals("/"))){
            log.info("path is invalid: path="+path);
            return false;
        }
        if(client!=null){
            try{
                DistributedAtomicLong seq = new DistributedAtomicLong(client, path,  new RetryNTimes(3, 200));
                AtomicValue<Long> newValue = seq.trySet(Long.valueOf(value,10));
                if(newValue.succeeded()){
                    //如果操作成功， preValue()代表操作前的值， postValue()代表操作后的值。
                    long preval=newValue.preValue();
                    long postval=newValue.postValue();
                    log.info("Successfully modify sequence value: path="+path+", preValue()="+preval+", postValue()="+postval);
                    return true;
                }
            }catch(Exception e){
                log.error("Failed to modify sequence value: path="+path+", value="+value,e.getMessage());
            }
        }
        return false;
    }

    /**
     * 在指定路径上设置数据
     *
     * @param client zk客户端
     * @param path 路径
     * @param data 数据
     * @throws Exception zk errors
     */
    public static void setData(CuratorFramework client, String path, byte[] data) throws Exception {
        client.setData().forPath(path, data);
    }

    /**
     * 获取指定路径上的数据
     *
     * @param client zk客户端
     * @param path 路径
     * @return 路径上的数据
     * @throws Exception zk errors
     */
    public static byte[] getData(CuratorFramework client, String path) throws Exception {

        return client.getData().forPath(path);
    }

    /**
     * 获取目录子节点列表
     *
     * @param client zk客户端
     * @param path 路径
     * @param isFullPath 是否输出完整路径
     * @return 子节点列表
     * @throws Exception zk errors
     */

    public static List<String> getChildren(CuratorFramework client, String path, boolean isFullPath)
            throws Exception {
        if (isFullPath) {
            List<String> paths = client.getChildren().forPath(path);
            List<String> fullPaths = new LinkedList<String>();
            for (String children : paths) {
                children = ZKPaths.makePath(ZKPaths.makePath(client.getNamespace(), path), children);
                fullPaths.add(children);
            }
            return fullPaths;
        }

        return client.getChildren().forPath(path);
    }
}
