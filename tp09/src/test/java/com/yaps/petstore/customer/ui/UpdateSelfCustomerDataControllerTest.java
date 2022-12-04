package com.yaps.petstore.customer.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.yaps.petstore.catalog.service.CatalogService;
import com.yaps.petstore.config.LoggerConfig;
import com.yaps.petstore.config.WebSecurityConfig;
import com.yaps.petstore.customer.dto.AddressDTO;
import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerService;
import com.yaps.petstore.security.service.YapsUserService;

@WebMvcTest
@Import({
        LoggerConfig.class,
        WebSecurityConfig.class
})
public class UpdateSelfCustomerDataControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    CatalogService catalogService;

    @MockBean
    YapsUserService userService;

    CustomerDTO customer;

    @BeforeEach
    public void init() {
        AddressDTO addresse = new AddressDTO("s1", "s2", "c1", "s3", "75000", "france");
        customer = new CustomerDTO("10", "username1", "n2", "n3", "00", "u@cnam.fr", addresse);
        when(customerService.findById("1")).thenReturn(Optional.of(customer));
        when(customerService.getCustomerByUsername("username1")).thenReturn(customer);
        // when(userService.loadUserByUsername("username1")).the
    }


    @Test
    @WithAnonymousUser
    public void testUpdateSelfGetError() throws Exception {
        mockMvc.perform(get("/customer/updateself")).andExpectAll(
                status().is3xxRedirection(),
                redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "username1", roles = "EMP")
    public void testUpdateSelfGetErrorForEmp() throws Exception {
        mockMvc.perform(get("/customer/updateself")).andExpectAll(
                status().isForbidden()
        );
    }

    @Test
    @WithAnonymousUser
    public void testUpdateSelfPostError() throws Exception {
        mockMvc.perform(post("/customer/updateself")).andExpectAll(
                status().isForbidden());
    }

    @Test
    @WithMockUser(username = "username1", roles = "EMP")
    public void testUpdateSelfPostErrorForEmp() throws Exception {
        mockMvc.perform(post("/customer/updateself")).andExpectAll(
                status().isForbidden()
        );
    }
    
}
