package com.yaps.petstore.catalog.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;


/**
 * This class represents a Category in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
@Entity
public final class Category  {

	@Id
    @NotBlank
	private String id = "";
    @NotBlank
    private String name = "";
    @NotBlank
    private String description = "";

    public Category() {
    }

    public Category(final String id, final String name, final String description) {        
        if (id == null || name == null || description == null)
            throw new NullPointerException();
        this.id = id;
        this.name = name;
        this.description = description;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null)
            throw new NullPointerException();
        this.id = id;
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
        if (description == null) {
            throw new NullPointerException("description should not be null");
        }
        this.description = description;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("category id=").append(id);
        buf.append(", name=").append(name);
        buf.append("\ndescription=").append(description);
        return buf.toString();
    }

    public String shortDisplay() {
        return id + "\t" + name;
    }
}
