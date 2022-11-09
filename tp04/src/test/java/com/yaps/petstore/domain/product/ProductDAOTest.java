package com.yaps.petstore.domain.product;

import com.yaps.petstore.AbstractDBUnitTestCase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;

import static com.yaps.petstore.domain.DefaultContent.*;

public class ProductDAOTest extends AbstractDBUnitTestCase {

    /**
     * tests if findAll returns empty when no category
     * 
     * @throws Exception
     */
    @Test
    public void findAllEmpty() throws Exception {
        setupEmptyContent();
        ProductDAO dao = buildDAO();
        List<Product> list = dao.findAll();
        assertTrue(list.isEmpty(), "With no Product, findAll should return an empty list");
    }

    // findAll returns available categories when available
    @Test
    public void findAllSome() throws Exception {
        // test.
        List<Product> list = buildDAO().findAll();
        // Note that we want to compare sets, not lists (the order is not relevant.)
        // we don't define equals for categories (because they are entities, more on
        // this later)
        // but we want to check if the list is the same as the expected one.
        // hence we use a trick : toString() depends only on content for our
        // products...

        Set<String> expectedStrings = Arrays.stream(DEFAULT_PRODUCTS)
                .map(p -> p.toString())
                .collect(Collectors.toSet());

        Set<String> computedStrings = list.stream().map(c -> c.toString()).collect(Collectors.toSet());
        assertEquals(expectedStrings, computedStrings, "findAll should return all categories");
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
    public void testFindUnknowIdFail() throws DataSetException, DatabaseUnitException, SQLException, Exception {
        Optional<Product> res = buildDAO().findById("x");
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
    public void testFindEmptyIdFail() throws DataSetException, DatabaseUnitException, SQLException, Exception {
        Optional<Product> res = buildDAO().findById("");
        assertTrue(res.isEmpty(), "empty id elements should not be found");
    }

    /**
     * find object with with null id should throw NPE.
     * (they are most likely a bug in the software)
     * 
     * @throws Exception
     * @throws SQLException
     * @throws DatabaseUnitException
     * @throws DataSetException
     */
    @Test
    public void testFindNullIdFail() throws DataSetException, DatabaseUnitException, SQLException, Exception {
        ProductDAO dao = buildDAO();
        assertThrows(NullPointerException.class,
                () -> dao.findById(null),
                "null id should lead to NPE.");
    }

    /**
     * Simple insert (with everything ok).
     * 
     * Could be replaced by save() and findById() test...
     * 
     * @throws Exception
     */
    @Test
    public void simpleInsertTest() throws Exception {
        ProductDAO dao = buildDAO();
        // Code to test..
        Product newProduct = new Product("00", "x1", "x2", DEFAULT_CATEGORIES[1]);
        dao.save(newProduct);
        // Done !
        // note : l'ordre est important, c'est celui des clefs...
        IDataSet expectedDataSet = buildIDataSet(
                "<product id='00' name='x1' description='x2' category_fk='b'/>"
                        + getStartDatasetContent());
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("product");
        ITable expectedTable = expectedDataSet.getTable("product");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // correct multiple insert
    @Test
    public void multipleInsertTest() throws Exception {
        ProductDAO dao = buildDAO();
        // Code to test..
        Product newProduct1 = new Product("00", "01", "02", DEFAULT_CATEGORIES[1]);
        dao.save(newProduct1);
        Product newProduct2 = new Product("x", "x1", "x2", DEFAULT_CATEGORIES[0]);
        dao.save(newProduct2);

        // Done !
        // note : l'ordre est important, c'est celui des clefs...
        IDataSet expectedDataSet = buildIDataSet(
                "<Product id='00' name='01' description='02' category_fk='b'/>"
                        + getStartDatasetContent()
                        + "<Product id='x' name='x1' description='x2' category_fk='a'/>");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("product");
        ITable expectedTable = expectedDataSet.getTable("product");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // insert error if id is known
    @Test
    public void testSaveErrorIfDuplicate() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(DuplicateKeyException.class, () -> {
            dao.save(new Product("pa", "aaaaaa", "bbbbbbb", DEFAULT_CATEGORIES[0]));
        });
    }

    /**
     * test remove Product
     * 
     * @throws Exception
     */
    @Test
    public void testRemoveProduct() throws Exception {
        ProductDAO dao = buildDAO();
        dao.remove("pa");
        IDataSet expectedDataSet = buildIDataSet(
            String.join(" ", PRODUCT_XML[1] + PRODUCT_XML[2]));                
        ITable expectedTable = expectedDataSet.getTable("product");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("product");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // test remove absent Product
    @Test
    public void testRemoveAbsentProduct() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(ObjectNotFoundException.class,
                () -> dao.remove("x"));
        checkNotModified();
    }

    /**
     * test save invalid Product
     * 
     * @throws Exception
     */
    @Test
    public void testSaveInvalidProduct() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.save(new Product("", "a", "b", DEFAULT_CATEGORIES[0])),
                "Product with empty id should not be saved");
        checkNotModified();
    }

    @Test
    public void testSaveInvalidProduct1() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.save(new Product("x", "", "b", DEFAULT_CATEGORIES[0])),
                "Product with empty name should not be saved");
        checkNotModified();
    }

    @Test
    public void testSaveInvalidProduct2() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.save(new Product("x", "a", "", DEFAULT_CATEGORIES[0])),
                "Product with empty description should not be saved");
        checkNotModified();
    }

    /**
     * test update Product
     * 
     * @throws Exception
     */
    @Test
    public void testUpdateProduct() throws Exception {
        ProductDAO dao = buildDAO();
        // Change product pb
        dao.update(new Product("pb", "newName", "newDescription", DEFAULT_CATEGORIES[0]));
        IDataSet expectedDataSet = buildIDataSet(
                String.join(" ", 
                PRODUCT_XML[0],
                "<product id='pb' name='newName' description='newDescription' category_fk='a'/>", 
                 PRODUCT_XML[2]));
        ITable expectedTable = expectedDataSet.getTable("product");
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("product");
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);
    }

    // test update invalid Product
    @Test
    public void testUpdateEmptyId() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.update(new Product("", "aa", "bb", DEFAULT_CATEGORIES[0])),
                "Product with empty id should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateEmptyDescription() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.update(new Product("a", "aa", "", DEFAULT_CATEGORIES[0])),
                "Product with empty description should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateEmptyName() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(CheckException.class,
                () -> dao.update(new Product("a", "", "bb", DEFAULT_CATEGORIES[0])),
                "Product with empty name should not be updated");
        checkNotModified();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        ProductDAO dao = buildDAO();
        assertThrows(ObjectNotFoundException.class,
                () -> dao.update(new Product("someid", "aa", "bb", DEFAULT_CATEGORIES[0])),
                "Product with unknown id should not be updated");
        checkNotModified();
    }

    // ---------------------------------------------------------------------------------
    // - Setup and teardown code
    // ---------------------------------------------------------------------------------
    @Override
    protected String getStartDatasetContent() {
        return String.join(" ", CATEGORY_XML)
            + " " + String.join(" ", PRODUCT_XML);        
    }

    @BeforeEach
    @Override
    protected void setup() throws Exception {
        super.setup();
    }

    @AfterEach
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    // ---------------------------------------------------------------------------------
    // - Utility functions
    // ---------------------------------------------------------------------------------

    /**
     * Creates a DAO.
     * 
     * @return
     * @throws Exception
     */
    private ProductDAO buildDAO() throws Exception {
        return new ProductDAO(connection());
    }

    /**
     * Sets the content for this DAO type to EMPTY.
     * 
     * @throws DatabaseUnitException
     * @throws SQLException
     * @throws Exception
     */
    public void setupEmptyContent() throws DatabaseUnitException, SQLException, Exception {
        // Fix the database content for this test.
        String initialContent = 
            String.join(" ", CATEGORY_XML)
            + " <product/>";
        IDataSet someDataSet = buildIDataSet(initialContent);
        DatabaseOperation.CLEAN_INSERT.execute(databaseTester.getConnection(), someDataSet);
    }

    /**
     * Checks if the current table has still its original content.
     * 
     * @throws Exception
     * @throws SQLException
     */
    private void checkNotModified() throws SQLException, Exception {
        IDataSet expectedDataSet = buildIDataSet(
                getStartDatasetContent());
        assertCurrentTableContentCorrespondsFor(expectedDataSet, "category");
        assertCurrentTableContentCorrespondsFor(expectedDataSet, "product");
    }

}
