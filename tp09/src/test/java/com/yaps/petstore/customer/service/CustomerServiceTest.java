package com.yaps.petstore.customer.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.yaps.petstore.customer.DefaultContent;
import com.yaps.petstore.customer.dao.CustomerDAO;
import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.customer.dto.AddressDTO;
import com.yaps.petstore.customer.dto.CustomerCreationDTO;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.security.dao.YapsUserDAO;
import com.yaps.petstore.security.domain.YapsUser;
import com.yaps.petstore.security.domain.YapsUserRole;
import com.yaps.petstore.security.exception.UsernameAlreadyExistsException;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
// Note : On n'est pas franchement dans du test d'intégration,
// mais l'annotation @SpringBootTest est faite pour cela.
//
// On utilise MOCK, et non plus NONE, parce que sinon, il proteste à propose de
// la sécurité.
// Nous améliorerons cela dans une version ultérieure.
//
// active application-test.properties en PLUS de application.properties
@ActiveProfiles("test")
// Sur un jeu de tests, @Transactional déclenche un rollback après le test, quel
// que soit
// sont résultat.
// Note : ça ne fonctionne pas bien sur les tests webclient, parce que la
// transaction est en dehors
// du test lui-même.
@Transactional
// Remarque générale sur l'utilisation de @Sql dans ces tests :
// @Sql posé sur une classe de tests est joué UNE SEULE FOIS avant tous les
// tests.
// Il n'est pas très intéressant, donc.
// @Sql posé sur une méthode de tests est joué avant le test
// (on peut aussi le faire jouer après le test, en passant des arguments)
// En revanche, il n'y a pas moyen de mettre en place un système pour fixer un code SQL
// global, qui serait joué avant chacun des tests (comme @BeforeEach pour le code java)
public class CustomerServiceTest {

    CustomerDTO c1, c2;

    @BeforeEach
    public void init() {
        c1 = CustomerDTOMapper.toDTO(DefaultContent.DEFAULT_CUSTOMER[0]);
        c2 = CustomerDTOMapper.toDTO(DefaultContent.DEFAULT_CUSTOMER[1]);
    }

    @Autowired
    CustomerService service;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    YapsUserDAO yapsUserDAO;

   
    @Test
    @Sql({
            "/testsql/customer/empty.sql"
    })
    public void testFindAllEmpty() {
        List<CustomerDTO> customers = service.findAll();
        assertNotNull(customers);
        assertEquals(0, customers.size());
    }

    @Test
    @Sql({
            "/testsql/customer/two.sql"
    })
    public void testFindAllTwo() {

        Set<String> expected = new TreeSet<>(Arrays.asList(c1.toString(), c2.toString()));
        Set<String> computed = new TreeSet<>(service.findAll().stream().map(c -> c.toString()).toList());
        assertEquals(expected, computed);
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testFindById() {
        Optional<CustomerDTO> optC = service.findById("2");
        assertAll(
                () -> optC.isPresent(),
                () -> optC.get().equals(c2));
    }

    @Test
    @Sql("/testsql/customer/empty.sql")
    public void testSave() throws UsernameAlreadyExistsException {
        CustomerCreationDTO c0 = new CustomerCreationDTO();
        c0.setUsername("logina");
        c0.setFirstname("loga");
        c0.setFirstname("a");
        c0.setLastname("b");
        c0.setPassword("pwd1");
        String res = service.save(c0);
        assertNotNull(res);
        // res représente un entier :
        assertDoesNotThrow(
                () -> Integer.parseInt(res));
        Customer checkCustomer = customerDAO.findById(res).get();
        assertEquals("a", checkCustomer.getFirstname());
        assertEquals("b", checkCustomer.getLastname());

        Optional<YapsUser> optUser = yapsUserDAO.findById("logina");
        assertTrue(optUser.isPresent(), "un utilisateur doit être créé pour chaque client");
        assertEquals(YapsUserRole.CUST, optUser.get().getRole(), "les clients doivent avoir les droits clients");
        String bcrypt = "{bcrypt}";
        String pwd = optUser.get().getUserPassword();
        assertTrue(pwd.length() > bcrypt.length());
        assertEquals(bcrypt, pwd.substring(0, bcrypt.length()), "le mot de passe doit être en bcrypt");

    }

    @Test
    @Sql("/testsql/customer/empty.sql")
    public void testTwoSaves() throws UsernameAlreadyExistsException {
        CustomerCreationDTO c0 = // new CustomerCreationDTO("loga", "a", "b", "pwd1");
                new CustomerCreationDTO("loga", "pwd1", "a", "b",
                        "", "", new AddressDTO());
        String res1 = service.save(c0);
        CustomerCreationDTO c1 = // new CustomerCreationDTO("logb", "c", "d", "pwd2");
                new CustomerCreationDTO("logb", "pwd2", "c", "d",
                        "", "", new AddressDTO());
        String res2 = service.save(c1);
        assertTrue(res1.compareTo(res2) < 0, "Ids should increase");
    }

    @Test
    @Sql("/testsql/customer/empty.sql")
    public void testFindyId() throws UsernameAlreadyExistsException {
        CustomerCreationDTO c0 = new CustomerCreationDTO("loga", "pwd1", "a", "b",
                "", "", new AddressDTO());
        String res1 = service.save(c0);
        CustomerCreationDTO c1 = new CustomerCreationDTO("logb", "pwd2", "c", "d",
                "", "", new AddressDTO());
        String res2 = service.save(c1);
        Optional<CustomerDTO> c3 = service.findById(res1);
        assertFalse(c3.isEmpty());
        assertEquals("a", c3.get().getFirstname());
        assertTrue(res1.compareTo(res2) < 0, "Ids should increase");
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testFindNone() {
        Optional<CustomerDTO> c3 = service.findById("nope");
        assertTrue(c3.isEmpty(), "Unknown elements should not be found");
    }


    @Test
    @Sql("/testsql/customer/two.sql")
    public void testSaveAlreadyExistsAsEmployee() {
        
        CustomerCreationDTO customerDTO = new CustomerCreationDTO(
            "e1", "pwd1", "a",
            "b", "", "", new AddressDTO());
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            service.save(customerDTO);
        });

        // Ensure the user is still the same.
        YapsUser user = yapsUserDAO.getReferenceById("e1");
        assertEquals(YapsUserRole.EMP, user.getRole());
    }

    @Test
    @Sql("/testsql/customer/two.sql")
    public void testSaveAlreadyExistsAsCustomer() {
        
        CustomerCreationDTO customerDTO = new CustomerCreationDTO(
            "u1", "pwd1", "a",
            "b", "", "", new AddressDTO());
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            service.save(customerDTO);
        });
        // Ensure the original customer is still here...

        List<Customer> customers = customerDAO.findByUsername("u1");
        assertEquals(1, customers.size());
        Customer customer = customers.get(0);
        assertEquals("fn1", customer.getFirstname());
    }

}
