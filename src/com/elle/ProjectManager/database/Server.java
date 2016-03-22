
package com.elle.ProjectManager.database;

import java.util.ArrayList;

/**
 * Server
 * This class is to create a server object.
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class Server {
    
    private String name;
    private String url;
    private boolean defaultSelection;
    private ArrayList<Database> databases;

    public Server() {
        name = "";
        url = "";
        defaultSelection = false;
        databases = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDefaultSelection() {
        return defaultSelection;
    }

    public void setDefaultSelection(boolean defaultSelection) {
        this.defaultSelection = defaultSelection;
    }

    public ArrayList<Database> getDatabases() {
        return databases;
    }

    public void setDatabases(ArrayList<Database> databases) {
        this.databases = databases;
    }
}
