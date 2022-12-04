package com.yaps.petstore.catalog.service;

import java.util.Collection;
import java.util.Optional;

import com.yaps.petstore.catalog.dto.CategoryDTO;
import com.yaps.petstore.catalog.dto.ItemDTO;
import com.yaps.petstore.catalog.dto.ProductDTO;
import com.yaps.petstore.catalog.service.exception.CategoryNotFoundException;
import com.yaps.petstore.catalog.service.exception.ProductNotFoundException;

public interface CatalogService {

    /**
     * Returns a dto for an item with a given id.
     * @param itemId the id of the item
     * @return the dto if the item exists, else Optional.empty().
     */
    Optional<ItemDTO> findItem(String itemId);

    /**
     * Returns all the items related to a given product.
     * @param productId the product id.
     * @return a collection of items
     * @throws ProductNotFoundException if the product doesn't exist in the base.
     */
    Collection<ItemDTO> findItemsForProduct(String productId) throws ProductNotFoundException;

    Optional<ProductDTO> findProduct(String productId);

    Collection<ProductDTO> findProductsForCategory(String categoryId) throws CategoryNotFoundException;

    Optional<CategoryDTO> findCategory(String id);
    
}
