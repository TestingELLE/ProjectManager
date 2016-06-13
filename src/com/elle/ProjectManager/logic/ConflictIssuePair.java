/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.entities.Issue;

/**
 *
 * @author Yi
 */
public class ConflictIssuePair {
    private Issue dbIssue;
    private Issue offlineIssue;
    
    //true for dbIssue, false for offlineIssue;
    private boolean choice;
    
    public ConflictIssuePair(Issue dbIssue, Issue offlineIssue) {
        this.dbIssue = dbIssue;
        this.offlineIssue = offlineIssue;
        
        //set default choice to true
        this.choice = true;
    }

    public Issue getDbIssue() {
        return dbIssue;
    }

    public void setDbIssue(Issue dbIssue) {
        this.dbIssue = dbIssue;
    }

    public Issue getOfflineIssue() {
        return offlineIssue;
    }

    public void setOfflineIssue(Issue offlineIssue) {
        this.offlineIssue = offlineIssue;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }
    
    //return the chosen one
    public Issue getChocie() {
        if (choice) return dbIssue;
        else return offlineIssue;
    }
   
}
