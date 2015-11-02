/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.database.DBConnection;
import com.elle.ProjectManager.database.ModifiedTableData;
import com.elle.ProjectManager.logic.ColumnPopupMenu;
import com.elle.ProjectManager.logic.Tab;
import com.elle.ProjectManager.logic.TableFilter;
import com.elle.ProjectManager.logic.Validator;
import com.elle.ProjectManager.presentation.LogWindow;
import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;

/**
 *
 * @author fuxiaoqian
 */
public class AddIssueWindow extends JFrame {

    // attributes
    private String[] columnNames;
    private float[] columnWidths;
    private int numRowsAdded;           // number of rows added counter
    private Map<String, Tab> tabs;       // used to update the records label
    private Statement statement;

    private Object[] formValues;

    // components
    private ProjectManagerWindow projectManager;
    private LogWindow logWindow;
    private DefaultTableModel model;
    private int rowInView;

    private Color defaultSelectedBG;

    private ArrayList<Integer> CellsNotEmpty; // only includes rows that have data
    private boolean notEmpty;

//    private PopupWindowInTableCell tableCellPopupWindow;
    private JTable table;

    private Map<String, JLabel> labelsInForm;
    private Map<String, JTextArea> textAreasInForm;

//    private ArrayList line;
    // used to notify if the tableSelected is editing
    // the tableSelected.isEditing method has issues from the tableModelListener
    private boolean isEditClicked;

//    private String lastEditColumn = "";
    /**
     * Creates new form AddRecordsWindow
     */
    public AddIssueWindow() {

        CellsNotEmpty = new ArrayList<>();
        notEmpty = false;
        isEditClicked = false;
        formValues = null;

        projectManager = ProjectManagerWindow.getInstance();
        logWindow = projectManager.getLogWindow();
        tabs = projectManager.getTabs();
        statement = projectManager.getStatement();

        table = new JTable();

//        columnNames = new String[projectManager.getSelectedTable().getColumnCount()];
        // set the selected tableSelected name
        table.setName(projectManager.getSelectedTabName());

        // get default selected bg color
        defaultSelectedBG = table.getSelectionBackground();

        // initialize components
        initComponents();

        // create a new empty tableSelected
        createTable();

        createEmptyForm();

        addIssueMode(true);

        defaultSetting();

//        copyPasteAndCut();
//        //sets the keyboard focus manager
//        setKeyboardFocusManager(this);
        // set the label header
        this.setTitle("Add Issue");

        this.setPreferredSize(new Dimension(445, 710));

        // set this window to appear in the middle of Project Manager
        this.setLocationRelativeTo(projectManager);

        this.pack();
    }

    public AddIssueWindow(Object[] cellsValue, int id, int row) {
        CellsNotEmpty = new ArrayList<>();
        notEmpty = false;
        isEditClicked = false;

        formValues = cellsValue;
        rowInView = row;

        projectManager = ProjectManagerWindow.getInstance();
        logWindow = projectManager.getLogWindow();
        tabs = projectManager.getTabs();
        statement = projectManager.getStatement();

        table = new JTable();

        // set the selected tableSelected name
        table.setName(projectManager.getSelectedTabName());

        // get default selected bg color
        defaultSelectedBG = table.getSelectionBackground();

        // initialize components
        initComponents();

        // create a new table with tableSelected values
        createTable();

        createEmptyForm();

        idText.setText(Integer.toString(id));

        addIssueMode(false);

        this.setTitle("view issue in " + table.getName());

        this.setPreferredSize(new Dimension(445, 750));

        // set this window to appear in the middle of Project Manager
        this.setLocationRelativeTo(projectManager);

        this.pack();
    }

    private void defaultSetting() {

        JTable currentTable = projectManager.getSelectedTable();

        int idNum = (int) currentTable.getValueAt(currentTable.getRowCount() - 1, 0) + 1;

        //set id to its default value
        idText.setText(Integer.toString(idNum));

        //set dateOpen to today's date
        makeContentDate(dateOpenedText);

    }

    /**
     * createEmptyTable creates an empty tableSelected with default 10 rows
     */
    private void createTable(String appDefaultValue) {
        columnNames = projectManager.getTabs().get(table.getName()).getTableColNames();
        // get tableSelected column width format
        columnWidths = tabs.get(table.getName()).getColWidthPercent();
//
//        System.out.println(columnWidths.length + "add issue");
//        System.out.println(columnNames.length + "add issue");
//        if (table.getName().equals("tasks")) {
//
//            // we don't want the ID column 
//            columnNames = Arrays.copyOfRange(columnNames, 1, columnNames.length - 2);
//
//            // set the tableSelected model - add 10 empty rows
//            model = new DefaultTableModel(columnNames, 1);
//
//            // add the tableSelected model to the tableSelected
//            table.setModel(model);
//
////            int changeColumnNum = table.getColumn("app").getModelIndex();
////            for (int i = 0; i < table.getRowCount(); i++) {
////                table.setValueAt(appDefaultValue, i, changeColumnNum);
////            }
//            columnWidths = Arrays.copyOfRange(columnWidths, 1, columnWidths.length - 2);
//
//            projectManager.setColumnFormat(columnWidths, table);
//
//        } else {
        // we don't want the ID column 
        columnNames = Arrays.copyOfRange(columnNames, 1, columnNames.length);

        // set the tableSelected model - add 10 empty rows
        model = new DefaultTableModel(columnNames, 1);

        // add the tableSelected model to the tableSelected
        table.setModel(model);

        columnWidths = Arrays.copyOfRange(columnWidths, 1, columnWidths.length);

//        System.out.println(columnWidths.length + "widths");
//
//        System.out.println(table.getColumnCount());
        projectManager.setColumnFormat(columnWidths, table);

        if (formValues == null) {
            formValues = new Object[columnNames.length];
//            System.out.println("here");
        }
        formValues[0] = appDefaultValue;

        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                if (formValues[col] != null) {
                    table.getModel().setValueAt(formValues[col], row, col);
//                    System.out.println("print out column Names here: " + columnNames[col] + " " + formValues[col]);
                }
            }
        }
    }

    /**
     * jSubmitActionPerformed This is performed when the submit button is
     * executed. Refactored by Carlos Igreja 7-28-2015
     *
     * @param evt
     */
    private void createTable() {

        String appDefaultType = projectManager.getAppColumnCurrentType();
        if (appDefaultType.equals(" NULL ")) {
            appDefaultType = "";
        }

        createTable(appDefaultType);

    }

