/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import javax.swing.JTable;

/**
 *
 * @author fuxiaoqian
 */
public class ConsistencyOfTableColumnName {

    static String ErrorMessage = "";

    public static boolean IsTableColumnNameTheSame(Tab tab, JTable table) {
        boolean columnNameIsTheSame = true;
        ErrorMessage = "";
        ErrorMessage = ErrorMessage + "In " + table.getName() + ", ";
        for (int index = 0; index < table.getColumnCount(); index++) {
            if (!tab.getTableColNames()[index].equalsIgnoreCase(table.getColumnName(index))) {
                columnNameIsTheSame = false;
                ErrorMessage = ErrorMessage + " " + table.getColumnName(index) + ",";
            }
        }
        if (!columnNameIsTheSame) {
            ErrorMessage = ErrorMessage + " is different from database!";
        }else{
            ErrorMessage = "";
        }
        return columnNameIsTheSame;
    }
    
    public static String getErrorMessage(){
        return ErrorMessage;
    }

}
