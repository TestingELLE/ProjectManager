/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.entities.AccessLevel;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.ConflictItemPair;
import com.elle.ProjectManager.logic.IssueConverter;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yi 07/14/2016
 * DataManager for PM
 */
public class PMDataManager{
    
    private static PMDataManager instance = null;
    IssueTableController issueController;
    ReferenceTableController refController;
    AccessLevelTableController alController;
    private IssueConverter issueConverter;
    
    public PMDataManager(boolean online){
        //adding controllers
        issueController = new IssueTableController(online);
        refController = new ReferenceTableController(online);
        alController = new AccessLevelTableController(online);
        //adding converters
        issueConverter = new IssueConverter();
        
        instance = this;
    }
    
    public void setOpMode(boolean online) {
        if(online) {
            issueController.setOpMode(Mode.ONLINE);
            refController.setOpMode(Mode.ONLINE);
            alController.setOpMode(Mode.ONLINE);
        }
        
        else{
            issueController.setOpMode(Mode.OFFLINE);
            refController.setOpMode(Mode.OFFLINE);
            alController.setOpMode(Mode.OFFLINE);
        }
    }
    
    public static PMDataManager getInstance() {
      return instance;
   }
    
    

   /* Issues related data operations */
    //get issues based on app name, requested by tab
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
    
    //get issue rowdata by id , requested by tab
    public Object[] getIssue(int id) {
      
        Issue issue = issueController.get(id);
        return (issueConverter.convertToRow(issue));
       
    }
    
    //get issue rowdata by entity , requested by tab
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
    
    
    //get issue entity by id , requested by issue window
    //it returns a copy of the issue
    //reasons: issue get updated later on, if failed, the original issue is intact
    //the changed copy is saved locally
    public Issue getIssueEntity(int id) {
      
        return (Issue) issueController.get(id).deepClone();
    }
    
    
    //this is for batch update, which does not include the description field
    //for batch edit mode
    //since table batch edit will not change description field
    //and the field lost all style info, so will not be used for update
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
    
    //get reference rowdata by id , requested by tab
    public Object[] getReference(int id) {
      
        Issue reference = refController.get(id);
        return (issueConverter.convertToRow(reference));
       
    }
    
    //get reference rowdata by entity, request by tab
    public Object[] getReference(Issue ref) {
      
        return (issueConverter.convertToRow(ref));
       
    }
    
    
    //get reference entity copy by id, requested by issuewindow
    public Issue getReferenceEntity(int id) {
      
        return (Issue) refController.get(id).deepClone();
       
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
    
    
    //get conflict issues
    public ArrayList<ConflictItemPair<Issue>> getConflictIssues() {
        ArrayList<ConflictItemPair<Issue>> conflictIssues = new ArrayList();
        conflictIssues.addAll(issueController.getConflictItems());
        conflictIssues.addAll(refController.getConflictItems());
        
        return conflictIssues;
        
    }
    
    public void resolveConflictPair(ConflictItemPair<Issue> pair) {
        
        if (pair.getDbItem().getIssueType().equals("REFERENCE")) {
            refController.resolveConflictPair(pair);
            
        }
        else{
            issueController.resolveConflictPair(pair);
        }
            
    }
    
    public void syncLocalData() {
        issueController.syncOfflineItems();
        refController.syncOfflineItems();
    }
    
    public Map<String, ArrayList<Integer>> checkIssueUpdates() {
        LoggingAspect.addLogMsg("Start to check update for table issues : ");
        Map<String, ArrayList<Integer>> issueChanges = issueController.getUpdateFromDb();
        LoggingAspect.addLogMsg("Check update for table issues done . ");
        
        return issueChanges;
    }
    
    public Map<String, ArrayList<Integer>> checkRefUpdates() {
    
        LoggingAspect.addLogMsg("Start to check update for table references : ");
        Map<String, ArrayList<Integer>> refChanges =refController.getUpdateFromDb();
        LoggingAspect.addLogMsg("Check update for table references done. ");
        
        return refChanges;
        
    }
    
    
    /*
    **AccessLevel Table related operations
    */
    
    public ArrayList<AccessLevel> getUsers(){
        return new ArrayList<AccessLevel>(alController.getAllItems().values()) ;
    }
    
    public void deleteUser(int id) {
        alController.delete(id);
    }
    
    public void updateUser(AccessLevel user) {
        alController.update(user);
    }
    
    public void insertUser(AccessLevel user) {
        alController.create(user);
    }
    
}
