package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.admissions.Authorization;
import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.database.ModifiedData;
import com.elle.ProjectManager.database.ModifiedTableData;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.imageio.ImageIO;

/**
 * ProjectManagerWindow
 *
 * @author Xiaoqian FU
 * @since Oct 29, 2015
 * @version 0.9.3
 */
public class ProjectManagerWindow extends JFrame implements ITableConstants {

    // Edit the version and date it was created for new archives and jars
    // this looks like it was moved to ITableConstants
    //private final String CREATION_DATE = "2016-02-25";
    //private final String VERSION = "1.2.0";
    // attributes
    private Map<String, Tab> tabs; // stores individual tabName information
    private Map<String, Map<Integer, ArrayList<Object>>> comboBoxForSearchDropDown;
    //   private Map<Integer, ArrayList<Object>> valueListMap;
    private static Statement statement;
    private String database;

    //tables
    private String[] tableNames = {"PM", "ELLEGUI", "Analyster", "Other", TASKFILES_TABLE_NAME};

    // components
    private static ProjectManagerWindow instance;
    private IssueWindow addIssueWindow;
    private LogWindow logWindow;
    private LoginWindow loginWindow;
    private BatchEditWindow batchEditWindow;
    private EditDatabaseWindow editDatabaseWindow;
    private ShortCutSetting ShortCut;
    private ConsistencyOfTableColumnName ColumnNameConsistency;

    private Map<Integer, IssueWindow> openingIssuesList;

    // colors - Edit mode labels
    private Color editModeDefaultTextColor;
    private Color editModeActiveTextColor;

    private String editingTabName; // stores the name of the tab that is editing

    // Misc 
    private boolean addIssueWindowShow;
    private boolean isBatchEditWindowShow;
    private boolean comboBoxStartToSearch;
    private boolean ifDeleteRecords;

//    private int addRecordLevel = 2;
//    private int deleteRecordLevel = 2;
//    private PopupWindowInTableCell tableCellPopupWindow;
    private boolean popupWindowShowInPM;
//    int lastSelectedRow = -1, lastSelectedColumn = -1;
    // create a jlabel to show the database used
    private JLabel databaseLabel;
    private String currentTabName;
    private String userName;
    private String searchValue = null;
//    private ArrayList<Integer> idNumOfOpenningIssues;
    private ArrayList<String> programmersActiveForSearching;

    /**
     * CONSTRUCTOR
     */
    public ProjectManagerWindow(String userName) {

        /**
         * Note: initComponents() executes the tabpaneChanged method. Thus, some
         * things need to be before or after the initComponents();
         */
        // the statement is used for sql statements with the database connection
        // the statement is created in LoginWindow and passed to Analyster.
        statement = DBConnection.getStatement();
        instance = this;                         // this is used to call this instance of Analyster 

        this.userName = userName;

        // initialize tabs
        tabs = new HashMap();
        // initialize comboBoxForSeachDropDownList
        comboBoxForSearchDropDown = new HashMap();
        programmersActiveForSearching = new ArrayList<String>();

        // create tabName objects -> this has to be before initcomponents();
        for (int tableName = 0; tableName < tableNames.length; tableName++) {
            tabs.put(tableNames[tableName], new Tab());

            // set tableSelected names 
            tabs.get(tableNames[tableName]).setTableName(tableNames[tableName]);
            tabs.get(tableNames[tableName]).setTableName(tableNames[tableName]);

            if (!tableNames[tableName].equals(TASKFILES_TABLE_NAME)) {
                // set the search fields for the comboBox for each tabName
                tabs.get(tableNames[tableName]).setSearchFields(TASKS_SEARCH_FIELDS);
                // set the search fields for the comboBox for each tabName
                tabs.get(tableNames[tableName]).setBatchEditFields(TASKS_BATCHEDIT_CB_FIELDS);
                // set column width percents to tables of the tabName objects
                tabs.get(tableNames[tableName]).setColWidthPercent(COL_WIDTH_PER_TASKS);
            } else {
                tabs.get(tableNames[tableName]).setSearchFields(TASKFILES_SEARCH_FIELDS);
                tabs.get(tableNames[tableName]).setBatchEditFields(TASKFILES_BATCHEDIT_CB_FIELDS);
                tabs.get(tableNames[tableName]).setColWidthPercent(COL_WIDTH_PER_REPORTS);
            }
//        tabs.get(TASKNOTES_TABLE_NAME).setSearchFields(TASKNOTES_SEARCH_FIELDS);
//        tabs.get(TASKNOTES_TABLE_NAME).setBatchEditFields(TASKNOTES_BATCHEDIT_CB_FIELDS);
//        tabs.get(TASKNOTES_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_ARCHIVE);

            // set Activate Records menu item enabled for each tabName
            tabs.get(tableNames[tableName]).setActivateRecordMenuItemEnabled(false);
            tabs.get(tableNames[tableName]).setActivateRecordMenuItemEnabled(false);
//        tabs.get(TASKNOTES_TABLE_NAME).setActivateRecordMenuItemEnabled(true);

            // set Archive Records menu item enabled for each tabName
            tabs.get(tableNames[tableName]).setArchiveRecordMenuItemEnabled(true);
            tabs.get(tableNames[tableName]).setArchiveRecordMenuItemEnabled(false);
//        tabs.get(TASKNOTES_TABLE_NAME).setArchiveRecordMenuItemEnabled(false);

            // set add records button visible for each tabName
            tabs.get(tableNames[tableName]).setAddRecordsBtnVisible(true);
            tabs.get(tableNames[tableName]).setAddRecordsBtnVisible(true);
//        tabs.get(TASKNOTES_TABLE_NAME).setAddRecordsBtnVisible(false);

            // set batch edit button visible for each tabName
            tabs.get(tableNames[tableName]).setBatchEditBtnVisible(true);
            tabs.get(tableNames[tableName]).setBatchEditBtnVisible(true);
//        tabs.get(TASKNOTES_TABLE_NAME).setBatchEditBtnVisible(false);
        }

        initComponents(); // generated code

        // initialize the colors for the edit mode text
        editModeActiveTextColor = new Color(44, 122, 22); //dark green
        editModeDefaultTextColor = labelEditMode.getForeground();

        // set names to tables (this was in tabbedPanelChanged method)
        PMTable.setName(tableNames[0]);
        ELLEGUITable.setName(tableNames[1]);
        AnalysterTable.setName(tableNames[2]);
        OtherTable.setName(tableNames[3]);

        issue_filesTable.setName(tableNames[4]);
//        issue_notesTable.setName(TASKNOTES_TABLE_NAME);

        // set tables to tabName objects
        tabs.get(tableNames[0]).setTable(PMTable);
        tabs.get(tableNames[1]).setTable(ELLEGUITable);
        tabs.get(tableNames[2]).setTable(AnalysterTable);
        tabs.get(tableNames[3]).setTable(OtherTable);

        tabs.get(TASKFILES_TABLE_NAME).setTable(issue_filesTable);
//        tabs.get(TASKNOTES_TABLE_NAME).setTable(issue_notesTable);

        // set array variable of stored column names of the tables
        // this is just to store and use the information
        // to actually change the tableSelected names it should be done
        // through properties in the gui design tabName
        tabs.get(tableNames[0]).setTableColNames(PMTable);
        tabs.get(tableNames[1]).setTableColNames(ELLEGUITable);
        tabs.get(tableNames[2]).setTableColNames(AnalysterTable);
        tabs.get(tableNames[3]).setTableColNames(OtherTable);

        tabs.get(TASKFILES_TABLE_NAME).setTableColNames(issue_filesTable);
//        tabs.get(TASKNOTES_TABLE_NAME).setTableColNames(issue_notesTable);

        informationLabel.setText("");

        // this sets the KeyboardFocusManger
        setKeyboardFocusManager(this);
        if (!getSelectedTabName().equals(TASKFILES_TABLE_NAME)) {
            btnAddIssue.setText("Add issue to " + getSelectedTabName());
        } else {
            btnAddIssue.setText("Add " + getSelectedTabName());
        }

        textComponentShortCutSetting();

        // show and hide components
        btnUploadChanges.setVisible(false);
        jPanelSQL.setVisible(false);
        btnEnterSQL.setVisible(true);
        btnCancelSQL.setVisible(true);
        btnBatchEdit.setVisible(true);
        jTextAreaSQL.setVisible(true);
        jPanelEdit.setVisible(true);
        btnRevertChanges.setVisible(false);

        // add filters for each tableSelected
        // must be before setting ColumnPopupMenu because this is its parameter
        tabs.get(tableNames[0]).setFilter(new TableFilter(PMTable));
        tabs.get(tableNames[1]).setFilter(new TableFilter(ELLEGUITable));
        tabs.get(tableNames[2]).setFilter(new TableFilter(AnalysterTable));
        tabs.get(tableNames[3]).setFilter(new TableFilter(OtherTable));

        //add custom id list for each tab
        tabs.get(tableNames[0]).setCustomIdList(new CustomIDList());
        tabs.get(tableNames[1]).setCustomIdList(new CustomIDList());
        tabs.get(tableNames[2]).setCustomIdList(new CustomIDList());
        tabs.get(tableNames[3]).setCustomIdList(new CustomIDList());

        tabs.get(TASKFILES_TABLE_NAME).setFilter(new TableFilter(issue_filesTable));
//        tabs.get(TASKNOTES_TABLE_NAME).setFilter(new TableFilter(issue_notesTable));

        // initialize columnPopupMenu 
        // - must be before setTerminalFunctions is called
        // - because the mouslistener is added to the tableSelected header
        tabs.get(tableNames[0])
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(tableNames[0]).
                                getFilter(), tabs.get(tableNames[0]).getCustomIdList()));
        tabs.get(tableNames[1])
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(tableNames[1]).
                                getFilter(), tabs.get(tableNames[1]).getCustomIdList()));
        tabs.get(tableNames[2])
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(tableNames[2]).
                                getFilter(), tabs.get(tableNames[2]).getCustomIdList()));
        tabs.get(tableNames[3])
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(tableNames[3]).
                                getFilter(), tabs.get(tableNames[3]).getCustomIdList()));

        tabs.get(TASKFILES_TABLE_NAME)
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(TASKFILES_TABLE_NAME).
                                getFilter(), tabs.get(TASKFILES_TABLE_NAME).getCustomIdList()));
//        tabs.get(TASKNOTES_TABLE_NAME)
//                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(TASKNOTES_TABLE_NAME).getFilter()));
        comboBoxStartToSearch = false;
        boolean ifDeleteRecords = false;
// load data from database to tables
        loadTables(tabs);

        // set initial record counts of now full tables
        // this should only need to be called once at start up of Analyster.
        // total counts are removed or added in the Tab class
        initTotalRowCounts(tabs);

        // set the cell renderers for each tabName
        tabs.get(tableNames[0]).setCellRenderer(new JTableCellRenderer(PMTable));
        tabs.get(tableNames[1]).setCellRenderer(new JTableCellRenderer(ELLEGUITable));
        tabs.get(tableNames[2]).setCellRenderer(new JTableCellRenderer(AnalysterTable));
        tabs.get(tableNames[3]).setCellRenderer(new JTableCellRenderer(OtherTable));

        tabs.get(TASKFILES_TABLE_NAME).setCellRenderer(new JTableCellRenderer(issue_filesTable));
//        tabs.get(TASKNOTES_TABLE_NAME).setCellRenderer(new JTableCellRenderer(issue_notesTable));

        // set the modified tableSelected data objects for each tabName
        tabs.get(tableNames[0]).setTableData(new ModifiedTableData(PMTable));
        tabs.get(tableNames[1]).setTableData(new ModifiedTableData(ELLEGUITable));
        tabs.get(tableNames[2]).setTableData(new ModifiedTableData(AnalysterTable));
        tabs.get(tableNames[3]).setTableData(new ModifiedTableData(OtherTable));

        tabs.get(TASKFILES_TABLE_NAME).setTableData(new ModifiedTableData(issue_filesTable));
//        tabs.get(TASKNOTES_TABLE_NAME).setTableData(new ModifiedTableData(issue_notesTable));

        // set all the tabs initially not in editing mode
        for (int tableName = 0; tableName < tableNames.length; tableName++) {
            tabs.get(tableNames[tableName]).setEditing(false);

        }

        // initial openedIssuesList to manager all the openning issues
        openingIssuesList = new HashMap<Integer, IssueWindow>();
//        tabs.get(TASKNOTES_TABLE_NAME).setEditing(false);

//        // Call the initTableCellPopup method to initiate the Table Cell Popup window
//        initTableCellPopup();
//       
//        // Set the tableSelected listener for the tableSelected cell popup window.
//        setTableListener(tasksTable);
//        setTableListener(task_filesTable);
//        setTableListener(task_notesTable);  
        isBatchEditWindowShow = false;

        currentTabName = getSelectedTabName();

        btnBatchEdit.setVisible(false);

//        numOfAddIssueWindowOpened = 0;
//        String tabName = getSelectedTabName();
        String searchContent = comboBoxField.getSelectedItem().toString();

        this.updateComboList(searchContent, currentTabName);

        this.comboBoxValue.setSelectedItem("Enter search value here");
        comboBoxStartToSearch = true;
        this.comboBoxValue.getEditor().getEditorComponent().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                comboBoxForSearchEditorMouseClicked(e);
            }
        });
        // set title of window to Project Manager
        this.setTitle("Project Manager");

        // set the size for project manager
        this.setPreferredSize(new Dimension(1014, 550));
        this.setMinimumSize(new Dimension(1000, 550));

        this.pack();

        // authorize user for this component
        Authorization.authorize(this);
    }

    /*
     * This is to make the scroll bar always scrolling down.
     */
    private void scrollDown(JScrollPane scrollPane) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }
