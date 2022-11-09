package com.yaps.petstore.domain.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.yaps.petstore.domain.category.Category;
import com.yaps.utils.model.CheckException;

public class ProductTest {

    Category cat = new Category("c", "c", "c");
    
    @Test
    public void testSimpleCreation() {        
        Product p = new Product("p", "p", "p", cat);
        assertEquals("c", p.getCategory().getId());
    }

    @Test
    public void testMandatoryCategory() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("p", "p", "p", null);
            p.checkData();
        });
    }

    @Test
    public void testIdNotEmpty() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("", "p", "p", cat);
            p.checkData();
        });
    }

    @Test
    public void testIdNotNull() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product(null, "p", "p", cat);
            p.checkData();
        });
    }


    @Test
    public void testNameNotEmpty() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("p", "", "p", cat);
            p.checkData();
        });
    }

    @Test
    public void testNameNotNull() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("p", null, "p", cat);
            p.checkData();
        });
    }

    @Test
    public void testDescriptionNotEmpty() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("p", "p", "", cat);
            p.checkData();
        });
    }

    @Test
    public void testDescriptionNotNull() {
        assertThrows(CheckException.class, () -> {
            Product p = new Product("p",  "p", null, cat);
            p.checkData();
        });
    }

    @Test
    public void checkToString() {
        Category cat = new Category("cat1", "name1", "descr1");
        Product p = new Product("p0", "n0", "d0", cat);
        // the category is displayed in its short form.
        String expected = "product id=p0, name=n0\ncategory=cat1\tname1\ndescription=d0";
        String computed = p.toString();
        assertEquals(expected, computed, "toString should be defined");        
    }

    @Test
    public void checkShort() {
        Category cat = new Category("cat1", "name1", "descr1");
        Product p = new Product("p0", "n0", "d0", cat);
        // the category is displayed in its short form.
        String expected = "p0\tn0";
        String computed = p.shortDisplay();
        assertEquals(expected, computed, "toString should be defined");        

    }
}
