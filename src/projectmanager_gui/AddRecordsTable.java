/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectmanager_gui;

import static projectmanager_gui.ITableConstants.TASKFILES_TABLE_NAME;
import static projectmanager_gui.ITableConstants.TASKNOTES_TABLE_NAME;
import static projectmanager_gui.ITableConstants.TASKS_TABLE_NAME;

/**
 *
 * @author Louis W.
 */
public class AddRecordsTable {

    String tableName;
    ProjectManager ana = new ProjectManager();

    AddRecordsTable() {
        // to do
    }

    AddRecordsTable(String table, ProjectManager a) {
        tableName = table;
        ana = a;
    }

    public void update(String table, ProjectManager a) {
        tableName = table;
        ana = a;
    }

    public String getDateName() {
        if (tableName.equals(TASKS_TABLE_NAME)) {
            return "dateAssigned";
        } else if (tableName.equals(TASKFILES_TABLE_NAME)) {
            return "date_";
        } else {
            return "status_date";
        }
    }

    public int getDateColumn() {
        String[] columnNames;
        String dateName = getDateName();
        int i;

        if (tableName.equals(TASKS_TABLE_NAME)) {
            columnNames = ana.getColumnNames(1);
            for (i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equals(dateName)) {
                    return i;
                }
            }
            return -1;
        } else if (tableName.equals(TASKFILES_TABLE_NAME)) {
            columnNames = ana.getColumnNames(2);
            for (i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equals(dateName)) {
                    return i;
                }
            }
            return -1;
        } else {
            columnNames = ana.getColumnNames(3);
            for (i = 0; i < columnNames.length; i++) {
                if (columnNames[i].equals(dateName)) {
                    return i;
                }
            }
            return -1;
        }
    }

    public int getLastColumn() {
        if (tableName.equals(TASKS_TABLE_NAME)) {
            return ana.getColumnNames(1).length - 1;    // -1 because array starts from 0
        } else if (tableName.equals(TASKFILES_TABLE_NAME)) {
            return ana.getColumnNames(2).length - 1;
        } else {
            return ana.getColumnNames(3).length - 1;
        }
    }

    public long getIdNum() {
        if (tableName.equals(TASKS_TABLE_NAME)) {
            return ana.tasks.getRecordsNumber();
        } else if (tableName.equals(TASKFILES_TABLE_NAME)) {
            return ana.task_files.getRecordsNumber();
        } else {
            return ana.task_notes.getRecordsNumber();
        }
    }

    public String[] getColumnTitles() {
//        String[] assignments = {"symbol", "analyst", "priority", "dateAssigned", "note"},
//                 reports = {"symbol", "author", "analysisDate", "path", "document", "notes", "notesL"};
        switch (tableName) {
            case TASKS_TABLE_NAME:
                return ana.getColumnNames(1);
            case TASKFILES_TABLE_NAME:
                return ana.getColumnNames(2);
            case TASKNOTES_TABLE_NAME:
                return ana.getColumnNames(3);
        }
        return null;
    }

    public String[] getEmptyRow() {
        String[] table1 = {"", "", "", "", "", "", "", "", ""},
                table2 = {"", "", "", "", "", "", ""},
                table3 = {"", "", "", ""};
        switch (tableName) {
            case TASKS_TABLE_NAME:
                return table1;
            case TASKFILES_TABLE_NAME:
                return table2;
            case TASKNOTES_TABLE_NAME:
                return table3;
        }
        return null;
    }

}
