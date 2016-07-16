
package com.elle.ProjectManager.entities;

/**
 * AccessLevel
 * @author Carlos Igreja
 * @since  May 12, 2016
 */
public class AccessLevel extends DbEntity {
    private int id;
    private String user;
    private String accessLevel;
    
    public AccessLevel(){
        
    }
    
    public AccessLevel(int id, String user, String accessLevel) {
        this.id = id;
        this.user = user;
        this.accessLevel = accessLevel;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //do not need, so no implementation
    @Override
    public Object deepClone() {
        
        return null;
    }
    
    
}
