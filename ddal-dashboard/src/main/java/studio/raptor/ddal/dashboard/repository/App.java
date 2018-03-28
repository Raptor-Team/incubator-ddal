package studio.raptor.ddal.dashboard.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Sam
 * @since 3.1.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "app")
public class App {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "app_name")
  private String appName;
  // 业务中心标识
  private String center;
  // 应用主机ip
  @Column(name = "host_ip")
  private String hostIp;
  // 应用端口
  private Integer port;
  // ddal 管理接口
  @Column(name = "admin_url")
  private String adminUrl;

  private String status;
}
