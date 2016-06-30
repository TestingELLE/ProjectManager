/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.entities.Issue;
import java.util.ArrayList;

/**
 *
 * @author Yi
 */
public interface AbstractDAO {
    public boolean insert(Issue issue);
    public boolean update(Issue issue);
    public Issue getSelectedRow(String tableName, String row);
    public ArrayList<Issue> get(String tableName);
    public Issue get(int id);
    public boolean delete(int[] ids);
    
}
