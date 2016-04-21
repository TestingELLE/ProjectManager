
package com.elle.ProjectManager.database;

/**
 * Database
 * @author Carlos Igreja
 * @since  Mar 16, 2016
 */
public class Database {

    private String name;
    private boolean defaultSelection;
    private String username;
    private String password;

    public Database(String name, boolean defaultSelection, String username, String password) {
        this.name = name;
        this.defaultSelection = defaultSelection;
        this.username = username;
        this.password = password;
    }
    
    public Database(String name){
        this(name,false,"","");
    }
    
    public Database(){
        this("",false,"","");
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
