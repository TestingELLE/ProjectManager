
package com.elle.ProjectManager.database;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                    url += server.getUrl();
            }
            
            url += selectedDB;
            
            // connect to server
            connection = DriverManager.getConnection(url, userName, userPassword);
            statement = connection.createStatement();
            System.out.println("Connection successfully");
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
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
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            handleSQLexWithMessageBox(ex);
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
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            handleSQLexWithMessageBox(ex);
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
     * read servers data from xml file
     * @return 
     */
    public static ArrayList<Server> readServers()
    {
        ArrayList<Server> servers = new ArrayList<>();
        Server server = null;

        // create an XMLInputFactory object
        XMLInputFactory inputFactory = XMLInputFactory.newFactory();
        try{
            InputStream inputStream = DBConnection.class.getResourceAsStream(SERVERS_FILENAME);
            InputStreamReader fileReader = new InputStreamReader(inputStream);
            XMLStreamReader reader = inputFactory.createXMLStreamReader(fileReader);
            
            //Read XML here
            while(reader.hasNext()){
                int eventType = reader.getEventType();
                switch(eventType){
                    case XMLStreamConstants.START_ELEMENT:
                        String elementName = reader.getLocalName();
                        if(elementName.equals("server")){
                            server = new Server();
                        }
                        else if(elementName.equals("name")){
                            String name = reader.getElementText();
                            server.setName(name);
                        }
                        else if(elementName.equals("url")){
                            String url = reader.getElementText();
                            server.setUrl(url);
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        elementName = reader.getLocalName();
                        if(elementName.equals("server")){
                            servers.add(server);
                        }
                        break;
                    default:
                        break;
                }
                reader.next();
            }
        }catch(XMLStreamException e){
            System.out.println(e);
        }
        return servers;
    }

    /**
     * writeServers
     * write server data to xml file
     * @param servers 
     */
    public static void writeServers(ArrayList<Server> servers)
    {
        // create the XMLOutputFactory object
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        try{
            //create XMLStreamWriter object
            FileWriter fileWriter = new FileWriter(SERVERS_FILENAME);
            XMLStreamWriter writer = outputFactory.createXMLStreamWriter(fileWriter);
            
            // write the servers to the file
            writer.writeStartDocument("1.0");
            writer.writeStartElement("servers");
            for (Server server : servers){
                writer.writeStartElement("server");
                writer.writeStartElement("name");
                writer.writeCharacters(server.getName());
                writer.writeEndElement();
                writer.writeStartElement("url");
                writer.writeCharacters(server.getUrl());
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.flush();
            writer.close();
        }catch(IOException | XMLStreamException e){
            System.out.println(e);
        }
    }

    private static void handleSQLexWithMessageBox(SQLException ex) {
        
        String message = ex.getMessage();
        
        // message dialog box 
        String title = "Error";
        int messageType = JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog( null, message, title, messageType);
    }
}
