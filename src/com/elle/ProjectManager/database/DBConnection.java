
package com.elle.ProjectManager.database;

import com.elle.ProjectManager.logic.FilePathFormat;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * DBConnection
 * This class makes the connection with the server and database 
 * and then the statement object is static and usable throughout
 * the application. 
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class DBConnection {
    
    private static String server;
    private static String database;
    private static String userName;
    private static String userPassword;
    private static Connection connection;
    private static Statement statement;
    private static final String SERVERS_FILENAME = "servers.xml";
    private static Component parentComponent;
    
    /**
     * connect
     * creates a connection to the server and database and also
     * creates a statement for executing sql statements.
     * @param selectedServer
     * @param selectedDB
     * @param userName
     * @param userPassword
     * @return boolean true if successful and false if an error occurred
     */
    public static boolean connect(String selectedServer, String selectedDB, String userName, String userPassword){
        
        try {
            DBConnection.server = selectedServer;
            DBConnection.database = selectedDB;
            DBConnection.userName = userName;
            DBConnection.userPassword = userPassword;
            
            String url = "";
            ArrayList<Server> servers = readServers();
            
            // load url for server
            for(Server server: servers){
                if(server.getName().equals(selectedServer))
                    url = server.getUrl();
            }
            
            url += selectedDB;
            
            // connect to server
            connection = DriverManager.getConnection(url, userName, userPassword);
            statement = connection.createStatement();
            LoggingAspect.addLogMsgWthDate("Connection successful");
            return true;
        } catch (Exception ex) {
            //LoggingAspect.afterThrown(ex);
            System.out.println(ex.toString());
            return false;
        } 
             
    }
    
    /**
     * open
     * opens the connection to the server and database.
     * This is used to reopen connections and prevent timeouts
     * from servers.
     * @return boolean true if successful and false if an error occurred
     */
    public static boolean open(){
        return connect(server, database, userName, userPassword);
    }
    
    /**
     * close
     * closes the connection to the server and database.
     * This is used to close the connection when the transaction
     * is finished.
     * @return boolean true if successful and false if an error occurred
     */
    public static boolean close(){
        try {
            statement.close();
            connection.close();
            LoggingAspect.addLogMsgWthDate("Connection closed successful");
            return true;
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            return false;
        }
    }
    
    public static boolean isClosed(){
        try {
            
            if(connection.isClosed()){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
            return true;
        }
    }

    /**
     * getServer
     * @return 
     */
    public static String getServer() {
        return server;
    }

    /**
     * setServer
     * @param server 
     */
    public static void setServer(String server) {
        DBConnection.server = server;
    }

    /**
     * getDatabase
     * @return 
     */
    public static String getDatabase() {
        return database;
    }

    /**
     * setDatabase
     * @param database 
     */
    public static void setDatabase(String database) {
        DBConnection.database = database;
    }

    /**
     * getUsersName
     * @return 
     */
    public static String getUserName() {
        return userName;
    }

    /**
     * setUsersName
     * @param userName 
     */
    public static void setUserName(String userName) {
        DBConnection.userName = userName;
    }

    /**
     * getUsersPassword
     * @return 
     */
    public static String getUserPassword() {
        return userPassword;
    }

    /**
     * setUsersPassword
     * @param userPassword 
     */
    public static void setUserPassword(String userPassword) {
        DBConnection.userPassword = userPassword;
    }

    /**
     * getConnection
     * @return 
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * setConnection
     * @param connection 
     */
    public static void setConnection(Connection connection) {
        DBConnection.connection = connection;
    }

    /**
     * getStatement
     * @return 
     */
    public static Statement getStatement() {
        return statement;
    }

    /**
     * setStatement
     * @param statement 
     */
    public static void setStatement(Statement statement) {
        DBConnection.statement = statement;
    }
    
    /**
     * readServers
     * read servers data from xml path
     * @return 
     */
    public static ArrayList<Server> readServers()
    {
        ArrayList<Server> servers = new ArrayList<>();
        Server server = null;

        // create an XMLInputFactory object
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        InputStream inputStream = null;
        InputStreamReader inStrReader = null;
        XMLStreamReader xmlStrReader = null;
        FileReader fileReader = null;
        File file = null;
        String path = null;
        try{
            path = FilePathFormat.supportFilePath() + SERVERS_FILENAME;
            file = new File(path);
            if(file.exists()){
                fileReader = new FileReader(path);
                xmlStrReader = inputFactory.createXMLStreamReader(fileReader);
            }
            else{
                inputStream = DBConnection.class.getResourceAsStream(SERVERS_FILENAME);
                inStrReader = new InputStreamReader(inputStream);
                xmlStrReader = inputFactory.createXMLStreamReader(inStrReader);
            }
            
            // Database variables
            String dbName = "";
            String dbUsername = "";
            String dbPassword = "";
            boolean dbDefault = false;
            
            // check for outdated path
            boolean readDB = false; // check that db was read then db-username
            boolean readDBUsername = false; // if username not readDBUsername then outdated
            boolean deleteFile = false; // delete file if corrupted
            
            //Read XML here
            while(xmlStrReader.hasNext()){
                int eventType = xmlStrReader.getEventType();
                switch(eventType){
                    case XMLStreamConstants.START_ELEMENT:
                        String elementName = xmlStrReader.getLocalName();
                        if(elementName.equals("server")){
                            server = new Server();
                        }
                        else if(elementName.equals("server-name")){
                            String name = xmlStrReader.getElementText();
                            server.setName(name);
                        }
                        else if(elementName.equals("server-url")){
                            String url = xmlStrReader.getElementText();
                            server.setUrl(url);
                        }
                        else if(elementName.equals("server-default")){
                            boolean serverDefault = (xmlStrReader.getElementText().equals("true"))?true:false;
                            server.setDefaultSelection(serverDefault);
                        }
                        else if(elementName.equals("database")){
                            readDB = true;
                        }
                        else if(elementName.equals("db-name")){
                            dbName = xmlStrReader.getElementText();
                        }
                        else if(elementName.equals("db-username")){
                            dbUsername = xmlStrReader.getElementText();
                            // this was newly added element.
                            // if this is checked then the path is updated.
                            // Otherwise the older path should be deleted
                            // and the jar path should be readDBUsername.
                            readDBUsername = true; 
                        }
                        else if(elementName.equals("db-password")){
                            dbPassword = xmlStrReader.getElementText();
                        }
                        else if(elementName.equals("db-default")){
                            dbDefault = (xmlStrReader.getElementText().equals("true"))?true:false;
                            server.getDatabases().add(new Database(dbName,dbDefault,dbUsername,dbPassword));
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        elementName = xmlStrReader.getLocalName();
                        if(elementName.equals("server")){
                            if(readDB){
                                if(readDBUsername){
                                    // reset boolean checks
                                    readDB = false;
                                    readDBUsername = false;
                                }
                                else{
                                    deleteFile = true;
                                    break;
                                }
                            }
                            servers.add(server);
                        }
                        break;
                    default:
                        break;
                }
                if(deleteFile){
                    break;
                }
                else{
                    xmlStrReader.next();
                }
            }
            if(deleteFile){
                try {
                    fileReader.close();
                    xmlStrReader.close();
                    Files.delete(file.toPath());
                    String msg = "servers.xml file corrupted and was deleted";
                    LoggingAspect.afterReturn(msg);
                    return readServers();
                } catch (IOException ex) {
                    String msg = "servers.xml file corrupted and was unable to delete";
                    msg += "\nError Message: " + ex.getMessage();
                    LoggingAspect.afterReturn(msg);
                }
            }
            else{
                LoggingAspect.afterReturn("read servers file successfully");
            }
        }catch(XMLStreamException e){
            LoggingAspect.afterThrown(e);
        } catch (FileNotFoundException ex) {
            LoggingAspect.afterThrown(ex);
        }
        return servers;
    }

    /**
     * writeServers
 write server data to xml path
     * @param servers 
     */
    public static void writeServers(ArrayList<Server> servers)
    {
        // create the XMLOutputFactory object
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        try{
            //create XMLStreamWriter object
            String file = FilePathFormat.supportFilePath() + SERVERS_FILENAME;
            FileWriter fileWriter = new FileWriter(file);
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(fileWriter);
            
            // write the servers to the path
            writer.writeStartDocument("1.0");
            writer.writeStartElement("servers");
            for (Server server : servers){
                writer.writeStartElement("server");
                writer.writeStartElement("server-name");
                writer.writeCharacters(server.getName());
                writer.writeEndElement();
                writer.writeStartElement("server-url");
                writer.writeCharacters(server.getUrl());
                writer.writeEndElement();
                writer.writeStartElement("server-default");
                writer.writeCharacters(Boolean.toString(server.isDefaultSelection()));
                writer.writeEndElement();
                for(Database database: server.getDatabases()){
                    writer.writeStartElement("database");
                    writer.writeStartElement("db-name");
                    writer.writeCharacters(database.getName());
                    writer.writeEndElement();
                    writer.writeStartElement("db-username");
                    writer.writeCharacters(database.getUsername());
                    writer.writeEndElement();
                    writer.writeStartElement("db-password");
                    writer.writeCharacters(database.getPassword());
                    writer.writeEndElement();
                    writer.writeStartElement("db-default");
                    writer.writeCharacters(Boolean.toString(database.isDefaultSelection()));
                    writer.writeEndElement();
                    writer.writeEndElement();
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.flush();
            writer.close();
            LoggingAspect.afterReturn("write to servers successful");
        }catch(IOException | XMLStreamException e){
            LoggingAspect.afterThrown(e);
        }
    }

    private static void handleSQLexWithMessageBox(SQLException ex) {
        
        String message = ex.getMessage();
        
        // message dialog box 
        String title = "Error";
        int messageType = JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog( null, message, title, messageType);
    }

    /**
     * Set the parent component to show message boxes relative to.
     * @param parentComponent 
     */
    public static void setParentComponent(Component parentComponent) {
        DBConnection.parentComponent = parentComponent;
    }
    
    
}
