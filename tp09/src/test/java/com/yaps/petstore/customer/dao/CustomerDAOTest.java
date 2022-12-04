package com.yaps.petstore.customer.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.yaps.petstore.customer.DefaultContent;
import com.yaps.petstore.customer.config.TestDBConfig;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.security.dao.YapsUserDAO;
import com.yaps.petstore.security.domain.YapsUser;
import com.yaps.petstore.security.domain.YapsUserRole;

@DataJpaTest
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
@Import({ TestDBConfig.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use mysql, not an embedded DB
public class CustomerDAOTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CustomerDAO customerDAO;

    // Customers are supposed to be linked with users,
    // Hence a dependency which prevents pure unit tests here.
    @Autowired
    YapsUserDAO userDAO;


    // ---------------------------------------------------------------------------------
    // - Actual code.
    // ---------------------------------------------------------------------------------

    // We tried to use a constructor, but JUnit and Spring get in the way.
    // Hence we use autowire.

    /**
     * tests if findAll returns empty when no Customer
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/empty.sql")
    public void findAllEmpty() throws Exception {
        List<Customer> list = customerDAO.findAll();
        assertTrue(list.isEmpty(), "With no Customer, findAll should return an empty list");
    }

    // findAll returns available categories when available
    @Test
    @Sql("/testsql/customer/two.sql")
    public void findAllSome() throws Exception {
        // test.
        List<Customer> list = customerDAO.findAll();
        // Note that we want to compare sets, not lists (the order is not relevant.)
        // we don't define equals for categories (because they are entities, more on
        // this later)
        // but we want to check if the list is the same as the expected one.
        // hence we use a trick : toString() depends only on content for our
        // categories...
        Set<String> expectedStrings = Arrays
                .stream(DefaultContent.DEFAULT_CUSTOMER)
                .map(c -> c.toString())
                .collect(Collectors.toSet());
        Set<String> computedStrings = list.stream()
                .map(c -> c.toString())
                .collect(Collectors.toSet());
        assertEquals(expectedStrings, computedStrings, "findAll should return all customers");
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
    @Sql("/testsql/customer/two.sql")
    public void testFindUnknowIdFail() throws Exception {
        Optional<Customer> res = customerDAO.findById("x");
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
    @Sql("/testsql/customer/two.sql")
    public void testFindEmptyIdFail() throws Exception {
        Optional<Customer> res = customerDAO.findById("");
        assertTrue(res.isEmpty(), "empty id elements should not be found");
    }

    /**
     * find object with with null id should throw an exception.
     * (they are most likely a bug in the software)
     * 
     * @throws Exception
     * 
     */
    @Test
    @Sql("/testsql/customer/two.sql")
    public void testFindNullIdFail() throws Exception {
        assertThrows(Exception.class,
                () -> customerDAO.findById(null),
                "null id should lead to exception.");
    }

    /**
     * Simple insert (with everything ok).
     * @throws Exception
     */
    @Test
    @Sql("/testsql/customer/two.sql")
    public void simpleInsertTest() throws Exception {
        YapsUser user = new YapsUser("l1", YapsUserRole.CUST, "pwd1");
        userDAO.save(user);
        // Code to test..
        Customer newCustomer = new Customer("0", "a", "b",
                "c", "d@toto.com", "e", "f", "g", "h", "i", "j", user);
        customerDAO.save(newCustomer);
        customerDAO.flush();
        Optional<Customer> optCustomer = customerDAO.findById("0");
        assertTrue(optCustomer.isPresent());
        final Customer c = optCustomer.get();
        assertAll(
                () -> assertEquals("0", c.getId()),
                () -> assertEquals("a", c.getFirstname()),
                () -> assertEquals("b", c.getLastname()),
                () -> assertEquals("c", c.getTelephone()),
                () -> assertEquals("d@toto.com", c.getEmail()),
                () -> assertEquals("e", c.getStreet1()),
                () -> assertEquals("f", c.getStreet2()),
                () -> assertEquals("g", c.getCity()),
                () -> assertEquals("h", c.getState()),
                () -> assertEquals("i", c.getZipcode()),
                () -> assertEquals("j", c.getCountry()));
    }

    // correct multiple insert
    @Test
    @Sql("/testsql/customer/two.sql")
    public void multipleInsertTest() throws Exception {
        // Code to test..
        YapsUser user1 = new YapsUser("l1", YapsUserRole.CUST, "pwd1");
        YapsUser user2 = new YapsUser("l2", YapsUserRole.CUST, "pwd2");
        userDAO.save(user1);
        userDAO.save(user2);
        
        Customer newCustomer1 = new Customer("0", "n0", "n0", "", "", "", "", "", "", "", "", user1);
        customerDAO.save(newCustomer1);
        Customer newCustomer2 = new Customer("x", "nx", "n0", "", "", "", "", "", "", "", "", user2);
        customerDAO.save(newCustomer2);
        customerDAO.flush();
        assertEquals(4, customerDAO.count(), "Two customers should have been added");
    }

    /**
     * test remove Customer
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/customer/two.sql")
    public void testRemoveCustomer() throws Exception {
        customerDAO.deleteById("2");
        customerDAO.flush();
        assertTrue(customerDAO.findById("2").isEmpty(), "Customer 2 should have been removed");
    }

    /**
     * test save invalid Customer
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/customer/two.sql")
    public void testSaveInvalidCustomer() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    YapsUser user1 = new YapsUser("l1", YapsUserRole.CUST);
                    userDAO.save(user1);
            
                    customerDAO.save(new Customer("", "a", "b", user1));
                    customerDAO.flush();
                }, "Customer with empty id should not be saved");
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testSaveCustomerWithoutUser() throws Exception {
        assertThrows(Exception.class,
                () -> {            
                    customerDAO.save(new Customer("", "a", "b", null));
                    customerDAO.flush();
                }, "Customer with no linked user should not be saved");
    }


    @Test
    @Sql("/testsql/customer/two.sql")
    public void testSaveInvalidCustomer1() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    YapsUser user1 = new YapsUser("l1", YapsUserRole.CUST);
                    userDAO.save(user1);
                    customerDAO.save(new Customer("x", "", "b", user1));
                    customerDAO.flush();
                },
                "Customer with empty name should not be saved");
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testSaveInvalidCustomer2() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    YapsUser user1 = new YapsUser("l1", YapsUserRole.CUST);
                    userDAO.save(user1);
                    customerDAO.save(new Customer("x", "a", "", user1));
                    customerDAO.flush();
                },
                "Customer with empty description should not be saved");
    }

    /**
     * test update Customer
     * 
     * @throws Exception
     */
    @Test
    @Sql("/testsql/customer/two.sql")
    public void testUpdateCustomer() throws Exception {
        YapsUser user1 = new YapsUser("l1", YapsUserRole.CUST, "pwd1");
        userDAO.save(user1);
        customerDAO.save(
                new Customer("2", "m", "n",
                        "o", "p@toto.com", "q", "r",
                        "s", "t", "u", "v", user1));
        customerDAO.flush();
        Customer c = customerDAO.getReferenceById("2");
        assertAll(
            () -> assertEquals("2", c.getId()),
            () -> assertEquals("m", c.getFirstname()),
            () -> assertEquals("n", c.getLastname()),
            () -> assertEquals("o", c.getTelephone()),
            () -> assertEquals("p@toto.com", c.getEmail()),
            () -> assertEquals("q", c.getStreet1()),
            () -> assertEquals("r", c.getStreet2()),
            () -> assertEquals("s", c.getCity()),
            () -> assertEquals("t", c.getState()),
            () -> assertEquals("u", c.getZipcode()),
            () -> assertEquals("v", c.getCountry()));
    }

    // test update invalid Customer
    @Test
    @Sql("/testsql/customer/two.sql")
    public void testUpdateEmptyId() throws Exception {
        assertThrows(Exception.class,
                () -> {
                    YapsUser user1 = new YapsUser("l1", YapsUserRole.CUST);
                    userDAO.save(user1);            
                    customerDAO.save(new Customer("", "aa", "bb", user1));
                    customerDAO.flush();
                },
                "Customer with empty id should not be updated");
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testUpdateEmptyDescription() throws Exception {

        assertThrows(Exception.class,
                () -> {
                    YapsUser user1 = new YapsUser("l1", YapsUserRole.CUST);
                    userDAO.save(user1);            
                    customerDAO.save(new Customer("a", "aa", "", user1));
                    customerDAO.flush();
                },
                "Customer with empty description should not be updated");
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testUpdateEmptyName() throws Exception {

        assertThrows(Exception.class,
                () -> {
                    YapsUser user1 = new YapsUser("l1", YapsUserRole.CUST);
                    userDAO.save(user1);            
                    customerDAO.save(new Customer("a", "", "bb", user1));
                    customerDAO.flush();
                },
                "Customer with empty name should not be updated");
    }

}
