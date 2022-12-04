package com.yaps.petstore.customer.service.exception;

public class MultipleCustomersForUsername extends RuntimeException{
    private final String username;

    public MultipleCustomersForUsername(String username) {
        super("Multiple customers for user %s".formatted(username));
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
