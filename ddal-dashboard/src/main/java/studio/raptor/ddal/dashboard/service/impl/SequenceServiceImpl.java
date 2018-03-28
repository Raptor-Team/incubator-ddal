package studio.raptor.ddal.dashboard.service.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.dashboard.curator.CuratorUtil;
import studio.raptor.ddal.dashboard.repository.Sequence;
import studio.raptor.ddal.dashboard.service.interfaces.SequenceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liujy on 2017/11/27.
 * raptor-gid : BreadCrumb序列全局展示与修改服务
 */
public class SequenceServiceImpl implements SequenceService {

    private static Logger log = LoggerFactory.getLogger(SequenceServiceImpl.class);
    private String zkConnectString;
    private int sessionTimeout =10000;  //会话超时(毫秒)
    private int connectionTimeout = 10000;   //连接超时(毫秒)
    private long startTime;//创建链接时的时间
    private long duration=0;//单位：秒
    private CuratorFramework client=null;

    public SequenceServiceImpl(String zkConnectString,int sessionTimeout,int connectionTimeout){
        this.zkConnectString=zkConnectString;
        this.sessionTimeout=sessionTimeout;
        this.connectionTimeout=connectionTimeout;
        //初始化zoo keeper操作工具类
        this.client=CuratorUtil.newClient( this.zkConnectString, this.sessionTimeout, this.connectionTimeout );
        this.startTime=System.currentTimeMillis();
        this.duration=0;
    }

    @Override
    public List<String> getCenters() throws Exception{
        if(this.client!=null){
            return CuratorUtil.getChildPath("/");
        }
        return null;
    }

    @Override
    public List<Sequence> getCenterSequences(String namespace) throws Exception{
        if(this.client!=null){
            return CuratorUtil.getChildPath("/"+namespace+"/raptor-sequences");
        }
        return null;
    }

    @Override
    public Sequence getSequence(String namespace, String sequenceName) throws Exception {
        if(this.client!=null){
            List<Sequence> sequencesList=CuratorUtil.getChildPath("/"+namespace+"/raptor-sequences/"+sequenceName);
            if(sequencesList!=null && sequencesList.size()>0){
                return sequencesList.get( 0 );
            }
        }
        return null;
    }

    @Override
    public boolean reset(String namespace, Sequence sequence) throws Exception {
        try{
            boolean result=CuratorUtil.setSequenceValue( "/"+namespace+"/raptor-sequences/"+sequence.getName(),sequence.getValue().toString());
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean[] resetAll(String namespace, List<Sequence> sequenceList) throws Exception {
        if(sequenceList==null || sequenceList.size()<0) return null;
        Boolean[] result=new Boolean[sequenceList.size()];
        int i=0;
        for (Sequence sequence:sequenceList) {
            boolean re=CuratorUtil.setSequenceValue( "/"+namespace+"/raptor-sequences/"+sequence.getName(), sequence.getValue().toString());
            result[ i++]=re;
        }
        return result;
    }

    @Override
    public String resetAll(List<Sequence> sequenceList) throws Exception{
        if(sequenceList==null || sequenceList.size()<0){
            return "{\"respMsg\":\"重置列表为空，请核查。\"}";
        }
        String respMsg="";
        int i=0;
        List<String> centers=new ArrayList(  );
        List<String> seqNames=new ArrayList(  );
        boolean flag=false;
        String namespace;
        String sequenceName;
        for (Sequence sequence:sequenceList) {
            namespace=sequence.getCenterName();
            if(! centers.contains( namespace )){
                flag=CuratorUtil.checkExists( this.client,"/"+ namespace);
                if(! flag){
                    throw new Exception( "表单中centerName: "+namespace+" 不存在，请确认或从表单中删除。" );
                }else{
                    centers.add( namespace );
                }
            }
            sequenceName=sequence.getName();
            if(! seqNames.contains( sequenceName )){
                flag=CuratorUtil.checkExists( this.client,"/"+ namespace+"/raptor-sequences/"+sequenceName);
                if(!flag){
                    throw new Exception( "表单中 /"+namespace+"/raptor-sequences/下"+sequenceName+" 序列不存在，请确认或从表单中删除。" );
                }else{
                    seqNames.add( sequenceName );
                }
            }
            boolean re=CuratorUtil.setSequenceValue( "/"+namespace+"/raptor-sequences/"+sequenceName, sequence.getValue().toString());
            if(!re){
                if("".equals( respMsg )){
                    respMsg += "\""+namespace+"-"+sequenceName+"\":\""+sequence.getPosition()+"修改失败\"";
                }
                else{
                    respMsg += ",\""+namespace+"-"+sequenceName+"\":\""+sequence.getPosition()+"修改失败\"";
                }
            }
        }
        if("".equals( respMsg )){
            respMsg="{\"respMsg\":\"批量重置成功。\"}";
        }
        else{
            respMsg="{\"respMsg\":{"+ respMsg +"}}";
        }
        return respMsg;
    }

    @Override
    public boolean modifyZkAddress(String zkAddress){
        CuratorUtil.destory();
        this.client=null;
        this.zkConnectString=zkAddress;
        this.client=CuratorUtil.newClient( zkAddress,this.sessionTimeout, this.connectionTimeout );
        this.startTime=System.currentTimeMillis();
        this.duration=0;
        if(null !=this.client ) return true;
        return false;
    }

    @Override
    public boolean stopZookeeper(){
        CuratorUtil.destory();
        this.client=null;
        this.startTime=System.currentTimeMillis();
        this.duration=0;
        return true;
    }

    @Override
    public Map<String,String> getZkInfo(){
        Map<String,String> zkInfoMap=new HashMap( );
        zkInfoMap.put( "zkConnectString",this.zkConnectString );
        zkInfoMap.put( "startTime",String.valueOf(this.startTime) );
        String state="注销";
        if(this.client !=null){
            if (client.getState() == CuratorFrameworkState.STARTED) {
                state="连接";
            }
            else  if (client.getState() == CuratorFrameworkState.STOPPED) {
                state="停止";
            }
            else  if (client.getState() == CuratorFrameworkState.LATENT) {
                state="休眠";
            }
        }
        zkInfoMap.put( "state",state );
        this.duration=(System.currentTimeMillis()-this.startTime)/1000;
        zkInfoMap.put( "duration",String.valueOf(this.duration) );
        return zkInfoMap;
    }
}
