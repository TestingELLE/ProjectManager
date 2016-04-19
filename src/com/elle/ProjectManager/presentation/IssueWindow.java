
package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.dao.IssueDAO;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.ShortCutSetting;
import com.elle.ProjectManager.logic.Tab;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author fuxiaoqian
 */
public class IssueWindow extends JFrame {

    private ProjectManagerWindow projectManager;
    private Issue issue;
    private JTable table;
    private int row;
    private IssueDAO dao;
    private boolean addIssueMode;
    private ShortCutSetting ShortCutSetting;
    private String[] dropdownlist = {"app","title", "description","programmer", "dateOpened", "rk", "version", "dateClosed"};
    private Map<String, Tab> tabs;       // used to update the records label

    /**
     * Creates new form IssueWindow
     */
    public IssueWindow(int row, JTable table) {
        projectManager = ProjectManagerWindow.getInstance();
         tabs = projectManager.getTabs();
        this.table = table;
        this.row = row;
        dao = new IssueDAO();
        issue = new Issue();

        // new issue
        if (this.row == -1) {
            addIssueMode = true;
            issue.setId(dao.getMaxId() + 1);
            issue.setApp(projectManager.getSelectedTabName());
            issue.setDateOpened(todaysDate());
            issue.setSubmitter(projectManager.getUserName());
        } 
        // existing issue
        else {
            addIssueMode = false;
            setIssueValuesFromTable(row,table);
        }
       
        initComponents();

        setComponentValuesFromIssue();
        
        /**
         * Add all JTextComponents to add document listener, input mappings,
         * and shortcuts.
         * Note: ComboBox and CheckBox components can use the action event.
         * You can double click it on the designer to create one for it.
         * You can reference one that exists for help with the code if needed.
         */
        ArrayList<JTextComponent> textComponentList = new ArrayList<>();
        textComponentList.add(submitterText);
        textComponentList.add(dateOpenedText);
        
        textComponentList.add(titleText);
        textComponentList.add(descriptionText);
   
        textComponentList.add(dateClosedText);
        textComponentList.add(versionText);
        addDocumentListener(textComponentList);
        addInputMappingsAndShortcuts(textComponentList);
         updateComboList("programmer", projectManager.getSelectedTabName());
        updateComboList("rk", projectManager.getSelectedTabName());
        updateComboList("app", projectManager.getSelectedTabName());
        setComponentValuesFromIssue();
        
        
        
        
        setOpenCloseIssueBtnText();
        setIssueWindowMode();
       

        this.setTitle("Issue in " + table.getName());
        this.setPreferredSize(new Dimension(620, 750));

        // set view issue window location in screen
        Point pmWindowLocation = projectManager.getLocationOnScreen(); //get the project manager window in screen
        int numWindow = projectManager.getOpeningIssuesList().size();
        int x = pmWindowLocation.x - 150;
        int y = pmWindowLocation.y - 120;
        this.setLocation(x + numWindow * 30, y + numWindow * 15); // set location of view issue window depend on how many window open

        this.pack();
    }

    /**
     * Displays the components accordingly 
     * for either a new issue submittal form
     * or populate the form with an existing issue.
     */
    private void setIssueWindowMode() {
        dateClosedText.setEnabled(!addIssueMode);
        dateClosedText.setVisible(!addIssueMode);
        dateClosed.setVisible(!addIssueMode);

        versionText.setEnabled(!addIssueMode);
        versionText.setVisible(!addIssueMode);
        version.setVisible(!addIssueMode);

        buttonConfirm.setEnabled(false);
        buttonConfirm.setVisible(!addIssueMode);

        btnCloseIssue.setEnabled(!addIssueMode);
        btnCloseIssue.setVisible(!addIssueMode);

        BtnNext.setVisible(!addIssueMode);
        BtnPrevious.setVisible(!addIssueMode);

        buttonSubmit.setEnabled(false);
        buttonSubmit.setVisible(addIssueMode);
    }

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
        title = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        rk = new javax.swing.JLabel();
        dateOpenedText = new javax.swing.JTextField();
        programmer = new javax.swing.JLabel();
        dateOpened = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        descriptionText = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        buttonCancel = new javax.swing.JButton();
        buttonSubmit = new javax.swing.JButton();
        dateClosed = new javax.swing.JLabel();
        version = new javax.swing.JLabel();
        buttonConfirm = new javax.swing.JButton();
        dateClosedText = new javax.swing.JTextField();
        versionText = new javax.swing.JTextField();
        btnCloseIssue = new javax.swing.JButton();
        app = new javax.swing.JLabel();
        appComboBox = new javax.swing.JComboBox();
        titleText = new javax.swing.JTextField();
        description = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        idText = new javax.swing.JLabel();
        BtnNext = new javax.swing.JButton();
        BtnPrevious = new javax.swing.JButton();
        lock = new javax.swing.JLabel();
        submitterText = new javax.swing.JTextField();
        submitter = new javax.swing.JLabel();
        lockCheckBox = new javax.swing.JCheckBox();
        comboBoxIssueType = new javax.swing.JComboBox<String>();
        programmerComboBox = new javax.swing.JComboBox();
        rkComboBox = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        title.setText(" title");

