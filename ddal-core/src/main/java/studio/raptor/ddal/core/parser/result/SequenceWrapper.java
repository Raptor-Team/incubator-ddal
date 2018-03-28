package studio.raptor.ddal.core.parser.result;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class SequenceWrapper {
  private String name;

  public SequenceWrapper(String name){
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
