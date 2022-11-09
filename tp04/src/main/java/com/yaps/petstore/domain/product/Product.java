package com.yaps.petstore.domain.product;

import com.yaps.petstore.domain.category.Category;
import com.yaps.utils.model.CheckException;
import com.yaps.utils.model.DomainObject;

public class Product extends DomainObject {
    private String name;
    private String description;
    private Category category;

    public Product() {
    }

    public Product(final String id){
        super(id);
    }

    public Product(final String id, final String name, final String description, Category category){
        super(id);
        this.name = name;
        this.description = description;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void checkData() throws CheckException{
        if(id == null || "".equals(id)){
            throw new CheckException("invalid id");
        }
        if(name == null || "".equals(name)){
            throw new CheckException("invalid name");
        }
        if(description == null || "".equals(description)){
            throw new CheckException("invalid description");
        }
        if(category == null){
            throw new CheckException("invalid category");
        }
    }

    @Override
    public String shortDisplay() {
        return id + "\t" + name;
    }

    @Override
    public String toString() {
       final StringBuilder buf = new StringBuilder();
       buf.append("product id=").append(id);
       buf.append(", name=").append(name);
       buf.append("\ncategory=").append(category.getId());
       buf.append("\t").append(category.getName());
       buf.append("\ndescription=").append(description);

       return buf.toString();
    }
}
