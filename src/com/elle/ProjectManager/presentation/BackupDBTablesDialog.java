package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.dao.BackupDBTableDAO;
import com.elle.ProjectManager.entities.BackupDBTableRecord;
import com.elle.ProjectManager.logic.BackupTableCheckBoxItem;
import com.elle.ProjectManager.logic.CheckBoxList;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This is a JDialog window for backing up DB tables.
 * @author Carlos Igreja
 * @since  2-11-2016
 */
public class BackupDBTablesDialog extends javax.swing.JPanel {

    // private variables
    private Component parentComponent; // used to display noBackupsMsg relative to parent component
    private CheckBoxList checkBoxList;
    private ArrayList<BackupTableCheckBoxItem> checkBoxItems;
    private Dimension dimension = new Dimension(600,400); // dimension
    private final String CHECK_ALL_ITEM_TEXT = "(All)";
    private BackupDBTableDAO dao;
    private String[] tableNames = {"issues"};

    /**
     * Creates new form BackupDBTablesWindow
     */
    public BackupDBTablesDialog(Component parent) {
        initComponents();

        this.parentComponent = parent;
        this.dao = new BackupDBTableDAO(parent);

        setCheckBoxListListener();

        // checkbox item array
        checkBoxItems = new ArrayList<>();
        checkBoxItems.add(new BackupTableCheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(getCheckBoxItemsFromDB());

        // if checkBoxItems only contains one item (check all) then remove it
        if(checkBoxItems.size() == 1){
            checkBoxItems.clear();
            displayNoBackupsCheckListMsg();
        }
        else{
            // add CheckBoxItems to CheckBoxList
            checkBoxList.setListData(checkBoxItems.toArray());
        }

        // add CheckBoxList to the panel
        ScrollPane scroll = new ScrollPane();
        scroll.add(checkBoxList);
        scroll.setPreferredSize(panelOutput.getPreferredSize());
        panelOutput.setLayout(new BorderLayout());
        panelOutput.add(scroll, BorderLayout.CENTER);

        // start the delete button as not enabled
        btnDelete.setEnabled(false);

        // set title
        String title = "Backup Tables"; // window title

        // show window
        setVisible(true);

        // add to a JDialog for modal funtionality
        JDialog dialog = new JDialog((Frame) parent, title, true);
        dialog.add(this);
        dialog.setSize(dimension);
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);

    }

