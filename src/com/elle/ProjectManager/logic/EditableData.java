/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

/**
 *
 * @author fuxiaoqian
 */
public class EditableData {
    private String tableName;
    private String columnName;
    private Object value;
    private boolean isValueChanged;
    private int id;
    
    /**
     * CONSTRUCTOR
     * ModifiedData
     * @param tableName
     * @param id
     * @param columnName
     * @param value 
     */
    
    public EditableData(String tableName, String columnName, Object value, int id) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.value = value;
        this.id = id;
        this.isValueChanged = false;
    }
    
    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getValue() {
        return value;
    }
    
    public int getId() {
        return id;
    }
    
    public boolean isValueChanged(){
        return isValueChanged;
    }
    
    public void setIsValueChanged(boolean change){
        isValueChanged = change;
    }
    
    public void setValue(Object newValue){
        value = newValue;
        isValueChanged = true;
    } 
}
