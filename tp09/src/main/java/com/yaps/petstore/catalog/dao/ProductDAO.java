package com.yaps.petstore.catalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yaps.petstore.catalog.domain.Product;

@Repository
public interface ProductDAO extends JpaRepository<Product,String>{
    
}
