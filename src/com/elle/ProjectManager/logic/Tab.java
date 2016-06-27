package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.database.ModifiedTableData;
import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import javax.swing.JTable;

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

    // attributes
    private String tableName;                    // name of the JTable
    private JTable table;                        // the JTable on the tab
    private TableFilter filter;                  // filter used for the table
    private float[] colWidthPercent;             // column width for each column
    private int totalRecords;                    // total records in table model
    private int recordsShown;                    // number of records shown on table
    private String[] tableColNames;              // column header names
    private String[] searchFields;               // search combobox options
    private String[] batchEditFields;            // batch edit combobox options
    private ColumnPopupMenu ColumnPopupMenu;     // column filter pop up menu
    private JTableCellRenderer cellRenderer;     // table cell renderer
    private ModifiedTableData tableData;         // modified table data object

    // these items are enabled differently for each tab
    private boolean activateRecordMenuItemEnabled; // enables activate record menu item
    private boolean archiveRecordMenuItemEnabled;  // enables archive record menu item
    private boolean addRecordsBtnVisible;          // sets the add records button visible
    private boolean batchEditBtnVisible;           // sets the batch edit button visible
    private boolean batchEditBtnEnabled;           // sets the batch edit button enabled
    private boolean AddRecordsBtnEnabled;          // sets the Add Records button enabled
    private CustomIDList customIdList;
    
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
    public Tab(JTable table) {
        tableName = "";
        this.table = table;
        totalRecords = 0;
        recordsShown = 0;
        filter = new TableFilter(table);
//        ColumnPopupMenu = new ColumnPopupMenu(filter);
        
        // store the column names for the table
        for (int i = 0; i < table.getColumnCount(); i++) {
            tableColNames[i] = table.getColumnName(i);
            System.out.println("this is" + tableName + " and " + tableColNames[i]);
        }

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
    
    public CustomIDList getCustomIdList(){
        return this.customIdList;
    }

    public void setCustomIdList(CustomIDList cIdList){
        this.customIdList = cIdList;
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
        return ColumnPopupMenu;
    }

    public void setColumnPopupMenu(ColumnPopupMenu ColumnPopupMenu) {
        this.ColumnPopupMenu = ColumnPopupMenu;
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
        return tableData;
    }

    public void setTableData(ModifiedTableData tableData) {
        this.tableData = tableData;
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

}// end Tab
