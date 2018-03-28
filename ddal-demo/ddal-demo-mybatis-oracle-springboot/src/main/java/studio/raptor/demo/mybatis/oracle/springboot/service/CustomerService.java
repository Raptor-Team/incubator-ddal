package studio.raptor.demo.mybatis.oracle.springboot.service;

import java.util.List;
import studio.raptor.demo.mybatis.oracle.springboot.entity.Customer;
import studio.raptor.demo.mybatis.oracle.springboot.entity.CustomerProfile;

/**
 * @author Sam
 * @since 3.0.0
 */
public interface CustomerService {
  Customer queryById(Long id);
  List<Customer> queryByName(String name);
  Long createCustomer(Customer customer);

  CustomerProfile queryProfileById(Long profileId);
  Long createCustomerProfile(CustomerProfile profile);
}