        id.setText(" id");

        rk.setText(" rk");

        dateOpenedText.setText("jTextField1");
        dateOpenedText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        dateOpenedText.setMargin(new java.awt.Insets(-1, -1, -1, -1));
        dateOpenedText.setName("dateOpened"); // NOI18N
        dateOpenedText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateOpenedTextKeyReleased(evt);
            }
        });

        programmer.setText(" programmer");

        dateOpened.setText(" dateOpened");
        dateOpened.setPreferredSize(new java.awt.Dimension(79, 12));

        descriptionText.setColumns(20);
        descriptionText.setLineWrap(true);
        descriptionText.setRows(5);
        descriptionText.setWrapStyleWord(true);
        descriptionText.setName("description"); // NOI18N
        descriptionText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                descriptionTextKeyReleased(evt);
            }
        });
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

        dateClosed.setText(" dateClosed");

        version.setText(" version");

        buttonConfirm.setText("Confirm");
        buttonConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConfirmActionPerformed(evt);
            }
        });

        dateClosedText.setText("jTextField2");
        dateClosedText.setName("dateClosed"); // NOI18N
        dateClosedText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateClosedTextKeyReleased(evt);
            }
        });

        versionText.setText("jTextField1");
        versionText.setName("version"); // NOI18N

        btnCloseIssue.setText("Close Issue");
        btnCloseIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseIssueActionPerformed(evt);
            }
        });

        app.setText(" app");

        appComboBox.setEditable(true);
        appComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        appComboBox.setPreferredSize(new java.awt.Dimension(80, 28));
        appComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(app)
                    .addComponent(appComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 180, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCloseIssue, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateClosedText, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateClosed))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(versionText, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(version)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(buttonConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(buttonCancel)
                        .addGap(0, 0, 0))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(app)
                    .addComponent(dateClosed)
                    .addComponent(version))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCloseIssue, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateClosedText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(versionText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buttonSubmit)
                            .addComponent(buttonCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonConfirm)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        titleText.setText("jTextField1");
        titleText.setName("title"); // NOI18N

        description.setText(" description");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );

        idText.setText("jLabel1");

        BtnNext.setText(">");
        BtnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnNextActionPerformed(evt);
            }
        });

        BtnPrevious.setText("<");
        BtnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPreviousActionPerformed(evt);
            }
        });

        lock.setText(" lock");

        submitterText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitterTextActionPerformed(evt);
            }
        });

        submitter.setText(" submitter");

        lockCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockCheckBoxActionPerformed(evt);
            }
        });

        comboBoxIssueType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FEATURE", "BUG", "REFERENCE" }));
        comboBoxIssueType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxIssueTypeActionPerformed(evt);
            }
        });

        programmerComboBox.setEditable(true);
        programmerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        programmerComboBox.setPreferredSize(new java.awt.Dimension(80, 28));
        programmerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programmerComboBoxActionPerformed(evt);
            }
        });

        rkComboBox.setEditable(true);
        rkComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        rkComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rkComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formPaneLayout = new javax.swing.GroupLayout(formPane);
        formPane.setLayout(formPaneLayout);
        formPaneLayout.setHorizontalGroup(
            formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPaneLayout.createSequentialGroup()
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(id)
                            .addComponent(lock))
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formPaneLayout.createSequentialGroup()
                                .addComponent(idText)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboBoxIssueType, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lockCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(submitterText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(submitter, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateOpenedText, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateOpened, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(programmer)
                            .addComponent(programmerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(formPaneLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(rkComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(formPaneLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(rk, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(formPaneLayout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6))
                    .addGroup(formPaneLayout.createSequentialGroup()
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPaneLayout.createSequentialGroup()
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(titleText, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(formPaneLayout.createSequentialGroup()
                                .addComponent(description)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BtnPrevious)
                                .addGap(0, 0, 0)
                                .addComponent(BtnNext)))
                        .addContainerGap())))
        );
        formPaneLayout.setVerticalGroup(
            formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPaneLayout.createSequentialGroup()
                .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(formPaneLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dateOpenedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(programmerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rkComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(submitterText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPaneLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(submitter, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(idText)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateOpened, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(programmer)
                            .addComponent(rk)
                            .addComponent(comboBoxIssueType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lock, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lockCheckBox))
                        .addGap(25, 25, 25))
                    .addGroup(formPaneLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titleText, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(formPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BtnNext)
                    .addComponent(BtnPrevious)
                    .addComponent(description, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        scrollPane.setViewportView(formPane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void FillItWithDate(JTextField dateArea) {

        dateArea.requestFocusInWindow();
        dateArea.selectAll();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        dateArea.setText(today);
    }
    
    /**
     * Returns today's date as a String in format yyyy-MM-dd
     * @return today's date as a String in format yyyy-MM-dd
     */
    private String todaysDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    /**
     * This updates the custom id list when traversing the table
     * @param newRow 
     */
    private void updateCustomIdList(int newRow) {
        
        // remove this id from the openIssuesList and CustomIdList
        projectManager.getOpeningIssuesList().remove(issue.getId(), this);
        projectManager.getSelectedTabCustomIdList(table.getName()).delete(issue.getId());

        String newID = table.getValueAt(newRow, 0).toString();

        // if issue is not open
        if (!projectManager.getOpeningIssuesList().containsKey(newID)) {
            projectManager.getOpeningIssuesList().put(issue.getId(), this);
            projectManager.getSelectedTabCustomIdList(table.getName()).add(issue.getId());

        } 
        // use the window with this issue already open
        else {
            projectManager.getViewIssueWindowOf(newID).toFront();
            this.dispose();
        }
    }

//    private void updateIssueWindow() {
//        for (int i = 0; i < issue.getFieldsNumber(); i++) {
//            String columnName = issue.getFieldName(i);
//            String cellValue = issue.getIssueValueAt(i);
//            switch (columnName) {
//                case "ID":
//                    idText.setText(cellValue);
//                case "app":
////                    System.out.println(issue.getIssueValueAt(i));
//                    appText.setText(cellValue);// set app textfield with the content in app column in view issue
//                    break;
//                case "title":
//                    titleText.setText(cellValue);
//                    break;
//                case "description":
//                    descriptionText.setText(cellValue);
//                    break;
//                case "programmer":
//                    programmerText.setText(cellValue);
//                    break;
//                case "dateOpened":
//                    dateOpenedText.setText(cellValue);
//                    break;
//                case "rk":
//                    rkText.setText(cellValue);
//                    break;
//                case "version":
//                    versionText.setText(cellValue);
//                    break;
//                case "dateClosed":
//                    dateClosedText.setText(cellValue);
//                    break;
//                case "submitter":
//                    submitterText.setText(cellValue);
//                    break;
//                case "locked":
//                    if (cellValue.equals("Y")) {
//                        lockCheckBox.setSelected(true);
//                    } else {
//                        lockCheckBox.setSelected(false);
//                    }
//                    break;
//                default:
//                    break;
//            }
//            issue.getIssueData(columnName).setChanged(false);
//        }
//        
//        setOpenCloseIssueBtnText();
//    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        issueWindowClosing();
//        System.out.println("window closing!");
    }//GEN-LAST:event_formWindowClosing

    private void dateOpenedTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateOpenedTextKeyReleased
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_D) {
            this.FillItWithDate((JTextField) evt.getComponent());
        }
    }//GEN-LAST:event_dateOpenedTextKeyReleased

    private void descriptionTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descriptionTextKeyReleased
        JTextArea dateArea = (JTextArea) evt.getComponent();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        String value = dateArea.getText();
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_D) {
            dateArea.requestFocusInWindow();
            dateArea.selectAll();
            value = value + " " + today;
            dateArea.setText(value);
        } else if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_N) {

            int pos = dateArea.getCaretPosition();
            String userName = projectManager.getUserName();
            String message = "\n" + "-- by " + userName + " on " + today + "-- \n";
            //String value1 = value.substring(0, pos) + message + value.substring(pos, value.length());
            dateArea.insert(message, pos);

            dateArea.setCaretPosition(pos + userName.length() + 25);

        }
    }//GEN-LAST:event_descriptionTextKeyReleased

    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        //        System.out.println(selectedTable.getValueAt(0, 0));

//        projectManager.getOpeningIssuesList().remove(issue.getID(), this);
        issueWindowClosing();
    }//GEN-LAST:event_buttonCancelActionPerformed

    /**
     * This method is called when the submit button is pressed.
     * @param evt 
     */
    private void buttonSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSubmitActionPerformed
        setIssueValuesFromComponents();
        
        dao.insert(issue);
        projectManager.inserTableRow(table,issue);
        projectManager.makeTableEditable(false);
        issueWindowClosing();
    }//GEN-LAST:event_buttonSubmitActionPerformed

    /**
     * This method is called when the confirm button is pressed.
     * @param evt 
     */
    private void buttonConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConfirmActionPerformed
        setIssueValuesFromComponents();

        dao.update(issue);
       
        projectManager.updateTableRow(table,issue);
        projectManager.makeTableEditable(false);
        issueWindowClosing();
    }//GEN-LAST:event_buttonConfirmActionPerformed


    private void dateClosedTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateClosedTextKeyReleased
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_D) {
            FillItWithDate((JTextField) evt.getComponent());
        }
    }//GEN-LAST:event_dateClosedTextKeyReleased

    private void btnCloseIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseIssueActionPerformed

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        String userName = projectManager.getUserName();
        String value = descriptionText.getText();
        if (btnCloseIssue.getText().equalsIgnoreCase("close issue")) {
            // set dateClosed text field with date today
            FillItWithDate(dateClosedText);
            String temperaryVersion = "XXX";
            versionText.setText(temperaryVersion);
            btnCloseIssue.setText("Reopen Issue");
            value = value + "\n--- Issue Closed by "
                    + userName + " on " + today + "\n";
        } else if (btnCloseIssue.getText().equalsIgnoreCase("reopen issue")) {
            value = value + "\n \n--- Issue reopened by "
                    + userName + " on " + today + " (version " + versionText.getText() + ") \n";
            versionText.setText("");
            dateClosedText.setText("");
            btnCloseIssue.setText("Close Issue");
        }
        descriptionText.setText(value);
    }//GEN-LAST:event_btnCloseIssueActionPerformed

    /**
     * Fired when the next button is invoked.
     * The next button traverses the table to get the next issue.
     * @param evt 
     */
    private void BtnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnNextActionPerformed
        
        /**
         * If table has not changed then no need to execute this for loop.
         * boolean rowFound makes sure the issue is still in the table view.
         */
        boolean rowFound = true;
        if (!table.getValueAt(row, 0).toString().equals(Integer.toString(issue.getId()))) {
            rowFound = false;
            for (int i = 0; i < table.getRowCount(); i++) {
                if (table.getValueAt(i, 0).toString().equals(Integer.toString(issue.getId()))) {
                    row = i;
                    rowFound = true;
                }
            }
        }
        
        // next row
        if(!rowFound){
            JOptionPane.showMessageDialog(this, "This issue is no longer on the table!");
        }
        else if (row == table.getRowCount() - 1) {
            JOptionPane.showMessageDialog(this, "This issue is already the last row on the table!");
        } else {
            row++;
            setIssueValuesFromTable(row,table);
            setComponentValuesFromIssue();
            table.setRowSelectionInterval(row, row);
            updateCustomIdList(row);
        }
    }//GEN-LAST:event_BtnNextActionPerformed

    /**
     * Fired when the previous button is invoked.
     * The previous button traverses the table to get the previous issue.
     * @param evt 
     */
    private void BtnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPreviousActionPerformed

        /**
         * If table has not changed then no need to execute this for loop.
         * boolean found makes sure the issue is still in the table view.
         */
        boolean rowFound = true;
        if (!table.getValueAt(row, 0).toString().equals(Integer.toString(issue.getId()))) {

            rowFound = false;
            for (int i = 0; i < table.getRowCount(); i++) {
                if (table.getValueAt(i, 0).toString().equals(Integer.toString(issue.getId()))) {
                    row = i;
                    rowFound = true;
                }
            }
        }
        
        // previous row
        if(!rowFound){
            JOptionPane.showMessageDialog(this, "This issue is no longer on the table!");
        }
        else if (row == 0) {
            JOptionPane.showMessageDialog(this, "This issue is already the first row on the table!");
        } else {
            row--;
            setIssueValuesFromTable(row,table);
            setComponentValuesFromIssue();
            table.setRowSelectionInterval(row, row);
            updateCustomIdList(row);
        }
    }//GEN-LAST:event_BtnPreviousActionPerformed

    /**
     * Fires when Lock CheckBox selection is changed
     * @param evt action event for the Lock CheckBox
     */
    private void lockCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockCheckBoxActionPerformed

        // if the same then check for other changes
        if((lockCheckBox.isSelected()?"Y":"").equals(issue.getLocked())){
            checkForChangeAndSetBtnsEnabled();
        }
        // we know right away there is a change so just set the button enabled
        else{
            setBtnsEnabled(true); // sets the submit or confirm button enabled
        }
    }//GEN-LAST:event_lockCheckBoxActionPerformed

    /**
     * Fires when IssueType ComboBox selection is changed
     * @param evt action event for the IssueType ComboBox
     */
    private void comboBoxIssueTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxIssueTypeActionPerformed
        // if the same then check for other changes
        if(comboBoxIssueType.getSelectedItem().toString().equals(issue.getIssueType())){
            checkForChangeAndSetBtnsEnabled();
        }
        // we know right away there is a change so just set the button enabled
        else{
            setBtnsEnabled(true); // sets the submit or confirm button enabled
        }
    }//GEN-LAST:event_comboBoxIssueTypeActionPerformed

    private void submitterTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitterTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_submitterTextActionPerformed

    private void programmerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programmerComboBoxActionPerformed
        if(programmerComboBox.getSelectedItem().toString().equals(issue.getProgrammer())){
            checkForChangeAndSetBtnsEnabled();
        }
        // we know right away there is a change so just set the button enabled
        else{
            setBtnsEnabled(true); // sets the submit or confirm button enabled
        }
    }//GEN-LAST:event_programmerComboBoxActionPerformed

    private void rkComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rkComboBoxActionPerformed
      if(rkComboBox.getSelectedItem().toString().equals(issue.getRk())){
            checkForChangeAndSetBtnsEnabled();
        }
        // we know right away there is a change so just set the button enabled
        else{
            setBtnsEnabled(true); // sets the submit or confirm button enabled
        }
    }//GEN-LAST:event_rkComboBoxActionPerformed

    private void appComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appComboBoxActionPerformed
        if(appComboBox.getSelectedItem().toString().equals(issue.getApp())){
            checkForChangeAndSetBtnsEnabled();
        }
        // we know right away there is a change so just set the button enabled
        else{
            setBtnsEnabled(true); // sets the submit or confirm button enabled
        }
    }//GEN-LAST:event_appComboBoxActionPerformed

    /**
     * Called to close the form
     */
    private void issueWindowClosing() {
        if (addIssueMode) {
            projectManager.setAddIssueWindowShow(false);
        } else {
            projectManager.getOpeningIssuesList().remove(issue.getId(), this);
            projectManager.getSelectedTabCustomIdList(table.getName()).delete(issue.getId());
            projectManager.getSelectedTabCustomIdList(table.getName()).printOutIDList();
        }
        this.dispose();
    }
    
    /**
     * Adds input mappings and shortcuts for JTextComponent Objects
     * (ex. TextFields, TextAreas)
     */
    private void addInputMappingsAndShortcuts(ArrayList<JTextComponent> textComponentList){

        for(JTextComponent comp:textComponentList){
            ShortCutSetting.copyAndPasteShortCut(comp.getInputMap());
            ShortCutSetting.undoAndRedoShortCut(comp);
        }      
    }

    /**
     * Sets issue values from table
     * @param row the row index on table for issue to retrieve
     * @param table the table with the row/issue data
     */
    private void setIssueValuesFromTable(int row, JTable table) {

        issue.setId(Integer.parseInt(getTableValueAt(row, 0).toString()));
        issue.setApp(getTableValueAt(row, 1).toString());
        issue.setTitle(getTableValueAt(row, 2).toString());
        issue.setDescription(getTableValueAt(row, 3).toString());
        issue.setProgrammer(getTableValueAt(row, 4).toString());
        issue.setDateOpened(getTableValueAt(row, 5).toString());
        issue.setRk(getTableValueAt(row, 6).toString());
        issue.setVersion(getTableValueAt(row, 7).toString());
        issue.setDateClosed(getTableValueAt(row, 8).toString());
        issue.setIssueType(getTableValueAt(row, 9).toString());
        issue.setSubmitter(getTableValueAt(row, 10).toString());
        issue.setLocked(getTableValueAt(row, 11).toString());
    }
    
    /**
     * This returns cell value of table but replaces null with "" to handle
     * null pointer exceptions.
     * @param row row of table cell
     * @param col column of table cell
     * @return Object of cell value but null values are replaced with ""
     */
    private Object getTableValueAt(int row, int col){
        return (table.getValueAt(row, col)==null)?"":table.getValueAt(row, col);
    }
    
    /**
     * Sets the issue values from the components and fields from issue window.
     */
    private void setIssueValuesFromComponents() {
        issue.setId(Integer.parseInt(idText.getText()));
        issue.setApp(appComboBox.getSelectedItem().toString());
        issue.setTitle(titleText.getText());
        issue.setDescription(descriptionText.getText());
        issue.setProgrammer(programmerComboBox.getSelectedItem().toString());
        issue.setDateOpened(dateOpenedText.getText());
        issue.setRk(rkComboBox.getSelectedItem().toString());
        issue.setVersion(versionText.getText());
        issue.setDateClosed(dateClosedText.getText());
        issue.setIssueType(comboBoxIssueType.getSelectedItem().toString());
        issue.setSubmitter(submitterText.getText());
        issue.setLocked((lockCheckBox.isSelected())?"Y":"");
    }

    /**
     * Sets the components and fields on issue window from the issue object.
     */
    private void setComponentValuesFromIssue() {

        idText.setText(Integer.toString(issue.getId()));
        appComboBox.setSelectedItem(issue.getApp());
        titleText.setText(issue.getTitle());
        descriptionText.setText(issue.getDescription());
        programmerComboBox.setSelectedItem(issue.getProgrammer());
        dateOpenedText.setText(issue.getDateOpened());
        rkComboBox.setSelectedItem(issue.getRk());
        versionText.setText(issue.getVersion());
        dateClosedText.setText(issue.getDateClosed());
        comboBoxIssueType.setSelectedItem(issue.getIssueType());
        submitterText.setText(issue.getSubmitter());
        lockCheckBox.setSelected(issue.getLocked().equals("Y")?true:false);
        
        setOpenCloseIssueBtnText(); // set button text to Open/Close issue
    }
    
    /**
     * This method compares the values of the issue with the component values
     * and returns true or false.
     * @return boolean true if there is a change in any of the component values
     */
    private boolean hasChange(){

        return ( appComboBox.getSelectedItem().equals(issue.getApp())
                &&titleText.getText().equals(issue.getTitle())
            && descriptionText.getText().equals(issue.getDescription())
            && programmerComboBox.getSelectedItem().equals(issue.getProgrammer())
            && dateOpenedText.getText().equals(issue.getDateOpened())
            && rkComboBox.getSelectedItem().equals(issue.getRk())
            && versionText.getText().equals(issue.getVersion())
            && dateClosedText.getText().equals(issue.getDateClosed())
            && comboBoxIssueType.getSelectedItem().equals(issue.getIssueType())
            && submitterText.getText().equals(issue.getSubmitter())
            && (lockCheckBox.isSelected()?"Y":"").equals(issue.getLocked()))
            ?false:true;
    }

    private void setOpenCloseIssueBtnText() {
        //set close issue btn property
        if (dateClosedText.getText().isEmpty() || versionText.getText().isEmpty()) {
            btnCloseIssue.setText("Close Issue");
        } else {
            btnCloseIssue.setText("Reopen Issue");
        }
    }

    private void addDocumentListener(ArrayList<JTextComponent> textComponentList) {
        
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkForChangeAndSetBtnsEnabled();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkForChangeAndSetBtnsEnabled();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // this does not get fired for plain text
            }
        };
        
        for(JTextComponent comp:textComponentList){
            comp.getDocument().addDocumentListener(documentListener);
        }      
    }
    
    /**
     * Checks all components for a change in value
     * and sets the submit or confirm buttons accordingly.
     */
    private void checkForChangeAndSetBtnsEnabled() {
        setBtnsEnabled(hasChange());
    }
    
    /**
     * Sets the visible button enabled for either submit or confirm
     * @param hasChange if true then sets the button enabled or disables if false.
     */
    private void setBtnsEnabled(boolean hasChange){
        if(buttonSubmit.isVisible()){
            buttonSubmit.setEnabled(hasChange);
        }
        else if(buttonConfirm.isVisible()){
            buttonConfirm.setEnabled(hasChange);
        }
    }
    private Map loadingDropdownList() {
        String selectedTabName = projectManager.getSelectedTabName();
        Tab tab = tabs.get(selectedTabName);
        
   
        Map<Integer, ArrayList<Object>> valueListMap = new HashMap();
        if (!selectedTabName.equalsIgnoreCase("issue_files")) {
            for (String searchField : dropdownlist) {

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
                System.out.println(uniqueList);
            }
        }
        return valueListMap;

    }
        private void updateComboList(String colName, String tableName) {
        //create a combo box model
        DefaultComboBoxModel comboBoxSearchModel = new DefaultComboBoxModel();
        if (colName.equalsIgnoreCase("programmer")) {
            programmerComboBox.setModel(comboBoxSearchModel);
        } else if (colName.equalsIgnoreCase("rk")) {
            rkComboBox.setModel(comboBoxSearchModel);
        } else if (colName.equalsIgnoreCase("app")) {
            appComboBox.setModel(comboBoxSearchModel);
        }


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
                } else if (colName.equalsIgnoreCase("programmer") || colName.equalsIgnoreCase("app") ) {
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

                for (Object item : dropDownList) {
 
                    comboBoxSearchModel.addElement(item);

                }

            }
        }
