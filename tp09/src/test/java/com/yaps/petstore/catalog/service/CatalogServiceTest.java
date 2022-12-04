package com.yaps.petstore.catalog.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.yaps.petstore.catalog.dto.CategoryDTO;
import com.yaps.petstore.catalog.dto.ItemDTO;
import com.yaps.petstore.catalog.dto.ProductDTO;

/**
 * This class tests the CatalogService class.
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@ActiveProfiles({"test"}) // active application-test.properties en PLUS de application.properties
@Sql(
// Note : cette annotation devra être déplacée vers les méthodes
// de tests si celles-ci ont besoin de bases différentes
// (par exemple qu'on teste sur des données vides)
"/testsql/catalog/small.sql")
public class CatalogServiceTest  {
	
    @Autowired
    CatalogService catalogService;

    public void name() {
        catalogService.findItemsForProduct(null);
        catalogService.findProduct(null);
        catalogService.findProductsForCategory(null);
    }

    @Test
    public void testFindCategoryNotFound() {
        Optional<CategoryDTO> res = catalogService.findCategory("none");
        assertTrue(res.isEmpty(), "Sould return empty when not found");
    }

    @Test
    public void testFindCategory() {
        Optional<CategoryDTO> res = catalogService.findCategory("c1a");
        assertTrue(res.isPresent());
        CategoryDTO cat = res.get();
        assertAll(
            () -> assertEquals("c1a", cat.getId()),
            () -> assertEquals("c1b", cat.getName()),
            () -> assertEquals("c1c", cat.getDescription())
        );
    }

    @Test
    public void testFindItemNotFound() {
        Optional<ItemDTO> optItem = catalogService.findItem("none");
        assertTrue(optItem.isEmpty(), "Sould return empty when not found");

    }

    @Test
    public void testFindProductNotFound() {
        Optional<ProductDTO> optItem = catalogService.findProduct("none");
        assertTrue(optItem.isEmpty(), "Sould return empty when not found");

    }

    @Test
    public void testFindItem() {
        Optional<ItemDTO> optItem = catalogService.findItem("it1a");
        // insert into item  values ('it1a', 'it1b', '10.00', 'p1', 'it1.jpg');
        assertTrue(optItem.isPresent());
        ItemDTO item = optItem.get();
        assertAll(
            () -> assertEquals("it1a", item.getId()),
            () -> assertEquals("it1b", item.getName()),
            () -> assertEquals("it1.jpg", item.getImagePath()),
            () -> assertEquals(10.0, item.getUnitCost(), 1e-10),
            () -> assertEquals("p2", item.getProductId()),
            () -> assertEquals("p2a", item.getProductName()),
            () -> assertEquals("p2b", item.getProductDescription())
        );
    }

    @Test
    public void testFindProduct() {
        
    }


}

