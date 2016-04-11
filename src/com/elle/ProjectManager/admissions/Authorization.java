package com.elle.ProjectManager.admissions;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.database.SQL_Commands;
import com.elle.ProjectManager.presentation.*;
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
public class Authorization {
    
    // database constants
    private static final String DB_TABLE_NAME = "PM_accessLevel_tbl";
    private static final String DB_COLUMN_1 = "user";
    private static final String DB_COLUMN_2 = "accessLevel";
    
    // constants
    private static final String ADMINISTRATOR = "administrator";
    private static final String DEVELOPER = "developer";
    private static final String USER = "user";
    private static final String VIEWER = "viewer";
    
    // class variables
    private static String userLogin;
    private static String accessLevel;
    
    /**
     * When the user logs in, we will need to know the access level and 
     * restrict the application accordingly per user type or access level.
     * This information will be stored and retrieved from the database.
     * This method will get the required information from the database.
     */
    public static boolean getInfoFromDB(){
        userLogin = DBConnection.getUserName();
        // use sql query to get the accesslevel from DB
        SQL_Commands sql_commands 
                = new SQL_Commands(DBConnection.getConnection());
        String query = "SELECT * FROM " + DB_TABLE_NAME +
                      " WHERE " + DB_COLUMN_1 + " = '" + userLogin +"';";
        HashMap<String,ArrayList<Object>> map;
        map = sql_commands.getTableData(sql_commands.executeQuery(query));
        if(!map.get(DB_COLUMN_2).isEmpty()){
            accessLevel = map.get(DB_COLUMN_2).get(0).toString();
            return true;
        }
        else{
            accessLevel = USER; // defaults to user
            return false;
        }
    }
    
    /**
     * This takes any component and overrides any behavior for that component.
     * @param c 
     */
    public static void authorize( Component c){
        
        if(accessLevel != null) // changed tab state is called from initComponents
            switch(accessLevel){
                case ADMINISTRATOR:
                    setPermissions(c, new Administrator());
                    break;
                case DEVELOPER:
                    setPermissions(c, new Administrator());
                    setPermissions(c, new Developer());
                    break;
                case USER:
                    setPermissions(c, new Administrator());
                    setPermissions(c, new Developer());
                    setPermissions(c, new User());
                    break;
                case VIEWER:
                    setPermissions(c, new Administrator());
                    setPermissions(c, new Developer());
                    setPermissions(c, new User());
                    setPermissions(c, new Viewer());
                    break;
                default:
                    break;
            }
    }
    
    private static void setPermissions(Component c, IAdminComponent admin){

        if(c instanceof BackupDBTablesDialog){
            admin.setComponent((BackupDBTablesDialog)c);
        }
        else if(c instanceof BatchEditWindow){
            admin.setComponent((BatchEditWindow)c);
        }
        else if(c instanceof CompIssuesListWindow){
            admin.setComponent((CompIssuesListWindow)c);
        }
        else if(c instanceof EditDatabaseWindow){
            admin.setComponent((EditDatabaseWindow)c);
        }
        else if(c instanceof LogWindow){
            admin.setComponent((LogWindow)c);
        }
        else if(c instanceof LoginWindow){
            admin.setComponent((LoginWindow)c);
        }
        else if(c instanceof ProjectManagerWindow){
            admin.setComponent((ProjectManagerWindow)c);
        }
    }
}
