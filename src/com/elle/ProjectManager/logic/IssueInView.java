///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.elle.ProjectManager.logic;
//
//import java.util.Vector;
//import javax.swing.JTable;
//
///**
// *
// * @author fuxiaoqian
// */
//public class IssueInView {
//    private int id;
//    private int selectedRow;
//    private int selectedCol;
//    private int rowInModel;
//    private String[] columnNames;
//    private JTable table;
//    private EditableTableModel tableModel;
//    private String tabName;
//    
//    private Vector<EditableData> issueValues;
//    
//    
//    public IssueInView(int Row, int Col, Tab selectedTab, String selectedTabName){
//        table = selectedTab.getTable();
//        tabName = selectedTabName;
//        tableModel = (EditableTableModel) table.getModel();
//        selectedRow = Row;
//        selectedCol = Col;
//        
//        id = (int) table.getValueAt(selectedRow, 0);
//        
////        System.out.println("id is: " + id);
//        
//        columnNames = selectedTab.getTableColNames();
//        
//        issueValues = new Vector<EditableData>();
//        for(int i = 0; i < table.getColumnCount(); i++){
//            Object value = table.getValueAt(selectedRow, i);
//            String columnName = table.getColumnName(i);
//            EditableData data = new EditableData(table.getName(), columnName,
//            value, id);
//            issueValues.addElement(data);
//            System.out.println(table.getName() + " "  + columnName + " add: " + value);
//        }
//        
//        selectedRow = Row;
//        for(int index = 0; index < tableModel.getRowCount(); index++){
//            if((int)tableModel.getValueAt(index, 0) == id){
//                rowInModel = index;
//            }
//        }
//        
//        
//    }
//    
//    public Vector getIssueValues(){
//        return issueValues;
//    }
//    
//    public Object getIssueValueAt(int col){
//        return issueValues.elementAt(col).getValue();
//    }
//    
//    public int getID(){
//        return id;
//    }
//    
//    public String getTabName(){
//        return tabName;
//    }
//    
//    public int getRowNumInTableModel(){
//        return rowInModel;
//    }
//    
//    public int getSelectedRowInTable(){
//        return selectedRow;
//    }
//    
//    public int getSelectedColumnInTable(){
//        return selectedCol;
//    }
//    
//    public String[] getSelectedIssueColumnNames(){
//        return columnNames;
//    }
//    
//    public JTable getSelectedTable(){
//        return table;
//    }
//    
//    public EditableTableModel getSelectedTableModel(){
//        return tableModel;
//    }
//    
//    public void setIssueValueAt(int col, Object newValue){
//        issueValues.get(col).setValue(newValue);
//    }
//    
//    
//    public EditableData getIssueValueDataAt(String columnName){
//        for(int i = 0; i < table.getColumnCount(); i++){
//            if(issueValues.get(i).getColumnName().equalsIgnoreCase(columnName)){
////                System.out.println(columnName + " " + issueValues.get(i).getColumnName());
//                return issueValues.get(i);
//            }
//        }
//        return null;
//    }
//}
