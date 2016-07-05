
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.entities.AccessLevel;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * AccessLevelDAO
 * @author Carlos Igreja
 * @since  May 12, 2016
 */
public class AccessLevelDAO {

    // database table information
    public static final String DB_TABLE_NAME = "accessLevels";
    private final String COL_PK_ID = "ID";
    public static final String COL_USER = "user";
    public static final String COL_ACCESS_LEVEL = "accessLevel";
    
    
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
    
    
     public boolean insert(AccessLevel account) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue id
            int id = account.getId();
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
            String user = account.getUser();
            String accessLevel = account.getAccessLevel();
            
                
            try {
                
                
            String sql = "INSERT INTO " + DB_TABLE_NAME + " (" + COL_PK_ID + ", " 
                    + COL_USER + ", " +  COL_ACCESS_LEVEL + ") " 
                    + "VALUES (" + id + ", '" + user + "', '" +  accessLevel +  "') ";
            
                Connection con = DBConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.execute();
                LoggingAspect.afterReturn("Upload Successful!");
                successful = true;
                //update the id after successful uploading
                if (account.getId() < 0)
                    account.setId(id);
                     
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
                successful = false;
            }
        }
        DBConnection.close();
        return successful;
    }
    
    public boolean update(AccessLevel account) {
        
        boolean successful = false;
        DBConnection.close();
        if(DBConnection.open()){
            
            // set issue values
            int id = account.getId();
            String user = account.getUser();
            String accessLevel = account.getAccessLevel();
            

            try {
                String sql = "UPDATE " + DB_TABLE_NAME + " SET " 
                    + COL_USER + " = '" + user + "', "
                    + COL_ACCESS_LEVEL + " = '" + accessLevel +  "' "
                    + "WHERE " + COL_PK_ID + " = " + id + ";";
                
                System.out.println("update : " + sql );
                Connection con = DBConnection.getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql);
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
    
    
    public boolean delete(int id){

        
        String sqlDelete = "DELETE FROM " + DB_TABLE_NAME
                + " WHERE " + COL_PK_ID + " =" + id;
        try {

            // delete records from database
            DBConnection.close();
            DBConnection.open();
            DBConnection.getStatement().executeUpdate(sqlDelete);
            LoggingAspect.afterReturn("Record is deleted from accessLevels table");
            String levelMessage = "3:" + sqlDelete;
            LoggingAspect.addLogMsgWthDate(levelMessage);
            return true;

        } catch (SQLException e) {
            LoggingAspect.afterThrown(e);
            return false;
        }

        
    }
    
    public ArrayList<AccessLevel> getAll(){
        ArrayList<AccessLevel> accounts = new ArrayList<>();
        ResultSet rs = null;
        String sql = "";
        
        
        sql = "SELECT * FROM " + DB_TABLE_NAME ;
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            while(rs.next()){
                AccessLevel acct = new AccessLevel();
                acct.setId(rs.getInt(COL_PK_ID));
                acct.setUser(rs.getString(COL_USER));
                acct.setAccessLevel(rs.getString(COL_ACCESS_LEVEL));
                
                accounts.add(acct);
            }
            
            LoggingAspect.afterReturn("Loaded accessLevels table ");
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }

        return accounts;
    }
   

    public static String get(String user) {
        String sql = "SELECT * FROM " + DB_TABLE_NAME +
                      " WHERE " + COL_USER + " = '" + user +"';";

        ResultSet rs = null;
        AccessLevel accessLevel = new AccessLevel();
        
        try {

            DBConnection.close();
            DBConnection.open();
            rs = DBConnection.getStatement().executeQuery(sql);
            
            while(rs.next()){
                accessLevel.setUser(rs.getString(COL_USER));
                accessLevel.setAccessLevel(rs.getString(COL_ACCESS_LEVEL));
            }
            
            LoggingAspect.afterReturn("Loaded access level from " + DB_TABLE_NAME + " for " + accessLevel.getUser());
        } 
        catch (SQLException e) {
            LoggingAspect.afterThrown(e);
        }
        
        return accessLevel.getAccessLevel();
    }
    
    
}