//    
//    /*
//     * This is to initiate the tableSelected cell popup window.
//     */
//    private void initTableCellPopup(){
//        
//        // initialize the textAreatableCellPopup
//        textAreatableCellPopup = new JTextArea();
//        textAreatableCellPopup.setOpaque(true);
//        textAreatableCellPopup.setBorder(BorderFactory.createLineBorder(Color.gray));
//        textAreatableCellPopup.setSize(new Dimension(500,200)); 
//        textAreatableCellPopup.setLineWrap(true);
//        textAreatableCellPopup.setWrapStyleWord(true);    
//        
//        // initialize the areaScrollPanetableCellPopup
//        areaScrollPanetableCellPopup = new JScrollPane(textAreatableCellPopup);
//        areaScrollPanetableCellPopup.setOpaque(true);       
//        areaScrollPanetableCellPopup.setSize(new Dimension(500,200));        
//        
//        // initialize the tableCellPopupPanel
//        tableCellPopupPanel = new JPanel();
//        tableCellPopupPanel.add(areaScrollPanetableCellPopup);
//        tableCellPopupPanel.setLayout(new BorderLayout());
//        tableCellPopupPanel.setOpaque(true);
//        tableCellPopupPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
//        tableCellPopupPanel.setSize(500, 200);   
//        tableCellPopupPanel.setVisible(false);
//        
//        // initialize the controlPopupPanel
//        controlPopupPanel = new JPanel(new GridBagLayout());
//        controlPopupPanel.setOpaque(true);
//        controlPopupPanel.setSize(500, 35);
//        controlPopupPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
//        controlPopupPanel.setVisible(false);
//
//        
//        // initialize the confirmButtonTableCellPopup                       
//        GridBagConstraints tableCellPopupConstraints = new GridBagConstraints();
//        tableCellPopupConstraints.gridx = 0;
//        tableCellPopupConstraints.gridy = 0;
//        tableCellPopupConstraints.fill = GridBagConstraints.HORIZONTAL;
//        
//        confirmButtonTableCellPopup = new JButton("Confirm");
//        confirmButtonTableCellPopup.setOpaque(true);
//        controlPopupPanel.add(confirmButtonTableCellPopup, tableCellPopupConstraints);
//
//        // initialize the cancelButtonTableCellPopup        
//        tableCellPopupConstraints.gridx = 1;
//        tableCellPopupConstraints.gridy = 0;
//        tableCellPopupConstraints.fill = GridBagConstraints.HORIZONTAL;
//        
//        cancelButtonTableCellPopup = new JButton("Cancel");
//        cancelButtonTableCellPopup.setOpaque(true);
//        controlPopupPanel.add(cancelButtonTableCellPopup, tableCellPopupConstraints);
//            
//        // set the popup_layer of projectmanager for the tableSelected cell popup window
//        this.getLayeredPane().add(tableCellPopupPanel, JLayeredPane.POPUP_LAYER);
//        this.getLayeredPane().add(controlPopupPanel, JLayeredPane.POPUP_LAYER);
//        
//    }
//
//    /*
//     * This is to set tableSelected listener for the tableSelected cell popup window.
//     * @parm tableSelected
//    */        
//    private void setTableListener(JTable tableSelected){
//        
//        tableSelected.addMouseListener(new MouseAdapter(){             
//            public void mouseClicked(MouseEvent evt){
//                int rowIndex = tableSelected.getSelectedRow();
//                int column = tableSelected.getSelectedColumn();   
//                if(tableSelected.equals(tasksTable)){                   
//                    if(column == 2 || column == 4 || column == 5){
//                        // popup tableSelected cell edit window
//                        tableCellPopup(tasksTable, rowIndex , column);
//                    }else{
//                        tableCellPopupPanel.setVisible(false);
//                        controlPopupPanel.setVisible(false);
//                    }
//                }
//                else if(tableSelected.equals(task_filesTable)){
//                    if(column == 5 || column == 6 || column == 7){
//                        // popup tableSelected cell edit window
//                        tableCellPopup(task_filesTable, rowIndex , column);
//                    }else{
//                        tableCellPopupPanel.setVisible(false);
//                        controlPopupPanel.setVisible(false);
//                    }                   
//                }
//                else if(tableSelected.equals(task_notesTable)){
//                    if(column == 3){
//                        // popup tableSelected cell edit window
//                        tableCellPopup(task_notesTable, rowIndex , column);
//                    }else{
//                        tableCellPopupPanel.setVisible(false);
//                        controlPopupPanel.setVisible(false);
//                    }                    
//                }
//            }
//        });
//        
//        tableSelected.setFocusTraversalKeysEnabled(false);
//        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher(){
//            @Override
//            public boolean dispatchKeyEvent(KeyEvent evt){                
//                if(evt.getKeyCode() == KeyEvent.VK_TAB || evt.getKeyCode() == KeyEvent.VK_LEFT ||
//                        evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_UP ||
//                        evt.getKeyCode() == KeyEvent.VK_DOWN){
//                    
//                    if (evt.getComponent() instanceof JTable){
//                        JTable tableSelected = (JTable) evt.getComponent();
//                        int rowIndex = tableSelected.getSelectedRow();                    
//                        int column = tableSelected.getSelectedColumn();   
//                        if(tableSelected.equals(tasksTable)){
//                            if(column == 2 || column == 4 || column == 5 || column == 6){
//                                // popup tableSelected cell edit window
//                                tableCellPopup(tasksTable, rowIndex , column);
//                            }else{
//                                tableCellPopupPanel.setVisible(false);
//                                controlPopupPanel.setVisible(false);
//                            }
//                        }
//                        else if(tableSelected.equals(task_filesTable)){
//                            if(column == 5 || column == 6 || column == 7){
//                                // popup tableSelected cell edit window
//                                tableCellPopup(task_filesTable, rowIndex , column);
//                            }else{
//                                tableCellPopupPanel.setVisible(false);
//                                controlPopupPanel.setVisible(false);
//                            }                   
//                        }
//                        else if(tableSelected.equals(task_notesTable)){
//                            if(column == 3){
//                                // popup tableSelected cell edit window
//                                tableCellPopup(task_notesTable, rowIndex , column);
//                            }else{
//                                tableCellPopupPanel.setVisible(false);
//                                controlPopupPanel.setVisible(false);
//                            }                    
//                        }
//                    }
//                }
//                return false; 
//            }
//        });
//    }
//        
//    
//    
//    /*
//     * This is to set the tableSelected cell popup window visible to edit.
//     * @parm selectedTable, rowIndex , column
//    */    
//    private void tableCellPopup(JTable selectedTable, int rowIndex, int column){
//        
//        // find the selected tableSelected cell 
//        Rectangle cellRect = selectedTable.getCellRect(rowIndex, column, true);
//        
//        // set the tableSelected cell popup window visible
//        tableCellPopupPanel.setVisible(true);
//        controlPopupPanel.setVisible(true); 
//   
//        // use the tableSelected cell content to set the content for textarea
//        textAreatableCellPopup.setText("");
//        textAreatableCellPopup.setText((String) selectedTable.getValueAt(rowIndex, column)); 
//    
//        // set the tableCellPopupPanel position
//        tableCellPopupPanel.setLocation(cellRect.x + 2, cellRect.y + cellRect.height + 2 + 150);  
//        
//        // set the controlPopupPanel position
//        controlPopupPanel.setLocation(cellRect.x + 2, cellRect.y + cellRect.height + 2 + 200 + 150);
//         
//        // update the tableSelected cell content and tableSelected cell popup window
//        confirmButtonTableCellPopup.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e){
//                String newTableCellValue = textAreatableCellPopup.getText();
//                tableCellPopupPanel.setVisible(false);
//                controlPopupPanel.setVisible(false);                   
//                selectedTable.setValueAt(newTableCellValue, rowIndex, column);
//                uploadChanges();
//            }
//        });
//           
//        // quit the tableSelected cell popup window
//        cancelButtonTableCellPopup.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e){  
//                tableCellPopupPanel.setVisible(false);
//                controlPopupPanel.setVisible(false);                      
//            }
//        });         
//    }   
//        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TableFilterBtnGroup = new javax.swing.ButtonGroup();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jPanel5 = new javax.swing.JPanel();
        tabbedPanel = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        PMTable = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        ELLEGUITable = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        AnalysterTable = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        OtherTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        issue_filesTable = new javax.swing.JTable();
        jPanelEdit = new javax.swing.JPanel();
        btnBatchEdit = new javax.swing.JButton();
        btnAddIssue = new javax.swing.JButton();
        btnUploadChanges = new javax.swing.JButton();
        labelEditModeState = new javax.swing.JLabel();
        labelEditMode = new javax.swing.JLabel();
        btnRevertChanges = new javax.swing.JButton();
        informationLabel = new javax.swing.JLabel();
        jPanelSQL = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaSQL = new javax.swing.JTextArea();
        btnEnterSQL = new javax.swing.JButton();
        btnCancelSQL = new javax.swing.JButton();
        btnCloseSQL = new javax.swing.JButton();
        addPanel_control = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        labelRecords = new javax.swing.JLabel();
        labelTimeLastUpdate = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        comboBoxField = new javax.swing.JComboBox();
        btnClearAllFilter = new javax.swing.JButton();
        searchInformationLabel = new javax.swing.JLabel();
        comboBoxValue = new javax.swing.JComboBox();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuItemVersion = new javax.swing.JMenuItem();
        menuSelectConn = new javax.swing.JMenu();
        menuItemAWSAssign = new javax.swing.JMenuItem();
        menuPrint = new javax.swing.JMenu();
        menuItemPrintGUI = new javax.swing.JMenuItem();
        menuItemPrintDisplay = new javax.swing.JMenuItem();
        menuItemSaveFile = new javax.swing.JMenuItem();
        menuItemLogOff = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuItemManageDBs = new javax.swing.JMenuItem();
        menuItemDeleteRecord = new javax.swing.JMenuItem();
        menuItemArchiveRecord = new javax.swing.JMenuItem();
        menuItemActivateRecord = new javax.swing.JMenuItem();
        menuFind = new javax.swing.JMenu();
        menuReports = new javax.swing.JMenu();
        menuView = new javax.swing.JMenu();
        menuItemLogChkBx = new javax.swing.JCheckBoxMenuItem();
        menuItemSQLCmdChkBx = new javax.swing.JCheckBoxMenuItem();
        menuitemViewOneIssue = new javax.swing.JMenuItem();
        menuItemViewSplashScreen = new javax.swing.JMenuItem();
        menuTools = new javax.swing.JMenu();
        menuItemReloadData = new javax.swing.JMenuItem();
        menuItemReloadAllData = new javax.swing.JMenuItem();
        menuItemTurnEditModeOff = new javax.swing.JMenuItem();
        menuItemMoveSeletedRowsToEnd = new javax.swing.JMenuItem();
        menuItemCompIssues = new javax.swing.JMenuItem();
        menuItemBackup = new javax.swing.JMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuItemRepBugSugg = new javax.swing.JMenuItem();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabbedPanel.setAlignmentX(0.0F);
        tabbedPanel.setAlignmentY(0.0F);
        tabbedPanel.setName("Analyster"); // NOI18N
        tabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPanelStateChanged(evt);
            }
        });

        PMTable.setAutoCreateRowSorter(true);
        PMTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "app", "title", "description", "programmer", "dateOpened", "rk", "version", "dateClosed"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PMTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        PMTable.setMinimumSize(new java.awt.Dimension(10, 240));
        PMTable.setName("PM"); // NOI18N
        jScrollPane1.setViewportView(PMTable);

        tabbedPanel.addTab("PM", jScrollPane1);

        ELLEGUITable.setAutoCreateRowSorter(true);
        ELLEGUITable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "taskID", "app", "title", "description", "programmer", "dateOpened", "rk", "version", "dateClosed"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ELLEGUITable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        ELLEGUITable.setMinimumSize(new java.awt.Dimension(10, 240));
        ELLEGUITable.setName("ELLEGUI"); // NOI18N
        jScrollPane6.setViewportView(ELLEGUITable);

        tabbedPanel.addTab("ELLEGUI", jScrollPane6);

        AnalysterTable.setAutoCreateRowSorter(true);
        AnalysterTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "taskID", "app", "title", "description", "programmer", "dateOpened", "rk", "version", "dateClosed"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        AnalysterTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        AnalysterTable.setMinimumSize(new java.awt.Dimension(10, 240));
        AnalysterTable.setName(""); // NOI18N
        jScrollPane7.setViewportView(AnalysterTable);

        tabbedPanel.addTab("Analyster", jScrollPane7);

        OtherTable.setAutoCreateRowSorter(true);
        OtherTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "taskID", "app", "title", "description", "programmer", "dateOpened", "rk", "version", "dateClosed"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        OtherTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        OtherTable.setMinimumSize(new java.awt.Dimension(10, 240));
        OtherTable.setName("Other"); // NOI18N
        jScrollPane5.setViewportView(OtherTable);

        tabbedPanel.addTab("Other", jScrollPane5);

        issue_filesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "fileID", "taskID", "app", "submitter", "step", "date_", "files", "path", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        issue_filesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        issue_filesTable.setMinimumSize(new java.awt.Dimension(10, 240));
        issue_filesTable.setName("issue_files"); // NOI18N
        jScrollPane4.setViewportView(issue_filesTable);

        tabbedPanel.addTab("issue_files", jScrollPane4);

        jPanelEdit.setPreferredSize(new java.awt.Dimension(636, 180));

        btnBatchEdit.setText("Batch Edit");
        btnBatchEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchEditActionPerformed(evt);
            }
        });

        btnAddIssue.setText("Add Issue");
        btnAddIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddIssueActionPerformed(evt);
            }
        });

        btnUploadChanges.setText("Upload Changes");
        btnUploadChanges.setMaximumSize(new java.awt.Dimension(95, 30));
        btnUploadChanges.setMinimumSize(new java.awt.Dimension(95, 30));
        btnUploadChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadChangesActionPerformed(evt);
            }
        });

        labelEditModeState.setText("OFF");

        labelEditMode.setText("Edit Mode:");

        btnRevertChanges.setText("Revert Changes");
        btnRevertChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevertChangesActionPerformed(evt);
            }
        });

        informationLabel.setText("jLabel2");

        javax.swing.GroupLayout jPanelEditLayout = new javax.swing.GroupLayout(jPanelEdit);
        jPanelEdit.setLayout(jPanelEditLayout);
        jPanelEditLayout.setHorizontalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelEditMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelEditModeState)
                .addGap(233, 233, 233)
                .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(informationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelEditLayout.createSequentialGroup()
                        .addComponent(btnUploadChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRevertChanges, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(btnAddIssue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBatchEdit)
                        .addGap(26, 26, 26))))
        );
        jPanelEditLayout.setVerticalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelEditMode)
                    .addComponent(labelEditModeState)
                    .addComponent(btnBatchEdit)
                    .addComponent(btnAddIssue)
                    .addComponent(btnRevertChanges)
                    .addComponent(btnUploadChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(informationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane2.setBorder(null);

        jTextAreaSQL.setBackground(new java.awt.Color(0, 153, 102));
        jTextAreaSQL.setColumns(20);
        jTextAreaSQL.setLineWrap(true);
        jTextAreaSQL.setRows(5);
        jTextAreaSQL.setText("Please input an SQL statement:\\n>>");
        ((AbstractDocument) jTextAreaSQL.getDocument())
        .setDocumentFilter(new CreateDocumentFilter(33));
        jTextAreaSQL.setWrapStyleWord(true);
        jTextAreaSQL.setMaximumSize(new java.awt.Dimension(1590, 200));
        jTextAreaSQL.setMinimumSize(new java.awt.Dimension(1590, 200));
        jScrollPane2.setViewportView(jTextAreaSQL);

        btnEnterSQL.setText("Enter");
        btnEnterSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterSQLActionPerformed(evt);
            }
        });

        btnCancelSQL.setText("Cancel");
        btnCancelSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelSQLActionPerformed(evt);
            }
        });

        btnCloseSQL.setText("Close");
        btnCloseSQL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseSQLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSQLLayout = new javax.swing.GroupLayout(jPanelSQL);
        jPanelSQL.setLayout(jPanelSQLLayout);
        jPanelSQLLayout.setHorizontalGroup(
            jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEnterSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCloseSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanelSQLLayout.setVerticalGroup(
            jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(btnEnterSQL)
                .addGap(4, 4, 4)
                .addComponent(btnCancelSQL)
                .addGap(4, 4, 4)
                .addComponent(btnCloseSQL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
            .addComponent(jPanelSQL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabbedPanel)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        tabbedPanel.getAccessibleContext().setAccessibleName("Reports");
        tabbedPanel.getAccessibleContext().setAccessibleParent(tabbedPanel);

        addPanel_control.setPreferredSize(new java.awt.Dimension(1045, 120));

        labelRecords.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelRecords.setText("labelRecords");

        labelTimeLastUpdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTimeLastUpdate.setText("Last updated: ");
        labelTimeLastUpdate.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelTimeLastUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(labelRecords, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(labelTimeLastUpdate)
                .addGap(0, 0, 0)
                .addComponent(labelRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        searchPanel.setPreferredSize(new java.awt.Dimension(584, 76));

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        comboBoxField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "programmer", "title", "description", "dateOpened", "dateClosed", "rk", "version" }));
        comboBoxField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxFieldActionPerformed(evt);
            }
        });

        btnClearAllFilter.setText("Clear All Filters");
        btnClearAllFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAllFilterActionPerformed(evt);
            }
        });

        comboBoxValue.setEditable(true);
        comboBoxValue.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        comboBoxValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnClearAllFilter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboBoxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(comboBoxValue, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addGap(0, 96, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchInformationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClearAllFilter)
                    .addComponent(comboBoxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(comboBoxValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(searchInformationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout addPanel_controlLayout = new javax.swing.GroupLayout(addPanel_control);
        addPanel_control.setLayout(addPanel_controlLayout);
        addPanel_controlLayout.setHorizontalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        addPanel_controlLayout.setVerticalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addPanel_controlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuFile.setText("File");

        menuItemVersion.setText("Version");
        menuItemVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemVersionActionPerformed(evt);
            }
        });
        menuFile.add(menuItemVersion);

        menuSelectConn.setText("Select Connection");
        menuSelectConn.setEnabled(false);

        menuItemAWSAssign.setText("AWS Assignments");
        menuItemAWSAssign.setEnabled(false);
        menuItemAWSAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAWSAssignActionPerformed(evt);
            }
        });
        menuSelectConn.add(menuItemAWSAssign);

        menuFile.add(menuSelectConn);

        menuPrint.setText("Print");

        menuItemPrintGUI.setText("Print GUI");
        menuPrint.add(menuItemPrintGUI);

        menuItemPrintDisplay.setText("Print Display Window");
        menuPrint.add(menuItemPrintDisplay);

        menuFile.add(menuPrint);

        menuItemSaveFile.setText("Save File");
        menuItemSaveFile.setEnabled(false);
        menuFile.add(menuItemSaveFile);

        menuItemLogOff.setText("Log out");
        menuItemLogOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLogOffActionPerformed(evt);
            }
        });
        menuFile.add(menuItemLogOff);

        menuBar.add(menuFile);

        menuEdit.setText("Edit");

        menuItemManageDBs.setText("Manage databases");
        menuItemManageDBs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemManageDBsActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemManageDBs);

        menuItemDeleteRecord.setText("Delete Record");
        menuItemDeleteRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDeleteRecordActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemDeleteRecord);

        menuItemArchiveRecord.setText("Archive Record");
        menuItemArchiveRecord.setEnabled(false);
        menuItemArchiveRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemArchiveRecordActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemArchiveRecord);

        menuItemActivateRecord.setText("Activate Record");
        menuItemActivateRecord.setEnabled(false);
        menuItemActivateRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemActivateRecordActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemActivateRecord);

        menuBar.add(menuEdit);

        menuFind.setText("Find");
        menuBar.add(menuFind);

        menuReports.setText("Reports");
        menuReports.setEnabled(false);
        menuBar.add(menuReports);

        menuView.setText("View");

        menuItemLogChkBx.setText("Log");
        menuItemLogChkBx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLogChkBxActionPerformed(evt);
            }
        });
        menuView.add(menuItemLogChkBx);

        menuItemSQLCmdChkBx.setText("SQL Command");
        menuItemSQLCmdChkBx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSQLCmdChkBxActionPerformed(evt);
            }
        });
        menuView.add(menuItemSQLCmdChkBx);

        menuitemViewOneIssue.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK));
        menuitemViewOneIssue.setText("View Selected Issue");
        menuitemViewOneIssue.setToolTipText("");
        menuitemViewOneIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuitemViewOneIssueActionPerformed(evt);
            }
        });
        menuView.add(menuitemViewOneIssue);

        menuItemViewSplashScreen.setText("View Splash Screen");
        menuItemViewSplashScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewSplashScreenActionPerformed(evt);
            }
        });
        menuView.add(menuItemViewSplashScreen);

        menuBar.add(menuView);

        menuTools.setText("Tools");

        menuItemReloadData.setText("Reload Tab data");
        menuItemReloadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReloadDataActionPerformed(evt);
            }
        });
        menuTools.add(menuItemReloadData);

        menuItemReloadAllData.setText("Reload ALL data");
        menuItemReloadAllData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReloadAllDataActionPerformed(evt);
            }
        });
        menuTools.add(menuItemReloadAllData);

        menuItemTurnEditModeOff.setText("Turn Edit Mode OFF");
        menuItemTurnEditModeOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemTurnEditModeOffActionPerformed(evt);
            }
        });
        menuTools.add(menuItemTurnEditModeOff);

        menuItemMoveSeletedRowsToEnd.setText("Move Selected Rows To End");
        menuItemMoveSeletedRowsToEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemMoveSeletedRowsToEndActionPerformed(evt);
            }
        });
        menuTools.add(menuItemMoveSeletedRowsToEnd);

        menuItemCompIssues.setText("Compile Issue List");
        menuItemCompIssues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemCompIssuesActionPerformed(evt);
            }
        });
        menuTools.add(menuItemCompIssues);

        menuItemBackup.setText("Backup Tables");
        menuItemBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemBackupActionPerformed(evt);
            }
        });
        menuTools.add(menuItemBackup);

        menuBar.add(menuTools);

        menuHelp.setText("Help");
        menuHelp.setEnabled(false);

        menuItemRepBugSugg.setText("Report a bug/suggestion");
        menuItemRepBugSugg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRepBugSuggActionPerformed(evt);
            }
        });
        menuHelp.add(menuItemRepBugSugg);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(addPanel_control, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 929, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(addPanel_control, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemVersionActionPerformed

        JOptionPane.showMessageDialog(this, "Creation Date: "
                + CREATION_DATE + "\n"
                + "Version: " + VERSION);
    }//GEN-LAST:event_menuItemVersionActionPerformed

    private Map loadingDropdownList() {
        String selectedTabName = this.getSelectedTabName();
        Tab tab = tabs.get(selectedTabName);

        Map<Integer, ArrayList<Object>> valueListMap = new HashMap();
        if (!selectedTabName.equalsIgnoreCase("issue_files")) {
            for (String searchField : tab.getSearchFields()) {

                for (int i = 0; i < tab.getTable().getColumnCount(); i++) {
                    if (tab.getTable().getColumnName(i).equalsIgnoreCase(searchField)) {
                        valueListMap.put(i, new ArrayList<Object>());
                    }
                }
            }
            for (int col : valueListMap.keySet()) {
                //for each search item, create a new drop down list
                ArrayList DropDownListValueForEachColumn = new ArrayList<Object>();
                // load drop down for each table
                for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
                    if (!entry.getKey().equalsIgnoreCase("issue_files")) {
                        tab = tabs.get(entry.getKey());

                        String[] columnNames = tab.getTableColNames();
                        JTable table = tab.getTable();
                        TableModel tableModel = table.getModel();
                        String colName;
                        colName = columnNames[col].toLowerCase();

                        switch (colName) {
                            case "title":
                            case "description":
                            case "version":
                                DropDownListValueForEachColumn.add("");
                                break;
                            default:
                                Object valueAddToDropDownList;
                                for (int row = 0; row < tableModel.getRowCount(); row++) {
                                    valueAddToDropDownList = tableModel.getValueAt(row, col);

                                    if (valueAddToDropDownList != null) {
                                        // add to drop down list
                                        DropDownListValueForEachColumn.add(valueAddToDropDownList);
                                    } else {
                                        DropDownListValueForEachColumn.add("");
                                    }
                                }
                                break;
                        }
                    }
                }

                //make every item in drop down list unique
                Set<Object> uniqueValue = new HashSet<Object>(DropDownListValueForEachColumn);
                ArrayList uniqueList = new ArrayList<Object>(uniqueValue);
//                System.out.println(col + " " + uniqueList);
                valueListMap.put(col, uniqueList);
            }
        }
        return valueListMap;

    }

    private void updateComboList(String colName, String tableName) {
        //create a combo box model
        DefaultComboBoxModel comboBoxSearchModel = new DefaultComboBoxModel();
        comboBoxValue.setModel(comboBoxSearchModel);
        Map comboBoxForSearchValue = loadingDropdownList();

        JTable table = tabs.get(tableName).getTable();

        for (int col = 0; col < table.getColumnCount(); col++) {

            if (table.getColumnName(col).equalsIgnoreCase(colName)) {
                ArrayList<Object> dropDownList = (ArrayList<Object>) comboBoxForSearchValue.get(col);

                if (colName.equalsIgnoreCase("dateOpened") || colName.equalsIgnoreCase("dateClosed")) {
                    Collections.sort(dropDownList, new Comparator<Object>() {
                        public int compare(Object o1, Object o2) {
                            return o2.toString().compareTo(o1.toString());
                        }

                    });

                } else if (colName.equalsIgnoreCase("rk")) {
                    if (dropDownList.get(0) == "") {
                        ArrayList<Object> list = new ArrayList<Object>();

                        for (int i = 1; i < dropDownList.size(); i++) {
                            list.add(dropDownList.get(i));
                        }
                        list.add(dropDownList.get(0));

                        dropDownList = list;
                    }
                } else if (colName.equalsIgnoreCase("programmer")) {
                    Object nullValue = "";

                    Collections.sort(dropDownList, new Comparator<Object>() {
                        public int compare(Object o1, Object o2) {
                            if (o1 == nullValue && o2 == nullValue) {
                                return 0;
                            }

                            if (o1 == nullValue) {

                                return 1;
                            }

                            if (o2 == nullValue) {

                                return -1;
                            }

                            return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
                        }

                    });

                }
//                System.out.println(dropDownList);
                comboBoxStartToSearch = false;
                for (Object item : dropDownList) {

                    comboBoxSearchModel.addElement(item);

                }

            }
        }
//        comboBoxForSearch.setSelectedItem("Enter " + colName + " here");
//        comboBoxStartToSearch = true;
    }

    /**
     * This method is called when the search button is pressed
     *
     * @param evt
     */
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        filterBySearch();
    }//GEN-LAST:event_btnSearchActionPerformed

    /**
     * This method is performed when the text field is used to search by either
     * clicking the search button or the Enter key in the text field. This
     * method is called by the searchActionPerformed method and the
     * textForSearchKeyPressed method
     */
    public void filterBySearch() {
        String text = "";

        int count = 0;
        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();

            String searchColName = comboBoxField.getSelectedItem().toString();
            String searchBoxValue = comboBoxValue.getSelectedItem().toString();  // store string from text box

            // this matches the combobox newValue with the column name newValue to get the column index
            for (int col = 0; col < table.getColumnCount(); col++) {
                String tableColName = table.getColumnName(col);
                if (tableColName.equalsIgnoreCase(searchColName)) {

                    // add item to filter
                    TableFilter filter = tab.getFilter();
                    filter.clearAllFilters();
                    filter.applyFilter();

                    boolean isValueInTable = false;
                    isValueInTable = checkValueInTableCell(col, searchBoxValue, table);


                    filter.addCustomIdListToFilterItem(tab.getCustomIdList());
                    filter.addFilterItem(col, searchBoxValue);
                    filter.applyFilter();
                    if (!isValueInTable) {
                        count++;
                    }

                    // set label record information
                    String recordsLabel = tab.getRecordsLabel();
                    labelRecords.setText(recordsLabel);
                }
            }
            if (count == 4) {
                text = "There is no " + searchBoxValue
                        + " under " + searchColName + " in all tables";
            }
        }
        if (!text.equals("")) {
            LoggingAspect.afterReturn(text);

        }
    }

    private boolean checkValueInTableCell(int col, String target, JTable table) {
        int count = 0;
        for (int row = 0; row < table.getRowCount(); row++) {
            String cellValue = "";
            if (table.getValueAt(row, col) != null) {
                cellValue = table.getValueAt(row, col).toString();
            }

            if (cellValue.toLowerCase().contains(target.toLowerCase())) {

                count++;
            }
        }
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    // not sure what this is
    private void menuItemAWSAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAWSAssignActionPerformed

        Tab PMTable = tabs.get(tableNames[0]);
        Tab issue_filesTable = tabs.get(tableNames[4]);
        loadTable(PMTable);
        loadTable(issue_filesTable);
    }//GEN-LAST:event_menuItemAWSAssignActionPerformed
    /**
     * This method is performed when we click the upload changes button. it
     * uploads the changes and switch the edit mode off
     */
    private void btnUploadChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUploadChangesActionPerformed

        uploadChanges();
    }//GEN-LAST:event_btnUploadChangesActionPerformed

    /**
     * This uploads changes made by editing and saves the changes by uploading
     * them to the database. This method is called by:
     * btnUploadChangesActionPerformed(java.awt.event.ActionEvent evt) and also
     * a keylistener when editing mode is on and enter is pressed
     */
    public void uploadChanges() {
//        String tabName;
//        if (addIssueWindowShow) {
//            tabName = addIssueWindow.getIssueActiveTabName();
//        } else {
//            tabName = getSelectedTabName();
//        }
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        JTableCellRenderer cellRenderer = tab.getCellRenderer();
        ModifiedTableData data = tab.getTableData();

        boolean uploaded = updateTable(table, data.getNewData());
        if (uploaded) {

//            int[] rowsId = getSelectedRowsId(table);
            int[] rows = table.getSelectedRows();

            loadTable(tab); // refresh tableSelected

            ListSelectionModel model = table.getSelectionModel();
            model.clearSelection();
            for (int r = 0; r < rows.length; r++) {
                model.addSelectionInterval(rows[r], rows[r]);
            }

            // clear cellrenderer
            cellRenderer.clearCellRender();

            // reload modified tableSelected data with current tableSelected model
            data.reloadData();

            makeTableEditable(labelEditModeState.getText().equals("OFF") ? true : false);

            data.getNewData().clear();    // reset the arraylist to record future changes
            setLastUpdateTime();          // update time

            // no changes to upload or revert
            setEnabledEditingButtons(true, false, false);

        }
    }

    private int[] getSelectedRowsId(JTable table) {
        int[] rows = table.getSelectedRows();

        int[] selectedRowsId = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            selectedRowsId[i] = (int) table.getValueAt(rows[i], 0);
        }
        return selectedRowsId;
    }

    private void menuItemRepBugSuggActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRepBugSuggActionPerformed
