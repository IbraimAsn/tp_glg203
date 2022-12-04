package com.yaps.petstore.catalog.dto;


/**
 * This class follows the Data Transfert Object design pattern
 * . It is a client view of a Category. This class only
 * transfers data from a distant service to a client.
 */
public class CategoryDTO {

    // ======================================
    // =             Attributes             =
    // ======================================
    private String id;
    private String name;
    private String description;

    // ======================================
    // =            Constructors            =
    // ======================================
    public CategoryDTO() {
    }

    public CategoryDTO(final String id, final String name, final String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // ======================================
    // =         Getters and Setters        =
    // ======================================
    

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("CategoryDTO{");
        buf.append("id=").append(getId());
        buf.append(",name=").append(getName());
        buf.append(",description=").append(getDescription());
        buf.append('}');
        return buf.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
