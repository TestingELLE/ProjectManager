
package com.elle.ProjectManager.database;

/**
 * Database
 * @author Carlos Igreja
 * @since  Mar 16, 2016
 */
public class Database {

    private String name;
    private boolean defaultSelection;

    public Database(String name, boolean defaultSelection) {
        this.name = name;
        this.defaultSelection = defaultSelection;
    }
    
    public Database(String name){
        this(name,false);
    }
    
    public Database(){
        this("",false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefaultSelection() {
        return defaultSelection;
    }

    public void setDefaultSelection(boolean defaultSelection) {
        this.defaultSelection = defaultSelection;
    }
    
    
}