//        reportWindow = new ReportWindow();
//        reportWindow.setLocationRelativeTo(this);
//        reportWindow.setVisible(true);
    }//GEN-LAST:event_menuItemRepBugSuggActionPerformed

    private void btnEnterSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterSQLActionPerformed

        int commandStart = jTextAreaSQL.getText().lastIndexOf(">>") + 2;
        String command = jTextAreaSQL.getText().substring(commandStart);
        if (command.toLowerCase().contains("select")) {
            loadTable(command, PMTable);
        } else {
            try {
                statement.executeUpdate(command);
            } catch (SQLException e) {
                LoggingAspect.afterThrown(e);
            } catch (Exception e) {
                LoggingAspect.afterThrown(e);
            }
        }
    }//GEN-LAST:event_btnEnterSQLActionPerformed

    /**
     * btnCancelSQLActionPerformed
     *
     * @param evt
     */
    private void btnCancelSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelSQLActionPerformed
        ((AbstractDocument) jTextAreaSQL.getDocument())
                .setDocumentFilter(new CreateDocumentFilter(0));
        jTextAreaSQL.setText("Please input an SQL statement:\n>>");
        ((AbstractDocument) jTextAreaSQL.getDocument())
                .setDocumentFilter(new CreateDocumentFilter(33));
    }//GEN-LAST:event_btnCancelSQLActionPerformed

    private void btnCloseSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseSQLActionPerformed

        jPanelSQL.setVisible(false);
        menuItemSQLCmdChkBx.setSelected(false);
         this.setSize(this.getWidth(), 560);
    }//GEN-LAST:event_btnCloseSQLActionPerformed

    /**
     * makeTableEditable Make tables editable or non editable
     *
     * @param makeTableEditable // takes boolean true or false to make editable
     */
    public void makeTableEditable(boolean makeTableEditable) {
        String selectedTabName = this.getSelectedTabName();
        Tab tab = tabs.get(selectedTabName);
        boolean isAddRecordsBtnVisible = tab.isAddRecordsBtnVisible();
        boolean isBatchEditBtnVisible = tab.isBatchEditBtnVisible();

        if (makeTableEditable) {
            tab.setEditing(true);
            labelEditModeState.setText("ON ");
//            btnSwitchEditMode.setVisible(true);
            btnAddIssue.setVisible(false);
//            btnBatchEdit.setVisible(true);
            if (!addIssueWindowShow) {
                btnUploadChanges.setVisible(true);
                btnRevertChanges.setVisible(true);
            }
            editModeTextColor(tab.isEditing());
        } else {
            tab.setEditing(false);
            labelEditModeState.setText("OFF");
//            btnSwitchEditMode.setVisible(true);
            btnUploadChanges.setVisible(false);
            btnAddIssue.setVisible(isAddRecordsBtnVisible);
//            btnBatchEdit.setVisible(isBatchEditBtnVisible);
            btnRevertChanges.setVisible(false);
            editModeTextColor(tab.isEditing());
        }

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
            EditableTableModel model = ((EditableTableModel) table.getModel());
            model.setCellEditable(makeTableEditable);
        }
    }

    /**
     * getEditMode on or off
     *
     */
    public boolean getEditMode() {
        String editState = labelEditModeState.getText();

        boolean editable = editState.equals("ON ");

        return editable;
    }

    /**
     * changeTabbedPanelState
     */
    private void changeTabbedPanelState() {

        // get selected tab
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);

        // get booleans for the states of the selected tab
//        boolean isActivateRecordMenuItemEnabled = tab.isActivateRecordMenuItemEnabled();
//        boolean isArchiveRecordMenuItemEnabled = tab.isArchiveRecordMenuItemEnabled();
        JTable table = tab.getTable();

        if (tabName.equals(TASKFILES_TABLE_NAME)) {
            String str = tabName;
            if (currentTabName.equals("PM") || currentTabName.equals("ELLEGUI") || currentTabName.equals("Analyster")) {
                str = str + " WHERE app = " + "'" + currentTabName + "'";
            } else if (currentTabName.equals("Other")) {
                str = str + " WHERE app != 'PM' and app != 'Analyster' "
                        + "and app != 'ELLEGUI' or app IS NULL";
            }

            // connection might time out
            DBConnection.close();
            DBConnection.open();

            statement = DBConnection.getStatement();
            String sql = "SELECT * FROM " + str + " ORDER BY taskId ASC";
//                System.out.println(sql);
            loadTable(sql, table);
        }
        currentTabName = getSelectedTabName();

        boolean isBatchEditBtnEnabled = tab.isBatchEditBtnEnabled();
        boolean isBatchEditWindowOpen = tab.isBatchEditWindowOpen();
        boolean isBatchEditWindowVisible = tab.isBatchEditWindowVisible();

        // this enables or disables the menu components for this tabName
