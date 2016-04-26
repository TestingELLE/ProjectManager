
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.sql.SQLException;
import javax.swing.JTable;

/**
 * IssueFilesDAO
 * @author Carlos Igreja
 * @since  Apr 26, 2016
 */
public class IssueFilesDAO {

    /**
     * I was told to ignore this table. It is supposed to be another table
     * in the database so I will start this DAO here. This will just be a 
     * place holder for any code for the DAO while I focus on the IssueDAO.
     */
    
    
    /**
     * This deletes the selected rows on the table from the database
     * @param table
     * @return 
     */
    public boolean delete(JTable table){

        String tableName = table.getName(); // name of the tableSelected
        
        String sqlDelete = ""; // String for the SQL Statement

        int[] selectedRows = table.getSelectedRows(); // array of the rows selected
        int rowCount = selectedRows.length; // the number of rows selected
        if (rowCount != -1) {
            for (int i = 0; i < rowCount; i++) {
                int row = selectedRows[i];
                Integer selectedID = (Integer) table.getValueAt(row, 0); // Add Note to selected taskID

                if (i == 0) // this is the first rowIndex
                {
                    sqlDelete += "DELETE FROM " + tableName
                            + " WHERE " + table.getColumnName(0) + " IN (" + selectedID; // 0 is the first column index = primary key
                } else // this adds the rest of the rows
                {
                    sqlDelete += ", " + selectedID;
                }

            }

            // windowClose the sql statement
            sqlDelete += ");";

            try {

                // delete records from database
                DBConnection.close();
                DBConnection.open();
                DBConnection.getStatement().executeUpdate(sqlDelete);
                LoggingAspect.afterReturn(rowCount + " Record(s) Deleted");
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
}
