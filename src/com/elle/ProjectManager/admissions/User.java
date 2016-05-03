
package com.elle.ProjectManager.admissions;

import com.elle.ProjectManager.presentation.*;

/**
 * User
 * The user access level configuration
 * @author Carlos Igreja
 * @since  Mar 1, 2016
 */
public class User extends Developer{

    @Override
    public void setComponent(BackupDBTablesDialog window) {
        super.setComponent(window);
    }

    @Override
    public void setComponent(BatchEditWindow window) {
        super.setComponent(window);
    }

    @Override
    public void setComponent(CompIssuesListWindow window) {
        super.setComponent(window);
    }

    @Override
    public void setComponent(EditDatabaseWindow window) {
        super.setComponent(window);
    }

    @Override
    public void setComponent(LogWindow window) {
        super.setComponent(window);
    }

    @Override
    public void setComponent(LoginWindow window) {
        super.setComponent(window);
    }

    @Override
    public void setComponent(ProjectManagerWindow window) {
        super.setComponent(window);
        
        //#PM364
        //3. Set the access level for the following menu commands to "developer"
        //FILE: Print
        window.getMenuPrint().setEnabled(false);
        //EDIT: Manage databases
        window.getMenuItemManageDBs().setEnabled(false);
        //View: Log, SQL command
        window.getMenuItemLogChkBx().setEnabled(false);
        window.getMenuItemSQLCmdChkBx().setEnabled(false);
        //Tools:comp issues list
        window.getMenuItemCompIssues().setEnabled(false);
    }
}