//        menuItemActivateRecord.setEnabled(isActivateRecordMenuItemEnabled);
//        menuItemArchiveRecord.setEnabled(isArchiveRecordMenuItemEnabled);
        // batch edit button enabled is only allowed for table that is editing
        btnBatchEdit.setEnabled(isBatchEditBtnEnabled);
        if (isBatchEditWindowOpen) {
            batchEditWindow.setVisible(isBatchEditWindowVisible);
        }

        // must be instance of EditableTableModel 
        // this method is called from init componenents before the table model is set
        // check whether editing and display accordingly
        boolean editing = tab.isEditing();

        if (table.getModel() instanceof EditableTableModel) {
            makeTableEditable(editing);
        }

        // set the color of the edit mode text
        editModeTextColor(tab.isEditing());

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);

        // buttons if in edit mode
        if (labelEditModeState.getText().equals("ON ")) {
            btnAddIssue.setVisible(false);
            btnBatchEdit.setVisible(true);
        }

        // batch edit window visible only on the editing tab
        if (batchEditWindow != null) {
            boolean batchWindowVisible = tab.isBatchEditWindowVisible();
            batchEditWindow.setVisible(batchWindowVisible);
        }

        // if this tab is editing
        if (editing) {

            // if there is no modified data
            if (tab.getTableData().getNewData().isEmpty()) {
                setEnabledEditingButtons(true, false, false);
            } // there is modified data to upload or revert
            else {
                setEnabledEditingButtons(false, true, true);
            }

            // set edit mode label
            labelEditMode.setText("Edit Mode: ");
            labelEditModeState.setVisible(true);
            editModeTextColor(true);
        } // else if no tab is editing
        else if (!isTabEditing()) {
//            btnSwitchEditMode.setEnabled(true);
            btnAddIssue.setEnabled(true);
            btnBatchEdit.setEnabled(true);

            // set edit mode label
            labelEditMode.setText("Edit Mode: ");
            labelEditModeState.setVisible(true);

            editModeTextColor(false);
        } // else if there is a tab editing but it is not this one
        else if (isTabEditing()) {
//            btnSwitchEditMode.setEnabled(false);
            btnAddIssue.setEnabled(false);
            btnBatchEdit.setEnabled(false);

            // set edit mode label
            labelEditMode.setText("Editing " + getEditingTabName() + " ... ");
            labelEditModeState.setVisible(false);
            editModeTextColor(true);
        }
        if (!getSelectedTabName().equals(TASKFILES_TABLE_NAME)) {
            btnAddIssue.setText("Add issue to " + getSelectedTabName());
        } else {
            btnAddIssue.setText("Add " + getSelectedTabName());
        }

        // Authorization
        Authorization.authorize(this);
    }

    private void btnBatchEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchEditActionPerformed

        // get selected tab
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        int[] rows = table.getSelectedRows();
//        this.moveSelectedRowsToTheEnd(rows, table);

        // set the tab to editing
        makeTableEditable(true);

        // set the color of the edit mode text
        editModeTextColor(tab.isEditing());

        // open a batch edit window and make visible only to this tab
        batchEditWindow = new BatchEditWindow();
        batchEditWindow.setVisible(true);
        tab.setBatchEditWindowVisible(true);
        tab.setBatchEditWindowOpen(true);
        tab.setBatchEditBtnEnabled(false);

        setBatchEditButtonStates(tab);

        // show the batch edit window in front of the Main Window
        showWindowInFront(batchEditWindow);
    }//GEN-LAST:event_btnBatchEditActionPerformed

    private void menuItemManageDBsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemManageDBsActionPerformed

        editDatabaseWindow = new EditDatabaseWindow();
        editDatabaseWindow.setLocationRelativeTo(this);
        editDatabaseWindow.setVisible(true);

    }//GEN-LAST:event_menuItemManageDBsActionPerformed

    /**
     * btnAddRecordsActionPerformed
     *
     * @param evt
     */
    private void btnAddIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddIssueActionPerformed

        if (addIssueWindowShow) {
            addIssueWindow.toFront();
        } else {
            if (btnAddIssue.getText().contains("Add issue to")) {
                addIssueWindow = new IssueWindow(-1, this.getSelectedTable());
                addIssueWindow.setVisible(true);
                addIssueWindowShow = true;
            } 
        }

        // update records
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);
    }//GEN-LAST:event_btnAddIssueActionPerformed

    /**
     * jMenuItemLogOffActionPerformed Log Off menu item action performed
     *
     * @param evt
     */
    private void menuItemLogOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLogOffActionPerformed
        Object[] options = {"Reconnect", "Log Out"};  // the titles of buttons

        int n = JOptionPane.showOptionDialog(this, "Would you like to reconnect?", "Log off",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, //do not use a custom Icon
                options, //the titles of buttons
                options[0]); //default button title

        switch (n) {
            case 0: {               // Reconnect

                // create a new Login Window
                loginWindow = new LoginWindow();
                loginWindow.setLocationRelativeTo(this);
                loginWindow.setVisible(true);

                // dispose of this Object and return resources
                this.dispose();

                break;
            }
            case 1:
                System.exit(0); // Quit
        }
    }//GEN-LAST:event_menuItemLogOffActionPerformed

    /**
     * menuItemDeleteRecordActionPerformed Delete records menu item action
     * performed
     *
     * @param evt
     */
    private void menuItemDeleteRecordActionPerformed(java.awt.event.ActionEvent evt) {

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        String sqlDelete = deleteRecordsSelected(table);
        reloadData();
        table.getSelectionModel().clearSelection();

        String levelMessage = "3:" + sqlDelete;
        logWindow.addMessageWithDate(levelMessage);
        System.out.println(levelMessage);
//        logWindow.setLevel(deleteRecordLevel);
    }

    /**
     * btnClearAllFilterActionPerformed clear all filters
     *
     * @param evt
     */
    private void btnClearAllFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAllFilterActionPerformed
        String recordsLabel = "";
        // clear all filters
        //      String tabName = getSelectedTabName();
        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            TableFilter filter = tab.getFilter();
            filter.clearAllFilters();
            filter.applyFilter();
            filter.applyColorHeaders();
            recordsLabel = recordsLabel + tab.getRecordsLabel() + " \n";

        }

        labelRecords.setText(recordsLabel);
    }//GEN-LAST:event_btnClearAllFilterActionPerformed

    /**
     * jMenuItemOthersLoadDataActionPerformed
     *
     * @param evt
     */

    private void menuItemReloadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReloadDataActionPerformed

        reloadData();
        String searchColName = comboBoxField.getSelectedItem().toString();

        String tabName = getSelectedTabName();
        updateComboList(searchColName, tabName);

        LoggingAspect.afterReturn(tabName + " is reloading");


    }//GEN-LAST:event_menuItemReloadDataActionPerformed

    //reload the current tab data in table
    private void reloadData() {
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTableCellRenderer cellRenderer = tab.getCellRenderer();
        ModifiedTableData data = tab.getTableData();

        // reload tableSelected from database
        JTable table = tab.getTable();
        loadTable(tab);

        // clear cellrenderer
        cellRenderer.clearCellRender();

        // reload modified tableSelected data with current tableSelected model
        data.reloadData();

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);

    }

    private void reloadAllData() {

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTableCellRenderer cellRenderer = tab.getCellRenderer();
            ModifiedTableData data = tab.getTableData();

            // reload tableSelected from database
            JTable table = tab.getTable();
            loadTable(tab);

            // clear cellrenderer
            cellRenderer.clearCellRender();

            // reload modified tableSelected data with current tableSelected model
            data.reloadData();

            // set label record information
            String recordsLabel = tab.getRecordsLabel();
            labelRecords.setText(recordsLabel);
            LoggingAspect.afterReturn(tab.getTableName() + " is reloading");
        }

    }

////    /**
////     * jArchiveRecordActionPerformed
////     *
////     * @param evt
////     */
    private void menuItemArchiveRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemArchiveRecordActionPerformed

//        int rowSelected = issuesTable.getSelectedRows().length;
//        int[] rowsSelected = issuesTable.getSelectedRows();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        String today = dateFormat.format(date);
//
//        // Delete Selected Records from Assignments
//        if (rowSelected != -1) {
//            for (int i = 0; i < rowSelected; i++) {
//                String analyst = (String) issuesTable.getValueAt(rowsSelected[i], 2);
//                Integer selectedTask = (Integer) issuesTable.getValueAt(rowsSelected[i], 0); // Add Note to selected taskID
//                String sqlDelete = "UPDATE " + database + "." + issuesTable.getName() + " SET analyst = \"\",\n"
//                        + " priority=null,\n"
//                        + " dateAssigned= '" + today + "',"
//                        + " dateDone=null,\n"
//                        + " notes= \'Previous " + analyst + "' " + "where ID=" + selectedTask;
//                try {
//                    statement.executeUpdate(sqlDelete);
//                } catch (SQLException e) {
//                    LoggingAspect.afterThrown(ex);
//                }
//            }
//        } else {
//            JOptionPane.showMessageDialog(null, "Please, select one task!");
//        }
//        // Archive Selected Records in Assignments Archive
//        if (rowSelected != -1) {
//
//            for (int i = 0; i < rowSelected; i++) {
//                String sqlInsert = "INSERT INTO " + database + "." + issue_notesTable.getName() + " (symbol, analyst, priority, dateAssigned,dateDone,notes) VALUES (";
//                int numRow = rowsSelected[i];
//                for (int j = 1; j < issuesTable.getColumnCount() - 1; j++) {
//                    if (issuesTable.getValueAt(numRow, j) == null) {
//                        sqlInsert += null + ",";
//                    } else {
//                        sqlInsert += "'" + issuesTable.getValueAt(numRow, j) + "',";
//                    }
//                }
//                if (issuesTable.getValueAt(numRow, issuesTable.getColumnCount() - 1) == null) {
//                    sqlInsert += null + ")";
//                } else {
//                    sqlInsert += "'" + issuesTable.getValueAt(numRow, issuesTable.getColumnCount() - 1) + "')";
//                }
//                try {
//                    statement.executeUpdate(sqlInsert);
////                    logwind.addMessageWithDate(sqlInsert);
//                } catch (SQLException e) {
//                    LoggingAspect.afterThrown(ex);
//                }
//            }
//            loadTableData(issuesTable);
//            loadTableData(issue_notesTable);
//            issuesTable.setRowSelectionInterval(rowsSelected[0], rowsSelected[rowSelected - 1]);
//            JOptionPane.showMessageDialog(null, rowSelected + " Record(s) Archived!");
//
//        } else {
//            JOptionPane.showMessageDialog(null, "Please, select one task!");
//        }
    }//GEN-LAST:event_menuItemArchiveRecordActionPerformed

    /**
     * jActivateRecordActionPerformed
     *
     * @param evt
     */
    private void menuItemActivateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemActivateRecordActionPerformed

//        int rowSelected = issue_notesTable.getSelectedRows().length;
//        int[] rowsSelected = issue_notesTable.getSelectedRows();
//        // Archive Selected Records in Assignments Archive
//        if (rowSelected != -1) {
//
//            for (int i = 0; i < rowSelected; i++) {
//                String sqlInsert = "INSERT INTO " + database + "." + issuesTable.getName() + "(symbol, analyst, priority, dateAssigned,dateDone,notes) VALUES ( ";
//                int numRow = rowsSelected[i];
//                for (int j = 1; j < issue_notesTable.getColumnCount() - 1; j++) {
//                    if (issue_notesTable.getValueAt(numRow, j) == null) {
//                        sqlInsert += null + ",";
//                    } else {
//                        sqlInsert += "'" + issue_notesTable.getValueAt(numRow, j) + "',";
//                    }
//                }
//                if (issue_notesTable.getValueAt(numRow, issue_notesTable.getColumnCount() - 1) == null) {
//                    sqlInsert += null + ")";
//                } else {
//                    sqlInsert += "'" + issue_notesTable.getValueAt(numRow, issue_notesTable.getColumnCount() - 1) + "')";
//                }
//                try {
//                    statement.executeUpdate(sqlInsert);
////                    ana.getLogWindow().addMessageWithDate(sqlInsert);
//                } catch (SQLException e) {
//                    LoggingAspect.afterThrown(ex);
//                }
//            }
//
//            issue_notesTable.setRowSelectionInterval(rowsSelected[0], rowsSelected[0]);
//            loadTableData(issue_notesTable);
//            loadTableData(issuesTable);
//
//            JOptionPane.showMessageDialog(null, rowSelected + " Record(s) Activated!");
//
//        } else {
//            JOptionPane.showMessageDialog(null, "Please, select one task!");
//        }
    }//GEN-LAST:event_menuItemActivateRecordActionPerformed

    /**
     * jCheckBoxMenuItemViewLogActionPerformed
     *
     * @param evt
     */
    private void menuItemLogChkBxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLogChkBxActionPerformed

        if (menuItemLogChkBx.isSelected()) {

            logWindow.setLocationRelativeTo(this);
            logWindow.setVisible(true); // show log window

            // remove check if window is closed from the window
            logWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    menuItemLogChkBx.setSelected(false);
                }
            });
        } else {
            // hide log window
            logWindow.setVisible(false);
        }
    }//GEN-LAST:event_menuItemLogChkBxActionPerformed

    /**
     * jCheckBoxMenuItemViewSQLActionPerformed
     *
     * @param evt
     */
    private void menuItemSQLCmdChkBxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSQLCmdChkBxActionPerformed

        /**
         * ************* Strange behavior ************************* The
         * jPanelSQL.getHeight() is the height before the
         * jCheckBoxMenuItemViewSQLActionPerformed method was called.
         *
         * The jPanelSQL.setVisible() does not change the size of the sql panel
         * after it is executed.
         *
         * The jPanel size will only change after the
         * jCheckBoxMenuItemViewSQLActionPerformed is finished.
         *
         * That is why the the actual integer is used rather than getHeight().
         *
         * Example: jPanelSQL.setVisible(true); jPanelSQL.getHeight(); // this
         * returns 0
         */
        if (menuItemSQLCmdChkBx.isSelected()) {

            // show sql panel
            jPanelSQL.setVisible(true);
            this.setSize(this.getWidth(), 560 + 112);

        } else {

            // hide sql panel
            jPanelSQL.setVisible(false);
            this.setSize(this.getWidth(), 560);
        }
    }//GEN-LAST:event_menuItemSQLCmdChkBxActionPerformed

    private void btnRevertChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevertChangesActionPerformed

        revertChanges();

    }//GEN-LAST:event_btnRevertChangesActionPerformed

    private void menuitemViewOneIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuitemViewOneIssueActionPerformed

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();

        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();

        boolean isIssueAlreadyOpened = false;

        Integer openningIssueId = (Integer) table.getValueAt(row, 0);

        for (Integer id : openingIssuesList.keySet()) {
            if (openningIssueId != null) {
                if (id == openningIssueId) {
                    isIssueAlreadyOpened = true;
                }
            }
        }

        if (isIssueAlreadyOpened) {
            openingIssuesList.get(openningIssueId).toFront();
        } else {
            if (openingIssuesList.size() < 6) {
                IssueWindow viewIssue = new IssueWindow(row, table);
                openingIssuesList.put(openningIssueId, viewIssue);
                tabs.get(getSelectedTabName()).getCustomIdList().add(openningIssueId);
//                tab.getCustomIdList().printOutIDList();
//                                    System.out.println("issue opened's id is: " + openningIssueId + 
//                                            " and id in view issue is: " + selectedIssue.getID().toString());
                viewIssue.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null,
                        "The number of view issue window "
                        + "opened reached its maximum: 6!");

            }
        }
    }//GEN-LAST:event_menuitemViewOneIssueActionPerformed

    private void comboBoxFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxFieldActionPerformed
        System.out.println("here" + comboBoxStartToSearch);
        String searchColName = comboBoxField.getSelectedItem().toString();
        searchValue = comboBoxValue.getSelectedItem().toString();
        String tabName = getSelectedTabName();
        // update the dropdown list when we change a searchable item
        updateComboList(searchColName, tabName);

        comboBoxValue.setSelectedItem(searchValue);

        comboBoxStartToSearch = true;
//        System.out.println("there" + comboBoxStartToSearch);

    }//GEN-LAST:event_comboBoxFieldActionPerformed

    private void menuItemTurnEditModeOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemTurnEditModeOffActionPerformed
        String tabName = getSelectedTabName();
        makeTableEditable(false);
    }//GEN-LAST:event_menuItemTurnEditModeOffActionPerformed

    /**
     * tabbedPanelStateChanged
     *
     * @param evt
     */
    private void tabbedPanelStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPanelStateChanged

        changeTabbedPanelState();

        // this changes the search fields for the comboBox for each tabName
        // this event is fired from initCompnents hence the null condition
        String tabName = getSelectedTabName();

        Tab tab = tabs.get(tabName);
        String[] searchFields = tab.getSearchFields();

        if (searchFields != null) {
            comboBoxField.setModel(new DefaultComboBoxModel(searchFields));
        }
