/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.ReferenceDAO;
import com.elle.ProjectManager.entities.Issue;
import java.util.List;

/**
 *
 * @author Yi
 */
public class ReferenceTableController extends DBTableController<Issue> {
    
    public ReferenceTableController(){
        super();
        tableName = REF_TABLE_NAME;
        onlineDAO = new ReferenceDAO();
        //load issues from db to map
        getAll();
    }

    public void updatePartial(Issue issue) {
        Issue stored = getAllItems().get(issue.getId());
        issue.setDescription(stored.getDescription());
        update(issue);
    }
    
    
} 

