
package com.elle.ProjectManager.logic;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * EditableTableModel
 * This class allows switching the table to editable and non editable
 * by overriding the isCellEditable method with a boolean to change
 * it on the fly.
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class EditableTableModel extends DefaultTableModel {

    private boolean cellEditable;
    
    /**
     * CONSTRUCTOR
     * EditableTableModel
     * @param data
     * @param columnNames
     */
    public EditableTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
        cellEditable = false;
    }

    /**
     * isCellEditable
     * Makes table editable or non editable
     * @param row
     * @param col
     * @return 
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        return cellEditable;
    }

    /**
     * isCellEditable
     * @return 
     */
    public boolean isCellEditable() {
        return cellEditable;
    }

    /**
     * setCellEditable
     * @param cellEditable 
     */
    public void setCellEditable(boolean cellEditable) {
        this.cellEditable = cellEditable;
    }
    
    
}
