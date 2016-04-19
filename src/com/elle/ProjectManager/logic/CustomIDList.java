
package com.elle.ProjectManager.logic;

import java.util.ArrayList;

/**
 *
 * @author fuxiaoqian
 */
public class CustomIDList extends ArrayList<Integer> {
    
    public CustomIDList(){
        super();
    }
    
    public Integer has(Integer target){
//        System.out.println("enter: " + target.toString());
        for(Integer id: this){
            if(id == target){
//                System.out.println("has: " + id.toString());
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
