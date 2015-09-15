package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.logic.ColumnPopupMenu;
import com.elle.ProjectManager.logic.CreateDocumentFilter;
import com.elle.ProjectManager.logic.EditableTableModel;
import com.elle.ProjectManager.logic.ITableConstants;
import com.elle.ProjectManager.database.ModifiedData;
import com.elle.ProjectManager.database.ModifiedTableData;
import static com.elle.ProjectManager.logic.ITableConstants.TASKFILES_TABLE_NAME;
import static com.elle.ProjectManager.logic.ITableConstants.TASKNOTES_TABLE_NAME;
import static com.elle.ProjectManager.logic.ITableConstants.TASKS_TABLE_NAME;
import com.elle.ProjectManager.logic.Tab;
import com.elle.ProjectManager.logic.TableFilter;
import com.elle.ProjectManager.logic.JTableCellRenderer;
//import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * ProjectManagerWindow
 *
 * @author Xijin Shan
 * @since June 29, 2015
 * @version 0.6.3
 */
public class ProjectManagerWindow extends JFrame implements ITableConstants {

    // Edit the version and date it was created for new archives and jars
    private final String CREATION_DATE = "2015-09-12";
    private final String VERSION = "0.8.5";

    // attributes
    private Map<String, Tab> tabs; // stores individual tabName information
    private static Statement statement;
    private String database;

    // components
    private static ProjectManagerWindow instance;
    private AddRecordsWindow addRecordsWindow;
    private LogWindow logWindow;
    private LoginWindow loginWindow;
    private BatchEditWindow batchEditWindow;
    private EditDatabaseWindow editDatabaseWindow;
    private ReportWindow reportWindow;
    private boolean addRecordWindowShow;
    
    private int addRecordLevel = 2;
    private int deleteRecordLevel = 2;

//    // create components for the tableSelected cell popup window in project manager window 
//    private JTextArea textAreatableCellPopup;
//    private JScrollPane areaScrollPanetableCellPopup;
//    private JPanel tableCellPopupPanel;
//    private JPanel controlPopupPanel;
//    private JButton confirmButtonTableCellPopup;
//    private JButton cancelButtonTableCellPopup;    
    private TableCellPopupWindow tableCellPopupWindow;

    // create a jlabel to show the database used
    private JLabel databaseLabel;

    /**
     * CONSTRUCTOR
     */
    public ProjectManagerWindow() {

        /**
         * Note: initComponents() executes the tabpaneChanged method. Thus, some
         * things need to be before or after the initComponents();
         */
        // the statement is used for sql statements with the database connection
        // the statement is created in LoginWindow and passed to Analyster.
        statement = DBConnection.getStatement();
        instance = this;                         // this is used to call this instance of Analyster 

        // initialize tabs
        tabs = new HashMap();

        // create tabName objects -> this has to be before initcomponents();
        tabs.put(TASKS_TABLE_NAME, new Tab());
        tabs.put(TASKFILES_TABLE_NAME, new Tab());
        tabs.put(TASKNOTES_TABLE_NAME, new Tab());

        // set tableSelected names 
        tabs.get(TASKS_TABLE_NAME).setTableName(TASKS_TABLE_NAME);
        tabs.get(TASKFILES_TABLE_NAME).setTableName(TASKFILES_TABLE_NAME);
        tabs.get(TASKNOTES_TABLE_NAME).setTableName(TASKNOTES_TABLE_NAME);

        // set the search fields for the comboBox for each tabName
        tabs.get(TASKS_TABLE_NAME).setSearchFields(TASKS_SEARCH_FIELDS);
        tabs.get(TASKFILES_TABLE_NAME).setSearchFields(TASKFILES_SEARCH_FIELDS);
        tabs.get(TASKNOTES_TABLE_NAME).setSearchFields(TASKNOTES_SEARCH_FIELDS);

        // set the search fields for the comboBox for each tabName
        tabs.get(TASKS_TABLE_NAME).setBatchEditFields(ASSIGNMENTS_BATCHEDIT_CB_FIELDS);
        tabs.get(TASKFILES_TABLE_NAME).setBatchEditFields(REPORTS_BATCHEDIT_CB_FIELDS);
        tabs.get(TASKNOTES_TABLE_NAME).setBatchEditFields(ARCHIVE_BATCHEDIT_CB_FIELDS);

        // set column width percents to tables of the tabName objects
        tabs.get(TASKS_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_TASKS);
        tabs.get(TASKFILES_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_REPORTS);
        tabs.get(TASKNOTES_TABLE_NAME).setColWidthPercent(COL_WIDTH_PER_ARCHIVE);

        // set Activate Records menu item enabled for each tabName
        tabs.get(TASKS_TABLE_NAME).setActivateRecordMenuItemEnabled(false);
        tabs.get(TASKFILES_TABLE_NAME).setActivateRecordMenuItemEnabled(false);
        tabs.get(TASKNOTES_TABLE_NAME).setActivateRecordMenuItemEnabled(true);

        // set Archive Records menu item enabled for each tabName
        tabs.get(TASKS_TABLE_NAME).setArchiveRecordMenuItemEnabled(true);
        tabs.get(TASKFILES_TABLE_NAME).setArchiveRecordMenuItemEnabled(false);
        tabs.get(TASKNOTES_TABLE_NAME).setArchiveRecordMenuItemEnabled(false);

        // set add records button visible for each tabName
        tabs.get(TASKS_TABLE_NAME).setAddRecordsBtnVisible(true);
        tabs.get(TASKFILES_TABLE_NAME).setAddRecordsBtnVisible(true);
        tabs.get(TASKNOTES_TABLE_NAME).setAddRecordsBtnVisible(false);

        // set batch edit button visible for each tabName
        tabs.get(TASKS_TABLE_NAME).setBatchEditBtnVisible(true);
        tabs.get(TASKFILES_TABLE_NAME).setBatchEditBtnVisible(true);
        tabs.get(TASKNOTES_TABLE_NAME).setBatchEditBtnVisible(false);

        initComponents(); // generated code

        // set names to tables (this was in tabbedPanelChanged method)
        tasksTable.setName(TASKS_TABLE_NAME);
        task_filesTable.setName(TASKFILES_TABLE_NAME);
        task_notesTable.setName(TASKNOTES_TABLE_NAME);

        // set tables to tabName objects
        tabs.get(TASKS_TABLE_NAME).setTable(tasksTable);
        tabs.get(TASKFILES_TABLE_NAME).setTable(task_filesTable);
        tabs.get(TASKNOTES_TABLE_NAME).setTable(task_notesTable);

        // set array variable of stored column names of the tables
        // this is just to store and use the information
        // to actually change the tableSelected names it should be done
        // through properties in the gui design tabName
        tabs.get(TASKS_TABLE_NAME).setTableColNames(tasksTable);
        tabs.get(TASKFILES_TABLE_NAME).setTableColNames(task_filesTable);
        tabs.get(TASKNOTES_TABLE_NAME).setTableColNames(task_notesTable);

        // this sets the KeyboardFocusManger
        setKeyboardFocusManager();

        // show and hide components
        btnUploadChanges.setVisible(false);
        jPanelSQL.setVisible(false);
        btnEnterSQL.setVisible(true);
        btnCancelSQL.setVisible(true);
        btnCancelEditMode.setVisible(false);
        btnBatchEdit.setVisible(true);
        jTextAreaSQL.setVisible(true);

        // add filters for each tableSelected
        // must be before setting ColumnPopupMenu because this is its parameter
        tabs.get(TASKS_TABLE_NAME).setFilter(new TableFilter(tasksTable));
        tabs.get(TASKFILES_TABLE_NAME).setFilter(new TableFilter(task_filesTable));
        tabs.get(TASKNOTES_TABLE_NAME).setFilter(new TableFilter(task_notesTable));

        // initialize columnPopupMenu 
        // - must be before setTerminalFunctions is called
        // - because the mouslistener is added to the tableSelected header
        tabs.get(TASKS_TABLE_NAME)
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(TASKS_TABLE_NAME).getFilter()));
        tabs.get(TASKFILES_TABLE_NAME)
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(TASKFILES_TABLE_NAME).getFilter()));
        tabs.get(TASKNOTES_TABLE_NAME)
                .setColumnPopupMenu(new ColumnPopupMenu(tabs.get(TASKNOTES_TABLE_NAME).getFilter()));

        // load data from database to tables
        loadTables(tabs);

        // set initial record counts of now full tables
        // this should only need to be called once at start up of Analyster.
        // total counts are removed or added in the Tab class
        initTotalRowCounts(tabs);

        // set the cell renderers for each tabName
        tabs.get(TASKS_TABLE_NAME).setCellRenderer(new JTableCellRenderer(tasksTable));
        tabs.get(TASKFILES_TABLE_NAME).setCellRenderer(new JTableCellRenderer(task_filesTable));
        tabs.get(TASKNOTES_TABLE_NAME).setCellRenderer(new JTableCellRenderer(task_notesTable));

        // set the modified tableSelected data objects for each tabName
        tabs.get(TASKS_TABLE_NAME).setTableData(new ModifiedTableData(tasksTable));
        tabs.get(TASKFILES_TABLE_NAME).setTableData(new ModifiedTableData(task_filesTable));
        tabs.get(TASKNOTES_TABLE_NAME).setTableData(new ModifiedTableData(task_notesTable));

