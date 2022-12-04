package com.yaps.petstore.catalog.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * This class represents an Item in the catalog of the YAPS company.
 * The catalog is divided into categories. Each one divided into products
 * and each product in items.
 */
@Entity
public final class Item {

    
	@Id
    @NotBlank
	private String id = "";

    // ======================================
    // =             Attributes             =
    // ======================================
    @NotBlank
    private String name = "";
    private double unitCost = 0.0;
    @NotNull
    private String imagePath = "";

    @ManyToOne
    @JoinColumn(name = "product_fk")
    @NotNull
    private Product product;


    // ======================================
    // =            Constructors            =
    // ======================================

    Item() {
        // empty !
    }

    public Item(final String id, final String name, final double unitCost, String imagePath, final Product product) {
        if (id == null || name == null || imagePath == null || product == null)
            throw new NullPointerException();
        this.id = id;
        this.name = name;
        this.unitCost = unitCost;
        this.imagePath = imagePath;
        this.product = product;
    }

  
    // ======================================
    // =         Getters and Setters        =
    // ======================================

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

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(final double unitCost) {
        this.unitCost = unitCost;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(final Product product) {
        if (product ==  null)
            throw new NullPointerException();
        this.product = product;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        if (imagePath ==  null)
            throw new NullPointerException();
        this.imagePath = imagePath;
    }

    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("item id=").append(id)
            .append(", name=").append(name)
            .append(", image path=").append(imagePath)
            .append(", product=").append(product.shortDisplay())
            .append(", unit cost=").append(unitCost);
        return buf.toString();
    }

    public String shortDisplay() {
        return id + "\t" + name;
    }

}
