/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.controller;

import com.elle.ProjectManager.dao.IssueDAO;
import com.elle.ProjectManager.entities.Issue;
import java.util.List;

/**
 *
 * @author Yi
 */
public class IssueTableController extends TableController<Issue> {
    
    public IssueTableController(){
        super();
        onlineDAO = new IssueDAO();      
    }

    @Override
    public boolean create(Issue item) {
        if(onlineDAO.insert(item)) {
            onlineItems.put(item.getId(), item);   
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Issue item) {
        if(onlineDAO.update(item)) {
            onlineItems.put(item.getId(), item);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        int[] ids = new int[1];
        ids[0] = id;
        if(onlineDAO.delete(ids)){
            onlineItems.remove(id);
            return true;
        }
        return false;
    }

    
    
}
