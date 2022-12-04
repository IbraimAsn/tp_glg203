package com.yaps.petstore.catalog.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // active application-test.properties en PLUS de application.properties
@Sql(
// Note : cette annotation devra être déplacée vers les méthodes
// de tests si celles-ci ont besoin de bases différentes
// (par exemple qu'on teste sur des données vides)
"/testsql/catalog/full.sql")
public class SecurityTest {

    @Autowired
    private WebClient webClient;

    @Test
    @WithAnonymousUser
    public void viewCategoryForAnonymousUser() throws Exception {
        Page page = webClient.getPage("/category/view?id=BIRDS");
        assertTrue(page.getWebResponse().isSuccess());
    }

    @Test
    @WithMockUser(roles = "EMP")
    public void viewCategoryForEmp() throws Exception {
        Page page = webClient.getPage("/category/view?id=BIRDS");
        assertTrue(page.getWebResponse().isSuccess());
    }

   

    @Test
    @WithAnonymousUser
    public void viewProductForAnonymousUser() throws Exception {
        Page page = webClient.getPage("/product/view?id=AVCB01");
        assertTrue(page.getWebResponse().isSuccess());
    }

    @Test
    @WithMockUser(roles = "EMP")
    public void viewProductForEmp() throws Exception {
        Page page = webClient.getPage("/product/view?id=AVCB01");
        assertTrue(page.getWebResponse().isSuccess());
    }

    @Test
    @WithAnonymousUser
    public void viewItemForAnonymousUser() throws Exception {
        Page page = webClient.getPage("/item/view?id=EST2");
        assertTrue(page.getWebResponse().isSuccess());
    }

    @Test
    @WithMockUser(roles = "EMP")
    public void viewItemForEmp() throws Exception {
        Page page = webClient.getPage("/item/view?id=EST2");
        assertTrue(page.getWebResponse().isSuccess());
    }

    


}
