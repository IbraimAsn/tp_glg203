package com.yaps.petstore.domain.item;

import com.yaps.petstore.domain.product.Product;
import com.yaps.utils.model.CheckException;
import com.yaps.utils.model.DomainObject;

public class Item extends DomainObject {
    private String name;
    private double unitCost;
    private Product product;

    public Item(){}
    public Item(String id){
        super(id);
    }
    public Item(String id, String name, double cost, Product product){
        super(id);
        this.name = name;
        this.unitCost = cost;
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double cost) {
        this.unitCost = cost;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void checkData() throws CheckException{
        if(id == null || "".equals(id)){
            throw new CheckException("invalid id");
        }
        if(name == null || "".equals(name)){
            throw new CheckException("invalid name");
        }
        if(unitCost < 0){
            throw new CheckException("invalid cost");
        }
        if(product == null){
            throw new CheckException("invalid product");
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("item id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", product=").append(product.getId());
        sb.append("\t").append(product.getName());
        sb.append(", unit cost=").append(unitCost);
        return sb.toString();
    }


    @Override
    public String shortDisplay(){
        return id + "\t" + name;
    }
}
