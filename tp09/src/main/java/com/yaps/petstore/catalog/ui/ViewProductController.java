package com.yaps.petstore.catalog.ui;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yaps.petstore.catalog.dto.ItemDTO;
import com.yaps.petstore.catalog.dto.ProductDTO;
import com.yaps.petstore.catalog.service.CatalogService;

@Controller
@RequestMapping("/product")
public class ViewProductController {
    
    @Autowired
    CatalogService catalogService;

    @GetMapping("/view")
    public ModelAndView view(String id) {
        Optional<ProductDTO> optProduct = catalogService.findProduct(id);
        if (optProduct.isPresent()) {   
            Collection<ItemDTO> items = catalogService.findItemsForProduct(id);
            return new ModelAndView("/catalog/product/view", Map.of("product", optProduct.get(), "items", items));
        } else {
            return new ModelAndView("error", "error", "No product found for id " + id);

        }
    }
}
