package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.database.SQL_Commands;
import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class will simply override any original behavior depending on the 
 * user authorization level. The highest level will have full access and no 
 * behavior would be altered. Restrictions can apply accordingly for each
 * access level type. This class will be static because it will be used 
 * throughout the application.
 * @author Carlos Igreja
 */
public class Authorization1 {
    
    // constants
    private static final String DB_TABLE_NAME = "A_accessLevel_tbl";
    private static final String DB_COLUMN_1 = "user";
    private static final String DB_COLUMN_2 = "accessLevel";
    private static final String LEVEL_1 = "administrator";
    private static final String LEVEL_2 = "developer";
    private static final String LEVEL_3 = "user";
    private static final String LEVEL_4 = "viewer";
    
    // class variables
    private static String userLogin;
    private static String accessLevel;
    
    /**
     * users table
     * username accesslevel -> Auth table id, nameType-admin, dev
     * column names userLogin, userLevel
     */

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
        // use sql query to get the accesslevel from DB
        SQL_Commands sql_commands 
                = new SQL_Commands(DBConnection.getConnection());
        String query = "SELECT * FROM " + DB_TABLE_NAME +
                      " WHERE " + DB_COLUMN_1 + " = '" + userLogin +"';";
        HashMap<String,ArrayList<Object>> map;
        map = sql_commands.getTableData(sql_commands.executeQuery(query));
        accessLevel = map.get(DB_COLUMN_2).get(0).toString();
    }
    
    /**
     * This takes any component and overrides any behavior for that component.
     * Example c instanceOf JFrame then is PM, issue window etc. and regulate
     * the behavior as needed. If full access no checks required, exit promptly.
     * Default is full access and restrictions may apply per user type.
     * @param c 
     */
    public static void authorize( Component c){
        
        if(accessLevel != null) // changed tab state is called from initComponents
            switch(accessLevel){
                case LEVEL_1:
                    break;
                case LEVEL_2:
                    developerPermissions(c);
                    break;
                case LEVEL_3:
                    developerPermissions(c);
                    break;
                case LEVEL_4:
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
