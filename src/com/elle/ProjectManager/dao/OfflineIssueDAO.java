/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.dao;

import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.FilePathFormat;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Yi
 */
public class OfflineIssueDAO implements AbstractDAO<Issue>{
    
    private final File dir;
    private String prefix;
   
    //the id is for starting id of new issue, when offlineIssueManager instantiated, it has to set the number 
    //based on the current offline data folder
    private static int newIssueId;
    public OfflineIssueDAO(String prefix){
        dir = FilePathFormat.localDataFilePath();
        
        this.prefix = prefix;
        newIssueId = initId();
    }
    
    //look into data folder, and find the minimum number in current offline data folder
    private int initId() {
        File[] listOfFiles = dir.listFiles();
        int temp = -10;

        //get the minimum id from offline files
        for (int i = 0; i < listOfFiles.length; i++) {
            String name = listOfFiles[i].getName();
            if (name.startsWith(prefix)){
                Pattern p = Pattern.compile("_id_([-]?[0-9]+)");
                Matcher m = p.matcher(name);
                if (m.find()) {
                    int id = Integer.parseInt(m.group(1));
                    if (id < temp) {
                        temp = id;
                    }
                }
                
            }
            
        }
        return temp - 1;
    }

    private boolean saveToFile(Issue issue){
        String fileName =  prefix + "_" + "id_" + issue.getId() + ".ser";
        File path = new File(dir, fileName);
        try {
            if (!path.exists()) {
                path.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(issue);
            oos.close();
            LoggingAspect.addLogMsgWthDate("Offline issue #" + issue.getId() + " is saved successfully");
            return true;

        } catch (IOException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
    }

    private Issue readIssueFromFile(File file) {
        try {
            ObjectInputStream ois
                    = new ObjectInputStream(new FileInputStream(file));
            Issue temp = (Issue) ois.readObject();
            ois.close();
            return temp;

        } catch (IOException ex) {
            LoggingAspect.afterThrown(ex);
        } catch (ClassNotFoundException ex) {
            LoggingAspect.afterThrown(ex);
        }
        return null;

    }

    @Override
    public boolean insert(Issue item) {
        if (item.getId() == -1) {
            item.setId(newIssueId);
            newIssueId--;  //need to update the newIssueId to use for next new issue
        }
        if(saveToFile(item)) return true;
        else 
            return false;
    }

    @Override
    public boolean update(Issue item) {
        int id = item.getId();
        if(id > 0 && id < 9000) {  
            item.setId(9000 + id);
        }
        return insert(item);
        
    }

    @Override
    public boolean delete(int id) {
        File[] listOfFiles = dir.listFiles();
        
        //get the minimum id from offline files
        for (int i = 0; i < listOfFiles.length; i++) {
            String name = listOfFiles[i].getName();
            String searchStr = prefix + "_" + "id_" + id;
            if (name.contains(searchStr)){
                if(listOfFiles[i].delete()) return true;
                
            }
            
        }
        return false;
        
    }

    @Override
    public List<Issue> getAll() {
        ArrayList<Issue> issues = new ArrayList();
        File[] listOfFiles = dir.listFiles();
        
        //get the minimum id from offline files
        for (int i = 0; i < listOfFiles.length; i++) {
            String name = listOfFiles[i].getName();
            if (name.startsWith(prefix))
               
                issues.add(readIssueFromFile(listOfFiles[i]));
        }
        return issues;
    }

    @Override
    public Issue get(int id) {
        File[] listOfFiles = dir.listFiles();
        
        //get the minimum id from offline files
        for (int i = 0; i < listOfFiles.length; i++) {
            String name = listOfFiles[i].getName();
            String searchStr = prefix + "_" + "id_" + id;
            
            if (name.contains(searchStr)){
                
                return readIssueFromFile(listOfFiles[i]);
            }
            
        }
        return null;
    }

    //following functions no implementation as not required.
    @Override
    public String getCurrentServerTimeStamp() {
        return null;
    }

    @Override
    public List<Issue> getUpdate(String timestamp) {
        return null;
    }

    @Override
    public List<Integer> getIDs() {
        return null;
    }

    @Override
    public int getTotalCnt() {
        return 0;
    }
    
    
}
