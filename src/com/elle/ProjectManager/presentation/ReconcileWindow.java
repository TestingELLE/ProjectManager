/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.controller.PMDataManager;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.ConflictItemPair;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

/**
 *
 * @author Yi
 */
public class ReconcileWindow extends javax.swing.JFrame {
    
    private PMDataManager dataManager;
    private ProjectManagerWindow pmWindow;
    private ArrayList<ConflictItemPair<Issue>> conflictIssues;
    private ConflictItemPair<Issue> currentIssuePair;
    private int index;
    private int total;
    
    

    
    public ReconcileWindow() {
        dataManager = PMDataManager.getInstance();
        pmWindow = ProjectManagerWindow.getInstance();
        conflictIssues = dataManager.getConflictIssues();
        index = 0;
        
        initComponents();
        jScrollPane2.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        
        jScrollPane1.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        
        
        //populate table with currentIssuePair
        setComponentsValues();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.pack();
     
    }
    
    
    private void setComponentsValues()  {
        try {
            //data is changed dynamically, each time, should reference conflictIssues again
            conflictIssues = dataManager.getConflictIssues();
            total = conflictIssues.size();
            currentIssuePair = conflictIssues.get(index);
            
            //set labels of progress
            totalLabel.setText(String.valueOf(total));
            currentCnt.setText(String.valueOf(index + 1));
            

            //updateBtn will only show on the last page
            if (index == 0) {
                previousButton.setEnabled(false);
            }
            else
                previousButton.setEnabled(true);
            
            if (index == total - 1) {
                
                nextButton.setEnabled(false);
            }
            else {
                
                nextButton.setEnabled(true);
            }
            
            
            //populate issues
            
            idLabel.setText(String.valueOf(currentIssuePair.getDbItem().getId()));
            titleLabel.setText(currentIssuePair.getDbItem().getTitle());
            byte[] dbissue = currentIssuePair.getDbItem().getDescription();
            InputStream dbissuestream = new ByteArrayInputStream(dbissue);
            byte[] offlineissue = currentIssuePair.getLocalItem().getDescription();
            InputStream offlinestream = new ByteArrayInputStream(offlineissue);
            onlinertftext.setText("");
            onlinertftext.getEditorKit().read(dbissuestream, onlinertftext.getDocument(), 0);
            offlinertftext.setText("");
            offlinertftext.getEditorKit().read(offlinestream, offlinertftext.getDocument(), 0);
        } catch (IOException ex) {
            Logger.getLogger(ReconcileWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(ReconcileWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String extracttextfrombyte (byte[] inputbytes)  {
        try {
            byte[] descriptiontablebytesout;
            
            if (inputbytes == null) {
                descriptiontablebytesout = new byte[0];
            } else {
                descriptiontablebytesout = inputbytes;
            };
            
            RTFEditorKit rtfParser = new RTFEditorKit();
            Document document = rtfParser.createDefaultDocument();
            rtfParser.read(new ByteArrayInputStream(descriptiontablebytesout), document, 0);
            String text = document.getText(0, document.getLength());
            
            return text;
        } catch (IOException ex) {
            Logger.getLogger(ReconcileWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(ReconcileWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    
    
    private void setIssuePairValuesFromComponents() {
        ByteArrayOutputStream dbissueoutputstream = new ByteArrayOutputStream(); 
        ByteArrayOutputStream offlineissueoutputstream = new ByteArrayOutputStream(); 
        
        try {
            onlinertftext.getEditorKit().write(dbissueoutputstream, onlinertftext.getDocument(), 0, onlinertftext.getDocument().getLength());
        } catch (IOException ex) {
            Logger.getLogger(ReconcileWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(ReconcileWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] dbissuebyte = dbissueoutputstream.toByteArray();
        
        try {
            offlinertftext.getEditorKit().write(offlineissueoutputstream, offlinertftext.getDocument(), 0, offlinertftext.getDocument().getLength());
        } catch (IOException ex) {
            Logger.getLogger(ReconcileWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(ReconcileWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] offlinebyte = offlineissueoutputstream.toByteArray();

        currentIssuePair.getDbItem().setDescription(dbissuebyte);
        currentIssuePair.getLocalItem().setDescription(offlinebyte);
     
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        currentCnt = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        nextButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        updateOnlineBtn = new javax.swing.JButton();
        updateOfflineBtn = new javax.swing.JButton();
        leftPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        onlinertftext = new javax.swing.JTextPane();
        rightPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        offlinertftext = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Reconcile Issues");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel2.setText("Id");

        idLabel.setText("0");

        jLabel4.setText("Title :");

        titleLabel.setText("issue title");

        totalLabel.setText("Total Cnt");

        jLabel5.setText("/");

        currentCnt.setText("Current Cnt");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(currentCnt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalLabel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(idLabel)
                    .addComponent(totalLabel)
                    .addComponent(jLabel5)
                    .addComponent(currentCnt))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(titleLabel))
                .addGap(5, 5, 5))
        );

        nextButton.setText(">");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        previousButton.setText("<");
        previousButton.setPreferredSize(new java.awt.Dimension(50, 30));
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        updateOnlineBtn.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        updateOnlineBtn.setText("Update");
        updateOnlineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateOnlineBtnActionPerformed(evt);
            }
        });

        updateOfflineBtn.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        updateOfflineBtn.setText("Update");
        updateOfflineBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateOfflineBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(previousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(updateOnlineBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateOfflineBtn)
                .addGap(81, 81, 81)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {updateOfflineBtn, updateOnlineBtn});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(previousButton, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateOnlineBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateOfflineBtn))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {nextButton, previousButton});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {updateOfflineBtn, updateOnlineBtn});

        leftPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        leftPanel.setPreferredSize(new java.awt.Dimension(254, 333));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Online");

        onlinertftext.setContentType("text/rtf"); // NOI18N
        onlinertftext.setPreferredSize(new java.awt.Dimension(240, 80));
        jScrollPane1.setViewportView(onlinertftext);

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addContainerGap())
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );

        rightPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Offine");
        jLabel7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        offlinertftext.setContentType("text/rtf"); // NOI18N
        offlinertftext.setPreferredSize(new java.awt.Dimension(240, 80));
        jScrollPane2.setViewportView(offlinertftext);

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(5, 5, 5))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
         
        
        //save to current Issue
        setIssuePairValuesFromComponents();
        
        index --;
        currentIssuePair = conflictIssues.get(index);
        
        setComponentsValues();
        
        
    }//GEN-LAST:event_previousButtonActionPerformed

