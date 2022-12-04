package com.yaps.petstore.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yaps.petstore.security.domain.YapsUser;

@Repository
public interface YapsUserDAO extends JpaRepository<YapsUser,String> {
    
}
