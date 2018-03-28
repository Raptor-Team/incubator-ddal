package studio.raptor.ddal.core.parser.result;

/**
 * SQL操作类型枚举类,支持DML,DDL,DCL,DAL四种类型
 *
 * @author Charley
 * @since 1.0
 */
public enum Operate {
  DML("DML"),

  DDL("DDL"),

  DCL("DCL"),

  DAL("DAL"),

  UNSUPPORTED("UNSUPPORTED");

  public final String name;

  Operate(){
    this(null);
  }

  Operate(String name){
    this.name = name;
  }
}
