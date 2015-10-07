/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.logic.ITableConstants;
import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Create table cell popup window class
 *
 * @author shanxijin
 * @author Xiaoqian Fu
 */
public class TableCellPopupWindow implements ITableConstants {

    // create components for the table cell popup window in project manager window 
    private JTextArea textAreatableCellPopup;
    private JScrollPane areaScrollPanetableCellPopup;
    private JPanel tableCellPopupPanel;
    private JPanel controlPopupPanel;
    private JButton confirmButtonTableCellPopup;
    private JButton cancelButtonTableCellPopup;
    private JButton editBtnPopup;
    private ProjectManagerWindow projectManager;

    // mics
    boolean editBtnIsClick;
    private boolean windowPopup;

    public TableCellPopupWindow(JFrame frame) {
        initTableCellPopup(frame);
//        windowPopup = false;
        projectManager = ProjectManagerWindow.getInstance();
    }

    /*
     * This is to initiate the table cell popup window.
     */
    public void initTableCellPopup(JFrame frame) {

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

        GridBagConstraints tableCellPopupConstraints = new GridBagConstraints();

        // initialize the editButtonTableCellPopup                       
        tableCellPopupConstraints.gridx = 0;
        tableCellPopupConstraints.gridy = 0;
        tableCellPopupConstraints.gridwidth = 1;
        tableCellPopupConstraints.fill = GridBagConstraints.HORIZONTAL;
        tableCellPopupConstraints.anchor = GridBagConstraints.LINE_START;

        editBtnPopup = new JButton("Edit");
        editBtnPopup.setOpaque(true);

        controlPopupPanel.add(editBtnPopup, tableCellPopupConstraints);

        // initialize the confirmButtonTableCellPopup  
        tableCellPopupConstraints.gridx = 1;
        tableCellPopupConstraints.gridy = 0;
        tableCellPopupConstraints.fill = GridBagConstraints.HORIZONTAL;

        confirmButtonTableCellPopup = new JButton("Confirm");
        confirmButtonTableCellPopup.setOpaque(true);

        controlPopupPanel.add(confirmButtonTableCellPopup, tableCellPopupConstraints);

        // initialize the cancelButtonTableCellPopup        
        tableCellPopupConstraints.gridx = 2;
        tableCellPopupConstraints.gridy = 0;
        tableCellPopupConstraints.fill = GridBagConstraints.HORIZONTAL;

        cancelButtonTableCellPopup = new JButton("Cancel");
        cancelButtonTableCellPopup.setOpaque(true);

        controlPopupPanel.add(cancelButtonTableCellPopup, tableCellPopupConstraints);
        System.out.println(confirmButtonTableCellPopup.getPreferredSize());
        System.out.println(cancelButtonTableCellPopup.getPreferredSize());

        if (frame instanceof ProjectManagerWindow) {

            editBtnPopup.setVisible(true);

            confirmButtonTableCellPopup.setVisible(false);
            confirmButtonTableCellPopup.setEnabled(false);
            cancelButtonTableCellPopup.setVisible(false);
            cancelButtonTableCellPopup.setEnabled(false);

            textAreatableCellPopup.setEditable(false);

            textAreatableCellPopup.setSize(new Dimension(500, 200));
            areaScrollPanetableCellPopup.setSize(new Dimension(500, 200));
            tableCellPopupPanel.setSize(500, 200);
            controlPopupPanel.setSize(500, 35);

        } else {
            editBtnPopup.setVisible(false);
            editBtnPopup.setEnabled(false);

            textAreatableCellPopup.setSize(new Dimension(400, 100));
            areaScrollPanetableCellPopup.setSize(new Dimension(400, 100));
            tableCellPopupPanel.setSize(400, 100);
            controlPopupPanel.setSize(400, 35);
        }

        editBtnIsClick = false;
        // set the popup_layer of projectmanager for the table cell popup window
        frame.getLayeredPane().add(tableCellPopupPanel, JLayeredPane.POPUP_LAYER);
        frame.getLayeredPane().add(controlPopupPanel, JLayeredPane.POPUP_LAYER);
    }

    public boolean getWindowPopup() {
        return windowPopup;
    }

