package com.yaps.petstore.customer.ui;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.opentest4j.MultipleFailuresError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.yaps.petstore.catalog.service.CatalogService;
import com.yaps.petstore.config.LoggerConfig;
import com.yaps.petstore.config.WebSecurityConfig;
import com.yaps.petstore.customer.dto.AddressDTO;
import com.yaps.petstore.customer.dto.CustomerCreationDTO;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerService;
import com.yaps.petstore.security.exception.UsernameAlreadyExistsException;
import com.yaps.petstore.security.service.YapsUserService;

/**
 * Tests for Create Customer Servlet.
 * 
 * <p>
 * We had a problem with @MockBean : save() is supposed to throw an error
 * which is in fact thrown by customer.checkData(). This contract does not
 * respect encapsulation rules, and it shows here.
 * 
 * <p>
 * Actual integration tests (with the full database) might be somehow easier to
 * perform (the problem being : how to set the whole application in a known
 * state ?).
 * 
 * <p>
 * In this exercice, we keep those tests from the previous stage.
 * They are in a way more correct than the integration tests used elsewhere in
 * this code.
 * 
 * <p>
 * However, it's quite likely that, for the sake of uniformity, we move
 * to a general use of integration tests (and use of the test database).
 * 
 * We could also use MockMVC instead of WebClient.
 * 
 * This test class mocks the service. Hence, it doesn't need a database.
 */
