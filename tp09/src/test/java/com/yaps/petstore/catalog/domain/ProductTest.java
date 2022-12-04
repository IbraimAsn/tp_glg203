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

public class ProductTest {
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

    Category category = new Category("c", "c", "c");

    @Test
    public void testSimpleCreation() {
        Product p = new Product("p", "p", "p", category);
        assertEquals("c", p.getCategory().getId());
    }

    @Test
    public void testIdNotEmpty() {
        Product product = new Product("", "p", "p", category);
        Set<ConstraintViolation<Product>> res = validator.validate(product);
        assertFalse(res.isEmpty(), "empty id illegal");

    }

    @Test
    public void testNameNotEmpty() {
        Product product = new Product("p", "", "p", category);
        Set<ConstraintViolation<Product>> res = validator.validate(product);
        assertFalse(res.isEmpty(), "empty name illegal");
    }

    @Test
    public void testDescriptionNotEmpty() {
        Product product = new Product("p", "p", "", category);
        Set<ConstraintViolation<Product>> res = validator.validate(product);
        assertFalse(res.isEmpty(), "empty description illegal");
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

    @Test
    public void testNullId() {
        assertThrows(NullPointerException.class,
                () -> new Product(null, "a", "a", category));
    }

    @Test
    public void testNullName() {
        assertThrows(NullPointerException.class,
                () -> new Product("a", null, "a", category));
    }

    @Test
    public void testNullDescription() {
        assertThrows(NullPointerException.class,
                () -> new Product("a", "a", null, category));
    }

    @Test
    public void testNullCategory() {
        assertThrows(NullPointerException.class,
                () -> new Product("a", "a", "a", null));
    }

}