//    private void createTableWithValue() {
//
//        columnNames = projectManager.getTabs().get(table.getName()).getTableColNames();
//        // get tableSelected column width format
//        columnWidths = tabs.get(table.getName()).getColWidthPercent();
//        // set the tableSelected model - add 10 empty rows
//        model = new DefaultTableModel(columnNames, 1);
//
//        // add the tableSelected model to the tableSelected
//        table.setModel(model);
//
//        projectManager.setColumnFormat(columnWidths, table);
//
//        
//    }
    // find the sepecfic textArea in Form
    private JTextArea findColumnInForm(String columnName) {
        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i].contains(columnName)) {

                return textAreasInForm.get(columnNames[i]);
            }
        }
        return null;
    }

    /**
     * validateCell
     *
     * @param row
     * @param col
     * @return returns true if valid or false if error
     */
    public boolean validateCell(int row, int col) {
//        System.out.println("enter validatecell: " + row + " " + col);

        String colName = table.getColumnName(col);           // column name
        Object cellValue = table.getValueAt(row, col);       // store cell value
        String errorMsg = "Error with " + colName
                + " in row " + (row + 1) + ".\n";            // error message
        boolean error = false;                               // error occurred

        switch (colName) {
//            case
            case "app":
                if (cellValue == null || cellValue.toString().equals("")) {
                    errorMsg += "Symbol cannot be null";
                    error = true;
                }
                break;
            case "title":
                break;
            case "step":
                break;
            case "description":
                break;
            case "instruction":
                break;
            case "programmer":
                break;
            case "rank":
                if (cellValue != null && !cellValue.toString().equals("")) {
                    if (!cellValue.toString().matches("[1-5]{1}")) {
                        errorMsg += "Priority must be an Integer (1-5)";
                        error = true;
                    }
                }
                break;
            case "dateOpened":
                if (cellValue != null && !cellValue.toString().equals("")) {
                    if (!Validator.isValidDate("yyyy-MM-dd", cellValue.toString())) {
                        errorMsg += "Date format not correct: YYYY-MM-DD";
                        error = true;
                    }
                }
                break;
            case "dateClosed":
                if (cellValue != null && !cellValue.toString().equals("")) {
                    if (!Validator.isValidDate("yyyy-MM-dd", cellValue.toString())) {
                        errorMsg += "Date format not correct: YYYY-MM-DD";
                        error = true;
                    }
                }
                break;
            case "date_":
                if (cellValue != null && !cellValue.toString().equals("")) {
                    if (!Validator.isValidDate("yyyy-MM-dd", cellValue.toString())) {
                        errorMsg += "Date format not correct: YYYY-MM-DD";
                        error = true;
                    }
                }
                break;
            case "taskID":
                break;
            case "notes":
                break;
            case "path":
                break;
            case "submitter":
                break;
            default:
                break;

        }// end switch

        if (error) {
            JOptionPane.showMessageDialog(table, errorMsg);
            //btnSubmit.setEnabled(true); 
        }

        return !error;  // if there was an error, return false for failed
    }

    /**
     * validateData Validates all the data in the tableSelected to make sure it
     * is correct. This is used to validate the data before it is executed to
     * the server and the database so that there will not be any errors.
     *
     * @return returns true if the data is all valid and false if the is a
     * single error
     */
    public boolean validateData() {

        int col = 0;                    // column index
        boolean isCellValid = true;    // if cell is valid entry 

//        System.out.println("enter validateData");
        // if tableSelected is not empty
        if (!CellsNotEmpty.isEmpty()) {

//            System.out.println("enter validateData" + " 1 ");
            // check data
            for (int cell : CellsNotEmpty) {

                // if there was an error stop
                if (!isCellValid) {
                    break;
                }

//                for (col = 0; col < table.getColumnCount(); col++) {
                // if there was an error stop
                if (!isCellValid) {
                    break;
                }
                int row = table.getRowCount() - 1;

//                System.out.println("enter validateData " + row);
                // begin error message
                isCellValid = validateCell(row, cell);

//                }// end col for loop
            }// end row for loop

            return isCellValid;
        }

        return false; // tableSelected is empty
    }

    /**
     * checkForEmptyRows This should be used when data is removed or deleted
     */
    private void checkForEmptyRows() {

//        ArrayList<Integer> arrayCopy = new ArrayList(CellsNotEmpty);
        CellsNotEmpty.clear();

        // check List for empty rows
//        for (int row : arrayCopy) {
        int row = table.getRowCount() - 1;
        boolean isNotEmpty = false;
        for (int col = 0; col < table.getColumnCount(); col++) {
//            System.out.println("check For Empty rows (table column count) : " + table.getColumnCount());
            Object value = table.getValueAt(row, col);
//            System.out.println("checkForEmptyRows " + value);
            if (value != null && !value.equals("")) {
                isNotEmpty = true;
//                break;
            } else {
                isNotEmpty = false;
            }
//            }
            if (isNotEmpty) {
                CellsNotEmpty.add(col);
//                System.out.print("cells not empty at " + col);
            }
        }
    }

