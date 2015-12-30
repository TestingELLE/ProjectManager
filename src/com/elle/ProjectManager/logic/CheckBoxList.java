/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * CheckBoxList
 * This is the JList used in the ColumnPopupMenu
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class CheckBoxList extends JList
{
    
   protected static Border noFocusBorder;

   public CheckBoxList()
   {
       // set the no focus border
       noFocusBorder = new EmptyBorder(1, 1, 1, 1);
       
       // set cell renderer
      setCellRenderer(new CellRenderer());

      // selection mode is single selection
      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }

   /**
    * CellRenderer
    * This is the ListCellRenderer used for the CheckBoxList JList.
    */
   protected class CellRenderer implements ListCellRenderer
   {
      public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus)
      {
         JCheckBox checkbox = (JCheckBox) value;
         checkbox.setBackground(isSelected ?
                 getSelectionBackground() : getBackground());
         checkbox.setForeground(isSelected ?
                 getSelectionForeground() : getForeground());
         checkbox.setEnabled(isEnabled());
         checkbox.setFont(getFont());
         checkbox.setFocusPainted(false);
         checkbox.setBorderPainted(true);
         checkbox.setBorder(isSelected ?
          UIManager.getBorder(
           "List.focusCellHighlightBorder") : noFocusBorder);
         return checkbox;
      }
   }
}