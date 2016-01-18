
package com.elle.ProjectManager.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is to read and write to files.
 * Created by Carlos Igreja on 1/11/2016.
 * @author Carlos Igreja
 * @since 1/11/2016
 */
public class ReadWriteFiles {
    
    private File file;
    private PrintWriter writer;
    private BufferedReader reader;
    private String path;
    
    public ReadWriteFiles(){
        file = null;
        writer = null;
        reader = null;
        path = null;
    }
    
    public PrintWriter getWriter(String fileName){
        try{
            file = new File(fileName);
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)), true);
            return writer;
        } catch (IOException ex) {
            Logger.getLogger(ReadWriteFiles.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }
    
    public BufferedReader getReader(String fileName){
        try{
            file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));
            return reader;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadWriteFiles.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * createDirectoryOutsideJar
     * This method creates a directory in the same directory that the 
     * jar is located.
     */
    public boolean createDirectoryInJarFolder(String folderName){
        
        path = getPathToJarFolder();
        
        // add folder to path and make directory
        path += folderName;
        File file = new File(path);
        return file.mkdir();
    }
    
    public String getPathToJarFolder(){
        
        // this gives the path to the jar including the jar
        Class cls = ReadWriteFiles.class;
        ProtectionDomain domain = cls.getProtectionDomain();
        CodeSource source = domain.getCodeSource();
        URL url = source.getLocation();
        try {
            URI uri = url.toURI();
            path = uri.getPath();
            
            // get path without the jar
            String[] pathSplitArray = path.split("/");
            path = "";
            for(int i = 0; i < pathSplitArray.length-1;i++){
                path += pathSplitArray[i] + "/";
            }
            
            return path;
            
        } catch (URISyntaxException ex) {
            Logger.getLogger(ReadWriteFiles.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    
}
