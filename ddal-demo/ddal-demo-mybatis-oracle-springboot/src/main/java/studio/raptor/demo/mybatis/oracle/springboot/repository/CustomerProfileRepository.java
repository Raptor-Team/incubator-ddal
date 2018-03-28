/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package studio.raptor.demo.mybatis.oracle.springboot.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import studio.raptor.demo.mybatis.oracle.springboot.entity.Customer;
import studio.raptor.demo.mybatis.oracle.springboot.entity.CustomerProfile;

/**
 * @author Sam
 * @since 3.0.0
 */
@Mapper
public interface CustomerProfileRepository {

  @Select("select customer_profile_id, customer_id, profile_type from CUSTOMER_PROFILE where customer_profile_id = #{id}")
  @Results({
      @Result(property = "customerProfileId", column = "customer_profile_id"),
      @Result(property = "customerId", column = "customer_id"),
      @Result(property = "profileType", column = "profile_type")
  })
  CustomerProfile queryById(Long id);


  @Insert(
      "Insert into CUSTOMER_PROFILE (CUSTOMER_PROFILE_ID, CUSTOMER_ID, PROFILE_TYPE) "
          + "values ("
          + "#{customerProfileId, jdbcType=INTEGER}, "
          + "#{customerId, jdbcType=INTEGER}, "
          + "#{profileType, jdbcType=VARCHAR})")
  void createCustomerProfile(CustomerProfile profile);
}