    /*
     * This is to set table listener for the table cell popup window.
     * @parm table
     */
    public void setTableListener(JTable table, JFrame frame) {

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int column = table.getSelectedColumn();
                if (table.getName().equals(TASKS_TABLE_NAME)) {
                    if (table.getColumnName(column).equals("title") || table.getColumnName(column).equals("description")
                            || table.getColumnName(column).equals("instructions")) {
                        // popup table cell edit window
                        tableCellPopup(table, frame);
                    } else {
                        setTableCellPopupWindowVisible(false);
                    }
                } else if (table.getName().equals(TASKFILES_TABLE_NAME)) {
                    if (table.getColumnName(column).equals("files") || table.getColumnName(column).equals("notes")
                            || table.getColumnName(column).equals("path")) {
                        // popup table cell edit window
                        tableCellPopup(table, frame);
                    } else {
                        setTableCellPopupWindowVisible(false);
                    }
                } else if (table.getName().equals(TASKNOTES_TABLE_NAME)) {
                    if (table.getColumnName(column).equals("status_notes")) {
                        // popup table cell edit window
                        tableCellPopup(table, frame);
                    } else {
                        setTableCellPopupWindowVisible(false);
                    }
                }
            }
        });
        table.setFocusTraversalKeysEnabled(false);
    }

    /*
     * Make popup window editable, confirm and cancel button show and edit button disappear
     * @parm editable
     */
    public void setEnableEdit(boolean editable) {

        textAreatableCellPopup.setEditable(editable);

        editBtnPopup.setEnabled(!editable);
        editBtnPopup.setVisible(!editable);

        confirmButtonTableCellPopup.setVisible(editable);
        confirmButtonTableCellPopup.setEnabled(editable);
        cancelButtonTableCellPopup.setVisible(editable);
        cancelButtonTableCellPopup.setEnabled(editable);
    }

    public void getTableCellPopup(JTable table, JFrame frame) {

        tableCellPopup(table, frame);
    }

    /*
     * This is to set the table cell popup window visible to edit.
     * @parm selectedTable, row , column
     */
    private void tableCellPopup(JTable selectedTable, JFrame frame) {

        int row = selectedTable.getSelectedRow();
        int column = selectedTable.getSelectedColumn();

        // popup Window popup
        windowPopup = true;

        // set the location of popup window in Project Manager Window or Add record Window
        setLocationOfPopupWindow(selectedTable, frame, row, column);

        // add control + tab keystroke to popup window
        setControlTabKeyEvent(selectedTable, row, column);

        //set Edit, confirm, cancel btn event
        setPopupWindowBtnsEvent(selectedTable, row, column);

        System.out.println("table cell popup method called and popup window show in ? " + windowPopup + " isedit btn clicked? " + editBtnIsClick);

        if (projectManager.getEditMode()) {
            setEnableEdit(true);
            selectedTable.setEnabled(false);
            setProjectManagerFunction(false);
        } else {
            setEnableEdit(false);
            selectedTable.setEnabled(true);
            setProjectManagerFunction(true);
        }

    }

    private void setControlTabKeyEvent(JTable selectedTable, int row, int column) {

        textAreatableCellPopup.setFocusTraversalKeysEnabled(false);

        //register cirt+tab to aumatically confirm and shift to the next cell
        Action confirmAndShiftEvent = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                confirmButtonActionPerformed(e, selectedTable);

                setProjectManagerFunction(true);

                selectedTable.changeSelection(row, column + 1, false, false);

            }
        };

        InputMap im = textAreatableCellPopup.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = textAreatableCellPopup.getActionMap();

        KeyStroke bindingKey = KeyStroke.getKeyStroke("control TAB");

        im.put(bindingKey, "confirm and shift");
        am.put("confirm and shift", confirmAndShiftEvent);
    }

    private void setLocationOfPopupWindow(JTable selectedTable, JFrame frame, int row, int column) {
        // find the selected table cell 
        Rectangle cellRectTable = selectedTable.getCellRect(row, column, true);

        int x, y, ytablePanelLocation, ycontrolPanelLocation;

        if (frame instanceof ProjectManagerWindow) {

            //convert the location in jtable to the location in projectManager window
            cellRectTable = SwingUtilities.convertRectangle(selectedTable, cellRectTable, projectManager);

            x = cellRectTable.x;
            y = cellRectTable.y;

            ytablePanelLocation = y - 20; // actually table Panel's y value

            ycontrolPanelLocation = ytablePanelLocation + tableCellPopupPanel.getHeight();// actually control Panel's y value

            int baseLineHeight = ytablePanelLocation + tableCellPopupPanel.getHeight() + controlPopupPanel.getHeight();//the popup window botton line location's y value

            // make the location of popup window always above the frame bottom line
            if (baseLineHeight > frame.getHeight() - 10) {
                ytablePanelLocation = frame.getHeight() - 25 - tableCellPopupPanel.getHeight() - controlPopupPanel.getHeight();
                ycontrolPanelLocation = ytablePanelLocation + tableCellPopupPanel.getHeight();
            }
        } else {
            cellRectTable = SwingUtilities.convertRectangle(selectedTable, cellRectTable, projectManager.getAddRecordsWindow());

            x = cellRectTable.x;
            y = cellRectTable.y;

            ytablePanelLocation = y - 20; // actually table Panel's y value

            ycontrolPanelLocation = ytablePanelLocation + tableCellPopupPanel.getHeight();// actually control Panel's y value

        }
        // set the table cell popup window visible
        setTableCellPopupWindowVisible(true);

        // use the table cell content to set the content for textarea
        textAreatableCellPopup.setText("");
        textAreatableCellPopup.setText((String) selectedTable.getValueAt(row, column));

        tableCellPopupPanel.setLocation(x, ytablePanelLocation);

        // set the controlPopupPanel position
        controlPopupPanel.setLocation(x, ycontrolPanelLocation);
    }

    // add edit, confirm, cancel buttons event 
    private void setPopupWindowBtnsEvent(JTable selectedTable, int row, int column) {
        editBtnPopup.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setEnableEdit(true);
                projectManager.makeTableEditable(true);
                editBtnIsClick = true;
                setProjectManagerFunction(false);
            }

        });

        // update the table cell content and table cell popup window
        Action confirmButtonAction = new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                confirmButtonActionPerformed(e, selectedTable);

                //recover the other functions in project manager
                setProjectManagerFunction(true);
            }
        };

        confirmButtonTableCellPopup.addActionListener(confirmButtonAction);

        // quit the table cell popup window
        ActionListener cancelButtonAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                setTableCellPopupWindowVisible(false);

                selectedTable.setEnabled(true);

                //recover the other functions in project manager
                setProjectManagerFunction(true);

                selectedTable.getComponentAt(row, column).requestFocus();
            }
        };
        cancelButtonTableCellPopup.addActionListener(cancelButtonAction);
    }

    public void setIsEditButtonClicked(boolean isClicked) {
        this.editBtnIsClick = isClicked;
    }

    public JButton getEditButton() {
        return this.editBtnPopup;
    }

    public boolean isEditButtonClicked() {
        return this.editBtnIsClick;
    }

    private void setProjectManagerFunction(boolean windowPopup) {

        projectManager.setDisableProjecetManagerFunction(windowPopup);

    }

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent e, JTable selectedTable) {
        int row = selectedTable.getSelectedRow();
        int column = selectedTable.getSelectedColumn();

        selectedTable.setEnabled(true);

        if (projectManager.getAddRecordsWindowShow()) {

            String newTableCellValue = textAreatableCellPopup.getText();

            setTableCellPopupWindowVisible(false);
            selectedTable.getModel().setValueAt(newTableCellValue, row, column);

        } else {

            String newTableCellValue = textAreatableCellPopup.getText();

            setTableCellPopupWindowVisible(false);
            selectedTable.setValueAt(newTableCellValue, row, column);
            projectManager.uploadChanges();
        }
    }

    public void setTableCellPopupWindowVisible(boolean Flag) {
        if (Flag) {
            tableCellPopupPanel.setVisible(true);
            controlPopupPanel.setVisible(true);
            windowPopup = true;
        } else {
            tableCellPopupPanel.setVisible(false);
            controlPopupPanel.setVisible(false);
            windowPopup = false;
        }
    }

}