//        if (tabName.equalsIgnoreCase("issue_files")) {
//            comboBoxForSearch.setSelectedItem("Enter submitter here");
//        } else {
//            comboBoxForSearch.setSelectedItem("Enter programmer here");
//        }
    }//GEN-LAST:event_tabbedPanelStateChanged

    private void menuItemMoveSeletedRowsToEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemMoveSeletedRowsToEndActionPerformed
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        int[] rows = table.getSelectedRows();

        moveSelectedRowsToTheEnd(rows, table);
    }//GEN-LAST:event_menuItemMoveSeletedRowsToEndActionPerformed

    private void comboBoxValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxValueActionPerformed

        if (comboBoxStartToSearch) {
            String fieldToSearch = comboBoxField.getSelectedItem().toString().toLowerCase();
            switch (fieldToSearch) {
                case "programmer":
                case "dateopened":
                case "dateclosed":
                case "rk":
                    filterBySearch();
                    break;
                default:
                    break;
            }
        }


    }//GEN-LAST:event_comboBoxValueActionPerformed

    private void menuItemViewSplashScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewSplashScreenActionPerformed
        try {
            String fileName = FilePathFormat.supportFilePath() + "splashImage.png";
            ImageIcon img = new ImageIcon(ImageIO.read(new File(fileName)));
            JFrame splashScreenImage = new JFrame();
            JLabel image = new JLabel(img);
            splashScreenImage.add(image);
            splashScreenImage.pack();
            splashScreenImage.setLocationRelativeTo(this);
            splashScreenImage.setVisible(true);
            LoggingAspect.addLogMsgWthDate("3:" + "splash screen image show.");
        } catch (IOException ex) {
            LoggingAspect.addLogMsgWthDate("3:" + ex.getMessage());
            LoggingAspect.afterThrown(ex);
        }
    }//GEN-LAST:event_menuItemViewSplashScreenActionPerformed

    private void menuItemCompIssuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemCompIssuesActionPerformed
        CompIssuesListWindow frame = new CompIssuesListWindow();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }//GEN-LAST:event_menuItemCompIssuesActionPerformed

    private void menuItemBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemBackupActionPerformed

        // open new connection
        DBConnection.close(); // connection might be timed out on server
        if (DBConnection.open()) {  // open a new connection
            BackupDBTablesDialog backupDBTables = new BackupDBTablesDialog(DBConnection.getConnection(), this);
        } else {
            JOptionPane.showMessageDialog(this, "Could not connect to Database");
        }
    }//GEN-LAST:event_menuItemBackupActionPerformed

    private void menuItemReloadAllDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReloadAllDataActionPerformed
        reloadAllData();
        String searchColName = comboBoxField.getSelectedItem().toString();

        String tabName = getSelectedTabName();
        updateComboList(searchColName, tabName);
        LoggingAspect.afterReturn("All tabs reload complete");

    }//GEN-LAST:event_menuItemReloadAllDataActionPerformed

    public void comboBoxForSearchEditorMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            comboBoxValue.getEditor().selectAll();
        } else if (e.isControlDown()) {
            comboBoxValue.showPopup();
        }
    }

    public void moveSelectedRowsToTheEnd(int[] rows, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowNum = table.getRowCount();
        int count = 0;
        for (int row : rows) {
            row = row - count;

            model.moveRow(row, row, rowNum - 1);
            count++;
        }
        table.setRowSelectionInterval(rowNum - count, rowNum - 1);
    }
//    private void buttonFilteringTables(JTable table, String str) {
//
//        try {
//            // open connection because might time out
//            DBConnection.open();
//            statement = DBConnection.getStatement();
//            String sql = "SELECT * FROM " + table.getName() + str + " ORDER BY "
//                    + "case when dateClosed IS null then 1 else 0 end, dateClosed asc, taskID ASC";
//            System.out.println(sql);
//            loadTableData(sql, table);
//
//        } catch (SQLException ex) {
//            // for debugging
//            LoggingAspect.afterThrown(ex);
//
//            // notify the user that there was an issue
//            JOptionPane.showMessageDialog(this, "connection failed");
//        }
//        String tabName = getSelectedTabName();
//        Tab tab = tabs.get(tabName);
//        JTable tableFiltered = tab.getTable();
//        tab.setTotalRecords(tableFiltered.getRowCount());
//        String recordsLabel = tab.getRecordsLabel();
//        labelRecords.setText(recordsLabel);
//
//    }

    /**
     * loadData
     */
    public void loadData() {
        loadTables(tabs);
    }

//    public void viewNextIssue(int rowIndex, String columnName, JTable selectedTable) {
//
//        Object[] cellsValue = new Object[selectedTable.getColumnCount()];
//        for (int col = 1; col < selectedTable.getColumnCount(); col++) {
//            cellsValue[col - 1] = selectedTable.getValueAt(rowIndex, col);
//        }
//        boolean alreadyOpened = false;
//
//        Integer openningIssue = (Integer) selectedTable.getValueAt(rowIndex, 0);
//
//        String value = "openning issues are: ";
//        for (int issue : idNumOfOpenningIssues) {
//            value = value + " " + issue;
//            if (issue == openningIssue) {
//                alreadyOpened = true;
//            }
//        }
//
//        if (alreadyOpened) {
//            addIssueWindow.toFront();
//
//        } else {
//            idNumOfOpenningIssues.add(openningIssue);
//            addIssueWindow.setId(openningIssue);
//            addIssueWindow.setFormValue(cellsValue);
//        }
//        makeTableEditable(false);
//
//    }
//    public ArrayList<Integer> getIdNumOfOpenningIssues() {
//        return this.idNumOfOpenningIssues;
//    }
//    public void deleteFromIdNumOfOpenningIssues(int rowNum, JTable table) {
//        int id = (Integer) table.getValueAt(rowNum, 0);
//
//        for (Integer IssueId : this.idNumOfOpenningIssues) {
//            if (IssueId == id) {
//                this.idNumOfOpenningIssues.remove(IssueId);
//                break;
//            }
//        }
//    }
    /**
     * setTableListeners This adds mouselisteners and keylisteners to tables.
     *
     * @param table
     * @param frame
     */
    public void setTableListeners(final JTable table, final JFrame frame) {

        // this adds a mouselistener to the tableSelected header
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if (e.getClickCount() == 2) {
                        clearFilterDoubleClick(e, table);
                    }
//                    if (e.getClickCount() == 1) {
//                        for (int j = 0; j < table.getColumnCount(); j++) {
//                            // Locate columns under "ID"
//                            String columnName = table.getColumnName(j);
//                            if (columnName.equalsIgnoreCase("ID")) {
//                                ArrayList<String> ID = new ArrayList<>();
//
//                                for (int rowIndex = 0; rowIndex < table.getModel().getRowCount(); rowIndex++) {
//
//                                    String Value = table.getModel().getValueAt(rowIndex, 0).toString();
//
//                                    ID.add(Value);
//                                }
//                                TableRowSorter<DefaultTableModel> RowSorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
//                                RowSorter.setComparator(0, new Comparator<String>() {
//
//                                    @Override
//                                    public int compare(String o1, String o2) {
//                                        return Integer.parseInt(o1) - Integer.parseInt(o2);
//                                    }
//
//                                });
//
//                            }
//                        }
//                    }
//
//
//                    for (int j = 0; j < table.getColumnCount(); j++) {
//                        // Locate columns under "ID"
//                        String columnName = table.getColumnName(j);
//                        if (e.getClickCount() == 1 && columnName.equalsIgnoreCase("ID")) {
//                            ArrayList<String> ID = new ArrayList<>();
//
//                            for (int rowIndex = 0; rowIndex < table.getModel().getRowCount(); rowIndex++) {
//
//                                String Value = table.getModel().getValueAt(rowIndex, 0).toString();
//
//                                ID.add(Value);
//                            }
//                            TableRowSorter<DefaultTableModel> rowSorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
//                            rowSorter.setComparator(0, new Comparator<String>() {
//
//                                @Override
//                                public int compare(String o1, String o2) {
//                                    return Integer.parseInt(o1) - Integer.parseInt(o2);
//                                }
//
//                            });
//
//                        }
//                    }
                }

                /**
                 * Popup menus are triggered differently on different platforms
                 * Therefore, isPopupTrigger should be checked in both
                 * mousePressed and mouseReleased events for proper
                 * cross-platform functionality.
                 *
                 * @param e
                 */
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        // this calls the column popup menu
                        tabs.get(table.getName())
                                .getColumnPopupMenu().showPopupMenu(e);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        // this calls the column popup menu
                        tabs.get(table.getName())
                                .getColumnPopupMenu().showPopupMenu(e);
                    }
                }
            }
            );
        }

        // add mouselistener to the tableSelected
        table.addMouseListener(new MouseAdapter() {

            boolean isClickOnce, doubleClick;
            long theFirstClick = 0, theSecondClick = 0;

            @Override
            public void mouseClicked(MouseEvent e) {

                // if left mouse clicks
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        if (e.isControlDown()) {
                            filterByDoubleClick(table);
                        } else {
                            if (e.getComponent() instanceof JTable) {
//                                System.out.println("clicked!");
                                int row = table.getSelectedRow();
                                int col = table.getSelectedColumn();

                                boolean isIssueAlreadyOpened = false;

                                Integer openningIssueId = (Integer) table.getValueAt(row, 0);

                                for (Integer id : openingIssuesList.keySet()) {
                                    if (openningIssueId != null) {
                                        if (id == openningIssueId) {
                                            isIssueAlreadyOpened = true;
                                        }
                                    }
                                }

                                if (isIssueAlreadyOpened) {
                                    openingIssuesList.get(openningIssueId).toFront();
                                } else {
                                    if (openingIssuesList.size() < 6) {
                                        IssueWindow viewIssue = new IssueWindow(row, table);
                                        openingIssuesList.put(openningIssueId, viewIssue);
                                        tabs.get(getSelectedTabName()).getCustomIdList().add(openningIssueId);
//                                        tabs.get(getSelectedTabName()).getCustomIdList().printOutIDList();
//                                    System.out.println("issue opened's id is: " + openningIssueId + 
//                                            " and id in view issue is: " + selectedIssue.getID().toString());
                                        viewIssue.setVisible(true);
                                    } else {
                                        JOptionPane.showMessageDialog(null,
                                                "The number of view issue window "
                                                + "opened reached its maximum: 6!");

                                    }
                                }
                            }
                        }
                    } else if (e.getClickCount() == 1) {
                        if (e.getComponent() instanceof JTable) {
                            JTable table = (JTable) e.getSource();
                            int[] rows = table.getSelectedRows();

                            if (rows.length >= 2) {

                                btnBatchEdit.setEnabled(true);
                                btnBatchEdit.setVisible(true);
                            } else {
                                btnBatchEdit.setEnabled(false);
                                btnBatchEdit.setVisible(false);
                            }
                        }
                    }
                } // end if left mouse clicks
                // if right mouse clicks
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (e.getClickCount() == 2) {

                        Tab tab = tabs.get(table.getName());

                        // check if this tab is editing or if allowed editing
                        boolean thisTabIsEditing = tab.isEditing();
                        boolean noTabIsEditing = !isTabEditing();

                        if (thisTabIsEditing || noTabIsEditing) {

                            // set the states for this tab
                            makeTableEditable(true);
                            setEnabledEditingButtons(true, true, true);
                            setBatchEditButtonStates(tab);

                            // set the color of the edit mode text
                            editModeTextColor(tab.isEditing());

                            // get selected cell for editing
                            int columnIndex = table.columnAtPoint(e.getPoint()); // this returns the column index
                            int rowIndex = table.rowAtPoint(e.getPoint()); // this returns the rowIndex index
                            if (rowIndex != -1 && columnIndex != -1) {

                                // make it the active editing cell
                                table.changeSelection(rowIndex, columnIndex, false, false);

                                selectAllText(e);

                                // if cell is being edited
                                // cannot cancel or upload or revert
                                setEnabledEditingButtons(false, false, false);

                            } // end not null condition

                        } // end of is tab editing conditions

                    } // end if 2 clicks 
                } // end if right mouse clicks

            }// end mouseClicked

            private void selectAllText(MouseEvent e) {// Select all text inside jTextField

                JTable table = (JTable) e.getComponent();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if (column != 0) {
                    table.getComponentAt(row, column).requestFocus();
                    table.editCellAt(row, column);
                    JTextField selectCom = (JTextField) table.getEditorComponent();
                    if (selectCom != null) {
                        selectCom.requestFocusInWindow();
                        selectCom.selectAll();
                    }
                }

            }
            long firstClick = 0;
            long secondClick = 0;
            boolean click = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (labelEditModeState.getText().equals("ON ")) {
                    if (click == false) {
                        firstClick = new Date().getTime();
                        click = true;
                    } else if (click == true) {
                        secondClick = new Date().getTime();
                        click = false;
                    }
                    if (Math.abs((secondClick - firstClick)) < 200 && (secondClick - firstClick) > 30) {
                        selectAllText(e);
                    }
                }
            }
        }
        );

        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getComponent() instanceof JTable) {
                    JTable table = (JTable) e.getSource();
                    int[] rows = table.getSelectedRows();
                    if (rows.length >= 2) {
//                        loadTableWhenSelectedRows(rows, table);
                        btnBatchEdit.setEnabled(true);
                        btnBatchEdit.setVisible(true);
                    } else {
                        btnBatchEdit.setEnabled(false);
                        btnBatchEdit.setVisible(false);
                    }
                }
            }
        });

        // add tableSelected model listener
        table.getModel().addTableModelListener(new TableModelListener() {  // add tableSelected model listener every time the tableSelected model reloaded
            @Override
            public void tableChanged(TableModelEvent e) {

                System.out.println("table changed called");
                int row = e.getFirstRow();
                int col = e.getColumn();
                String tab = table.getName();

                ModifiedTableData data = tabs.get(tab).getTableData();

                if (col != -1) {
                    Object oldValue = data.getOldData()[row][col];
                    Object newValue = table.getModel().getValueAt(row, col);

                    // check that data is different
                    if (!newValue.equals(oldValue)) {

                        String tableName = table.getName();
                        String columnName = table.getColumnName(col);
                        int id = (Integer) table.getModel().getValueAt(row, 0);

                        data.getNewData().add(new ModifiedData(tableName, columnName, newValue, id));

                        // color the cell
                        JTableCellRenderer cellRender = tabs.get(tab).getCellRenderer();
                        cellRender.getCells().get(col).add(row);
                        table.getColumnModel().getColumn(col).setCellRenderer(cellRender);

                        // can upload or revert changes
                        setEnabledEditingButtons(false, true, true);
                    } // if modified data then cancel button not enabled
                    else if (!data.getNewData().isEmpty()) {
                        // can upload or revert changes
                        setEnabledEditingButtons(false, true, true);
                    } // there is no new modified data
                    else {
                        // no changes to upload or revert (these options disabled)
                        setEnabledEditingButtons(true, false, false);
                    }
                    int rows[] = table.getSelectedRows();
                    if (rows.length >= 2) {
                        btnBatchEdit.setEnabled(true);
                        btnBatchEdit.setVisible(true);
                    } else {
                        btnBatchEdit.setEnabled(false);
                        btnBatchEdit.setVisible(false);
                    }
                }
//                }
            }
        });

        // add keyListener to the tableSelected
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_F2) {

                    // I believe this is meant to toggle edit mode
                    // so I passed the conditional
                    makeTableEditable(labelEditModeState.getText().equals("ON ") ? false : true);
                }
            }
        });

    }
    //    private void popupWindowShowInTableByDiffTitle(JTable selectedTable) {
    //        int selectedColumn = selectedTable.getSelectedColumn();
    //
    //        if (selectedTable.getName().equals(TASKS_TABLE_NAME)) {
    //            if (selectedTable.getColumnName(selectedColumn).equals("title")
    //                    || selectedTable.getColumnName(selectedColumn).equals("description")) {
    //                tableCellPopupWindow = new PopupWindowInTableCell(this, selectedTable);
    ////                tableCellPopupWindow.showWindow();
    ////                //to check it is edit mode or not in project manager
    ////                //or in add records window it directly into edit mode
    ////                tableCellPopupWindow.editModeSwich();
    ////            } else {
    ////                tableCellPopupWindow.windowClose();
    //            }
    //        } else if (selectedTable.getName().equals(TASKFILES_TABLE_NAME)) {
    //            if (selectedTable.getColumnName(selectedColumn).equals("files")
    //                    || selectedTable.getColumnName(selectedColumn).equals("notes")
    //                    || selectedTable.getColumnName(selectedColumn).equals("path")) {
    //
    //                tableCellPopupWindow = new PopupWindowInTableCell(this, selectedTable);
    //
    ////                // popup table cell edit window
    ////                tableCellPopupWindow.showWindow();
    ////                //to check it is edit mode or not in project manager
    ////                //or in add records window it directly into edit mode
    ////                tableCellPopupWindow.editModeSwich();
    ////            } else {
    ////                tableCellPopupWindow.windowClose();
    //            }
    //        } else if (selectedTable.getName().equals(TASKNOTES_TABLE_NAME)) {
    //            if (selectedTable.getColumnName(selectedColumn).equals("status_notes")) {
    //
    //                tableCellPopupWindow = new PopupWindowInTableCell(this, selectedTable);
    ////                // popup table cell edit window
    ////                tableCellPopupWindow.showWindow();
    ////                //to check it is edit mode or not in project manager
    ////                //or in add records window it directly into edit mode
    ////                tableCellPopupWindow.editModeSwich();
    ////            } else {
    ////                tableCellPopupWindow.windowClose();
    //            }
    //        }
    //    }

    public void loadTableWhenSelectedRows(int[] selectedRows, JTable table) {
        String str = table.getName();

        if (str == "PM" || str == "ELLEGUI" || str == "Analyster") {

            str = TASKS_TABLE_NAME + " WHERE app = " + "'" + str + "'";
        } else if (str == "Other") {
            str = TASKS_TABLE_NAME + " WHERE app != 'PM' and app != 'Analyster' "
                    + "and app != 'ELLEGUI' or app IS NULL";
        }

        str = str + " ORDER BY CASE";

        for (int row = 0; row < selectedRows.length; row++) {
            str = str + " WHEN taskID = " + table.getValueAt(selectedRows[row], 0) + " THEN " + (selectedRows.length + 1 - row);
        }
        str = str + " ELSE ";

        // connection might time out
        DBConnection.close();
        DBConnection.open();
        statement = DBConnection.getStatement();
        String sql;
        if (!table.getName().equals(TASKFILES_TABLE_NAME)) {
            sql = "SELECT * FROM " + str
                    + "CASE WHEN dateClosed IS NULL THEN 1 ELSE 0 END END, dateClosed asc, taskID ASC";
        } else {
            sql = "SELECT * FROM " + str + "0 END, taskId ASC";
        }
        loadTable(sql, table);
        LoggingAspect.afterReturn("Selected Rows Move to the End...");
//        informationLabel.setText("Selected Rows Move to the End...");
//        startCountDownFromNow(10);

        ListSelectionModel model = table.getSelectionModel();
        model.clearSelection();
        for (int r = 0; r < selectedRows.length; r++) {
            model.addSelectionInterval((table.getRowCount() - 1 - r), (table.getRowCount() - 1 - r));
        }

    }

    /**
     * setTableListeners This method overloads the seTerminalFunctions to take
     * tabs instead of a single tableSelected
     *
     * @param tabs
     * @return
     */
    public Map<String, Tab> setTableListeners(Map<String, Tab> tabs) {

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            setTableListeners((Map<String, Tab>) tabs.get(entry.getKey()).getTable());
        }
        return tabs;
    }

    /**
     * filterByDoubleClick this selects the item double clicked on to be
     * filtered
     *
     * @param table
     */
    public void filterByDoubleClick(JTable table) {

        int columnIndex = table.getSelectedColumn(); // this returns the column index
        int rowIndex = table.getSelectedRow(); // this returns the rowIndex index
        if (rowIndex != -1) {
            Object selectedField = table.getValueAt(rowIndex, columnIndex);
            String tabName = getSelectedTabName();
            Tab tab = tabs.get(tabName);
            TableFilter filter = tab.getFilter();

//            tab.getCustomIdList().printOutIDList();
            filter.addFilterItem(columnIndex, selectedField);
            filter.addCustomIdListToFilterItem(tab.getCustomIdList());
            filter.applyFilter();
            String recordsLabel = tab.getRecordsLabel();
            labelRecords.setText(recordsLabel);
        }
    }

    /**
     * clearFilterDoubleClick This clears the filters for that column by double
     * clicking on that column header.
     */
    private void clearFilterDoubleClick(MouseEvent e, JTable table) {

        int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        TableFilter filter = tab.getFilter();
        filter.clearColFilter(columnIndex);
        filter.applyFilter();

        // update records label
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);
    }

    /**
     * setColumnFormat sets column format for each tableSelected
     *
     * @param width
     * @param table
     */
    public void setColumnFormat(float[] width, JTable table) {

        // Center column content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        //LEFT column content
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        //Center column header
        JTableHeader header = table.getTableHeader();
        if (!(header.getDefaultRenderer() instanceof AlignmentTableHeaderCellRenderer)) {
            header.setDefaultRenderer(new AlignmentTableHeaderCellRenderer(header.getDefaultRenderer()));
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        switch (table.getName()) {

            // Set the format for tableSelected task.
            case "PM": {
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i == 2 || i == 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    
                    // hide lock and submitter columns
                    if(i == 10 || i == 11){
                        TableColumn column = table.getColumnModel().getColumn(i);
                        column.setMinWidth(pWidth);
                        column.setMaxWidth(pWidth);
                        column.setPreferredWidth(pWidth);
                    }
                }
                break;
            }
            case "ELLEGUI": {
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i == 2 || i == 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    
                    // hide lock and submitter columns
                    if(i == 10 || i == 11){
                        TableColumn column = table.getColumnModel().getColumn(i);
                        column.setMinWidth(pWidth);
                        column.setMaxWidth(pWidth);
                        column.setPreferredWidth(pWidth);
                    }
                }
                break;
            }
            case "Analyster": {
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i == 2 || i == 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    
                    // hide lock and submitter columns
                    if(i == 10 || i == 11){
                        TableColumn column = table.getColumnModel().getColumn(i);
                        column.setMinWidth(pWidth);
                        column.setMaxWidth(pWidth);
                        column.setPreferredWidth(pWidth);
                    }
                }
                break;
            }
            case "Other": {
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i == 2 || i == 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    
                    // hide lock and submitter columns
                    if(i == 10 || i == 11){
                        TableColumn column = table.getColumnModel().getColumn(i);
                        column.setMinWidth(pWidth);
                        column.setMaxWidth(pWidth);
                        column.setPreferredWidth(pWidth);
                    }
                }
                break;
            }

            // Set the format for tableSelected task_files
            case TASKFILES_TABLE_NAME: {
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i >= width.length - 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                }
                break;
            }
            default: {
                System.out.println("Load table errer!");
                break;
            }
        }
    }

    /**
     * updateTable Updates database with edited data This is called from batch
     * edit & uploadChanges
     *
     * @param table
     * @param modifiedDataList
     */
    public boolean updateTable(JTable table, List<ModifiedData> modifiedDataList) {
        boolean updateSuccessful = true;
        // should probably not be here
        // this method is to update the database, that is all it should do.
        table.getModel().addTableModelListener(table);

        //String uploadQuery = uploadRecord(tableSelected, modifiedDataList);
        String sqlChange = null;

        DBConnection.close();
        if (DBConnection.open()) {
            statement = DBConnection.getStatement();
            for (ModifiedData modifiedData : modifiedDataList) {

                String tableName = modifiedData.getTableName();
                String columnName = modifiedData.getColumnName();
                Object value = modifiedData.getValue();
                int id = modifiedData.getId();

                value = processCellValue(value);
                if (!tableName.equals(TASKFILES_TABLE_NAME)) {
                    tableName = TASKS_TABLE_NAME;
                }

                try {

                    if (value.equals("")) {
                        value = null;
                        sqlChange = "UPDATE " + tableName + " SET " + columnName
                                + " = " + value + " WHERE ID = " + id + ";";
                    } else {
                        sqlChange = "UPDATE " + tableName + " SET " + columnName
                                + " = '" + value + "' WHERE ID = " + id + ";";
                    }

                    statement.executeUpdate(sqlChange);
                    LoggingAspect.afterReturn(sqlChange);

                } catch (SQLException e) {
                    LoggingAspect.addLogMsgWthDate("3:" + e.getMessage());
                    LoggingAspect.addLogMsgWthDate("3:" + e.getSQLState() + "\n");
                    LoggingAspect.addLogMsgWthDate(("Upload failed! " + e.getMessage()));
                    LoggingAspect.afterThrown(e);
                    updateSuccessful = false;
                }
            }
            if (updateSuccessful) {
                LoggingAspect.afterReturn(("Edits uploaded successfully!"));
            }
        } else {
            // connection failed
            LoggingAspect.afterReturn("Failed to connect");
        }
        // finally close connection
        DBConnection.close();
        return updateSuccessful;
    }

    /**
     * getSelectedTable gets the selected tabName
     *
     * @return
     */
    public JTable getSelectedTable() {  //get JTable by  selected Tab
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        return table;
    }

    /**
     * get Opening Issues list
     *
     * @return openingIssuesList
     */
    public Map getOpenningIssuesList() {
        return openingIssuesList;
    }

    public IssueWindow getViewIssueWindowOf(String id) {
        return this.openingIssuesList.get(id);
    }

    private Object processCellValue(Object cellValue) {
        return cellValue.toString().replaceAll("'", "''");
    }

    /**
     * setLastUpdateTime sets the last update time label
     */
    public void setLastUpdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        labelTimeLastUpdate.setText("Last updated: " + time);
    }

    /**
     * setKeyboardFocusManager sets the keyboard focus manager
     */
    private void setKeyboardFocusManager(JFrame frame) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_TAB) {
                            if (e.isAltDown()) {
//                        if (e.getComponent() instanceof JTable) {
//                            JTable table = (JTable) e.getComponent();
                                Tab tab = tabs.get(getSelectedTabName());
                                JTable table = tab.getTable();

                                if (table.isEditing()) {
                                    table.getCellEditor().stopCellEditing();
                                }
                                setEnabledEditingButtons(true, true, true);

                            } else {
                                if (labelEditModeState.getText().equals("ON ")) {
                                    if (e.getComponent() instanceof JTable) {
                                        JTable table = (JTable) e.getComponent();
                                        int row = table.getSelectedRow();
                                        int column = table.getSelectedColumn();
                                        if (column == table.getRowCount() || column == 0) {
                                            return false;
                                        } else {
                                            table.getComponentAt(row, column).requestFocus();
                                            table.editCellAt(row, column);
                                            JTextField selectCom = (JTextField) table.getEditorComponent();
                                            selectCom.requestFocusInWindow();
                                            selectCom.selectAll();
                                        }

                                        // if table cell is editing 
                                        // then the editing buttons should not be enabled
                                        if (table.isEditing()) {
                                            setEnabledEditingButtons(false, false, false);
                                        }
                                    }
                                }
                            }

                        } else if (!isBatchEditWindowShow && !addIssueWindowShow) {
                            if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {

                                if (labelEditModeState.getText().equals("ON ")) {                       // Default Date input with today's date
//                        && !batchEditWindow.isBatchEditWindowShow()
                                    JTable table = (JTable) e.getComponent().getParent();
                                    int column = table.getSelectedColumn();
                                    if (table.getColumnName(column).toLowerCase().contains("date")) {
                                        if (e.getID() != 401) { // 401 = key down, 402 = key released
                                            return false;
                                        } else {
                                            JTextField selectCom = (JTextField) e.getComponent();
                                            selectCom.requestFocusInWindow();
                                            selectCom.selectAll();
                                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            Date date = new Date();
                                            String today = dateFormat.format(date);
                                            selectCom.setText(today);
                                        }
                                    }
                                }
                            }
                        } else if ((e.getKeyCode() == KeyEvent.VK_ENTER)) {
                            if (e.getComponent() instanceof JTable) {
                                JTable table = (JTable) e.getComponent();

                                // make sure in editing mode
                                if (labelEditModeState.getText().equals("ON ")
                                && !table.isEditing()
                                && e.getID() == KeyEvent.KEY_PRESSED) {

                                    // only show popup if there are changes to upload or revert
                                    if (btnUploadChanges.isEnabled() || btnRevertChanges.isEnabled()) {
                                        // if finished display dialog box
                                        // Upload Changes? Yes or No?
                                        Object[] options = {"Commit", "Revert"};  // the titles of buttons

                                        // store selected rowIndex before the table is refreshed
                                        int rowIndex = table.getSelectedRow();

                                        int selectedOption = JOptionPane.showOptionDialog(ProjectManagerWindow.getInstance(),
                                                "Would you like to upload changes?", "Upload Changes",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE,
                                                null, //do not use a custom Icon
                                                options, //the titles of buttons
                                                options[0]); //default button title

                                        switch (selectedOption) {
                                            case 0:
                                                // if Commit, upload changes and return to editing
                                                uploadChanges();  // upload changes to database
                                                break;
                                            case 1:
                                                // if Revert, revert changes
                                                revertChanges(); // reverts the model back
                                                break;
                                            default:
                                                // do nothing -> cancel
                                                break;
                                        }

                                        // highlight previously selected rowIndex
                                        if (rowIndex != -1) {
                                            table.setRowSelectionInterval(rowIndex, rowIndex);
                                        }
                                    }
                                }

                            }

                        }

