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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemTest {
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

    Category category;
    Product product;

    @BeforeEach
    public void beforeEach() {
        category = new Category("c0", "nc0", "dc0");
        product = new Product("p0", "np0", "dp0", category);
    }

    @Test
    public void testCreateItem() {
        Item item = new Item("i0", "ni0", 100, "image1", product);
        assertEquals("i0", item.getId());
        assertEquals("ni0", item.getName());
        assertEquals("image1", item.getImagePath());
        assertEquals(100.0, item.getUnitCost(), 1e-8);
        assertEquals("np0", item.getProduct().getName());
    }

    @Test
    public void testCheckEmptyId() {

        Item item = new Item("", "a", 1000, "image", product);
        Set<ConstraintViolation<Item>> res = validator.validate(item);
        assertFalse(res.isEmpty(), "empty id illegal");
    }

    @Test
    public void testCheckEmptyName() {
            Item item = new Item("i0", "", 1000, "image", product);
            Set<ConstraintViolation<Item>> res = validator.validate(item);
            assertFalse(res.isEmpty(), "empty name illegal");
    }

    @Test
    public void testShortDisplay() {
        Item it = new Item("i0", "ni0", 100, "image", product);
        String expected = "i0\tni0";
        String computed = it.shortDisplay();
        assertEquals(expected, computed);
    }

    @Test
    public void testToString() {
        Item it = new Item("i0", "ni0", 100, "pict", product);
        String expected = "item id=i0, name=ni0, image path=pict, product=p0\tnp0, unit cost=100.0";
        String computed = it.toString();
        assertEquals(expected, computed);
    }

    @Test
    public void testNullId() throws Exception {
        assertThrows(NullPointerException.class, () -> new Item(null, "a", 0, "a", product));
    }

    @Test
    public void testNullName() throws Exception {
        assertThrows(NullPointerException.class, () -> new Item("a", null, 0, "a", product));
    }

    @Test
    public void testNullImagePath() throws Exception {
        assertThrows(NullPointerException.class, () -> new Item("a", "a", 0, null, product));
    }

    @Test
    public void testNullSetId() throws Exception {
        assertThrows(NullPointerException.class,
                () -> {
                    Item item = new Item("a", "a", 0, "a", product);
                    item.setId(null);
                });
    }

    @Test
    public void testNullSetName() throws Exception {
        assertThrows(NullPointerException.class,
                () -> {
                    Item item = new Item("a", "a", 0, "a", product);
                    item.setName(null);
                });
    }

    @Test
    public void testNullSetImagePath() throws Exception {
        assertThrows(NullPointerException.class,
                () -> {
                    Item item = new Item("a", "a", 0, "a", product);
                    item.setImagePath(null);
                });
    }

    @Test
    public void testNullSetProduct() throws Exception {
        assertThrows(NullPointerException.class,
                () -> {
                    Item item = new Item("a", "a", 0, "a", product);
                    item.setProduct(null);
                });
    }

}
