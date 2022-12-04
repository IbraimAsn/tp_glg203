package com.yaps.petstore.catalog.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yaps.petstore.catalog.dao.CategoryDAO;
import com.yaps.petstore.catalog.dao.ItemDAO;
import com.yaps.petstore.catalog.dao.ProductDAO;
import com.yaps.petstore.catalog.domain.Item;
import com.yaps.petstore.catalog.dto.CategoryDTO;
import com.yaps.petstore.catalog.dto.ItemDTO;
import com.yaps.petstore.catalog.dto.ProductDTO;

@Component
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    ItemDAO itemDAO;

    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @Override
    public Optional<ItemDTO> findItem(String itemId) {
        return itemDAO.findById(itemId).map(CatalogDTOMapper::toDTO);
    }

    @Override
    public Optional<CategoryDTO> findCategory(String id) {
        return categoryDAO.findById(id).map(CatalogDTOMapper::toDTO);
    }

    @Override
    public Collection<ItemDTO> findItemsForProduct(String productId) {
        List<ItemDTO> items = new ArrayList<>();
        for (Item item : itemDAO.findAll()) {
            if (item.getProduct().getId().equals(productId)) {
                items.add(CatalogDTOMapper.toDTO(item));
            }
        }
        return items;
    }

    @Override
    public Optional<ProductDTO> findProduct(String productId) {
        return productDAO.findById(productId).map(CatalogDTOMapper::toDTO);
    }

    @Override
    public Collection<ProductDTO> findProductsForCategory(String categoryId) {
        return productDAO.findAll().stream()
            .filter(p -> p.getCategory().getId().equals(categoryId))
            .map(CatalogDTOMapper::toDTO)
            .toList();
    }

}
