/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.dao.IssueWindowDAO;
import java.util.Vector;
import javax.swing.JTable;

/**
 *
 * @author fuxiaoqian
 */
public class Issue {

    private Vector<Field> issue;
    private int selectedRow;
    private IssueWindowDAO dao;

    public Issue(int row, IssueWindowDAO dao) {
        this.issue = new Vector<Field>();
        this.selectedRow = row;
        this.dao = dao;
    }
    
    public void  setRowNum(int r){
        selectedRow = r;
    }

    public int getFieldsNumber() {
        return issue.size();
    }

    public void setIssueValues(JTable table) {

        String cellValue;
        for (int i = 0; i < table.getColumnCount(); i++) {
            cellValue = "";
            if (selectedRow != -1) {
                if (table.getValueAt(selectedRow, i) != null) {
                    cellValue = table.getValueAt(selectedRow, i).toString();
                }
            }
            Field field = new Field(table.getColumnName(i), cellValue);
            issue.add(field);
        }
        if (selectedRow != - 1) {
            issue.add(new Field("submitter", dao.getValueFromDataBase("submitter", this)));
            issue.add(new Field("locked", dao.getValueFromDataBase("locked", this)));
        } else {
            issue.add(new Field("locked", ""));
            issue.add(new Field("submitter", ""));
        }
        
    }

    public Vector<Field> getIssue() {
        return issue;
    }

    public String getID() {
        return issue.elementAt(0).getValue();
    }

    public String getIssueValueAt(int col) {
        return issue.elementAt(col).getValue();
    }

    public String getIssueValueAt(String colName) {
        for (int i = 0; i < issue.size(); i++) {
            if (issue.get(i).getName().equals(colName)) {

                return issue.elementAt(i).getValue();
            }
        }
        return "";
    }

    public String getFieldName(int col) {
        return issue.get(col).getName();
    }

    public Field getIssueData(String columnName) {
        for (int i = 0; i < issue.size(); i++) {
            if (issue.get(i).getName().equalsIgnoreCase(columnName)) {
//                System.out.println(columnName + " " + issueValues.get(i).getColumnName());
                return issue.get(i);
            }
        }
        return null;
    }
    public Field getIssueData(int col) {
        
        return issue.get(col);
    }

    public void setIssueValueAt(int col, String newValue) {
        issue.get(col).setValue(newValue);
    }

    public void setIssueValueAt(String colName, String newValue) {
        for (int i = 0; i < issue.size(); i++) {
            if (issue.get(i).getName().equals(colName)) {
                issue.get(i).setValue(newValue);
            }
        }
    }

}
