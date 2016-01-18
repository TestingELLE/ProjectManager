/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

/**
 * @author fuxiaoqian
 * this class is to change the text component's copy and paste short cut to 
 * command c/v in mac and control c/v in windows
 */
public class ShortCutSetting {
    
    private InputMap ComponentInputMap;
    
    public ShortCutSetting(InputMap ip){
        ComponentInputMap = ip;
    }
    
    /**
     * Changing the text component's copy and paste short cut to 
     * command c/v in mac and control c/v in windows
     */
    public void copyAndPasteShortCut() {
        ComponentInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.copyAction);
        ComponentInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.pasteAction);
        ComponentInputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMask()), DefaultEditorKit.cutAction);
    }
}
