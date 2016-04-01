/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import java.util.Vector;

/**
 *
 * @author fuxiaoqian
 */
public class CustomIDList {
    private String selectedTabName;
    private Vector<Integer> idList;
    
    public CustomIDList(String tabName){
        selectedTabName = tabName;
        idList = new Vector<Integer>();
        
    }
}
