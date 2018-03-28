package studio.raptor.ddal.config.parser.shard;

import com.google.common.base.Strings;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import studio.raptor.ddal.common.exception.ConfigException;
import studio.raptor.ddal.common.exception.ConfigException.Code;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.model.shard.PhysicalDBCluster;
import studio.raptor.ddal.config.model.shard.PhysicalDBClusters;

/**
 *
 *
 * @author Charley
 * @since 1.0
 */
public class PhysicalDbParser {
  private static final XPath XPATH = XPathFactory.newInstance().newXPath();

  public static void parse(ShardConfig shardConfig, Element root){
    try{
      NodeList physicalDBClustersDom = (NodeList) XPATH
          .evaluate("physicalDBClusters/physicalDBCluster", root, XPathConstants.NODESET);

      PhysicalDBClusters physicalDBClusters = new PhysicalDBClusters();
      for (int i = 0; i < physicalDBClustersDom.getLength(); i++) {
        Node physicalDBClusterDom = physicalDBClustersDom.item(i);

        PhysicalDBCluster physicalDBCluster = new PhysicalDBCluster();
        physicalDBCluster.setName(XPATH.evaluate("@name", physicalDBClusterDom));
        physicalDBCluster.setType(XPATH.evaluate("@type", physicalDBClusterDom));

        NodeList dbInstancesDom = (NodeList) XPATH
            .evaluate("dbInstance", physicalDBClusterDom, XPathConstants.NODESET);
        for (int j = 0; j < dbInstancesDom.getLength(); j++) {
          Node dbInstanceDom = dbInstancesDom.item(j);

          PhysicalDBCluster.DBInstance db = new PhysicalDBCluster.DBInstance();
          db.setName(XPATH.evaluate("@name", dbInstanceDom));
          db.setRw(XPATH.evaluate("@rw", dbInstanceDom));
          db.setStatus(XPATH.evaluate("@status", dbInstanceDom));
          String role = XPATH.evaluate("@role", dbInstanceDom);
          if (!Strings.isNullOrEmpty(role)) {
            db.setRole(role);
          }

          String url = XPATH.evaluate("@url", dbInstanceDom);
          if(!Strings.isNullOrEmpty(url)){
            db.setUrl(url);
          }else{
            db.setHostname(XPATH.evaluate("@hostname", dbInstanceDom));
            db.setPort(Integer.parseInt(XPATH.evaluate("@port", dbInstanceDom)));
            db.setH2db(XPATH.evaluate("@h2db", dbInstanceDom));
            db.setH2dir(XPATH.evaluate("@h2dir", dbInstanceDom));
            String sid = XPATH.evaluate("@sid", dbInstanceDom);
            if (!Strings.isNullOrEmpty(sid)) {
              db.setSid(sid);
            }

            String serviceName = XPATH.evaluate("@serviceName", dbInstanceDom);
            if (!Strings.isNullOrEmpty(serviceName)) {
              db.setServiceName(serviceName);
            }
          }
          physicalDBCluster.add(db);
        }
        physicalDBClusters.add(physicalDBCluster);
      }
      shardConfig.setPhysicalDBClusters(physicalDBClusters);
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }
}
