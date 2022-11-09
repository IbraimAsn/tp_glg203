package com.yaps.petstore.catalogApplication.domainObjectManagement;

import java.sql.Connection;

import com.yaps.petstore.catalogApplication.ConnectionHelper;
import com.yaps.petstore.domain.category.Category;
import com.yaps.petstore.domain.category.CategoryDAO;
import com.yaps.petstore.domain.product.Product;
import com.yaps.petstore.domain.product.ProductDAO;
import com.yaps.utils.textUi.Menu;
import com.yaps.utils.textUi.SimpleIO;

public class ProductManagementUI extends AbstractEntityManagementUI<Product, ProductDAO> {

    public ProductManagementUI(SimpleIO io) {
        super(io, "product");
    }

    @Override
    protected ProductDAO createDAO(Connection connection) {
        return new ProductDAO(connection);
    }

    @Override
    protected Menu createEditMenu() {
        return new Menu(io)
                .setWelcomeText("Select item to change")
                .add("n", "name")
                .add("d", "description")
                .add("c", "category")
                .add("q", "finish editing");
    }

    /**
     * Pre-condition to create a product : at least an category should be available.
     */
    @Override
    protected boolean canCreate() {
        return ! ConnectionHelper.computeWithConnection(io, 
            connection -> new CategoryDAO(connection).findAll().isEmpty()
        );
    }

    @Override
    protected void processEditing(Connection connection, String fieldCode, Product product) {
        switch (fieldCode) {
            case "n":
                String name = io.askForString("name");
                product.setName(name);
                break;
            case "d":
                String description = io.askForString("description");
                product.setDescription(description);
                break;
            case "c":
                Category cat = selectCategory(connection);
                product.setCategory(cat);
                break;
        }
    }

    @Override
    protected Product askForEntityData(Connection connection) {
        String id = io.askForString("id");
        String name = io.askForString("name");
        String description = io.askForString("description");
        Category category = selectCategory(connection);
        return new Product(id, name, description, category);
    }

    private Category selectCategory(Connection connection) {
        return new EntitySelector<>("category", new CategoryDAO(connection), io).selectEntity();
    }

}
