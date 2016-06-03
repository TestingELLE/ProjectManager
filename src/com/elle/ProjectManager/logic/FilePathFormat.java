package com.elle.ProjectManager.logic;

import java.io.File;

/**
 * Converts a file path format to either windows or non-windows format.
 * Windows uses backslash for file paths.
 * Non-Windows uses forward slash for file paths.
 * This class can be used to convert file path formats.
 * @author Carlos Igreja
 * @since  1-20-2016
 */
public class FilePathFormat {
    
    private static final String OS_WINDOWS = "win";
    
    /**
     * Converts a file path format to either windows or non-windows format.
     * @param path path string to convert
     * @param toWindows true converts to windows path and false to non-windows
     * @return path converted to specified platform's file path format
     */
    public static String convert(String path, Boolean toWindows){
        
        // null exception handling
        if(path == null){
            return "";
        }
        
        final String F_SLASH = "/";   // forward slash for non- windows path
        final String B_SLASH = "\\";  // backslash for windows path
        System.out.println("1: " + path);

        // get the path directories
        String[] dirs; 
        if(path.contains(F_SLASH))
            dirs = path.split(F_SLASH);
        else if(path.contains(B_SLASH))
            dirs = path.split(B_SLASH + B_SLASH); // regex = \\\\ -> \\
        else
            dirs = new String[]{path};
        
        // get path slash
        String slash = (toWindows)? B_SLASH : F_SLASH;
        
        // get the new path
        path = ""; 
        for (String dir : dirs){
            path += dir + slash;
        }
        System.out.println("2: " + path);

        return path;
    }
    
    /**
     * This method will return the path of where the we put our support file is
     * @supportFilePath string
     */
    public static String supportFilePath(){
        String supportFilePath = "";

        if (isWindows()) {
            supportFilePath = "C:\\Users\\" + System.getProperty("user.name") + 
                    "\\Documents\\ProjectManager\\";
        } else {
            supportFilePath = "/Users/" + System.getProperty("user.name") + 
                    "/Library/Application Support/ProjectManager/";
        }
        File dir = new File(supportFilePath);
        dir.mkdir();
        return supportFilePath;
    }
    
    /**
     * This method will return the path of the offline issues.
     * @supportFilePath string
     */
    public static File localDataFilePath(){
        String supportFilePath = supportFilePath();
        
        File dir = new File(supportFilePath, "offlineTempIssues");
        if (!dir.exists())
            dir.mkdir();
        return dir;
    }
    
    /**
     * This method will return if the platform is windows or not.
     * @return boolean true if windows and false if not
     */
    public static boolean isWindows(){
        // All os have the os.name property value.
        // Not all os have the sun.desktop property value
        // and it may return null for some os.
        // Hence os.name is used and checks for "windows" with the startsWith().
        String osName = System.getProperty("os.name").toLowerCase().trim();
        return osName.startsWith(OS_WINDOWS);
    }
}
