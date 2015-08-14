
package com.elle.ProjectManager.logic;

import java.util.Comparator;
import java.util.Date;

/**
 * LogMessage class
 * this class stores log message information
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class LogMessage {

    // attributes
    private final Date date;
    private final String message;
    

    /**
     * CONSTRUCTOR
     * @param date
     * @param message 
     */
    public LogMessage(Date date, String message) {
        this.date = date;
        this.message = message;
    }

    /**
     * getDate
     * @return 
     */
    public Date getDate(){ return date;}
    
    /**
     * getMessage
     * @return 
     */
    public String getMessage(){return message;}
    
    /**
     * SortByMostRecentDateFirst
     */
    public static class SortByMostRecentDateFirst implements Comparator<LogMessage>
    {
        @Override
        public int compare(LogMessage c, LogMessage c1) {
            return c1.getDate().compareTo(c.getDate());
        }    
    }

    /**
     * SortByMostRecentDateLast
     */
    public static class SortByMostRecentDateLast implements Comparator<LogMessage>
    {
        @Override
        public int compare(LogMessage c, LogMessage c1) {
            return c.getDate().compareTo(c1.getDate());
        }    
    }
}
