package com.elle.ProjectManager.logic;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Custom JList that takes JTextArea Objects
 * @author Carlos Igreja
 * @since  2-3-2016
 */
public class TextAreaList extends JList{
    
    protected static Border noFocusBorder;

   public TextAreaList()
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
    * This is the ListCellRenderer used for the TextAreaList JList.
    */
   protected class CellRenderer implements ListCellRenderer
   {
      public Component getListCellRendererComponent(
                    JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus)
      {
         JTextArea textArea = (JTextArea) value;
         textArea.setBackground(isSelected ?
                 getSelectionBackground() : getBackground());
         textArea.setForeground(isSelected ?
                 getSelectionForeground() : getForeground());
         textArea.setEnabled(isEnabled());
         textArea.setFont(getFont());
         textArea.setBorder(isSelected ?
          UIManager.getBorder(
           "List.focusCellHighlightBorder") : noFocusBorder);
         return textArea;
      }
   }
}
