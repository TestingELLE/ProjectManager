
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.entities.BackupDBTableRecord;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final String COL_ISSUE_TYPE = "issue_type";
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
        if(DBConnection.open()){

            String sql = "SELECT MAX(" + COL_PK_ID + ") "
                       + "FROM " + DB_TABLE_NAME + ";";

            ResultSet result = null;

            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement statement = con.prepareStatement(sql);
                result = statement.executeQuery();
                id = result.getInt(1);
            }
            catch (SQLException ex) {
                LoggingAspect.afterThrown(ex);
            }
        }
        DBConnection.close();
        return id;
    }
}