    public void setCheckBoxListListener(){

        // create the checkbox JList
        checkBoxList = new CheckBoxList(); // JList

        // add mouseListener to the list
        checkBoxList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)
            {
                // get the checkbox item index
               int index = checkBoxList.locationToIndex(e.getPoint());

               // index cannot be null
               if (index != -1) {

                   // get the check box item at this index
                  JCheckBox checkbox = (JCheckBox)
                              checkBoxList.getModel().getElementAt(index);

                  // check if the (All) selection was checked
                  if(checkbox.getText().equals(CHECK_ALL_ITEM_TEXT)){
                      if(checkbox.isSelected()){
                          removeAllChecks();
                      }
                      else{
                          checkAllItems();
                      }
                  }
                  //rename the checkbox item
                  else if(e.isControlDown()){
                      renameCheckBoxItem((BackupTableCheckBoxItem)checkbox);
                  }
                  else{
                      // toggle the check for the checkbox item
                      checkbox.setSelected(!checkbox.isSelected());
                  }
                  checkBoxList.repaint(); // redraw graphics
                  // enable buttons
                  if(isACheckBoxChecked()){
                      btnDelete.setEnabled(true);
                      btnBackup.setEnabled(false);
                  }
                  else{
                      btnDelete.setEnabled(false);
                      btnBackup.setEnabled(true);
                  }
               }
            }
        });
    }

    public void renameCheckBoxItem(BackupTableCheckBoxItem checkbox) {

        BackupDBTableRecord record = checkbox.getRecord();
        String backupTableName = record.getBackupTableName();
        String msg = "Rename " + backupTableName;
        boolean updateSuccess = true;

        // input dialog
        String newName = JOptionPane.showInputDialog(this, // parent 
                                                     msg,  // msg
                                                     backupTableName); // init selected

        if(newName != null && !newName.equals(backupTableName)){
            record.setBackupTableName(newName);
            if(!dao.updateRecord(record,backupTableName)){
                updateSuccess = false;
            }
        }

        msg = (updateSuccess)? "Update Complete!":"Update Failed!";
        JOptionPane.showMessageDialog(this, msg);
        LoggingAspect.afterReturn(msg);
        if(updateSuccess){
            reloadCheckList();
        }
    }
    
    /**
     * removeAllChecks
     */
    public void removeAllChecks(){

        for(BackupTableCheckBoxItem item: checkBoxItems)
            item.setSelected(false);
    }

    /**
     * checkAll
     */
    public void checkAllItems(){

        for(BackupTableCheckBoxItem item: checkBoxItems)
            item.setSelected(true);
    }

    public void deleteSelectedItems(){

        boolean backupSuccess = true;
        
        for(BackupTableCheckBoxItem item: checkBoxItems){
            if(item.isSelected()){
                if(!item.getText().equals(CHECK_ALL_ITEM_TEXT)){
                    if(!dao.deleteRecord(item.getRecord())){
                        backupSuccess = false;
                    }
                }
            }
        }
        
        String msg = (backupSuccess)? "Deletion Complete!":"Deletion Failed!";
        JOptionPane.showMessageDialog(this, msg);
        LoggingAspect.afterReturn(msg);
    }

    public void reloadCheckList() {
        checkBoxItems.clear();
        checkBoxItems.add(new BackupTableCheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(getCheckBoxItemsFromDB());

        // if checkBoxItems only contains one item (check all) then remove it
        if (checkBoxItems.size() == 1) {
            checkBoxItems.clear();
            displayNoBackupsCheckListMsg();
        }
        else{
            // add CheckBoxItems to CheckBoxList
            checkBoxList.setListData(checkBoxItems.toArray());
        }
    }

        /**
     * Creates a backup table with the same table name and today's
     * date appended to the end.
     * @return boolean true if backup successful and false if error exception
     */
    public void backupDBTablesWithDate() {

        boolean backupSuccess = true;
        String overwrite = "undefined";
        
        for (String tableName : tableNames) {
            String backupTableName = tableName + getTodaysDate();
            BackupDBTableRecord record = new BackupDBTableRecord();
            record.setTableName(tableName);
            record.setBackupTableName(backupTableName);
            for (BackupTableCheckBoxItem item : checkBoxItems) {
                if (item.getText().equals(backupTableName)) {
                    if(overwrite.equals("undefined")){
                        String msg = "Tables already exist. Overwrite?";
                        int selection = JOptionPane.showConfirmDialog(this, msg);
                        if(selection == 0){
                            overwrite = "true"; // will not ask anymore
                        }
                        else{
                            overwrite = "false"; // flag to break for loop
                            break;
                        }
                    }
                    if(!dao.deleteRecord(backupTableName)){
                        backupSuccess = false;
                    }
                }
            }
            if(overwrite.equals("false")){
                break;
            }
            if(!dao.addRecord(record)){
                backupSuccess = false;
            }
        }
        
        if(!overwrite.equals("false")){
            String msg = (backupSuccess)? "Backup Complete!":"Backup Failed!";
            JOptionPane.showMessageDialog(this, msg);
            LoggingAspect.afterReturn(msg);
        }
    }

    /**
     * Gets todays date
     * @return today's date (ex. _2015_12_21)
     * Returns today's date in a format to append to a table name for backup.
     */
    public String getTodaysDate(){

        // get today's date
        Calendar calendar = Calendar.getInstance();
        int year =  calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return "_" + year + "_" + month + "_" + day;
    }

    /**
     * checks if a checkbox is checked
     * @return boolean if a checkbox is checked (true) or not (false)
     */
    public boolean isACheckBoxChecked() {

        // check if a checkbox is checked
        for (BackupTableCheckBoxItem item : checkBoxItems) {
            if (item.isSelected())
                return true;
        }
        return false;
    }

    private void displayNoBackupsCheckListMsg() {
        // add some noBackupsMsg
        String noBackupsMsg = "no known prior backups";
        JLabel labelCheckListMsg = new JLabel(noBackupsMsg);
        labelCheckListMsg.setHorizontalAlignment(JLabel.CENTER);
        labelCheckListMsg.setHorizontalTextPosition(JLabel.CENTER);
        checkBoxList.setListData(new JLabel[]{labelCheckListMsg});
    }

    public ArrayList<BackupTableCheckBoxItem> getCheckBoxItemsFromDB() {

        ArrayList<BackupTableCheckBoxItem> items = new ArrayList<>();
        ArrayList<BackupDBTableRecord> records = dao.getRecords();

        for(BackupDBTableRecord record: records){
            items.add(new BackupTableCheckBoxItem(record));
        }

        return items;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelOutput = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelButtons = new javax.swing.JPanel();
        btnBackup = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        javax.swing.GroupLayout panelOutputLayout = new javax.swing.GroupLayout(panelOutput);
        panelOutput.setLayout(panelOutputLayout);
        panelOutputLayout.setHorizontalGroup(
            panelOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelOutputLayout.setVerticalGroup(
            panelOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
        );

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Existing Backup Database tables");
        jLabel1.setRequestFocusEnabled(false);

        btnBackup.setText("Backup");
        btnBackup.setToolTipText("Create a table backup (name defaults with data appended)");
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.setToolTipText("Delete selectd table backups");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelButtonsLayout = new javax.swing.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelButtonsLayout.createSequentialGroup()
                .addGap(218, 218, 218)
                .addComponent(btnDelete)
                .addGap(20, 20, 20)
                .addComponent(btnBackup)
                .addGap(223, 223, 223))
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnBackup))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(panelOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelOutput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackupActionPerformed
        // defaults to backup with the date appended
        backupDBTablesWithDate();
        reloadCheckList();
    }//GEN-LAST:event_btnBackupActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        deleteSelectedItems();
        reloadCheckList();
        btnDelete.setEnabled(false);
        btnBackup.setEnabled(true);
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnDelete;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelOutput;
    // End of variables declaration//GEN-END:variables

}
