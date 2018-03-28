package studio.raptor.demo.mybatis.mysql.springboot.service;

import studio.raptor.demo.mybatis.mysql.springboot.entity.Customer;

/**
 * @author Sam
 * @since 3.0.0
 */
public interface CustomerService {
  Customer queryById(Long id);
  Long createCustomer(Customer customer);
}
