package com.yaps.petstore.customer.ui;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerService;

@Controller
@RequestMapping("/customer/viewself")
public class ViewSelfController {
    
    @Autowired
    CustomerService customerService;

    @GetMapping
    public String view(Principal principal, Model model) {
        CustomerDTO customer = customerService.getCustomerByUsername(principal.getName());
        model.addAttribute("customer", customer);
        return "customer/view";
    }
}