//                if (!addRecordWindowShow) {
//                if (e.getKeyCode() == KeyEvent.VK_TAB
//                        || e.getKeyCode() == KeyEvent.VK_LEFT
//                        || e.getKeyCode() == KeyEvent.VK_RIGHT
//                        || e.getKeyCode() == KeyEvent.VK_UP
//                        || e.getKeyCode() == KeyEvent.VK_DOWN) {
//
//                    if (e.getComponent() instanceof JTable) {
//
//                        JTable tableSelected = (JTable) e.getComponent();
//
//                        if (e.getID() == KeyEvent.KEY_RELEASED) {
////                            System.out.println(popupWindowShowInPM);
//                            if (popupWindowShowInPM) {
//                                tableCellPopupWindow.windowClose();
//                            }
//
//                            popupWindowShowInTableByDiffTitle(tableSelected);
////                            if table get selected location is not the same as last selection
////                            if (tableSelected.getSelectedRow() != lastSelectedRow
////                                    || tableSelected.getSelectedColumn() != lastSelectedColumn) {
////
////                                if (lastSelectedRow == -1 || lastSelectedColumn == -1) {
////                                    lastSelectedRow = tableSelected.getSelectedRow();
////                                    lastSelectedColumn = tableSelected.getSelectedColumn();
////                                    tableCellPopupWindow = new PopupWindowInTableCell(frame, tableSelected);
////                                    popupWindowShowInPM = tableCellPopupWindow.isPopupWindowShow();
////                                } else {
////                                    tableCellPopupWindow.windowClose();
////                                    tableCellPopupWindow = new PopupWindowInTableCell(frame, tableSelected);
////                                    lastSelectedRow = tableSelected.getSelectedRow();
////                                    lastSelectedColumn = tableSelected.getSelectedColumn();
////                                    popupWindowShowInPM = tableCellPopupWindow.isPopupWindowShow();
////                                }// last popup window dispose and new popup window show at the selected cell
////                            } else {
////                                //if current selection equals last selection nothing happens
////                            }
//                            tableSelected.requestFocus();
//
////                            }
//                        }
//                    }
//
//                }
                        if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_V) {
                            if (e.getComponent() instanceof JTable) {

                                String tabName = getSelectedTabName();
                                Tab tab = tabs.get(tabName);
                                JTable table = tab.getTable();

                                int row = table.getSelectedRow();
                                int col = table.getSelectedColumn();

                                boolean isIssueAlreadyOpened = false;

                                Integer openningIssueId = (Integer) table.getValueAt(row, 0);

                                for (Integer id : openingIssuesList.keySet()) {
                                    if (openningIssueId != null) {
                                        if (id == openningIssueId) {
                                            isIssueAlreadyOpened = true;
                                        }
                                    }
                                }

                                if (isIssueAlreadyOpened) {
                                    openingIssuesList.get(openningIssueId).toFront();
                                } else {
                                    if (openingIssuesList.size() < 6) {
                                        IssueWindow viewIssue = new IssueWindow(row, table);
                                        openingIssuesList.put(openningIssueId, viewIssue);
                                        tabs.get(getSelectedTabName()).getCustomIdList().add(openningIssueId);
//                                        tab.getCustomIdList().printOutIDList();
//                                    System.out.println("issue opened's id is: " + openningIssueId + 
//                                            " and id in view issue is: " + selectedIssue.getID().toString());
                                        viewIssue.setVisible(true);
                                    } else {
                                        JOptionPane.showMessageDialog(null,
                                                "The number of view issue window "
                                                + "opened reached its maximum: 6!");

                                    }
                                }
                            }
                        }
                        return false;
                    }
                });
    }

    public void setDisableProjecetManagerFunction(boolean f) {
        setEnableProjectManagerFunction(f);
    }

    private void setEnableProjectManagerFunction(boolean disable) {

        tabbedPanel.setEnabled(disable);
        btnAddIssue.setEnabled(disable);
        btnBatchEdit.setEnabled(disable);
        btnClearAllFilter.setEnabled(disable);
        btnSearch.setEnabled(disable);
        btnUploadChanges.setEnabled(disable);
        btnRevertChanges.setEnabled(disable);
        comboBoxField.setEnabled(disable);
        menuEdit.setEnabled(disable);
        menuFile.setEnabled(disable);
        menuFind.setEnabled(disable);
        menuHelp.setEnabled(disable);
        menuView.setEnabled(disable);
        menuTools.setEnabled(disable);
        menuReports.setEnabled(disable);
        searchPanel.setEnabled(disable);

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        tab.getTable().setEnabled(disable);
        if (!disable) {
            //set sort and filter enabled
            TableFilter filter = tab.getFilter();
            for (int i = 0; i < tab.getTable().getColumnCount(); i++) {
                filter.getSorter().setSortable(i, disable);
            }
        }
    }

    public static ProjectManagerWindow getInstance() {
        return instance;
    }

    public JLabel getRecordsLabel() {
        return labelRecords;
    }

    public LogWindow getLogWindow() {
        return logWindow;
    }

    public Map<String, Tab> getTabs() {
        return tabs;
    }

    public String getSelectedTabName() {
        String title = tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex());
        if (title.startsWith("<")) {
            title = title.substring(9, title.length() - 11);
        }

        return title;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void showDatabase() {
        databaseLabel = new JLabel("                                          "
                + "                                                             "
                + "                                                             " + database);

        menuBar.add(databaseLabel);
    }

    public void setLogWindow(LogWindow logWindow) {
        this.logWindow = logWindow;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setInformationLabel(String inf, int second) {
        this.informationLabel.setText(inf);
        startCountDownFromNow(second);
    }

    public void setFiltersclean() {
        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            TableFilter filter = tab.getFilter();
            filter.clearAllFilters();
            filter.applyFilter();
            filter.applyColorHeaders();

        }
//        Tab tab1 = tabs.get("PM");
//        TableFilter filter1 = tab1.getFilter();
//        filter1.clearAllFilters();
//        filter1.applyFilter();
//        filter1.applyColorHeaders();
//
//        // set label record information
//        String recordsLabel1 = tab1.getRecordsLabel();
//
//        Tab tab2 = tabs.get("ELLEGUI");
//        TableFilter filter2 = tab2.getFilter();
//        filter2.clearAllFilters();
//        filter2.applyFilter();
//        filter2.applyColorHeaders();
//
//        // set label record information
//        String recordsLabel2 = tab2.getRecordsLabel();
//
//        Tab tab3 = tabs.get("Analyster");
//        TableFilter filter3 = tab3.getFilter();
//        filter3.clearAllFilters();
//        filter3.applyFilter();
//        filter3.applyColorHeaders();
//
//        // set label record information
//        String recordsLabel3 = tab3.getRecordsLabel();
//
//        Tab tab4 = tabs.get("Other");
//        TableFilter filter4 = tab4.getFilter();
//        filter4.clearAllFilters();
//        filter4.applyFilter();
//        filter4.applyColorHeaders();
//
//        // set label record information
//        String recordsLabel4 = tab4.getRecordsLabel();
//
//        Tab tab5 = tabs.get("issue_files");
//        TableFilter filter5 = tab5.getFilter();
//        filter5.clearAllFilters();
//        filter5.applyFilter();
//        filter5.applyColorHeaders();
    }

    public String getUserName() {
        return this.userName;
    }

    public Statement getStatement() {
        return statement;
    }

    public JPanel getjPanelEdit() {
        return jPanelEdit;
    }

    public String getVersion() {
        return this.VERSION;
    }

    /**
     * initTotalRowCounts called once to initialize the total rowIndex counts of
     * each tabs tableSelected
     *
     * @param tabs
     * @return
     */
    public Map<String, Tab> initTotalRowCounts(Map<String, Tab> tabs) {

        int totalRecords;

//        boolean isFirstTabRecordLabelSet = false;
        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
//            JTable table = this.getSelectedTable();
            totalRecords = table.getRowCount();
            tab.setTotalRecords(totalRecords);

            if (entry.getKey().equals("PM")) {
                String recordsLabel = tab.getRecordsLabel();

                labelRecords.setText(recordsLabel);
//            }
            }
        }

        return tabs;
    }

    /**
     * Should only be called from loadTable(tab) to update orange dot
     *
     * @param tab
     */
    private void detectOpenIssues(Tab tab) {
        JTable table = tab.getTable();
        boolean openIssue = false;
        for (int row = 0; row < table.getRowCount(); row++) {
            String tableCellValue = "";
            if (table.getValueAt(row, 4) != null) {
                tableCellValue = table.getValueAt(row, 4).toString();
            }
            if (userName.equals(tableCellValue)) {
                if (table.getValueAt(row, 8) == null || table.getValueAt(row, 8).toString().equals("")) {

                    openIssue = true;
                }
            }
        }

        String title = table.getName();
        JLabel titleLabel = new JLabel(title);
        int tabIndex = -1;
        for (int i = 0; i < tableNames.length; i++) {
            if (tableNames[i].equals(title)) {
                tabIndex = i;
                break;
            }
        }

        if (openIssue) {
            titleLabel = this.getTabLabelWithOrangeDot(title);
        }

        tabbedPanel.setTabComponentAt(tabIndex, titleLabel);
    }

    /**
     * This returns a JLabel with the title and an orange dot
     *
     * @param title
     * @return
     */
    protected JLabel getTabLabelWithOrangeDot(String title) {
        ImageIcon imcon = new ImageIcon(getClass().getResource("orange-dot.png"));

//        ImageIcon icon = new ImageIcon(getClass().getResource("splashImage.png"));
        JLabel label = new JLabel(imcon);
        label.setText(title);

        label.setIconTextGap(10);

        label.setHorizontalTextPosition(JLabel.LEFT);
        label.setVerticalTextPosition(JLabel.TOP);
        return label;
    }

    /**
     * Loads all tab tables and updates the orange dots. It also checks for
     * table consistency
     *
     * @param tabs
     * @return
     */
    public Map<String, Tab> loadTables(Map<String, Tab> tabs) {

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();

            loadTable(tab);

            LoggingAspect.afterReturn("Table loaded succesfully");
//            informationLabel.setText("Table loaded succesfully");
//            startCountDownFromNow(10);
            setTableListeners(table, this);

            String[] colNames = tab.getTableColNames();
//            Map tableComboBoxForSearchDropDownList = this.loadingDropdownListToTable();
//            this.comboBoxForSearchDropDown.put(entry.getKey(), tableComboBoxForSearchDropDownList);

            boolean isColumnNameTheSame = ColumnNameConsistency.IsTableColumnNameTheSame(tab, table);
            if (!isColumnNameTheSame) {
                System.out.println(ColumnNameConsistency.getErrorMessage());
//                logWindow.addMessage(a);
//                logWindow.addMessageWithDate("3:" + a);
                LoggingAspect.afterReturn("Column Name(s) is(are) different from what in database");
            }
        }

        setLastUpdateTime();
        return tabs;
    }

    public void compareTablesColumnName(Tab tab, JTable table) {

    }

    /**
     * This loads table data and updates orange dot.
     *
     * @param tab
     * @return
     */
    public JTable loadTable(Tab tab) {
        JTable table = tab.getTable();
        table = loadTableData(table);
        detectOpenIssues(tab); // this puts an orange dot in tab title
        return table;
    }

    /**
     * This loads table data but does not update orange dot. Use loadTable(Tab
     * tab) to load table and update orange dot.
     *
     * @param table
     */
    public JTable loadTableData(JTable table) {
        String str = table.getName();

        int[] rows = table.getSelectedRows();
        Object[] selectedRowsID = new Object[rows.length];

        if (ifDeleteRecords) {
            //get selected rows' id store it into an object array
            for (int row = 0; row < rows.length - 1; row++) {
                Object Id = table.getValueAt(rows[row], 0);
                selectedRowsID[row] = Id;
            }
        } else {
            for (int row = 0; row < rows.length; row++) {
                Object Id = table.getValueAt(rows[row], 0);
                selectedRowsID[row] = Id;
            }
            ifDeleteRecords = false;
        }

        if (str == "PM" || str == "ELLEGUI" || str == "Analyster") {

            str = TASKS_TABLE_NAME + " WHERE app = " + "'" + str + "'";
        } else if (str == "Other") {
            str = TASKS_TABLE_NAME + " WHERE app != 'PM' and app != 'Analyster' "
                    + "and app != 'ELLEGUI' or app IS NULL";
        }

        // connection might time out
        DBConnection.close();
        DBConnection.open();

        statement = DBConnection.getStatement();
        String sql;
        if (!table.getName().equals(TASKFILES_TABLE_NAME)) {
            sql = "SELECT * FROM " + str + " ORDER BY "
                    + "case when dateClosed IS null then 1 else 0 end, dateClosed asc, ID ASC";
        } else {
            sql = "SELECT * FROM " + str + " ORDER BY taskId ASC";
        }
        loadTable(sql, table);

        for (int i = 0; i < selectedRowsID.length; i++) {
            for (int r = 0; r < table.getRowCount(); r++) {
                if (table.getValueAt(r, 0).equals(selectedRowsID[i])) {

                    rows[i] = r;
                }
            }
        }
        ListSelectionModel model = table.getSelectionModel();
        model.clearSelection();
        for (int row : rows) {
            model.addSelectionInterval(row, row);
        }

        return table;

    }

    /**
     * Loads table using an sql command
     *
     * @param sql
     * @param table
     * @return
     */
    public JTable loadTable(String sql, JTable table) {

        Vector data = new Vector();
        Vector columnNames = new Vector();
        Vector columnClass = new Vector();
        int columns;

        ResultSet rs = null;
        ResultSetMetaData metaData = null;
        try {
            rs = statement.executeQuery(sql);
            metaData = rs.getMetaData();

        } catch (Exception ex) {
            LoggingAspect.afterThrown(ex);
        }
        try {
            // Do not show "submitter" in "PM", "ELLEGUI", "Analyster" and "other" table
            if (!table.getName().equals("issue_files")) {
                columns = metaData.getColumnCount();
            } else {
                columns = metaData.getColumnCount();

            }
            for (int i = 1; i <= columns; i++) {
                columnClass.addElement(metaData.getColumnClassName(i));
                //              System.out.println(metaData.getColumnClassName(i) + " 1");
                columnNames.addElement(metaData.getColumnName(i));
                //               System.out.println(metaData.getColumnName(i) + " 2");
            }
            while (rs.next()) {
                Vector row = new Vector(columns);
                for (int i = 1; i <= columns; i++) {
                    row.addElement(rs.getObject(i));
                }
                data.addElement(row);
            }
            rs.close();

        } catch (SQLException ex) {
            LoggingAspect.afterThrown(ex);
        }

        EditableTableModel model = new EditableTableModel(data, columnNames, columnClass);

        // this has to be set here or else I get errors
        // I tried passing the model to the filter and setting it there
        // but it caused errors
        table.setModel(model);

        // check that the filter items are initialized
        String tabName = table.getName();
        Tab tab = tabs.get(tabName);

        // apply filter
        TableFilter filter = tab.getFilter();
        if (filter.getFilterItems() == null) {
            filter.initFilterItems();
        }
        filter.applyFilter();
        filter.applyColorHeaders();

        // load all checkbox items for the checkbox column pop up filter
        ColumnPopupMenu columnPopupMenu = tab.getColumnPopupMenu();
        columnPopupMenu.loadAllCheckBoxItems();

        // set column format
        float[] colWidthPercent = tab.getColWidthPercent();
        setColumnFormat(colWidthPercent, table);

        // set the listeners for the tableSelected
        setTableListeners(table, this);
//        table.setEnabled(false);

        // update last time the tableSelected was updated
        setLastUpdateTime();

        //make table scroll down as default
        scrollDown(jScrollPane1);
        scrollDown(jScrollPane4);
        scrollDown(jScrollPane5);
        scrollDown(jScrollPane6);
        scrollDown(jScrollPane7);
        return table;
    }

    /**
     * set the timer for information Label show
     *
     * @param waitSeconds
     */
    public static void startCountDownFromNow(int waitSeconds) {
        Timer timer = new Timer(waitSeconds * 1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                informationLabel.setText("");
                searchInformationLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * deleteRecordsSelected deletes the selected records
     *
     * @param table
     * @return
     * @throws HeadlessException
     */
    public String deleteRecordsSelected(JTable table) throws HeadlessException {

        String sqlDelete = ""; // String for the SQL Statement
        String tableName = table.getName(); // name of the tableSelected
        if (tableName.equals(TASKFILES_TABLE_NAME)) {
            tableName = TASKFILES_TABLE_NAME;
        } else {
            tableName = TASKS_TABLE_NAME;
        }

        int[] selectedRows = table.getSelectedRows(); // array of the rows selected
        int rowCount = selectedRows.length; // the number of rows selected
        if (rowCount != -1) {
            for (int i = 0; i < rowCount; i++) {
                int row = selectedRows[i];
                Integer selectedID = (Integer) table.getValueAt(row, 0); // Add Note to selected taskID

                if (i == 0) // this is the first rowIndex
                {
                    sqlDelete += "DELETE FROM " + database + "." + tableName
                            + " WHERE " + table.getColumnName(0) + " IN (" + selectedID; // 0 is the first column index = primary key
                } else // this adds the rest of the rows
                {
                    sqlDelete += ", " + selectedID;
                }

            }

            // windowClose the sql statement
            sqlDelete += ");";

            try {

                // delete records from database
                DBConnection.close();
                DBConnection.open();
                statement = DBConnection.getStatement();
                statement.executeUpdate(sqlDelete);

                // output pop up dialog that a record was deleted 
//                JOptionPane.showMessageDialog(this, rowCount + " Record(s) Deleted");
                LoggingAspect.afterReturn(rowCount + " Record(s) Deleted");
//                informationLabel.setText(rowCount + " Record(s) Deleted");
//                startCountDownFromNow(10);
                // set label record information
                String tabName = getSelectedTabName();
                Tab tab = tabs.get(tabName);
                // refresh tableSelected and retain filters
                loadTable(tab);
                tab.subtractFromTotalRowCount(rowCount); // update total rowIndex count
                String recordsLabel = tab.getRecordsLabel();
                labelRecords.setText(recordsLabel); // update label

            } catch (SQLException e) {
                LoggingAspect.afterThrown(e);
            }

        }
        ifDeleteRecords = true;
        return sqlDelete;
    }

    /**
     * ***** added methods ******************************
     */
    public JPanel getAddPanel_control() {
        return addPanel_control;
    }

    public JPanel getjPanel5() {
        return jPanel5;
    }

    public JPanel getjPanelSQL() {
        return jPanelSQL;
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }

    /**
     * setBatchEditButtonStates Sets the batch edit button enabled if editing
     * allowed for that tab and disabled if editing is not allowed for that tab
     *
     * @param selectedTab // this is the editing tab
     */
    private void setBatchEditButtonStates(Tab selectedTab) {

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());

            // if selectedTab is editing, that means the switch button was pressed
            if (selectedTab.isEditing()) {
                if (tab == selectedTab) {
                    if (selectedTab.isBatchEditWindowOpen()) {
                        btnBatchEdit.setEnabled(false);
                    } else {
                        tab.setBatchEditBtnEnabled(true);
                    }
                } else {
                    tab.setBatchEditBtnEnabled(false);
                }
            } else {
                tab.setBatchEditBtnEnabled(true);
            }
        }
    }

    /**
     * getBtnBatchEdit
     *
     * @return
     */
    public JButton getBtnBatchEdit() {
        return btnBatchEdit;
    }

    public BatchEditWindow getBatchEditWindow() {
        return batchEditWindow;
    }

    /**
     * isTabEditing This method returns true or false whether a tab is in
     * editing mode or not
     *
     * @return boolean isEditing
     */
    public boolean isTabEditing() {

        boolean isEditing = false;

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            isEditing = tab.isEditing();

            // if editing break and return true
            if (isEditing) {
                editingTabName = entry.getKey();
                break;
            }
        }

        return isEditing;
    }

    /**
     * setEnabledEditingButtons sets the editing buttons enabled
     *
     * @param switchBtnEnabled
     * @param uploadEnabled
     * @param revertEnabled
     */
    public void setEnabledEditingButtons(boolean switchBtnEnabled, boolean uploadEnabled, boolean revertEnabled) {

        // the three editing buttons (cancel, upload, revert)
//        btnSwitchEditMode.setEnabled(switchBtnEnabled);
        btnUploadChanges.setEnabled(uploadEnabled);
        btnRevertChanges.setEnabled(revertEnabled);
    }

    /**
     * revertChanges used to revert changes of modified data to original data
     */
    public void revertChanges() {

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        ModifiedTableData modifiedTableData = tab.getTableData();
        modifiedTableData.getNewData().clear();  // clear any stored changes (new data)
        loadTable(tab); // reverts the model back

        LoggingAspect.afterReturn("Nothing has been Changed!");
//        informationLabel.setText("Nothing has been Changed!");
//        startCountDownFromNow(5);

        modifiedTableData.reloadData();  // reloads data of new table (old data) to compare with new changes (new data)

        makeTableEditable(labelEditModeState.getText().equals("OFF") ? true : false);
        // no changes to upload or revert
        setEnabledEditingButtons(true, false, false);

        // set the color of the edit mode text
        editModeTextColor(tab.isEditing());
    }

    /**
     * editModeTextColor This method changes the color of the edit mode text If
     * edit mode is active then the text is green and if it is not active then
     * the text is the default color (black)
     */
    public void editModeTextColor(boolean editing) {

        // if editing
        if (editing) {
            labelEditMode.setForeground(editModeActiveTextColor);
            labelEditModeState.setForeground(editModeActiveTextColor);
        } // else not editing
        else {
            labelEditMode.setForeground(editModeDefaultTextColor);
            labelEditModeState.setForeground(editModeDefaultTextColor);
        }
    }

    /**
     * showWindowInFront This shows the component in front of the Main Window
     *
     * @param c Any component that needs to show on top of the Main window
     */
    public void showWindowInFront(Component c) {

        ((Window) (c)).setAlwaysOnTop(true);

    }

    public String getEditingTabName() {
        return editingTabName;
    }

    // @formatter:off
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AnalysterTable;
    private javax.swing.JTable ELLEGUITable;
    private javax.swing.JTable OtherTable;
    private javax.swing.JTable PMTable;
    private javax.swing.ButtonGroup TableFilterBtnGroup;
    private javax.swing.JPanel addPanel_control;
    private javax.swing.JButton btnAddIssue;
    private javax.swing.JButton btnBatchEdit;
    private javax.swing.JButton btnCancelSQL;
    private javax.swing.JButton btnClearAllFilter;
    private javax.swing.JButton btnCloseSQL;
    private javax.swing.JButton btnEnterSQL;
    private javax.swing.JButton btnRevertChanges;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUploadChanges;
    private javax.swing.JComboBox comboBoxField;
    private javax.swing.JComboBox comboBoxValue;
    public static javax.swing.JLabel informationLabel;
    private javax.swing.JTable issue_filesTable;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelEdit;
    private javax.swing.JPanel jPanelSQL;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextArea jTextAreaSQL;
    private javax.swing.JLabel labelEditMode;
    private javax.swing.JLabel labelEditModeState;
    private javax.swing.JLabel labelRecords;
    private javax.swing.JLabel labelTimeLastUpdate;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenu menuFind;
    private javax.swing.JMenu menuHelp;
    private javax.swing.JMenuItem menuItemAWSAssign;
    private javax.swing.JMenuItem menuItemActivateRecord;
    private javax.swing.JMenuItem menuItemArchiveRecord;
    private javax.swing.JMenuItem menuItemBackup;
    private javax.swing.JMenuItem menuItemCompIssues;
    private javax.swing.JMenuItem menuItemDeleteRecord;
    private javax.swing.JCheckBoxMenuItem menuItemLogChkBx;
    private javax.swing.JMenuItem menuItemLogOff;
    private javax.swing.JMenuItem menuItemManageDBs;
    private javax.swing.JMenuItem menuItemMoveSeletedRowsToEnd;
    private javax.swing.JMenuItem menuItemPrintDisplay;
    private javax.swing.JMenuItem menuItemPrintGUI;
    private javax.swing.JMenuItem menuItemReloadAllData;
    private javax.swing.JMenuItem menuItemReloadData;
    private javax.swing.JMenuItem menuItemRepBugSugg;
    private javax.swing.JCheckBoxMenuItem menuItemSQLCmdChkBx;
    private javax.swing.JMenuItem menuItemSaveFile;
    private javax.swing.JMenuItem menuItemTurnEditModeOff;
    private javax.swing.JMenuItem menuItemVersion;
    private javax.swing.JMenuItem menuItemViewSplashScreen;
    private javax.swing.JMenu menuPrint;
    private javax.swing.JMenu menuReports;
    private javax.swing.JMenu menuSelectConn;
    private javax.swing.JMenu menuTools;
    private javax.swing.JMenu menuView;
    private javax.swing.JMenuItem menuitemViewOneIssue;
    public static javax.swing.JLabel searchInformationLabel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTabbedPane tabbedPanel;
    // End of variables declaration//GEN-END:variables
    // @formatter:on

    public CustomIDList getSelectedTabCustomIdList(String TabName) {
        return tabs.get(TabName).getCustomIdList();
    }

    public boolean getAddRecordsWindowShow() {

        return this.addIssueWindowShow;

    }

    public JFrame getAddRecordsWindow() {
        return this.addIssueWindow;
    }

    public void setAddRecordsWindowShow(boolean a) {

        this.addIssueWindowShow = a;

    }

    public void setIsBatchEditWindowShow(boolean a) {
        this.isBatchEditWindowShow = a;
    }

    public boolean getIsBatchEditWindowShow() {
        return this.isBatchEditWindowShow;
    }

    public int getNumOfAddIssueWindowOpened() {
        return this.openingIssuesList.size();
    }

