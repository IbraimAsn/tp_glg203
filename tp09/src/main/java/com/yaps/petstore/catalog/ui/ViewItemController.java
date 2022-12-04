package com.yaps.petstore.catalog.ui;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yaps.petstore.catalog.dto.ItemDTO;
import com.yaps.petstore.catalog.service.CatalogService;

/**
 * These servlets returns the selected item / items.
 */
@Controller
@RequestMapping("/item")
public class ViewItemController {
	
    @Autowired
    Logger logger;

    @Autowired
	private CatalogService catalogService;

    @GetMapping("/view")
    protected ModelAndView findItem(String id) {
        final String mname = "findItem";
        logger.debug("entering {} with id={}", mname, id);
        // Version avec des lambda partout :
        // return 
        //     cs.findItem(itemId)
        //         .map(itemDTO -> new ModelAndView("item", Map.of("itemDTO", itemDTO)))
        //     .orElseGet(() -> new ModelAndView("error", Map.of("exception", "No item found for id " + itemId)))
        // ;
        // // Version sans lambdas (équivalente)
        Optional<ItemDTO> optItem = catalogService.findItem(id);
        if (optItem.isPresent()) {
            return new ModelAndView("/catalog/item/view", "item", optItem.get());
        } else {
            return new ModelAndView("error", "error", "No item found for id " + id);
        }

    } 
}
