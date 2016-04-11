package com.elle.ProjectManager.admissions;

import com.elle.ProjectManager.presentation.*;

/**
 * 
 * @author Carlos Igreja
 */
public interface IAdminComponent {

    public abstract void setComponent(BackupDBTablesDialog window);
    public abstract void setComponent(BatchEditWindow window);
    public abstract void setComponent(CompIssuesListWindow window);
    public abstract void setComponent(EditDatabaseWindow window);
    public abstract void setComponent(LogWindow window);
    public abstract void setComponent(LoginWindow window);
    public abstract void setComponent(ProjectManagerWindow window);
}
