package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import java.awt.Component;

/**
 * This class will simply override any original behavior depending on the 
 * user authorization level. The highest level will have full access and no 
 * behavior would be altered. Restrictions can apply accordingly for each
 * access level type. This class will be static because it will be used 
 * throughout the application.
 * @author Carlos Igreja
 */
public class Authorization {
    
    // Information will probably come from the database at start up
    
    // user information
    // userid - LOGIN (pupone_Carlos) - issue can't access users table if exists?
    // userLevel - Admin or Developer
    private static String userLogin;
    private static int userLevel;
    
    // constants
    private static final int ADMINISTRATOR = 1;
    private static final int DEVELOPER = 2;

    /**
     * we might want to cascade permissions / restrictions
     * For example. Level 1, 2, 3 (highest to lowest)
     * Example: Level 2 disable menu items 1,2 
     *          and Level 3 disable menu items 1,2,3
     * Level 3 will have all of Level 2 restrictions plus additional 
     * restrictions. If this is the case, then the restrictions for 
     * level 2 may be applied and then level 3. 
     */
    
    /**
     * When the user logs in, we will need to know the access level and 
     * restrict the application accordingly per user type or access level.
     * This information will be stored and retrieved from the database.
     * This method will get the required information from the database.
     */
    public static void getInfoFromDB(){
        userLogin = DBConnection.getUserName();
        // use sql query to get the userLevel from DB
        userLevel = 2; // DB needs to be implemented
    }
    
    /**
     * This takes any component and overrides any behavior for that component.
     * Example c instanceOf JFrame then is PM, issue window etc. and regulate
     * the behavior as needed. If full access no checks required, exit promptly.
     * Default is full access and restrictions may apply per user type.
     * @param c 
     */
    public static void authorize( Component c){
        
        switch(userLevel){
            case ADMINISTRATOR:
                break;
            case DEVELOPER:
                developerPermissions(c);
                break;
            default:
                break;
        }
    }

    /**
     * Developer Restrictions
     * @param c The component to be authorized (restrict features or behavior)
     */
    private static void developerPermissions(Component c) {
        
        // ProjectManagerWindow
        if(c instanceof ProjectManagerWindow){
            ProjectManagerWindow pm = (ProjectManagerWindow)c;
            // menu item components
            pm.getMenuItemDummy().setEnabled(false);
        }
    }
}
