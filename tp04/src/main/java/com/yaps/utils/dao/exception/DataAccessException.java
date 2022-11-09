package com.yaps.utils.dao.exception;

import java.sql.SQLException;

/**
 * This exception is throw when a general problem occurs in the persistent layer.
 */
public final class DataAccessException extends RuntimeException {

    public DataAccessException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(SQLException e) {
        super(e);
    }
}
