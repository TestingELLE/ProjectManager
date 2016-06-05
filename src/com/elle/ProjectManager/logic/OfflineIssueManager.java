/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.dao.IssueDAO;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Yi
 * It is the class to manage all offline issues
 */
public class OfflineIssueManager {
    private final ProjectManagerWindow projectManager;
    private final File dir;
    private Map<Issue, File> issuesList;
    private final String userName;
    private ArrayList<Integer> ids;
    //the id is for starting id of new issue, when offlineIssueManager instantiated, it has to set the number 
    //based on the current offline data folder
    
    private static int newIssueId;
    
    public OfflineIssueManager(String userName) {
        //initialize the offline data folder
        projectManager = ProjectManagerWindow.getInstance();
        dir = FilePathFormat.localDataFilePath();
        issuesList = new HashMap();
        
        //set up the offline issues' ids and map
        readInLocalData();
        setIds();
        
        //sync local data
        if (projectManager.isOnline())
        syncLocalData();
        
        
        newIssueId = initId();
        
        this.userName = userName;
        
    }
    
    private void readInLocalData(){
        File[] listOfFiles = dir.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            Issue temp = readIssueFromFile(listOfFiles[i]);
            if (temp != null) {
                issuesList.put(temp, listOfFiles[i]);
            }
        }
    }
    
    private Issue readIssueFromFile(File file) {
         try{
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(file));
            Issue temp = (Issue)ois.readObject();
            ois.close();
            return temp;
          
        }  catch (IOException ex) {
            LoggingAspect.afterThrown(ex);
        } catch (ClassNotFoundException ex) {
            LoggingAspect.afterThrown(ex);
        }
        return null;
        
    }
    
    //populate the offline data
    public void loadTableData(JTable table) {
        for(Issue issue : issuesList.keySet()) {
            String app = issue.getApp();
            
            if (table.getName().equals(app))
                projectManager.insertTableRow(table, issue);    
            
        }
        
    }
    
    //look into data folder, and find the minimum number in current offline data folder
    private int initId() {
        File[] listOfFiles = dir.listFiles();
        int temp = -10;
        
        //get the minimum id from offline files
        for (int i = 0; i < listOfFiles.length; i++) {
            String name = listOfFiles[i].getName();
            Pattern p = Pattern.compile("_id_([-]?[0-9]+)");
            Matcher m = p.matcher(name);
            if (m.find()){
                int id = Integer.parseInt(m.group(1));
                if (id < temp) 
                    temp = id;   
            }
        } 
        return temp -1 ;  
    }
    
    //generate the filename
    private File generateFileName(Issue issue) {
        String status;
        String timeStamp = currentTimeStamp().replaceAll(":", "-");
        
        if (issue.getId() < 0) status = "new";
        else status = "update";
        String filename = status + "_"+ userName + "_" + "id_" + issue.getId() + "_" + timeStamp + ".ser";
        return new File(dir, filename);
    }
    
    //save issue to file
    private boolean saveIssueToFile(Issue issue, File issuefile) {
      
        try {
            if (!issuefile.exists()) 
                issuefile.createNewFile();
	    FileOutputStream fos = new FileOutputStream(issuefile);	
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(issue);
            oos.close();
            LoggingAspect.addLogMsgWthDate("Offline issue : " + issue.getId() +" is saved successfully");
            return true;
            
        } catch (IOException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        } 
    }
    
    private Issue copyIssue(Issue issue) {
        Issue temp = new Issue();
        temp.setId(issue.getId());
        temp.setApp(issue.getApp());
        temp.setTitle(issue.getTitle());
        temp.setDescription(issue.getDescription());
        temp.setProgrammer(issue.getProgrammer());
        temp.setDateOpened(issue.getDateOpened());
        temp.setRk(issue.getRk());
        temp.setVersion(issue.getVersion());
        temp.setDateClosed(issue.getDateClosed());
        temp.setIssueType(issue.getIssueType());
        temp.setSubmitter(issue.getSubmitter());
        temp.setLocked(issue.getLocked());
        temp.setLastmodtime(issue.getLastmodtime());
        return temp;
    }
    
    public boolean addIssue(Issue issue) { 
        if (issue.getId() == -1) {
            issue.setId(newIssueId);
            newIssueId --;  //need to update the newIssueId to use for next new issue
        }
        Issue newIssue = copyIssue(issue);
        
        
        File filename = generateFileName(newIssue);
        
        if (saveIssueToFile(newIssue, filename)) {
            issuesList.put(newIssue, filename);
            //System.out.println("new issue added : " + issue.getId());
            //update the ids list
            ids.add(newIssue.getId());
            
            return true;
        }
        else return false;
    }
    
    public boolean updateIssue(Issue issue) {
        //check the offline mgr, if the issue is already in.
        
        if (issue.getId() < 9000 && issue.getId() > 0) 
            issue.setId(issue.getId()+9000);
            
        
        Issue foundIssue = getIssue(issue);
        
        if (foundIssue == null) {
            return addIssue(issue);
        }
        else {
            
            removeIssue(foundIssue);
            return addIssue(issue);
        }
        
        
        
    }
    
    
    public void removeIssue(Issue issue) { 
       
        
        
        
         //clean up local data
        File filename = issuesList.get(issue);
        System.out.println(filename.getName());
        filename.delete();
        issuesList.remove(issue);
        
        int index = ids.indexOf(issue.getId());
        ids.remove(index);
        
        
    }
    
    
    public void deleteIssues(int[] ids) {
        for (int i : ids) {
            for (Issue temp : issuesList.keySet()) {
                if (temp.getId()== i) {
                    removeIssue(temp);
                    break;
                }
            }
        } 
    }

    public Map<Issue, File> getIssuesList() {
        return issuesList;
    }
    
    
    public Issue getIssue(Issue issue) {
        
        for (Issue temp : issuesList.keySet()) 
            if (temp.getId() == issue.getId()) {
                return temp;
            }
        
        return null;
            
    }
    
    public Issue getIssue(int id) {
        for (Issue temp : issuesList.keySet()) {
            if (temp.getId() == id) {
                
                return temp;
            }   
        }
        return null;
            
    }
    
    private String currentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds() {
        ids = new ArrayList<Integer>();
        if (!issuesList.isEmpty()) {
            for(Issue temp : issuesList.keySet()) {
                ids.add(temp.getId());
            }
        }
    }
    
    //for data sync
    
    public void syncLocalData() {
        IssueDAO dao = new IssueDAO();  
        int[] ids = new int[issuesList.size()];
        int index = 0;
        for (Issue issue : issuesList.keySet()) {
            if (syncIssueToDbFromFile(issuesList.get(issue),dao))
                ids[index++] = issue.getId();
            
            
        }
        deleteIssues(ids);
    }
    
    
    
    private boolean syncIssueToDbFromFile(File file, IssueDAO dao) {
        LoggingAspect.addLogMsgWthDate("Now sync file: " + file.getName());
        
        try{
            ObjectInputStream ois =
                    new ObjectInputStream(new FileInputStream(file));
            Issue temp = (Issue)ois.readObject();
            
            //record the offline id , later remove from table using the offline Id
            int originalId = temp.getId();
            ois.close();
            //reset the update id
            if (temp.getId() > 9000) temp.setId(temp.getId()-9000);
            
            //check update time stamp, if it is older than db issue, pass for manual inspection.
            if (temp.getId() > 0) {
                Issue dbIssue = dao.get(temp.getId());
                if (temp.getLastmodtime().compareTo(dbIssue.getLastmodtime()) < 0){
                    String message = "Offline issue " + originalId + " has an older timestamp,\nplease update manually.";
                    JOptionPane.showMessageDialog(projectManager, message);
                    return false;
                }
                    
            }
            
            
            
            boolean success = (originalId < 0) ? dao.insert(temp):dao.update(temp);
            if (success) {
                
                
                if (projectManager.getTabs() != null) {
                   for (Tab tab : projectManager.getTabs().values()) {
                        if (tab.getTable().getName().equals(temp.getApp())){
                      
                            projectManager.removeTableRow(tab.getTable(), originalId);
                            projectManager.removeTableRow(tab.getTable(), temp.getId());
                            projectManager.insertTableRow(tab.getTable(),temp);
                            projectManager.makeTableEditable(false);
                            break;
                        }
                    }
                    
                }

                LoggingAspect.addLogMsg(file.getName() +" is updated to server successfully");
                
                return true;
            }
            
            else{
                LoggingAspect.addLogMsg(file.getName() + " failed to update to db server");    
                return false;
            }
            
        }  catch (IOException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        } catch (ClassNotFoundException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
        
        
    }
     
    
    public void print(){
        for (Issue temp : issuesList.keySet()) {
            System.out.println(temp.getId());
        }
        
    }
    
     
    

}
