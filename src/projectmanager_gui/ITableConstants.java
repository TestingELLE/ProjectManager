/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectmanager_gui;

/**
 *
 * @author shanxijin
 */
public interface ITableConstants {
    
    public static final String TASKS_TABLE_NAME = "tasks";
    public static final String TASKFILES_TABLE_NAME = "task_files";
    public static final String TASKNOTES_TABLE_NAME = "task_notes";
    
    
    // column width percent constants
    public static final float[] COL_WIDTH_PER_TASKS = {60, 50, 170, 50, 245, 260, 95, 100, 35, 50, 95};
    public static final float[] COL_WIDTH_PER_TASKFILES = {55, 60, 115, 60, 95, 275, 135, 398};
    public static final float[] COL_WIDTH_PER_TASKNOTES = {60, 60, 115, 855, 95};
    
    // search fields for the comboBox for each table
    public static final String[] TASKS_SEARCH_FIELDS = {"programmer", "dateAssigned", "done", "rk"};
    public static final String[] TASKFILES_SEARCH_FIELDS = {"submitter"};
    public static final String[] TASKNOTES_SEARCH_FIELDS = {"submitter"};
    
}
