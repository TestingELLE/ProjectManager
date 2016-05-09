
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.logic.EditableTableModel;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.awt.Component;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * SqlOutputWindowDAO
 * @author Carlos Igreja
 * @since  Mar 24, 2016
 */
public class SqlOutputWindowDAO {

    private Component parentComponent;
    
    public SqlOutputWindowDAO(){
        this(null);
    }
    
    public SqlOutputWindowDAO(Component parentComponent){
        this.parentComponent = parentComponent;
    }
    
    public DefaultTableModel getTableModel(String sqlCommand){
        
        DefaultTableModel tableModel = null;
        
        if (sqlCommand.toLowerCase().trim().startsWith("select")
                || sqlCommand.toLowerCase().trim().startsWith("show")
                || sqlCommand.toLowerCase().trim().startsWith("describe")) {
        Vector data = new Vector();
        Vector columnNames = new Vector();
        int columns;

        ResultSet rs = null;
        ResultSetMetaData metaData = null;
        
        DBConnection.close();
        DBConnection.open();
        try {
            rs = DBConnection.getStatement().executeQuery(sqlCommand);
            metaData = rs.getMetaData();
        } catch (Exception ex) {
            String msg = "There was an error: \n" + ex.getMessage();
            JOptionPane.showMessageDialog(parentComponent, msg);
            LoggingAspect.afterThrown(ex);
            return tableModel;
        }
        try {
            columns = metaData.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                columnNames.addElement(metaData.getColumnName(i));
            }
            while (rs.next()) {
                Vector row = new Vector(columns);
                for (int i = 1; i <= columns; i++) {
                    row.addElement(rs.getObject(i));
                }
                data.addElement(row);
            }
            rs.close();

        } catch (SQLException ex) {
            String msg = "There was an error: \n" + ex.getMessage();
            JOptionPane.showMessageDialog(parentComponent, msg);
            LoggingAspect.afterThrown(ex);
        }

        tableModel = new DefaultTableModel(data, columnNames);
        
        return tableModel;
        }
        else{
            String msg = "Only select, show, or describe commands are allowed.";
            JOptionPane.showMessageDialog(parentComponent, msg);
            return tableModel;
        }
    }

    public Component getParentComponent() {
        return parentComponent;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }
    
    
}
