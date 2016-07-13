
package com.elle.ProjectManager.entities;

import java.io.Serializable;

/**
 * Issue
 * @author Carlos Igreja
 * @since  Apr 5, 2016
 */
public class Issue extends DbEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String app;
    private String title;
    private byte[] description;
    private String programmer;
    private String dateOpened;
    private String rk; 
    private String version;
    private String dateClosed;
    private String issueType;
    private String submitter;
    private String locked;
    private String lastmodtime;
   
    
    public Issue(){
        this(-1,"","",new byte[0],"","","","","","FEATURE","","","");
    }

    public Issue(int id, String app, String title, byte[] description, String programmer, String dateOpened, String rk, String version, String dateClosed, String issue_type, String submitter, String locked, String lastmodtime) {
        this.id = id;
        this.app = app;
        this.title = title;
        this.description = description;
        this.programmer = programmer;
        this.dateOpened = dateOpened;
        this.rk = rk;
        this.version = version;
        this.dateClosed = dateClosed;
        this.issueType = issue_type;
        this.submitter = submitter;
        this.locked = locked;
        this.lastmodtime = lastmodtime;
        
    }

  
    public String getApp() {
        if (app == null) {
            return "";
        } else {
            return app;  
        }
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getTitle() {
        if (title == null) {
            return "";
        } else {
            return title;  
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getDescription() {
        if (description == null) {
            return new byte[0];
        } else {
            return description;  
        }
    }

    public void setDescription(byte[] description) {
        this.description = description;
    }

    public String getProgrammer() {
        if (programmer == null) {
            return "";
        } else {
            return programmer;  
        }
    }

    public void setProgrammer(String programmer) {
        this.programmer = programmer;
    }

    public String getDateOpened() {
        if (dateOpened == null) {
            return "";
        } else {
            return dateOpened;  
        }
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getRk() {
        if (rk == null) {
            return "";
        } else {
            return rk;  
        }
    }

    public void setRk(String rk) {
        this.rk = rk;
    }

    public String getVersion() {
        if (version == null) {
            return "";
        } else {
            return version;  
        }
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDateClosed() {
        if (dateClosed == null) {
            return "";
        } else {
            return dateClosed;  
        }
    }

    public void setDateClosed(String dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getIssueType() {
        if (issueType == null) {
            return "";
        } else {
            return issueType;  
        }
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getSubmitter() {
        if (submitter == null) {
            return "";
        } else {
            return submitter;  
        }
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getLocked() {
        if (locked == null) {
            return "";
        } else {
            return locked;  
        }
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getLastmodtime() {
        if (lastmodtime == null) {
            return "";
        } else {
            return lastmodtime;  
        }
    }

    public void setLastmodtime(String lastmodtime) {
        this.lastmodtime = lastmodtime;
    }

}
