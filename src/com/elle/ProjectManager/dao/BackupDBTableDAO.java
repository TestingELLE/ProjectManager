
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.database.SQL_Commands;
import com.elle.ProjectManager.entities.BackupDBTableRecord;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * BackupDBTableDAO
 * @author Carlos Igreja
 * @since  Mar 7, 2016
 */
public class BackupDBTableDAO {

    // database table information
    private final String DB_TABLE_NAME = "Table_Backups";
    private final String COL_PK_ID = "id";
    private final String COL_APPLICATION = "applicationName";
    private final String COL_TABLE_NAME = "tableName";
    private final String COL_BACKUP_NAME = "backupTableName";
    private final String APPLICATION_NAME = "PM";
    
    // components
    private Component parentComponent;
    
    public BackupDBTableDAO(Component parentComponent){

        this.parentComponent = parentComponent;
    }
    
    public void createDBTableToStoreBackupsInfo(){

        if(DBConnection.open()){
            String createTableQuery = 
                "CREATE TABLE " + DB_TABLE_NAME +
                "(" +
                        COL_PK_ID + " int(4) PRIMARY KEY AUTO_INCREMENT, " +
                        COL_APPLICATION + " VARCHAR(50) NOT NULL, " +
                        COL_TABLE_NAME + " VARCHAR(50) NOT NULL, " +
                        COL_BACKUP_NAME + " VARCHAR(50) NOT NULL " +
                ");";

            try {
                DBConnection.getStatement().executeUpdate(createTableQuery);
                LoggingAspect.afterReturn("Created table " + DB_TABLE_NAME);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(parentComponent, 
                        "unable to create table " + DB_TABLE_NAME);
                LoggingAspect.afterThrown(ex);
            }
        }
        DBConnection.close();
    }

    public ArrayList<BackupDBTableRecord> getRecords() {
        
        ArrayList<BackupDBTableRecord> records = new ArrayList<>();
        
        if(DBConnection.open()){

            // sql query to return a result set
            String sql = "SELECT * " +
                         " FROM " + DB_TABLE_NAME +
                         " WHERE " + COL_APPLICATION + " = '" + APPLICATION_NAME + "' ;";

            ResultSet result = null;

            try {
                //Creating a variable to execute query
                result = DBConnection.getStatement().executeQuery(sql);

            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                // if table doesn't exist and needs to be created
                if(ex.getMessage().endsWith("exist")){
                    createDBTableToStoreBackupsInfo();
                    try {
                        result = DBConnection.getStatement().executeQuery(sql);
                    } catch (SQLException ex1) {
                    }
                }
            }
            finally{
                // create checkbox items from result set and load up array list
                if(result != null){
                    try {
                        while(result.next())
                        {
                            BackupDBTableRecord record = new BackupDBTableRecord();
                            record.setId(result.getInt(COL_PK_ID));
                            record.setApplicationName(result.getString(COL_APPLICATION));
                            record.setTableName(result.getString(COL_TABLE_NAME));
                            record.setBackupTableName(result.getString(COL_BACKUP_NAME));
                            records.add(record);
                        }
                    } catch (SQLException ex) {
                        LoggingAspect.afterThrown(ex);
                    }
                }
            }
        }
        DBConnection.close();
        return records;
    }

    /**
     * Deletes record of the backup table (not the actual backup table)
     * @param record database table record
     * @return boolean true if successful and false if sql error occurred 
     */
    public boolean deleteRecord(BackupDBTableRecord record) {

        if(DBConnection.open()){
            String sql = "";
            int id = record.getId();
            String backupTableName = record.getBackupTableName();

            try {
                // drop table
                sql = "DROP TABLE " + backupTableName + " ; ";
                DBConnection.getStatement().executeUpdate(sql);

                // delete record
                sql = "DELETE FROM " + DB_TABLE_NAME +
                        " WHERE " + COL_PK_ID + " = " + id + ";";
                DBConnection.getStatement().executeUpdate(sql);

                LoggingAspect.afterReturn("Deleted backup " + backupTableName);
                DBConnection.close();
                return true;
            } catch (SQLException e) {
                LoggingAspect.afterThrown(e);
                DBConnection.close();
                return false;
            }
        }
        else{
            DBConnection.close();
            return false;
        }
    }

    /**
     * Deletes record of the backup table (not the actual backup table)
     * @param backupTableName name of backup table to delete
     * @return boolean true if successful and false if sql error occurred
     */
    public boolean deleteRecord(String backupTableName) {

        if(DBConnection.open()){
            String sql = "";

            try {
                // drop table
                sql = "DROP TABLE " + backupTableName + " ; ";
                DBConnection.getStatement().executeUpdate(sql);

                // delete record
                sql = "DELETE FROM " + DB_TABLE_NAME +
                        " WHERE " + COL_BACKUP_NAME + " = '" + backupTableName + "';";
                DBConnection.getStatement().executeUpdate(sql);

                LoggingAspect.afterReturn("Deleted backup " + backupTableName);
                DBConnection.close();
                return true;
            } catch (SQLException e) {
                LoggingAspect.afterThrown(e);
                DBConnection.close();
                return false;
            }
        }
        else{
            DBConnection.close();
            return false;
        }
    }
    
    public boolean addRecord(BackupDBTableRecord record){

        if(DBConnection.open()){
            String sql = "";
            String tableName = record.getTableName();
            String backupTableName = record.getBackupTableName();

            try {

                // create the backup table
                sql = "CREATE TABLE " + backupTableName
                        + " LIKE " + tableName + " ; ";
                DBConnection.getStatement().executeUpdate(sql);

                // backup the table data
                sql =  "INSERT INTO " + backupTableName
                        + " SELECT * FROM " + tableName +  " ;";
                DBConnection.getStatement().executeUpdate(sql);

                // add record
                sql = "INSERT INTO " + DB_TABLE_NAME + " ( " + COL_APPLICATION + ", " + COL_TABLE_NAME + ", " + COL_BACKUP_NAME + ")"
                        + " VALUES ('" + APPLICATION_NAME + "', '" +  tableName + "', '" +  backupTableName + "');";
                DBConnection.getStatement().executeUpdate(sql);

                LoggingAspect.afterReturn("Created backup " + backupTableName);
                DBConnection.close();
                return true;
            } catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                DBConnection.close();
                return false;
            }
        }
        else{
            DBConnection.close();
            return false;
        }
    }

    public boolean updateRecord(BackupDBTableRecord record, String oldTableName) {
        
        if(DBConnection.open()){
            int id = record.getId();
            String app = record.getApplicationName();
            String tableName = record.getTableName();
            String backupName = record.getBackupTableName();

            String update = "UPDATE " + DB_TABLE_NAME +
                           " SET " + COL_APPLICATION  + " = '" + app + "', "
                           + COL_TABLE_NAME + " = '" + tableName + "', "
                           + COL_BACKUP_NAME + " = '" + backupName + 
                          "' WHERE " + COL_PK_ID  + " = " + id + ";";
            
            String rename = "RENAME TABLE " + oldTableName + 
                           " TO " + backupName;
            try {
                DBConnection.getStatement().executeUpdate(rename);
                LoggingAspect.afterReturn(update);
                DBConnection.getStatement().executeUpdate(update);
                LoggingAspect.afterReturn(update);
                DBConnection.close();
                return true;
            } catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                DBConnection.close();
                return false;
            }
        }
        else{
            DBConnection.close();
            return false;
        }
        
    }
}
