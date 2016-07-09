package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.dao.AbstractDAO;
import com.elle.ProjectManager.dao.IssueDAO;
import com.elle.ProjectManager.dao.ReferenceDAO;
import com.elle.ProjectManager.database.ModifiedTableData;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.presentation.IssueWindow;
import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * Tab This class is used to create a tab object. This object contains all the
 * components of the tab on Analyster. Each tab may have its own attributes and
 * that is what this class is for.
 *
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class Tab implements ITableConstants {
    
    //reference to PMwindow
    ProjectManagerWindow pmWindow;

    // attributes
    private String tableName;                    // name of the JTable
    private JTable table;                        // the JTable on the tab
    
    // for pm window display
    private int totalRecords;                    // total records in table model
    private int recordsShown;                    // number of records shown on table
    
    //array data members
    private float[] colWidthPercent;             // column width for each colum
    private String[] tableColNames;              // column header names
    private String[] searchFields;               // search combobox options
    private String[] batchEditFields;            // batch edit combobox options
    
    //object data members
    private TableFilter filter;                  // filter used for the table
    private ColumnPopupMenu columnPopupMenu;     // column filter pop up menu
    private JTableCellRenderer cellRenderer;     // table cell renderer
    private ModifiedTableData modTableData;         // modified table data object
                 

    // these items are enabled differently for each tab
    private boolean activateRecordMenuItemEnabled; // enables activate record menu item
    private boolean archiveRecordMenuItemEnabled;  // enables archive record menu item
    private boolean addRecordsBtnVisible;          // sets the add records button visible
    private boolean batchEditBtnVisible;           // sets the batch edit button visible
    private boolean batchEditBtnEnabled;           // sets the batch edit button enabled
    private boolean AddRecordsBtnEnabled;          // sets the Add Records button enabled
    
    
    // each tab can either be editing or not
    private boolean Editing;

    // batch edit window states
    private boolean batchEditWindowOpen;
    private boolean batchEditWindowVisible;

    /**
     * CONSTRUCTOR Tab This is used if no table is ready such as before
     * initComponents of a frame.
     */
    public Tab() {
        tableName = "";
        table = new JTable();
        totalRecords = 0;
        recordsShown = 0;
        activateRecordMenuItemEnabled = false;
        archiveRecordMenuItemEnabled = false;
        addRecordsBtnVisible = false;
        batchEditBtnVisible = false;
        batchEditBtnEnabled = true;
        batchEditWindowOpen = false;
        batchEditWindowVisible = false;
    }

    /**
     * CONSTRUCTOR This would be the ideal constructor, but there are issues
     * with the initcomponents in Analyster so the tab must be initialized first
     * then the table can be added
     *
     * @param table
     */
    public Tab(JTable table) throws IOException, BadLocationException {
        //set up reference to main window
        pmWindow = ProjectManagerWindow.getInstance();
        
        //set up table
        this.table = table;
        tableName = table.getName();
        
        //setup tab data
        setUpTabData();
        
    }
    
    
    private void setUpTabData() throws IOException, BadLocationException {
        //set up data array
        
        if (tableName.equals("References")) {
            setSearchFields(REFERENCES_SEARCH_FIELDS);
            setBatchEditFields(REFERENCES_BATCHEDIT_CB_FIELDS);
            setColWidthPercent(COL_WIDTH_PER_REFERENCES);
        }
        
        else{
            setSearchFields(TASKS_SEARCH_FIELDS);
            setBatchEditFields(TASKS_BATCHEDIT_CB_FIELDS);
            setColWidthPercent(COL_WIDTH_PER_TASKS);
        }
       
        setTableColNames(table);
        
        // set up boolean values
        activateRecordMenuItemEnabled = false;
        archiveRecordMenuItemEnabled = true;
        addRecordsBtnVisible = true;
        batchEditBtnVisible = true;
        batchEditBtnEnabled = true;
        batchEditWindowOpen = false;
        batchEditWindowVisible = false;
        
        //set up objects
        filter = new TableFilter(this);
        columnPopupMenu = new ColumnPopupMenu(this);
        cellRenderer = new JTableCellRenderer(table);
        modTableData = new ModifiedTableData(table);
        
        //initialize counts
        totalRecords = 0;
        recordsShown = 0;
        
        //load table data
        loadTableData();
        
        //set up table listeners
        setTableListeners();
        
        //detect open issues
        
        //Editing false 
        Editing = false;
        
    }
    
    
    private void loadTableData() throws IOException, BadLocationException {
         System.out.println("now loading..." + tableName);
        
        // set table model data
        
        AbstractDAO dao;
        if (tableName.equals("References")) {
            dao = new  ReferenceDAO();
        }
        else dao = new IssueDAO();
        // this is for all the tabs which are all from the issues table
       
        ArrayList<Issue> issues = dao.get(tableName);
       
        for (Issue issue : issues) {
            insertRow(issue);
        }
        
        addEditableTableModel(table);
        
         // apply filter
        
        if (filter.getFilterItems() == null) {
            filter.initFilterItems();
        }
        filter.applyFilter();
        filter.applyColorHeaders();

        // load all checkbox items for the checkbox column pop up filter
        columnPopupMenu.loadAllCheckBoxItems();

        // set column format
        setColumnFormat();

        // update last time the tableSelected was updated
        setLastUpdateTime();
     
    }
    
    /*
    ** insert, update, and delete row
    */
    public void insertRow(Issue issue) throws IOException, BadLocationException {
        
       
        Object[] rowData = new Object[13];
        rowData[0] = issue.getId();
        rowData[1] = issue.getApp();
        rowData[2] = issue.getTitle();
        byte[] descriptiontablebytesout;

        if (issue.getDescription() == null) {
            descriptiontablebytesout = new byte[0];
        } else {
            descriptiontablebytesout = issue.getDescription();
        }
        
        InputStream descriptiontablestream = new ByteArrayInputStream(descriptiontablebytesout);
        String convertedstrings = convertStreamToString(descriptiontablestream);
        
        String rtfsign = "\\par";
        boolean rtfornot = convertedstrings.contains(rtfsign);
        
        if (rtfornot) {
            RTFEditorKit rtfParser = new RTFEditorKit();
            Document document = rtfParser.createDefaultDocument();
            rtfParser.read(new ByteArrayInputStream(descriptiontablebytesout), document, 0);
            String text = document.getText(0, document.getLength());
            rowData[3] = text;
        } else {
            rowData[3] = convertedstrings;
        }

        rowData[4] = issue.getProgrammer();
        rowData[5] = issue.getDateOpened();
        rowData[6] = issue.getRk();
        rowData[7] = issue.getVersion();
        rowData[8] = issue.getDateClosed();
        rowData[9] = issue.getIssueType();
        rowData[10] = issue.getSubmitter();
        rowData[11] = issue.getLocked();
        rowData[12] = issue.getLastmodtime();
        ((DefaultTableModel)table.getModel()).addRow(rowData);
        
        addToTotalRowCount(1);
        
    }
    
    public void updateRow(Issue issue) throws IOException, BadLocationException {
        int row = findTableModelRow(issue);
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
            
            byte[] descriptiontablebytesout = issue.getDescription();
            InputStream descriptiontablestream = new ByteArrayInputStream(descriptiontablebytesout);
            RTFEditorKit rtfParser = new RTFEditorKit();
            Document document = rtfParser.createDefaultDocument();
            rtfParser.read(descriptiontablestream, document, 0);
            String text = document.getText(0, document.getLength());
            model.setValueAt(text, row, 3);
            
            
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

    
    
    
    /*
    **register table listeners
    */
    
    private void setTableListeners() {

        setTableHeaderListeners();
        setTableBodyListeners();
    }
       
    private void setTableHeaderListeners() {
        // this adds a mouselistener to the tableSelected header
        JTableHeader header = table.getTableHeader();
        //disable default mouse listeners
        MouseListener[] listeners = header.getMouseListeners();

        for (MouseListener ml: listeners)
        {
            String className = ml.getClass().toString();
            if (className.contains("BasicTableHeaderUI"))
                header.removeMouseListener(ml);
        }

        //add customized mouselistener
     
        if (header != null) {
            header.addMouseListener(new MouseAdapter() {
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    //double click header : clear filter for the column
                    if (e.getClickCount() == 2) {
                        e.consume();
                        int columnIndex = table.getColumnModel().getColumnIndexAtX(e.getX());
                        clearColumnFilter(columnIndex);
                    }
                    
                    //ctrl + click header: show popupmenu
                    if (e.getClickCount() == 1 && !e.isConsumed() && e.isControlDown()) {
                        e.consume();
                        columnPopupMenu.showPopupMenu(e);
                        
                    }
                    
                    //click : sort asc or desc column
                    if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1 && !e.isConsumed()) {
                        int columnIndex = header.columnAtPoint(e.getPoint());
                        if (columnIndex != -1) {
                                columnIndex = table.convertColumnIndexToModel(columnIndex);
                                table.getRowSorter().toggleSortOrder(columnIndex);

                        }
                        e.consume();
                    }
 
                }

                //right mouse click for showing popupmenu
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        columnPopupMenu.showPopupMenu(e);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                   
                    if (e.isPopupTrigger()) {
                        // this calls the column popup menu
                        columnPopupMenu.showPopupMenu(e);
                    }
                }
            });
        }     
        
    }
    
    private void setTableBodyListeners() {
        table.addMouseListener(new MouseAdapter() {

            boolean isClickOnce, doubleClick;
            long theFirstClick = 0, theSecondClick = 0;

            @Override
            public void mouseClicked(MouseEvent e) {

                // if left mouse clicks
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        
                        //ctrl + double click  : filter
                        if (e.isControlDown()) {
                            filterByDoubleClick();
                        } else {
                             //double click : open issue window
                            if (e.getComponent() instanceof JTable) {     
                                int row = table.getSelectedRow();
                                int col = table.getSelectedColumn();

                                boolean isIssueAlreadyOpened = false;
                            }
                        }
                    }
                    else {
                        if (e.getClickCount() == 1) {
                            if (e.getComponent() instanceof JTable) {
                                JTable table = (JTable) e.getSource();
                                int[] rows = table.getSelectedRows();

                                if (rows.length >= 2) {

                                //btnBatchEdit.setEnabled(true);
                                //btnBatchEdit.setVisible(true);
                                } else {
                                //btnBatchEdit.setEnabled(false);
                                //btnBatchEdit.setVisible(false);
                                }
                            }
                        }
                        
                    }
                }

                // end if left mouse clicks
                // if right mouse clicks
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (e.getClickCount() == 2) {

                          if (Editing) {

                            // set the states for this tab
                           // makeTableEditable(true);
                        //    setEnabledEditingButtons(true, true, true);
                        //    setBatchEditButtonStates(tab);

                            // set the color of the edit mode text
                        //    editModeTextColor(tab.isEditing());

                            // get selected cell for editing
                            int columnIndex = table.columnAtPoint(e.getPoint()); // this returns the column index
                            int rowIndex = table.rowAtPoint(e.getPoint()); // this returns the rowIndex index
                            if (rowIndex != -1 && columnIndex != -1) {

                                // make it the active editing cell
                                table.changeSelection(rowIndex, columnIndex, false, false);

                                //selectAllText(e);

                                // if cell is being edited
                                // cannot cancel or upload or revert
                               // setEnabledEditingButtons(false, false, false);

                            } // end not null condition

                        } // end of is tab editing conditions

                    } // end if 2 clicks 
                } // end if right mouse clicks
            }
        });
    }
    
    private void clearColumnFilter(int columnIndex) {
        
        filter.clearColFilter(columnIndex);
        filter.applyFilter();
        //reset recordsLabel
        setLabelRecords();
    }
    
    private void filterByDoubleClick() {

        int columnIndex = table.getSelectedColumn(); // this returns the column index
        int rowIndex = table.getSelectedRow(); // this returns the rowIndex index
        if (rowIndex != -1) {
            Object selectedField = table.getValueAt(rowIndex, columnIndex);
  
            filter.addFilterItem(columnIndex, selectedField);
            filter.applyFilter();
            
            //need to reset labelRecords
            setLabelRecords();
        }
    }
    
    
    
    /*
    ** updating main window jcomponents elements
    */
    
    private void setLastUpdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        pmWindow.getLabelTimeLastUpdate().setText("Last updated: " + time);
    }
    
    public void setLabelRecords() {
        pmWindow.getLabelRecords().setText(getRecordsLabel());
    } 
    
            
    private int findTableModelRow(Issue issue) {
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
    
    
    
    
    
    private void setColumnFormat() {

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

        switch (tableName) {

            // Set the format for tableSelected task.
            case "PM": 
            case "ELLEGUI": 
            case "Analyster": 
            case "Other": {
                for (int i = 0; i < colWidthPercent.length; i++) {
                    int pWidth = Math.round(colWidthPercent[i]);
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
            
            case "References": {
                // hide columns
                Set<Integer> hideCols = new HashSet();
                hideCols.add(1);
                for(int temp = 6; temp<=12; temp++)
                    hideCols.add(temp);
                
                for (int i = 0; i < colWidthPercent.length; i++) {
                    int pWidth = Math.round(colWidthPercent[i]);
                    table.getColumnModel().getColumn(i).setPreferredWidth(pWidth);
                    if (i == 2 || i == 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    
                    if(hideCols.contains(i)){
                        TableColumn column = table.getColumnModel().getColumn(i);
                        column.setMinWidth(pWidth);
                        column.setMaxWidth(pWidth);
                        column.setPreferredWidth(pWidth);
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
    
    private String convertStreamToString(InputStream is) throws IOException {
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader;
                reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }
        return "";
    }
    
    private void addEditableTableModel(JTable table) {
        
        DefaultTableModel model = (DefaultTableModel)table.getModel();        
        EditableTableModel etm = new EditableTableModel(model);
        table.setModel(etm);
    }

    /**
     * ************************************************************************
     *************************** Methods **************************************
     * ************************************************************************
     */
    /**
     * This method subtracts an amount from the totalRecords value This is used
     * when records are deleted to update the totalRecords value
     *
     * @param amountOfRecordsDeleted
     */
    public void subtractFromTotalRowCount(int amountOfRecordsDeleted) {
        totalRecords = totalRecords - amountOfRecordsDeleted;
    }

    /**
     * This method subtracts an amount from the totalRecords value This is used
     * when records are deleted to update the totalRecords value
     *
     * @param amountOfRecordsAdded
     */
    public void addToTotalRowCount(int amountOfRecordsAdded) {
        totalRecords = totalRecords + amountOfRecordsAdded;
    }

    /**
     * This method returns a string that displays the records.
     *
     * @return String This returns a string that has the records for both total
     * and shown
     */
    public String getRecordsLabel() {

        String output;

        switch (getTableName()) {
//            case TASKS_TABLE_NAME:
//                output = "<html><pre>"
//                        + "     Number of records shown: " + getRecordsShown()
//                        + "<br/> Number of records in Issues: " + getTotalRecords()
//                        + "</pre></html>";
//                System.out.println(output);
//                break;
            case "PM":
                output = "<html><pre>"
                        + "     Number of records shown: " + getRecordsShown()
                        + "<br/> Number of records in Issues: " + getTotalRecords()
                        + "</pre></html>";
                break;
            case "ELLEGUI":
                output = "<html><pre>"
                        + "     Number of records shown: " + getRecordsShown()
                        + "<br/> Number of records in Issues: " + getTotalRecords()
                        + "</pre></html>";
                break;
            case "Analyster":
                output = "<html><pre>"
                        + "     Number of records shown: " + getRecordsShown()
                        + "<br/> Number of records in Issues: " + getTotalRecords()
                        + "</pre></html>";
                break;
            case "Other":
                output = "<html><pre>"
                        + "     Number of records shown: " + getRecordsShown()
                        + "<br/> Number of records in Issues: " + getTotalRecords()
                        + "</pre></html>";
                break;

            
            case "References":
                output = "<html><pre>"
                        + "      Number of records shown: " + getRecordsShown()
                        + "<br/> Number of records in Issues: " + getTotalRecords()
                        + "</pre></html>";
                break;
            default:
                // this means an invalid table name constant was passed
                // this exception will be handled and thrown here
                // the program will still run and show the stack trace for debugging
                output = "<html><pre>"
                        + "*******ATTENTION*******"
                        + "<br/>Not a valid table name constant entered"
                        + "</pre></html>";
                try {
                    String errorMessage = "ERROR: unknown table";
                    throw new NoSuchFieldException(errorMessage);
                } catch (NoSuchFieldException ex) {
                    // post to log.txt
                    LoggingAspect.addLogMsgWthDate("1:" + ex.getMessage());
                    LoggingAspect.afterThrown(ex);
                }

                break;
        }

        return output;
    }
    
    
    
    /**
     * ************************************************************************
     ********************** Setters & Getters *********************************
     * ************************************************************************
     */
    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public TableFilter getFilter() {
        return filter;
    }

    public void setFilter(TableFilter filter) {
        this.filter = filter;
    }
    
    
    public float[] getColWidthPercent() {
        return colWidthPercent;
    }

    public void setColWidthPercent(float[] colWidthPercent) {
        this.colWidthPercent = colWidthPercent;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getRecordsShown() {
//        System.out.println("table name: " + getTableName() + " row count " +  getTable().getRowCount());
        return getTable().getRowCount();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isActivateRecordMenuItemEnabled() {
        return activateRecordMenuItemEnabled;
    }

    public void setActivateRecordMenuItemEnabled(boolean activateRecordMenuItemEnabled) {
        this.activateRecordMenuItemEnabled = activateRecordMenuItemEnabled;
    }

    public boolean isArchiveRecordMenuItemEnabled() {
        return archiveRecordMenuItemEnabled;
    }

    public void setArchiveRecordMenuItemEnabled(boolean archiveRecordMenuItemEnabled) {
        this.archiveRecordMenuItemEnabled = archiveRecordMenuItemEnabled;
    }

    public String[] getTableColNames() {
        return tableColNames;
    }

    public void setTableColNames(String[] tableColNames) {
        this.tableColNames = tableColNames;
    }

    public void setTableColNames(JTable table) {
        tableColNames = new String[table.getColumnCount()];
        for (int i = 0; i < table.getColumnCount(); i++) {
            tableColNames[i] = table.getColumnName(i);
        }
    }

    public String[] getSearchFields() {
        return searchFields;
    }

    public void setSearchFields(String[] searchFields) {
        this.searchFields = searchFields;
    }

    public boolean isAddRecordsBtnVisible() {
        return addRecordsBtnVisible;
    }

    public void setAddRecordsBtnVisible(boolean addRecordsBtnVisible) {
        this.addRecordsBtnVisible = addRecordsBtnVisible;
    }

    public ColumnPopupMenu getColumnPopupMenu() {
        return columnPopupMenu;
    }

    public void setColumnPopupMenu(ColumnPopupMenu ColumnPopupMenu) {
        this.columnPopupMenu = ColumnPopupMenu;
    }

    public String[] getBatchEditFields() {
        return batchEditFields;
    }

    public void setBatchEditFields(String[] batchEditFields) {
        this.batchEditFields = batchEditFields;
    }

    public boolean isBatchEditBtnVisible() {
        return batchEditBtnVisible;
    }

    public void setBatchEditBtnVisible(boolean batchEditBtnVisible) {
        this.batchEditBtnVisible = batchEditBtnVisible;
    }

    public JTableCellRenderer getCellRenderer() {
        return cellRenderer;
    }

    public void setCellRenderer(JTableCellRenderer cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    public ModifiedTableData getTableData() {
        return modTableData;
    }

    public void setTableData(ModifiedTableData tableData) {
        this.modTableData = tableData;
    }

    public boolean isEditing() {
        return Editing;
    }

    public void setEditing(boolean Editing) {
        this.Editing = Editing;
    }

    public boolean isBatchEditBtnEnabled() {
        return batchEditBtnEnabled;
    }

    public void setBatchEditBtnEnabled(boolean batchEditBtnEnabled) {
        this.batchEditBtnEnabled = batchEditBtnEnabled;
    }

    public boolean isBatchEditWindowOpen() {
        return batchEditWindowOpen;
    }

    public void setBatchEditWindowOpen(boolean batchEditWindowOpen) {
        this.batchEditWindowOpen = batchEditWindowOpen;
    }

    public boolean isBatchEditWindowVisible() {
        return batchEditWindowVisible;
    }

    public void setBatchEditWindowVisible(boolean batchEditWindowVisible) {
        this.batchEditWindowVisible = batchEditWindowVisible;
    }

    public boolean isAddRecordsBtnEnabled() {
        return AddRecordsBtnEnabled;
    }

    public void setAddRecordsBtnEnabled(boolean AddRecordsBtnEnabled) {
        this.AddRecordsBtnEnabled = AddRecordsBtnEnabled;
    }

}// end Tab



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
            

            label.setHorizontalAlignment(column == table.getColumnCount() - 1 ? JLabel.LEFT : JLabel.CENTER);
            return label;

        }

    }