//        comboBoxForSearch.setSelectedItem("Enter " + colName + " here");
//        comboBoxStartToSearch = true;
    }
            private void setComboBoxValue() {
        int row = projectManager.getSelectedTable().getSelectedRow();
        String programmer = "";
        String rk = "";
        String app = "";
        if (projectManager.getSelectedTable().getModel().getValueAt(row, 1) != null) {
            app = projectManager.getSelectedTable().getModel().getValueAt(row, 1).toString();
        }
        if (projectManager.getSelectedTable().getModel().getValueAt(row, 4) != null) {
            programmer = projectManager.getSelectedTable().getModel().getValueAt(row, 4).toString();
        }
        if (projectManager.getSelectedTable().getModel().getValueAt(row, 6) != null) {
            rk = projectManager.getSelectedTable().getModel().getValueAt(row, 6).toString();
        }
        programmerComboBox.setSelectedItem(programmer);
        rkComboBox.setSelectedItem(rk);
        appComboBox.setSelectedItem(app);

    }
    

//    private void updateComboBoxValue() {
//        int row = projectManager.getSelectedTable().getSelectedRow();
//        String programmer = "";
//        String rk = "";
//        String app = "";
//        programmer = programmerComboBox.getSelectedItem().toString();
//        rk = rkComboBox.getSelectedItem().toString();
//        app = appComboBox.getSelectedItem().toString();
//        projectManager.getSelectedTable().getModel().setValueAt(programmer, row, 4);
//        projectManager.getSelectedTable().getModel().setValueAt(rk, row, 6);
//        projectManager.getSelectedTable().getModel().setValueAt(app, row, 1);
//
//    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnNext;
    private javax.swing.JButton BtnPrevious;
    private javax.swing.JLabel app;
    private javax.swing.JComboBox appComboBox;
    private javax.swing.JButton btnCloseIssue;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonConfirm;
    private javax.swing.JButton buttonSubmit;
    private javax.swing.JComboBox<String> comboBoxIssueType;
    private javax.swing.JLabel dateClosed;
    private javax.swing.JTextField dateClosedText;
    private javax.swing.JLabel dateOpened;
    private javax.swing.JTextField dateOpenedText;
    private javax.swing.JLabel description;
    private javax.swing.JTextArea descriptionText;
    private javax.swing.JPanel formPane;
    private javax.swing.JLabel id;
    private javax.swing.JLabel idText;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lock;
    private javax.swing.JCheckBox lockCheckBox;
    private javax.swing.JLabel programmer;
    private javax.swing.JComboBox programmerComboBox;
    private javax.swing.JLabel rk;
    private javax.swing.JComboBox rkComboBox;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel submitter;
    private javax.swing.JTextField submitterText;
    private javax.swing.JLabel title;
    private javax.swing.JTextField titleText;
    private javax.swing.JLabel version;
    private javax.swing.JTextField versionText;
    // End of variables declaration//GEN-END:variables

}
