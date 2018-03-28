package studio.raptor.ddal.tests;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class Test {
  @org.junit.Test
  public void test(){
    System.out.println(new obj().getA());
  }


  public static class obj{
    int a;

    public int getA() {
      return a;
    }

    public void setA(int a) {
      this.a = a;
    }
  }
}
