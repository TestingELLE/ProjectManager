/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.IssueDAO;
import com.elle.ProjectManager.entities.Issue;
import java.util.List;

/**
 *
 * @author Yi
 */
public class IssueTableController extends DBTableController<Issue> {
    
    public IssueTableController(){
        super();
        tableName = TASKS_TABLE_NAME;
        onlineDAO = new IssueDAO();
        //load issues from db to map
        getAll();
    }
    
    //this update will exclude the description field
    //the issue is collected directly from table
    //descirption is not editable
    public void updatePartial(Issue issue) {
        Issue stored = getAllItems().get(issue.getId());
        issue.setDescription(stored.getDescription());
        update(issue);
        
    }
    

} 

