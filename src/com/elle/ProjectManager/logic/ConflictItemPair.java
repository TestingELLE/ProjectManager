/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

/**
 *
 * @author Yi
 */
public class ConflictItemPair<T> {
    protected T dbItem;
    protected T localItem;
    
    //true : dbItem
    //false: localItem
    protected boolean choice;
    
    
    
    public ConflictItemPair(T dbItem, T localItem) {
        this.dbItem = dbItem;
        this.localItem = localItem;
        choice = true;
        
    }
    
    //return the chosen one
    public T getChocie() {
        if (choice) return dbItem;
        else return localItem;
    }
   

    public T getDbItem() {
        return dbItem;
    }

    public void setDbItem(T dbItem) {
        this.dbItem = dbItem;
    }

    public T getLocalItem() {
        return localItem;
    }

    public void setLocalItem(T localItem) {
        this.localItem = localItem;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

   
    
}
