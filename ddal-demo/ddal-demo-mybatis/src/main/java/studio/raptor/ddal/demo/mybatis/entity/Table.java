package studio.raptor.ddal.demo.mybatis.entity;

import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sam
 * @since 3.0.0
 */
@Getter
@Setter
@ToString
public class Table {

  private Long id;
  private String name;
  private Timestamp createTime;
  private Integer state;
}