@WebMvcTest
@Import({
        LoggerConfig.class,
        WebSecurityConfig.class
})
public class CreateCustomerServletTest extends AbstractCustomerServletTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    CatalogService catalogService;

    @MockBean
    YapsUserService yapsUserService;

    // ----------------------------------------------
    // For testing validation annotations
    // -----------------------------------------------
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    /**
     * Default Parameters to send in a request.
     * If changes are needed, please clone this list...
     */

    Map<String, String> parameterMap;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void closeValidator() {
        validatorFactory.close();
    }

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        parameterMap = new HashMap<>();
        String params[] = {
                "login", "",
                "firstname", "",
                "lastname", "",
                "password1", "",
                "password2", "",
                "telephone", "",
                "email", "",
                "street1", "",
                "street2", "",
                "city", "",
                "zipcode", "",
                "state", "",
                "country", ""
        };
        for (int i = 0; i < params.length; i += 2) {
            parameterMap.put(params[i], params[i + 1]);
        }
    }

    private Map<String, String> buildFullParameterMap() {
        String[] parametersArray = new String[] {
                "username", "l1",
                "password1", "pwd1",
                "password2", "pwd1",
                "firstname", "p1",
                "lastname", "p2",
                "telephone", "p3",
                "email", "p4@toto.com",
                "street1", "a1",
                "street2", "a2",
                "city", "a3",
                "zipcode", "a4",
                "state", "a5",
                "country", "a6"
        };
        Map<String, String> parameters = new HashMap<>();

        for (int i = 0; i < parametersArray.length; i += 2) {
            parameters.put(parametersArray[i], parametersArray[i + 1]);
        }
        return parameters;
    }

    @Test
    @WithAnonymousUser
    public void testJustOk() throws Exception {
        // The new id which will be given...
        final String newId = "23";
        CustomerDTO adaCreated = new CustomerDTO("23", "ada",
                "Ada", "Lovelace",
                "", "", new AddressDTO());
        // Configure the service to call checkData on customer and return an id.
        Mockito.when(customerService.save(any(CustomerCreationDTO.class))).thenAnswer(
                invocation -> {
                    CustomerCreationDTO c = invocation.getArgument(0, CustomerCreationDTO.class);
                    Set<ConstraintViolation<CustomerCreationDTO>> res = validator.validate(c);
                    assertTrue(res.isEmpty(), "Customer should be valid");
                    return newId;
                });
        when(customerService.findById(newId)).thenReturn(Optional.of(adaCreated));

        // Deactivate JS errors (which don't correspond to reality)
        // webClient.getOptions().setThrowExceptionOnScriptError(false);

        parameterMap.put("username", "ada");
        parameterMap.put("password1", "pwd1");
        parameterMap.put("password2", "pwd1");

        parameterMap.put("firstname", "Ada");
        parameterMap.put("lastname", "Lovelace");
        ResultActions pageResult = sendCreateRequest(parameterMap);

        pageResult
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpectAll(redirectedUrl("/"));
    }

    @Test
    @WithMockUser("EMP")
    public void testCreateAsEMP() throws Exception {
        testJustOk(); // same test as anonymous
    }


    @Test
    @WithMockUser(roles = "CUST")
    public void testLoggedCustomerCantCreate() throws Exception {
        // The new id which will be given...
        final String newId = "23";
        CustomerDTO adaCreated = new CustomerDTO("23", "ada",
                "Ada", "Lovelace",
                "", "", new AddressDTO());
        // Configure the service to call checkData on customer and return an id.
        Mockito.when(customerService.save(any(CustomerCreationDTO.class))).thenAnswer(
                invocation -> {
                    CustomerCreationDTO c = invocation.getArgument(0, CustomerCreationDTO.class);
                    Set<ConstraintViolation<CustomerCreationDTO>> res = validator.validate(c);
                    assertTrue(res.isEmpty(), "Customer should be valid");
                    return newId;
                });
        when(customerService.findById(newId)).thenReturn(Optional.of(adaCreated));

        // Deactivate JS errors (which don't correspond to reality)
        // webClient.getOptions().setThrowExceptionOnScriptError(false);

        parameterMap.put("username", "ada");
        parameterMap.put("password1", "pwd1");
        parameterMap.put("password2", "pwd1");

        parameterMap.put("firstname", "Ada");
        parameterMap.put("lastname", "Lovelace");
        ResultActions pageResult = sendCreateRequest(parameterMap);

        pageResult
                .andExpect(status().is4xxClientError());
    }

    /**
     * Check if when submitting a new customer info, the correct service method is
     * called.
     * 
     * @throws IOException
     * @throws MalformedURLException
     * @throws CheckException
     */
    @Test
    @WithAnonymousUser
    public void testFullOk() throws Exception {
        // see https://www.baeldung.com/mockito-verify
        // Check that customerService.save will be called with all values from
        // the form...
        final String newId = "23";
        CustomerDTO adaCreated = new CustomerDTO("23", "ada",
                "Ada", "Lovelace",
                "", "", new AddressDTO());
        // Configure the service to call checkData on customer and return an id.
        Mockito.when(customerService.save(any(CustomerCreationDTO.class))).thenAnswer(
                invocation -> {
                    CustomerCreationDTO c = invocation.getArgument(0, CustomerCreationDTO.class);
                    Set<ConstraintViolation<CustomerCreationDTO>> res = validator.validate(c);
                    assertTrue(res.isEmpty(), "Customer should be valid");
                    return newId;
                });
        when(customerService.findById(newId)).thenReturn(Optional.of(adaCreated));

        ArgumentCaptor<CustomerCreationDTO> customerCaptor = ArgumentCaptor.forClass(CustomerCreationDTO.class);
        ResultActions pageResult = sendCreateRequest(buildFullParameterMap());

        pageResult
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpectAll(redirectedUrl("/"));

        verify(customerService).save(customerCaptor.capture());
        CustomerCreationDTO capturedCustomer = customerCaptor.getValue();
        assertAll(
                () -> assertEquals("l1", capturedCustomer.getUsername()),
                () -> assertEquals("pwd1", capturedCustomer.getPassword()),
                () -> assertEquals("p1", capturedCustomer.getFirstname()),
                () -> assertEquals("p2", capturedCustomer.getLastname()),
                () -> assertEquals("p3", capturedCustomer.getTelephone()),
                () -> assertEquals("p4@toto.com", capturedCustomer.getEmail()),
                () -> assertEquals("a1", capturedCustomer.getStreet1()),
                () -> assertEquals("a2", capturedCustomer.getStreet2()),
                () -> assertEquals("a3", capturedCustomer.getCity()),
                () -> assertEquals("a4", capturedCustomer.getZipcode()),
                () -> assertEquals("a5", capturedCustomer.getState()),
                () -> assertEquals("a6", capturedCustomer.getCountry()));
    }

    @Test
    @WithAnonymousUser
    public void testBadFirstName() throws Exception {
        Map<String, String> parameters = buildFullParameterMap();
        parameters.put("firstname", "");
        checkBadData(parameters);
    }

    @Test
    @WithAnonymousUser
    public void testBadLastName() throws Exception {
        Map<String, String> parameters = buildFullParameterMap();
        parameters.put("lastname", "");
        checkBadData(parameters);
        checkBadData(parameters);
    }

    /**
     * Takes bad entry and check it's actually bad.
     * 
     * @param parametersMap
     * @throws MultipleFailuresError
     * @throws Exception
     */
    @WithAnonymousUser
    private void checkBadData(Map<String, String> parametersMap)
            throws MultipleFailuresError, Exception {
        // Configure the service to call checkData on customer.
        Mockito.when(customerService.save(any(CustomerCreationDTO.class))).thenAnswer(
                invocation -> {
                    CustomerCreationDTO c = invocation.getArgument(0, CustomerCreationDTO.class);
                    Set<ConstraintViolation<CustomerCreationDTO>> res = validator.validate(c);
                    if (!res.isEmpty())
                        throw new ValidationException();
                    fail("validation should fail here");
                    return "";
                });
        ResultActions pageResult = sendCreateRequest(parametersMap);

        String expected = "champ obligatoire";

        pageResult
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("customer/createForm"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(expected)));
    }

    @Test
    public void testUserAlreadyDefined() throws Exception {
        String errorMessage = "Cet identifiant de connexion est déjà pris!";
        //UserDetails ada = new User("ada", "fdfdsf", Set.of(new SimpleGrantedAuthority("CUST")));
        // Mockito.when(yapsUserService.loadUserByUsername("ada")).thenReturn(ada);
        Mockito.when(customerService.save(any(CustomerCreationDTO.class))).thenThrow(UsernameAlreadyExistsException.class);
        parameterMap.put("username", "ada");
        parameterMap.put("password1", "pwd1");
        parameterMap.put("password2", "pwd1");

        parameterMap.put("firstname", "Ada");
        parameterMap.put("lastname", "Lovelace");
        ResultActions res = sendCreateRequest(parameterMap);
        res.andExpectAll(
            status().isOk(),    
            content().string(containsString(errorMessage))
        );
    }

    private ResultActions sendCreateRequest(Map<String, String> parametersMap) throws Exception {

        MockHttpServletRequestBuilder request = post("/customer/create").with(csrf());
        for (Entry<String, String> e : parametersMap.entrySet()) {
            request = request.param(e.getKey(), e.getValue());
        }
        return mockMvc.perform(
                request);
    }
}
