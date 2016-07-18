/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.OfflineIssueDAO;
import com.elle.ProjectManager.dao.ReferenceDAO;
import com.elle.ProjectManager.entities.Issue;
import java.util.List;

/**
 *
 * @author Yi
 */
public class ReferenceTableController extends DBTableController<Issue> {
    
    public ReferenceTableController(boolean online){
        super();
        tableName = REF_TABLE_NAME;
        onlineDAO = new ReferenceDAO();
        offlineDAO = new OfflineIssueDAO("ref");
        //load issues from db to map
        if (online) opMode = Mode.ONLINE;
        else opMode = Mode.OFFLINE;
        offlineEnabled = true;
        //populate data from database and local files
        getAll();
        
        //sync local data 
        syncOfflineItems();
        
        
    }

    public void updatePartial(Issue issue) {
        Issue stored = getAllItems().get(issue.getId());
        issue.setDescription(stored.getDescription());
        update(issue);
    }

    @Override
    public boolean checkConflict(Issue onlineItem, Issue offlineItem) {
        if (offlineItem.getLastmodtime().compareTo(onlineItem.getLastmodtime()) < 0) {
                return true;
        }
        return false;
    }
    
    
} 