    private void updateOnlineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateOnlineBtnActionPerformed
       //confirm to continue
        
        
        int confirm = JOptionPane.showConfirmDialog(this, "Update action is final.Please confirm to continue.", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm != 0) return;
      
        setIssuePairValuesFromComponents();
        //update the choice in the pair to indicate selection is online one
        currentIssuePair.setChoice(true);
        dataManager.resolveConflictPair(currentIssuePair);
        
        
        if (dataManager.getConflictIssues().size() > 0) {
            if (index == dataManager.getConflictIssues().size()) {
                index --;
            }
            
            setComponentsValues();
           
        }
        else reconcileWindowClosing();
    }//GEN-LAST:event_updateOnlineBtnActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        
            //save to current Issue
            setIssuePairValuesFromComponents();
            
            // TODO add your handling code here:
            index ++;
            currentIssuePair = conflictIssues.get(index);
            setComponentsValues();
       
    }//GEN-LAST:event_nextButtonActionPerformed

    private void updateOfflineBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateOfflineBtnActionPerformed
        //confirm to continue
        int confirm = JOptionPane.showConfirmDialog(this, "Update action is final.Please confirm to continue.", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm != 0) return;
        setIssuePairValuesFromComponents();
        //update the choice in the pair to indicate selection is offline one
        currentIssuePair.setChoice(false);
        dataManager.resolveConflictPair(currentIssuePair);
        
        
        if (dataManager.getConflictIssues().size() > 0) {
            if (index == dataManager.getConflictIssues().size()) {
                index --;
            }
            
            setComponentsValues();
           
        }
        else reconcileWindowClosing();
    }//GEN-LAST:event_updateOfflineBtnActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        reconcileWindowClosing();
    }//GEN-LAST:event_formWindowClosing

   
    
    private void reconcileWindowClosing() {
        
        pmWindow.setEnabled(true);
        pmWindow.reloadAllData();
        LoggingAspect.afterReturn("Conflicts are resolved.");
        this.dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel currentCnt;
    private javax.swing.JLabel idLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JTextPane offlinertftext;
    private javax.swing.JTextPane onlinertftext;
    private javax.swing.JButton previousButton;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel totalLabel;
    private javax.swing.JButton updateOfflineBtn;
    private javax.swing.JButton updateOnlineBtn;
    // End of variables declaration//GEN-END:variables
}
