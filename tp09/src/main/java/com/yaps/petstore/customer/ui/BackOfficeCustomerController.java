package com.yaps.petstore.customer.ui;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yaps.petstore.customer.dto.CustomerDTO;
import com.yaps.petstore.customer.service.CustomerService;

/**
 * Controller used for administrative operations (from EMP) on Customers.
 */
@Controller
@RequestMapping("/office/customer")
public class BackOfficeCustomerController {

    @Autowired
    Logger logger;

    @Autowired
    CustomerService customerService;

    @GetMapping("/search")
    protected String showSearchForm() throws ServletException, IOException {
        logger.debug("entering {}", "showSearchForm");
        return "customer/search";
    }

    @GetMapping("/view")
    protected ModelAndView displayCustomer(String id) throws ServletException, IOException {
        logger.debug("entering {}", "displayCustomer");
        Optional<CustomerDTO> optCustomer = customerService.findById(id);
        if (optCustomer.isPresent()) {
            return new ModelAndView("customer/view", "customer", optCustomer.get());
        } else {
            return new ModelAndView("customer/missingClient", "id",  id);
        }
    }

    @GetMapping("/list")
    protected String displayCustomerList(Model model) {
        logger.debug("entering {}", "displayCustomerList");
        List<CustomerDTO> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "customer/list";
    }

}
