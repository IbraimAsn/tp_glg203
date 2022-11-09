package com.yaps.utils.model;

/**
 * This exception is thrown when some checking validation error is found.
 */
public final class CheckException extends Exception {

    public CheckException(final String message) {
        super(message);
    }
}
