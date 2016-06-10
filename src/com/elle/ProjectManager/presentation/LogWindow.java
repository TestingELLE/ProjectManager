package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.logic.FilePathFormat;
import com.elle.ProjectManager.logic.LogMessage;
import com.elle.ProjectManager.logic.LoggingAspect;
import com.elle.ProjectManager.logic.ShortCutSetting;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.jdesktop.swingx.util.OS;

/**
 * LogWindow
 *
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class LogWindow extends JFrame {

    // variables
    public static final String HYPHENS = "-------------------------"; // delimiter
    public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    public static String fileName;
    private ArrayList<LogMessage> logMessages = new ArrayList<>();
    
    // components
    private static Component parent;
    private static JTextArea logText;
    private JScrollPane scrollPane;
    private JPanel panelLogWindowButtons;
    private JPanel panelLogWindowButtonsUp;
    private JButton btnLevelOne;
    private JButton btnLevelTwo;
    private JButton btnLevelThree;
    private JButton btnClearAllButToday;
    private JButton btnDeleteAllButToday;
    private JButton showAll;
    //private final JCheckBox jCheckBoxOrder;  // check box for order of dates
    //private final JLabel jLabelOrder; // label for checkbox order

    

    // constructor
    public LogWindow() {
        
        this.setTitle("Log Window");
        ImageIcon imag = new ImageIcon(
                "Images/elle gui image.jpg");
        this.setIconImage(imag.getImage());

        logText = new JTextArea(5, 30);
        logText.setEditable(false);
        logText.setLineWrap(true);
        ShortCutSetting.copyAndPasteShortCut(logText.getInputMap());

        //was testing the JList
        // I need a no horizontal scroll and a wrap
        // going to come back to this
//                JList list = new JList();
//                list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
//                list.add(logText);
//                list.setVisibleRowCount(0);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(logText);
//        scrollPane.setPreferredSize(new Dimension(924, 900));
//        logText.setPreferredSize(new Dimension(2000, 2000));
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // change layout of frame
        this.setLayout(new GridBagLayout());

        // set constraints for the scroll panel
        GridBagConstraints scrollPanelConstraints = new GridBagConstraints();
        scrollPanelConstraints.fill = GridBagConstraints.BOTH;
        scrollPanelConstraints.weightx = 1; // takes up whole x axis
        scrollPanelConstraints.weighty = 1; // takes up most y axis with room for buttons
        scrollPanelConstraints.gridx = 0; // first col cell
        scrollPanelConstraints.gridy = 0; // first row cell

        // add scroll panel to frame
        this.add(scrollPane, scrollPanelConstraints);

        // create a panel for buttons
        panelLogWindowButtons = new JPanel();

        panelLogWindowButtonsUp = new JPanel();
        // create buttons 
        btnClearAllButToday = new JButton("Clear All But Today");
        btnClearAllButToday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAllButTodayActionPerformed(evt);
            }
        });

        btnDeleteAllButToday = new JButton("Delete All But Today");
        btnDeleteAllButToday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAllButTodayActionPerformed(evt);
            }
        });

        /**
         * ******* THIS IS THE CHECKBOX ORDER FEATURE ****************
         */
//                jCheckBoxOrder = new JCheckBox();
//                jCheckBoxOrder.addActionListener(new java.awt.event.ActionListener() {
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        jCheckBoxOrderActionPerformed(evt);
//                    }
//                });
////                jLabelOrder = new JLabel("Order");
        showAll = new JButton("Show All");
        showAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllActionPerformed(evt);
            }
        });

        btnLevelOne = new JButton("Maximun");
        btnLevelOne.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                levelOneActionPerformed(e);
            }

        });
        btnLevelTwo = new JButton("Medium");
        btnLevelTwo.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                levelTwoActionPerformed(e);
            }

        });
        btnLevelThree = new JButton("Minimun");
        btnLevelThree.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                levelThreeActionPerformed(e);
            }

        });

        // add buttons to panel
