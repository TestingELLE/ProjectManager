
package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.logic.CheckBoxItem;
import com.elle.ProjectManager.logic.CheckBoxList;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 *
 * @author Carlos Igreja
 */
public class PopupWindow extends JFrame{
    
    private String title;
    private String message;
    private Component component;
    private JButton[] buttons;
    private Dimension dimension;
    private int row;
    private int col;
    
    /**
     * Creates a popup window
     * @param title          window title
     * @param message        a message to display to user
     * @param component      a component to display in the window
     * @param buttons        buttons to display in the window
     * @param dimension      the size of the window
     */
    public PopupWindow(String title, String message, Component component, JButton[] buttons, Dimension dimension){
        
        // set title
        this.setTitle(title);
        
        // change layout of frame
        this.setLayout(new GridBagLayout());
        
        // add message
        addMessage(message);
        
        // add component
        addComponent(component);
        
        // add buttons
        addButtons(buttons);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(dimension);

        this.pack();
    }
    
    private void addMessage(String message){
        
        // JPanel
        JPanel panel = new JPanel();
        
        // Jlabel to display message
        JLabel label = new JLabel(message);
        
        // add label to panel
        panel.add(label);
        
        // set constraints for the component
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.fill = GridBagConstraints.BOTH;
        labelConstraints.weightx = 1; // takes up whole x axis
        labelConstraints.weighty = 0; // takes up most y axis with room for buttons
        labelConstraints.gridx = 0; // first col cell
        labelConstraints.gridy = 0; // first row cell

        // add component panel to frame
        this.add(panel, labelConstraints);
    }
    
    private void addComponent(Component component){
        
        // create a panel for component
        JPanel panel = new JPanel();
        
        // add component to panel
        panel.add(component);
        
        // set constraints for the component
        GridBagConstraints componentConstraints = new GridBagConstraints();
        componentConstraints.fill = GridBagConstraints.BOTH;
        componentConstraints.weightx = 1; // takes up whole x axis
        componentConstraints.weighty = 1; // takes up most y axis with room for buttons
        componentConstraints.gridx = 0; // first col cell
        componentConstraints.gridy = 1; // first row cell

        // add component panel to frame
        this.add(component, componentConstraints);
    }
    
    private void addButtons(JButton[] buttons){
        
        // create a panel for buttons
        JPanel panelButtons = new JPanel();

        // add buttons to panel
        for(JButton button: buttons){
            panelButtons.add(button);
        }

        // set constraints for the buttons panel
        GridBagConstraints buttonsPanelConstraints = new GridBagConstraints();
        buttonsPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonsPanelConstraints.weightx = 1; // takes up whole x axis
        buttonsPanelConstraints.weighty = 0; // takes up enough y axis just for buttons
        buttonsPanelConstraints.gridx = 0; // first col cell
        buttonsPanelConstraints.gridy = 2; // second row cell

        // add panel to the frame
        this.add(panelButtons,buttonsPanelConstraints);
    }
    
}
