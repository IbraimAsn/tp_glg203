package com.yaps.petstore.catalog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * This class represents a Product in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
@Entity
public final class Product {

    @Id
    @NotBlank
    private String id = "";

    // ======================================
    // = Attributes =
    // ======================================
    @NotBlank
    private String name = "";
    
    @NotBlank
    private String description = "";

    @ManyToOne
    @NotNull
    @JoinColumn(name = "category_fk")
    private Category category;

    // ======================================
    // = Constructors =
    // ======================================

    Product() {

    }

    public Product(final String id, final String name, final String description, final Category category) {
        if (id == null || name == null || description == null || category == null)
            throw new NullPointerException();
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    // ======================================
    // = Business methods =
    // ======================================

    // ======================================
    // = Getters and Setters =

    public void setId(String id) {
        if (id == null)
            throw new NullPointerException();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (name == null)
            throw new NullPointerException();
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        if (description == null)
            throw new NullPointerException();
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        if (category == null)
            throw new NullPointerException();
        this.category = category;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("product id=").append(id)
                .append(", name=").append(name)
                .append("\n")
                .append("category=")
                .append(category.shortDisplay())
                .append("\n")
                .append("description=")
                .append(description);
        return buf.toString();
    }

    public String shortDisplay() {
        return id + "\t" + name;
    }

}
