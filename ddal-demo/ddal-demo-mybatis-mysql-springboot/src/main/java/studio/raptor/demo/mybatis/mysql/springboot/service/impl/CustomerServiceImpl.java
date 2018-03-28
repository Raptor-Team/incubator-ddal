package studio.raptor.demo.mybatis.mysql.springboot.service.impl;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.raptor.demo.mybatis.mysql.springboot.entity.Customer;
import studio.raptor.demo.mybatis.mysql.springboot.repository.CustomerRepository;
import studio.raptor.demo.mybatis.mysql.springboot.service.CustomerService;

/**
 * @author Sam
 * @since 3.0.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private static final AtomicLong ID = new AtomicLong(System.currentTimeMillis());

  public CustomerServiceImpl(
      CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }


  @Override
  public Customer queryById(Long id) {
    return customerRepository.queryById(id);
  }

  @Transactional
  @Override
  public Long createCustomer(Customer customer) {
    customer.setId(ID.getAndIncrement());
    this.customerRepository.createCustomer(customer);
    return customer.getId();
  }
}
