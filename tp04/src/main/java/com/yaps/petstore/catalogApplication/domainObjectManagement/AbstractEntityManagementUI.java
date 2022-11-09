package com.yaps.petstore.catalogApplication.domainObjectManagement;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import com.yaps.petstore.catalogApplication.ConnectionHelper;
import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;
import com.yaps.utils.model.DomainObject;
import com.yaps.utils.textUi.Menu;
import com.yaps.utils.textUi.SimpleIO;

/**
 * Template for Category/Product/Item management ui.
 * The code for editing/listing/etc... each kind of DomainObject is pretty
 * repetitive.
 * Instead of copy/pasting and editing, we tried to abstract this code using
 * the <b>Template Method</b> pattern.
 */
public abstract class AbstractEntityManagementUI<E extends DomainObject, D extends AbstractDAO<E>> {

    protected SimpleIO io;

    private String entityName;

    /**
     * A Y/N Menu. Should move to a separate class for it is used in multiple
     * places.
     */
    protected Menu yesNoMenu;

    /**
     * Initialise the AbstractEntityManagementUI
     * 
     * @param io         the input/output used by the software
     * @param entityName the name of the kind of DomainObject to edit (e.g.
     *                   "product").
     */
    protected AbstractEntityManagementUI(SimpleIO io, String entityName) {
        this.io = io;
        this.entityName = entityName;
        this.yesNoMenu = new Menu(io).add("y", "yes").add("n", "no");
    }

    /**
     * Returns the kind of domain objects edited by this ui element.
     * <p>
     * Used in text display, dialogs, etc.
     * 
     * @return
     */
    private String getEntityName() {
        return entityName;
    }

    /**
     * This menu is dynamically created depending on the application state.
     * @return
     */
    private Menu buildManagementMenu() {
        Menu manageMenu = new Menu(io)
                .setWelcomeText("Please choose one action")
                .add("l", "list entries");
        if (isNotEmpty()) {
            manageMenu.add("v", "view entry");
        }
        if (canCreate()) {
                manageMenu.add("c", "create entry");
        }
        if (isNotEmpty()) {
            manageMenu
                .add("e", "edit entry")
                .add("d", "delete entry");                
        }
        manageMenu.add("q", "quit");
        return manageMenu;
    }

    public void run() {
        
        String choice;
        do {
            choice = buildManagementMenu()
                    .choose();
            switch (choice) {
                case "l":
                    listEntities();
                    break;
                case "v":
                    viewEntity();
                    break;
                case "c":
                    createEntity();
                    break;
                case "e":
                    editEntity();
                    break;
                case "d":
                    deleteEntity();
                    break;
            }
        } while (!"q".equals(choice));
    }

    /**
     * Is it possible to create an entity of this kind.
     * Checks pre-conditions.
     * 
     * @return
     */
    protected boolean canCreate() {
        return true;
    }

    /**
     * Is there at least one entry to view/delete/update...
     * 
     * @return
     */
    protected boolean isNotEmpty() {
        return !ConnectionHelper.computeWithConnection(io, connection -> createDAO(connection).findAll().isEmpty());
    }

    private void deleteEntity() {
        io.println();
        final String id = io.askForString("please enter the id of the " + entityName + " to delete");
        ConnectionHelper.doWithConnection(io, connection -> {
            D dao = createDAO(connection);
            Optional<E> found = dao.findById(id);
            if (found.isEmpty()) {
                io.printf("Sorry, nothing available for id %s%n", id);
            } else {
                E cat = found.get();
                io.println("found:");
                io.println(cat.toString());
                String yesNo = yesNoMenu
                        .setWelcomeText("Are you sure you want to delete this entry")
                        .choose();
                if ("y".equals(yesNo)) {
                    try {
                        dao.remove(id);
                        io.println("element deleted");
                    } catch (ObjectNotFoundException e) {
                        // Should not happen, we have just checked.
                        // or someone has deleted it meanwhile ?
                        io.println("The element seems to have been already deleted!");
                    }
                }
            }
        });
        io.waitEnter();
    }

