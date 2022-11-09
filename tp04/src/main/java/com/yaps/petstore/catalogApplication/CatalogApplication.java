package com.yaps.petstore.catalogApplication;

import com.yaps.petstore.catalogApplication.domainObjectManagement.CategoryManagemenUI;
import com.yaps.petstore.catalogApplication.domainObjectManagement.ItemManagementUI;
import com.yaps.petstore.catalogApplication.domainObjectManagement.ProductManagementUI;
import com.yaps.utils.textUi.Menu;
import com.yaps.utils.textUi.SimpleIO;

public class CatalogApplication {

    SimpleIO io = new SimpleIO();

    Menu yesNoMenu = new Menu(io).add("y", "yes").add("n", "no");

    public void run() {
        Menu mainMenu = new Menu(io)
                .setWelcomeText("Please choose what you want to manage")
                .add("c", "manage Categories")
                .add("p", "manage Products")
                .add("i", "manage Items")
                .add("q", "quit");
        String choice;
        boolean quit = false;
        do {
            choice = mainMenu.choose();
            switch (choice) {
                case "c":
                    manageCategories();
                    break;
                case "p":
                    manageProducts();
                    break;
                case "i":
                    manageItems();
                    break;
                case "q": {
                    String confirmation = yesNoMenu.setWelcomeText("Do you really want to quit ?").choose();
                    quit = "y".equals(confirmation);
                    break;
                }
                default:
                    io.println("unexpected choice " + choice);
                    break;
            }
        } while (!quit);
    }

    private void manageItems() {
        new ItemManagementUI(io).run();
    }

    private void manageProducts() {
        new ProductManagementUI(io).run();
    }

    private void manageCategories() {
        new CategoryManagemenUI(io).run();
    }

}
