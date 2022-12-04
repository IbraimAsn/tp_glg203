package com.yaps.petstore.customer.ui;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yaps.petstore.common.i18n.ErrorCodes;
import com.yaps.petstore.customer.service.CustomerService;
import com.yaps.petstore.customer.ui.forms.CreateCustomerForm;
import com.yaps.petstore.security.exception.UsernameAlreadyExistsException;

/**
 * This controller creates a new customer.
 */
@Controller
@RequestMapping("/customer")
public class CreateCustomerController {

    private static final String CUSTOMER_CREATE_FORM = "customer/createForm";

    @Autowired
    Logger logger;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/create")
    public String customerForm(Model model) {
        logDebugEnter("customerForm");
        model.addAttribute("customerForm", new CreateCustomerForm());
        return CUSTOMER_CREATE_FORM;
    }

    /**
     * Le contrôleur pour la création de customers.
     * 
     * Note importante : pour que l'élément du modèle qui contient le formulaire
     * soit
     * correctement traité, il FAUT utiliser le même nom dans le paramètre du
     * contrôleur
     * (ici customerForm) et dans l'attribut.
     * 
     * Il y a deux approches possibles :
     * 
     * <ul>
     * <li>se baser sur le nom de la <strong>classe</strong> de l'objet. Ici, il est
     * correct.
     * <li>fixer explicitement le nom avec @ModelAttribute
     * </ul>
     * 
     * à la réflexion, cette dernière approche, même si elle est un peu redondante,
     * a le mérite qu'on ne
     * risque pas d'être victime d'un bête renommage de classe.
     * 
     * @param customerForm
     * @param bindingResult
     * @param redirectAttributes : utilisé pour passer un message lors de la redirection.
     * @return
     */
    @PostMapping("/create")
    public ModelAndView createCustomerProcessV1(
            @Valid @ModelAttribute("customerForm") CreateCustomerForm customerForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // Dans la version précédente, on passait par les exceptions.
        // Ici, on utilise le mécanisme de Spring MVC pour la validation des
        // formulaires.
        logDebugEnter("createCustomerProcess");
        if (bindingResult.hasErrors()) {
            ModelAndView result = new ModelAndView(CUSTOMER_CREATE_FORM);
            return result;
        } else {
            // On pourrait aller plus loin dans la validation "automatique" et écrire un
            // valideur qui vérifierait que
            // les mots de passe sont égaux. Mais il est aussi simple de le faire à la main.
            if (!customerForm.getPassword1().equals(customerForm.getPassword2())) {
                bindingResult.rejectValue("password2", ErrorCodes.PASSWORD_MUST_MATCH, "password must match");
                return new ModelAndView(CUSTOMER_CREATE_FORM);
            } else {
                String newId;
                try {
                    newId = customerService.save(customerForm.toDTO());
                    // TODO : i18n for this message.
                    redirectAttributes.addFlashAttribute("successMessage",  "User %s created".formatted(newId));
                    return new ModelAndView("redirect:/");
                } catch (UsernameAlreadyExistsException e) {
                    bindingResult.reject(ErrorCodes.LOGIN_ALREADY_EXISTS, "login already exists");
                    return new ModelAndView(CUSTOMER_CREATE_FORM);                    
                }
            }
        }
    }

    /**
     * Log the call of a method - we might like to know if controllers are actually
     * called.
     * 
     * @param methodName
     */
    private void logDebugEnter(String methodName) {
        logger.debug("entering {}", methodName);
    }
}