package com.elle.ProjectManager.database;

import java.awt.Component;
import java.sql.Connection;
import javax.swing.JOptionPane;
import com.elle.ProjectManager.logic.CheckBoxList;
import com.elle.ProjectManager.logic.CheckBoxItem;
import com.elle.ProjectManager.presentation.PopupWindow;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

/**
 * This class is used to track backups made for database tables.
 * PSUEDO CODE
 * - Access database table and retrieve and store backup table data.
 * 1) Associates backup files with tables backed up.
 * 2) Retrieves an array of the backup tables associated with that table.
 * 3) In order to write and retrieve this data, it would have to be stored
 *    in the database.
 *   3a) A database table would be required.
 *   3b) A connection would be required for this class to access the database.
 * 4) Method to create a new table if one does not exist.
 *    4a) prompt user permission to do so.
 *    4b) an error may occur if cannot create database; handle this.
 *
 * - Backup DB Tables GUI
 * 1) Display backups as checkbox items for deletion.
 *
 *
 *
 * @author Carlos Igreja
 * @since  2015 December 28
 */
public class BackupDBTables{
    
    // backup database table information
    // this information should match the database table
    private final String BACKUP_DB_TABLE_NAME = "Table_Backups";
    private final String BACKUP_DB_TABLE_COLUMN_PK = "id";
    private final String BACKUP_DB_TABLE_COLUMN_1 = "tableName";
    private final String BACKUP_DB_TABLE_COLUMN_2 = "backupTableName";
    private final String CHECK_ALL_ITEM_TEXT = "(All)";
    
    // private variables
    private String tableName;
    private String backupTableName;
    private Connection connection;
    private Statement statement;
    private Component parentComponent; // used to display message relative to parent component
    private CheckBoxList checkBoxList;
    private PopupWindow popupWindow;
    private ArrayList<CheckBoxItem> checkBoxItems;
    private JButton btnBackup;
    private JButton btnDelete;
    private SQL_Commands sql_commands;

    public BackupDBTables(Connection connection, String tableName) {
        this.tableName = tableName;
        this.backupTableName = null;
        this.parentComponent = null;
        this.connection = connection;
        this.sql_commands = new SQL_Commands(connection);
        try {
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        initComponents();
    }

    public BackupDBTables(Connection connection, String tableName, Component parentComponent) {
        this.tableName = tableName;
        this.backupTableName = null;
        this.parentComponent = parentComponent;
        this.connection = connection;
        this.sql_commands = new SQL_Commands(connection);
        try {
            this.statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        initComponents();
    }

    public BackupDBTables(Statement statement, String tableName) {
        this.tableName = tableName;
        this.backupTableName = null;
        this.parentComponent = null;
        try {
            this.connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        this.statement = statement;
        this.sql_commands = new SQL_Commands(connection);
        initComponents();
    }

    public BackupDBTables(Statement statement, String tableName, Component parentComponent) {
        this.tableName = tableName;
        this.backupTableName = null;
        this.parentComponent = parentComponent;
        try {
            this.connection = statement.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        this.statement = statement;
        this.sql_commands = new SQL_Commands(connection);
        initComponents();
    }

    public BackupDBTables(String host, String database, String username, String password, String tableName) {
        this.tableName = tableName;
        this.backupTableName = null;
        this.parentComponent = null;
        this.connection = createConnection(host, database, username, password);
        if(connection != null){
            this.statement = createStatement(connection);
        }
        this.sql_commands = new SQL_Commands(connection);
        initComponents();
    }

    public BackupDBTables(String host, String database, String username, String password, String tableName, Component parentComponent) {
        this.tableName = tableName;
        this.backupTableName = null;
        this.parentComponent = parentComponent;
        this.connection = createConnection(host, database, username, password);
        if(connection != null){
            this.statement = createStatement(connection);
        }
        this.sql_commands = new SQL_Commands(connection);
        initComponents();
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
                  else{
                      // toogle the check for the checkbox item
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
    
    /**
     * removeAllChecks
     */
    public void removeAllChecks(){

        for(CheckBoxItem item: checkBoxItems)
            item.setSelected(false);
    }
    
    /**
     * checkAll
     */
    public void checkAllItems(){
        
        for(CheckBoxItem item: checkBoxItems)
            item.setSelected(true);
    }
    
    private void initComponents(){
        
        String title = ""; // small window so empty
        String message = "Existing Backup Database tables";
        
        setCheckBoxListListener();
        
        // checkbox item array
        checkBoxItems = new ArrayList<>();
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(getCheckBoxItemsFromDB());
        
        // if checkBoxItems only contains one item (check all) then remove it
        if(checkBoxItems.size() == 1)
            checkBoxItems.clear();
        
        // add CheckBoxItems to CheckBoxList
        checkBoxList.setListData(checkBoxItems.toArray());
        
        // add CheckBoxList to a scrollpane
        ScrollPane scroll = new ScrollPane();
        scroll.add(checkBoxList);
        
        // buttons
        // create Delete button
        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSelectedItems();
                reloadCheckList();
                // if no checkbox items are left
                if(checkBoxItems.isEmpty()){
                    btnDelete.setEnabled(false);
                    btnBackup.setEnabled(true);
                }
            }
        });
        
        // start the delete button as not enabled
        btnDelete.setEnabled(false);
        
        // create Backup button
        btnBackup = new JButton("Backup");
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // defaults to backup with the date appended
                backupDBTableWithDate(getTableName());
                reloadCheckList();
            }
        });
        
        // add buttons to the buttons array
        JButton[] buttons = new JButton[]{btnDelete, btnBackup};
        
        // dimension
        Dimension dimension = new Dimension(0,200);
        
        // Create a popup window 
        PopupWindow popup = new PopupWindow(title, message, scroll, buttons, dimension);
        popup.setLocationRelativeTo(parentComponent);
        popup.setVisible(true);

    }
    
    
    public void createDBTableToStoreBackupsInfo(){
        
        String createTableQuery = 
                "CREATE TABLE " + BACKUP_DB_TABLE_NAME +
                "(" +
                BACKUP_DB_TABLE_COLUMN_PK + " int(4) PRIMARY KEY AUTO_INCREMENT, " +
                BACKUP_DB_TABLE_COLUMN_1 + " VARCHAR(50) NOT NULL, " +
                BACKUP_DB_TABLE_COLUMN_2 + " VARCHAR(50) NOT NULL " +
                ");";
        
        // TODO - execute sql query
        if(!sql_commands.updateQuery(createTableQuery))
            JOptionPane.showMessageDialog(parentComponent, "unable to create table " + BACKUP_DB_TABLE_NAME );
    }

