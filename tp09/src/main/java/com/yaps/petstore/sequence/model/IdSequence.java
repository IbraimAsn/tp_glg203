package com.yaps.petstore.sequence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sequence_id")
public class IdSequence {
    @Id
    @Column(name = "table_name", length = 10)
    private String tableName;

    @Column(name = "max_id")
    private int maxId;

    IdSequence() {
    }

    public IdSequence(String tableName, int maxId) {
        this.tableName = tableName;
        this.maxId = maxId;
    }

    public int getMaxId() {
        return maxId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setMaxId(int maxId) {
        this.maxId = maxId;
    }

    public int nextId() {
        maxId++;
        return maxId;
    }
    
}
