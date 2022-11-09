package com.yaps.utils.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An extractor which knows how to extract one element from a resultset line.
 */
@FunctionalInterface
public interface ResultSetExtractor<T> {    
    /**
     * Returns an object corresponding to the data in the current ResultSet line.
     * <p> Usually, the extractor will only use the ResultSet. 
     * @param res
     * @return an object of type T.
     * @throws SQLException
     */
    T extract(ResultSet res) throws SQLException;
}
