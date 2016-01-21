package com.elle.ProjectManager.logic;

/**
 *
 * @author Carlos Igreja
 */
public abstract class Authorization {
    
    // Information will probably come from the database at start up
    
    // user information
    // userid
    // userLevel
    
    // constants
    // ADMINISTRATOR = ?
    // DEVELOPER = ?
    
    public abstract void getInfoFromDB();
    public abstract void authorize();
}
