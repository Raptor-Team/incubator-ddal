package studio.raptor.ddal.common.algorithm;

import org.junit.Test;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class ModEfficientlyTest {

  private static final int count = 4;

  @Test
  public void testEfficiency(){
    long value = 1487532412413L;
    long result = 0;
    //Mod
    long oldStart = System.currentTimeMillis();
    for(long i=0; i<1000000000L; i++){
      result = value % count;
    }
    System.out.println("Old cost: "+(System.currentTimeMillis() -oldStart)+"ms");

    //new
    long newStart = System.currentTimeMillis();
    for(long i=0; i<1000000000L; i++){
      result = value & (count-1);
    }
    System.out.println("New cost: "+(System.currentTimeMillis() -newStart)+"ms");
  }
}
