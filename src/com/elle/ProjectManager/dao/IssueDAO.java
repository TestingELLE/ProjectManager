
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.database.ModifiedData;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.LoggingAspect;
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
    private final String COL_LASTMODTIME = "lastmodtime";
  
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
    
    public Issue get(int id) {
        ResultSet rs = null;
        String sql = "";
        
        
            sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE ID = " + "'" 
            + id + "'";
        Issue issue = new Issue();
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                
                issue.setId(rs.getInt(COL_PK_ID));
                issue.setApp(rs.getString(COL_APP));
                issue.setTitle(rs.getString(COL_TITLE));
                issue.setDescription(rs.getBytes(COL_DESCRIPTION));
                issue.setProgrammer(rs.getString(COL_PROGRAMMER));
                issue.setDateOpened(rs.getString(COL_DATE_OPENED));
                issue.setRk(rs.getString(COL_RK));
                issue.setVersion(rs.getString(COL_VERSION));
                issue.setDateClosed(rs.getString(COL_DATE_CLOSED));
                issue.setIssueType(rs.getString(COL_ISSUE_TYPE));
                issue.setSubmitter(rs.getString(COL_SUBMITTER));
                issue.setLocked(rs.getString(COL_LOCKED));
                issue.setLastmodtime(rs.getString(COL_LASTMODTIME));
                
                
            }
     
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return issue;
        
    }
    public boolean insert(Issue issue) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue id
            int id = issue.getId();
            if (id < 0) {
                String sql = "SELECT MAX(" + COL_PK_ID + ") "
                       + "FROM " + DB_TABLE_NAME + ";";

                ResultSet result = null;
                int maxId = -1;

                try {
                    Connection con = DBConnection.getConnection();
                    PreparedStatement statement = con.prepareStatement(sql);
                    result = statement.executeQuery();
                    if(result.next()){
                        maxId = result.getInt(1);
                    }
                    id = maxId + 1;
                    
                }
                catch (SQLException ex) {
                    LoggingAspect.afterThrown(ex);
                    return false;
                }
                
                
            }
            String app = format(issue.getApp());
            String title = format(issue.getTitle());
            byte[] description = issue.getDescription();
            String programmer = format(issue.getProgrammer());
            String dateOpened = format(issue.getDateOpened());
            String rk = (issue.getRk().equals(""))?null:issue.getRk(); // no single quotes
            String version = format(issue.getVersion());
            String dateClosed = format(issue.getDateClosed());
            String issueType = format(issue.getIssueType());
            String submitter = format(issue.getSubmitter());
            String locked = format(issue.getLocked());
                
            try {
                
                
            String sql = "INSERT INTO " + DB_TABLE_NAME + " (" + COL_PK_ID + ", " 
                    + COL_APP + ", " +  COL_TITLE + ", " +  COL_DESCRIPTION + ", " 
                    +  COL_PROGRAMMER + ", " +  COL_DATE_OPENED + ", " +  COL_RK 
                    + ", " +  COL_VERSION + ", " +  COL_DATE_CLOSED + ", " 
                    +  COL_ISSUE_TYPE + ", " +  COL_SUBMITTER + ", " 
                    +  COL_LOCKED  +  ") " 
                    + "VALUES (" + id + ", " + app + ", " +  title + ", " 
                    +  "?" + ", " +  programmer + ", " +  dateOpened 
                    + ", " +  rk + ", " +  version + ", " +  dateClosed 
                    + ", " +  issueType + ", " +  submitter + ", " 
                    +  locked  +  ") ";
            
                Connection con = DBConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setBytes(1, description);
                pstmt.execute();
                LoggingAspect.afterReturn("Upload Successful!");
                successful = true;
                //update the id after successful uploading
                if (issue.getId() < 0)
                    issue.setId(id);
                     
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                successful = false;
            }
        }
        DBConnection.close();
        return successful;
    }
    
    private String processCellValue(String cellValue) {

        return cellValue.replaceAll("'", "''");
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
            byte[] description = issue.getDescription();
            String programmer = format(issue.getProgrammer());
            String dateOpened = format(issue.getDateOpened());
            String rk = (issue.getRk().equals(""))?null:issue.getRk(); // no single quotes
            String version = format(issue.getVersion());
            String dateClosed = format(issue.getDateClosed());
            String issueType = format(issue.getIssueType());
            String submitter = format(issue.getSubmitter());
            String locked = format(issue.getLocked());
            
   
            

            try {
                String sql = "UPDATE " + DB_TABLE_NAME + " SET " 
                    + COL_APP + " = " + app + ", "
                    + COL_TITLE + " = " + title + ", "
                    + COL_DESCRIPTION + " = " + "?" + ", "
                    + COL_PROGRAMMER + " = " + programmer + ", "
                    + COL_DATE_OPENED + " = " + dateOpened + ", "
                    + COL_RK + " = " + rk + ", "
                    + COL_VERSION + " = " + version + ", "
                    + COL_DATE_CLOSED + " = " + dateClosed + ", "
                    + COL_ISSUE_TYPE + " = " + issueType + ", "
                    + COL_SUBMITTER + " = " + submitter + ", "
                    + COL_LOCKED + " = " + locked + " "
                    + "WHERE " + COL_PK_ID + " = " + id + ";";
                
                System.out.println("update : " + sql );
                Connection con = DBConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setBytes(1, description);
                pstmt.execute();
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
                String levelMessage = "3:" + sqlDelete;
                LoggingAspect.addLogMsgWthDate(levelMessage);
                return true;

            } catch (SQLException e) {
                LoggingAspect.afterThrown(e);
                return false;
            }
        }
        else{
            // ids were passed in empty
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
        s=processCellValue(s);
        return (s.equals(""))?null:"'"+s+"'";
    }

    private Object processCellValue(Object cellValue) {
        return cellValue.toString().replaceAll("'", "''");
    }
    
    /**
     * update
     * @param tableName
     * @param modifiedData
     * @return 
     */
    public boolean update(String tableName,ModifiedData modifiedData) {
        
        boolean updateSuccessful = true;
        String sqlChange = null;

        DBConnection.close();
        if (DBConnection.open()) {

            String columnName = modifiedData.getColumnName();
            Object value = modifiedData.getValue();
            value = processCellValue(value);
            int id = modifiedData.getId();

            try {

                if (value.equals("")) {
                    value = null;
                    sqlChange = "UPDATE " + tableName + " SET " + columnName
                            + " = " + value + " WHERE ID = " + id + ";";
                } else {
                    sqlChange = "UPDATE " + tableName + " SET " + columnName
                            + " = '" + value + "' WHERE ID = " + id + ";";
                }

                DBConnection.getStatement().executeUpdate(sqlChange);
                LoggingAspect.afterReturn(sqlChange);

            } catch (SQLException e) {
                LoggingAspect.addLogMsgWthDate("3:" + e.getMessage());
                LoggingAspect.addLogMsgWthDate("3:" + e.getSQLState() + "\n");
                LoggingAspect.addLogMsgWthDate(("Upload failed! " + e.getMessage()));
                LoggingAspect.afterThrown(e);
                updateSuccessful = false;
            }
            if (updateSuccessful) {
                LoggingAspect.afterReturn(("Edits uploaded successfully!"));
            }
        } else {
            // connection failed
            LoggingAspect.afterReturn("Failed to connect");
        }
        // finally close connection
        DBConnection.close();
        return updateSuccessful;
    }

    public ArrayList<Issue> get(String tableName) {
        
        ArrayList<Issue> issues = new ArrayList<>();
        ResultSet rs = null;
        String sql = "";
        
        if(tableName.equals("Other")){
            sql = "SELECT * FROM " + DB_TABLE_NAME 
               + " WHERE app != 'PM' and app != 'Analyster'"
               + " AND app != 'ELLEGUI' or app IS NULL";
        }
        else{
            sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE app = " + "'" 
            + tableName + "' ORDER BY case when dateClosed IS null then 1 else 0 end, dateClosed asc, ID ASC";
        }
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                Issue issue = new Issue();
                issue.setId(rs.getInt(COL_PK_ID));
                issue.setApp(rs.getString(COL_APP));
                issue.setTitle(rs.getString(COL_TITLE));
                issue.setDescription(rs.getBytes(COL_DESCRIPTION));
                issue.setProgrammer(rs.getString(COL_PROGRAMMER));
                issue.setDateOpened(rs.getString(COL_DATE_OPENED));
                issue.setRk(rs.getString(COL_RK));
                issue.setVersion(rs.getString(COL_VERSION));
                issue.setDateClosed(rs.getString(COL_DATE_CLOSED));
                issue.setIssueType(rs.getString(COL_ISSUE_TYPE));
                issue.setSubmitter(rs.getString(COL_SUBMITTER));
                issue.setLocked(rs.getString(COL_LOCKED));
                issue.setLastmodtime(rs.getString(COL_LASTMODTIME));
                issues.add(issue);
            }
            
            LoggingAspect.afterReturn("Loaded table " + tableName);
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return issues;
    }
    
    public Issue getSelectedRow(String tableName, String row) {
        
       
      
        ResultSet rs = null;
        String sql = "";
        
        
            sql = "SELECT * FROM " + DB_TABLE_NAME + " WHERE ID = " + "'" 
            + row + "'";
        Issue issue = new Issue();
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                
                issue.setId(rs.getInt(COL_PK_ID));
                issue.setApp(rs.getString(COL_APP));
                issue.setTitle(rs.getString(COL_TITLE));
                issue.setDescription(rs.getBytes(COL_DESCRIPTION));
                issue.setProgrammer(rs.getString(COL_PROGRAMMER));
                issue.setDateOpened(rs.getString(COL_DATE_OPENED));
                issue.setRk(rs.getString(COL_RK));
                issue.setVersion(rs.getString(COL_VERSION));
                issue.setDateClosed(rs.getString(COL_DATE_CLOSED));
                issue.setIssueType(rs.getString(COL_ISSUE_TYPE));
                issue.setSubmitter(rs.getString(COL_SUBMITTER));
                issue.setLocked(rs.getString(COL_LOCKED));
                issue.setLastmodtime(rs.getString(COL_LASTMODTIME));          
            }
            
            LoggingAspect.afterReturn("Loaded table " + tableName);
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return issue;
    }
}
