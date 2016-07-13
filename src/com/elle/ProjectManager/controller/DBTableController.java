/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.AbstractDAO;
import com.elle.ProjectManager.entities.DbEntity;
import com.elle.ProjectManager.logic.ITableConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yi
 */
public abstract class DBTableController<T extends DbEntity> implements ITableConstants {
    
    protected Map<Integer, T> onlineItems;
    protected Map<Integer, T> offlineItems;
    protected AbstractDAO onlineDAO;
    protected AbstractDAO offlineDAO;
    protected Mode opMode;
    protected String tableName;
    
    
    public DBTableController() {
        onlineItems = new HashMap();
        offlineItems = new HashMap();   
    }
     
    public boolean create(T item) {
        if(onlineDAO.insert(item)) {
            onlineItems.put(item.getId(), item);   
            System.out.println("New record #" + item.getId() + " is inserted into table " + tableName + ".");
            return true;
        }
        return false;
    }

    
    public boolean update(T item) {
        if(onlineDAO.update(item)) {
            onlineItems.put(item.getId(), item);
            System.out.println("Record #" + item.getId() + " is updated in table " + tableName + ".");
            return true;
        }
        return false;
    }

    public boolean delete(int id) {
        if(onlineDAO.delete(id)){
            onlineItems.remove(id);
            System.out.println("Record #" + id + " is deleted from table " + tableName + ".");
            return true;
        }
        return false;
    }

    public void getAll() {
        List<T> items =  onlineDAO.getAll();
        for(T item: items) {
            onlineItems.put(item.getId(), item);
        }
        System.out.println("Table " + tableName +" is loaded from database.");
    }
    
   
    public T get(int id) {
        return (T) getAllItems().get(id);
    };
    
    public Map<Integer,T> getAllItems(){
        Map<Integer, T> Items = new HashMap();
        Items.putAll(onlineItems);
        Items.putAll(offlineItems);
        return Items;
    }
    
    public int totalOnlineCnt() {
        return onlineItems.size();
    }
    
    public int totalOfflineCnt() {
        return offlineItems.size();
    }

    public Map<Integer, T> getOnlineItems() {
        return onlineItems;
    }

    public void setOnlineItems(Map<Integer, T> onlineItems) {
        this.onlineItems = onlineItems;
    }

    public Map<Integer, T> getOfflineItems() {
        return offlineItems;
    }

    public void setOfflineItems(Map<Integer, T> offlineItems) {
        this.offlineItems = offlineItems;
    }
    
    
   
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
   
    
}
