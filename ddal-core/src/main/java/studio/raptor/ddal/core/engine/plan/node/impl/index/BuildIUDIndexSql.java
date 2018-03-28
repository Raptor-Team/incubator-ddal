package studio.raptor.ddal.core.engine.plan.node.impl.index;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import studio.raptor.ddal.common.collections.FastArrayList;
import studio.raptor.ddal.config.model.shard.Index;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.core.engine.IndexTableOps;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.parser.result.SQLStatementType;

/**
 * 构造IUD类型Index执行SQL
 *
 * @author Charley
 * @since 1.0
 */
public class BuildIUDIndexSql extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    String sql = context.getOriginSql();
    SQLStatementType sqlType = context.getParseResult().getSqlType();
    List<Object> parameters = context.getSqlParameters();
    Collection<Index> indexTable = getIndexTable(context);
    List<IndexTableOps> opsList = assembleIndexOps(sql, sqlType, parameters, indexTable);
    context.getIndexTableOpsList().addAll(opsList);
  }

  /**
   * 获取需要处理的索引信息
   */
  private Collection<Index> getIndexTable(ProcessContext context) {
    Collection<Index> result = Collections.EMPTY_LIST;
    int count = 0;
    for (Table shardTable : context.getShardTables()) {
      if (shardTable.hasIndex()) {
        result = shardTable.getIndexColumn().values();
        count++;
      }
    }

    if (count > 1) {
      throw new UnsupportedOperationException(
          "Could not deal with more than one table contain index.");
    }

    return result;
  }

  private List<IndexTableOps> assembleIndexOps(String sql, SQLStatementType sqlType,
      List<Object> parameters, Collection<Index> indexTable) {

    StringPosition position;
    switch (sqlType) {
      case UPDATE:
        position = new UpdateLexer(sql).lex();
        break;
      case INSERT:
        position = new InsertLexer(sql).lex();
        break;
      case DELETE:
        position = new DeleteLexer(sql).lex();
        break;
      default:
        throw new UnsupportedOperationException();
    }

    List<IndexTableOps> opsList = new FastArrayList<>();
    for (Index index : indexTable) {
      IndexTableOps ops = new IndexTableOps();
      ops.getParameters().addAll(parameters);
      ops.setIdxSql(sql.replace(sql.substring(position.begin, position.end), index.getName()));
      opsList.add(ops);
    }
    return opsList;
  }

  private static abstract class Lexer {

    protected final String sql;
    protected final int length;
    protected int index = 0;
    protected char ch;
    protected boolean isStarted = false;

    protected Lexer(String sql) {
      this.sql = sql;
      this.length = sql.length();
    }

    protected void reader() {
      ch = sql.charAt(index++);
    }

    public StringPosition lex() {
      StringPosition position = new StringPosition();

      for (; ; ) {
        reader();
        if (index >= length) {
          break;
        }

        if (isStarted && ch != ' ') {
          position.begin = index - 1;
          for(;;){
            reader();
            if (' ' == ch || '(' == ch) {
              break;
            }
          }
          position.end = index - 1;
          break;
        }

        readKey();
      }
      return position;
    }

    abstract void readKey();

    protected String readString() {
      StringBuffer sb = new StringBuffer().append(ch);
      for (; ; ) {
        if (index >= length) {
          break;
        }
        reader();
        if (' ' == ch) {
          break;
        }
        sb.append(ch);
      }
      return sb.toString();
    }
  }

  private static class InsertLexer extends Lexer {

    public InsertLexer(String sql) {
      super(sql);
    }

    @Override
    public void readKey() {
      if (ch == 'I' || ch == 'i') {
        String key = readString();
        if (key.toUpperCase().equals("INTO")) {
          isStarted = true;
        }
      }
    }
  }

  private static class UpdateLexer extends Lexer {

    public UpdateLexer(String sql) {
      super(sql);
    }

    @Override
    public void readKey() {
      if (ch == 'U' || ch == 'u') {
        String key = readString();
        if (key.toUpperCase().equals("UPDATE")) {
          isStarted = true;
        }
      }
    }
  }

  private static class DeleteLexer extends Lexer {

    public DeleteLexer(String sql) {
      super(sql);
    }

    @Override
    public void readKey() {
      if (ch == 'F' || ch == 'f') {
        String key = readString();
        if (key.toUpperCase().equals("FROM")) {
          isStarted = true;
        }
      }
    }
  }

  public static class StringPosition {
    private int begin = 0;
    private int end = 0;
  }
}
