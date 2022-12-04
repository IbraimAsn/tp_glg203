package com.yaps.petstore.sequence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yaps.petstore.sequence.dao.IdSequenceDAO;

@Service
public class IdSequenceServiceImpl implements IdSequenceService {

    @Autowired
    IdSequenceDAO dao;

    @Override
    public int nextCustomerId() {
        int newId = dao.getCurrentMaxId("customer") + 1;
        dao.setCurrentMaxId("customer", newId);
        return newId;
    }    
}
