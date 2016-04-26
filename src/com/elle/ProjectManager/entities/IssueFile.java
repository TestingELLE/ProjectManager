
package com.elle.ProjectManager.entities;

/**
 * IssueFile
 * @author Carlos Igreja
 * @since  Apr 26, 2016
 */
public class IssueFile {

    private String fileID;
    private String taskID;
    private String app;
    private String submitter;
    private String step;
    private String date;
    private String files;
    private String path;
    private String notes;

    public IssueFile(){
        this("","","","","","","","","");
    }
    
    public IssueFile(String fileID, String taskID, String app, String submitter, String step, String date, String files, String path, String notes) {
        this.fileID = fileID;
        this.taskID = taskID;
        this.app = app;
        this.submitter = submitter;
        this.step = step;
        this.date = date;
        this.files = files;
        this.path = path;
        this.notes = notes;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
    

}