//        // Call the initTableCellPopup method to initiate the Table Cell Popup window
//        initTableCellPopup();
//       
//        // Set the tableSelected listener for the tableSelected cell popup window.
//        setTableListener(tasksTable);
//        setTableListener(task_filesTable);
//        setTableListener(task_notesTable);      
        addRecordWindowShow = false;

        // set the tableSelected cell popup window
//        tableCellPopupWindow = new TableCellPopupWindow();
//        tableCellPopupWindow.initTableCellPopup(this);
//        tableCellPopupWindow.setTableListener(tasksTable);
//        tableCellPopupWindow.setTableListener(task_filesTable);
//        tableCellPopupWindow.setTableListener(task_notesTable);
        if(!addRecordWindowShow){
            initTableCellPopupWindow();
        }

        // set title of window to Analyster
        this.setTitle("Project Manager");

        // set the size for project manager
        this.setPreferredSize(new Dimension(1207, 631));
        this.setMinimumSize(new Dimension(1207, 631));

    }

//    
//    /*
//     * This is to initiate the tableSelected cell popup window.
//    */ 
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
//                int row = tableSelected.getSelectedRow();
//                int column = tableSelected.getSelectedColumn();   
//                if(tableSelected.equals(tasksTable)){                   
//                    if(column == 2 || column == 4 || column == 5){
//                        // popup tableSelected cell edit window
//                        tableCellPopup(tasksTable, row , column);
//                    }else{
//                        tableCellPopupPanel.setVisible(false);
//                        controlPopupPanel.setVisible(false);
//                    }
//                }
//                else if(tableSelected.equals(task_filesTable)){
//                    if(column == 5 || column == 6 || column == 7){
//                        // popup tableSelected cell edit window
//                        tableCellPopup(task_filesTable, row , column);
//                    }else{
//                        tableCellPopupPanel.setVisible(false);
//                        controlPopupPanel.setVisible(false);
//                    }                   
//                }
//                else if(tableSelected.equals(task_notesTable)){
//                    if(column == 3){
//                        // popup tableSelected cell edit window
//                        tableCellPopup(task_notesTable, row , column);
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
//                        int row = tableSelected.getSelectedRow();                    
//                        int column = tableSelected.getSelectedColumn();   
//                        if(tableSelected.equals(tasksTable)){
//                            if(column == 2 || column == 4 || column == 5 || column == 6){
//                                // popup tableSelected cell edit window
//                                tableCellPopup(tasksTable, row , column);
//                            }else{
//                                tableCellPopupPanel.setVisible(false);
//                                controlPopupPanel.setVisible(false);
//                            }
//                        }
//                        else if(tableSelected.equals(task_filesTable)){
//                            if(column == 5 || column == 6 || column == 7){
//                                // popup tableSelected cell edit window
//                                tableCellPopup(task_filesTable, row , column);
//                            }else{
//                                tableCellPopupPanel.setVisible(false);
//                                controlPopupPanel.setVisible(false);
//                            }                   
//                        }
//                        else if(tableSelected.equals(task_notesTable)){
//                            if(column == 3){
//                                // popup tableSelected cell edit window
//                                tableCellPopup(task_notesTable, row , column);
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
//     * @parm selectedTable, row , column
//    */    
//    private void tableCellPopup(JTable selectedTable, int row, int column){
//        
//        // find the selected tableSelected cell 
//        Rectangle cellRect = selectedTable.getCellRect(row, column, true);
//        
//        // set the tableSelected cell popup window visible
//        tableCellPopupPanel.setVisible(true);
//        controlPopupPanel.setVisible(true); 
//   
//        // use the tableSelected cell content to set the content for textarea
//        textAreatableCellPopup.setText("");
//        textAreatableCellPopup.setText((String) selectedTable.getValueAt(row, column)); 
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
//                selectedTable.setValueAt(newTableCellValue, row, column);
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

        addPanel_control = new javax.swing.JPanel();
        labelTimeLastUpdate = new javax.swing.JLabel();
        searchPanel = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        textFieldForSearch = new javax.swing.JTextField();
        comboBoxSearch = new javax.swing.JComboBox();
        btnClearAllFilter = new javax.swing.JButton();
        labelRecords = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        tabbedPanel = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tasksTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        task_filesTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        task_notesTable = new javax.swing.JTable();
        jPanelEdit = new javax.swing.JPanel();
        btnBatchEdit = new javax.swing.JButton();
        btnAddRecords = new javax.swing.JButton();
        btnUploadChanges = new javax.swing.JButton();
        btnCancelEditMode = new javax.swing.JButton();
        btnSwitchEditMode = new javax.swing.JButton();
        jLabelEdit = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanelSQL = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaSQL = new javax.swing.JTextArea();
        btnEnterSQL = new javax.swing.JButton();
        btnCancelSQL = new javax.swing.JButton();
        btnCloseSQL = new javax.swing.JButton();
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
        menuItemViewTasks = new javax.swing.JMenuItem();
        menuItemViewTaskFiles = new javax.swing.JMenuItem();
        menuItemViewAllTasks = new javax.swing.JMenuItem();
        menuItemViewTaskNotes = new javax.swing.JMenuItem();
        menuTools = new javax.swing.JMenu();
        menuItemReloadData = new javax.swing.JMenuItem();
        menuItemLogChkBx = new javax.swing.JCheckBoxMenuItem();
        menuItemSQLCmdChkBx = new javax.swing.JCheckBoxMenuItem();
        menuHelp = new javax.swing.JMenu();
        menuItemRepBugSugg = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelTimeLastUpdate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTimeLastUpdate.setText("Last updated: ");
        labelTimeLastUpdate.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        textFieldForSearch.setText("Enter Symbol name");
        textFieldForSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textFieldForSearchMouseClicked(evt);
            }
        });
        textFieldForSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldForSearchKeyPressed(evt);
            }
        });

        comboBoxSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "symbol", "analyst" }));

        btnClearAllFilter.setText("Clear All Filters");
        btnClearAllFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearAllFilterActionPerformed(evt);
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
                .addComponent(comboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textFieldForSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSearch)
                .addGap(17, 17, 17))
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textFieldForSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(comboBoxSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClearAllFilter))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labelRecords.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelRecords.setText("labelRecords");

        javax.swing.GroupLayout addPanel_controlLayout = new javax.swing.GroupLayout(addPanel_control);
        addPanel_control.setLayout(addPanel_controlLayout);
        addPanel_controlLayout.setHorizontalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelTimeLastUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelRecords, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        addPanel_controlLayout.setVerticalGroup(
            addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addPanel_controlLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(addPanel_controlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(labelTimeLastUpdate)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tabbedPanel.setPreferredSize(new java.awt.Dimension(800, 584));
        tabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPanelStateChanged(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        tasksTable.setAutoCreateRowSorter(true);
        tasksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "taskID", "num", "title", "step", "description", "instructions", "programmer", "dateAssigned", "rk", "done", "dateDone"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tasksTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tasksTable.setMinimumSize(new java.awt.Dimension(10, 240));
        tasksTable.setName(""); // NOI18N
        jScrollPane1.setViewportView(tasksTable);

        tabbedPanel.addTab("tasks", jScrollPane1);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        task_filesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "fileID", "taskID", "submitter", "step", "date_", "files", "path", "notes"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        task_filesTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        task_filesTable.setMinimumSize(new java.awt.Dimension(10, 240));
        jScrollPane4.setViewportView(task_filesTable);

        tabbedPanel.addTab("task_files", jScrollPane4);

        task_notesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "noteID", "taskID", "submitter", "status_notes", "_date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        task_notesTable.setAutoscrolls(false);
        task_notesTable.setMinimumSize(new java.awt.Dimension(10, 240));
        jScrollPane3.setViewportView(task_notesTable);

        tabbedPanel.addTab("task_notes", jScrollPane3);

        jPanelEdit.setPreferredSize(new java.awt.Dimension(636, 180));

        btnBatchEdit.setText("Batch Edit");
        btnBatchEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchEditActionPerformed(evt);
            }
        });

        btnAddRecords.setText("Add Record(s)");
        btnAddRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRecordsActionPerformed(evt);
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

        btnCancelEditMode.setText("Cancel");
        btnCancelEditMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelEditModeActionPerformed(evt);
            }
        });

        btnSwitchEditMode.setText("Switch");
        btnSwitchEditMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSwitchEditModeActionPerformed(evt);
            }
        });

        jLabelEdit.setText("OFF");

        jLabel2.setText("Edit Mode:");

        javax.swing.GroupLayout jPanelEditLayout = new javax.swing.GroupLayout(jPanelEdit);
        jPanelEdit.setLayout(jPanelEditLayout);
        jPanelEditLayout.setHorizontalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSwitchEditMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCancelEditMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUploadChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddRecords)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatchEdit)
                .addGap(26, 26, 26))
        );
        jPanelEditLayout.setVerticalGroup(
            jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEditLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUploadChanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnSwitchEditMode)
                    .addComponent(jLabelEdit)
                    .addComponent(btnCancelEditMode)
                    .addComponent(btnBatchEdit)
                    .addComponent(btnAddRecords))
                .addGap(4, 4, 4))
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
        jTextAreaSQL.setMaximumSize(new java.awt.Dimension(1590, 150));
        jTextAreaSQL.setMinimumSize(new java.awt.Dimension(1590, 150));
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE))
        );
        jPanelSQLLayout.setVerticalGroup(
            jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSQLLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanelSQLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanelSQLLayout.createSequentialGroup()
                        .addComponent(btnEnterSQL)
                        .addGap(4, 4, 4)
                        .addComponent(btnCancelSQL)
                        .addGap(4, 4, 4)
                        .addComponent(btnCloseSQL)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
            .addComponent(jPanelEdit, javax.swing.GroupLayout.DEFAULT_SIZE, 908, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(tabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanelSQL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabbedPanel.getAccessibleContext().setAccessibleName("Reports");
        tabbedPanel.getAccessibleContext().setAccessibleParent(tabbedPanel);

        menuFile.setText("File");

        menuItemVersion.setText("Version");
        menuItemVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemVersionActionPerformed(evt);
            }
        });
        menuFile.add(menuItemVersion);

        menuSelectConn.setText("Select Connection");

        menuItemAWSAssign.setText("AWS Assignments");
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
        menuItemArchiveRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemArchiveRecordActionPerformed(evt);
            }
        });
        menuEdit.add(menuItemArchiveRecord);

        menuItemActivateRecord.setText("Activate Record");
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
        menuBar.add(menuReports);

        menuView.setText("View");

        menuItemViewTasks.setText("View Tasks Columns");
        menuItemViewTasks.setEnabled(false);
        menuView.add(menuItemViewTasks);

        menuItemViewTaskFiles.setText("View Task_Files Columns");
        menuItemViewTaskFiles.setEnabled(false);
        menuView.add(menuItemViewTaskFiles);

        menuItemViewAllTasks.setText("View All Tasks");
        menuItemViewAllTasks.setEnabled(false);
        menuItemViewAllTasks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewAllTasksActionPerformed(evt);
            }
        });
        menuView.add(menuItemViewAllTasks);

        menuItemViewTaskNotes.setText("View Task_Notes");
        menuItemViewTaskNotes.setEnabled(false);
        menuItemViewTaskNotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemViewTaskNotesActionPerformed(evt);
            }
        });
        menuView.add(menuItemViewTaskNotes);

        menuBar.add(menuView);

        menuTools.setText("Tools");

        menuItemReloadData.setText("Reload data");
        menuItemReloadData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReloadDataActionPerformed(evt);
            }
        });
        menuTools.add(menuItemReloadData);

        menuItemLogChkBx.setText("Log");
        menuItemLogChkBx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLogChkBxActionPerformed(evt);
            }
        });
        menuTools.add(menuItemLogChkBx);

        menuItemSQLCmdChkBx.setText("SQL Command");
        menuItemSQLCmdChkBx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSQLCmdChkBxActionPerformed(evt);
            }
        });
        menuTools.add(menuItemSQLCmdChkBx);

        menuBar.add(menuTools);

        menuHelp.setText("Help");

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
            .addComponent(addPanel_control, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(addPanel_control, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemVersionActionPerformed

        JOptionPane.showMessageDialog(this, "Creation Date: "
                + CREATION_DATE + "\n"
                + "Version: " + VERSION);
    }//GEN-LAST:event_menuItemVersionActionPerformed

    private void textFieldForSearchMouseClicked(MouseEvent evt) {//GEN-FIRST:event_textFieldForSearchMouseClicked

        textFieldForSearch.setText(""); // clears text
    }//GEN-LAST:event_textFieldForSearchMouseClicked

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

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        String searchColName = comboBoxSearch.getSelectedItem().toString();

        // this matches the combobox newValue with the column name newValue to get the column index
        for (int col = 0; col < table.getColumnCount(); col++) {
            String tableColName = table.getColumnName(col);
            if (tableColName.equalsIgnoreCase(searchColName)) {

                String searchBoxValue = textFieldForSearch.getText();  // store string from text box

                // add item to filter
                TableFilter filter = tab.getFilter();
                filter.addFilterItem(col, searchBoxValue);
                filter.applyFilter();

                // set label record information
                String recordsLabel = tab.getRecordsLabel();
                labelRecords.setText(recordsLabel);
            }
        }
    }

    // not sure what this is
    private void menuItemAWSAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAWSAssignActionPerformed

        loadTable(tasksTable);
        loadTable(task_filesTable);


    }//GEN-LAST:event_menuItemAWSAssignActionPerformed

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

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTable table = tab.getTable();
        JTableCellRenderer cellRenderer = tab.getCellRenderer();
        ModifiedTableData data = tab.getTableData();

        updateTable(table, data.getNewData());

        loadTable(table); // refresh tableSelected

        // clear cellrenderer
        cellRenderer.clearCellRender();

        // reload modified tableSelected data with current tableSelected model
        data.reloadData();

        makeTableEditable(jLabelEdit.getText().equals("OFF") ? true : false);

        data.getNewData().clear();    // reset the arraylist to record future changes
        setLastUpdateTime();          // update time
    }

    private void menuItemRepBugSuggActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRepBugSuggActionPerformed
        reportWindow = new ReportWindow();
        reportWindow.setLocationRelativeTo(this);
        reportWindow.setVisible(true);
    }//GEN-LAST:event_menuItemRepBugSuggActionPerformed

    private void btnEnterSQLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterSQLActionPerformed

        int commandStart = jTextAreaSQL.getText().lastIndexOf(">>") + 2;
        String command = jTextAreaSQL.getText().substring(commandStart);
        if (command.toLowerCase().contains("select")) {
            loadTable(command, tasksTable);
        } else {
            try {
                statement.executeUpdate(command);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
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
    }//GEN-LAST:event_btnCloseSQLActionPerformed

    private void btnSwitchEditModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSwitchEditModeActionPerformed

        // this was the way it is being checked - with the label text
        // this checks the text and passes the opposite - ON = false to turn off
        makeTableEditable(jLabelEdit.getText().equals("ON ") ? false : true);

    }//GEN-LAST:event_btnSwitchEditModeActionPerformed

    /**
     * makeTableEditable Make tables editable or non editable
     *
     * @param makeTableEditable // takes boolean true or false to make editable
     */
    public void makeTableEditable(boolean makeTableEditable) {

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        boolean isAddRecordsBtnVisible = tab.isAddRecordsBtnVisible();
        boolean isBatchEditBtnVisible = tab.isBatchEditBtnVisible();

        if (makeTableEditable) {
            jLabelEdit.setText("ON ");
            btnSwitchEditMode.setVisible(false);
            btnUploadChanges.setVisible(true);
            btnCancelEditMode.setVisible(true);
            btnAddRecords.setVisible(false);
            btnBatchEdit.setVisible(false);
        } else {
            jLabelEdit.setText("OFF");
            btnSwitchEditMode.setVisible(true);
            btnUploadChanges.setVisible(false);
            btnCancelEditMode.setVisible(false);
            btnAddRecords.setVisible(isAddRecordsBtnVisible);
            btnBatchEdit.setVisible(isBatchEditBtnVisible);
        }

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
            EditableTableModel model = ((EditableTableModel) table.getModel());
            model.setCellEditable(makeTableEditable);
        }
    }

    /**
     * btnCancelEditModeActionPerformed
     *
     * @param evt
     */
    private void btnCancelEditModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelEditModeActionPerformed

        makeTableEditable(false); // exit edit mode;

    }//GEN-LAST:event_btnCancelEditModeActionPerformed

    /**
     * changeTabbedPanelState
     */
    private void changeTabbedPanelState() {

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        boolean isActivateRecordMenuItemEnabled = tab.isActivateRecordMenuItemEnabled();
        boolean isArchiveRecordMenuItemEnabled = tab.isArchiveRecordMenuItemEnabled();
        boolean isAddRecordsBtnVisible = tab.isAddRecordsBtnVisible();
        boolean isBatchEditBtnVisible = tab.isBatchEditBtnVisible();

        // this enables or disables the menu components for this tabName
        menuItemActivateRecord.setEnabled(isActivateRecordMenuItemEnabled);
        menuItemArchiveRecord.setEnabled(isArchiveRecordMenuItemEnabled);

        // show or hide the add records button and the batch edit button
        btnAddRecords.setVisible(isAddRecordsBtnVisible);
        btnBatchEdit.setVisible(isBatchEditBtnVisible);

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);

        // hide buttons if in edit mode
        if (jLabelEdit.getText().equals("ON ")) {
            btnAddRecords.setVisible(false);
            btnBatchEdit.setVisible(false);
        }
    }

    private void btnBatchEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchEditActionPerformed
        batchEditWindow = new BatchEditWindow();
        batchEditWindow.setVisible(true);
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
    private void btnAddRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRecordsActionPerformed
        
        addRecordsWindow = new AddRecordsWindow();
        addRecordsWindow.setVisible(true);
        addRecordWindowShow = true;

        // update records
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);
    }//GEN-LAST:event_btnAddRecordsActionPerformed

    /**
     * This method listens if the enter key was pressed in the search text box.
     * This allows the newValue to be entered without having to click the search
     * button.
     *
     * @param evt
     */
    private void textFieldForSearchKeyPressed(KeyEvent evt) {//GEN-FIRST:event_textFieldForSearchKeyPressed

        // if the enter key is pressed call the filterBySearch method.
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            filterBySearch();
        }
    }//GEN-LAST:event_textFieldForSearchKeyPressed

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
        logWindow.addMessageWithDate(sqlDelete);