//    public void deleteNumOfAddIssueWindowOpened() {
//        this.numOfAddIssueWindowOpened--;
//        if (numOfAddIssueWindowOpened == 0) {
//            addIssueWindowShow = false;
//        }
//    }
//    public void setPopupWindowShowInPM(boolean b) {
//        popupWindowShowInPM = b;
//    }
    public JLabel getInformationLabel() {
        return this.informationLabel;
    }

    public JLabel getLabelEditModeState() {
        return this.labelEditModeState;
    }

    public String getCREATION_DATE() {
        return CREATION_DATE;
    }

    public String getVERSION() {
        return VERSION;
    }

    public Map<String, Map<Integer, ArrayList<Object>>> getComboBoxForSearchDropDown() {
        return comboBoxForSearchDropDown;
    }

    public String getDatabase() {
        return database;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    public IssueWindow getAddIssueWindow() {
        return addIssueWindow;
    }

    public LoginWindow getLoginWindow() {
        return loginWindow;
    }

    public EditDatabaseWindow getEditDatabaseWindow() {
        return editDatabaseWindow;
    }

    public Color getEditModeDefaultTextColor() {
        return editModeDefaultTextColor;
    }

    public Color getEditModeActiveTextColor() {
        return editModeActiveTextColor;
    }

    public boolean isAddIssueWindowShow() {
        return addIssueWindowShow;
    }

    public boolean isIsBatchEditWindowShow() {
        return isBatchEditWindowShow;
    }

    public boolean isPopupWindowShowInPM() {
        return popupWindowShowInPM;
    }

    public JLabel getDatabaseLabel() {
        return databaseLabel;
    }

    public String getCurrentTabName() {
        return currentTabName;
    }

    public ArrayList<String> getProgrammersActiveForSearching() {
        return programmersActiveForSearching;
    }

    public JTable getAnalysterTable() {
        return AnalysterTable;
    }

    public JTable getELLEGUITable() {
        return ELLEGUITable;
    }

    public JTable getOtherTable() {
        return OtherTable;
    }

    public JTable getPMTable() {
        return PMTable;
    }

    public ButtonGroup getTableFilterBtnGroup() {
        return TableFilterBtnGroup;
    }

    public JButton getBtnAddIssue() {
        return btnAddIssue;
    }

    public JButton getBtnCancelSQL() {
        return btnCancelSQL;
    }

    public JButton getBtnClearAllFilter() {
        return btnClearAllFilter;
    }

    public JButton getBtnCloseSQL() {
        return btnCloseSQL;
    }

    public JButton getBtnEnterSQL() {
        return btnEnterSQL;
    }

    public JButton getBtnRevertChanges() {
        return btnRevertChanges;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnUploadChanges() {
        return btnUploadChanges;
    }

    public JComboBox getComboBoxForSearch() {
        return comboBoxValue;
    }

    public JComboBox getComboBoxSearch() {
        return comboBoxField;
    }

    public JTable getIssue_filesTable() {
        return issue_filesTable;
    }

    public JCheckBoxMenuItem getjCheckBoxMenuItem1() {
        return jCheckBoxMenuItem1;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public JScrollPane getjScrollPane2() {
        return jScrollPane2;
    }

    public JScrollPane getjScrollPane4() {
        return jScrollPane4;
    }

    public JScrollPane getjScrollPane5() {
        return jScrollPane5;
    }

    public JScrollPane getjScrollPane6() {
        return jScrollPane6;
    }

    public JScrollPane getjScrollPane7() {
        return jScrollPane7;
    }

    public JTextArea getjTextAreaSQL() {
        return jTextAreaSQL;
    }

    public JLabel getLabelEditMode() {
        return labelEditMode;
    }

    public JLabel getLabelRecords() {
        return labelRecords;
    }

    public JLabel getLabelTimeLastUpdate() {
        return labelTimeLastUpdate;
    }

    public JMenu getMenuEdit() {
        return menuEdit;
    }

    public JMenu getMenuFile() {
        return menuFile;
    }

    public JMenu getMenuFind() {
        return menuFind;
    }

    public JMenu getMenuHelp() {
        return menuHelp;
    }

    public JMenuItem getMenuItemAWSAssign() {
        return menuItemAWSAssign;
    }

    public JMenuItem getMenuItemActivateRecord() {
        return menuItemActivateRecord;
    }

    public JMenuItem getMenuItemArchiveRecord() {
        return menuItemArchiveRecord;
    }

    public JMenuItem getMenuItemCompIssues() {
        return menuItemCompIssues;
    }

    public JMenuItem getMenuItemDeleteRecord() {
        return menuItemDeleteRecord;
    }

    public JCheckBoxMenuItem getMenuItemLogChkBx() {
        return menuItemLogChkBx;
    }

    public JMenuItem getMenuItemLogOff() {
        return menuItemLogOff;
    }

    public JMenuItem getMenuItemManageDBs() {
        return menuItemManageDBs;
    }

    public JMenuItem getMenuItemMoveSeletedRowsToEnd() {
        return menuItemMoveSeletedRowsToEnd;
    }

    public JMenuItem getMenuItemPrintDisplay() {
        return menuItemPrintDisplay;
    }

    public JMenuItem getMenuItemPrintGUI() {
        return menuItemPrintGUI;
    }

    public JMenuItem getMenuItemReloadData() {
        return menuItemReloadData;
    }

    public JMenuItem getMenuItemRepBugSugg() {
        return menuItemRepBugSugg;
    }

    public JCheckBoxMenuItem getMenuItemSQLCmdChkBx() {
        return menuItemSQLCmdChkBx;
    }

    public JMenuItem getMenuItemSaveFile() {
        return menuItemSaveFile;
    }

    public JMenuItem getMenuItemTurnEditModeOff() {
        return menuItemTurnEditModeOff;
    }

    public JMenuItem getMenuItemVersion() {
        return menuItemVersion;
    }

    public JMenuItem getMenuItemViewSplashScreen() {
        return menuItemViewSplashScreen;
    }

    public JMenu getMenuPrint() {
        return menuPrint;
    }

    public JMenu getMenuReports() {
        return menuReports;
    }

    public JMenu getMenuSelectConn() {
        return menuSelectConn;
    }

    public JMenu getMenuTools() {
        return menuTools;
    }

    public JMenu getMenuView() {
        return menuView;
    }

    public JMenuItem getMenuitemViewOneIssue() {
        return menuitemViewOneIssue;
    }

    public JLabel getSearchInformationLabel() {
        return searchInformationLabel;
    }

    public JTabbedPane getTabbedPanel() {
        return tabbedPanel;
    }

    public ShortCutSetting getShortCut() {
        return ShortCut;
    }

    public void setShortCut(ShortCutSetting ShortCut) {
        this.ShortCut = ShortCut;
    }

    public ConsistencyOfTableColumnName getColumnNameConsistency() {
        return ColumnNameConsistency;
    }

    public void setColumnNameConsistency(ConsistencyOfTableColumnName ColumnNameConsistency) {
        this.ColumnNameConsistency = ColumnNameConsistency;
    }

    public boolean isComboBoxStartToSearch() {
        return comboBoxStartToSearch;
    }

    public void setComboBoxStartToSearch(boolean comboBoxStartToSearch) {
        this.comboBoxStartToSearch = comboBoxStartToSearch;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public void setMenuBar(JMenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public JMenuItem getMenuItemBackup() {
        return menuItemBackup;
    }

    public void setMenuItemBackup(JMenuItem menuItemBackup) {
        this.menuItemBackup = menuItemBackup;
    }

    public JMenuItem getMenuItemReloadAllData() {
        return menuItemReloadAllData;
    }

    public void setMenuItemReloadAllData(JMenuItem menuItemReloadAllData) {
        this.menuItemReloadAllData = menuItemReloadAllData;
    }

    private void textComponentShortCutSetting() {
        //changing the text field copy and paste short cut to default control key
        //(depend on system) + c/v
        InputMap ip = (InputMap) UIManager.get("TextField.focusInputMap");
        InputMap ip2 = this.jTextAreaSQL.getInputMap();
//        InputMap ip2 = (InputMap) UIManager.get("TextArea.focusInputMap");
        ShortCut.copyAndPasteShortCut(ip);
        ShortCut.copyAndPasteShortCut(ip2);

        // add redo and undo short cut to text component
    }

    public Tab getSelectedTab() {
        return tabs.get(getSelectedTabName());
    }

    /**
     * Updates a table rowIndex's data
     * @param table
     * @param issue 
     */
    public void updateTableRow(JTable table, Issue issue) {
        int row = findTableModelRow(table,issue);
        if(row != -1){
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            
            // remove table listeners because this listens for changes in 
            // table and changes the cell green for upload changes and revert
            // changes. So I remove them and then put them back.
            TableModelListener[] listeners = model.getTableModelListeners();
            for(int i = 0; i < listeners.length; i++){
                model.removeTableModelListener(listeners[i]);
            }

            // update -> no need for id
            model.setValueAt(issue.getApp(), row, 1);
            model.setValueAt(issue.getTitle(), row, 2);
            model.setValueAt(issue.getDescription(), row, 3);
            model.setValueAt(issue.getProgrammer(), row, 4);
            model.setValueAt(issue.getDateOpened(), row, 5);
            model.setValueAt(issue.getRk(), row, 6);
            model.setValueAt(issue.getVersion(), row, 7);
            model.setValueAt(issue.getDateClosed(), row, 8);
            model.setValueAt(issue.getIssueType(), row, 9);
            model.setValueAt(issue.getSubmitter(), row, 10);
            model.setValueAt(issue.getLocked(), row, 11);
            
            // add back the table listeners
            for(int i = 0; i < listeners.length; i++){
                model.addTableModelListener(listeners[i]);
            }
            
            table.repaint();
        }
        else{
            String errMsg = "Problem updating row: Row not Found";
            LoggingAspect.afterReturn(errMsg);
        }
    }

    /**
     * Inserts a new rowIndex in the table
     * @param table
     * @param issue 
     */
    public void inserTableRow(JTable table, Issue issue) {

        Object[] rowData = new Object[12];
        rowData[0] = issue.getId();
        rowData[1] = issue.getApp();
        rowData[2] = issue.getTitle();
        rowData[3] = issue.getDescription();
        rowData[4] = issue.getProgrammer();
        rowData[5] = issue.getDateOpened();
        rowData[6] = issue.getRk();
        rowData[7] = issue.getVersion();
        rowData[8] = issue.getDateClosed();
        rowData[9] = issue.getIssueType();
        rowData[10] = issue.getSubmitter();
        rowData[11] = issue.getLocked();
        ((DefaultTableModel)table.getModel()).addRow(rowData);
    }

    /**
     * Locates the table model rowIndex index
     * @param issue
     * @return int table model rowIndex
     */
    private int findTableModelRow(JTable table, Issue issue) {
        int rowCount = table.getModel().getRowCount();
        TableModel model = table.getModel();
        for(int rowIndex = 0; rowIndex < rowCount; rowIndex++){
            int rowId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
            if(rowId == issue.getId()){
                return rowIndex;
            }
        }
        return -1; // rowIndex not found
    }

    /**
     * CLASS
     */
    class AlignmentTableHeaderCellRenderer implements TableCellRenderer {

        private final TableCellRenderer wrappedRenderer;
        private final JLabel label;

        public AlignmentTableHeaderCellRenderer(TableCellRenderer wrappedRenderer) {
            if (!(wrappedRenderer instanceof JLabel)) {
                throw new IllegalArgumentException("The supplied renderer must inherit from JLabel");
            }
            this.wrappedRenderer = wrappedRenderer;
            this.label = (JLabel) wrappedRenderer;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            wrappedRenderer.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            if (table.getName().equals(TASKFILES_TABLE_NAME)) {

                if (column < table.getColumnCount() - 4) {
                    label.setHorizontalAlignment(JLabel.CENTER);
                    return label;
                } else {
                    label.setHorizontalAlignment(JLabel.LEFT);
                    return label;
                }
            }

            label.setHorizontalAlignment(column == table.getColumnCount() - 1 ? JLabel.LEFT : JLabel.CENTER);
            return label;

        }

    }
}
