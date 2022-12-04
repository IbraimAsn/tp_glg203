package com.yaps.petstore.customer.ui;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.yaps.petstore.catalog.service.CatalogService;
import com.yaps.petstore.config.LoggerConfig;
import com.yaps.petstore.config.WebSecurityConfig;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerService;

/**
 * Test class for displaying customers.
 * 
 * 
 * This test class mocks the service. Hence, it doesn't need a database.
 * 
 */
@WebMvcTest
@Import({
        LoggerConfig.class,
        WebSecurityConfig.class
})
public class BackOfficeCustomerControllerTest extends AbstractCustomerServletTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    CatalogService catalogService;

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
    }

    @Test
    @WithAnonymousUser
    public void testAnonymous() throws Exception {
        mockMvc.perform(get("/office/customer/view")
                .param("id", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "CUST")
    public void testAsCustomer() throws Exception {
        CustomerDTO customer10 = map.get("2");
        // Configuration du pseudo service...
        when(customerService.findById("2")).thenReturn(Optional.of(customer10));
        mockMvc.perform(get("/office/customer/view")
                .param("id", "2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMP")
    public void testSimple() throws Exception {
        CustomerDTO customer10 = map.get("2");
        // Configuration du pseudo service...
        when(customerService.findById("2")).thenReturn(Optional.of(customer10));
        checkDisplayForCustomer(customer10);
    }

    @Test
    @WithMockUser(roles = "EMP")
    public void testRealistic() throws Exception {
        CustomerDTO customer = map.get("2");
        // Configuration du pseudo service...
        when(customerService.findById("2")).thenReturn(Optional.of(customer));
        checkDisplayForCustomer(customer);
    }

    @Test
    @WithMockUser(roles = "EMP")
    public void testNotFound() throws Exception {
        String expectedContent = "Pas de client pour";
        mockMvc.perform(get("/office/customer/view").param("id", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(expectedContent)))
                .andExpect(content().string(containsString("id 10")));
    }

    private void checkDisplayForCustomer(CustomerDTO customer) throws Exception {
        mockMvc.perform(get("/office/customer/view").param("id", customer.getId()))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.TEXT_HTML),
                        content().string(containsString(customer.getId())),
                        content().string(containsString(customer.getFirstname())),
                        content().string(containsString(customer.getLastname())),
                        content().string(containsString(customer.getEmail())),
                        content().string(containsString(customer.getTelephone())),
                        content().string(containsString(customer.getStreet1())),
                        content().string(containsString(customer.getStreet2())),
                        content().string(containsString(customer.getCity())),
                        content().string(containsString(customer.getZipcode())),
                        content().string(containsString(customer.getCountry())),
                        content().string(containsString(customer.getState())));
    }

    @Test
    @WithMockUser(roles = "EMP")
    public void testList() throws Exception {
        CustomerDTO c1 = getCustomer("1");
        CustomerDTO c2 = getCustomer("2");
        when(customerService.findAll()).thenReturn(List.of(c1, c2));
        mockMvc.perform(get("/office/customer/list"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.TEXT_HTML),
                        content().string(containsString(c1.getUsername())),
                        content().string(containsString(c2.getUsername())));
    }

    @Test
    @WithMockUser(roles = "CUST")
    public void testListForbidden() throws Exception {
        CustomerDTO c1 = getCustomer("1");
        CustomerDTO c2 = getCustomer("2");
        when(customerService.findAll()).thenReturn(List.of(c1, c2));
        mockMvc.perform(get("/office/customer/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void testListAnonymous() throws Exception {
        CustomerDTO c1 = getCustomer("1");
        CustomerDTO c2 = getCustomer("2");
        when(customerService.findAll()).thenReturn(List.of(c1, c2));
        mockMvc.perform(get("/office/customer/list"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }
}
