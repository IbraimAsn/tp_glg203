package com.yaps.petstore.catalog.dao;

import static com.yaps.petstore.catalog.DefaultContent.DEFAULT_PRODUCTS;
import static com.yaps.petstore.catalog.DefaultContent.DEFAULT_ITEMS;
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

import com.yaps.petstore.catalog.domain.Item;
import com.yaps.petstore.catalog.domain.Product;
import com.yaps.petstore.customer.config.TestDBConfig;

@DataJpaTest
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
@Import({ TestDBConfig.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use mysql, not an embedded DB
public class ItemDAOTest  {

    // ---------------------------------------------------------------------------------
    // - Configuration et préparation.
    // ---------------------------------------------------------------------------------


    @Autowired
    ItemDAO dao;

    @Autowired
    CategoryDAO categoryDao;

    @Autowired
    ProductDAO productDao;

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
        List<Item> list = dao.findAll();
        assertTrue(list.isEmpty(), "With no Item, findAll should return an empty list");
    }

    // findAll returns available categories when available
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void findAllSome() throws Exception {
        // test.
        List<Item> list = dao.findAll();
        // Note that we want to compare sets, not lists (the order is not relevant.)
        // we don't define equals for categories (because they are entities, more on
        // this later)
        // but we want to check if the list is the same as the expected one.
        // hence we use a trick : toString() depends only on content for our
        // products...

        // sorted set errors will be easier to read...
        Set<String> expectedStrings = new TreeSet<>(Arrays.stream(DEFAULT_ITEMS)
                .map(p -> p.toString()).toList()
        );

        Set<String> computedStrings = new TreeSet<>(list.stream().map(c -> c.toString()).toList());
        assertEquals(expectedStrings, computedStrings,
                "findAll should return all items");
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
        Optional<Item> res = dao.findById("x");
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
        Optional<Item> res = dao.findById("");
        assertTrue(res.isEmpty(), "empty id elements should not be found");
    }

    /**
     * find object with with null id should throw and exception.
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
                () -> dao.findById(null),
                "null id should lead to exception.");
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
        String productId = DEFAULT_PRODUCTS[1].getId();
        Product p = productDao.getReferenceById(productId);
        // Code to test..
        Item newItem = new Item("00i", "name of 00i", 1000, "image1", p);
        dao.save(newItem);
        dao.flush();
        assertEquals(4, dao.count());
        Item item = dao.getReferenceById("00i");
        assertEquals("image1", item.getImagePath());
        assertEquals(productId, item.getProduct().getId());
    }

    // correct multiple insert
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void multipleInsertTest() throws Exception {
        // Code to test..
        Item newItem1 = new Item("00i", "name of 00i", 1000, "image1", DEFAULT_PRODUCTS[2]);
        dao.save(newItem1);
        Item newItem2 = new Item("x00", "name of 00i", 1000, "image2", DEFAULT_PRODUCTS[2]);
        dao.save(newItem2);
        dao.flush();
        assertTrue( dao.findById("00i").isPresent());
        assertTrue( dao.findById("x00").isPresent());
        assertEquals(5, dao.count())   ;
    }

    /**
     * test remove Product
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testRemoveProduct() throws Exception {
        assertTrue(dao.findById("it1a").isPresent());
        dao.deleteById("it1a");
        dao.flush();
        assertTrue(dao.findById("it1a").isEmpty());
    }


    /**
     * test save invalid Product
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/empty.sql")
    public void testSaveInvalidProduct() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    dao.save(new Item("", "a", 10, "image1", DEFAULT_PRODUCTS[0]));
                    dao.flush();
                },
                "items with empty id should not be saved");
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testSaveInvalidProduct1() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    dao.save(new Item("dsfds", "", 10, "image1", DEFAULT_PRODUCTS[0]));
                    dao.flush();
                },
                "Product with empty name should not be saved");
    }

    /**
     * test update Product
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateItem() throws Exception {
        Product p = productDao.getReferenceById(DEFAULT_PRODUCTS[2].getId());
        dao.save(new Item("it1a", "it1b", 300, "it1.jpg", p));        
        dao.flush();
        Item item = dao.getReferenceById("it1a");
        assertEquals(300, item.getUnitCost(), 1e-10);
        assertEquals(DEFAULT_PRODUCTS[2].getDescription(), item.getProduct().getDescription());
    }

    // test update invalid Product
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateEmptyId() throws Exception {
        assertThrows(Exception.class,
                () -> 
                {
                    dao.save(new Item("", "newName", 300, "image1", DEFAULT_PRODUCTS[2]));
                    dao.flush();
                },
                "items with empty id should not be updated");
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateEmptyDescription() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    dao.save(new Item("ib", "", 300, "image1", DEFAULT_PRODUCTS[2]));
                    dao.flush();
                },
                "Items with empty names should not be updated");
    }

}
