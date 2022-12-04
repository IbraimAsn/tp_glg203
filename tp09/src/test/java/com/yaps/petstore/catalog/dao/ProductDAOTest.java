package com.yaps.petstore.catalog.dao;

import static com.yaps.petstore.catalog.DefaultContent.DEFAULT_CATEGORIES;
import static com.yaps.petstore.catalog.DefaultContent.DEFAULT_PRODUCTS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.yaps.petstore.catalog.domain.Product;
import com.yaps.petstore.customer.config.TestDBConfig;

@DataJpaTest
@Import({ TestDBConfig.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use mysql, not an embedded DB//
                                                                             // @TestPropertySource(locations =
                                                                             // "classpath:application-test.properties")
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
public class ProductDAOTest {

    // ---------------------------------------------------------------------------------
    // - Configuration et préparation.
    // ---------------------------------------------------------------------------------

    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    EntityManager entityManager;

    // ---------------------------------------------------------------------------------
    // - Actual tests.
    // ---------------------------------------------------------------------------------

    /**
     * tests if findAll returns empty when no category
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/empty.sql")
    public void findAllEmpty() throws Exception {
        List<Product> list = productDAO.findAll();
        assertTrue(list.isEmpty(), "With no Product, findAll should return an empty list");
    }

    // findAll returns available categories when available
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void findAllSome() throws Exception {
        // test.
        List<Product> list = productDAO.findAll();
        // Note that we want to compare sets, not lists (the order is not relevant.)
        // we don't define equals for categories (because they are entities, more on
        // this later)
        // but we want to check if the list is the same as the expected one.
        // hence we use a trick : toString() depends only on content for our
        // products...

        Set<String> expectedStrings = new TreeSet<>(Arrays.stream(DEFAULT_PRODUCTS)
                .map(p -> p.toString())
                .toList());

        Set<String> computedStrings = new TreeSet<>(list.stream().map(c -> c.toString()).toList());
        assertEquals(expectedStrings, computedStrings, "findAll should return all products");
    }

    /**
     * find object with unknown id fails
     * 
     * @throws Exception
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws DataSetException
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testFindUnknowIdFail() throws Exception {
        Optional<Product> res = productDAO.findById("x");
        assertTrue(res.isEmpty(), "unknown elements should not be found");
    }

    /**
     * find object with with empty id fails
     * 
     * @throws Exception
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws DataSetException
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testFindEmptyIdFail() throws Exception {
        Optional<Product> res = productDAO.findById("");
        assertTrue(res.isEmpty(), "empty id elements should not be found");
    }

    /**
     * find object with with null id should throw an exception.
     * (they are most likely a bug in the software)
     * 
     * @throws Exception
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws DataSetException
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testFindNullIdFail() throws Exception {
        assertThrows(Exception.class,
                () -> productDAO.findById(null),
                "null id should lead to an Exception.");
    }

    /**
     * Simple insert (with everything ok).
     * 
     * Could be replaced by save() and findById() test...
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void simpleInsertTest() throws Exception {
        // Code to test..
        Product newProduct = new Product("00", "x1", "x2", DEFAULT_CATEGORIES[1]);
        productDAO.save(newProduct);
        productDAO.flush();
        // Done !
        Optional<Product> optManagedProduct = productDAO.findById("00");
        assertTrue(optManagedProduct.isPresent(), "product has been saved");
        Product managedProduct = optManagedProduct.get();
        assertAll(
                () -> assertEquals(4, productDAO.count()),
                () -> assertEquals("00", managedProduct.getId()),
                () -> assertEquals("x1", managedProduct.getName()),
                () -> assertEquals("x2", managedProduct.getDescription()),
                () -> assertEquals("c2a", managedProduct.getCategory().getId()));
    }

    // correct multiple insert
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void multipleInsertTest() throws Exception {
        // Code to test..
        Product newProduct1 = new Product("00", "01", "02", DEFAULT_CATEGORIES[1]);
        productDAO.save(newProduct1);
        Product newProduct2 = new Product("x", "x1", "x2", DEFAULT_CATEGORIES[0]);
        productDAO.save(newProduct2);
        productDAO.flush();
        assertEquals(5, productDAO.count());
    }

    /**
     * test remove Product
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testRemoveProduct() throws Exception {
        assertEquals(3, productDAO.count());
        productDAO.deleteById("p1");
        productDAO.flush();
        assertAll(
                () -> assertEquals(2, productDAO.count()),
                () -> assertTrue(productDAO.findById("p1").isEmpty(), "p1 should not be found"));
    }

    /**
     * test save invalid Product
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testSaveInvalidProduct() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    productDAO.save(new Product("", "a", "b", DEFAULT_CATEGORIES[0]));
                    productDAO.flush();
                },
                "Product with empty id should not be saved");
        checkP1NotModified();
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testSaveInvalidProduct1() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    productDAO.save(new Product("x", "", "b", DEFAULT_CATEGORIES[0]));
                    productDAO.flush();
                },
                "Product with empty name should not be saved");
        checkP1NotModified();
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testSaveInvalidProduct2() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    productDAO.save(new Product("x", "a", "", DEFAULT_CATEGORIES[0]));
                    productDAO.flush();
                },
                "Product with empty description should not be saved");
        checkP1NotModified();
    }

    /**
     * test update Product
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateProduct() throws Exception {
        // Change product p1
        productDAO.save(new Product("p1", "newName", "newDescription", DEFAULT_CATEGORIES[1]));
        productDAO.flush();
        Product p = productDAO.getReferenceById("p1");
        assertEquals("p1", p.getId());
        assertEquals("newName", p.getName());
        assertEquals("newDescription", p.getDescription());
        assertEquals("c2a", p.getCategory().getId());
    }

    // test update invalid Product
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateEmptyId() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    productDAO.save(new Product("", "aa", "bb", DEFAULT_CATEGORIES[0]));
                    productDAO.flush();
                },
                "Product with empty id should not be updated");
        checkP1NotModified();
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateEmptyDescription() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    productDAO.save(new Product("p1", "aa", "", DEFAULT_CATEGORIES[0]));
                    productDAO.flush();
                },
                "Product with empty description should not be updated");
        // Ensure we get the entry from the database...
        entityManager.clear();
        checkP1NotModified();
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateEmptyName() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    productDAO.save(new Product("p1", "", "bb", DEFAULT_CATEGORIES[0]));
                    productDAO.flush();
                },
                "Product with empty name should not be updated");
        // Ensure we get the entry from the database...
        entityManager.clear();
        checkP1NotModified();
    }

    private void checkP1NotModified() {
        Product p1 = productDAO.getReferenceById("p1");
        assertAll(
                () -> assertEquals("p1a", p1.getName(), "p1 name unchanged"),
                () -> assertEquals("p1b", p1.getDescription(), "p1 description unchanged"),
                () -> assertEquals("c1a", p1.getCategory().getId(), "p1 category unchanged"));
    }
}
