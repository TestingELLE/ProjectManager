/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.entities.Issue;
import static com.elle.ProjectManager.logic.UtilityTools.byteArrayToString;
import java.util.Vector;

/**
 *
 * @author Yi
 */
public class IssueConverter implements Converter<Issue> {

    @Override
    public Object[] convertToRow(Issue item) {
        Object[] rowData = new Object[13];
        rowData[0] = item.getId();
        rowData[1] = item.getApp();
        rowData[2] = item.getTitle();
        rowData[3] = byteArrayToString(item.getDescription());
        rowData[4] = item.getProgrammer();
        rowData[5] = item.getDateOpened();
        rowData[6] = item.getRk();
        rowData[7] = item.getVersion();
        rowData[8] = item.getDateClosed();
        rowData[9] = item.getIssueType();
        rowData[10] = item.getSubmitter();
        rowData[11] = item.getLocked();
        rowData[12] = item.getLastmodtime();
        return rowData;
       
    }

    //table row description cannot be changed as it does not contain style info
    //this conversion only incorporates incorporate the rest columns
    @Override
    public Issue convertFromRow(Object[] rowData) {
        Issue item = new Issue();
        item.setId((int) rowData[0]);
        item.setApp((String) rowData[1]);
        item.setTitle((String) rowData[2]);
        item.setProgrammer((String) rowData[4]);
        item.setDateOpened((String) rowData[5]);
        item.setRk((String) rowData[6]);
        item.setVersion((String) rowData[7]);
        item.setDateClosed((String) rowData[8]);
        item.setIssueType((String) rowData[9]);
        item.setSubmitter((String) rowData[10]);
        item.setLocked((String) rowData[11]);
        item.setLastmodtime((String) rowData[12]);
        
        return item;
        
        
    }
    
}
