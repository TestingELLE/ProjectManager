/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.AbstractDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yi
 */
public abstract class TableController<T> {
    
    protected Map<Integer, T> onlineItems;
    protected Map<Integer, T> offlineItems;
    protected AbstractDAO onlineDAO;
    protected AbstractDAO offlineDAO;
    protected Mode opMode;
    
    
    public TableController() {
        onlineItems = new HashMap();
        offlineItems = new HashMap();   
    }
    
    
    public abstract boolean create(T item);
    public abstract boolean update(T item);
    public abstract boolean delete(int id);
    
    public T read(int id) {
        return (T) totalItems().get(id);
    };
    
    public Map totalItems(){
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
    
    
    public List<T> getOnlineItems(){
        return new ArrayList(onlineItems.values());
    }
    
    public List<T> getOfflineItems(){
        return new ArrayList(offlineItems.values());
    }
    
    
   
    
}
