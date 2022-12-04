package com.yaps.petstore.catalog.dao;

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
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.yaps.petstore.catalog.DefaultContent;
import com.yaps.petstore.catalog.domain.Category;
import com.yaps.petstore.customer.config.TestDBConfig;

/**
 * Test categories.
 * 
 * The content of the database can be found in class
 * {@link com.yaps.petstore.catalog.DefaultContent}
 * 
 * To avoid problems between JPA and DBUnit, we have reverted to simple JPA
 * tests
 * 
 * Note that the "standard" approach, which would be, for instance, to save
 * objects and retrieve them
 * in JPA, will fail to check if the actual SQL query is correct, as it's quite
 * likely that everything will
 * take place in memory.
 * 
 * A flush() could be a good step forward, though.
 */
@DataJpaTest
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
@Import({ TestDBConfig.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use mysql, not an embedded DB
public class CategoryDAOTest {

    // ---------------------------------------------------------------------------------
    // - Configuration et préparation.
    // ---------------------------------------------------------------------------------

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    EntityManager entityManager; // to clear cache during tests...

    // ---------------------------------------------------------------------------------
    // - Actual code.
    // ---------------------------------------------------------------------------------

    /**
     * tests if findAll returns empty when no category
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/empty.sql")
    public void findAllEmpty() throws Exception {
        List<Category> list = categoryDAO.findAll();
        assertTrue(list.isEmpty(), "With no category, findAll should return an empty list");
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testFindC1A() throws SQLException {
        Optional<Category> optC = categoryDAO.findById("c1a");
        assertTrue(optC.isPresent());
        Category c = optC.get();
        assertAll(
                () -> assertEquals("c1a", c.getId()),
                () -> assertEquals("c1b", c.getName()),
                () -> assertEquals("c1c", c.getDescription()));
    }

    // findAll returns available categories when available
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void findAllSome() throws Exception {
        // test.
        List<Category> list = categoryDAO.findAll();
        // Note that we want to compare sets, not lists (the order is not relevant.)
        // we don't define equals for categories (because they are entities, more on
        // this later)
        // but we want to check if the list is the same as the expected one.
        // hence we use a trick : toString() depends only on content for our
        // categories...
        Set<String> expectedStrings = Arrays
                .stream(DefaultContent.DEFAULT_CATEGORIES)
                .map(c -> c.toString())
                .collect(Collectors.toSet());
        Set<String> computedStrings = list.stream()
                .map(c -> c.toString())
                .collect(Collectors.toSet());
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
    @Sql("/testsql/catalog/small.sql")
    public void testFindUnknowIdFail() throws Exception {
        Optional<Category> res = categoryDAO.findById("x");
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
        Optional<Category> res = categoryDAO.findById("");
        assertTrue(res.isEmpty(), "empty id elements should not be found");
    }

    /**
     * find object with null id used to throw NPE.
     * In fact, we don't care, we want any exception.
     * (they are most likely a bug in the software)
     * 
     * We won't check this in the future : it's provided by JPA, we don't test JPA
     * implementation.
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
                () -> categoryDAO.findById(null),
                "null id should lead to InvalidDataAccessApiUsageException.");
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
        Category newCategory = new Category("00", "x1", "x2");
        categoryDAO.save(newCategory);
        categoryDAO.flush();
        // Ensure we get the entry from the database...
        entityManager.clear();

        List<Category> categories = categoryDAO.findAll();
        assertEquals(3, categories.size());
        Category cat = categoryDAO.getReferenceById("00");
        assertEquals("00", cat.getId());
        assertEquals("x1", cat.getName());
        assertEquals("x2", cat.getDescription());
    }

    // correct multiple insert
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void multipleInsertTest() throws Exception {
        // Code to test..
        Category newCategory1 = new Category("00", "01", "02");
        categoryDAO.save(newCategory1);
        Category newCategory2 = new Category("x", "x1", "x2");
        categoryDAO.save(newCategory2);
        categoryDAO.flush();
        // Ensure we get the entry from the database...
        entityManager.clear();

        assertEquals(4, categoryDAO.count(), "We should have 4 categories now");

        Set<String> expectedIds = new TreeSet<>(Arrays.asList("00", "x"));
        expectedIds.addAll(Arrays.stream(DefaultContent.DEFAULT_CATEGORIES).map(c -> c.getId()).toList());

        Set<String> computedIds = categoryDAO.findAll().stream().map(c -> c.getId()).collect(Collectors.toSet());
        assertEquals(expectedIds, computedIds);
    }

    /**
     * test remove category
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testRemoveCategory() throws Exception {
        categoryDAO.deleteById("c1a");
        assertTrue(categoryDAO.findById("c1a").isEmpty());
        assertEquals(1, categoryDAO.count());
    }

    /**
     * test save invalid category
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/empty.sql")
    public void testSaveInvalidCategory() throws Exception {
        assertThrows(ConstraintViolationException.class,
                () -> {
                    categoryDAO.save(new Category("", "a", "b"));
                    categoryDAO.flush();
                },
                "actual save should fail (JPA only validates on DB access)");
    }

    @Test
    @Sql("/testsql/empty.sql")
    public void testSaveInvalidCategory1() throws Exception {
        assertThrows(ConstraintViolationException.class,
                () -> {
                    categoryDAO.save(new Category("x", "", "b"));
                    categoryDAO.flush();
                }, "category with empty name should not be saved");
    }

    @Test
    @Sql("/testsql/empty.sql")
    public void testSaveInvalidCategory2() throws Exception {
        assertThrows(ConstraintViolationException.class,
                () -> {
                    categoryDAO.save(new Category("x", "a", ""));
                    categoryDAO.flush();
                }, "category with empty name should not be saved");

    }

    /**
     * test update category
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateCategoryV1() throws Exception {
        Category c = categoryDAO.save(new Category("c2a", "c2b", "other"));
        assertEquals("c2a", c.getId());
        assertEquals("other", c.getDescription());

    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateCategoryV2() throws Exception {
        Category c = categoryDAO.getReferenceById("c2a");
        c.setDescription("other");
        Category c2 = categoryDAO.getReferenceById("c2a");
        assertEquals("other", c2.getDescription());
        categoryDAO.save(c2);
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateEmptyDescription() throws Exception {
        assertThrows(ConstraintViolationException.class,
                () -> {
                    Category c = categoryDAO.getReferenceById("c1a");
                    c.setDescription("");
                    categoryDAO.flush();
                },
                "category with empty description should not be updated");
        // Ensure we get the entry from the database...
        entityManager.clear();
        testFindC1A();
    }

    @Test
    @Sql("/testsql/catalog/small.sql")
    public void testUpdateEmptyName() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    Category c = categoryDAO.getReferenceById("c1a");
                    c.setName("");
                    categoryDAO.flush();
                },
                "category with empty name should not be updated");
        entityManager.clear();
        testFindC1A();
    }
}
