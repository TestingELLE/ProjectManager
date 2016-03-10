
package com.elle.ProjectManager.dbtables;

/**
 * BackupDBTableRecord
 *
 * @author Carlos Igreja
 * @since Mar 7, 2016
 */
public class BackupDBTableRecord {

    private int id;
    private String applicationName;
    private String tableName;
    private String backupTableName;

    public BackupDBTableRecord(int id, String applicationName, String tableName, String backupTableName) {
        this.id = id;
        this.applicationName = applicationName;
        this.tableName = tableName;
        this.backupTableName = backupTableName;
    }

    public BackupDBTableRecord() {
        this(0, null, null, null);
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getTableName() {

        return tableName;
    }

    public void setTableName(String tableName) {

        this.tableName = tableName;
    }

    public String getBackupTableName() {

        return backupTableName;
    }

    public void setBackupTableName(String backupTableName) {

        this.backupTableName = backupTableName;
    }


}
