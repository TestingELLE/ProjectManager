
package com.elle.ProjectManager.logic;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * JTableCellRenderer
 * This is a custom cell renderer to color cells
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class JTableCellRenderer extends DefaultTableCellRenderer{
    
    private Map<Integer,ArrayList<Integer>> cells;  // cells to color
    private String[][] data;                        // original model data
    private Color defaultCellColor;
    private Color selectedCellColor;
    private Color modifiedCellColor;

    /**
     * CONSTRUCTOR 
     * @param table // the table this is for
     */
    public JTableCellRenderer(JTable table) {
        
        // initialize the Map of cells
        cells = new HashMap<>();
        for(int col = 0; col < table.getColumnCount(); col++){
            cells.put(col, new ArrayList<>());
        }
        
        // initialize the default cell color
        defaultCellColor = table.getBackground();
        selectedCellColor = table.getSelectionBackground();
        modifiedCellColor = new Color(44,122,22);
    }

    public Map<Integer, ArrayList<Integer>> getCells() {
        return cells;
    }

    public void setCells(Map<Integer, ArrayList<Integer>> cells) {
        this.cells = cells;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }
    
    public void clearCellRender(){
        
        // clear colors from cells
        for(int col = 0; col < cells.size(); col++){
            cells.get(col).clear();
        }
    }

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col ){
        
        row = table.convertRowIndexToModel(row);
        col = table.convertColumnIndexToModel(col);
        
        Component component =  super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        
        // check if cell is in the list
        if(!cells.get(col).isEmpty() && cells.get(col).contains(row)){
            component.setBackground(modifiedCellColor);
        }
        else{
            if(isSelected){
                component.setBackground(selectedCellColor);
            }
            else{
                component.setBackground(defaultCellColor);
            }
        }
        
        return component;
    }
}
