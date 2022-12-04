package com.yaps.petstore.customer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yaps.petstore.customer.domain.Customer;

public interface CustomerDAO extends JpaRepository<Customer,String> {
    
    @Query("select c from Customer c where c.user.username = :username")
    List<Customer> findByUsername(@Param("username") String username);
}
