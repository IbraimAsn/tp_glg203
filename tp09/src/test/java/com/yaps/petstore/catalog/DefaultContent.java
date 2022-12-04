package com.yaps.petstore.catalog;

import com.yaps.petstore.catalog.domain.Category;
import com.yaps.petstore.catalog.domain.Item;
import com.yaps.petstore.catalog.domain.Product;

/**
 * Utility class for holding the content of the default test DB for DBUnit.
 */
public class DefaultContent {

    private DefaultContent() {
    }

    /**
     * The categories found in the default test setting.
     */
    public static final Category[] DEFAULT_CATEGORIES = {
            new Category("c1a", "c1b", "c1c"),
            new Category("c2a", "c2b", "c2c")
    };

    /**
     * The products found in the default test setting.
     * 
     * @throws Exception
     */

    public static final Product[] DEFAULT_PRODUCTS = {
            new Product("p1", "p1a", "p1b", DEFAULT_CATEGORIES[0]),
            new Product("p2", "p2a", "p2b", DEFAULT_CATEGORIES[0]),
            new Product("p3", "p3a", "p3b", DEFAULT_CATEGORIES[1])
    };


    public static final Item[] DEFAULT_ITEMS = {
        new Item("it1a", "it1b", 10, "it1.jpg", DEFAULT_PRODUCTS[1]),
        new Item("it2a", "it2b", 30, "it2.jpg", DEFAULT_PRODUCTS[1]),
        new Item("it3a", "it3b", 20, "it3.jpg", DEFAULT_PRODUCTS[2])
    };

}
