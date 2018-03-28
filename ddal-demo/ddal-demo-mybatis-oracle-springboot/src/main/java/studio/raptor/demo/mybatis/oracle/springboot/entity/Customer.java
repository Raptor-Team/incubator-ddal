package studio.raptor.demo.mybatis.oracle.springboot.entity;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sam
 * @since 3.0.0
 */

@Setter
@Getter
@ToString
public class Customer {
  private Long id;
  private String name;
  private Integer gender;
  private String address;
  private Integer level;
  private Integer isLocked;
  private Timestamp lastActiveTime;

}
