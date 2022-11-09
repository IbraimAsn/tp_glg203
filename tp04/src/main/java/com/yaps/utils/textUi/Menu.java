package com.yaps.utils.textUi;

import java.util.LinkedHashMap;

/**
 * Simple text menu.
 * 
 * A menu is initialized by a number of entries, which are couples
 * (code/label). The code is what is returned by the menu.
 *
 * <p>
 * pre-condition : 
 * code should be unique in a given menu.
 * 
 * <p>
 * More sophisticated menus with embedded actions could be interesting.
 * 
 * <p> Remark : the way to pass the console is an interesting,
 * even if minor, design choice.
 * 
 * <ul>
 * <li> to keep it as an instance variable (our current choice) is
 *      practical, and allows testing ;
 * <li> pass it to individual methods has merit : the building of "Menu" would 
 *      be separated from its use, and the console is not logically part
 *      of the menu ;
 * <li> to fetch the console from System would be easy, but introduce
 *      a hidden dependency, which is bad in case of code modification.
 * </ul>
 */
public class Menu {

   

    /**
     * Menu entries.
     * 
     * We use a LinkedHashMap, which is convenient here because :
     * a) it's a map, and it allows us to know quickly if a code was 
     *    chosen
     * b) it keeps the original order.
     * 
     * Obviously, keeping the original array and the set of codes 
     * would have been ok too.
     */    
    private LinkedHashMap<String, MenuEntry> entryMap;
    private String welcomeText = "Please choose one entry";


    /**
     * The console to use to read entries.
     */
    private SimpleIO simpleIO;

    
    /**
     * Create a menu from the entries.
     * 
     * The same code should not be used twice in the entries,
     * else the contructor would fail with a IllegalArgumentException.
     * @param entries
     * @param simpleIO : the input console to use.
     */
    public Menu(MenuEntry[] entries, SimpleIO simpleIO) {       
        this.simpleIO = simpleIO;
        this.entryMap = new LinkedHashMap<>();
        for (MenuEntry m : entries) {
            add(m);
        }        
    }

    /**
     * Creates an empty menu (to be filled with addEntry)
     * @return
     */
    public Menu(SimpleIO simpleIO) {
        this(new MenuEntry[0], simpleIO);
    }

    public Menu add(MenuEntry m) {
        if (entryMap.containsKey(m.getCode())) {
            throw new IllegalArgumentException("duplicate code "+ m.getCode());
        } else {
            entryMap.put(m.getCode(), m);                
        }
        return this;
    }

    /**
     * Add a new entry in the menu with a specific code and label.
     * @param code
     * @param label
     * @return the menu itself for fluent interface.
     */
    public Menu add(String code, String label) {
        return add(new MenuEntry(code, label));
    }

    public Menu setWelcomeText(String welcomeText) {
        this.welcomeText = welcomeText;
        return this;
    }
    
    /**
     * Choose an entry from the menu.
     * Will return a code in the possible codes, and loops otherwise.
     * @return
     */
    public String choose() {
        String choice = null; // We might choose to use a boolean later.
        while (choice == null) {
            simpleIO.println(welcomeText);
            simpleIO.printf("\n\n");
            printMenu();
            simpleIO.printf("your choice: ");
            simpleIO.flush(); // ensure immediate writing.
            String result = simpleIO.readLine();
            if (entryMap.containsKey(result)) {
                choice = result;
            } else {
                simpleIO.printf("Incorrect choice : %s.\n", result);
            }
        }
        return choice;
    }


    private void printMenu() {
        for (MenuEntry m : entryMap.values()) {
            simpleIO.printf("%s)\t%s\n", m.getCode(), m.getLabel());
        }
    }

    public static class MenuEntry {
        private String code;
        private String label;

        public MenuEntry(String code, String label) {
            this.code = code;
            this.label = label;
        }

        public String getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }
    }
    
}
