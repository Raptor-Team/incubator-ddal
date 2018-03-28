package studio.raptor.demo.mybatis.mysql.springboot.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import studio.raptor.demo.mybatis.mysql.springboot.entity.Customer;

/**
 * @author Sam
 * @since 3.0.0
 */
@Mapper
public interface CustomerRepository {

  @Select("select id, name, gender, address, level, is_locked, last_active_time "
      + "from customer "
      + "where id = #{id}")
  @Results({
      @Result(property = "isLocked", column = "is_locked"),
      @Result(property = "lastActiveTime", column = "last_active_time")
  })
  Customer queryById(Long id);

  @Insert(
      "insert into customer (id, name, gender, address, level, is_locked, last_active_time) "
          + "value ("
          + "#{id, jdbcType=INTEGER},"
          + "#{name, jdbcType=VARCHAR},"
          + "#{gender, jdbcType=INTEGER},"
          + "#{address, jdbcType=VARCHAR},"
          + "#{level, jdbcType=INTEGER},"
          + "#{isLocked, jdbcType=INTEGER},"
          + "#{lastActiveTime, jdbcType=VARCHAR}"
          + ")")
  void createCustomer(Customer customer);
}
