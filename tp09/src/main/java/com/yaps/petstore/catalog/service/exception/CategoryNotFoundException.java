package com.yaps.petstore.catalog.service.exception;

public class CategoryNotFoundException extends RuntimeException {
    
    private String id;

    public CategoryNotFoundException(String id) {
        super("Unknown category %s".formatted(id));
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
}
