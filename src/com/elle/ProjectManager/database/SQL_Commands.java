package com.elle.ProjectManager.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Created by Carlos Igreja on 1/11/2016.
 */
public class SQL_Commands {

    // attributes
    private Connection connection;
    private String username;
    private String password;
    private String host;
    private String database;
    private String server;
    private Statement statement;

    /**
     * This constructor takes a connection
     * @param connection an open connection
     */
    public SQL_Commands(Connection connection){
        this.connection = connection;
    }
    /**
     * This constructor creates a connection with the parameters
     * @param username     // username to connect to the database
     * @param password     // password to connect to the database
     * @param host         // the host to connect to
     * @param database     // the database to connect to
     */
    public SQL_Commands(String username, String password, String host, String database) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.database = database;
        createConnection(host, database, username, password);
    }
    
    /**
     * Handles sql exceptions with a message box to notify the user
     * @param ex the sql exception that was thrown
     */
    public void handleSQLexWithMessageBox(SQLException ex){
        System.out.println(ex.getMessage());
        
//        String message = ex.getMessage();
//        
//        // if backup database already exists
//        if (message.endsWith("already exists")){
//            // option dialog box
//            message = "Backup database " + backupTableName + " already exists";
//            String title = "Backup already exists";
//            int optionType = JOptionPane.YES_NO_CANCEL_OPTION;
//            int messageType = JOptionPane.QUESTION_MESSAGE;
//            Object[] options = {"Overwrite", "Create a new one", "Cancel"};
//            int optionSelected = JOptionPane.showOptionDialog(parentComponent, 
//                                        message, 
//                                        title, 
//                                        optionType, 
//                                        messageType, 
//                                        null, 
//                                        options, 
//                                        null);
//            
//            // handle option selected
//            switch(optionSelected){
//                case 0:
//                    overwriteBackupDB();
//                    break;
//                case 1:
//                    backupTableName = getInputTableNameFromUser();
//                    backupTable(tableName, backupTableName);
//                    break;
//                default:
//                    break;
//            }
//        }
//        
//        // display message to user
//        else{
//            
//            // message dialog box 
//            String title = "Error";
//            int messageType = JOptionPane.ERROR_MESSAGE;
//            JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
//        }
    }
    
    /**
     * creates a database connection
     * @param host        the website host or localhost ( ex. website.com or localhost)
     * @param database    database to connect to
     * @param username    user name to connect to the database
     * @param password    user password to connect to the database
     * @return Connection connection to the database
     */
    public boolean createConnection(String host, String database, String username, String password){
        
        server = "jdbc:mysql://" + host +":3306/" + database;
        connection = null;
        
        try {
            // Accessing driver
            Class.forName("com.mysql.jdbc.Driver");
            // set connection
            connection = DriverManager.getConnection(server, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SQL_Commands.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * creates a Statement object from a Connection object
     * @param connection  connection object to create a statement object
     * @return statement  statement object created from connection object
     */
    public boolean createStatement(Connection connection){
        
        statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(SQL_Commands.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
        return true;
    }
    
    /**
     * Creates a table in the database
     * @param tableName           the original table name
     * @param backupTableName     the name of the backup table
     * @throws SQLException       can use handleSQLexWithMessageBox method in catch
     */
    public boolean createTableLike(String tableName, String likeTableName){
        
        // sql query to create the table 
        String sqlCreateTable = "CREATE TABLE " + tableName
                             + " LIKE " + likeTableName + " ; ";
        
        try {
            // execute sql statements
            statement.executeUpdate(sqlCreateTable);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SQL_Commands.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * Backs up table data in the database
     * @param tableName           the original table name
     * @param backupTableName     the name of the backup table
     * @throws SQLException       can use handleSQLexWithMessageBox method in catch
     */
    public boolean copyTableData(String fromTableName, String toTableName){
        
        // sql query to backup the table data
        String sqlBackupData =  "INSERT INTO " + toTableName 
                             + " SELECT * FROM " + fromTableName +  " ;";
        
        try {
            // execute sql statements
            statement.executeUpdate(sqlBackupData);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SQL_Commands.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * Drops a table in the database
     * @param tableName drop this table name from database
     * @return boolean dropped from database? true or false
     * @throws SQLException can use handleSQLexWithMessageBox method in catch
     */
    public boolean dropTable(String tableNameToDrop){
        
        // sql query to drop the table 
        String sqlCreateTable = "DROP TABLE " + tableNameToDrop + " ; ";
        
        try {
            // execute sql statements
            statement.executeUpdate(sqlCreateTable);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SQL_Commands.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
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
     * This can be used to check that the connection is open and not null
     * @return database connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * This closes the connection and statement objects
     * @return boolean true if closed and false if not closed
     */
    public boolean closeConnection(){
        try {
            connection.close();
            statement.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SQL_Commands.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return false;
        }
    }
    
    /**
     * This opens the connection
     * @return boolean true if opened or false if not opened
     */
    public boolean openConnection(){
        return createConnection(host, database, username, password);
    }
    
    /**
     * This returns all columns in the table
     * @param tableName
     * @return 
     */
    public ArrayList<HashMap<String,String>> getTableData(String tableName){
        
        // get the result set from the database
        String query = "SELECT * FROM " + tableName + ";";
        
        return getTableData(executeQuery(query));
    }
    
    /**
     * This returns specified columns in table
     * @param tableName
     * @param columnNames
     * @return 
     */
    public ArrayList<HashMap<String,String>> getTableData(String tableName, String[] columnNames){

        // create the sql query
        String query = "SELECT ";
        for(int i = 0; i < columnNames.length; i++){
            query += columnNames[i];
            if(i != columnNames.length -1){
                query += ", ";
            }
        }         
        query += " FROM " + tableName + ";";
        
        return getTableData(executeQuery(query));
    }
    
    /**
     * This returns the result set in an ArrayList
     * @param resultSet
     * @param numColumns
     * @return 
     */
    public ArrayList<HashMap<String,String>> getTableData(ResultSet resultSet){

        // declare and initialize array list
        ArrayList<HashMap<String,String>> a = new ArrayList<>();

        // fill the array list with the data from the result set and return
        try {
            // get the result set meta data
            ResultSetMetaData meta = resultSet.getMetaData();
            
            String columnName;       // to store column name
            String cellData;         // to store the cell data
            HashMap<String,String> map = new HashMap<>();
            
            while(resultSet.next()){
                for(int i = 0; i < meta.getColumnCount(); i++){
                    columnName = meta.getColumnName(i);
                    cellData = resultSet.getString(i);
                    map.put(columnName, cellData);      // add field data
                }
                a.add(map);         // add record to the array list
                map.clear();        // clear HashMap data
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQL_Commands.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
        }
        
        return a;
    }
    
    public ResultSet executeQuery(String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(SQL_Commands.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
            return null;
        }
    }

}
