package studio.raptor.demo.mybatis.mysql.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sam
 * @since 3.0.0
 */
@RestController
public class HelloController {

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot!";
  }
}
