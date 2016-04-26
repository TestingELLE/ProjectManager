
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.entities.BackupDBTableRecord;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * IssueDAO
 * @author Carlos Igreja
 * @since  Apr 5, 2016
 */
public class IssueDAO {

    // database table information
    private final String DB_TABLE_NAME = "issues";
    private final String COL_PK_ID = "ID";
    private final String COL_APP = "app";
    private final String COL_TITLE = "title";
    private final String COL_DESCRIPTION = "description";
    private final String COL_PROGRAMMER = "programmer";
    private final String COL_DATE_OPENED = "dateOpened";
    private final String COL_RK = "rk";
    private final String COL_VERSION = "version";
    private final String COL_DATE_CLOSED = "dateClosed";
    private final String COL_ISSUE_TYPE = "issueType";
    private final String COL_SUBMITTER = "submitter";
    private final String COL_LOCKED = "locked";
    
    // components
    private Component parent;
    
    public IssueDAO(){
        this(null);
    }
    
    public IssueDAO(Component parent){
        this.parent = parent;
    }
    
    /**
     * get max id from issues table
     * @return max id from issues table
     */
    public int getMaxId() {
        
        int id = -1;
        DBConnection.close();
        if(DBConnection.open()){

            String sql = "SELECT MAX(" + COL_PK_ID + ") "
                       + "FROM " + DB_TABLE_NAME + ";";

            ResultSet result = null;

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement statement = con.prepareStatement(sql);
                result = statement.executeQuery();
                if(result.next()){
                    id = result.getInt(1);
                }
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
            }
        }
        DBConnection.close();
        return id;
    }

    /**
     * Inserts issue into the database.
     * Insert statement is used to add the issue to the database. 
     * @param sue 
     */
    public boolean insert(Issue issue) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue values
            int id = issue.getId();
            String app = format(issue.getApp());
            String title = format(issue.getTitle());
            String description = format(issue.getDescription());
            String programmer = format(issue.getProgrammer());
            String dateOpened = format(issue.getDateOpened());
            String rk = (issue.getRk().equals(""))?null:issue.getRk(); // no single quotes
            String version = format(issue.getVersion());
            String dateClosed = format(issue.getDateClosed());
            String issueType = format(issue.getIssueType());
            String submitter = format(issue.getSubmitter());
            String locked = format(issue.getLocked());

            String sql = "INSERT INTO " + DB_TABLE_NAME + " (" + COL_PK_ID + ", " 
                    + COL_APP + ", " +  COL_TITLE + ", " +  COL_DESCRIPTION + ", " 
                    +  COL_PROGRAMMER + ", " +  COL_DATE_OPENED + ", " +  COL_RK 
                    + ", " +  COL_VERSION + ", " +  COL_DATE_CLOSED + ", " 
                    +  COL_ISSUE_TYPE + ", " +  COL_SUBMITTER + ", " 
                    +  COL_LOCKED  + ") " 
                    + "VALUES (" + id + ", " + app + ", " +  title + ", " 
                    +  description + ", " +  programmer + ", " +  dateOpened 
                    + ", " +  rk + ", " +  version + ", " +  dateClosed 
                    + ", " +  issueType + ", " +  submitter + ", " 
                    +  locked  + ") ";

            try {
                Statement statement = DBConnection.getStatement();
                statement.executeUpdate(sql);
                LoggingAspect.afterReturn("Upload Successful!");
                successful = true;
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                successful = false;
            }
        }
        DBConnection.close();
        return successful;
    }

    /**
     * update
     * @param issue 
     */
    public boolean update(Issue issue) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue values
            int id = issue.getId();
            String app = format(issue.getApp());
            String title = format(issue.getTitle());
            String description = format(issue.getDescription());
            String programmer = format(issue.getProgrammer());
            String dateOpened = format(issue.getDateOpened());
            String rk = (issue.getRk().equals(""))?null:issue.getRk(); // no single quotes
            String version = format(issue.getVersion());
            String dateClosed = format(issue.getDateClosed());
            String issueType = format(issue.getIssueType());
            String submitter = format(issue.getSubmitter());
            String locked = format(issue.getLocked());
            
            String sql = "UPDATE " + DB_TABLE_NAME + " SET " 
                    + COL_APP + " = " + app + ", "
                    + COL_TITLE + " = " + title + ", "
                    + COL_DESCRIPTION + " = " + description + ", "
                    + COL_PROGRAMMER + " = " + programmer + ", "
                    + COL_DATE_OPENED + " = " + dateOpened + ", "
                    + COL_RK + " = " + rk + ", "
                    + COL_VERSION + " = " + version + ", "
                    + COL_DATE_CLOSED + " = " + dateClosed + ", "
                    + COL_ISSUE_TYPE + " = " + issueType + ", "
                    + COL_SUBMITTER + " = " + submitter + ", "
                    + COL_LOCKED + " = " + locked + " "
                    + "WHERE " + COL_PK_ID + " = " + id + ";";

            try {
                Statement statement = DBConnection.getStatement();
                statement.executeUpdate(sql);
                LoggingAspect.afterReturn("Upload Successful!");
                successful = true;
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                successful = false;
            }
        }
        DBConnection.close();
        return successful;
    }
    
    /**
     * delete
     * @param ids 
     */
    public boolean delete(int[] ids){

        String sqlDelete = ""; // String for the SQL Statement

        if (ids.length != -1) {
            for (int i = 0; i < ids.length; i++) {
                if (i == 0) // this is the first rowIndex
                {
                    sqlDelete += "DELETE FROM " + DB_TABLE_NAME
                            + " WHERE " + COL_PK_ID + " IN (" + ids[i]; 
                } else // this adds the rest of the rows
                {
                    sqlDelete += ", " + ids[i];
                }
            }
            sqlDelete += ");";

            try {

                // delete records from database
                DBConnection.close();
                DBConnection.open();
                DBConnection.getStatement().executeUpdate(sqlDelete);
                LoggingAspect.afterReturn(ids.length + " Record(s) Deleted");
                return true;

            } catch (SQLException e) {
                LoggingAspect.afterThrown(e);
                return false;
            }
        }
        else{
            return false;
        }
    }
    
    /**
     * Formats string to return null or single quotes.
     * This will work for now as all the defaults for
     * the issues table is null. However his could change.
     * This was a last minute fix to get the factoring out.
     * @param s
     * @return 
     */
    private String format(String s){
        return (s.equals(""))?null:"'"+s+"'";
    }
}
