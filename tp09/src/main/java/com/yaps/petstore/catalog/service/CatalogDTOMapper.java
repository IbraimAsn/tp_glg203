package com.yaps.petstore.catalog.service;

import com.yaps.petstore.catalog.domain.Category;
import com.yaps.petstore.catalog.domain.Item;
import com.yaps.petstore.catalog.domain.Product;
import com.yaps.petstore.catalog.dto.CategoryDTO;
import com.yaps.petstore.catalog.dto.ItemDTO;
import com.yaps.petstore.catalog.dto.ProductDTO;

/**
 * Helper class for mapping Catalog items.
 */
class CatalogDTOMapper {

    public static ItemDTO toDTO(Item item) {
        Product p = item.getProduct();
        return new ItemDTO(item.getId(), item.getName(), item.getUnitCost(), item.getImagePath(),
             p.getId(), p.getName(), p.getDescription());
    }

    public static ProductDTO toDTO(Product product) {
        Category category = product.getCategory();
        return new ProductDTO(
                product.getId(), product.getName(), product.getDescription(),
                category.getId(), category.getName());
    }

    public static CategoryDTO toDTO(Category category) {
        return new CategoryDTO(category.getId(), category.getName(), category.getDescription());
    }

}