    public ArrayList<CheckBoxItem> getCheckBoxItemsFromDB() {
        
        // check box items array list to return
        ArrayList<CheckBoxItem> items = new ArrayList<>();
        
        // sql query to return a result set
        String sql = 
                "SELECT " + BACKUP_DB_TABLE_COLUMN_PK + "," + BACKUP_DB_TABLE_COLUMN_2 +
               " FROM " + BACKUP_DB_TABLE_NAME +
               " WHERE " + BACKUP_DB_TABLE_COLUMN_1 + " = '" + getTableName() + "' ;";
        
        ResultSet result = null;
        
        try {
            //Here we create our query
            PreparedStatement statement = getConnection().prepareStatement(sql);

            //Creating a variable to execute query
            result = statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            // if table doesn't exist and needs to be created
            if(ex.getMessage().endsWith("exist")){
                System.out.println("ENTERED !!!!!!!!!!!!!!!!!!!!!");
                createDBTableToStoreBackupsInfo();
                result = sql_commands.executeQuery(sql);
            }else{
                handleSQLexWithMessageBox(ex); // all other error messages
            } 
        } finally{
            // create checkbox items from result set and load up array list
            if(result != null){
                try {
                    while(result.next())
                    {
                        // get column data
                        int id = Integer.parseInt(result.getString(1));
                        String backupName = result.getString(2);
                        
                        // create checkBoxItem
                        CheckBoxItem item = new CheckBoxItem(backupName);
                        
                        // set checkbox item id (same id as primary key on db table)
                        item.setId(id);
                        
                        // add checkbox item to the array list
                        items.add(item);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                    handleSQLexWithMessageBox(ex); // any errors 
                }
            }
        }
        
        return items;
    }
    
    public void deleteSelectedItems(){
        
        for(CheckBoxItem item: checkBoxItems){
            if(item.isSelected()){
                deleteItem(item.getId());
                deleteItem(item.getCapped());
            }
        }
    }
    
    /**
     * Deletes record of the backup table (not the actual backup table)
     * @param id id of record in database table
     * @return boolean true if successful and false if sql error occurred 
     */
    public boolean deleteItem(int id) {
        
        if(id == -1)
            return false;
        
        String sql = 
                "DELETE FROM " + BACKUP_DB_TABLE_NAME +
               " WHERE " + BACKUP_DB_TABLE_COLUMN_PK + " = " + id + ";";
        
        try {
            getStatement().executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * Deletes the actual backup table (not the record of the backup table)
     * @param tableName tableName to be dropped from the database
     * @return boolean true if successful and false if sql error occurred 
     */
    public boolean deleteItem(String tableName) {
        
        if(tableName == CHECK_ALL_ITEM_TEXT)
            return false;
        
        try {
            dropTable(tableName);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }

    public void reloadCheckList() {
        checkBoxItems.clear();
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
        checkBoxItems.addAll(getCheckBoxItemsFromDB());
        
        // if checkBoxItems only contains one item (check all) then remove it
        if(checkBoxItems.size() == 1)
            checkBoxItems.clear();
        
        // add CheckBoxItems to CheckBoxList
        checkBoxList.setListData(checkBoxItems.toArray());
    }
    
    /**
     * creates a database connection
     * @param host        the website host or localhost ( ex. website.com or localhost)
     * @param database    database to connect to
     * @param username    user name to connect to the database
     * @param password    user password to connect to the database
     * @return Connection connection to the database
     */
    public Connection createConnection(String host, String database, String username, String password){
        
        try {
            //Accessing driver from the JAR file
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
        String server = "jdbc:mysql://" + host +":3306/" + database;
        Connection connection = null;
        
        try {
            // get connection
            connection = DriverManager.getConnection(server, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        
        return connection;
    }
    
    /**
     * creates a Statement object from a Connection object
     * @param connection  connection object to create a statement object
     * @return statement  statement object created from connection object
     */
    public Statement createStatement(Connection connection){
        
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        return statement;
    }
    
    /**
     * Creates a backup table with the same table name and today's 
     * date appended to the end. 
     * @param tableName table name in the database to backup
     * @return boolean true if backup successful and false if error exception
     */
    public boolean backupDBTableWithDate(String tableName) {
        
        this.tableName = tableName; // needs to be set for backup complete message
        
        // create a new backup table name with date
        this.backupTableName = tableName + getTodaysDate();
        
        // execute sql statements
        try {
            
            createTableLike(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            addBackupRecord(tableName, backupTableName);
            displayBackupCompleteMessage();
            return true;

        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * Backs up a table in the database
     * @param tableName         the table in the database to backup up (original)
     * @param backupTableName   the name of the new table (the backup table)
     * @return                  boolean returns true if the backup was a success 
     */
    public boolean backupTable(String tableName, String backupTableName){
        
        // these need to be set for the backup complete message
        this.tableName = tableName;
        this.backupTableName = backupTableName;
        
        try {
            createTableLike(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            addBackupRecord(tableName, backupTableName);
            displayBackupCompleteMessage();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * Creates a table in the database
     * @param tableName           the original table name
     * @param backupTableName     the name of the backup table
     * @throws SQLException       can use handleSQLexWithMessageBox method in catch
     */
    public void createTableLike(String tableName, String backupTableName) throws SQLException{
        
        // sql query to create the table 
        String sqlCreateTable = "CREATE TABLE " + backupTableName
                             + " LIKE " + tableName + " ; ";
        
        // execute sql statements
        statement.executeUpdate(sqlCreateTable);
    }
    
    /**
     * Backs up table data in the database
     * @param tableName           the original table name
     * @param backupTableName     the name of the backup table
     * @throws SQLException       can use handleSQLexWithMessageBox method in catch
     */
    public void backupTableData(String tableName, String backupTableName) throws SQLException{
        
        // sql query to backup the table data
        String sqlBackupData =  "INSERT INTO " + backupTableName 
                             + " SELECT * FROM " + tableName +  " ;";
        
        // execute sql statements
        statement.executeUpdate(sqlBackupData);
    }
    
    /**
     * Drops a table in the database
     * @param tableName drop this table name from database
     * @return boolean dropped from database? true or false
     * @throws SQLException can use handleSQLexWithMessageBox method in catch
     */
    public void dropTable(String tableName) throws SQLException{
        
        // sql query to drop the table 
        String sqlCreateTable = "DROP TABLE " + tableName + " ; ";
        
        // execute sql statements
        statement.executeUpdate(sqlCreateTable);
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
     * Handles sql exceptions with a message box to notify the user
     * @param ex the sql exception that was thrown
     */
    public void handleSQLexWithMessageBox(SQLException ex){
        
        String message = ex.getMessage();
        
        // if backup database already exists
        if (message.endsWith("already exists")){
            // option dialog box
            message = "Backup database " + backupTableName + " already exists";
            String title = "Backup already exists";
            int optionType = JOptionPane.YES_NO_CANCEL_OPTION;
            int messageType = JOptionPane.QUESTION_MESSAGE;
            Object[] options = {"Overwrite", "Create a new one", "Cancel"};
            int optionSelected = JOptionPane.showOptionDialog(parentComponent, 
                                        message, 
                                        title, 
                                        optionType, 
                                        messageType, 
                                        null, 
                                        options, 
                                        null);
            
            // handle option selected
            switch(optionSelected){
                case 0:
                    overwriteBackupDB();
                    reloadCheckList();
                    break;
                case 1:
                    backupTableName = getInputTableNameFromUser();
                    backupTable(tableName, backupTableName);
                    reloadCheckList();
                    break;
                default:
                    break;
            }
        }
        
        // display message to user
        else{
            
            // message dialog box 
            String title = "Error";
            int messageType = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
        }
    }
    
    /**
     * Drops a table and creates a new one if it already exists
     * Drops the backup table and creates a new backup with the table name
     * and today's date.
     */
    public void overwriteBackupDB(){
        
        try {
            dropTable(backupTableName);
            dropBackupRecord(tableName, backupTableName);
            createTableLike(tableName, backupTableName);
            backupTableData(tableName, backupTableName);
            addBackupRecord(tableName, backupTableName);
            displayBackupCompleteMessage();
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
    }
    
    /**
     * Gets input for the table name from the user using an input message box
     * @return the input the user entered into the input text box
     */
    public String getInputTableNameFromUser(){
        // input dialog box 
        String message = "Enter the name for the backup";
        return JOptionPane.showInputDialog(parentComponent, message);
    }
    
    /**
     * A message box that displays when 
     * the backup was completed successfully.
     */
    public void displayBackupCompleteMessage(){
        String message = tableName + " was backed up as " + backupTableName;
        JOptionPane.showMessageDialog(parentComponent, message);
    }

    /**
     * This can be used to check that the connection is open and not null
     * @return database connection
     */
    public Connection getConnection() {
        return connection;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBackupTableName() {
        return backupTableName;
    }

    public void setBackupTableName(String backupTableName) {
        this.backupTableName = backupTableName;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Component getParentComponent() {
        return parentComponent;
    }

    public void setParentComponent(Component parentComponent) {
        this.parentComponent = parentComponent;
    }
    
    public boolean addBackupRecord(){
        return addBackupRecord(getTableName(), getBackupTableName());
    }
    
    public boolean addBackupRecord(String tableName, String backupTableName){
        String sql = 
                "INSERT INTO " + BACKUP_DB_TABLE_NAME + 
               " ( " + BACKUP_DB_TABLE_COLUMN_1 + ", " + BACKUP_DB_TABLE_COLUMN_2 + ")" 
                + " VALUES ('" + tableName + "', '" +  backupTableName + "');";
        try {
            getStatement().executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }

    public boolean dropBackupRecord(String tableName, String backupTableName) {
        String sql = 
                "DELETE FROM " + BACKUP_DB_TABLE_NAME + 
               " WHERE " + BACKUP_DB_TABLE_COLUMN_1 + " = '" + tableName +
               "' AND " + BACKUP_DB_TABLE_COLUMN_2 + " = '" + backupTableName + "' ;";
        try {
            getStatement().executeUpdate(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(BackupDBTables.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * checks if a checkbox is checked
     * @return boolean if a checkbox is checked (true) or not (false)
     */
    public boolean isACheckBoxChecked(){
        
        // check if a checkbox is checked
        for(CheckBoxItem item: checkBoxItems){
            if(item.isSelected())
                return true;
        }
        return false;
    }
    
    public void addCheckBoxAllCheckBoxItem(){
        checkBoxItems.add(new CheckBoxItem(CHECK_ALL_ITEM_TEXT));
    }
}
