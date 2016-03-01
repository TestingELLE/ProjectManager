
package com.elle.ProjectManager.admissions;

import com.elle.ProjectManager.presentation.*;

/**
 * Developer
 * The developer access level configuration
 * @author Carlos Igreja
 * @since  Mar 1, 2016
 */
public class Developer implements IAdminComponent{

    @Override
    public void setComponent(AddIssueFileWindow window) {
    }

    @Override
    public void setComponent(AddIssueWindow window) {
    }

    @Override
    public void setComponent(BackupDBTablesDialog window) {
    }

    @Override
    public void setComponent(BatchEditWindow window) {
    }

    @Override
    public void setComponent(CompIssuesListWindow window) {
    }

    @Override
    public void setComponent(EditDatabaseWindow window) {
    }

    @Override
    public void setComponent(LogWindow window) {
    }

    @Override
    public void setComponent(LoginWindow window) {
    }

    @Override
    public void setComponent(PopupWindowInTableCell window) {
    }

    @Override
    public void setComponent(ProjectManagerWindow window) {
        
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

    @Override
    public void setComponent(ReportWindow window) {
    }

}
