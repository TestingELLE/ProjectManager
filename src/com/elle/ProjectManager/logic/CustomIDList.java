/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import java.util.Vector;
import javax.swing.JTable;

/**
 *
 * @author fuxiaoqian
 */
public class CustomIDList extends Vector<Integer> {
    private JTable table;
    
    public CustomIDList(JTable table){
        super();
        this.table = table;
    }
    
    public Integer has(Integer target){
        System.out.println("enter: " + target.toString());
        for(Integer id: this){
            if(id == target){
                System.out.println("has: " + id.toString());
                return id;
            }
        }
        return -1;
    }
    
    public void delete(Integer item){
        this.remove(item);
    }
    
    public void printOutIDList(){
        String line = "current openning issues' id are: ";
        for(Integer id: this){
            line += id + " ";
        }
        System.out.println(line);
    }
    
    
}
