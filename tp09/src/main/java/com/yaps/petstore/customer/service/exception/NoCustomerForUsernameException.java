package com.yaps.petstore.customer.service.exception;

public class NoCustomerForUsernameException extends RuntimeException {
    private final String username;

    public NoCustomerForUsernameException(String username) {
        super("No customer for user %s".formatted(username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    
}
