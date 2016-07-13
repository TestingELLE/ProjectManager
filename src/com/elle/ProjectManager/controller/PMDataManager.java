/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.Converter;
import com.elle.ProjectManager.logic.ITableConstants;
import com.elle.ProjectManager.logic.IdColumnRenderer;
import com.elle.ProjectManager.logic.IssueConverter;
import com.elle.ProjectManager.logic.OfflineIssueManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yi
 */
public class PMDataManager{
    
    private static PMDataManager instance = null;
    IssueTableController issueController;
    ReferenceTableController refController;
    private Converter issueConverter;
    
    public PMDataManager(){
        //adding controllers
        issueController = new IssueTableController();
        refController = new ReferenceTableController();
       
        issueConverter = new IssueConverter();
    }
    
    public static PMDataManager getInstance() {
      if(instance == null) {
         instance = new PMDataManager();
      }
      return instance;
   }

   
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
    
    //get references
    public List<Object[]> getReferences() {
               
        ArrayList<Object[]> tableRowsData = new ArrayList();
        List<Issue> references = new ArrayList<Issue>(refController.getAllItems().values()) ;
        for(Issue ref: references) {   
            tableRowsData.add(issueConverter.convertToRow(ref));
        }
        
        return tableRowsData;
      
    }
    
    //get reference by id
    public Object[] getReference(int id) {
      
        Issue reference = refController.get(id);
        return (issueConverter.convertToRow(reference));
       
    }
            
    
    
}
