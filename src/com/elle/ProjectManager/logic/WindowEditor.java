/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import java.awt.Component;
import java.awt.Container;
import javax.swing.AbstractButton;

/**
 * WindowEditor
 * This class has methods to edit windows
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class WindowEditor {
    
    /**
     * removeMinMaxClose
     * This removes all the buttons on the title bar (Close, Min, and Max)
     * @param comp 
     */
    public static void removeMinMaxClose(Component comp) {
        
        if(comp instanceof AbstractButton){
          comp.getParent().remove(comp);
        }
        
        if (comp instanceof Container){
            Component[] comps = ((Container)comp).getComponents();
            for(int x = 0, y = comps.length; x < y; x++){
              removeMinMaxClose(comps[x]);
            }
        }
    }
}
