package studio.raptor.demo.mybatis.oracle.springboot.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import studio.raptor.demo.mybatis.oracle.springboot.entity.Customer;

/**
 * @author Sam
 * @since 3.0.0
 */
@Mapper
public interface CustomerRepository {

  @Select("select id, name, gender, address, customer_level, is_locked, last_active_time "
      + "from customer "
      + "where id = #{id}")
  @Results({
      //@Result(property = "isLocked", column = "is_locked", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
      @Result(property = "level", column = "customer_level"),
      @Result(property = "lastActiveTime", column = "last_active_time")
  })
  Customer queryById(Long id);

  @Select("select id, name, gender, address, customer_level, is_locked, last_active_time "
      + "from customer "
      + "where name = #{name}")
  @Results({
      @Result(property = "level", column = "customer_level"),
      @Result(property = "lastActiveTime", column = "last_active_time")
  })
  List<Customer> queryByName(String name);

  @Insert(
      "insert into CUSTOMER (id, name, gender, address, customer_level, is_locked, last_active_time) "
          + "values ("
          + "#{id, jdbcType=INTEGER},"
          + "#{name, jdbcType=VARCHAR},"
          + "#{gender, jdbcType=INTEGER},"
          + "#{address, jdbcType=VARCHAR},"
          + "#{level, jdbcType=INTEGER},"
          + "#{isLocked, jdbcType=INTEGER},"
          + "#{lastActiveTime, jdbcType=VARCHAR}"
          + ")")
  int createCustomer(Customer customer);
}
