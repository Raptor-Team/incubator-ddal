package studio.raptor.demo.mybatis.mysql.springboot.controller;

import java.sql.Timestamp;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import studio.raptor.demo.mybatis.mysql.springboot.entity.Customer;
import studio.raptor.demo.mybatis.mysql.springboot.service.CustomerService;

/**
 * @author Sam
 * @since 3.0.0
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CustomerController {

  private static Logger log = LoggerFactory.getLogger(CustomerController.class);

  @Resource
  private CustomerService customerService;

  @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity queryCustomerById(@PathVariable Long id) {
    try {
      Customer customer = customerService.queryById(id);
      if (null == customer) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
      return ResponseEntity.ok(customer);
    } catch (Exception e) {
      log.error("Query customer error", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  /**
   *  入参示例：{"name":"明天礼拜六"}
   */
  @ResponseBody
  @RequestMapping(value = "/customer/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity createCustomer(@RequestBody  Customer customer) {
    try {
      Customer newCustomer = new Customer();
      newCustomer.setName(customer.getName());
      newCustomer.setAddress("1601 Willow Road, Menlo Park");
      newCustomer.setGender(1);
      newCustomer.setIsLocked(0);
      newCustomer.setLevel(100);
      newCustomer.setLastActiveTime(new Timestamp(System.currentTimeMillis()));
      Long customerId = customerService.createCustomer(newCustomer);
      newCustomer.setId(customerId);
      return ResponseEntity.ok(newCustomer);
    } catch (Exception e) {
      log.error("Create customer error", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }
}
