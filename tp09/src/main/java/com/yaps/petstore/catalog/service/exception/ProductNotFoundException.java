package com.yaps.petstore.catalog.service.exception;

public class ProductNotFoundException extends RuntimeException {
      
    private String id;

    public ProductNotFoundException(String id) {
        super("Unknown product %s".formatted(id));
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
