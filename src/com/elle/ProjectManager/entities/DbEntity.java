/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.entities;

/**
 *
 * @author Yi
 */
public abstract class DbEntity  {
    protected int id;
    public DbEntity(){
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //this is to create a deep clone copy of the entity object
    public abstract Object deepClone();
    
    
}
