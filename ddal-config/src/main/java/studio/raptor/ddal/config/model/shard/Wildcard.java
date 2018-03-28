package studio.raptor.ddal.config.model.shard;

import java.util.List;

/**
 * 通配符
 *
 * @author Charley
 * @since 1.0
 */
public class Wildcard {

  private String name;
  private String pattern;
  private boolean isGlobal;
  private List<String> databaseShards;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public boolean isGlobal() {
    return isGlobal;
  }

  public void setGlobal(boolean global) {
    isGlobal = global;
  }

  public List<String> getDatabaseShards() {
    return databaseShards;
  }

  public void setDatabaseShards(List<String> databaseShards) {
    this.databaseShards = databaseShards;
  }
}
