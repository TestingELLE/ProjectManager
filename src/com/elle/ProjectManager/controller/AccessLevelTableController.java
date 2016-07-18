/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.AccessLevelDAO;
import com.elle.ProjectManager.entities.DbEntity;

/**
 *
 * @author Yi
 */
public class AccessLevelTableController extends DBTableController {

    public AccessLevelTableController(boolean online){
        super();
        tableName = ACCESSLEVEL_TABLE_NAME;
        onlineDAO = new AccessLevelDAO();
        offlineDAO = null;
        if (online) opMode = Mode.ONLINE;
        else opMode = Mode.OFFLINE;
        this.offlineEnabled = false;
        
        //load issues from db to map
        getAll();
        
        
    }
    
    //not useful, just set false
    @Override
    public boolean checkConflict(DbEntity onlineItem, DbEntity offlineItem) {
       return false;
    }
    
}
