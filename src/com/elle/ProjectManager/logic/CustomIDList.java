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
public class CustomIDList extends Vector<Object> {
    private JTable table;
    
    public CustomIDList(JTable table){
        super();
        this.table = table;
    }
    
    public void delete(Object item){
//        for(int index = 0; index < this.size(); index++){
//            System.out.println("here is " +this.elementAt(index) + "item is " + item);
//            if(this.elementAt(index) == item){
//                System.out.println("delete: " + item);
//                this.remove(index);
//                this.remove(item);
//            }
//        }
        System.out.println("delete: " + item);
        this.remove(item);
    }
    
    public void printOutIDList(){
        String line = "current openning issues' id are: ";
        for(Object id: this){
            line += id.toString() + " ";
        }
        System.out.println(line);
    }
    
    
}
