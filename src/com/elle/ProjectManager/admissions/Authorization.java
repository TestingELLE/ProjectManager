package com.elle.ProjectManager.admissions;

import com.elle.ProjectManager.dao.AccessLevelDAO;
import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.presentation.*;
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

    // constants
    private static final String ADMINISTRATOR = "administrator";
    private static final String DEVELOPER = "developer";
    private static final String USER = "user";
    private static final String VIEWER = "viewer";
    
    // class variables
    private static String userLogin;
    private static String accessLevel;
    private static IAdminComponent adminComponent;
    
    /**
     * When the user logs in, we will need to know the access level and 
     * restrict the application accordingly per user type or access level.
     * This information will be stored and retrieved from the database.
     * This method will get the required information from the database.
     */
    public static boolean getInfoFromDB(){
        userLogin = DBConnection.getUserName();
        accessLevel = AccessLevelDAO.get(userLogin);

        if(accessLevel != null){
            setAdminComponentType(accessLevel);
            return true;
        }
        else{
            accessLevel = USER; // defaults to user
            setAdminComponentType(accessLevel);
            return false;
        }
    }
    
    /**
     * Sets the IAdminComponent according to the accessLevel
     * @param accessLevel access level of the user
     */
    public static void setAdminComponentType(String accessLevel){
        if(accessLevel != null) // changed tab state is called from initComponents
            switch(accessLevel){
                case ADMINISTRATOR:
                    adminComponent = new Administrator();
                    break;
                case DEVELOPER:
                    adminComponent = new Developer();
                    break;
                case USER:
                    adminComponent = new User();
                    break;
                case VIEWER:
                    adminComponent = new Viewer();
                    break;
                default:
                    break;
            }
    }
    
    /**
     * This takes any component and overrides any behavior for that component.
     * @param c 
     */
    public static void authorize( Component c){
        setPermissions(c, adminComponent);
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
        else if(c instanceof IssueWindow){
            admin.setComponent((IssueWindow)c);
        }
    }

    public static String getAccessLevel() {
        return accessLevel;
    }
    
    
}
