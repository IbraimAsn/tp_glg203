package com.yaps.petstore.customer.ui;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.dto.CustomerUpdateDTO;
import com.yaps.petstore.customer.service.CustomerService;
import com.yaps.petstore.customer.ui.forms.UpdateCustomerForm;

/**
 * This controller will allow a CUST customer to update his OWN data.
 */
@Controller
public class UpdateSelfCustomerDataController {

    private static final String CUSTOMER_UPDATE_SELF = "customer/updateself";
    @Autowired
    CustomerService customerService;

    @GetMapping
    public String displayForm(Principal principal, Model model) {
        throw new RuntimeException("à compléter");
    }

    /**
     * Traite le formulaire.
     * 
     * S'il est incorrect : le réaffiche avec les messages d'erreur ;
     * s'il est correct : met à jour le customer, **et redirige** vers l'url
     * /customer/viewself.
     * 
     * @param updateForm
     * @param bindingResult
     * @param model
     * @param principal
     * @return
     */
    @PostMapping
    public String processForm(
            @Valid @ModelAttribute("updateForm") UpdateCustomerForm updateForm,
            BindingResult bindingResult,
            Model model,
            Principal principal) {
        throw new RuntimeException("à compléter");
    }
}
