package com.yaps.petstore.domain.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yaps.petstore.domain.category.Category;
import com.yaps.petstore.domain.product.Product;
import com.yaps.utils.model.CheckException;

public class ItemTest {

    Category category;
    Product product;

    @BeforeEach
    public void beforeEach() {
        category = new Category("c0", "nc0", "dc0");    
        product = new Product("p0", "np0", "dp0", category);
    }
    
    

    @Test
    public void testCreateItem() {
        Item item = new Item("i0", "ni0", 100, product);
        assertEquals("i0", item.getId());
        assertEquals("ni0", item.getName());
        assertEquals(100.0, item.getUnitCost(), 1e-8);
        assertEquals("np0", item.getProduct().getName());        
    }

    @Test
    public void testCheck1() {
        assertThrows(CheckException.class, () -> {
            Item it = new Item(null, "a", 1000, product);
            it.checkData();
        },
        "null id illegal"
        );

        assertThrows(CheckException.class, () -> {
            Item it = new Item("", "a", 1000, product);
            it.checkData();
        },
        "empty id illegal"
        );

        assertThrows(CheckException.class, () -> {
            Item it = new Item("i0", null, 1000, product);
            it.checkData();
        },
        "null name  illegal"
        );

        assertThrows(CheckException.class, () -> {
            Item it = new Item("i0", "", 1000, product);
            it.checkData();
        },
        "empty name illegal"
        );


        assertThrows(CheckException.class, () -> {
            Item it = new Item("i0", "np", 1000, null);
            it.checkData();
        },
        "null product  illegal"
        );
    }

    @Test
    public void testShortDisplay() {
        Item it = new Item("i0", "ni0", 100, product);
        String expected = "i0\tni0";
        String computed = it.shortDisplay();
        assertEquals(expected, computed);        
    }

    @Test
    public void testToString() {
        Item it = new Item("i0", "ni0", 100, product);
        String expected = "item id=i0, name=ni0, product=p0\tnp0, unit cost=100.0";
        String computed = it.toString();
        assertEquals(expected, computed);        
    }

}
