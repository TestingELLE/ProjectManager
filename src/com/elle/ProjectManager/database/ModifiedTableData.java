package com.elle.ProjectManager.database;

import java.util.ArrayList;
import javax.swing.JTable;

/**
 * ModifiedTableData
 * This class is used to store table model data
 * and new data changed to the table model.
 * The old data can then be compared with the new 
 * data to know if changes have been made.
 * The new changes can then be uploaded to the 
 * database.
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */

public class ModifiedTableData{
    
    // attributes
    private JTable table;                       // the table 
    private String oldData[][];                 // original data values
    private ArrayList<ModifiedData> newData;    // new data values
    
    /**
     * CONSTRUCTOR
     * ModifiedTableData
     * @param table 
     */
    public ModifiedTableData(JTable table) {
        this.table = table;
        newData = new ArrayList<>();
        
        // initialize data for the table model
        reloadData();
    }
    
    /**
     * reloadData
     * This reloads the data from the table model
     */
    public void reloadData(){
        
        // initialize the array size
        oldData = new String[table.getModel().getRowCount()][table.getColumnCount()];
        
        // load the array with values
        for(int row = 0; row < table.getModel().getRowCount(); row++){
            for(int col = 0; col < table.getColumnCount(); col++){
                Object value = table.getModel().getValueAt(row, col);
                if(value == null)
                    value ="";
                oldData[row][col]  = value.toString();
            }
        }
    }

    public String[][] getOldData() {
        return oldData;
    }

    public ArrayList<ModifiedData> getNewData() {
        return newData;
    }

   
    
    
}
