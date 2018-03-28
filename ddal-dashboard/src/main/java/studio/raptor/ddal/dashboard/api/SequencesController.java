package studio.raptor.ddal.dashboard.api;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studio.raptor.ddal.dashboard.repository.Sequence;
import studio.raptor.ddal.dashboard.service.interfaces.SequenceService;
import studio.raptor.ddal.dashboard.util.ExcelReader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by liujy on 2017/11/27.
 *  raptor-gid : BreadCrumb 全局序列 统一管理器
 */
@RestController
public class SequencesController {

    private static Logger log = LoggerFactory.getLogger( SequencesController.class);
    private SequenceService sequenceService;

    @Autowired
    public SequencesController(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * 获取zk下所有的序列命名空间，即所有使用序列的应用中心
     * @return
     */
    @RequestMapping(value = "/gid/centers", method = RequestMethod.GET)
    public List<String>  getCenters() {
        try{
            List<String> sequencesList=sequenceService.getCenters();
            return sequencesList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定中心namespace下的全部序列
     * @param namespace 命名空间
     * @return 序列列表
     */
    @RequestMapping(value = "/gid/{namespace}/list", method = RequestMethod.GET)
    public List<Sequence> getSequencesByNamespace(@PathVariable String namespace) {
        try{
            List<Sequence> sequencesList= sequenceService.getCenterSequences( namespace );
            return sequencesList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重置指定序列
     * @param namespace
     * @param name
     * @param value
     * @return
     */
    @RequestMapping(value = "/gid/reset/{namespace}&{name}&{value}", method = RequestMethod.POST)
    public boolean reset(@PathVariable String namespace,@PathVariable String name,@PathVariable Long value) {
        try{
            Sequence sequence=new Sequence( name,value );
            boolean result= sequenceService.reset( namespace,sequence );
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 重置指定序列
     * @param namespace
     * @param sequence
     * @return
     */
    @RequestMapping(value = "/gid/{namespace}/reset", method = RequestMethod.POST)
    public boolean reset(@PathVariable String namespace,@RequestBody Sequence sequence) {
        try{
            boolean result= sequenceService.reset( namespace,sequence );
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 重置指定序列列表
     * @param namespace
     * @param sequenceList
     * @return
     */
    @RequestMapping(value = "/gid/{namespace}/resetAll", method = RequestMethod.POST)
    public Boolean[] resetAll(@PathVariable String namespace,@RequestBody List<Sequence> sequenceList) {
        try{
            Boolean[] result= sequenceService.resetAll( namespace,sequenceList );
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改zookeeper地址,并建立客户端
     * @param zkAddress zookeeper连接串地址,多个地址 , 隔开:192.0.0.0:2181,192.0.0.1:2181,192.0.0.2:2181
     * @return
     */
    @RequestMapping(value = "/gid/zookeeper/modify", method = RequestMethod.GET)
    public String modifyZkAddress(@RequestParam(value = "zkAddress", required = true) String zkAddress) {
        if(zkAddress==null) return "zookeeper连接串地址不能为空！";
        Pattern pattern  =   Pattern.compile("^(\\d+\\.){3}\\d+:\\d+(,(\\d+\\.){3}\\d+:\\d+)*$");
        Matcher matcher   =   pattern.matcher(zkAddress.trim());
        boolean  flag   =   matcher.matches();
        if(! flag) return "zookeeper连接串地址非法！";
        boolean resut= sequenceService.modifyZkAddress( zkAddress.trim() );
        if(resut) return "修改成功！";
        else return "修改失败！";
    }

    /**
     * 停止zookeeper链接
     * @return
     */
    @RequestMapping(value = "/gid/zookeeper/stop", method = RequestMethod.GET)
    public String stopZookeeper() {
        boolean resut= sequenceService.stopZookeeper( );
        if(resut) return "zookeeper链接已断开！";
        else return "zookeeper链接断开失败！";
    }

    /**
     * 获取zookeeper链接信息
     * @return
     */
    @RequestMapping(value = "/gid/zookeeper/info", method = RequestMethod.GET)
    public Map<String,String> getZkInfo() {
        Map<String,String> zkInfoMap= sequenceService.getZkInfo();
        return zkInfoMap;
    }

    /**
     * 重置指定序列列表，读取excel文件中序列列表
     * @param fileName
     * @return
     */
    @RequestMapping(value = "/gid/resetAll", method = RequestMethod.POST)
    public String resetAll(@RequestParam("fileName") MultipartFile fileName){
        FileInputStream fis=null;
        try{
            fis=(FileInputStream)fileName.getInputStream();
            List<Sequence> sequenceList=ExcelReader.readSequencesData(fis);
           String msg= sequenceService.resetAll(sequenceList);
           return msg;
        }catch (Exception e){
            log.error("Failed to parse excel file",e);
            return "{\"respMsg\":\""+e.getMessage()+"\"}";
        }finally{
            if(fis!=null)
                try{
                    fis.close();
                }catch(IOException e){
                    log.error("Failed to close filestream",e);
                }
        }
    }

    /**
     * description:下载序列模板
     * @param response
     */
    @RequestMapping(value="/gid/download",method=RequestMethod.POST)
    public void downloadSequenceTemplate(HttpServletResponse response){
        InputStream bis=null;
        ServletOutputStream out=null;
        byte[] buff = new byte[1024];
        int bytes;
        try{
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=sequencetemplate.xlsx");
            out=response.getOutputStream();
            bis=this.getClass().getResourceAsStream("/sequencetemplate.xlsx");
            while(-1!=(bytes=bis.read(buff,0,buff.length))){
                out.write(buff,0,bytes);
            }
            out.flush();
        }catch (IOException e){
            log.error("Failed to download sequence template ",e);
        }finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(bis!=null){
                    bis.close();
                }
            }catch(IOException e){
                log.error("Failed to close filestream ",e);
            }
        }
    }
}
