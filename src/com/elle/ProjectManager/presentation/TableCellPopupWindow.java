/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.logic.ITableConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

/**
 * Create table cell popup window class
 * @author shanxijin
 */
public class TableCellPopupWindow implements ITableConstants{

    
    // create components for the table cell popup window in project manager window 
    private JTextArea textAreatableCellPopup;
    private JScrollPane areaScrollPanetableCellPopup;
    private JPanel tableCellPopupPanel;
    private JPanel controlPopupPanel;
    private JButton confirmButtonTableCellPopup;
    private JButton cancelButtonTableCellPopup; 
    
       
    /*
     * This is to initiate the table cell popup window.
    */ 
    public void initTableCellPopup(JFrame frame){
        
        // initialize the textAreatableCellPopup
        textAreatableCellPopup = new JTextArea();
        textAreatableCellPopup.setOpaque(true);
        textAreatableCellPopup.setBorder(BorderFactory.createLineBorder(Color.gray));
        textAreatableCellPopup.setLineWrap(true);
        textAreatableCellPopup.setWrapStyleWord(true);    
        
        // initialize the areaScrollPanetableCellPopup
        areaScrollPanetableCellPopup = new JScrollPane(textAreatableCellPopup);
        areaScrollPanetableCellPopup.setOpaque(true);       
       
        
        // initialize the tableCellPopupPanel
        tableCellPopupPanel = new JPanel();
        tableCellPopupPanel.add(areaScrollPanetableCellPopup);
        tableCellPopupPanel.setLayout(new BorderLayout());
        tableCellPopupPanel.setOpaque(true);
        tableCellPopupPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
  
        tableCellPopupPanel.setVisible(false);
        
        // initialize the controlPopupPanel
        controlPopupPanel = new JPanel(new GridBagLayout());
        controlPopupPanel.setOpaque(true);

        controlPopupPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        controlPopupPanel.setVisible(false);

        
        // initialize the confirmButtonTableCellPopup                       
        GridBagConstraints tableCellPopupConstraints = new GridBagConstraints();
        tableCellPopupConstraints.gridx = 0;
        tableCellPopupConstraints.gridy = 0;
        tableCellPopupConstraints.fill = GridBagConstraints.HORIZONTAL;
        
        confirmButtonTableCellPopup = new JButton("Confirm");
        confirmButtonTableCellPopup.setOpaque(true);
        controlPopupPanel.add(confirmButtonTableCellPopup, tableCellPopupConstraints);

        // initialize the cancelButtonTableCellPopup        
        tableCellPopupConstraints.gridx = 1;
        tableCellPopupConstraints.gridy = 0;
        tableCellPopupConstraints.fill = GridBagConstraints.HORIZONTAL;
        
        cancelButtonTableCellPopup = new JButton("Cancel");
        cancelButtonTableCellPopup.setOpaque(true);
        controlPopupPanel.add(cancelButtonTableCellPopup, tableCellPopupConstraints);
        
        if(frame instanceof ProjectManagerWindow){
            textAreatableCellPopup.setSize(new Dimension(500,200)); 
            areaScrollPanetableCellPopup.setSize(new Dimension(500,200));       
            tableCellPopupPanel.setSize(500, 200); 
            controlPopupPanel.setSize(500, 35);        
        }
        else{
            textAreatableCellPopup.setSize(new Dimension(400,100)); 
            areaScrollPanetableCellPopup.setSize(new Dimension(400,100));       
            tableCellPopupPanel.setSize(400, 100); 
            controlPopupPanel.setSize(400, 35);            
        }
            
        // set the popup_layer of projectmanager for the table cell popup window
        frame.getLayeredPane().add(tableCellPopupPanel, JLayeredPane.POPUP_LAYER);
        frame.getLayeredPane().add(controlPopupPanel, JLayeredPane.POPUP_LAYER);
        
    }

