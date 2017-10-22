package com.elle.ProjectManager.logic;

import com.elle.ProjectManager.controller.PMDataManager;
import com.elle.ProjectManager.database.ModifiedData;
import com.elle.ProjectManager.database.ModifiedTableData;
import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;

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
    PMDataManager dataManager;

    // attributes
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
    private IdColumnRenderer idRenderer;             
 
    //Storing Jcomponents states
    private ButtonsState state;
    
   
    /**
     * CONSTRUCTOR Tab This is used if no table is ready such as before
     * initComponents of a frame.
     */
    public Tab() {
        
    }

    /**
     * CONSTRUCTOR This would be the ideal constructor, but there are issues
     * with the initcomponents in Analyster so the tab must be initialized first
     * then the table can be added
     *
     * @param table
     */
    public Tab(JTable table)  {
        //set up reference to main window
        pmWindow = ProjectManagerWindow.getInstance();
        dataManager = PMDataManager.getInstance();
        //set up table
        this.table = table;  
        //setup tab data
        setUpTabData();
        
        
        
    }
    
    
    private void setUpTabData()  {
        //set up data array
        
        if (table.getName().equals("References")) {
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
        
 
        /*set up inital components state*/
        /* addButton visible, 
           batchEdit invisible,  
           uploadchangebtn invisible, 
           revertchangebtn invisible,
           editmode false
        */
        state = new ButtonsState(true, false, false, false, false);
        
         //set up objects
        filter = new TableFilter(this);
        columnPopupMenu = new ColumnPopupMenu(this);
        cellRenderer = new JTableCellRenderer(table);
        idRenderer = IdColumnRenderer.getInstance();
        
        
        //initialize counts
        totalRecords = 0;
        recordsShown = 0;
        
        //load table data
        loadTableData();
        
        //after loading table data, set up modeTableData
        modTableData = new ModifiedTableData(table);
        
        //set up id renderer
        table.getColumnModel().getColumn(0).setCellRenderer(idRenderer);
        
        //set up table listeners
        setTableListeners();
        
       
     
    }
    
    //update table, reload table, load table functions
    private void loadTableData()  {
         System.out.println("now loading..." + table.getName());
        
         List<Object[]> tableData;
        // set table model data
        
        if (table.getName().equals("References")) {
            tableData = dataManager.getReferences();
            
        }
        else {
            tableData = dataManager.getIssues(table.getName());
            
        }
        
       
        for (Object[] rowData : tableData) {
            insertRow(rowData);
        }
        
        //make table editable 
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
    
    
    //the batch upload does not include description field
    public void uploadChanges() {
        //get changed rows
        ArrayList<Object[]> changedRowsData = new ArrayList();
        List<ModifiedData> modifiedDataList = modTableData.getNewData();
        
        //loop the modified data list
        for (ModifiedData modifiedData : modifiedDataList) {
            int id = modifiedData.getId();
            
            int rowIndex = getRowIndex(id);
            //get the row data
            if (rowIndex != -1) {
                Object[] rowData = new Object[table.getColumnCount()];
                for (int i = 0; i < table.getColumnCount(); i++) {
                    //set null to description field
                    if( i == 3) rowData[i] = null;
                    else
                        rowData[i] = table.getValueAt(rowIndex, i);
                }
                changedRowsData.add(rowData);
            }
        }
        
        //upload changes   
        String tableName = table.getName();
        if (!tableName.equals("References")) {
            dataManager.updateIssues(changedRowsData);
        }
        else{
            dataManager.updateReferences(changedRowsData);
        }
         
            int[] rows = table.getSelectedRows();
 
            ListSelectionModel model = table.getSelectionModel();
            model.clearSelection();
            for (int r = 0; r < rows.length; r++) {
                model.addSelectionInterval(rows[r], rows[r]);
            }

            // clear cellrenderer
            cellRenderer.clearCellRender();

            // reload modified tableSelected data with current tableSelected model
            modTableData.reloadData();

            //makeTableEditable(labelEditModeState.getText().equals("OFF") ? true : false);
            modTableData.getNewData().clear();    // reset the arraylist to record future changes
            setLastUpdateTime();          // update time
            
            //set up btton state
            state.enableEdit(false);
            pmWindow.changeTabbedPanelState(this);

    }
    
    //get rowIndex for a particular issue id
    private int getRowIndex(int id) {
        int tableRowsCnt = table.getRowCount();
        for(int i = 0; i < tableRowsCnt; i++) {
            int field0 = (int) table.getValueAt(i, 0);
            if (field0 == id) {
                return i;
            }
        }
        return -1;
    }
    
    public void revertChanges() {
       
        
        modTableData.getNewData().clear();  // clear any stored changes (new data)
        loadTableData(); // reverts the model back

        LoggingAspect.afterReturn("Nothing has been Changed!");

        modTableData.reloadData();  // reloads data of new table (old data) to compare with new changes (new data)

    
    }
    
    public void reloadTable(){
        
        DefaultTableModel dm = (DefaultTableModel)table.getModel();
        //clear all current rows
        while (dm.getRowCount() > 0) {
            dm.removeRow(0);
        }
        //reset the total records count to 0
        setTotalRecords(0);
        
        
        //if table is sorted, save the info -Yi
        List<RowSorter.SortKey> keys = (List<RowSorter.SortKey>)table.getRowSorter().getSortKeys();
     
        try {
            reloadData();
        } catch (IOException ex) {
            Logger.getLogger(ProjectManagerWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(ProjectManagerWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //reset the sorter key -Yi
        table.getRowSorter().setSortKeys(keys);
        table.getColumnModel().getColumn(0).setCellRenderer(idRenderer);
       

        LoggingAspect.afterReturn(table.getName() + " is reloading");

    }
    
    
    //reload selected data
    public void reloadSelectedData() {
        int row = table.getSelectedRow();
        if(row == -1){
           
        }
        else{
            int id = (int)table.getValueAt(row, 0);
            Object[] rowData = null;
            if (table.getName().equals("References")) {
                rowData = dataManager.getReference(id);
            }
            else{
               rowData = dataManager.getIssue(id);
            }
            
            updateRow(rowData);
            LoggingAspect.afterReturn("Selected record #" + id + " is reloading");
          
       }
        
        
        
    }
    
    
    //reload the current tab data in table
    private void reloadData() throws IOException, BadLocationException {
        
        
        // reload tableSelected from database
        loadTableData();

        // clear cellrenderer
        cellRenderer.clearCellRender();

        // reload modified tableSelected data with current tableSelected model
        modTableData.reloadData();

        // set label record information
        setRecordsShownLabel();
        setTotalRecordsLabel();
    }
    
    /*
    ** insert, update, and delete row
    */
    public void insertRow(Object[] rowData) {
        
        if (rowData != null) {
            ((DefaultTableModel)table.getModel()).addRow(rowData);
            addToTotalRowCount(1);
        
        }
   
        
    }
    
    public void updateRow(Object[] rowData) {
        int row = findTableModelRow(rowData);
        if(row != -1 && rowData != null){
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            
            // remove table listeners because this listens for changes in 
            // table and changes the cell green for upload changes and revert
            // changes. So I remove them and then put them back.
            TableModelListener[] listeners = model.getTableModelListeners();
            for(int i = 0; i < listeners.length; i++){
                model.removeTableModelListener(listeners[i]);
            }

            // update -> no need for id
            model.setValueAt(rowData[1], row, 1);
            model.setValueAt(rowData[2], row, 2);
            
            model.setValueAt(rowData[3], row, 3);
            
            model.setValueAt(rowData[4], row, 4);
            model.setValueAt(rowData[5], row, 5);
            model.setValueAt(rowData[6], row, 6);
            model.setValueAt(rowData[7], row, 7);
            model.setValueAt(rowData[8], row, 8);
            model.setValueAt(rowData[9], row, 9);
            model.setValueAt(rowData[10], row, 10);
            model.setValueAt(rowData[11], row, 11);
            model.setValueAt(rowData[12], row, 12);
            
            // add back the table listeners
            for(int i = 0; i < listeners.length; i++){
                model.addTableModelListener(listeners[i]);
            }
            
            table.repaint();
        }
        else{
            //offline update , insert new row
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.addRow(rowData);
        }
    }

    public void deleteRow(int rowIndex){
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //need to convert to model index in case table is sorted
        int index  = table.convertRowIndexToModel(rowIndex);
        model.removeRow(index);
        subtractFromTotalRowCount(1);
        
    }
    
    
    
    /*
    **register table listeners
    */
    
    private void setTableListeners() {

        setTableHeaderListeners();
        setTableBodyListeners();
        setTableModelListener();
    }
       
    private void setTableHeaderListeners() {
        // this adds a mouselistener to the tableSelected header
        JTableHeader header = table.getTableHeader();
        //disable default mouse listeners
        MouseListener[] listeners = header.getMouseListeners();

        for (MouseListener ml: listeners)
        {
            String className = ml.getClass().toString();
            System.out.println(className);
            if (className.contains("BasicTableHeaderUI$MouseInputHandler"))
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
        Tab localTab = this;
        table.addMouseListener(new MouseAdapter() {
        
            @Override
            public void mouseClicked(MouseEvent e) {

                // if left mouse clicks
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        //select a row, then ctrl + double click  : filter based on selected value
                        if (e.isControlDown()) {
                            filterByDoubleClick();
                        } else {
                            //double click : open issue window
                            if (e.getComponent() instanceof JTable) {                                
                                int row = table.getSelectedRow();
                                pmWindow.openIssueWindow(row, localTab);
                            }
                        }
                    } else {
                        if (e.getClickCount() == 1) {
                            if (e.getComponent() instanceof JTable) {
                                JTable table = (JTable) e.getSource();
                                int[] rows = table.getSelectedRows();
                                
                                if (rows.length >= 2) {
                                    state.setBatchEditBtnVisible(true);
                                    pmWindow.changeTabbedPanelState(localTab);
                                    
                                } else {
                                    state.setBatchEditBtnVisible(false);
                                    pmWindow.changeTabbedPanelState(localTab);
                                }
                            }
                        }
                        
                    }
                } // end if left mouse clicks
                // if right mouse clicks
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (e.getClickCount() == 2) {
                        //this is to enable table edit
                        //commented out as requested by Professor
//                        state.enableEdit(true);
//                        
//                        pmWindow.changeTabbedPanelState(localTab);
//                        
//                        EditableTableModel model = ((EditableTableModel) table.getModel());
//                        
//                        // get selected cell for editing
//                        int columnIndex = table.columnAtPoint(e.getPoint()); // this returns the column index
//                        int rowIndex = table.rowAtPoint(e.getPoint()); // this returns the rowIndex index
//                        
//                        //column 3 : description column cannot be edited
//                        if (rowIndex != -1 && columnIndex != -1 && columnIndex != 3) {
//                            // make it the active editing cell
//                            
//                            table.changeSelection(rowIndex, columnIndex, false, false);
//                            model.setCellEditable(true);
//                            selectAllText(e);
                            
                       // } // end not null condition

                    } // end of is tab editing conditions

                } // end if 2 clicks 
            } // end if right mouse clicks
        
        private void selectAllText(MouseEvent e) {// Select all text inside jTextField

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
            
        
        });
  
        //drag to select multiple rows 
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getComponent() instanceof JTable) {
                    int[] rows = table.getSelectedRows();
                    if (rows.length >= 2) {
                        state.setBatchEditBtnVisible(true);
                        pmWindow.changeTabbedPanelState(localTab);

                    } else {
                        state.setBatchEditBtnVisible(true);
                        pmWindow.changeTabbedPanelState(localTab);
                    }
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
                    state.setAddBtnVisible(false);
                    state.setEditMode(true);
                    state.setRevertChangesBtnVisible(true);
                    state.setUploadChangesBtnVisible(true);
                    pmWindow.changeTabbedPanelState(localTab);
                    
                }
            }
        });
        
        
    }
    
    private void setTableModelListener() {
        
        table.getModel().addTableModelListener(new TableModelListener() {  // add tableSelected model listener every time the tableSelected model reloaded
            @Override
            public void tableChanged(TableModelEvent e) {

                int row = e.getFirstRow();
                int col = e.getColumn();

                ModifiedTableData data = modTableData;

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
                        cellRenderer.getCells().get(col).add(row);
                        table.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);

                    } 
                    
                }
            }
        });
    }
    
    
    /*end of registering table listeners*/
    
    
    /*
    **detect open issues 
    */
    public boolean detectOpenIssues() {
        //reference table do not need to detect open issues
        if (table.getName().equals("References")) return false;
        
        String userName = pmWindow.getUserName();
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
        return openIssue;
      
    }

    
    
    /* flitering related methods */
    //clear filter for a column
    private void clearColumnFilter(int columnIndex) {
        
        filter.clearColFilter(columnIndex);
        filter.applyFilter();
        
    }
    
    //filter a cell value by double click
    private void filterByDoubleClick() {

        int columnIndex = table.getSelectedColumn(); // this returns the column index
        int rowIndex = table.getSelectedRow(); // this returns the rowIndex index
        if (rowIndex != -1) {
            Object selectedField = table.getValueAt(rowIndex, columnIndex);
  
            filter.addFilterItem(columnIndex, selectedField);
            filter.applyFilter();
            
            
        }
    }
    
    
    
    /*
    ** updating main window jcomponents elements
    */
    
    //set up table last update time
    private void setLastUpdateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(new Date());
        pmWindow.getLabelTimeLastUpdate().setText("Last updated: " + time);
    }
    
   
    /*
    **table-related helper functions 
    */
    
    //find issue in table
    private int findTableModelRow(Object[] rowData) {
        int rowCount = table.getModel().getRowCount();
        TableModel model = table.getModel();
        for(int rowIndex = 0; rowIndex < rowCount; rowIndex++){
            int rowId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
            if(rowId == (int)rowData[0]){
                return rowIndex;
            }
        }
        return -1; // rowIndex not found
    }
    
    
    /*
    ** table layout
    */
    //set up table column layout
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

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        switch (table.getName()) {

            // Set the format for tableSelected task.
            case "PM": 
            case "ELLEGUI": 
            case "Analyster": 
            case "Other": {
                for (int i = 0; i < colWidthPercent.length; i++) {
                    int pWidth = Math.round(colWidthPercent[i]);
                    TableColumn column = table.getColumnModel().getColumn(i);
                    column.setPreferredWidth(pWidth);
                    
                    //Fixes the width of all columns except description 
                    String name = table.getColumnName(i);
                    if(!table.getColumnName(i).equalsIgnoreCase("description")){
                        column.setMinWidth(pWidth);
                        column.setMaxWidth(pWidth);
                    }
              
                    if (i == 2 || i == 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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
                    TableColumn column = table.getColumnModel().getColumn(i);
                    column.setPreferredWidth(pWidth);
                    
                    //Fixes the width of all columns except description 
                    if(!table.getColumnName(i).equalsIgnoreCase("description")){
                        column.setMinWidth(pWidth);
                        column.setMaxWidth(pWidth);
                    }
              
                    if (i == 2 || i == 3) {
                        table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
                    } else {
                        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                    }
                    
                }
                break;
            }

            
            default: {
                System.out.println("Load table error!");
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

    
    //get the unique values for search columns
    public Map loadingDropdownList() {

        Map<Integer, ArrayList<Object>> valueListMap = new HashMap();

        for (String searchField : searchFields) {

            for (int i = 0; i < table.getColumnCount(); i++) {
                if (table.getColumnName(i).equalsIgnoreCase(searchField)) {
                    valueListMap.put(i, new ArrayList<Object>());
                }
            }
        }
        for (int col : valueListMap.keySet()) {
            //for each search item, create a new drop down list
            ArrayList DropDownListValueForEachColumn = new ArrayList<Object>();

            String[] columnNames = tableColNames;
            TableModel tableModel = table.getModel();
            String colName = columnNames[col].toLowerCase();

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

            //make every item in drop down list unique
            Set<Object> uniqueValue = new HashSet<Object>(DropDownListValueForEachColumn);
            ArrayList uniqueList = new ArrayList<Object>(uniqueValue);
//                System.out.println(col + " " + uniqueList);
            valueListMap.put(col, uniqueList);
        }

        return valueListMap;

    }
    
     public void moveSelectedRowsToTheEnd() {
         
        int[] rows = table.getSelectedRows();
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
     
     
    public String getTotalRecordsLabel(){
       String output;

        switch (table.getName()) {

            case "PM":
            case "ELLEGUI":
            case "Analyster":
            case "Summarizer":
            case "Other":
                output = "Number of records in Issues: " + getTotalRecords();
                break;

            
            case "References":
                output = "Number of records in References: " + getTotalRecords();
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
     
     
    public String getRecordsShownLabel() {

        String output;        
        output =  "Number of records shown: " + getRecordsShown();
               
        return output;
    }
    
    
     
     //set up recordsShownLabel
    public void setRecordsShownLabel() {
        pmWindow.getRecordsShownLabel().setText(getRecordsShownLabel());
    }
    
    public void setTotalRecordsLabel(){
       pmWindow.getTotalRecordsLabel().setText(getTotalRecordsLabel());  
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

    public ButtonsState getState() {
        return state;
    }

    public void setState(ButtonsState state) {
        this.state = state;
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



