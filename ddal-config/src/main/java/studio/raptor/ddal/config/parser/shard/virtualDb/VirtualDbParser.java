package studio.raptor.ddal.config.parser.shard.virtualDb;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import studio.raptor.ddal.common.util.SplitUtil;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.model.rule.ShardRule;
import studio.raptor.ddal.config.model.rule.ShardRules;
import studio.raptor.ddal.config.model.shard.Index;
import studio.raptor.ddal.config.model.shard.Sequences;
import studio.raptor.ddal.config.model.shard.ShardGroups;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.config.model.shard.Tables;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.config.model.shard.VirtualDbs;
import studio.raptor.ddal.config.model.shard.Wildcard;
import studio.raptor.gid.common.Type;
import studio.raptor.gid.def.DefaultBreadcrumbDef;
import studio.raptor.gid.def.DefaultSnowflakeDef;
import studio.raptor.gid.def.DefaultTicktockDef;
import studio.raptor.gid.def.SequenceDef;

/**
 * 虚拟数据库配置解析器
 *
 * @author Charley
 * @since 1.0
 */
public class VirtualDbParser {

  private static final XPath XPATH = XPathFactory.newInstance().newXPath();

  public static void parse(ShardConfig shardConfig, Element root) {
    try {
      NodeList virtualDBsDom = (NodeList) XPATH
          .evaluate("virtualDBs/virtualDB", root, XPathConstants.NODESET);

      VirtualDbs virtualDbs = new VirtualDbs();
      for (int i = 0; i < virtualDBsDom.getLength(); i++) {
        Node virtualDBDom = virtualDBsDom.item(i);

        VirtualDb virtualDb = new VirtualDb();
        virtualDb.setName(XPATH.evaluate("@name", virtualDBDom));
        virtualDb.setRmOwner("true".equalsIgnoreCase(XPATH.evaluate("@rmOwner", virtualDBDom)));
        virtualDb.setSqlMaxLimit(Integer.parseInt(XPATH.evaluate("@sqlMaxLimit", virtualDBDom)));
        virtualDb.setShardGroup(XPATH.evaluate("@shardGroup", virtualDBDom));
        ShardGroups.ShardGroup shardGroup = shardConfig.getShardGroups()
            .get(virtualDb.getShardGroup());
        virtualDb.setShards(shardGroup.getShards());

        Sequences sequences = new Sequences();
        parseSequences(virtualDBDom, sequences);
        parseTables(virtualDb, virtualDBDom, shardConfig, sequences);
        virtualDb.setSequences(sequences);
        virtualDbs.add(virtualDb);
      }
      shardConfig.setVirtualDbs(virtualDbs);
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static void parseSequences(Node virtualDBDom, Sequences sequences) {
    try {
      NodeList sequencesDom = (NodeList) XPATH
          .evaluate("sequence", virtualDBDom, XPathConstants.NODESET);
      for (int i = 0; i < sequencesDom.getLength(); i++) {
        Node sequenceDom = sequencesDom.item(i);
        NodeList namesDom = (NodeList) XPATH.evaluate("name", sequenceDom, XPathConstants.NODESET);
        String nameAttr = XPATH.evaluate("@name", sequenceDom);
        if (Strings.isNullOrEmpty(nameAttr) && namesDom.getLength() == 0) {
          throw ConfigException
              .create(Code.PARSE_CONFIG_XML_ERROR, "Independent sequence name can not be null");
        }

        if (namesDom.getLength() > 0) {
          for (int j = 0; j < namesDom.getLength(); j++) {
            Node nameDom = namesDom.item(j);
            String nameContent = nameDom.getTextContent();
            if (!Strings.isNullOrEmpty(nameContent)) {
              createSequence(sequenceDom, sequences, nameContent);
            }
          }
        } else {
          createSequence(sequenceDom, sequences, nameAttr);
        }
      }
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static void parseTables(VirtualDb virtualDb, Node virtualDBDom, ShardConfig shardConfig,
      Sequences sequences) {
    try {
      NodeList tablesDom = (NodeList) XPATH.evaluate("table", virtualDBDom, XPathConstants.NODESET);

      Tables tables = new Tables();
      for (int i = 0; i < tablesDom.getLength(); i++) {
        Node tableDom = tablesDom.item(i);

        Table parentTable = createTableBaseParam(tableDom, virtualDb.getShards().allShardNames(),
            sequences);
        //分片表解析
        if (!parentTable.isGlobal()) {
          //DatabaseRule
          String databaseRule = XPATH.evaluate("@databaseRule", tableDom);
          if (!Strings.isNullOrEmpty(databaseRule)) {
            ShardRule dbShardRule = shardConfig.getRuleConfig().getShardRules().get(databaseRule);

            parentTable.setDatabaseRule(dbShardRule);
            parentTable.addShardColumns(dbShardRule.getShardColumns());
          }

          //TableRule
          addTableRule(parentTable, tableDom, shardConfig.getRuleConfig().getShardRules());

          if (null == parentTable.getDatabaseRule() && null == parentTable.getTableRule()) {
            if (parentTable.getDatabaseShards().size() == 1) {
              String tableName = parentTable.getName();
              if (tableName.contains("*")) {
                addWildcard(virtualDb, tableName, false, parentTable.getDatabaseShards());
                continue;
              }
            }else{
              throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR,
                  "Database rule and Table rule can not both be null when actualTables exist, table name = "
                      + parentTable.getName());
            }
          }

          //SubTable
          NodeList subTablesDom = (NodeList) XPATH
              .evaluate("subTable", tableDom, XPathConstants.NODESET);
          for (int j = 0; j < subTablesDom.getLength(); j++) {
            Node subTableDom = subTablesDom.item(j);

            Table subTable = createTableBaseParam(subTableDom, parentTable.getDatabaseShards(),
                sequences);
            subTable.setParentTable(parentTable);
            subTable.setSubTable(true);
            subTable.setDatabaseShards(parentTable.getDatabaseShards());
            subTable.setDatabaseRule(parentTable.getDatabaseRule());
            subTable.addShardColumns(parentTable.getDatabaseRule().getShardColumns());
            addTableRule(subTable, subTableDom, shardConfig.getRuleConfig().getShardRules());
            check(subTable);
            tables.add(subTable);
          }

          //index
          NodeList indexesDom = (NodeList) XPATH
              .evaluate("index", tableDom, XPathConstants.NODESET);
          Map<String, Index> indexColumn = new HashMap<>();
          for (int j = 0; j < indexesDom.getLength(); j++) {
            Node indexDom = indexesDom.item(j);

            Index index = parseIndex(virtualDb, tables, indexDom, shardConfig);
            indexColumn.put(index.getRefColumn(), index);
          }
          if(indexColumn.size() > 0){
            parentTable.setIndexColumn(indexColumn);
            parentTable.setHasIndex(true);
          }
        } else {
          String tableName = parentTable.getName();
          if (tableName.contains("*")) {
            addWildcard(virtualDb, tableName, true, parentTable.getDatabaseShards());
            continue;
          }
        }
        check(parentTable);
        tables.add(parentTable);
      }
      virtualDb.setTables(tables);
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static void addWildcard(VirtualDb virtualDb, String tableName, boolean isGlobal,
      List<String> databaseShards) {
    for (Wildcard wildcard : virtualDb.getWildcards()) {
      if (wildcard.getName().equals(tableName)) {
        throw ConfigException
            .create(Code.PARSE_CONFIG_XML_ERROR, "Duplicate wildcard, name: " + tableName);
      }
    }

    String pattern = tableName.replaceAll("\\*", ".*");
    Wildcard wildcard = new Wildcard();
    wildcard.setName(tableName);
    wildcard.setPattern(pattern);
    wildcard.setGlobal(isGlobal);
    wildcard.setDatabaseShards(databaseShards);
    virtualDb.getWildcards().add(wildcard);
  }

  private static Table createTableBaseParam(Node tableDom, List<String> defaultShards,
      Sequences sequences) {
    try {
      Table table = new Table();
      table.setName(XPATH.evaluate("@name", tableDom).toUpperCase());
      String shardOnTable = XPATH.evaluate("@shards", tableDom);
      if (!Strings.isNullOrEmpty(shardOnTable)) {
        String[] shardNames = SplitUtil.split(shardOnTable, ',', '$', '-');
        Arrays.sort(shardNames);// 排序以解决同一分片集中的分片配置顺序不同导致的路由错乱
        table.setDatabaseShards(Arrays.asList(shardNames));
      } else {
        table.setDatabaseShards(defaultShards);
      }
      String type = XPATH.evaluate("@type", tableDom);
      if (!Strings.isNullOrEmpty(type)) {
        if (!"global".equals(type)) {
          throw ConfigException
              .create(Code.PARSE_CONFIG_XML_ERROR, "Unsupported type of table, type: " + type);
        }
        table.setGlobal(true);
      }
      createSequenceOnTable(table, tableDom, sequences);
      return table;
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static void createSequenceOnTable(Table table, Node tableDom, Sequences sequences) {
    try {
      Map<String, String> sequenceWithColumn = new HashMap<>();

      NodeList sequencesDom = (NodeList) XPATH
          .evaluate("sequence", tableDom, XPathConstants.NODESET);
      for (int i = 0; i < sequencesDom.getLength(); i++) {
        Node sequenceDom = sequencesDom.item(i);

        String sequenceName = createSequence(sequenceDom, sequences);
        String column = XPATH.evaluate("@refColumn", sequenceDom);
        if (Strings.isNullOrEmpty(column)) {
          throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR,
              "Sequence on table not contain 'refColumn', table name: " + table.getName()
                  + " ; sequence name: " + sequenceName);
        }

        sequenceWithColumn.put(column.toUpperCase(), sequenceName);
      }
      if (!sequenceWithColumn.isEmpty()) {
        table.setSequences(sequenceWithColumn);
        table.setAutoIncrement(true);
      }
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static String createSequence(Node sequenceDom, Sequences sequences) {
    return createSequence(sequenceDom, sequences, null);
  }

  private static String createSequence(Node sequenceDom, Sequences sequences, String sequenceName) {
    try {
      final String name = !Strings.isNullOrEmpty(sequenceName) ? sequenceName.toUpperCase()
          : XPATH.evaluate("@name", sequenceDom).toUpperCase();
      if (Strings.isNullOrEmpty(name)) {
        throw ConfigException
            .create(Code.SEQUENCE_INCORRECT_ERROR, "Sequence name can not be null.");
      }
      String type = XPATH.evaluate("@type", sequenceDom);
      try {
        //BreadCrumb
        if (Type.BREADCRUMB.name.equals(type)) {
          String cacheString = XPATH.evaluate("@cache", sequenceDom);
          final int cache = !Strings.isNullOrEmpty(cacheString) ? Integer.parseInt(cacheString)
              : DefaultBreadcrumbDef.DEFAULT_CACHE;

          String incrString = XPATH.evaluate("@incr", sequenceDom);
          final long incr = !Strings.isNullOrEmpty(incrString) ? Long.parseLong(incrString)
              : DefaultBreadcrumbDef.DEFAULT_INCR;

          String startString = XPATH.evaluate("@start", sequenceDom);
          final long start = !Strings.isNullOrEmpty(startString) ? Long.parseLong(startString)
              : DefaultBreadcrumbDef.DEFAULT_START;

          SequenceDef sequenceDef = new DefaultBreadcrumbDef() {
            @Override
            public String name() {
              return name;
            }

            @Override
            public int cache() {
              return cache;
            }

            @Override
            public long incr() {
              return incr;
            }

            @Override
            public long start() {
              return start;
            }
          };
          sequences.add(sequenceDef);
        } else {
          String workerIdWidthString = XPATH.evaluate("@workerIdWidth", sequenceDom);
          String sequenceWidthString = XPATH.evaluate("@sequenceWidth", sequenceDom);
          //SnowFlake
          if (Type.SNOWFLAKE.name.equals(type)) {
            final int workerIdWidth =
                !Strings.isNullOrEmpty(workerIdWidthString) ? Integer.parseInt(workerIdWidthString)
                    : DefaultSnowflakeDef.DEFAULT_WORKERID_BITS;
            final int sequenceWidth =
                !Strings.isNullOrEmpty(sequenceWidthString) ? Integer.parseInt(sequenceWidthString)
                    : DefaultSnowflakeDef.DEFAULT_SEQUENCE_BITS;
            SequenceDef sequenceDef = new DefaultSnowflakeDef() {
              @Override
              public String name() {
                return name;
              }

              @Override
              public int workerIdWidth() {
                return workerIdWidth;
              }

              @Override
              public int sequenceWidth() {
                return sequenceWidth;
              }
            };
            sequences.add(sequenceDef);
          }
          //Ticktock
          if (Type.TICKTOCK.name.equals(type)) {
            final int workerIdWidth =
                !Strings.isNullOrEmpty(workerIdWidthString) ? Integer.parseInt(workerIdWidthString)
                    : DefaultTicktockDef.DEFAULT_WORKERID_BITS;
            final int sequenceWidth =
                !Strings.isNullOrEmpty(sequenceWidthString) ? Integer.parseInt(sequenceWidthString)
                    : DefaultTicktockDef.DEFAULT_SEQUENCE_BITS;
            SequenceDef sequenceDef = new DefaultTicktockDef() {
              @Override
              public String name() {
                return name;
              }

              @Override
              public int workerIdWidth() {
                return workerIdWidth;
              }

              @Override
              public int sequenceWidth() {
                return sequenceWidth;
              }
            };
            sequences.add(sequenceDef);
          }
        }
      } catch (Exception e) {
        throw ConfigException.create(Code.SEQUENCE_INCORRECT_ERROR, e.getMessage());
      }
      return name;
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static void addTableRule(Table table, Node tableDom, ShardRules shardRules) {
    try {
      String actualTablesString = XPATH.evaluate("@actualTables", tableDom);
      if (!Strings.isNullOrEmpty(actualTablesString)) {
        //actualTables
        String[] actualTables = SplitUtil.split(actualTablesString, ',', '$', '-');
        Arrays.sort(actualTables);// 排序以解决同一分片集中的分片配置顺序不同导致的路由错乱
        table.setActualTables(Arrays.asList(actualTables));

        //tableRule
        String tableRuleName = XPATH.evaluate("@tableRule", tableDom);
        if (Strings.isNullOrEmpty(tableRuleName)) {
          throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR,
              "Table rule can not be null when actualTables exists, table name = " + table
                  .getName());
        }

        ShardRule tableRule = shardRules.get(tableRuleName);
        table.setTableRule(tableRule);
        table.addShardColumns(tableRule.getShardColumns());
      }
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static Index parseIndex(VirtualDb virtualDb, Tables tables, Node indexDom, ShardConfig shardConfig) {
    try {
      Index index = new Index();
      index.setName(XPATH.evaluate("@name", indexDom).toUpperCase());
      String shardOnTable = XPATH.evaluate("@shards", indexDom);
      if (!Strings.isNullOrEmpty(shardOnTable)) {
        String[] shardNames = SplitUtil.split(shardOnTable, ',', '$', '-');
        Arrays.sort(shardNames);// 排序以解决同一分片集中的分片配置顺序不同导致的路由错乱
        index.setDatabaseShards(Arrays.asList(shardNames));
      } else {
        index.setDatabaseShards(virtualDb.getShards().allShardNames());
      }
      //DatabaseRule
      String databaseRule = XPATH.evaluate("@databaseRule", indexDom);
      if (!Strings.isNullOrEmpty(databaseRule)) {
        ShardRule dbShardRule = shardConfig.getRuleConfig().getShardRules().get(databaseRule);

        index.setDatabaseRule(dbShardRule);
        index.addShardColumns(dbShardRule.getShardColumns());
      }

      //TableRule
      addTableRule(index, indexDom, shardConfig.getRuleConfig().getShardRules());

      if (null == index.getDatabaseRule() && null == index.getTableRule()
          && index.getDatabaseShards().size() != 1) {
        throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR,
            "Database rule and Table rule can not both be null when actualTables exist, table name = "
                + index.getName());
      }

      check(index);

      index.setRefColumn(XPATH.evaluate("@refColumn", indexDom).toUpperCase());

      String columnsString = XPATH.evaluate("@columns", indexDom);
      if("*".equals(columnsString)){
        index.setCopy(true);
      }else{
        String[] columns = columnsString.toUpperCase().split(",");
        index.setColumns(Arrays.asList(columns));
      }
      tables.add(index);

      return index;
    } catch (XPathExpressionException e) {
      throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR, e.getMessage());
    }
  }

  private static void check(Table table) {
    if (table.isGlobal()) {
      return;
    }
    //分表校验
    if (table.hasTableShard()) {
      if (null == table.getDatabaseRule() && table.getDatabaseShards().size() != 1) {
        throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR,
            "Table-shard must have one shard, table name = " + table.getName());
      }
    }
    //分片校验
    else {
      if (null == table.getDatabaseRule() && table.getDatabaseShards().size() != 1) {
        throw ConfigException.create(Code.PARSE_CONFIG_XML_ERROR,
            "Database-shard must have rule, table name = " + table.getName());
      }
    }
  }
}
