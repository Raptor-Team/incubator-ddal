package studio.raptor.ddal.dashboard.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Global id bean
 *
 * @author Sam
 * @since 3.1.0
 */
@Getter
@Setter
@Entity
@Table(name = "gid")
public class GlobalID {

  @Id
  private Integer id;
  private String name;

  @Column(name = "last_number")
  private Long lastNumber;

  private Integer stepLength;
  private String status;
}