//                panelLogWindowButtons.add(btnClearAll);
        panelLogWindowButtons.add(btnClearAllButToday);
        panelLogWindowButtons.add(btnDeleteAllButToday);
        //jPanelLogWindowButtons.add(jCheckBoxOrder);
        //jPanelLogWindowButtons.add(jLabelOrder);
        panelLogWindowButtons.add(showAll);
        panelLogWindowButtonsUp.add(btnLevelOne);
        panelLogWindowButtonsUp.add(btnLevelTwo);
        panelLogWindowButtonsUp.add(btnLevelThree);

        // set constraints for the buttons panel
        GridBagConstraints buttonsPanelConstraints = new GridBagConstraints();
        buttonsPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonsPanelConstraints.weightx = 1; // takes up whole x axis
        buttonsPanelConstraints.weighty = 0; // takes up enough y axis just for buttons
        buttonsPanelConstraints.gridx = 0; // first col cell
        buttonsPanelConstraints.gridy = 1; // second row cell

        GridBagConstraints buttonsPanelUpConstraints = new GridBagConstraints();
        buttonsPanelUpConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonsPanelUpConstraints.weightx = 1; // takes up whole x axis
        buttonsPanelUpConstraints.weighty = 0; // takes up enough y axis just for buttons
        buttonsPanelUpConstraints.gridx = 0; // first col cell
        buttonsPanelUpConstraints.gridy = 2; // second row cell

        // add panel to the frame
        this.add(panelLogWindowButtons, buttonsPanelConstraints);
        this.add(panelLogWindowButtonsUp, buttonsPanelUpConstraints);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(860, 540));
        this.pack();
        this.setVisible(false);
    }

    public static void readCurrentMessages(String str) {
        logText.append("\n");
        logText.append(str);
    }

    public void readMessages() {
        String line = "";
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            while (line != null) {
                String newLine = line;
                if (newLine.startsWith("1:") || newLine.startsWith("2:") || newLine.startsWith("3:")) {
                    newLine = newLine.substring(2);
                }
                logText.append("\n");
                logText.append(newLine);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException ex) {
            LoggingAspect.afterThrown(ex);
        } catch (Exception ex) {
            LoggingAspect.afterThrown(ex);
        }
    }

    /**
     * addMessage
     *
     * @param str
     */
    public static void addMessage(String str) {

        File file = new File(fileName);
        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (str.startsWith(HYPHENS)) {
                bufferedWriter.newLine();
            }
            bufferedWriter.write(str);
            bufferedWriter.newLine();
            bufferedWriter.close();
            readCurrentMessages(str);
        } catch (IOException ex) {
            LoggingAspect.afterThrown(ex);
        } catch (Exception ex) {
            LoggingAspect.afterThrown(ex);
        }
    }

    /**
     * addMessageWithDate
     *
     * @param str
     */
    public static void addMessageWithDate(String str) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss a");
        String storeStr = str;
        String dateStr = dateFormat.format(date);

        if (str.startsWith("1:") || str.startsWith("2:") || str.startsWith("3:")) {
            dateStr = str.substring(0, 2) + dateStr;
            storeStr = str.substring(2);
        }
        System.out.println(dateStr + " and " + storeStr);
        addMessage(dateStr + ": " + storeStr);
    }

    private void levelOneActionPerformed(ActionEvent e) {
        // store log messages in an array of log messages
        storeLogMessages(); // get most current messages to array

        // sorts by least recent date first
        Collections.sort(logMessages, new LogMessage.SortByMostRecentDateLast());

        logText.setText(""); // clear text box

        // print log messages to log window text box
        for (LogMessage logMessage : logMessages) {
            System.out.println("message here" + logMessage.getMessage());

            String message = logMessage.getMessage();
            if (message.startsWith("\n")) {
                if (!message.endsWith("\n")) {
                    message = message + "\n";
                }
                if (message.substring(1).startsWith("1:")) {
                    logText.append("\n");
                    logText.append(HYPHENS + dateFormat.format(logMessage.getDate()) + HYPHENS);
                    message = message.substring(0, 1) + message.substring(3);
                } else {
                    message = "";
                }
            }

            System.out.println("level One message: " + message);
            logText.append(message);
        }
    }

    private void levelTwoActionPerformed(ActionEvent e) {
        // store log messages in an array of log messages
        storeLogMessages(); // get most current messages to array

        // sorts by least recent date first
        Collections.sort(logMessages, new LogMessage.SortByMostRecentDateLast());

        logText.setText(""); // clear text box

        // print log messages to log window text box
        for (LogMessage logMessage : logMessages) {
            String message = logMessage.getMessage();
            if (message.startsWith("\n")) {
                if (!message.endsWith("\n")) {
                    message = message + "\n";
                }
                if (message.substring(1).startsWith("2:")) {
                    logText.append("\n");
                    logText.append(HYPHENS + dateFormat.format(logMessage.getDate()) + HYPHENS);
                    message = message.substring(0, 1) + message.substring(3);
                } else {
                    message = "";
                }
            }
            System.out.println("level two message: " + message);
            logText.append(message);
        }
    }

    private void levelThreeActionPerformed(ActionEvent e) {
        // store log messages in an array of log messages
        storeLogMessages(); // get most current messages to array

        // sorts by least recent date first
        Collections.sort(logMessages, new LogMessage.SortByMostRecentDateLast());

        logText.setText(""); // clear text box

        // print log messages to log window text box
        for (LogMessage logMessage : logMessages) {

            String message = logMessage.getMessage();

            if (message.startsWith("\n")) {
                if (!message.endsWith("\n")) {
                    message = message + "\n";
                }
                if (message.substring(1).startsWith("3:")) {
                    logText.append("\n");
                    logText.append(HYPHENS + dateFormat.format(logMessage.getDate()) + HYPHENS);
                    message = message.substring(0, 1) + message.substring(3);
                } else {
                    message = "";
                }
            }
            logText.append(message);
        }
    }

    /**
     * Clear all but today button action performed: When the Clear all but today
     * button is clicked, all the messages are removed from the scroll panel
     * text box, except todays.
     */
    private void btnClearAllButTodayActionPerformed(ActionEvent evt) {

        // store log messages in an array of log messages
        storeLogMessages(); // get most current messages to array

        /**
         * **************** CHECK BOX ORDER FEATURE *******************
         */
//            // get the order of messages
//            if(jCheckBoxOrder.isSelected()){
//                // sorts by most recent date first
//                Collections.sort(logMessages, new LogMessage.SortByMostRecentDateFirst());
//            }else if(!jCheckBoxOrder.isSelected()){
//                // sorts by most recent date last
//                Collections.sort(logMessages, new LogMessage.SortByMostRecentDateLast());
//            }
        // sorts by most recent date last
        Collections.sort(logMessages, new LogMessage.SortByMostRecentDateLast());

        // compare date with todays date and print to screen
        Date date = new Date(); // get todays date
        logText.setText(""); // clear text box
        for (LogMessage logMessage : logMessages) {

            // if date is today then print to screen
            if (logMessage.getDate().getYear() == date.getYear()
                    && logMessage.getDate().getMonth() == date.getMonth()
                    && logMessage.getDate().getDate() == date.getDate()) {
                logText.append(HYPHENS + dateFormat.format(logMessage.getDate()) + HYPHENS);

                String message = logMessage.getMessage();

                if (message.startsWith("\n")) {
                    if (message.substring(1).startsWith("1:") || message.substring(1).startsWith("2:") || message.substring(1).startsWith("3:")) {
                        message = message.substring(0, 1) + message.substring(3);
                    }
                    if (!message.endsWith("\n")) {
                        message = message + "\n";
                        System.out.println("not end with!");
                    }
                }
                logText.append(message);
            }
        }
    }

    /**
     * Clear all but today button action performed: When the Clear all but today
     * button is clicked, all the messages are removed from the scroll panel
     * text box, except todays.
     */
    private void btnDeleteAllButTodayActionPerformed(ActionEvent evt) {

        // store log messages in an array of log messages
        storeLogMessages(); // get most current messages to array

        /**
         * **************** CHECK BOX ORDER FEATURE *******************
         */
//            // get the order of messages
//            if(jCheckBoxOrder.isSelected()){
//                // sorts by most recent date first
//                Collections.sort(logMessages, new LogMessage.SortByMostRecentDateFirst());
//            }else if(!jCheckBoxOrder.isSelected()){
//                // sorts by most recent date last
//                Collections.sort(logMessages, new LogMessage.SortByMostRecentDateLast());
//            }
        // clear the text file
        clearTextFile();

        // sorts by most recent date last
        Collections.sort(logMessages, new LogMessage.SortByMostRecentDateLast());

        // compare date with todays date and print to screen
        Date date = new Date(); // get todays date
        logText.setText(""); // clear text box
        for (LogMessage logMessage : logMessages) {

            // if date is today then print to screen
            if (logMessage.getDate().getYear() == date.getYear()
                    && logMessage.getDate().getMonth() == date.getMonth()
                    && logMessage.getDate().getDate() == date.getDate()) {
                addMessage(HYPHENS + dateFormat.format(logMessage.getDate()) + HYPHENS);
                String message = logMessage.getMessage();
                if (message.startsWith("\n")) {
                    if (message.substring(1).startsWith("1:") || message.substring(1).startsWith("2:") || message.substring(1).startsWith("3:")) {
                        message = message.substring(0, 1) + message.substring(3);
                    }
                    if (!message.endsWith("\n")) {
                        message = message + "\n";
                        System.out.println("not end with!");
                    }
                }
                logText.append(message);
            }
        }

        // reload the messages from the file
        readMessages();
        storeLogMessages();
    }

    /**
     * Order check box: When the order check box is checked, all the messages
     * are reversed in order in the scroll pane text box.
     */
