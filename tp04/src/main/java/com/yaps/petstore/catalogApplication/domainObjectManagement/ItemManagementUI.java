package com.yaps.petstore.catalogApplication.domainObjectManagement;

import java.sql.Connection;

import com.yaps.petstore.catalogApplication.ConnectionHelper;
import com.yaps.petstore.domain.item.Item;
import com.yaps.petstore.domain.item.ItemDAO;
import com.yaps.petstore.domain.product.Product;
import com.yaps.petstore.domain.product.ProductDAO;
import com.yaps.utils.textUi.Menu;
import com.yaps.utils.textUi.SimpleIO;

public class ItemManagementUI extends AbstractEntityManagementUI<Item, ItemDAO> {

    public ItemManagementUI(SimpleIO io) {
        super(io, "item");
    }

    @Override
    protected ItemDAO createDAO(Connection connection) {
        return new ItemDAO(connection);
    }

    @Override
    protected Menu createEditMenu() {
        return new Menu(io)
                .add("n", "name")
                .add("c", "unit cost")
                .add("p", "product")
                .add("q", "quit");
    }

     /**
     * Pre-condition to create an item : at least a product should be available.
     */
    @Override
    protected boolean canCreate() {
        return ! ConnectionHelper.computeWithConnection(io, 
            connection -> new ProductDAO(connection).findAll().isEmpty()
        );
    }

    @Override
    protected void processEditing(Connection connection, String fieldCode, Item entity) {
        switch (fieldCode) {
            case "n":
                String name = io.askForString("new name");
                entity.setName(name);
                break;
            case "c":
                double newCost = io.askForPositiveDouble("new unit cost");
                entity.setUnitCost(newCost);
                break;
            case "p":
                Product p = selectProduct(connection);
                entity.setProduct(p);
                break;
            default:
                break;
        }
    }

    @Override
    protected Item askForEntityData(Connection connection) {
        String id = io.askForString("id");
        String name = io.askForString("name");
        double unitCost = io.askForPositiveDouble("unit cost");
        Product product = selectProduct(connection);
        return new Item(id, name, unitCost, product);
    }

    private Product selectProduct(Connection connection) {
        return new EntitySelector<>("product",
                new ProductDAO(connection), io).selectEntity();
    }

}
