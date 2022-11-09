package com.yaps.petstore.catalogApplication.domainObjectManagement;

import java.sql.Connection;

import com.yaps.petstore.domain.category.Category;
import com.yaps.petstore.domain.category.CategoryDAO;
import com.yaps.utils.textUi.Menu;
import com.yaps.utils.textUi.SimpleIO;

public class CategoryManagemenUI
        extends AbstractEntityManagementUI<Category, CategoryDAO> {

    public CategoryManagemenUI(SimpleIO io) {
        super(io, "category");
    }

    @Override
    protected CategoryDAO createDAO(Connection connection) {
        return new CategoryDAO(connection);
    }

    @Override
    protected Menu createEditMenu() {
        return new Menu(io)
                .setWelcomeText("Select item to change")
                .add("n", "name")
                .add("d", "description")
                .add("q", "finish editing");
    }

    @Override
    protected void processEditing(Connection connection, String fieldCode, Category category) {
        switch (fieldCode) {
            case "n":
                String name = io.askForString("name");
                category.setName(name);
                break;
            case "d":
                String description = io.askForString("description");
                category.setDescription(description);
                break;
        }

    }

    @Override
    protected Category askForEntityData(Connection connection) {
        String id = io.askForString("new category id");
        String name = io.askForString("new category name");
        String description = io.askForString("new category description");
        return new Category(id, name, description);
    }

}
