
package com.elle.ProjectManager.admissions;

import com.elle.ProjectManager.presentation.*;

/**
 * Viewer
 * The viewer access level configuration
 * @author Carlos Igreja
 * @since  Mar 1, 2016
 */
public class Viewer extends User{

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
    }
}
