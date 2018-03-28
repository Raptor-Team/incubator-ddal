package studio.raptor.demo.mybatis.oracle.springboot.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.raptor.demo.mybatis.oracle.springboot.entity.Customer;
import studio.raptor.demo.mybatis.oracle.springboot.entity.CustomerProfile;
import studio.raptor.demo.mybatis.oracle.springboot.repository.CustomerProfileRepository;
import studio.raptor.demo.mybatis.oracle.springboot.repository.CustomerRepository;
import studio.raptor.demo.mybatis.oracle.springboot.service.CustomerService;

/**
 * @author Sam
 * @since 3.0.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerProfileRepository customerProfileRepository;
  private static final AtomicLong ID = new AtomicLong(System.currentTimeMillis());

  public CustomerServiceImpl(
      CustomerRepository customerRepository, CustomerProfileRepository customerProfileRepository) {
    this.customerRepository = customerRepository;
    this.customerProfileRepository = customerProfileRepository;
  }

  @Override
  public Customer queryById(Long id) {
    return customerRepository.queryById(id);
  }

  @Override
  public List<Customer> queryByName(String name) {
    return customerRepository.queryByName(name);
  }

  @Transactional
  @Override
  public Long createCustomer(Customer customer) {
    customer.setId(ID.getAndIncrement());
    int affectedRows = this.customerRepository.createCustomer(customer);
    if(affectedRows <= 0) {
      throw new RuntimeException("Create customer failed.");
    }
    return customer.getId();
  }

  @Override
  public CustomerProfile queryProfileById(Long profileId) {
    return customerProfileRepository.queryById(profileId);
  }

  @Transactional
  @Override
  public Long createCustomerProfile(CustomerProfile profile) {
    profile.setCustomerProfileId(ID.getAndIncrement());
    this.customerProfileRepository.createCustomerProfile(profile);
    return profile.getCustomerProfileId();
  }
}