//     Variables declaration - do not modify 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        formPane = new javax.swing.JPanel();
        app = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        appText = new javax.swing.JTextArea();
        title = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        titleText = new javax.swing.JTextArea();
        id = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        idText = new javax.swing.JTextArea();
        programmer = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        programmerText = new javax.swing.JTextArea();
        rk = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        rkText = new javax.swing.JTextArea();
        dateOpened = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        dateOpenedText = new javax.swing.JTextArea();
        description = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        descriptionText = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        buttonCancel = new javax.swing.JButton();
        buttonSubmit = new javax.swing.JButton();
        dateClosedScroll = new javax.swing.JScrollPane();
        dateClosedText = new javax.swing.JTextArea();
        dateClosed = new javax.swing.JLabel();
        versionScroll = new javax.swing.JScrollPane();
        versionText = new javax.swing.JTextArea();
        version = new javax.swing.JLabel();
        buttonEdit = new javax.swing.JToggleButton();
        buttonConfirm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        app.setText(" app");

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        appText.setColumns(20);
        appText.setRows(5);
        appText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        appText.setName("app"); // NOI18N
        jScrollPane1.setViewportView(appText);

        title.setText(" title");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setToolTipText("");
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        titleText.setColumns(20);
        titleText.setRows(5);
        titleText.setName("title"); // NOI18N
        jScrollPane2.setViewportView(titleText);

        id.setText(" id");

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        idText.setEditable(false);
        idText.setColumns(20);
        idText.setRows(5);
        idText.setName("taskID"); // NOI18N
        idText.setNextFocusableComponent(dateOpenedText);
        jScrollPane3.setViewportView(idText);

        programmer.setText(" programmer");

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        programmerText.setColumns(20);
        programmerText.setRows(5);
        programmerText.setName("programmer"); // NOI18N
        jScrollPane4.setViewportView(programmerText);

        rk.setText(" rk");

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        rkText.setColumns(20);
        rkText.setRows(5);
        rkText.setName("rk"); // NOI18N
        jScrollPane5.setViewportView(rkText);

        dateOpened.setText(" dateOpened");

        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane6.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        dateOpenedText.setColumns(20);
        dateOpenedText.setRows(5);
        dateOpenedText.setToolTipText("");
        dateOpenedText.setName("dateOpened"); // NOI18N
        dateOpenedText.setNextFocusableComponent(programmerText);
        dateOpenedText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateOpenedTextKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(dateOpenedText);

        description.setText(" description");

        descriptionText.setColumns(20);
        descriptionText.setLineWrap(true);
        descriptionText.setRows(5);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setName("description"); // NOI18N
        jScrollPane7.setViewportView(descriptionText);

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });

        buttonSubmit.setText("Submit");
        buttonSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSubmitActionPerformed(evt);
            }
        });

        dateClosedScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dateClosedScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        dateClosedText.setColumns(20);
        dateClosedText.setRows(5);
        dateClosedText.setName("dateClosed"); // NOI18N
        dateClosedText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateClosedTextKeyReleased(evt);
            }
        });
        dateClosedScroll.setViewportView(dateClosedText);

        dateClosed.setText(" dateClosed");

        versionScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        versionScroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        versionText.setColumns(20);
        versionText.setRows(5);
        versionText.setName("version"); // NOI18N
        versionScroll.setViewportView(versionText);

        version.setText(" version");

        buttonEdit.setText("Edit");
        buttonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEditActionPerformed(evt);
            }
        });

        buttonConfirm.setText("Confirm");
        buttonConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateClosed)
                    .addComponent(dateClosedScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(versionScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(version, javax.swing.GroupLayout.Alignment.TRAILING)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(buttonEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(buttonConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(buttonSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(buttonCancel))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateClosed)
                            .addComponent(version))
                        .addGap(0, 0, 0)
                        .addComponent(dateClosedScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(versionScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSubmit)
                    .addComponent(buttonCancel)
                    .addComponent(buttonEdit)
                    .addComponent(buttonConfirm))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout formPaneLayout = new javax.swing.GroupLayout(formPane);
        formPane.setLayout(formPaneLayout);
        formPaneLayout.setHorizontalGroup(
            formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPaneLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPaneLayout.createSequentialGroup()
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(id))
                        .addGap(18, 18, 18)
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateOpened)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPaneLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(formPaneLayout.createSequentialGroup()
                                .addComponent(programmer)
                                .addGap(69, 69, 69)))
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rk, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(formPaneLayout.createSequentialGroup()
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(description)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(formPaneLayout.createSequentialGroup()
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(app)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        formPaneLayout.setVerticalGroup(
            formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPaneLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(programmer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rk))
                    .addComponent(dateOpened, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(id))
                .addGap(0, 0, 0)
                .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formPaneLayout.createSequentialGroup()
                        .addComponent(app)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        scrollPane.setViewportView(formPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConfirmActionPerformed
        for (int col = 0; col < formValues.length; col++) {
            if (formValues[col] != null) {

                projectManager.getSelectedTable().setValueAt(formValues[col], rowInView, col + 1);
            }
        }
        this.dispose();
    }//GEN-LAST:event_buttonConfirmActionPerformed

    private void buttonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEditActionPerformed
        AbstractButton abstractButton = (AbstractButton) evt.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        makeCellEditable(selected);
        projectManager.makeTableEditable(selected);
    }//GEN-LAST:event_buttonEditActionPerformed

    private void dateClosedTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateClosedTextKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_D && evt.isControlDown()) {
            JTextArea dateArea = (JTextArea) evt.getSource();
            makeContentDate(dateArea);

        }
    }//GEN-LAST:event_dateClosedTextKeyReleased

    private void buttonSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSubmitActionPerformed

        submit();
    }//GEN-LAST:event_buttonSubmitActionPerformed

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void dateOpenedTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateOpenedTextKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_D && evt.isControlDown()) {
            JTextArea dateArea = (JTextArea) evt.getSource();
            makeContentDate(dateArea);

        }
    }//GEN-LAST:event_dateOpenedTextKeyReleased

    /**
     * submit This is used when the submit button is pressed or if the enter key
     * is pressed when the tableSelected is finished editing to submit the data
     * to the database.
     */
    private void submit() {
//        storeValueInTable();
        for (int i = 0; i < table.getColumnCount(); i++) {
//            System.out.println("table cell value at " + i + " " + table.getValueAt(0, i));
        }
        checkForEmptyRows();

        Object cellValue = null;                 // store cell value
        int col = 0;                             // column index
        int row = 0;                             // row index

        // check if data is valid
        if (validateData()) {

            // once data checked, execute sql statement
            // first get the insert statement for the tableSelected
            String insertInto = "INSERT INTO " + table.getName() + " (";

            // this tableSelected should already not include the primary key
            for (col = 0; col < table.getColumnCount(); col++) {
                if (col != table.getColumnCount() - 1) {
                    insertInto += table.getColumnName(col) + ", ";
                } else {
                    insertInto += table.getColumnName(col) + ") ";
                }
            }

//            System.out.println(insertInto);
            numRowsAdded = 0; // reset numRowsAdded counter

            // Now get the values to add to the database
            String values = "";
//            for (row = 0; row < table.getRowCount(); row++) {
            values = "VALUES (";  // start the values statement
//            System.out.println(table.getColumnCount() + "table column number");
            for (col = 0; col < table.getColumnCount(); col++) {

                // get cell value
                cellValue = table.getValueAt(row, col);

                // format the cell value for sql
                if (cellValue != null) {

                    // if cell is empty it must be null
                    if (cellValue.toString().equals("")) {
                        cellValue = null;
                    } // if the cell is not empty it must have single quotes
                    else {
                        cellValue = processCellValue(cellValue);
                        cellValue = "'" + cellValue + "'";
                    }
                }
//                System.out.println("add record submit" + cellValue + "at " + row + " " + col);

//                    // skip empty rows
//                    // this must be after the format cell value so the "" => null
//                    if (col == 0 && cellValue == null) {
//                        break;
//                    }
                // add each value for each column to the values statement
                if (col != table.getColumnCount() - 1) {
                    values += cellValue + ", ";
                } else {
                    values += cellValue + ");";
                }
            }
//            System.out.println(values);

            try {
                // execute the sql statement
                if (!values.equals("VALUES (")) {      //skip if nothing was added
                    // open connection because might time out
                    DBConnection.open();
                    statement = DBConnection.getStatement();
                    statement.executeUpdate(insertInto + values);
                    numRowsAdded++;   // increment the number of rows added
                }
            } catch (SQLException sqlException) {
                try {
                    JOptionPane.showMessageDialog(null, "Upload failed!");

                    if (statement.getWarnings().getMessage() != null) {

                        String levelMessage = "2:" + statement.getWarnings().getMessage();
                        logWindow.addMessageWithDate(levelMessage);
//                            logWindow.
//                        System.out.println(statement.getWarnings().getMessage());

//                        System.out.println(levelMessage);//delete
                        statement.clearWarnings();
                    }
                    logWindow.addMessageWithDate("2:add record submit failed!");
                } // end try-catch
                catch (SQLException ex) {
                    // this should never be called
                    ex.printStackTrace();
                }
            }
//            }
            
                this.dispose();

//            System.out.println("numRowsAdded" + numRowsAdded);
            if (numRowsAdded > 0) {
                projectManager.getInformationLabel().setVisible(true);
                projectManager.getInformationLabel().setText("submitting to " + 
                        projectManager.getSelectedTabName());
                // update tableSelected and records label
                String tabName = projectManager.getSelectedTabName();              // tab name
                Tab tab = tabs.get(tabName);                                  // selected tab

                JTable table = tab.getTable();
                projectManager.loadTable(table);                              // load tableSelected data from database

                // reload new table data for modifiedTableData
                ModifiedTableData data = tab.getTableData();
                data.reloadData();

                TableFilter filter = tab.getFilter();                         // tableSelected filter
                filter.applyFilter();                                         // apply filter
                filter.applyColorHeaders();                                   // apply color headers

                ColumnPopupMenu ColumnPopupMenu = tab.getColumnPopupMenu();   // column popup menu 
                ColumnPopupMenu.loadAllCheckBoxItems();                       // refresh the data for the column pop up

                tab.addToTotalRowCount(numRowsAdded);                         // add the number of records added to the total records count
                JLabel recordsLabel = projectManager.getRecordsLabel();
                String recordsLabelText = tab.getRecordsLabel();              // store the records label string
                recordsLabel.setText(recordsLabelText);                       // update the records label text

                projectManager.setLastUpdateTime();                                // set the last update time from database

//                JOptionPane.showMessageDialog(this,
//                        numRowsAdded + " Add successfully!");                 // show dialog box that upload was successful
                formValues = null;
                
                projectManager.getInformationLabel().setText("Add successfully to " +
                        projectManager.getSelectedTabName());
//                createTable();                                           // create a new empty tableSelected with default 10 rows
//                resetForm();
            }
        }
    }

    private void resetForm() {
        for (int col = 0; col < columnNames.length; col++) {
            if (formValues[col] != null) {
                textAreasInForm.get(columnNames[col]).setText(formValues[col].toString());
            } else {
                textAreasInForm.get(columnNames[col]).setText(null);
            }
        }
        defaultSetting();
        CellsNotEmpty.clear();
    }

    private void storeValueInTable() {

        for (int i = 0; i < columnNames[i].length(); i++) {
            String labelName = columnNames[i];
            if (labelName.equals(table.getColumnName(i))) {
                table.setValueAt(textAreasInForm.get(labelName).getText(), 0, i);
            }
        }
    }

    private void makeContentDate(JTextArea dateArea) {

        dateArea.requestFocusInWindow();
        dateArea.selectAll();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        dateArea.setText(today);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel app;
    private javax.swing.JTextArea appText;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonConfirm;
    private javax.swing.JToggleButton buttonEdit;
    private javax.swing.JButton buttonSubmit;
    private javax.swing.JLabel dateClosed;
    private javax.swing.JScrollPane dateClosedScroll;
    private javax.swing.JTextArea dateClosedText;
    private javax.swing.JLabel dateOpened;
    private javax.swing.JTextArea dateOpenedText;
    private javax.swing.JLabel description;
    private javax.swing.JTextArea descriptionText;
    private javax.swing.JPanel formPane;
    private javax.swing.JLabel id;
    private javax.swing.JTextArea idText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel programmer;
    private javax.swing.JTextArea programmerText;
    private javax.swing.JLabel rk;
    private javax.swing.JTextArea rkText;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel title;
    private javax.swing.JTextArea titleText;
    private javax.swing.JLabel version;
    private javax.swing.JScrollPane versionScroll;
    private javax.swing.JTextArea versionText;
    // End of variables declaration//GEN-END:variables

    private void createEmptyForm() {
//        labelsInForm = new HashMap<String, JLabel>();
        textAreasInForm = new HashMap<String, JTextArea>();

        JTextArea textAreaArray[] = new JTextArea[9];
        textAreaArray[0] = appText;
        textAreaArray[1] = (dateOpenedText);
        textAreaArray[2] = (descriptionText);
        textAreaArray[3] = (programmerText);
        textAreaArray[4] = (idText);
        textAreaArray[5] = (titleText);
        textAreaArray[6] = (rkText);
        textAreaArray[7] = (dateClosedText);
        textAreaArray[8] = (versionText);
        String areaName = "";
//        System.out.println(columnNames.length + " hei");
        for (int i = 0; i < columnNames.length; i++) {
            for (int j = 0; j < textAreaArray.length; j++) {
                areaName = textAreaArray[j].getName();
                Object tableValue = formValues[i];
                if (areaName.equals(columnNames[i])) {
                    textAreasInForm.put(columnNames[i], textAreaArray[j]);
//                    System.out.println(columnNames[i] + " add to text area array!");

                    if (tableValue != null) {
                        textAreasInForm.get(columnNames[i]).setText(tableValue.toString());
                    } else {
                        textAreasInForm.get(columnNames[i]).setText(null);
                    }
                    formValues[i] = null;
                    break;
                }
            }
        }

        idText.setEditable(false);

        setDocumentListener();

        setTabKeyTransferFocusBtwTextArea();
    }

    private void setDocumentListener() {
        DocumentListener textDocumentLis = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {

                String columnName = (String) e.getDocument().getProperty("id");
                String value = textAreasInForm.get(columnName).getText();
                updateValueToTableAt(value, columnName);
//                try {
////                    System.out.println("insert: " + e.getDocument().getText(0,
////                            e.getDocument().getLength()) + "in " + e.getDocument().getProperty("id"));
////                    for (int i = 0; i < columnNames.length; i++) {
////                        if (textAreasInForm.get(columnNames[i]).getText().equals("")) {
////                            notEmpty = false;
////
////                        }
////                    }
//                } catch (BadLocationException ex) {
//                    Logger.getLogger(AddIssueWindow.class
//                            .getName()).log(Level.SEVERE, null, ex);
//                }

//                if (!lastEditColumn.equals(columnName)) {
//                    if (lastEditColumn.equals("")) {
//                        lastEditColumn = columnName;
//                    } else {
//                        String value = textAreasInForm.get(lastEditColumn).getText();
//                        System.out.println(lastEditColumn + " " + value);
//                        addValueToTableAt(value, lastEditColumn);
//                        lastEditColumn = columnName;
//                    }
//                }
//                notEmpty = true;
//                if (!lastEditColumn.equals(columnName)) {
//                    if (lastEditColumn.equals("")) {
//                        lastEditColumn = columnName;
//                    } else {
//                        String value = textAreasInForm.get(lastEditColumn).getText();
//                        System.out.println(lastEditColumn + " " + value);
//                        addValueToTableAt(value, lastEditColumn);
//                        lastEditColumn = columnName;
//                    }
//                }
//                notEmpty = true;
//                if (!lastEditColumn.equals(columnName)) {
//                    if (lastEditColumn.equals("")) {
//                        lastEditColumn = columnName;
//                    } else {
//                        String value = textAreasInForm.get(lastEditColumn).getText();
//                        System.out.println(lastEditColumn + " " + value);
//                        addValueToTableAt(value, lastEditColumn);
//                        lastEditColumn = columnName;
//                    }
//                }
//                notEmpty = true;
//                if (!lastEditColumn.equals(columnName)) {
//                    if (lastEditColumn.equals("")) {
//                        lastEditColumn = columnName;
//                    } else {
//                        String value = textAreasInForm.get(lastEditColumn).getText();
//                        System.out.println(lastEditColumn + " " + value);
//                        addValueToTableAt(value, lastEditColumn);
//                        lastEditColumn = columnName;
//                    }
//                }
//                notEmpty = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String columnName = (String) e.getDocument().getProperty("id");
                String value = textAreasInForm.get(columnName).getText();
                updateValueToTableAt(value, columnName);
//                try {
//                    System.out.println("remove: " + e.getDocument().getText(0,
//                            e.getDocument().getLength()) + "in " + e.getDocument().getProperty("id"));
////                    for (int i = 0; i < columnNames.length; i++) {
////                        if (textAreasInForm.get(columnNames[i]).getText().equals("")) {
////                            notEmpty = false;
////
////                        }
////                    }
//                } catch (BadLocationException ex) {
//                    Logger.getLogger(AddIssueWindow.class
//                            .getName()).log(Level.SEVERE, null, ex);
//                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
//                try {
//                    System.out.println("changed: " + e.getDocument().getText(0,
//                            e.getDocument().getLength()) + "in " + e.getDocument().getProperty("id"));
//
//                } catch (BadLocationException ex) {
//                    Logger.getLogger(AddIssueWindow.class
//                            .getName()).log(Level.SEVERE, null, ex);
//                }
            }

        };
        for (int i = 0; i < columnNames.length; i++) {
            Document doc = textAreasInForm.get(columnNames[i]).getDocument();
            doc.addDocumentListener(textDocumentLis);
            doc.putProperty("id", columnNames[i]);
        }
    }

    private void setTabKeyTransferFocusBtwTextArea() {
        AbstractAction transferFocus = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ((Component) e.getSource()).transferFocus();
            }
        };

        for (int i = 0; i < columnNames.length; i++) {
            if (!columnNames[i].equals("description")) {
                textAreasInForm.get(columnNames[i]).getInputMap().put(KeyStroke.
                        getKeyStroke("TAB"), "transferFocus");
                textAreasInForm.get(columnNames[i]).getActionMap().put("transferFocus", transferFocus);
            }
        }
    }

    private void updateValueToTableAt(String value, String columnName) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            if (table.getColumnName(column).equals(columnName)) {
                table.setValueAt(value, 0, column);
//                System.out.println("table get Value: " + table.getValueAt(0, column) + "at " + column);
//                checkForEmptyRows();

//                System.out.println("formValue old value at " + column + " is " + formValues[column]);
                formValues[column] = value;
//                System.out.println("formValue update at " + column + " " + formValues[column]);
            }
        }
    }

    public void setEditable(boolean b) {
        makeCellEditable(b);
    }

    private void makeCellEditable(boolean b) {
        for (int col = 0; col < columnNames.length; col++) {
            if (!columnNames[col].equals("taskID")) {
                textAreasInForm.get(columnNames[col]).setEditable(b);
            }
        }
        buttonConfirm.setEnabled(b);
        buttonCancel.setEnabled(b);
        buttonConfirm.setVisible(b);
        buttonCancel.setVisible(b);
    }

    private void addIssueMode(boolean b) {
        dateClosedText.setEnabled(!b);
        dateClosedText.setVisible(!b);
        dateClosed.setVisible(!b);

        versionText.setEnabled(!b);
        versionText.setVisible(!b);
        version.setVisible(!b);

        dateClosedScroll.setVisible(!b);
        versionScroll.setVisible(!b);

        buttonEdit.setEnabled(!b);
        buttonEdit.setVisible(!b);

        buttonConfirm.setEnabled(!b);
        buttonConfirm.setVisible(!b);

        buttonSubmit.setEnabled(b);
        buttonSubmit.setVisible(b);

    }

    private Object processCellValue(Object cellValue) {

        return cellValue.toString().replaceAll("'", "''");
    }

