/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.IssueConverter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yi
 */
public class PMDataManager{
    
    private static PMDataManager instance = null;
    IssueTableController issueController;
    ReferenceTableController refController;
    private IssueConverter issueConverter;
    
    public PMDataManager(){
        //adding controllers
        issueController = new IssueTableController();
        refController = new ReferenceTableController();
        //adding converters
        issueConverter = new IssueConverter();
    }
    
    public static PMDataManager getInstance() {
      if(instance == null) {
         instance = new PMDataManager();
      }
      return instance;
   }

   /* Issues related data operations */
    //get issues based on app name
    public List<Object[]> getIssues(String appType) {
        ArrayList<String> apps = new ArrayList();
        apps.add("PM");
        apps.add("Analyster");
        apps.add("ELLEGUI");
                
        ArrayList<Object[]> tableRowsData = new ArrayList();
        List<Issue> issues = new ArrayList<Issue>(issueController.getAllItems().values());
        for(Issue issue : issues) {
            if (appType.equals("Other")){
                if(!apps.contains(issue.getApp())) 
                    tableRowsData.add(issueConverter.convertToRow(issue));
            }
            else{
                if (issue.getApp().equals(appType)){ 
                    tableRowsData.add(issueConverter.convertToRow(issue));
                }
            }
        }
        
        return tableRowsData;
      
    }
    
    //get issue by id
    public Object[] getIssue(int id) {
      
        Issue issue = issueController.get(id);
        return (issueConverter.convertToRow(issue));
       
    }
    
    //get issue rowdata by entity
    public Object[] getIssue(Issue issue) {
      return (issueConverter.convertToRow(issue));   
    }
    
    //update issue
    public void updateIssue(Issue issue) {
        issueController.update(issue);
    }
    
    //insert issue
    public void insertIssue(Issue issue) {
        issueController.create(issue);
    }
    
    
    //get issue entity by id
    public Issue getIssueEntity(int id) {
      
        return issueController.get(id);
    }
    
    
    //this is for batch update, which does not include the description field
    public void updateIssues(List<Object[]> rowsData) {
        ArrayList<Issue> changedIssues = new ArrayList();
        for(Object[] rowData : rowsData) {
            changedIssues.add(issueConverter.convertFromRow(rowData));
        }
        for(Issue changedIssue : changedIssues) {
            issueController.updatePartial(changedIssue);
        }
    }
    
    //delete issues
    public void deleteIssues(List<Integer> ids) {
        for(Integer id: ids) {
            issueController.delete(id);
        }
    }
    
    /*reference related data operations */
    //get references
    public List<Object[]> getReferences() {
               
        ArrayList<Object[]> tableRowsData = new ArrayList();
        List<Issue> references = new ArrayList<Issue>(refController.getAllItems().values()) ;
        for(Issue ref: references) {   
            tableRowsData.add(issueConverter.convertToRow(ref));
        }
        
        return tableRowsData;
      
    }
    
    //get reference rowdata by id
    public Object[] getReference(int id) {
      
        Issue reference = refController.get(id);
        return (issueConverter.convertToRow(reference));
       
    }
    
    //get reference rowdata by entity
    public Object[] getReference(Issue ref) {
      
        return (issueConverter.convertToRow(ref));
       
    }
    
    
    //get reference by id
    public Issue getReferenceEntity(int id) {
      
        return refController.get(id);
       
    }
    
    //this is for batch update, which does not include the description field
    public void updateReferences(List<Object[]> rowsData) {
        ArrayList<Issue> changedIssues = new ArrayList();
        for(Object[] rowData : rowsData) {
            changedIssues.add(issueConverter.convertFromRow(rowData));
        }
        for(Issue changedIssue : changedIssues) {
            refController.updatePartial(changedIssue);
        }
    }
    
    //update reference
    public void updateReference(Issue reference) {
        refController.update(reference);
    }
    
    //insert reference
    public void insertReference(Issue reference) {
        refController.create(reference);
    }
    
    //delete references
    public void deleteReferences(List<Integer> ids) {
        for(Integer id: ids) {
            refController.delete(id);
        }
    }
}
