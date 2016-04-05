
package com.elle.ProjectManager.entities;

import java.util.Date;

/**
 * Issue
 * @author Carlos Igreja
 * @since  Apr 5, 2016
 */
public class Issue {

    private int id;
    private String app;
    private String title;
    private String description;
    private String programmer;
    private Date dateOpened;
    private int rk;
    private String version;
    private Date dateClosed;
    private String issue_type;
    private String submitter;
    private String locked;
    
    public Issue(){
        this(-1,null,null,null,null,null,-1,null,null,null,null,null);
    }

    public Issue(int id, String app, String title, String description, String programmer, Date dateOpened, int rk, String version, Date dateClosed, String issue_type, String submitter, String locked) {
        this.id = id;
        this.app = app;
        this.title = title;
        this.description = description;
        this.programmer = programmer;
        this.dateOpened = dateOpened;
        this.rk = rk;
        this.version = version;
        this.dateClosed = dateClosed;
        this.issue_type = issue_type;
        this.submitter = submitter;
        this.locked = locked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProgrammer() {
        return programmer;
    }

    public void setProgrammer(String programmer) {
        this.programmer = programmer;
    }

    public Date getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(Date dateOpened) {
        this.dateOpened = dateOpened;
    }

    public int getRk() {
        return rk;
    }

    public void setRk(int rk) {
        this.rk = rk;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getIssue_type() {
        return issue_type;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }
    
    
    
    
}
