package com.yaps.petstore.catalog.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class CategoryTest {

    // ----------------------------------------------
    // For testing validation annotations
    // -----------------------------------------------
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    // ----------------------------------------------
    // Actual tests
    // -----------------------------------------------

    /**
     * This test tries to create an object with a invalid values.
     */
    @Test
    public void testCreateCategoryWithInvalidValues() throws Exception {
        final Category category = new Category(new String(), new String(), new String());
        Set<ConstraintViolation<Category>> res = validator.validate(category);
        assertFalse(res.isEmpty(), "Object with empty values are not valid");
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

    @Test
    public void testNullId2() {
        assertThrows(NullPointerException.class,
                () -> new Category(null, "a", "a"));
    }

    @Test
    public void testNullName() {
        assertThrows(NullPointerException.class,
                () -> new Category("id1", null, "a"));
    }

    @Test
    public void testNullDescr() {
        assertThrows(NullPointerException.class,
                () -> new Category("id1", "dsfdf", null));
    }

    @Test
    public void testNullSetId() {
        assertThrows(NullPointerException.class,
                () -> {
                    Category cat = new Category("a", "a", "a");
                    cat.setId(null);
                });
    }

    @Test
    public void testNullSetName() {
        assertThrows(NullPointerException.class,
                () -> {
                    Category cat = new Category("a", "a", "a");
                    cat.setName(null);
                });
    }

    @Test
    public void testNullSetDescription() {
        assertThrows(NullPointerException.class,
                () -> {
                    Category cat = new Category("a", "a", "a");
                    cat.setDescription(null);
                });
    }

}