//        private void jCheckBoxOrderActionPerformed(ActionEvent evt) {
//            
//            // store log messages in an array of log messages
//            storeLogMessages(); // get most current messages to array
//            
//            // sort log messages
//            if(jCheckBoxOrder.isSelected()){
//                // sorts by most recent date first
//                Collections.sort(logMessages, new LogMessage.SortByMostRecentDateFirst());
//            }else if(!jCheckBoxOrder.isSelected()){
//                // sorts by least recent date first
//                Collections.sort(logMessages, new LogMessage.SortByLeastRecentDateFirst());
//            }
//            
//            logText.setText("");// clear text box
//            // print log messages to log window text box
//            for (LogMessage logMessage : logMessages) {
//                logText.append("-------------------------" + dateFormat.format(logMessage.getDate()) + "-------------------------");
//                logText.append(logMessage.getMessage());
//            }
//        }
    /**
     * Show all message with most recent appearing at the bottom
     *
     * @param evt
     */
    private void showAllActionPerformed(ActionEvent evt) {

        // store log messages in an array of log messages
        storeLogMessages(); // get most current messages to array

        // sorts by least recent date first
        Collections.sort(logMessages, new LogMessage.SortByMostRecentDateLast());

        logText.setText(""); // clear text box

        // print log messages to log window text box
        for (LogMessage logMessage : logMessages) {
            System.out.println("message here" + logMessage.getMessage());
            logText.append("\n");
            logText.append(HYPHENS + dateFormat.format(logMessage.getDate()) + HYPHENS);
            String message = logMessage.getMessage();
            if (message.startsWith("\n")) {
                if (message.substring(1).startsWith("1:") || message.substring(1).startsWith("2:") || message.substring(1).startsWith("3:")) {
                    message = message.substring(0, 1) + message.substring(3);
                }
            }
            if (!message.endsWith("\n")) {
                message = message + "\n";
                System.out.println("not end with!");
            }

            logText.append(message);
        }
    }

    /**
     * storeLogMessages: Method Stores each LogMessage object in an array. This
     * is to be able to easily retrieve specific data according to specific
     * times or dates.
     */
    private void storeLogMessages() {

        File file = new File(fileName);
        logMessages.clear(); // clear array of any elements
        Date date = new Date();
        String message = "";

        if (file.exists()) // prevent the FileNotFoundException
        {
            try {

                BufferedReader in
                        = new BufferedReader(
                                new FileReader(fileName));

                // read all log messages stored in the log file
                // and store them into the array list
                String line = in.readLine();
                while (line != null) {
                    System.out.println();
                    // first get date between hyphens
                    if (line.startsWith(HYPHENS)) {
                        String[] columns = line.split(HYPHENS);
                        date = dateFormat.parse(columns[1]);
                        message = ""; // reset message string

                        line = in.readLine();
                    } // get message until next date
                    else {
                        message = message + "\n" + line;
                        line = in.readLine();

                        // if next line is null or next date 
                        // then store this logmessage
                        if (line == null || line.startsWith(HYPHENS)) {
                            logMessages.add(new LogMessage(date, message));
                        }
                    }
                }

                in.close(); // close the input stream
            } catch (IOException e) {
                LoggingAspect.afterThrown(e);
            } catch (ParseException ex) {
                LoggingAspect.afterThrown(ex);
            }
        }
    }

    /**
     * clearTextFile clear the text file
     */
    public void clearTextFile() {

        // clear the log.text file
        try {
            PrintWriter pw = new PrintWriter(fileName);
            pw.close();
        } catch (FileNotFoundException ex) {
            LoggingAspect.afterThrown(ex);
        }
    }
    
    public void setUserLogFileDir(String userName) {
        fileName = FilePathFormat.supportFilePath()+ "PM_" + userName + "_log.txt";
    }
    
    public static void setParent(Component c){
        parent = c;
    }
}
