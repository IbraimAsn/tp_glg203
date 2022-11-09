package com.yaps.petstore.domain.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.yaps.utils.model.CheckException;

public class CategoryTest {

    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testCreateCategoryWithInvalidValues() throws Exception {

        // Creates an object with empty values
        try {
            final Category category = new Category(new String(), new String(), new String());
            category.checkData();
            fail("Object with empty values should not be created");
        } catch (CheckException e) {
        }

        // Creates an object with null values
        try {
            final Category category = new Category(null, null, null);
            category.checkData();
            fail("Object with null values should not be created");
        } catch (CheckException e) {
        }
    }

    @Test
    public void checkToString() {
        Category cat = new Category("cat1", "name1", "descr1");
        String expected = "category id=cat1, name=name1\ndescription=descr1";
        String computed = cat.toString();
        assertEquals(expected, computed, "toString should be defined");        
    }

    @Test
    public void checkShort() {
        Category cat = new Category("cat1", "name1", "descr1");
        String expected = "cat1\tname1";
        String computed = cat.shortDisplay();
        assertEquals(expected, computed, "shortDisplay should be defined");        
    }
}
