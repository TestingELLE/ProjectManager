/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

/**
 *
 * @author Yi  
 */
import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class CustomComboBoxRenderer extends BasicComboBoxRenderer {

    private ListSelectionModel enabledItems;

    private Color disabledColor = Color.lightGray;

    public CustomComboBoxRenderer() {}

    public CustomComboBoxRenderer(ListSelectionModel enabled) {
        super();
        this.enabledItems = enabled;
    }

    public void setEnabledItems(ListSelectionModel enabled) {
        this.enabledItems = enabled;
    }

    public void setDisabledColor(Color disabledColor) {
        this.disabledColor = disabledColor;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
      
        //index ! = -1 has to be included, as the combobox will be set
        // if not using the  condition, -1 will not belong to enabled items, and will show as gray color
        if (!enabledItems.isSelectedIndex(index) && (index != -1)) {// not enabled
            
            c.setForeground(disabledColor);
            c.setBackground(Color.white);

        } else {
            if (isSelected)
                c.setBackground(super.getBackground());
            else
                c.setBackground(Color.white);
            c.setForeground(super.getForeground());
        }
        
        return c;
    }
    
}