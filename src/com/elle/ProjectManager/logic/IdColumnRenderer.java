/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Yi
 */
public class IdColumnRenderer extends DefaultTableCellRenderer {
    private OfflineIssueManager mgr;
    private static IdColumnRenderer instance = null;
    
    protected IdColumnRenderer(OfflineIssueManager mgr) {
        this.mgr = mgr;
      
   }
   public static IdColumnRenderer getInstance(OfflineIssueManager mgr) {
      if(instance == null) {
         instance = new IdColumnRenderer(mgr);
      }
      return instance;
   }
     
     @Override
     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ){
        
        
        row = table.convertRowIndexToModel(row);
        col = table.convertColumnIndexToModel(col);
       
        Component component =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        
        int id = Integer.parseInt(table.getModel().getValueAt(row, 0).toString());
       
        if(isSelected) {
            component.setBackground(table.getSelectionBackground());
            if (mgr.getIds().contains(id)) component.setForeground(Color.RED);
            else component.setForeground(table.getForeground());
        }
        else {
            component.setBackground(table.getBackground());
            if (mgr.getIds().contains(id)) component.setForeground(Color.RED);
            else component.setForeground(table.getForeground());
        }
        
        
        return component;
    }

       
    
}
