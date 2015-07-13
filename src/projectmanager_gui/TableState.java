/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectmanager_gui;

import javax.swing.table.TableRowSorter;
import javax.swing.JTable;

import java.util.*;

/**
 *
 * @author Louis W.
 */
public class TableState {
        
//    private Analyster ana;  // should not attach "= new Analyster()"
    private JTable table;
    private int rowsNum;
    private int columnsNum;
    private int recordsNum;
    private TableRowSorter sorter; 
//    String tabName;
//    String tableName;
    private String[] searchFields;
//    JTable table;
    private Vector columnNames = new Vector();
    private Vector data = new Vector();
  
    
    
    public TableState () {
        // to do  
    }
    
    public TableState (JTable t) {
        table = t;    
    }
    
    public void init (JTable t, String[] sf) {  // init should only be called after "select * from" command in Analyster (loadData)
        table = t;    
//        rowsNum = table.getRowCount();        // initialize records number in connection of Analyster
        columnsNum = table.getColumnCount();
        recordsNum = table.getRowCount();
        setSearchFields(sf);
        setColumnNames();
        setData();
        // (to do) initialize sorter
    }
    
//    public String getSelectedTabName() {
//        String selectedTab=ana.getTabName();
//        return selectedTab;
//    }
//    
//    public JTable getSelectedTable() {
//        JTable selectedTable = ana.getSelectedTable();
//        return selectedTable;
//    }
    
//    public long getRecordsNumber(JTable table) {
    public int getRecordsNumber() {
//        String tableName = ana.getTableName(table);
//        
//        if (tableName.equals("Assignments")) {
//             return ana.table1Records;
//        } else {
//            return ana.table2Records;
//        } 
        return recordsNum;
    }
    
//    public String getSelectedTableName() {
//        String selectedTab = ana.getTabName();
//        
//        if (selectedTab.equals("Assignments")) {
//             return "Assignments";
//        } else {
//            return "Reports";
//        }
//    }
    
    public Vector getColumnNames() {
//        String selectedTab = ana.getTabName();
//        
//        if (selectedTab.equals("Assignments")) {
//             return ana.columnNames1;
//        } else {
//            return ana.columnNames2;
//        }
        return columnNames;
    }
    
    public Vector getData() {
//        String selectedTab = ana.getTabName();
//        
//        if (selectedTab.equals("Assignments")) {
//             return ana.data1;
//        } else {
//            return ana.data2;
//        }
        return data;
    }
    
    public String[] getSearchFields() {
//        String selectedTab = ana.getTabName();
//        String[] Assignment = {"Symbol", "Analyst"}, Reports = {"Symbol", "Author"};
//        
//        if (selectedTab.equals("Assignments")) {
//            return Assignment;
//        } else {
//            return Reports;
//        }
        return searchFields;
    }
    
//    public long getRowsNumber(String tableName) {
    public int getRowsNumber() {
//        return ana.getTable(tableName).getRowCount();
        return rowsNum;
    }
        
    public TableRowSorter getSorter() {
        return sorter;
    }
    
//    public void setData(String tableName, Vector dataset) {
    public void setData(Vector dataset) {
//        if (tableName.equals("Assignments")) {
//            ana.data1 = dataset;
//        } else {
//            ana.data2 = dataset;
//        }
        data = dataset;
    }
    
    public void setData() {
//        if (tableName.equals("Assignments")) {
//            ana.data1 = dataset;
//        } else {
//            ana.data2 = dataset;
//        }
        
        data.clear();
        for (int i = 0; i < rowsNum; i++) {
            Vector rowData = new Vector(columnsNum);
            for (int j = 0; j < columnsNum; j++) {
                rowData.add(table.getValueAt(i, j));
            }
            data.add(rowData);
        }
 
    }
//    public void setColumnNames(String tableName, Vector colName) {
    public void setColumnNames() {
//        if (tableName.equals("Assignments")) {
//            ana.columnNames1 = colName;
//        } else {
//            ana.columnNames2 = colName;
//        }
        int i;
        columnNames.clear();
        
        for (i = 0; i < table.getColumnCount(); i++) {
            columnNames.add(table.getColumnName(i));
        }
    }
    
    public void setColumnNames(Vector col) {
        columnNames = col;
    }
   
    public void setRowsNumber(int num) {
        rowsNum = num;
    }
    
//    public void setRecordsNumber(JTable table, long num) {
    public void setRecordsNumber(int num) {
//        String tableName = ana.getTableName(table);
//
//        if (tableName.equals("Assignments")) {
//            ana.table1Records = num;
//        } else {
//            ana.table2Records = num;
//        }
        recordsNum = num;
    }
    
    public void setSearchFields(String[] temp) {
        searchFields = temp;
    }
    
}
