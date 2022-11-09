package com.yaps.petstore.catalogApplication.domainObjectManagement;

import java.util.List;

import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.model.DomainObject;
import com.yaps.utils.textUi.SimpleIO;

/**
 * UI element which allows to choose an item of a certain type.
 */
public class EntitySelector<E extends DomainObject, D extends AbstractDAO<E>> {
    
    private SimpleIO io;
    private  D dao;
    private String entityName;

    public EntitySelector(String entityName, D dao,  SimpleIO io) {
        this.dao = dao;
        this.io = io;
        this.entityName = entityName;
    }

    /**
     * Select a product from a list.
     * This code is somehow repetitive. We should create an EntitySelector.
     * @param connection
     * @return
     */
    public E selectEntity() {
        E product = null;
        List<E> list = dao.findAll();
        if (list.isEmpty()) {
            // This test should probably be done before entering product management !
            io.println("Can't select a %s : there are none !!".formatted(entityName));
            throw new IllegalStateException(
                "Can't work on products if there are no %s".formatted(entityName));
        } else {
            do {
                io.println("please select a %s in the following list".formatted(entityName));
                for (int i = 0; i < list.size(); i++) {
                    io.printf("%d)\t%s%n", i + 1, list.get(i).shortDisplay());
                }
                int answer = io.askForInt("%s index".formatted(entityName), 1, list.size());
                product = list.get(answer - 1);
            } while (product == null);
            return product;
        }
    }
}
