/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.AbstractDAO;
import com.elle.ProjectManager.entities.DbEntity;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.ITableConstants;
import java.util.Arrays;
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
        opMode = Mode.ONLINE;
    }
     
    public void create(T item) {
        // if offline mode, do offline insert.
        if (opMode == Mode.OFFLINE) {
            offlineDAO.insert(item);
            offlineItems.put(item.getId(), item);
            System.out.println("New record #" + item.getId() + " is saved locally.");
        }
        else {
            //if online insert fails, do offline insert
            if(onlineDAO.insert(item)) {
                onlineItems.put(item.getId(), item);   
                System.out.println("New record #" + item.getId() + " is inserted into table " + tableName + ".");
            
            }
            else {
                offlineDAO.insert(item);
                offlineItems.put(item.getId(), item);
                System.out.println("New record #" + item.getId() + " is saved locally.");
            
            }
            
        }
   
    }

    
    public void update(T item) {
    
        int id = item.getId();
        if(id > 0 && id < 9000 && opMode == Mode.ONLINE && onlineDAO.update(item)) {
            onlineItems.put(id, item);
            System.out.println("Record #" + item.getId() + " is updated in table " + tableName + ".");
            
        }
        else {
            offlineDAO.update(item);
            offlineItems.put(item.getId(), item);
            System.out.println("Record #" + item.getId() + " is updated locally. ");
        }
    
    }

    public void delete(int id) {
        
        if(id > 0 && id < 9000 && onlineDAO.delete(id)){
            onlineItems.remove(id);
            System.out.println("Record #" + id + " is deleted from table " + tableName + ".");
            
        }
        else{
            if (id < 0 || id > 9000) {
                offlineItems.remove(id);
                System.out.println("Record #" + id + " is deleted locally . ");
            }
            else {
                System.out.println("Record #" + id + " failed to be deleted from " + tableName + ", please try again later.");
            }
            
        }
    }

    public void getAll() {
        if (opMode == Mode.ONLINE) {
             List<T> items =  onlineDAO.getAll();
            for(T item: items) {
                onlineItems.put(item.getId(), item);
            }
            
        }
       
        List<T> offlineitems = offlineDAO.getAll();
        for(T item: offlineitems) {
            offlineItems.put(item.getId(), item);
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

    public Mode getOpMode() {
        return opMode;
    }

    public void setOpMode(Mode opMode) {
        this.opMode = opMode;
    }
    
    
   
}