//        logWindow.setLevel(deleteRecordLevel);
    }

    /**
     * jMenuItemViewAllAssigActionPerformed calls load data method
     *
     * @param evt
     */
    private void menuItemViewAllTasksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewAllTasksActionPerformed
        loadData();
    }//GEN-LAST:event_menuItemViewAllTasksActionPerformed

    /**
     * jMenuItemViewActiveAssigActionPerformed load only active data from
     * analyst
     *
     * @param evt
     */
    private void menuItemViewTaskNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemViewTaskNotesActionPerformed

        String sqlC = "select A.* from Assignments A left join t_analysts T\n" + "on A.analyst = T.analyst\n" + "where T.active = 1\n" + "order by A.symbol";
        loadTable(sqlC, tasksTable);

        String tabName = TASKS_TABLE_NAME;
        Tab tab = tabs.get(tabName);
        float[] colWidthPercent = tab.getColWidthPercent();
        JTable table = tab.getTable();
        setColumnFormat(colWidthPercent, table);

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);
    }//GEN-LAST:event_menuItemViewTaskNotesActionPerformed

    /**
     * btnClearAllFilterActionPerformed clear all filters
     *
     * @param evt
     */
    private void btnClearAllFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearAllFilterActionPerformed

        // clear all filters
        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        TableFilter filter = tab.getFilter();
        filter.clearAllFilters();
        filter.applyFilter();
        filter.applyColorHeaders();

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);

    }//GEN-LAST:event_btnClearAllFilterActionPerformed

    /**
     * jMenuItemOthersLoadDataActionPerformed
     *
     * @param evt
     */
    private void menuItemReloadDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReloadDataActionPerformed

        String tabName = getSelectedTabName();
        Tab tab = tabs.get(tabName);
        JTableCellRenderer cellRenderer = tab.getCellRenderer();
        ModifiedTableData data = tab.getTableData();

        // reload tableSelected from database
        JTable table = tab.getTable();
        loadTable(table);

        // clear cellrenderer
        cellRenderer.clearCellRender();

        // reload modified tableSelected data with current tableSelected model
        data.reloadData();

        // set label record information
        String recordsLabel = tab.getRecordsLabel();
        labelRecords.setText(recordsLabel);
    }//GEN-LAST:event_menuItemReloadDataActionPerformed

    /**
     * jArchiveRecordActionPerformed
     *
     * @param evt
     */
    private void menuItemArchiveRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemArchiveRecordActionPerformed

        int rowSelected = tasksTable.getSelectedRows().length;
        int[] rowsSelected = tasksTable.getSelectedRows();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);

        // Delete Selected Records from Assignments
        if (rowSelected != -1) {
            for (int i = 0; i < rowSelected; i++) {
                String analyst = (String) tasksTable.getValueAt(rowsSelected[i], 2);
                Integer selectedTask = (Integer) tasksTable.getValueAt(rowsSelected[i], 0); // Add Note to selected taskID
                String sqlDelete = "UPDATE " + database + "." + tasksTable.getName() + " SET analyst = \"\",\n"
                        + " priority=null,\n"
                        + " dateAssigned= '" + today + "',"
                        + " dateDone=null,\n"
                        + " notes= \'Previous " + analyst + "' " + "where ID=" + selectedTask;
                try {
                    statement.executeUpdate(sqlDelete);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please, select one task!");
        }
        // Archive Selected Records in Assignments Archive
        if (rowSelected != -1) {

            for (int i = 0; i < rowSelected; i++) {
                String sqlInsert = "INSERT INTO " + database + "." + task_notesTable.getName() + " (symbol, analyst, priority, dateAssigned,dateDone,notes) VALUES (";
                int numRow = rowsSelected[i];
                for (int j = 1; j < tasksTable.getColumnCount() - 1; j++) {
                    if (tasksTable.getValueAt(numRow, j) == null) {
                        sqlInsert += null + ",";
                    } else {
                        sqlInsert += "'" + tasksTable.getValueAt(numRow, j) + "',";
                    }
                }
                if (tasksTable.getValueAt(numRow, tasksTable.getColumnCount() - 1) == null) {
                    sqlInsert += null + ")";
                } else {
                    sqlInsert += "'" + tasksTable.getValueAt(numRow, tasksTable.getColumnCount() - 1) + "')";
                }
                try {
                    statement.executeUpdate(sqlInsert);
//                    logwind.addMessageWithDate(sqlInsert);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            loadTable(tasksTable);
            loadTable(task_notesTable);
            tasksTable.setRowSelectionInterval(rowsSelected[0], rowsSelected[rowSelected - 1]);
            JOptionPane.showMessageDialog(null, rowSelected + " Record(s) Archived!");

        } else {
            JOptionPane.showMessageDialog(null, "Please, select one task!");
        }
    }//GEN-LAST:event_menuItemArchiveRecordActionPerformed

    /**
     * jActivateRecordActionPerformed
     *
     * @param evt
     */
    private void menuItemActivateRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemActivateRecordActionPerformed

        int rowSelected = task_notesTable.getSelectedRows().length;
        int[] rowsSelected = task_notesTable.getSelectedRows();
        // Archive Selected Records in Assignments Archive
        if (rowSelected != -1) {

            for (int i = 0; i < rowSelected; i++) {
                String sqlInsert = "INSERT INTO " + database + "." + tasksTable.getName() + "(symbol, analyst, priority, dateAssigned,dateDone,notes) VALUES ( ";
                int numRow = rowsSelected[i];
                for (int j = 1; j < task_notesTable.getColumnCount() - 1; j++) {
                    if (task_notesTable.getValueAt(numRow, j) == null) {
                        sqlInsert += null + ",";
                    } else {
                        sqlInsert += "'" + task_notesTable.getValueAt(numRow, j) + "',";
                    }
                }
                if (task_notesTable.getValueAt(numRow, task_notesTable.getColumnCount() - 1) == null) {
                    sqlInsert += null + ")";
                } else {
                    sqlInsert += "'" + task_notesTable.getValueAt(numRow, task_notesTable.getColumnCount() - 1) + "')";
                }
                try {
                    statement.executeUpdate(sqlInsert);
//                    ana.getLogWindow().addMessageWithDate(sqlInsert);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
                }
            }

            task_notesTable.setRowSelectionInterval(rowsSelected[0], rowsSelected[0]);
            loadTable(task_notesTable);
            loadTable(tasksTable);

            JOptionPane.showMessageDialog(null, rowSelected + " Record(s) Activated!");

        } else {
            JOptionPane.showMessageDialog(null, "Please, select one task!");
        }
    }//GEN-LAST:event_menuItemActivateRecordActionPerformed

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
            comboBoxSearch.setModel(new DefaultComboBoxModel(searchFields));
        }
    }//GEN-LAST:event_tabbedPanelStateChanged

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

    /**
     * loadData
     */
    public void loadData() {
        loadTables(tabs);
    }

    /**
     * setTableListeners This adds mouselisteners and keylisteners to tables.
     *
     * @param table
     */
    public void setTableListeners(final JTable table) {

        // this adds a mouselistener to the tableSelected header
        JTableHeader header = table.getTableHeader();
        if (header != null) {
            header.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if (e.getClickCount() == 2) {
                        clearFilterDoubleClick(e, table);
                    }
                    System.out.println("print");
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
            });
        }

        // add mouselistener to the tableSelected
        table.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        // if left mouse clicks
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (e.getClickCount() == 2) {
                                filterByDoubleClick(table);
                            } else if (e.getClickCount() == 1) {
                                if (jLabelEdit.getText().equals("ON ")) {
                                    selectAllText(e);
                                }
                            }
                        } // end if left mouse clicks
                        // if right mouse clicks
                        else if (SwingUtilities.isRightMouseButton(e)) {
                            if (e.getClickCount() == 2) {

                                // make tableSelected editable
                                makeTableEditable(true);

                                // get selected cell
                                int columnIndex = table.columnAtPoint(e.getPoint()); // this returns the column index
                                int rowIndex = table.rowAtPoint(e.getPoint()); // this returns the rowIndex index
                                if (rowIndex != -1 && columnIndex != -1) {

                                    // make it the active editing cell
                                    table.changeSelection(rowIndex, columnIndex, false, false);

                                    selectAllText(e);

                                } // end not null condition

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
                }
        );

        // add tableSelected model listener
        table.getModel().addTableModelListener(new TableModelListener() {  // add tableSelected model listener every time the tableSelected model reloaded
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int col = e.getColumn();
                String tab = getSelectedTabName();
                JTable table = tabs.get(tab).getTable();
                ModifiedTableData data = tabs.get(tab).getTableData();
                Object oldValue = data.getOldData()[row][col];
                Object newValue = table.getModel().getValueAt(row, col);

                // disable the upload changes button
                btnUploadChanges.setEnabled(false);

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
                }
            }
        });

        // add keyListener to the tableSelected
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_F2) {

                    // I believe this is meant to toggle edit mode
                    // so I passed the conditional
                    makeTableEditable(jLabelEdit.getText().equals("ON ") ? false : true);
                }
            }
        });
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
            setTableListeners(tabs.get(entry.getKey()).getTable());
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
            filter.addFilterItem(columnIndex, selectedField);
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
            case TASKS_TABLE_NAME: {
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i == 2 || i == 4 || i == 5 || i == 6) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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

            // Set the format for tableSelected task_notes
            case TASKNOTES_TABLE_NAME: {
                for (int i = 0; i < width.length; i++) {
                    int pWidth = Math.round(width[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i == 3) {
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
    public void updateTable(JTable table, List<ModifiedData> modifiedDataList) {

        // should probably not be here
        // this method is to update the database, that is all it should do.
        table.getModel().addTableModelListener(table);

        //String uploadQuery = uploadRecord(tableSelected, modifiedDataList);
        String sqlChange = null;

        for (ModifiedData modifiedData : modifiedDataList) {

            String tableName = modifiedData.getTableName();
            String columnName = modifiedData.getColumnName();
            Object value = modifiedData.getValue();
            int id = modifiedData.getId();

            try {

                if ("".equals(value)) {
                    value = null;
                    sqlChange = "UPDATE " + tableName + " SET " + columnName
                            + " = " + value + " WHERE taskID = " + id + ";";
                } else {
                    sqlChange = "UPDATE " + tableName + " SET " + columnName
                            + " = '" + value + "' WHERE taskID = " + id + ";";
                }
                System.out.println(sqlChange);
                statement.executeUpdate(sqlChange);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Upload failed!");
                logWindow.addMessageWithDate(e.getMessage());
                logWindow.addMessageWithDate(e.getSQLState() + "\n");
            }
        }

        JOptionPane.showMessageDialog(this, "Edits uploaded!");

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
    private void setKeyboardFocusManager() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (!addRecordWindowShow) {
                    if (jLabelEdit.getText().equals("ON ")) {
                        if (e.getKeyCode() == KeyEvent.VK_TAB) {
                            if (e.getComponent() instanceof JTable) {
                                JTable table = (JTable) e.getComponent();
                                table.setFocusTraversalKeysEnabled(false);
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
                            }

                        } else if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {                     // Default Date input with today's date
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
                        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                            if (e.getComponent() instanceof JTable) {
                                JTable table = (JTable) e.getComponent();
                                table.setFocusTraversalKeysEnabled(false);

                                // make sure in editing mode
                                if (!table.isEditing()
                                        && e.getID() == KeyEvent.KEY_PRESSED) {

                                    // if finished display dialog box
                                    // Upload Changes? Yes or No?
                                    Object[] options = {"Commit", "Revert"};  // the titles of buttons

                                    // store selected rowIndex before the tableSelected is refreshed
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
                                            makeTableEditable(false); // exit edit mode;
                                            break;
                                        case 1:
                                            // if Revert, revert changes
                                            loadTable(table); // reverts the model back
                                            makeTableEditable(false); // exit edit mode;

                                            break;
                                        default:
                                            // do nothing -> cancel
                                            break;
                                    }

                                    // highligh previously selected rowIndex
                                    if (rowIndex != -1) {
                                        table.setRowSelectionInterval(rowIndex, rowIndex);
                                    }
                                }

                                // if enter is pressed then enable upload changes button
                                btnUploadChanges.setEnabled(true);
                            }

                        }
                    } else {
                        if (e.getKeyCode() == KeyEvent.VK_TAB
                                || e.getKeyCode() == KeyEvent.VK_LEFT
                                || e.getKeyCode() == KeyEvent.VK_RIGHT
                                || e.getKeyCode() == KeyEvent.VK_UP
                                || e.getKeyCode() == KeyEvent.VK_DOWN) {

                            if (e.getComponent() instanceof JTable) {

                                    JTable tableSelected = (JTable) e.getComponent();
                                    
                                if (e.getID() == KeyEvent.KEY_RELEASED) {
                                    
                                    popupWindowShowInDiffTable(tableSelected);  //determind which tableSelected to show popup window
                                    
                                }
                            }
                        }
                    }
                }
                return false;
            }
        }
        );
    }
    
    private void popupWindowShowInDiffTable(JTable table) {
        
        int column = table.getSelectedColumn();

        if (table.getName().equals(TASKS_TABLE_NAME)) {

            if (table.getColumnName(column).equals("title")
                    || table.getColumnName(column).equals("description")
                    || table.getColumnName(column).equals("instructions")) {
                // popup tableSelected cell edit window
                tableCellPopupWindow.tableCellPopup(table);

            } else {
                tableCellPopupWindow.setTableCellPopupWindowVisible(false);
            }
        } else if (table.getName().equals(TASKFILES_TABLE_NAME)) {

            if (table.getColumnName(column).equals("files")
                    || table.getColumnName(column).equals("notes")
                    || table.getColumnName(column).equals("path")) {
                // popup tableSelected cell edit window
                tableCellPopupWindow.tableCellPopup(table);
                
            } else {
                tableCellPopupWindow.setTableCellPopupWindowVisible(false);
            }
        } else if (table.getName().equals(TASKNOTES_TABLE_NAME)) {

            if (table.getColumnName(column).equals("status_notes")) {
                // popup tableSelected cell edit window
                tableCellPopupWindow.tableCellPopup(table);
                
            } else {
                tableCellPopupWindow.setTableCellPopupWindowVisible(false);
            }
        }
    }
    
    public TableCellPopupWindow getPopupWindow(){
        return this.tableCellPopupWindow;
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
        return tabbedPanel.getTitleAt(tabbedPanel.getSelectedIndex());
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void showDatabase() {
        databaseLabel = new JLabel("                                                                        "
                + "                                                                          "
                + "                                                                      " + database);
        menuBar.add(databaseLabel);
    }

    public void setLogWindow(LogWindow logWindow) {
        this.logWindow = logWindow;
    }

    public Statement getStatement() {
        return statement;
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

        boolean isFirstTabRecordLabelSet = false;

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
            totalRecords = table.getRowCount();
            tab.setTotalRecords(totalRecords);

            if (isFirstTabRecordLabelSet == false) {
                String recordsLabel = tab.getRecordsLabel();
                labelRecords.setText(recordsLabel);
                isFirstTabRecordLabelSet = true; // now its set
            }
        }

        return tabs;
    }

    /**
     * loadTables This method takes a tabs Map and loads all the tabs/tables
     *
     * @param tabs
     * @return
     */
    public Map<String, Tab> loadTables(Map<String, Tab> tabs) {

        for (Map.Entry<String, Tab> entry : tabs.entrySet()) {
            Tab tab = tabs.get(entry.getKey());
            JTable table = tab.getTable();
            loadTable(table);
            setTableListeners(table);
        }

        setLastUpdateTime();

        return tabs;
    }

    /**
     * loadTable This method takes a tableSelected and loads it Does not need to
     * pass the tableSelected back since it is passed by reference However, it
     * can make the code clearer and it's good practice to return
     *
     * @param table
     */
    public JTable loadTable(JTable table) {

        String sql = "SELECT * FROM " + table.getName() + " ORDER BY taskID ASC";
        loadTable(sql, table);

        return table;
    }

    public JTable loadTable(String sql, JTable table) {

        Vector data = new Vector();
        Vector columnNames = new Vector();
        int columns;

        ResultSet rs = null;
        ResultSetMetaData metaData = null;
        try {
            rs = statement.executeQuery(sql);
            metaData = rs.getMetaData();
        } catch (Exception ex) {
            System.out.println("SQL Error:");
            ex.printStackTrace();
        }
        try {
            columns = metaData.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                columnNames.addElement(metaData.getColumnName(i));
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
            System.out.println("SQL Error:");
            ex.printStackTrace();
        }

        EditableTableModel model = new EditableTableModel(data, columnNames);

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
        setTableListeners(table);

        // update last time the tableSelected was updated
        setLastUpdateTime();

        System.out.println("Table loaded succesfully");

        return table;
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

            // close the sql statement
            sqlDelete += ");";

            try {

                // delete records from database
                statement.executeUpdate(sqlDelete);

                // refresh tableSelected and retain filters
                loadTable(table);

                // output pop up dialog that a record was deleted 
                JOptionPane.showMessageDialog(this, rowCount + " Record(s) Deleted");

                // set label record information
                String tabName = getSelectedTabName();
                Tab tab = tabs.get(tabName);
                tab.subtractFromTotalRowCount(rowCount); // update total rowIndex count
                String recordsLabel = tab.getRecordsLabel();
                labelRecords.setText(recordsLabel); // update label

            } catch (SQLException e) {
                System.out.println("SQL Error:");
                e.printStackTrace();

                // output pop up dialog that there was an error 
                JOptionPane.showMessageDialog(this, "There was an SQL Error.");
            }
        }
        return sqlDelete;
    }

    // @formatter:off
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel addPanel_control;
    private javax.swing.JButton btnAddRecords;
    private javax.swing.JButton btnBatchEdit;
    private javax.swing.JButton btnCancelEditMode;
    private javax.swing.JButton btnCancelSQL;
    private javax.swing.JButton btnClearAllFilter;
    private javax.swing.JButton btnCloseSQL;
    private javax.swing.JButton btnEnterSQL;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSwitchEditMode;
    private javax.swing.JButton btnUploadChanges;
    private javax.swing.JComboBox comboBoxSearch;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelEdit;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelEdit;
    private javax.swing.JPanel jPanelSQL;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextAreaSQL;
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
    private javax.swing.JMenuItem menuItemDeleteRecord;
    private javax.swing.JCheckBoxMenuItem menuItemLogChkBx;
    private javax.swing.JMenuItem menuItemLogOff;
    private javax.swing.JMenuItem menuItemManageDBs;
    private javax.swing.JMenuItem menuItemPrintDisplay;
    private javax.swing.JMenuItem menuItemPrintGUI;
    private javax.swing.JMenuItem menuItemReloadData;
    private javax.swing.JMenuItem menuItemRepBugSugg;
    private javax.swing.JCheckBoxMenuItem menuItemSQLCmdChkBx;
    private javax.swing.JMenuItem menuItemSaveFile;
    private javax.swing.JMenuItem menuItemVersion;
    private javax.swing.JMenuItem menuItemViewAllTasks;
    private javax.swing.JMenuItem menuItemViewTaskFiles;
    private javax.swing.JMenuItem menuItemViewTaskNotes;
    private javax.swing.JMenuItem menuItemViewTasks;
    private javax.swing.JMenu menuPrint;
    private javax.swing.JMenu menuReports;
    private javax.swing.JMenu menuSelectConn;
    private javax.swing.JMenu menuTools;
    private javax.swing.JMenu menuView;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JTabbedPane tabbedPanel;
    private javax.swing.JTable task_filesTable;
    private javax.swing.JTable task_notesTable;
    private javax.swing.JTable tasksTable;
    private javax.swing.JTextField textFieldForSearch;
    // End of variables declaration//GEN-END:variables
    // @formatter:on

    private void initTableCellPopupWindow() {

        tableCellPopupWindow = new TableCellPopupWindow();
        tableCellPopupWindow.initTableCellPopup(this);

        tableCellPopupWindow.setTableListener(tasksTable);
        tableCellPopupWindow.setTableListener(task_filesTable);
        tableCellPopupWindow.setTableListener(task_notesTable);

    }
    
    public boolean getAddRecordsWindowShow(){
        
        return this.addRecordWindowShow;
        
    }
    
    public void setAddRecordsWindowShow(boolean a){
        this.addRecordWindowShow = a;
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