    /*
     * This is to set table listener for the table cell popup window.
     * @parm table
    */        
    public void setTableListener(JTable table){

        table.addMouseListener(new MouseAdapter(){             
            public void mouseClicked(MouseEvent evt){
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();   
                if(table.getName().equals(TASKS_TABLE_NAME)){                   
                    if(table.getColumnName(column).equals("title") || table.getColumnName(column).equals("description")
                            || table.getColumnName(column).equals("instructions")){
                        // popup table cell edit window
                        tableCellPopup(table, row , column);
                    }else{
                        setTableCellPopupWindowVisible(false);
                    }
                }
                else if(table.getName().equals(TASKFILES_TABLE_NAME)){
                    if(table.getColumnName(column).equals("files") || table.getColumnName(column).equals("notes")
                            || table.getColumnName(column).equals("path")){
                        // popup table cell edit window
                        tableCellPopup(table, row , column);
                    }else{
                        setTableCellPopupWindowVisible(false);
                    }                   
                }
                else if(table.getName().equals(TASKNOTES_TABLE_NAME)){
                    if(table.getColumnName(column).equals("status_notes")){
                        // popup table cell edit window
                        tableCellPopup(table, row , column);
                    }else{
                        setTableCellPopupWindowVisible(false);
                    }                    
                }
            }
        });
        
        table.setFocusTraversalKeysEnabled(false);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){
            @Override
            public boolean dispatchKeyEvent(KeyEvent evt){                
                if(evt.getKeyCode() == KeyEvent.VK_TAB || evt.getKeyCode() == KeyEvent.VK_LEFT ||
                        evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_UP ||
                        evt.getKeyCode() == KeyEvent.VK_DOWN){
                    
                    if (evt.getComponent() instanceof JTable){
                        JTable table = (JTable) evt.getComponent();
                        int row = table.getSelectedRow();                    
                        int column = table.getSelectedColumn();   
                        if(table.getName().equals(TASKS_TABLE_NAME)){
                            
                            if(table.getColumnName(column).equals("title") || table.getColumnName(column).equals("description")
                            || table.getColumnName(column).equals("instructions")){
                                // popup table cell edit window
                                tableCellPopup(table, row , column);                               
                            }else{
                                setTableCellPopupWindowVisible(false);
                            }
                        }
                        else if(table.getName().equals(TASKFILES_TABLE_NAME)){
                            
                            if(table.getColumnName(column).equals("files") || table.getColumnName(column).equals("notes")
                            || table.getColumnName(column).equals("path")){
                                // popup table cell edit window
                                tableCellPopup(table, row , column);
                            }else{
                                setTableCellPopupWindowVisible(false);
                            }                   
                        }
                        else if(table.getName().equals(TASKNOTES_TABLE_NAME)){
                            
                            if(table.getColumnName(column).equals("status_notes")){
                                // popup table cell edit window
                                tableCellPopup(table, row , column);
                            }else{
                                setTableCellPopupWindowVisible(false);
                            }                    
                        }
                    }
                }
                return false; 
            }
        });
    }
        
    
    
    /*
     * This is to set the table cell popup window visible to edit.
     * @parm selectedTable, row , column
    */    
    public void tableCellPopup(JTable selectedTable, int row, int column){
        
        // find the selected table cell 
        Rectangle cellRect = selectedTable.getCellRect(row, column, true);
        
        // set the table cell popup window visible
        setTableCellPopupWindowVisible(true);
   
        // use the table cell content to set the content for textarea
        textAreatableCellPopup.setText("");
        textAreatableCellPopup.setText((String) selectedTable.getValueAt(row, column)); 
        
        if(selectedTable.getColumnCount() == 10 || selectedTable.getColumnCount() == 7){
            // set the tableCellPopupPanel position
            tableCellPopupPanel.setLocation(cellRect.x, cellRect.y + cellRect.height + 5);  

            // set the controlPopupPanel position
            controlPopupPanel.setLocation(cellRect.x, cellRect.y + cellRect.height + 100 + 5);
        } 
        else{
            // set the tableCellPopupPanel position
            tableCellPopupPanel.setLocation(cellRect.x + 2, cellRect.y + cellRect.height + 2 + 150);  

            // set the controlPopupPanel position
            controlPopupPanel.setLocation(cellRect.x + 2, cellRect.y + cellRect.height + 2 + 200 + 150);
        }

        if(selectedTable.getColumnCount() == 10 || selectedTable.getColumnCount() == 7){       
            // update the table cell content and table cell popup window
            confirmButtonTableCellPopup.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String newTableCellValue = textAreatableCellPopup.getText();
                    setTableCellPopupWindowVisible(false);
                    selectedTable.setValueAt(newTableCellValue, row, column);              
                }
            });                
        }
        else{
            // update the table cell content and table cell popup window
            confirmButtonTableCellPopup.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String newTableCellValue = textAreatableCellPopup.getText();
                    setTableCellPopupWindowVisible(false);
                    selectedTable.setValueAt(newTableCellValue, row, column);
                    ProjectManagerWindow.getInstance().uploadChanges();               
                }
            });
        }
       
        // quit the table cell popup window
        cancelButtonTableCellPopup.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){  
                setTableCellPopupWindowVisible(false);                     
            }
        });         
    }   
        
    public void setTableCellPopupWindowVisible(boolean Flag){
        if(Flag){
            tableCellPopupPanel.setVisible(true);
            controlPopupPanel.setVisible(true);             
        }
        else{
            tableCellPopupPanel.setVisible(false);
            controlPopupPanel.setVisible(false);            
        }      
    }
    
}
