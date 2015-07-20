/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectmanager_gui;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author shanxijin
 */
class MyTableModel extends DefaultTableModel {

    boolean isFiltering;

    public MyTableModel(Object rowData[][], Object columnNames[], boolean filteringStatus) {
        super(rowData, columnNames);
        isFiltering = filteringStatus;
    }

    public MyTableModel(Vector data, Vector columnNames, boolean filteringStatus) {
        
        // Set the default table model to establish a table to store the data.
        super(data, columnNames);
        isFiltering = filteringStatus;
    }

//    public MyTableModel(Vector data, boolean filteringStatus) {
//        isFiltering = filteringStatus;
//    }
    public MyTableModel(boolean filteringStatus) {
        isFiltering = filteringStatus;
    }

    @Override
    public Class getColumnClass(int col) {
        /*if (col == 0 || col == 2) // second column accepts only Integer values
         return Integer.class;
         else if (col == 3 || col == 5)
         return SimpleDateFormat.class;
         else
         return String.class; // other columns accept String values
         */
        if (col == 0) {
            return Integer.class;
        } else {
            return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
//        JOptionPane.showMessageDialog(null, isFiltering);
        if (col == 0) // first column will be uneditable
        {
            return false;
        } else if (isFiltering == true) // when edit mode is off
        {
            return false;
        } else {
            return true;
        }
    }

//    @Override
//    public void setValueAt(Object value, int row, int col) {
//        data[row][col] = ((Integer) value).intValue();   // originalBoard: name of array
//        System.out.println("Setting value");
//        fireTableCellUpdated(row, col);
//  // return true;
//    }
//        public void setCellEditable(int row, int col, boolean value) {
//            this. editable_cells[row][col] = value; // set cell true/false
//            this. fireTableCellUpdated(row, col);
//        }
}