//    private void copyPasteAndCut() {
//        InputMap imap = this.getIn
//    }
//    private void addKeyListener() {
//        KeyListener textKeyListener = new KeyListener() {
//
//            @Override
//            public void keyTyped(KeyEvent event) {
//                System.out.println("key typed");
//            }
//
//            @Override
//            public void keyReleased(KeyEvent event) {
//                System.out.println("key released");
//            }
//
//            @Override
//            public void keyPressed(KeyEvent event) {
//                System.out.println("key pressed");
//            }
//        };
//        for (int i = 0; i < columnNames.length; i++) {
//            System.out.println(columnNames[i]);
//            textAreasInForm.get(columnNames[i]).setA
//            Document doc = textAreasInForm.get(columnNames[i]).getDocument();
//            doc.addKeyListener(textKeyListener);
//            doc.putProperty("id", columnNames[i]);
//            System.out.println("add!");
//        }
//
//    }
//    private void addMouseListener() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    //
//        // add listeners
//        addTableListeners(this);
//
//        // submit button does not start enabled because the tableSelected is empty
//        btnSubmit.setEnabled(false);
//
//        // set the label header
//        this.setTitle("Add Records to " + table.getName());
//
//        Dimension scrollPanelDimension = scrollpane.getPreferredSize();
//
//        // set the size for AddRecord window
//        this.setPreferredSize(new Dimension((int) scrollPanelDimension.getWidth(),
//                (int) (scrollPanelDimension.getHeight() + 80)));
//        this.setMinimumSize(new Dimension((int) scrollPanelDimension.getWidth(), 120));
//
////        if (!tableCellPopupWindow.isPopupWindowShow(isEditing)) {
////            tableCellPopupWindow.setTableListener(table, this);
////        }
//        // set this window to appear in the middle of Project Manager
//        this.setLocationRelativeTo(projectManager);
//
//        this.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
//                projectManager.setAddRecordsWindowShow(false);
//
//                projectManager.setDisableProjecetManagerFunction(true);
//
//            }
//        });
////        this.pack();
//        System.out.println("add record window create!");
//    }
//
//    private void btnSubmitActionPerformed(java.awt.event.ActionEvent evt) {
//
//        projectManager.setDisableProjecetManagerFunction(true);
//        submit();
//    }
//
//    /**
//     * submit This is used when the submit button is pressed or if the enter key
//     * is pressed when the tableSelected is finished editing to submit the data
//     * to the database.
//     */
//    private void submit() {
//
//        Object cellValue = null;                 // store cell value
//        int col = 0;                             // column index
//        int row = 0;                             // row index
//
//        // check if data is valid
//        if (validateData()) {
//
//            // once data checked, execute sql statement
//            // first get the insert statement for the tableSelected
//            String insertInto = "INSERT INTO " + table.getName() + " (";
//
//            // this tableSelected should already not include the primary key
//            for (col = 0; col < table.getColumnCount(); col++) {
//                if (col != table.getColumnCount() - 1) {
//                    insertInto += table.getColumnName(col) + ", ";
//                } else {
//                    insertInto += table.getColumnName(col) + ") ";
//                }
//            }
//
//            numRowsAdded = 0; // reset numRowsAdded counter
//
//            // Now get the values to add to the database
//            String values = "";
//            for (row = 0; row < table.getRowCount(); row++) {
//                values = "VALUES (";  // start the values statement
//                System.out.println(table.getColumnCount() + "table column number");
//                for (col = 0; col < table.getColumnCount(); col++) {
//
//                    // get cell value
//                    cellValue = table.getValueAt(row, col);
//
//                    // format the cell value for sql
//                    if (cellValue != null) {
//
//                        // if cell is empty it must be null
//                        if (cellValue.toString().equals("")) {
//                            cellValue = null;
//                        } // if the cell is not empty it must have single quotes
//                        else {
//                            cellValue = "'" + cellValue + "'";
//                        }
//                    }
//                    System.out.println("add record submit" + cellValue + "at " + row + " " + col);
//
//                    // skip empty rows
//                    // this must be after the format cell value so the "" => null
//                    if (col == 0 && cellValue == null) {
//                        break;
//                    }
//
//                    // add each value for each column to the values statement
//                    if (col != table.getColumnCount() - 1) {
//                        values += cellValue + ", ";
//                    } else {
//                        values += cellValue + ");";
//                    }
//                }
//                System.out.println(values);
//
//                try {
//                    // execute the sql statement
//                    if (!values.equals("VALUES (")) {      //skip if nothing was added
//                        // open connection because might time out
//                        DBConnection.open();
//                        statement = DBConnection.getStatement();
//                        statement.executeUpdate(insertInto + values);
//                        numRowsAdded++;   // increment the number of rows added
//                    }
//                } catch (SQLException sqlException) {
//                    try {
//                        JOptionPane.showMessageDialog(null, "Upload failed!");
//
//                        if (statement.getWarnings().getMessage() != null) {
//
//                            String levelMessage = "2:" + statement.getWarnings().getMessage();
//                            logWindow.addMessageWithDate(levelMessage);
////                            logWindow.
//                            System.out.println(statement.getWarnings().getMessage());
//
//                            System.out.println(levelMessage);//delete
//
//                            statement.clearWarnings();
//                        }
//                        logWindow.addMessageWithDate("2:add record submit failed!");
//                    } // end try-catch
//                    catch (SQLException ex) {
//                        // this should never be called
//                        ex.printStackTrace();
//                    }
//                }
//            }
//
//            System.out.println("numRowsAdded" + numRowsAdded);
//
//            if (numRowsAdded > 0) {
//                // update tableSelected and records label
//                String tabName = projectManager.getSelectedTabName();              // tab name
//                Tab tab = tabs.get(tabName);                                  // selected tab
//
//                JTable table = tab.getTable();
//                projectManager.loadTable(table);                              // load tableSelected data from database
//
//                // reload new table data for modifiedTableData
//                ModifiedTableData data = tab.getTableData();
//                data.reloadData();
//
//                TableFilter filter = tab.getFilter();                         // tableSelected filter
//                filter.applyFilter();                                         // apply filter
//                filter.applyColorHeaders();                                   // apply color headers
//
//                ColumnPopupMenu ColumnPopupMenu = tab.getColumnPopupMenu();   // column popup menu 
//                ColumnPopupMenu.loadAllCheckBoxItems();                       // refresh the data for the column pop up
//
//                tab.addToTotalRowCount(numRowsAdded);                         // add the number of records added to the total records count
//                JLabel recordsLabel = projectManager.getRecordsLabel();
//                String recordsLabelText = tab.getRecordsLabel();              // store the records label string
//                recordsLabel.setText(recordsLabelText);                       // update the records label text
//
//                projectManager.setLastUpdateTime();                                // set the last update time from database
//
//                JOptionPane.showMessageDialog(this,
//                        numRowsAdded + " Add successfully!");                 // show dialog box that upload was successful
//                createEmptyTable();                                           // create a new empty tableSelected with default 10 rows
//            }
//        }
//    }
//
//    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
//
//        projectManager.setAddRecordsWindowShow(false);
//        projectManager.setDisableProjecetManagerFunction(true);
//        tableCellPopupWindow.windowClose();
//        this.dispose();
//    }
//
//    // add an empty row to the tableSelected
////        model.addRow(new Object[]{});
    /**
     * setKeyboardFocusManager Sets the Keyboard Focus Manager
     */
