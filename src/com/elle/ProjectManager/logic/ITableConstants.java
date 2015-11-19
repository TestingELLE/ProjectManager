/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

/**
 * ITableConstants
 * This interface stores all the table constants
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public interface ITableConstants {
    
    public static final String TASKS_TABLE_NAME = "issues";
    public static final String TASKFILES_TABLE_NAME = "issue_files";
//    public static final String TASKNOTES_TABLE_NAME = "issue_notes";
    
    // column header name constants
    public static final String SYMBOL_COLUMN_NAME = "Symbol";
    
    // column width percent constants
    public static final float[] COL_WIDTH_PER_TASKS = {50, 70, 175, 350, 80, 90, 30, 55, 90};
    public static final float[] COL_WIDTH_PER_REPORTS = {55, 60, 70, 115, 60, 95, 275, 200, 325};
//    public static final float[] COL_WIDTH_PER_ARCHIVE = {60, 60, 115, 845, 105};
    
    // search fields for the comboBox for each table
    public static final String[] TASKS_SEARCH_FIELDS = {"programmer", "dateOpened", "done", "rk"};
    public static final String[] TASKFILES_SEARCH_FIELDS = {"submitter"};
//    public static final String[] TASKNOTES_SEARCH_FIELDS = {"submitter"};
    
    // batch edit combobox selections for each table
    public static final String[] TASKS_BATCHEDIT_CB_FIELDS = {"programmer", "rk", "dateOpened", "notes", "done", "dateClosed"};
    public static final String[] TASKFILES_BATCHEDIT_CB_FIELDS = {"submitter", "date", "path", "notes"};
//    public static final String[] TASKNOTES_BATCHEDIT_CB_FIELDS = {"submitter", "status_notes", "date"};
}
