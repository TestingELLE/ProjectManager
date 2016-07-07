
package com.elle.ProjectManager.logic;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
 * 
 * Modified by Corinne Martus
 * June 16, 2016
 * Version 1.9.0c
 * To recieve a DefaultTableModel and retrieve the column names, 
 * class types, and data from the model
 */
public class EditableTableModel extends DefaultTableModel {
    Class [] colClassTypes;
    private boolean cellEditable;
   Vector columnNames;
    /**
     * CONSTRUCTOR EditableTableModel
     */
    public EditableTableModel(DefaultTableModel sourceModel) {  
        super(sourceModel.getDataVector(),getColumnNamesFromModel(sourceModel));
        cellEditable = false;
        copyColumnClasses(sourceModel);
    }
    public static Vector getColumnNamesFromModel (DefaultTableModel sourceModel){
       Vector names = new Vector();
        for (int col = 0; col < sourceModel.getColumnCount(); col ++){
            names.addElement(sourceModel.getColumnName(col));
        }
        return names;
    }

    /**
     * isCellEditable Makes table editable or non editable
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
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
     *
     * @param cellEditable
     */
    public void setCellEditable(boolean cellEditable) {
        this.cellEditable = cellEditable;
    }
    
    /*public void getSelectedRowContent(int Id){
        
    }*/
    /**
     * Override getColumnClass() in DefaultTableModel     *
     * @param columnIndex
     */
    
    public Class getColumnClass(int columnIndex){
        Class columnClassType;
        columnClassType = colClassTypes[columnIndex];
        return columnClassType;
    }
    
    private void copyColumnClasses(DefaultTableModel sourceModel) {
        {
            colClassTypes = new Class[sourceModel.getColumnCount()];
               for (int col = 0; col < sourceModel.getColumnCount(); col++){
                   colClassTypes[col] = sourceModel.getColumnClass(col);
               }
            }
    }
}
