package com.yaps.petstore.sequence.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yaps.petstore.sequence.model.IdSequence;

@Repository
public interface IdSequenceDAO extends JpaRepository<IdSequence, String> {

    @Query("select s.maxId from IdSequence s where s.tableName = :tableName")
    int getCurrentMaxId(@Param("tableName") String tableName);

    @Modifying
    @Query("update IdSequence s set s.maxId = :id where s.tableName = :tableName")
    void setCurrentMaxId(@Param("tableName") String tableName, @Param("id") int newMaxId);
}
