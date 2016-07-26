/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.dao;

import java.util.List;

/**
 *
 * @author Yi
 */
public interface AbstractDAO<T> {
    //implemented in every DAO
    public boolean insert(T item);
    public boolean update(T item);
    public boolean delete(int id);
    public List<T> getAll();
    public T get(int id);
    
    //these functions are for checking updates
    // only implemented in some DAOs.
    //please check each individual DAO before you try to use its functions.
    public String getCurrentServerTimeStamp();
    public List<T> getUpdate(String timestamp);
    public List<Integer> getIDs();
    public int getTotalCnt();
 
}
