package studio.raptor.ddal.config.parser.shard;

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
import studio.raptor.ddal.config.model.shard.Shard;
import studio.raptor.ddal.config.model.shard.ShardGroups;
import studio.raptor.ddal.config.model.shard.Shards;

/**
 * 分片组配置解析器
 *
 * @author Charley
 * @since 1.0
 */
public class ShardGroupParser {

  private static final XPath XPATH = XPathFactory.newInstance().newXPath();

  public static void parse(ShardConfig shardConfig, Element root) {
    try {
      NodeList shardGroupsDom = (NodeList) XPATH
          .evaluate("shardGroups/shardGroup", root, XPathConstants.NODESET);

      ShardGroups shardGroups = new ShardGroups();
      for (int i = 0; i < shardGroupsDom.getLength(); i++) {
        Node shardGroupDom = shardGroupsDom.item(i);

        ShardGroups.ShardGroup shardGroup = new ShardGroups.ShardGroup();
        shardGroup.setName(XPATH.evaluate("@name", shardGroupDom));
        createShardZones(shardGroup, shardGroupDom);
        shardGroups.addGroup(shardGroup);
      }

      shardConfig.setShardGroups(shardGroups);
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static void createShardZones(ShardGroups.ShardGroup shardGroup, Node shardGroupDom){
    try{
      NodeList shardZonesDom = (NodeList) XPATH.evaluate("shardZone", shardGroupDom, XPathConstants.NODESET);

      Shards shards = new Shards();
      String[] shardZones = new String[shardZonesDom.getLength()];

      if (shardZonesDom.getLength() > 0) {
        for (int i = 0; i < shardZonesDom.getLength(); i++) {
          Node shardZoneDom = shardZonesDom.item(i);

          String shardZone = XPATH.evaluate("@name", shardZoneDom);
          shardZones[i] = shardZone;

          createShard(shards, shardZoneDom, shardZone);
        }

      } else {
        createShard(shards, shardGroupDom, null);
      }

      shardGroup.setShardZones(shardZones);
      shardGroup.setShards(shards);
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static void createShard(Shards shards, Node parentDom, String shardZone){
    try {
      NodeList shardsDom = (NodeList) XPATH.evaluate("shard", parentDom, XPathConstants.NODESET);
      for (int i = 0; i < shardsDom.getLength(); i++) {
        Node shardDom = shardsDom.item(i);

        Shard shard = new Shard();
        shard.setName(XPATH.evaluate("@name", shardDom));
        shard.setDsGroup(XPATH.evaluate("@dsGroup", shardDom));
        shard.setSchema(XPATH.evaluate("@schema", shardDom));
        if (null != shardZone) {
          shard.setShardZone(shardZone);
        }

        shards.add(shard);
      }
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }
}
