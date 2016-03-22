
package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import static com.elle.ProjectManager.presentation.ProjectManagerWindow.informationLabel;
import static com.elle.ProjectManager.presentation.ProjectManagerWindow.searchInformationLabel;
import com.elle.ProjectManager.presentation.LogWindow;

/**
 * LoggingAspect
 * @author Carlos Igreja
 * @since  Feb 23, 2016
 */
public class LoggingAspect {
    
    public static void addLogMsg(String msg){
        if(LogWindow.fileName != null)
            LogWindow.addMessage(msg);
    }
    
    public static void addLogMsgWthDate(String msg){
        if(LogWindow.fileName != null)
            LogWindow.addMessageWithDate(msg);
    }
    
    public static void timerCntDwn(int time){
        ProjectManagerWindow.startCountDownFromNow(time);
    }
    
    public static void afterReturn(String msg){
        
        // display message to user
        System.out.println(msg);
        if (informationLabel != null) {
            informationLabel.setText(msg);
            timerCntDwn(10);
        }

        // add message to log
        if(LogWindow.fileName != null)
            addLogMsgWthDate(msg);
    }
    
    public static void afterThrown(Exception e){
        
        //NOTES
        // seTinformationLabel method
        // analyster.setInformationLabel("Upload failed!", 10);
        // I can probably get this specific message and handle user output
        // handle specific exception in specific ways
        // getmessage or exception type or name 
        
        // display message to user
        e.printStackTrace();
        if(informationLabel != null){
            informationLabel.setText("An error occurred. Please see log file.");
            timerCntDwn(10);
        }
        
        // add error message to log
        if(LogWindow.fileName != null)
            addLogMsgWthDate("An exception was thrown: ");
        
        // log exception 
        if(LogWindow.fileName != null)
            addLogMsg("Error message: " + e.getMessage());
        
        // get first element that package starts with com.elle.
        if(LogWindow.fileName != null){
            StackTraceElement[] elements = e.getStackTrace();
            for(StackTraceElement element:elements){
                if(element.getClassName().startsWith("com.elle.")){
                    addLogMsg("Package.Class: " + element.getClassName());
                    addLogMsg("Method: " + element.getMethodName());
                    addLogMsg("Line: " + element.getLineNumber());
                    break;
                }
            }
        }
    }
}
