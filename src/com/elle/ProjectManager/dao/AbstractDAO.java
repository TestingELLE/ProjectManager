/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.entities.Issue;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yi
 */
public interface AbstractDAO<T> {
    public boolean insert(T item);
    public boolean update(T item);
    public abstract boolean delete(int id);
    public abstract List<T> getAll();
    public abstract T get(int id);
 
}
