package com.yaps.petstore.catalog.ui;


import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yaps.petstore.catalog.dto.CategoryDTO;
import com.yaps.petstore.catalog.dto.ProductDTO;
import com.yaps.petstore.catalog.service.CatalogService;

/**
 * This servlet returns the list of all products for a specific category.
 */
@Controller
@RequestMapping("/category")
public class ViewCategoryController {
	

    @Autowired
    Logger logger;

    @Autowired
	private CatalogService catalogService;

    @GetMapping("/view")
    protected ModelAndView view(String id) {
        logger.debug("entering {} with id={}", "view", id);
        Optional<CategoryDTO> optCategory = catalogService.findCategory(id);
        if (optCategory.isPresent()) {
            Collection<ProductDTO> productsDTO = catalogService.findProductsForCategory(id);
            return new ModelAndView("/catalog/category/view", 
                Map.of("category", optCategory.get(),"products", productsDTO));         
        } else {
            return new ModelAndView("error", "error", "no category for id "+ id);
        }
    }
}
