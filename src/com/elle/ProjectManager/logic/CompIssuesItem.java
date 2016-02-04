package com.elle.ProjectManager.logic;

import javax.swing.JTextArea;

/**
 * Custom JTextArea for the comp issues TextAreaList.
 * @author Carlos Igreja
 * @since  2-3-2016
 */
public class CompIssuesItem extends JTextArea{
    
    private String id;
    private String app;
    private String title;
    private String description;
    private String programmer;
    private String dateOpened;
    private String rk;
    private String version;
    private String dateClosed;

    public CompIssuesItem(String id, String app, String title, String description, 
            String programmer, String dateOpened, String rk, String version, String dateClosed) {
        super();
        this.id = id;
        this.app = app;
        this.title = title;
        this.description = description;
        this.programmer = programmer;
        this.dateOpened = dateOpened;
        this.rk = rk;
        this.version = version;
        this.dateClosed = dateClosed;
        
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public String getRk() {
        return rk;
    }

    public void setRk(String rk) {
        this.rk = rk;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(String dateClosed) {
        this.dateClosed = dateClosed;
    }

}
