package studio.raptor.ddal.config.parser.shard;

import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.Map;
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
import studio.raptor.ddal.config.model.shard.DataSource;
import studio.raptor.ddal.config.model.shard.DataSourceGroup;
import studio.raptor.ddal.config.model.shard.DataSourceGroups;

/**
 * 数据源配置解析器
 *
 * @author Charley
 * @since 1.0
 */
public class DataSourceParser {

  private static final Integer DATASOURCE_DEFAULT_SEQ = -1;
  private static final XPath XPATH = XPathFactory.newInstance().newXPath();

  public static void parse(ShardConfig shardConfig, Element root) {
    try {
      NodeList dataSourcesDom = (NodeList) XPATH
          .evaluate("dataSources/group", root, XPathConstants.NODESET);
      DataSourceGroups dataSources = new DataSourceGroups();
      for (int i = 0; i < dataSourcesDom.getLength(); i++) {
        Node groupDom = dataSourcesDom.item(i);

        DataSourceGroup group = new DataSourceGroup();
        group.setName(XPATH.evaluate("@name", groupDom));
        group.setRelaCluster(XPATH.evaluate("@relaCluster", groupDom));

        String balance = XPATH.evaluate("@balance", groupDom);
        if (!Strings.isNullOrEmpty(balance)) {
          group.setBalance(balance);
        }

        DataSource[] dsArr = createDataSourceArr(groupDom);
        group.setDataSources(dsArr);
        dataSources.add(group);
      }
      shardConfig.setDataSourceGroups(dataSources);
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static DataSource[] createDataSourceArr(Node groupDom) {
    try {
      NodeList dataSourcesDom = (NodeList) XPATH.evaluate("dataSource", groupDom, XPathConstants.NODESET);
      DataSource[] dsArr = new DataSource[dataSourcesDom.getLength()];

      for (int i = 0; i < dataSourcesDom.getLength(); i++) {
        Node dataSourceDom = dataSourcesDom.item(i);
        DataSource ds = new DataSource();

        ds.setUser(XPATH.evaluate("@user", dataSourceDom));
        ds.setPwd(XPATH.evaluate("@pwd", dataSourceDom));
        ds.setDbInstName(XPATH.evaluate("@dbInstName", dataSourceDom));
        ds.setAccessLevel(XPATH.evaluate("@accessLevel", dataSourceDom));
        ds.setDbDriver(XPATH.evaluate("@dbDriver", dataSourceDom));
        ds.setJndiName(XPATH.evaluate("@jndiName", dataSourceDom));
        Double seq = (Double) XPATH.evaluate("@seq", dataSourceDom, XPathConstants.NUMBER);
        ds.setSeq(Double.isNaN(seq) ? DATASOURCE_DEFAULT_SEQ : seq.intValue());
        Map<String, String> params = createParams(dataSourceDom);
        ds.setParams(params);
        dsArr[i] = ds;
      }
      return dsArr;
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static Map<String, String> createParams(Node dataSourceDom) {
    try {
      NodeList propertiesDom = (NodeList) XPATH.evaluate("params/property", dataSourceDom, XPathConstants.NODESET);
      Map<String, String> map = new HashMap<>();
      for (int i = 0; i < propertiesDom.getLength(); i++) {
        Node propertyDom = propertiesDom.item(i);
        map.put(XPATH.evaluate("@name", propertyDom), XPATH.evaluate("@value", propertyDom));
      }
      return map;
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }
}
