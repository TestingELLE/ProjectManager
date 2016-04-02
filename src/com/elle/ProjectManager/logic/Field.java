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
public class Field {
    private String fieldName;
    private boolean isValueChanged;
    private String fieldValue;
    
    public Field(String name, String value){
        this.fieldName = name;
        this.fieldValue = value;
        this.isValueChanged = false;
    }
    
    public void setValue(String value){
        fieldValue = value;
    }
    
    public void setName(String name){
        fieldName = name;
    }
    
    public void setChanged(boolean T){
        isValueChanged = T;
    }
    
    public String getValue(){
        return fieldValue;
    }
    
    public String getName(){
        return fieldName;
    }
    public boolean isValueChanged(){
        return isValueChanged;
    }
}