    private void editEntity() {
        io.println();
        String id = io.askForString("Id of %s to edit".formatted(getEntityName()));
        ConnectionHelper.doWithConnection(io, connection -> {
            D dao = createDAO(connection);
            dao.findById(id).ifPresentOrElse(
                    entity -> {
                        Menu editMenu = createEditMenu();
                        String answer;
                        do {
                            io.println("Current value of %s".formatted(getEntityName()));
                            io.println(entity.toString());
                            answer = editMenu.choose();
                            if (!"q".equals(answer)) {
                                processEditing(connection, answer, entity);
                            }
                        } while (!"q".equals(answer));
                        askIfOkAndSave(dao, entity);
                    },
                    () -> io.printf("Sorry, %s %s is unknown%n", getEntityName(), id));
        });
    }

    /**
     * Ask if edit was ok, and save the domain object if needed.
     * 
     * @param dao    a DAO for the domain object
     * @param entity the domain object to save
     */
    private void askIfOkAndSave(D dao, E entity) {
        io.println("edited %s:".formatted(getEntityName()));
        io.println(entity.toString());
        String yesNo = yesNoMenu.setWelcomeText(
                "Do you want to save your edit").choose();
        if ("y".equals(yesNo)) {
            try {
                dao.update(entity);
            } catch (ObjectNotFoundException e) {
                io.println("Object was not found. This is a bug.");
            } catch (CheckException e) {
                io.printf("%s is not correct: %s%n",
                        getEntityName(),
                        e.getMessage());
            }
        }
    }

    private void createEntity() {
        ConnectionHelper.doWithConnection(io, connection -> {
            E entity = askForEntityData(connection);
            io.println(entity.toString());
            String confirmation = yesNoMenu.setWelcomeText(
                    "save new %s ?".formatted(getEntityName())).choose();
            if ("y".equals(confirmation)) {
                D dao = createDAO(connection);
                try {
                    dao.save(entity);
                } catch (DuplicateKeyException e) {
                    io.printf("Id %s is already used%n", entity.getId());
                } catch (CheckException e) {
                    io.printf("Invalid data : %s%n", e.getMessage());
                }
            }
        });
    }

    private void viewEntity() {
        io.println();
        final String id = io.askForString(
                "please enter the id of the %s to display: ".formatted(getEntityName()));
        ConnectionHelper.doWithConnection(io, connection -> {
            D dao = createDAO(connection);
            dao.findById(id).ifPresentOrElse(
                    e -> io.println(e.toString()),
                    () -> io.printf("Sorry, nothing available for id %s%n", id));
        });
        io.waitEnter();
    }

    private void listEntities() {
        io.println();
        ConnectionHelper.doWithConnection(io, connection -> {
            D dao = createDAO(connection);
            List<E> list = dao.findAll();
            if (list.isEmpty()) {
                io.println("Sorry, list is empty");
            } else {
                for (E p : list) {
                    io.printf("%s%n", p.shortDisplay());
                }
            }
            io.waitEnter();
        });
    }

    protected abstract D createDAO(Connection connection);

    /**
     * Creates a menu for choosing fields in edition.
     * 
     * @return
     */
    protected abstract Menu createEditMenu();

    /**
     * Process the editing of the entity for a particular field
     * <p>
     * Should ask the value for the chosen field, and set it.
     * <p>
     * A connection is provided in case one wants to display other
     * information from the database. E.g. When inputing product info, one might
     * want to list the available categories.
     *
     * @param connection : a connection to the database.
     * @param fieldCode  : the menu code for choosing the field
     * @param entity     : the entity to edit.
     */
    protected abstract void processEditing(Connection connection, String fieldCode, E entity);

    /**
     * Ask the data for an entity.
     * <p>
     * A connection is provided in case one wants to display other
     * information from the database. E.g. When inputing product info, one might
     * want to list the available categories.
     * 
     * @param connection
     * @return
     */
    protected abstract E askForEntityData(Connection connection);

}
