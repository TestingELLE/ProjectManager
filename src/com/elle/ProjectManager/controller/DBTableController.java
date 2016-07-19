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
    protected boolean offlineEnabled;
   
    
    public DBTableController() {
        onlineItems = new HashMap();
        offlineItems = new HashMap();   
        conflictItems = new ArrayList();
        opMode = Mode.ONLINE;
        offlineEnabled = false;
        
    }
    
    
    /*
    ** online data operations
    */
    private boolean insertOnline(T item) {
        if(onlineDAO.insert(item)) {
            onlineItems.put(item.getId(), item);   
            LoggingAspect.afterReturn("New record #" + item.getId() + " is saved locally.");
            return true;
                
        }
        return false;
    }
    
    private boolean updateOnline(T item) {
        if(onlineDAO.update(item)) {
            onlineItems.put(item.getId(), item);
            LoggingAspect.afterReturn("Record #" + item.getId() + " is updated in table " + tableName + ".");
            return true;
        }
        
        return false;
        
    }
    
    private boolean deleteOnline(int id) {
        if(onlineDAO.delete(id)){
            onlineItems.remove(id);
            LoggingAspect.afterReturn("Record #" + id + " is deleted from table " + tableName + ".");
            return true;
            
        }
        return false;
    }
     
    /*
    ** offline data operations
    */
    
    private boolean insertOffline(T item) {
        if(offlineDAO.insert(item)) {
            offlineItems.put(item.getId(), item);   
            LoggingAspect.afterReturn("New record #" + item.getId() + " is inserted into table " + tableName + ".");
            return true;
                
        }
        return false;
        
    }
    
    private boolean updateOffline(T item) {
        if(offlineDAO.update(item)) {
            offlineItems.put(item.getId(), item);
            LoggingAspect.afterReturn("Record #" + item.getId() + " is updated locally. ");
            return true;
        }
        
        return false;
        
    }
    
    private boolean deleteOffline(int id) {
         if(offlineDAO.delete(id)){
             offlineItems.remove(id);
             LoggingAspect.afterReturn("Record #" + id + " is deleted locally . ");
             return true;
         }
         return false;
    }
    
    
    //populate data from db and local folder
    public void getAll() {
        if (opMode == Mode.ONLINE) {
             List<T> items =  onlineDAO.getAll();
            for(T item: items) {
                onlineItems.put(item.getId(), item);
            }
            
        }
        
        
        if(offlineEnabled) {
            List<T> offlineitems = offlineDAO.getAll();
            for(T item: offlineitems) {
                offlineItems.put(item.getId(), item);
            }
            
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
            if (copyItem.getId() < 0 && insertOnline(copyItem)) {
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
                        updateOnline(copyItem);
                        
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
        if (pair.isChoice() && updateOnline(pair.getDbItem())) {
            //online item is chosen, however, it could be changed, thus still need to update database
            conflictItems.remove(pair);
            //offline item should be deleted
            int itemId = pair.getLocalItem().getId();
            deleteOffline(itemId);
            
        }
        
        else {
            if(!pair.isChoice()) {
                //local copy is chosen. 
                //get id 9000+
                int itemId = pair.getLocalItem().getId();
                
                //reset local item id
                pair.getLocalItem().setId(itemId - 9000);
                updateOnline(pair.getLocalItem());
               
                conflictItems.remove(pair);
                
                //remove local issues by originalId
                deleteOffline(itemId);
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

    /*
    ** interface methods for crud
    */
    public void create(T item) {
        
        switch(opMode) {
            case ONLINE : {
                if(!insertOnline(item)) {
                    if(offlineEnabled) 
                        insertOffline(item);
                    else        
                        LoggingAspect.afterReturn("Could not create the record, please try again later.");
                }
                break;
          
            }
            case OFFLINE : {
                if (offlineEnabled) {
                    insertOffline(item);
                }
                else 
                    LoggingAspect.afterReturn("Could not create the record, please try again later.");
                break;
            }
            default :{
                break;
            }
        }
    }

    
    public void update(T item) {
    
        int id = item.getId();
        
        switch(opMode) {
            case ONLINE : {
                if (id > 0 && id < 9000) {
                    if( !updateOnline(item)) {
                        if (offlineEnabled) updateOffline(item);
                        else LoggingAspect.afterReturn("Could not update the record, please try again later.");
                    }  
                }

                break;
                
            }
            case OFFLINE :{
                if(offlineEnabled) updateOffline(item);
                else LoggingAspect.afterReturn("Could not update the record, please try again later.");
              
            }
            default : break;
        }
    }

    public void delete(int id) {
        
        //if issue is online issue, and delete by onlinedao successfully
        //else if issue is offline, delete by offlinedao
        //      else online delete fails, prompt the message
        if(id > 0 && id < 9000){
            if (!deleteOnline(id))
                LoggingAspect.afterReturn("Record #" + id + " failed to be deleted from " + tableName + ", please try again later.");
        }
        else{
            if (id < 0 || id > 9000) {
                if (!deleteOffline(id))
                    LoggingAspect.afterReturn("Record #" + id + " failed to be deleted locally . ");
            }
    
        }
    }

    
    
    
    
    
    
    
    
    /*
    **getters and setters
    */
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

    public ArrayList<ConflictItemPair<T>> getConflictItems() {
        return conflictItems;
    }

    public void setConflictItems(ArrayList<ConflictItemPair<T>> conflictItems) {
        this.conflictItems = conflictItems;
    }
    
    
   
}
