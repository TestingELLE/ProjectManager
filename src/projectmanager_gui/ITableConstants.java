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
    public static final float[] COL_WIDTH_PER_TASKS = {2.0f, 5.0f, 14.0f, 3.0f, 21.0f, 21.0f, 6.5f, 7.0f, 3.0f, 4.0f, 7.0f};
    public static final float[] COL_WIDTH_PER_TASKFILES = {4.0f, 4.0f, 6.0f, 3.5f, 7.0f, 20.0f, 20.0f, 20.5f};
    public static final float[] COL_WIDTH_PER_TASKNOTESD = {4.0f, 4.0f, 6.0f, 50.0f, 7.0f};
    
    // search fields for the comboBox for each table
    public static final String[] TASKS_SEARCH_FIELDS = {"programmer", "dateAssigned", "done", "rank"};
    public static final String[] TASKFILES_SEARCH_FIELDS = {"submitter"};
    public static final String[] TASKNOTES_SEARCH_FIELDS = {"submitter"};
    
}
