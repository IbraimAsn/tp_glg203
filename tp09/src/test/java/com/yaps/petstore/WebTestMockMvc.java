package com.yaps.petstore;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

/**
 * This class tests the HTML Pages and controllers
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
@Sql(
// Note : cette annotation devra être déplacée vers les méthodes
// de tests si celles-ci ont besoin de bases différentes
// (par exemple qu'on teste sur des données vides)
"/testsql/catalog/full.sql")
public class WebTestMockMvc {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Checks that all pages are static pages deployed
     * 
     * @throws Exception
     */
    @Test
    public void test404() throws Exception {
        this.mockMvc.perform(get("/dummy.html")).andExpect(status().is4xxClientError());
    }

    @Test
    public void testPageContent() throws Exception {
        String expectedText = "The YAPS Pet Store Demo for Spring Boot is a fictional sample application";
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedText)));
    }

    @WithAnonymousUser
    public void testFirstPageWithAnonymous() throws Exception {
        // @formatter:off
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
        // @formatter:on
    }



    
    @WithMockUser(roles = "EMP")
    public void testFirstPageWithEmp() throws Exception {
        // @formatter:off
        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
        // @formatter:on
    }

    @Test
    public void templatesAreNotVisible() throws Exception {
        // @formatter:off
        // check that templates are not directly accessible...
        this.mockMvc.perform(get("/customer/form.html"))
            .andExpect(status().is4xxClientError());
        // @formatter:on
    }

    /**
     * Checks that all servlets are deployed
     */
    @Test
    public void testWebCheckDeployedCreateCustomer() throws Exception {
        // @formatter:off
        this.mockMvc.perform(get("/customer/create"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Formulaire de création de compte client")));
        // @formatter:on
    }

    @Test
    @WithMockUser(roles = { "EMP" })
    public void testWebViewCustomer() throws Exception {
        // @formatter:off
        this.mockMvc.perform(get("/office/customer/view?id=1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Informations personnelles")));
        // @formatter:on
    }

    @Test
    @WithAnonymousUser
    public void testWebViewCustomerNotLogged() throws Exception {
        // @formatter:off
        this.mockMvc.perform(get("/office/customer/view?id=1"))
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"))
            ;
        // @formatter:on
    }

    @Test
    public void testWebCheckDeployedFindItem() throws Exception {
        this.mockMvc.perform(get("/item/view?id=EST25")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Amazon Parrot")));
    }

    @Test
    public void testWebCheckDeployedFindItems() throws Exception {
        // @formatter:off
        String expectedString = "Liste des items appartenant au produit Amazon Parrot";
        this.mockMvc.perform(get("/product/view?id=AVCB01"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(
                        content().string(
                            containsString(
                                expectedString
                                )));
        // @formatter:on
    }

    @Test
    public void testWebCheckDeployedFindProducts() throws Exception {
        // @formatter:off
        String expectedString = "Liste de produits appartenant à la catégorie Birds";
        this.mockMvc
            .perform(get("/category/view?id=BIRDS"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(expectedString)));
        // @formatter:on
    }

}
