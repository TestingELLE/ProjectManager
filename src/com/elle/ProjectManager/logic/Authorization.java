package com.elle.ProjectManager.logic;

import java.awt.Component;

/**
 * This class will simply override any original behavior depending on the 
 * user authorization level. Obviously the highest level will have full
 * access and no behavior would be 
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
    
    /**
     * When the user logs in, we will need to know the access level and 
     * restrict the application accordingly per user type or access level.
     * This information will be stored and retrieved from the database.
     * This method will get the required information from the database.
     */
    public abstract void getInfoFromDB();
    /**
     * This takes any component and overrides any behavior for that component.
     * Example c instanceOf JFrame then is PM, issue window etc. and regulate
     * the behavior as needed. If full access no checks required, exit promptly.
     * Default is full access and restrictions may apply per user type.
     * @param c 
     */
    public abstract void authorize( Component c);
}