//    private void setKeyboardFocusManager(JFrame frame) {
//
//        /*
//         No Tab key-pressed or key-released events are received by the key event listener. This is because the focus subsystem 
//         consumes focus traversal keys, such as Tab and Shift Tab. To solve this, apply the following to the component that is 
//         firing the key events 
//         */
//        table.setFocusTraversalKeysEnabled(false);
//        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {// Allow to TAB-
////
//            @Override
//            public boolean dispatchKeyEvent(KeyEvent e) {
//
//                if (e.getComponent() instanceof JTextArea) {
//                    if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
//                        JTextArea dateArea = new JTextArea();
//                        for (int i = 0; i < columnNames.length; i++) {
//                            if (columnNames[i].contains("date")) {
//                                dateArea = textAreasInForm.get(columnNames[i]);
//                                makeContentDate(dateArea);
//                            }
//                        }
//
//                    }
//                }
//                return false;
//            }
//
//            private void makeContentDate(JTextArea dateArea) {
//
//                dateArea.requestFocusInWindow();
//                dateArea.selectAll();
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                Date date = new Date();
//                String today = dateFormat.format(date);
//                dateArea.setText(today);
//            }
//
//        });
//    }
//                    if (e.getKeyCode() == KeyEvent.VK_TAB && e.isControlDown()) {
//                        System.out.println(activeTextArea.getText());
//                    }
//                }
//                return false;
//
//            }
//        });
//    }
//
//                if (e.getComponent() instanceof JTable) {
//
//                    // this is called to either clear data or submit data
//                    if (e.getKeyCode() == KeyEvent.VK_ENTER && !table.isEditing()) {
//
//                        // clear the row(s)
//                        if (e.getID() == KeyEvent.KEY_PRESSED) {
//                            if (table.getSelectionBackground() == Color.RED) {
//                                int[] rows = table.getSelectedRows();
//
//                                if (rows != null) {
//                                    for (int row : rows) {
//                                        for (int col = 0; col < table.getColumnCount(); col++) {
//                                            table.getModel().setValueAt("", row, col);
//                                        }
//                                    }
//                                }
//                                table.setSelectionBackground(defaultSelectedBG);
//
//                                // check for empty rows/table
//                                checkForEmptyRows();
//                                if (rowsNotEmpty.isEmpty()) {
//                                    btnSubmit.setEnabled(false);
//                                } else {
//                                    btnSubmit.setEnabled(true);
//                                }
//                            } // submit the data
//                            else if (table.getSelectionBackground() != Color.RED) {
//                                submit();
//                            }
//                        }
//                    } // this toggles the red bg for clearing row data
//                    else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
//
//                        if (e.getID() == KeyEvent.KEY_RELEASED) {
//                            if (table.isEditing()) {
//                                table.getCellEditor().stopCellEditing();
//                            }
//
//                            if (table.getSelectionBackground() == defaultSelectedBG) {
//                                table.setSelectionBackground(Color.RED);
//                            } else {
//                                table.setSelectionBackground(defaultSelectedBG);
//                            }
//                        }
//                    } // this is to tab and move to cells with arrow keys
//                    else if (e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_LEFT
//                            || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_UP
//                            || e.getKeyCode() == KeyEvent.VK_DOWN) {
//
//                        JTable tableSelected = (JTable) e.getComponent();
//
//                        if (e.getID() == KeyEvent.KEY_RELEASED) {
//                            System.out.println("add records tabs!");
//                            //if table get selected location is not the same as last selection
//                            if (tableSelected.getSelectedRow() != lastSelectedRow
//                                    || tableSelected.getSelectedColumn() != lastSelectedColumn) {
//
//                                if (lastSelectedRow == -1 || lastSelectedColumn == -1) {
//                                    lastSelectedRow = tableSelected.getSelectedRow();
//                                    lastSelectedColumn = tableSelected.getSelectedColumn();
//                                    tableCellPopupWindow = new PopupWindowInTableCell(frame, tableSelected);
//                                } else {
//                                    tableCellPopupWindow.windowClose();
//                                    tableCellPopupWindow = new PopupWindowInTableCell(frame, tableSelected);
//                                    lastSelectedRow = tableSelected.getSelectedRow();
//                                    lastSelectedColumn = tableSelected.getSelectedColumn();
//                                }// last popup window dispose and new popup window show at the selected cell
//                            }
////                        } else if (e.getID() == KeyEvent.KEY_PRESSED) {
////                            if (selectedCol == tableSelected.getColumnCount() - 1) {
////
////                                DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
////
////                                tableModel.addRow(new Object[]{});
////                            }
//
//                        } else {
//
//                        }
//                    }
//
//                } // end table component condition
//                // ctrl + D fills in the current date
//                else if (e.getKeyCode() == KeyEvent.VK_D && e.isControlDown()) {
//                    JTable table = (JTable) e.getComponent().getParent();
//                    int column = table.getSelectedColumn();
//                    if (table.getColumnName(column).toLowerCase().contains("date")) {
//                        if (e.getID() != 401) {
//                            return false;
//                        } else {
//                            JTextField selectCom = (JTextField) e.getComponent();
//                            selectCom.requestFocusInWindow();
//                            selectCom.selectAll();
//                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                            Date date = new Date();
//                            String today = dateFormat.format(date);
//                            selectCom.setText(today);
//                        }// default date input with today's date}
//                    }
//                }
//
//                return false;
//            }
//        });
//    }
//
//
//    /**
//     * addTableListeners This is called to add the listeners to the
//     * tableSelected The listeners added are the TableModel listener the
//     * MouseListener and the KeyListener
//     */
//    public void addTableListeners(JFrame frame) {
//
//        // add tableModelListener
//        table.getModel().addTableModelListener(new TableModelListener() {
//
//            @Override
//            public void tableChanged(TableModelEvent e) {
//
//                // isEditing is a class boolean triggered true on double click
//                if (!isEditing) {
//                    // if clearing row then do not validate
//                    if (table.getSelectionBackground() != Color.RED) {
//
//                        // check the cell for valid entry
//                        int row = e.getLastRow();            // row index
//                        int col = e.getColumn();             // column index
//
//                        System.out.println("tableChanged at: " + row + " " + col);
//                        validateCell(row, col);
//                    }
//
//                    // get value of cell
//                    int row = e.getFirstRow();
//                    int col = e.getColumn();
//                    Object value = table.getValueAt(row, col);
//
//                    // if cell value is empty
//                    if (value == null || value.equals("")) {
//                        // check to see if it was a deletion
//                        if (!rowsNotEmpty.isEmpty() && rowsNotEmpty.contains(row)) {
//                            checkForEmptyRows();
//                        }
//                    } // else add the row to the list as not empty
//                    else {
//                        rowsNotEmpty.add(row);
//                    }
//
//                    // if list is empty then the tableSelected is empty
//                    if (!rowsNotEmpty.isEmpty() && !isEditing) {
//                        btnSubmit.setEnabled(true);
//                    }
//                }
//
//                // reset isEditing boolean
//                isEditing = false;
//            }
//
//        });
//
//        // add mouseListener
//        table.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//                if (e.getClickCount() == 1) {
//                    // if we click away the red delete should go away
//                    if (table.getSelectionBackground() == Color.RED && !e.isControlDown()) {
//                        table.setSelectionBackground(defaultSelectedBG);
//                    }
//                } // this enters edit mode
//                else if (e.getClickCount() == 2) {
//                    btnSubmit.setEnabled(false);
//                    isEditing = true;
//                    selectAllText(e);
//                }
//            }
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//                // if left mouse clicks
//                if (SwingUtilities.isLeftMouseButton(e)) {
//                    if (e.getClickCount() == 1) {
//
//                        //if table get selected location is not the same as last selection
//                        if (table.getSelectedRow() != lastSelectedRow
//                                || table.getSelectedColumn() != lastSelectedColumn) {
//
//                            if (lastSelectedRow == -1 || lastSelectedColumn == -1) {
//                                lastSelectedRow = table.getSelectedRow();
//                                lastSelectedColumn = table.getSelectedColumn();
//                                tableCellPopupWindow = new PopupWindowInTableCell(frame, table);
//                            } else {
//                                tableCellPopupWindow.windowClose();
//                                tableCellPopupWindow = new PopupWindowInTableCell(frame, table);
//                                lastSelectedRow = table.getSelectedRow();
//                                lastSelectedColumn = table.getSelectedColumn();
//                            }// last popup window dispose and new popup window show at the selected cell
//                        } else {
//                            //if current selection equals last selection nothing happens
//                        }
//                    }
//                } // end if left mouse clicks
//            }
//        });
//    }
//
//    /**
//     * selectAllText Select all text inside jTextField or a cell
//     *
//     * @param e
//     */
//    private void selectAllText(MouseEvent e) {
//
//        JTable table = (JTable) e.getComponent();
//        int row = table.getSelectedRow();
//        int column = table.getSelectedColumn();
//        if (column != -1) {
//            table.getComponentAt(row, column).requestFocus();
//            table.editCellAt(row, column);
//            JTextField selectCom = (JTextField) table.getEditorComponent();
//            if (selectCom != null) {
//                selectCom.requestFocusInWindow();
//                selectCom.selectAll();
//            }
//        }
//    }
//
    //    private void createForm() {
//        createEmptyForm();
//        for (int col = 0; col < formValues.length; col++) {
//            System.out.println(col + " " + formValues[col] + " !");
//            Object tableValue = formValues[col];
//            if (tableValue != null) {
//                textAreasInForm.get(columnNames[col]).setText(tableValue.toString());
//            } else {
//                textAreasInForm.get(columnNames[col]).setText(null);
//            }
//            idText.setEditable(false);
//        }
//    }
}
