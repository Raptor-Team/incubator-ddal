package studio.raptor.ddal.config.model.shard;

/**
 * 主键
 *
 * @author Charley
 * @since 1.0
 */
public class PrimaryKey {
  private String column;
  private String sequence;

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public String getSequence() {
    return sequence;
  }

  public void setSequence(String sequence) {
    this.sequence = sequence;
  }
}
