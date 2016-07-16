/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.AbstractDAO;
import com.elle.ProjectManager.entities.DbEntity;
import com.elle.ProjectManager.logic.ConflictItemPair;
import com.elle.ProjectManager.logic.ITableConstants;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yi
 */
public abstract class DBTableController<T extends DbEntity> implements ITableConstants {
    
    protected Map<Integer, T> onlineItems;
    protected Map<Integer, T> offlineItems;
    protected ArrayList<ConflictItemPair<T>> conflictItems;
    protected AbstractDAO onlineDAO;
    protected AbstractDAO offlineDAO;
    protected Mode opMode;
    protected String tableName;
    
    
    public DBTableController() {
        onlineItems = new HashMap();
        offlineItems = new HashMap();   
        conflictItems = new ArrayList();
        opMode = Mode.ONLINE;
    }
     
    public void create(T item) {
        // if offline mode, do offline insert.
        if (opMode == Mode.OFFLINE) {
            offlineDAO.insert(item);
            offlineItems.put(item.getId(), item);
            LoggingAspect.afterReturn("New record #" + item.getId() + " is saved locally.");
        }
        else {
            //if online insert fails, do offline insert
            if(onlineDAO.insert(item)) {
                onlineItems.put(item.getId(), item);   
                LoggingAspect.afterReturn("New record #" + item.getId() + " is inserted into table " + tableName + ".");
            
            }
            else {
                offlineDAO.insert(item);
                offlineItems.put(item.getId(), item);
                LoggingAspect.afterReturn("New record #" + item.getId() + " is saved locally.");
            
            }
            
        }
   
    }

    
    public void update(T item) {
    
        int id = item.getId();
        
        //if issue is online issue, mode is online, and online update sucessful
        //otherwise, do offline update
        if(id > 0 && id < 9000 && opMode == Mode.ONLINE && onlineDAO.update(item)) {
            onlineItems.put(id, item);
            LoggingAspect.afterReturn("Record #" + item.getId() + " is updated in table " + tableName + ".");
            
        }
        else { 
            //adding 9000 is done in offlineDAO update.
            offlineDAO.update(item);
            offlineItems.put(item.getId(), item);
            LoggingAspect.afterReturn("Record #" + item.getId() + " is updated locally. ");
        }
    
    }

    public void delete(int id) {
        //if issue is online issue, and delete by onlinedao successfully
        //else if issue is offline, delete by offlinedao
        //      else online delete fails, prompt the message
        if(id > 0 && id < 9000 && onlineDAO.delete(id)){
            onlineItems.remove(id);
            LoggingAspect.afterReturn("Record #" + id + " is deleted from table " + tableName + ".");
            
        }
        else{
            if (id < 0 || id > 9000) {
                offlineItems.remove(id);
                offlineDAO.delete(id);
                LoggingAspect.afterReturn("Record #" + id + " is deleted locally . ");
            }
            else {
                LoggingAspect.afterReturn("Record #" + id + " failed to be deleted from " + tableName + ", please try again later.");
            }
            
        }
    }

    //populate data from db and local folder
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
    
    
    public void syncOfflineItems(){
        //use iterator for deletion, as map is concurrent
        Iterator<Integer> it = offlineItems.keySet().iterator();
        
        while (it.hasNext()){
            Integer key = it.next();
            T offlineItem = offlineItems.get(key);
            //use clone copy to operate 
            T copyItem = (T) offlineItem.deepClone();
            if (copyItem.getId() < 0 && onlineDAO.insert(copyItem)) {
                onlineItems.put(copyItem.getId(), copyItem);
                //delete from local folder, remove from offline items
                offlineDAO.delete(key);
                it.remove();
            }
            if (copyItem.getId() > 9000) {
                //get the online item
                T onlineItem = onlineItems.get(copyItem.getId()-9000);
                
                //check if item exsits
                if (onlineItem != null) {
                    //check if there is conflict 
                    if(!checkConflict(onlineItem, copyItem)) {
                        //no conflicts, reset copyItem id
                        copyItem.setId(onlineItem.getId());
                        //update to db
                        onlineDAO.update(copyItem);
                        //put into online array
                        onlineItems.put(copyItem.getId(), copyItem);
                        //remove from offline items and delete locally
                        offlineDAO.delete(key);
                        it.remove();
                        
                    }
                    else{
                        //copyItem maintains id +9000
                        //add to conflict array
                        conflictItems.add(new ConflictItemPair(onlineItem, copyItem));
                    } 
                }
                //if online item was deleted accidentally by others
                else{
                    //not implemented yet
                    //can add logic here if required
                }
                
            }
        }
   
    }
    
    //logics for resolving conflict pair
    public void resolveConflictPair(ConflictItemPair<T> pair) {
        if (pair.isChoice() && onlineDAO.update(pair.getDbItem())) {
            //online item is chosen, however, it could be changed, thus still need to update database
            conflictItems.remove(pair);
            //offline item should be deleted
            int itemId = pair.getLocalItem().getId();
            offlineDAO.delete(itemId);
            offlineItems.remove(itemId);
            
        }
        
        else {
            if(!pair.isChoice()) {
                //local copy is chosen. 
                //get id 9000+
                int itemId = pair.getLocalItem().getId();
                
                //reset local item id
                pair.getLocalItem().setId(itemId - 9000);
                onlineDAO.update(pair.getLocalItem());
                onlineItems.put(pair.getLocalItem().getId(), pair.getLocalItem());
                conflictItems.remove(pair);
                
                //remove local issues by originalId
                offlineDAO.delete(itemId);
                offlineItems.remove(itemId);
            }
        }
        
    }
    
    //returns if conflicted or not
    //if with conflicts, return true
    //no conflicts, return false;
    //this function can be implemented differently for different tables, eg lastmodtime
    public abstract boolean checkConflict(T onlineItem, T offlineItem);
    
    
    
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
    
    
    
    /*
    **getters and setters
    */
   
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

    public ArrayList<ConflictItemPair<T>> getConflictItems() {
        return conflictItems;
    }

    public void setConflictItems(ArrayList<ConflictItemPair<T>> conflictItems) {
        this.conflictItems = conflictItems;
    }
    
    
   
}
