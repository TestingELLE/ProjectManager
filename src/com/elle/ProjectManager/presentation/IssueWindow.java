package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.admissions.Authorization;
import com.elle.ProjectManager.controller.PMDataManager;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.CustomComboBoxRenderer;
import com.elle.ProjectManager.logic.ShortCutSetting;
import com.elle.ProjectManager.logic.Tab;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.JTextComponent;
import javax.swing.text.LabelView;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.ParagraphView;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.StyledEditorKit.ItalicAction;
import javax.swing.text.StyledEditorKit.UnderlineAction;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.rtf.RTFEditorKit;

/**
 *
 * @author fuxiaoqian
 */
public class IssueWindow extends JFrame {

    private ProjectManagerWindow projectManager;
    private PMDataManager dataManager;
    private Tab tab;
    private Issue issue;
    private JTable table;
    private int row;
    private boolean addIssueMode;
    private boolean refIssueMode;
    
    private String previousValue= "";
    private ShortCutSetting ShortCutSetting;
    private Map<Integer, Tab> tabs;       // used to update the records label
    private int originalId;

    
    
    
    IssueWindow(int id, Tab tab) {
        
        //set singleton reference
        projectManager = ProjectManagerWindow.getInstance();
        dataManager = PMDataManager.getInstance();
        
        //set up tab and table
        this.tab = tab;
        this.table = tab.getTable();
        String tableName = table.getName();
                
        
        //set up modes
        if(tableName.equals("References")) {
            refIssueMode = true;
        }

        if (id == -1) {
            addIssueMode = true;
        }
        
        //if new issue
        if (addIssueMode) {
            issue = new Issue();
            issue.setId(-1);
            if(!refIssueMode) {
                issue.setApp(tableName);
                issue.setSubmitter(projectManager.getUserName());
                issue.setIssueType("TEST ISSUE");
 
            }
            else issue.setIssueType("REFERENCE");
            issue.setDateOpened(todaysDate());
        }
        
        //if open exsiting issue
        else{
            if(refIssueMode) issue = dataManager.getReferenceEntity(id);
            else issue = dataManager.getIssueEntity(id);
            originalId = id;
        }
        
        //set up gui components
        initComponents();
        
        //set up window for ref or issue
        if(refIssueMode) {
            setUpRefIssueWindow();
        }
        
        else{
            setUpIssueWindow();
        }
        
        this.setTitle("Issue in " + tableName);
        
        
        // get current monitor resolution.height
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // set the preferred framesize
        Dimension frameSize = new Dimension(620, 750);
       
        // if the screensize is not big enough, change the preferred size height
        if (screenSize.height * 0.85 < frameSize.height) {
            frameSize.height = (int)(screenSize.height * 0.85);
        }
        
       // set the minimum size, width 620, height 750 or 80% of screen size
        //int minHeight = (screenSize.height * 0.85 < 750) ? (int)(screenSize.height * 0.8) : 750; 
        Dimension minSize = new Dimension(620, 400);
        
        this.setPreferredSize(frameSize);
        this.setMinimumSize(minSize);
        
 
        // set view issue window location in screen
        // check x and y , if beyond the boarder, set to default 10 and 5
        Point pmWindowLocation = projectManager.getLocationOnScreen(); //get the project manager window in screen
        int numWindow = projectManager.getOpeningIssuesList().size();
        int x = (pmWindowLocation.x - 150 > 0)? pmWindowLocation.x - 150 :10;
        int y = (pmWindowLocation.y - 120 > 0)?pmWindowLocation.y - 120 : 5;
        this.setLocation(x + numWindow * 30, y + numWindow * 15); // set location of view issue window depend on how many window open

        this.pack();
        
        Authorization.authorize(this);
        
        
        
    }
    
    
    public void setVersionText(JTextField versionText) {
        this.versionText = versionText;
    }
    
    
    private void setUpIssueWindow(){
        //implement the logic for disabling 'test issue' or not
        //if not in new issue mode, if not admin, and not test issue, disable the "test issue".
        if (projectManager.isOnline() && !addIssueMode && !issue.getIssueType().equals(comboBoxIssueType.getItemAt(3)) &&
                !Authorization.getAccessLevel().equals("administrator")) {
            CustomComboBoxRenderer customRenderer = new CustomComboBoxRenderer();
            DefaultListSelectionModel model = new DefaultListSelectionModel();
            model.addSelectionInterval(0, 2);
            customRenderer.setEnabledItems(model);
            comboBoxIssueType.setRenderer(customRenderer);
           //previousValue is defined as the issuewindow class data member, 
            //thus it can stay as long as the issue window stays
            //therefore be available to the comboBox all the time
            //you can not define it as local variable
            previousValue = (String)comboBoxIssueType.getSelectedItem();
            comboBoxIssueType.addActionListener (new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        if (comboBoxIssueType.getSelectedIndex() == 3) {
                            comboBoxIssueType.setSelectedItem(previousValue);
                        }
                        else{
                            previousValue = (String)comboBoxIssueType.getSelectedItem();
                        }
                            
                    }
            });
            
        } 
        

        
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
        textComponentList.add(rtftext);
   
        textComponentList.add(dateClosedText);
        textComponentList.add(versionText);
        addDocumentListener(textComponentList);
        addInputMappingsAndShortcuts(textComponentList);
        updateComboList("programmer");
        updateComboList("rk");
        updateComboList("app");

        setComponentValuesFromIssue(this); 
        setOpenCloseIssueBtnText();
        setIssueWindowMode();
        
    }
    
    private void setUpRefIssueWindow(){
        
        /**
         * Add all JTextComponents to add document listener, input mappings,
         * and shortcuts.
         * Note: ComboBox and CheckBox components can use the action event.
         * You can double click it on the designer to create one for it.
         * You can reference one that exists for help with the code if needed.
         */
        ArrayList<JTextComponent> textComponentList = new ArrayList<>();
        textComponentList.add(dateOpenedText);
        
        textComponentList.add(titleText);
        textComponentList.add(rtftext);
   
        addDocumentListener(textComponentList);
        addInputMappingsAndShortcuts(textComponentList);
        updateComboList("programmer");
        
        setComponentValuesFromIssue(this);
        
        setIssueWindowMode();
        //hide components
        comboBoxIssueType.setVisible(false);
        submitter.setVisible(false);
        submitterText.setVisible(false);
        rk.setVisible(false);
        rkComboBox.setVisible(false);
        app.setVisible(false);
        appComboBox.setVisible(false);
        btnCloseIssue.setVisible(false);
        dateClosed.setVisible(false);
        dateClosedText.setVisible(false);
        version.setVisible(false);
        versionText.setVisible(false);
        
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
        java.awt.GridBagConstraints gridBagConstraints;

        jMenu1 = new javax.swing.JMenu();
        jScrollBar1 = new javax.swing.JScrollBar();
        scrollPane = new javax.swing.JScrollPane();
        formPane = new javax.swing.JPanel();
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
        jLabel1 = new javax.swing.JLabel();
        lastmodTime = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lock = new javax.swing.JLabel();
        idText = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        lockCheckBox = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        comboBoxIssueType = new javax.swing.JComboBox<String>();
        submitter = new javax.swing.JLabel();
        submitterText = new javax.swing.JTextField();
        dateOpenedText = new javax.swing.JTextField();
        dateOpened = new javax.swing.JLabel();
        programmerComboBox = new javax.swing.JComboBox();
        programmer = new javax.swing.JLabel();
        rk = new javax.swing.JLabel();
        rkComboBox = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        titleText = new javax.swing.JTextField();
        title = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        BtnNext = new javax.swing.JButton();
        BtnPrevious = new javax.swing.JButton();
        description = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rtftext = new javax.swing.JTextPane();
        Fsize = new javax.swing.JButton();
        Italic = new javax.swing.JButton();
        StrikethroughBotton = new javax.swing.JButton();
        UnderlineBotton = new javax.swing.JButton();
        B_Bold = new javax.swing.JButton();
        colorButton1 = new javax.swing.JButton();
        Plain = new javax.swing.JButton();
        Hyperlink = new javax.swing.JButton();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        formPane.setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel1.add(buttonCancel, gridBagConstraints);

        buttonSubmit.setText("Submit");
        buttonSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSubmitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel1.add(buttonSubmit, gridBagConstraints);

        dateClosed.setText(" dateClosed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel1.add(dateClosed, gridBagConstraints);

        version.setText(" version");
        version.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel1.add(version, gridBagConstraints);

        buttonConfirm.setText("Confirm");
        buttonConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonConfirmActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel1.add(buttonConfirm, gridBagConstraints);

        dateClosedText.setText("jTextField2");
        dateClosedText.setName("dateClosed"); // NOI18N
        dateClosedText.setPreferredSize(new java.awt.Dimension(90, 28));
        dateClosedText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateClosedTextKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel1.add(dateClosedText, gridBagConstraints);

        versionText.setText("jTextField1");
        versionText.setMinimumSize(new java.awt.Dimension(10, 20));
        versionText.setName("version"); // NOI18N
        versionText.setPreferredSize(new java.awt.Dimension(90, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel1.add(versionText, gridBagConstraints);

        btnCloseIssue.setText("Close Issue");
        btnCloseIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseIssueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel1.add(btnCloseIssue, gridBagConstraints);

        app.setText(" app");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel1.add(app, gridBagConstraints);

        appComboBox.setEditable(true);
        appComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        appComboBox.setPreferredSize(new java.awt.Dimension(90, 28));
        appComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                appComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(appComboBox, gridBagConstraints);

        jLabel1.setText("lastModTime : ");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel1.setIconTextGap(0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel1.add(jLabel1, gridBagConstraints);

        lastmodTime.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lastmodTime.setIconTextGap(0);
        lastmodTime.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(lastmodTime, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        formPane.add(jPanel1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        lock.setText(" lock");
        lock.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        lock.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        jPanel6.add(lock, gridBagConstraints);

        idText.setText("jLabel1");
        idText.setMaximumSize(new java.awt.Dimension(34, 13));
        idText.setMinimumSize(new java.awt.Dimension(34, 13));
        idText.setPreferredSize(new java.awt.Dimension(34, 13));
        idText.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 19, 0, 0);
        jPanel6.add(idText, gridBagConstraints);

        id.setText(" id");
        id.setMaximumSize(new java.awt.Dimension(13, 12));
        id.setMinimumSize(new java.awt.Dimension(13, 12));
        id.setPreferredSize(new java.awt.Dimension(15, 13));
        id.setRequestFocusEnabled(false);
        id.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel6.add(id, gridBagConstraints);

        lockCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lockCheckBox.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lockCheckBox.setMaximumSize(new java.awt.Dimension(13, 13));
        lockCheckBox.setMinimumSize(new java.awt.Dimension(13, 13));
        lockCheckBox.setPreferredSize(new java.awt.Dimension(13, 13));
        lockCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 30, 0, 0);
        jPanel6.add(lockCheckBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        jPanel3.add(jPanel6, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        comboBoxIssueType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FEATURE", "BUG", "REFERENCE", "TEST ISSUE" }));
        comboBoxIssueType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxIssueTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel7.add(comboBoxIssueType, gridBagConstraints);

        submitter.setText(" submitter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel7.add(submitter, gridBagConstraints);

        submitterText.setMinimumSize(new java.awt.Dimension(90, 20));
        submitterText.setPreferredSize(new java.awt.Dimension(90, 28));
        submitterText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitterTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel7.add(submitterText, gridBagConstraints);

        dateOpenedText.setText("jTextField1");
        dateOpenedText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        dateOpenedText.setMargin(new java.awt.Insets(-1, -1, -1, -1));
        dateOpenedText.setMinimumSize(new java.awt.Dimension(6, 20));
        dateOpenedText.setName("dateOpened"); // NOI18N
        dateOpenedText.setPreferredSize(new java.awt.Dimension(90, 28));
        dateOpenedText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateOpenedTextActionPerformed(evt);
            }
        });
        dateOpenedText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dateOpenedTextKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel7.add(dateOpenedText, gridBagConstraints);

        dateOpened.setText(" dateOpened");
        dateOpened.setPreferredSize(new java.awt.Dimension(79, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel7.add(dateOpened, gridBagConstraints);

        programmerComboBox.setEditable(true);
        programmerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        programmerComboBox.setMinimumSize(new java.awt.Dimension(90, 20));
        programmerComboBox.setName(""); // NOI18N
        programmerComboBox.setPreferredSize(new java.awt.Dimension(90, 28));
        programmerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                programmerComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel7.add(programmerComboBox, gridBagConstraints);

        programmer.setText(" programmer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel7.add(programmer, gridBagConstraints);

        rk.setText(" rk");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel7.add(rk, gridBagConstraints);

        rkComboBox.setEditable(true);
        rkComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        rkComboBox.setPreferredSize(new java.awt.Dimension(40, 28));
        rkComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rkComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel7.add(rkComboBox, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        formPane.add(jPanel3, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        titleText.setText("jTextField1");
        titleText.setName("title"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel4.add(titleText, gridBagConstraints);

        title.setText(" title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(title, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        formPane.add(jPanel4, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        BtnNext.setText(">");
        BtnNext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        BtnNext.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        BtnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnNextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel5.add(BtnNext, gridBagConstraints);

        BtnPrevious.setText("<");
        BtnPrevious.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BtnPrevious.setIconTextGap(0);
        BtnPrevious.setMinimumSize(new java.awt.Dimension(41, 50));
        BtnPrevious.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        BtnPrevious.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        BtnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPreviousActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel5.add(BtnPrevious, gridBagConstraints);

        description.setText(" description");
        description.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
        jPanel5.add(description, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 100));
        jScrollPane1.setVerticalScrollBar(jScrollBar1);

        rtftext.setEditorKit(new WrapEditorKit());
        rtftext.setContentType("text/rtf"); // NOI18N
        rtftext.setMinimumSize(new java.awt.Dimension(25, 25));
        rtftext.setPreferredSize(new java.awt.Dimension(100, 98));
        rtftext.setRequestFocusEnabled(true);
        rtftext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rtftextMouseClicked(evt);
            }
        });
        rtftext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rtftextKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(rtftext);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jScrollPane1, gridBagConstraints);

        Fsize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/elle/ProjectManager/presentation/Font3.png"))); // NOI18N
        Fsize.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Fsize.setMargin(new java.awt.Insets(1, 2, 0, 2));
        Fsize.setMaximumSize(new java.awt.Dimension(16, 16));
        Fsize.setMinimumSize(new java.awt.Dimension(16, 16));
        Fsize.setPreferredSize(new java.awt.Dimension(16, 16));
        Fsize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FsizeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 0);
        jPanel5.add(Fsize, gridBagConstraints);

        Italic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/elle/ProjectManager/presentation/Italic_os2.png"))); // NOI18N
        Italic.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Italic.setMargin(new java.awt.Insets(1, 2, 0, 2));
        Italic.setMaximumSize(new java.awt.Dimension(16, 16));
        Italic.setMinimumSize(new java.awt.Dimension(16, 16));
        Italic.setPreferredSize(new java.awt.Dimension(16, 16));
        Italic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ItalicActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 0);
        jPanel5.add(Italic, gridBagConstraints);

        StrikethroughBotton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/elle/ProjectManager/presentation/Strike_os2.png"))); // NOI18N
        StrikethroughBotton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        StrikethroughBotton.setMaximumSize(new java.awt.Dimension(16, 16));
        StrikethroughBotton.setMinimumSize(new java.awt.Dimension(16, 16));
        StrikethroughBotton.setPreferredSize(new java.awt.Dimension(16, 16));
        StrikethroughBotton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StrikethroughBottonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 0);
        jPanel5.add(StrikethroughBotton, gridBagConstraints);

        UnderlineBotton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/elle/ProjectManager/presentation/Underline_os.png"))); // NOI18N
        UnderlineBotton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        UnderlineBotton.setMargin(new java.awt.Insets(1, 2, 0, 2));
        UnderlineBotton.setMaximumSize(new java.awt.Dimension(16, 16));
        UnderlineBotton.setMinimumSize(new java.awt.Dimension(16, 16));
        UnderlineBotton.setPreferredSize(new java.awt.Dimension(16, 16));
        UnderlineBotton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnderlineBottonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 0);
        jPanel5.add(UnderlineBotton, gridBagConstraints);

        B_Bold.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/elle/ProjectManager/presentation/Bold_os.png"))); // NOI18N
        B_Bold.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        B_Bold.setMargin(new java.awt.Insets(1, 2, 0, 2));
        B_Bold.setMaximumSize(new java.awt.Dimension(16, 16));
        B_Bold.setMinimumSize(new java.awt.Dimension(16, 16));
        B_Bold.setPreferredSize(new java.awt.Dimension(16, 16));
        B_Bold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_BoldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 0);
        jPanel5.add(B_Bold, gridBagConstraints);

        colorButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/elle/ProjectManager/presentation/Color3.png"))); // NOI18N
        colorButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        colorButton1.setMaximumSize(new java.awt.Dimension(16, 16));
        colorButton1.setMinimumSize(new java.awt.Dimension(16, 16));
        colorButton1.setPreferredSize(new java.awt.Dimension(16, 16));
        colorButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 0);
        jPanel5.add(colorButton1, gridBagConstraints);

        Plain.setText("P");
        Plain.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Plain.setMargin(new java.awt.Insets(1, 2, 0, 2));
        Plain.setMaximumSize(new java.awt.Dimension(16, 16));
        Plain.setMinimumSize(new java.awt.Dimension(16, 16));
        Plain.setPreferredSize(new java.awt.Dimension(16, 16));
        Plain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlainActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 0);
        jPanel5.add(Plain, gridBagConstraints);

        Hyperlink.setText("H");
        Hyperlink.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Hyperlink.setMargin(new java.awt.Insets(1, 2, 0, 2));
        Hyperlink.setMaximumSize(new java.awt.Dimension(16, 16));
        Hyperlink.setMinimumSize(new java.awt.Dimension(16, 16));
        Hyperlink.setPreferredSize(new java.awt.Dimension(16, 16));
        Hyperlink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HyperlinkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 1, 0);
        jPanel5.add(Hyperlink, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 10, 0);
        formPane.add(jPanel5, gridBagConstraints);

        scrollPane.setViewportView(formPane);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 7, 15);
        getContentPane().add(scrollPane, gridBagConstraints);

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
     * ##############requires mod
     */
    private void updateCustomIdList(int oldID) {
        
        // remove this id from the openIssuesList and CustomIdList
        projectManager.getOpeningIssuesList().remove(table.getName() + oldID, this);
        tab.getFilter().getCustomIdListFilter().remove((Object)oldID);
        
        int newID = issue.getId();

        String identifier = table.getName() + newID;

        // if issue is not open
        if (!projectManager.getOpeningIssuesList().containsKey(identifier)) {
            projectManager.getOpeningIssuesList().put(identifier, this);
            tab.getFilter().getCustomIdListFilter().add(newID);
         
        } 
        // use the window with this issue already open
        else {
            projectManager.getViewIssueWindowOf(identifier).toFront();
            this.dispose();
        }
    }



    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        issueWindowClosing();

    }//GEN-LAST:event_formWindowClosing

    private void rkComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rkComboBoxActionPerformed
        if(rkComboBox.getSelectedItem().toString().equals(issue.getRk())){
            checkForChangeAndSetBtnsEnabled();
        }
        // we know right away there is a change so just set the button enabled
        else{
            setBtnsEnabled(true); // sets the submit or confirm button enabled
        }
    }//GEN-LAST:event_rkComboBoxActionPerformed

    private void programmerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_programmerComboBoxActionPerformed
        if(programmerComboBox.getSelectedItem().toString().equals(issue.getProgrammer())){
            checkForChangeAndSetBtnsEnabled();
        }
        // we know right away there is a change so just set the button enabled
        else{
            setBtnsEnabled(true); // sets the submit or confirm button enabled
        }
    }//GEN-LAST:event_programmerComboBoxActionPerformed

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

    private void submitterTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitterTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_submitterTextActionPerformed

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
            //keep record of old id
            int oldId = issue.getId();
            row--;
            
            int newId = (int)table.getValueAt(row, 0);
            if (table.getName().equals("References")) 
                issue = dataManager.getReferenceEntity(newId);
            else
                issue = dataManager.getIssueEntity(newId);
            
            updateCustomIdList(oldId);
                
            setComponentValuesFromIssue(this);
            table.setRowSelectionInterval(row, row);
            
        }
    }//GEN-LAST:event_BtnPreviousActionPerformed

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
            
            int oldId = issue.getId();
            row++;
            
            int newId = (int)table.getValueAt(row, 0);
            if (table.getName().equals("References")) 
                issue = dataManager.getReferenceEntity(newId);
            else
                issue = dataManager.getIssueEntity(newId);
            
            updateCustomIdList(oldId);
            
            setComponentValuesFromIssue(this);
            
            table.setRowSelectionInterval(row, row);
            
            
        }
    }//GEN-LAST:event_BtnNextActionPerformed

    private void appComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_appComboBoxActionPerformed
        if(appComboBox.getSelectedItem().toString().equals(issue.getApp())){
            checkForChangeAndSetBtnsEnabled();
        }
        // we know right away there is a change so just set the button enabled
        else{
            setBtnsEnabled(true); // sets the submit or confirm button enabled
        }
    }//GEN-LAST:event_appComboBoxActionPerformed

    private void btnCloseIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseIssueActionPerformed

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        String userName = projectManager.getUserName();

        //clean the text pane

        ByteArrayOutputStream getcurrentdescriptiontext = new ByteArrayOutputStream();
        try {
            rtftext.getEditorKit().write(getcurrentdescriptiontext, rtftext.getDocument(), 0, rtftext.getDocument().getLength());
        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] getcurrentdescriptiontextbytearray = getcurrentdescriptiontext.toByteArray();
        String rtfstring = getcurrentdescriptiontext.toString();
        String newrtfstring = rtfstring.substring(0,rtfstring.length()-2);
        
       
//        System.out.println(newrtfstring);
//        int start = newrtfstring.indexOf("{\\colortbl");
//        int stop = newrtfstring.indexOf("}", start);
//        //find current color counts
//        String colortable = newrtfstring.substring(start, stop);
//        int index = 0 , count = 0;
//        do {
//            index  = colortable.indexOf("red", index);
//            if ( index == -1 ) break;
//            index += "red".length();
//            count++;
//        } while( true );
//        
//        if (colortable.indexOf("\\red0\\green51\\blue204") == -1){
//            newrtfstring = newrtfstring.substring(0, stop) + "\\red0\\green51\\blue204;" + newrtfstring.substring(stop);
//            
//        }
//       String newcolor = "\\cf" + count;     
//        
//        
//        //System.out.println(newrtfstring);

        if (btnCloseIssue.getText().equalsIgnoreCase("close issue")) {
            // set dateClosed text field with date today
            FillItWithDate(dateClosedText);
            String temperaryVersion = "XXX";
            versionText.setText(temperaryVersion);
            btnCloseIssue.setText("Reopen Issue");

            newrtfstring = newrtfstring + "\n-- Issue Closed by "
            + userName + " on " + today + " --\\par";
            newrtfstring = newrtfstring + "\n}";

        } else if (btnCloseIssue.getText().equalsIgnoreCase("reopen issue")) {
            
            newrtfstring = newrtfstring + "\n \n-- Issue reopened by "
                    
            + userName + " on " + today + " (version " + versionText.getText() + ")" + " --\\par";
            newrtfstring = newrtfstring + "\n}" ;

            versionText.setText("");
            dateClosedText.setText("");
            btnCloseIssue.setText("Close Issue");
            
            rtftext.requestFocus();
        }
        
        System.out.println(newrtfstring);

        byte[] close_openrtfbytearray = newrtfstring.getBytes(Charset.forName("UTF-8"));
        InputStream close_openrtfstream = new ByteArrayInputStream(close_openrtfbytearray);
        try {
            rtftext.setText("");
            rtftext.getEditorKit().read(close_openrtfstream, rtftext.getDocument(), 0);
            //descriptionText.setText(value);
        } catch (IOException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCloseIssueActionPerformed

    private void dateClosedTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateClosedTextKeyReleased
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_D) {
            FillItWithDate((JTextField) evt.getComponent());
        }
    }//GEN-LAST:event_dateClosedTextKeyReleased

    /**
     * This method is called when the confirm button is pressed.
     * @param evt 
     */
    private void buttonConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonConfirmActionPerformed
        setIssueValuesFromComponents();
        if(refIssueMode)
            dataManager.updateReference(issue);
        else
            dataManager.updateIssue(issue);
                
        //update rows in tab
        Object[] rowData = dataManager.getIssue(issue);
        tab.updateRow(rowData);
        
        
        issueWindowClosing();
    }//GEN-LAST:event_buttonConfirmActionPerformed

    
    
    
    /**
     * This method is called when the submit button is pressed.
     * @param evt 
     */
    private void buttonSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSubmitActionPerformed

        setIssueValuesFromComponents();
        if(refIssueMode)
            dataManager.insertReference(issue);
        else
            dataManager.insertIssue(issue);
                
        //insert row in tab
        Object[] rowData = dataManager.getIssue(issue);
        tab.insertRow(rowData);

        issueWindowClosing();
    }//GEN-LAST:event_buttonSubmitActionPerformed
    
    
    
    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        
        issueWindowClosing();
    }//GEN-LAST:event_buttonCancelActionPerformed

    private void dateOpenedTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateOpenedTextKeyReleased
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_D) {
            this.FillItWithDate((JTextField) evt.getComponent());
        }
    }//GEN-LAST:event_dateOpenedTextKeyReleased

    private void dateOpenedTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateOpenedTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateOpenedTextActionPerformed

    private void FsizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FsizeActionPerformed
        Action b = (Action) new FontAndSizeAction();
        b.actionPerformed(evt);
    }//GEN-LAST:event_FsizeActionPerformed

    private void ItalicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ItalicActionPerformed
        Action b = new ItalicAction();
        b.actionPerformed(evt);
    }//GEN-LAST:event_ItalicActionPerformed

    private void StrikethroughBottonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StrikethroughBottonActionPerformed
        Action b = (Action) new StrikethroughAction();
        b.actionPerformed(evt);
    }//GEN-LAST:event_StrikethroughBottonActionPerformed

    private void UnderlineBottonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnderlineBottonActionPerformed
        Action b = new UnderlineAction();
        b.actionPerformed(evt);
    }//GEN-LAST:event_UnderlineBottonActionPerformed

    private void B_BoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_BoldActionPerformed
        Action b = new BoldAction();
        b.actionPerformed(evt);
    }//GEN-LAST:event_B_BoldActionPerformed

    private void colorButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorButton1ActionPerformed
        Action b = new ForegroundAction();
        b.actionPerformed(evt);
    }//GEN-LAST:event_colorButton1ActionPerformed

    private void rtftextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rtftextKeyReleased
        JTextPane rtftext = (JTextPane) evt.getComponent();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        
        ByteArrayOutputStream getcurrentdescriptiontext = new ByteArrayOutputStream();
        try {
            rtftext.getEditorKit().write(getcurrentdescriptiontext, rtftext.getDocument(), 0, rtftext.getDocument().getLength());
        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        String rtfstring = getcurrentdescriptiontext.toString();
        String value = rtfstring.substring(0,rtfstring.length()-2);
        
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_D) {
            String value1 = value + "\n" + today + "\\par";
            value1 = value1 + "\n}";
            byte[] value1bytearray = value1.getBytes(Charset.forName("UTF-8"));
            InputStream value1rtfstream = new ByteArrayInputStream(value1bytearray);
            try {
                rtftext.setText("");
                rtftext.getEditorKit().read(value1rtfstream, rtftext.getDocument(), 0);
            } catch (IOException ex) {
                Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadLocationException ex) {
                Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_N) {

            String userName = projectManager.getUserName();
            String message1 = value + "\n" + "-- by " + userName + " on " + today + "-- \n";
            message1 = message1 + "}";
            byte[] messagertfbytearray = message1.getBytes(Charset.forName("UTF-8"));
            InputStream messagertfinputstream = new ByteArrayInputStream(messagertfbytearray);
            try {
                rtftext.setText("");
                rtftext.getEditorKit().read(messagertfinputstream, rtftext.getDocument(), 0);
                //descriptionText.setText(value);
            } catch (IOException ex) {
                Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadLocationException ex) {
                Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
            }


        }      
    }//GEN-LAST:event_rtftextKeyReleased

    private void PlainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlainActionPerformed
        Action b = new getplaintext();
        b.actionPerformed(evt);
    }//GEN-LAST:event_PlainActionPerformed

    private void HyperlinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HyperlinkActionPerformed
        Action b = new seturltextformat();
        b.actionPerformed(evt);
    }//GEN-LAST:event_HyperlinkActionPerformed

    private void rtftextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rtftextMouseClicked
        if (evt.isControlDown()) {

            String selectedtext = getselectedtext();
            String urlstring = selectedtext;
            String currentstring = getcurrentrtf();
            String stringtosearch = "\\i\\ul\\cf2 " + selectedtext.substring(0,10);
            boolean checkformat = currentstring.contains(stringtosearch);
            
            if (!urlstring.contains("https") && !urlstring.contains("http")) {
                String urlstringcheck = System.getProperty("user.dir");
                System.out.println(urlstringcheck);
                
                if (urlstringcheck.contains("ELLE Prog 2015")) {
                    urlstring = "file://" + System.getProperty("user.dir") + urlstring;
                    urlstring = urlstring.replace(" ","%20");
                    System.out.println(urlstring);
                } else {                   
                    urlstring = "file://" + System.getProperty("user.home")+File.separator+"Dropbox" + urlstring;
                    urlstring = urlstring.replace(" ","%20");
                }
            } else {
                urlstring = urlstring.replaceAll("\\s","");
            }
            
            try {
                URI uri = new URI(urlstring);
                System.out.println(uri);
                
                if (checkformat && uri != null) {
                    openWebpage(uri);
                } else {
                    System.out.println("Not a link!");
                }

            } catch (URISyntaxException ex) {
                Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_rtftextMouseClicked
        
    public static void openWebpage(URI uri) {
        
       if (!Desktop.isDesktopSupported()) return;

	Desktop desktop = Desktop.getDesktop();

	if (!desktop.isSupported(Desktop.Action.BROWSE)) return;

	try {
		desktop.browse(uri);
	} catch (Exception e) {

	}
        
    }
    
    public class seturltextformat extends StyledEditorKit.StyledTextAction {

        private seturltextformat() {
            super("Set the hyperlink format!");        
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JEditorPane editor = getEditor(e);
            if (editor != null) {
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setUnderline(sas, true);
                StyleConstants.setForeground(sas, Color.BLUE);
                StyleConstants.setItalic(sas, true);
                setCharacterAttributes(editor, sas, true);
            }
        }
    }
    
    public String seturl() {
        JFrame frame = new JFrame("URL/File Directory");
        String urlorfiledirectory = JOptionPane.showInputDialog(frame, "URL/File Directory:");
        return urlorfiledirectory;
    }
    
    public String getselectedtext() {
        String selectedtext = rtftext.getSelectedText();
        return selectedtext;
    }
    
    public String modifyrtfforhyperlink() {
        
        ByteArrayOutputStream getcurrentdescriptiontext = new ByteArrayOutputStream();

        try {
            rtftext.getEditorKit().write(getcurrentdescriptiontext, rtftext.getDocument(), 0, rtftext.getDocument().getLength());
        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        String rtfstring = getcurrentdescriptiontext.toString();
        /*
        //String newrtfstringtoreplace = "{\\field {\\*\\fldinst HYPERLINK \\\\l \"" + urlstrings + "\"}{\\fldrslt " + selectedstring + "}}";
        String newrtfstringtoreplace = "{\\*\\fldinst HYPERLINK " + selectedstring + "\"" + urlstrings + "\"} " + selectedstring;
        String newrtfstring = rtfstring.replace(selectedstring, newrtfstringtoreplace);
        */
        return rtfstring;
    }
    
    public String getcurrentrtf () {
        
        ByteArrayOutputStream getcurrentdescriptiontext = new ByteArrayOutputStream();
        
        try {
            rtftext.getEditorKit().write(getcurrentdescriptiontext, rtftext.getDocument(), 0, rtftext.getDocument().getLength());
        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        String rtfstring = getcurrentdescriptiontext.toString();

        return rtfstring;
    }

    public class getplaintext extends StyledEditorKit.StyledTextAction {

        private getplaintext() {
            super("Clean the text!");        
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JEditorPane editor = getEditor(e);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setStrikeThrough(sas, false);
                StyleConstants.setUnderline(sas, false);
                StyleConstants.setFontSize(sas, 12);
                StyleConstants.setForeground(sas, Color.BLACK);
                StyleConstants.setFontFamily(sas, "font-family-Monospaced");
                StyleConstants.setItalic(sas, false);
                StyleConstants.setBold(sas, false);
                setCharacterAttributes(editor, sas, false);
            }
        }
    
    }
    
    public class StrikethroughAction extends StyledEditorKit.StyledTextAction {

        private static final long serialVersionUID = 9174670038684056758L;

        public StrikethroughAction() {
            super("font-bold");
        }

        public String toString() {
            return "Strikethrough";
        }

        public void actionPerformed(ActionEvent e) {
            JEditorPane editor = getEditor(e);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                boolean bold;
                bold = !(StyleConstants.isStrikeThrough(attr));
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setStrikeThrough(sas, bold);
                setCharacterAttributes(editor, sas, false);

            }
        }
    }
    
    public class ForegroundAction extends StyledEditorKit.StyledTextAction {

        private static final long serialVersionUID = 6384632651737400352L;

        JColorChooser colorChooser = new JColorChooser();

        JDialog dialog = new JDialog();

        boolean noChange = false;

        boolean cancelled = false;

        public ForegroundAction() {
            super("foreground");

        }

        public void actionPerformed(ActionEvent e) {
            JTextPane editor = (JTextPane) getEditor(e);

            if (editor == null) {
                JOptionPane.showMessageDialog(null,
                        "You need to select the editor pane before you can change the color.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int p0 = editor.getSelectionStart();
            StyledDocument doc = getStyledDocument(editor);
            Element paragraph = doc.getCharacterElement(p0);
            AttributeSet as = paragraph.getAttributes();
            fg = StyleConstants.getForeground(as);
            if (fg == null) {
                fg = Color.BLACK;
            }
            colorChooser.setColor(fg);

            JButton accept = new JButton("OK");
            accept.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    fg = colorChooser.getColor();
                    dialog.dispose();
                }
            });

            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    cancelled = true;
                    dialog.dispose();
                }
            });

            JButton none = new JButton("None");
            none.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    noChange = true;
                    dialog.dispose();
                }
            });

            JPanel buttons = new JPanel();
            buttons.add(accept);
            buttons.add(none);
            buttons.add(cancel);

            dialog.getContentPane().setLayout(new BorderLayout());
            dialog.getContentPane().add(colorChooser, BorderLayout.CENTER);
            dialog.getContentPane().add(buttons, BorderLayout.SOUTH);
            dialog.setModal(true);
            dialog.pack();
            dialog.setVisible(true);

            if (!cancelled) {

                MutableAttributeSet attr = null;
                if (editor != null) {
                    if (fg != null && !noChange) {
                        attr = new SimpleAttributeSet();
                        StyleConstants.setForeground(attr, fg);
                        setCharacterAttributes(editor, attr, false);
                    }
                }
            }// end if color != null
            noChange = false;
            cancelled = false;
        }

        private Color fg;
    }
    
    public class FontAndSizeAction extends StyledEditorKit.StyledTextAction {

        private static final long serialVersionUID = 584531387732416339L;

        private String family;

        private float fontSize;

        JDialog formatText;

        private boolean accept = false;

        JComboBox fontFamilyChooser;

        JComboBox fontSizeChooser;

        public FontAndSizeAction() {
            super("Font and Size");
        }

        public String toString() {
            return "Font and Size";
        }


         
        public void actionPerformed(ActionEvent e) {
            
            JTextPane editor = (JTextPane) getEditor(e);
            int p0 = editor.getSelectionStart();
            StyledDocument doc = getStyledDocument(editor);
            Element paragraph = doc.getCharacterElement(p0);
            AttributeSet as = paragraph.getAttributes();

            family = StyleConstants.getFontFamily(as);
            fontSize = StyleConstants.getFontSize(as);

            formatText = new JDialog(new JFrame(), "Font and Size", true);
            formatText.getContentPane().setLayout(new BorderLayout());

            JPanel choosers = new JPanel();
            choosers.setLayout(new GridLayout(2, 1));

            JPanel fontFamilyPanel = new JPanel();
            fontFamilyPanel.add(new JLabel("Font"));

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fontNames = ge.getAvailableFontFamilyNames();

            fontFamilyChooser = new JComboBox();
            for (int i = 0; i < fontNames.length; i++) {
                fontFamilyChooser.addItem(fontNames[i]);
            }
            fontFamilyChooser.setSelectedItem(family);
            fontFamilyPanel.add(fontFamilyChooser);
            choosers.add(fontFamilyPanel);

            JPanel fontSizePanel = new JPanel();
            fontSizePanel.add(new JLabel("Size"));
            fontSizeChooser = new JComboBox();
            fontSizeChooser.setEditable(true);
            fontSizeChooser.addItem(new Float(4));
            fontSizeChooser.addItem(new Float(8));
            fontSizeChooser.addItem(new Float(12));
            fontSizeChooser.addItem(new Float(16));
            fontSizeChooser.addItem(new Float(20));
            fontSizeChooser.addItem(new Float(24));
            fontSizeChooser.setSelectedItem(new Float(fontSize));
            fontSizePanel.add(fontSizeChooser);
            choosers.add(fontSizePanel);

            JButton ok = new JButton("OK");
            ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    accept = true;
                    formatText.dispose();
                    family = (String) fontFamilyChooser.getSelectedItem();
                    fontSize = Float.parseFloat(fontSizeChooser.getSelectedItem().toString());
                }
            });

            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    formatText.dispose();
                }
            });

            JPanel buttons = new JPanel();
            buttons.add(ok);
            buttons.add(cancel);
            formatText.getContentPane().add(choosers, BorderLayout.CENTER);
            formatText.getContentPane().add(buttons, BorderLayout.SOUTH);
            formatText.pack();
            formatText.setVisible(true);

            MutableAttributeSet attr = null;
            if (editor != null && accept) {
                attr = new SimpleAttributeSet();
                StyleConstants.setFontFamily(attr, family);
                StyleConstants.setFontSize(attr, (int) fontSize);
                setCharacterAttributes(editor, attr, false);
            }

        }
    }

    /**
     * Called to close the form
     */
    private void issueWindowClosing() {
        if (addIssueMode) {
            projectManager.setAddIssueWindowShow(false);
        } else {
            //if it is offline update
            if (issue.getId() == originalId + 9000)
                projectManager.getOpeningIssuesList().remove(table.getName()+ originalId, this); 
            else
                projectManager.getOpeningIssuesList().remove(table.getName()+ issue.getId(), this); 
            System.out.println(projectManager.getOpeningIssuesList().keySet());
            //convert id to Object ,otherwise, remove uses id as index.
            tab.getFilter().getCustomIdListFilter().remove((Object) issue.getId());
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
    private void setIssueValuesFromTable(int row, JTable table) throws IOException {

        issue.setId(Integer.parseInt(getTableValueAt(row, 0).toString()));
        issue.setApp(getTableValueAt(row, 1).toString());
        issue.setTitle(getTableValueAt(row, 2).toString());
        
        
        issue.setDescription(convertStringToBytearrary((String) getTableValueAt(row, 3)));
        
        issue.setProgrammer(getTableValueAt(row, 4).toString());
        issue.setDateOpened(getTableValueAt(row, 5).toString());
        issue.setRk(getTableValueAt(row, 6).toString());
        issue.setVersion(getTableValueAt(row, 7).toString());
        issue.setDateClosed(getTableValueAt(row, 8).toString());
        issue.setIssueType(getTableValueAt(row, 9).toString());
        issue.setSubmitter(getTableValueAt(row, 10).toString());
        issue.setLocked(getTableValueAt(row, 11).toString());
    }
    
    private int getcurrentissueid(int row, JTable table) throws IOException {
        
        return (int) getTableValueAt(row, 0);
    }
    
    public byte[] convertStringToBytearrary(String is) throws IOException {
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        if (is != null) {
            byte[] b = is.getBytes(Charset.forName("UTF-8"));
            return b;
        }
        return new byte[0];
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
        
        if (appComboBox.getSelectedItem() == null) {
            issue.setApp("");
        } else {
            issue.setApp(appComboBox.getSelectedItem().toString());
        }
        
        if (titleText.getText() == null) {
            issue.setTitle("");
        } else {
            issue.setTitle(titleText.getText());
        }
        
        ByteArrayOutputStream setdescriptionoutputstream = new ByteArrayOutputStream();                   
        try {
            rtftext.getEditorKit().write(setdescriptionoutputstream, rtftext.getDocument(), 0, rtftext.getDocument().getLength());
        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] setdescriptionbytearray = setdescriptionoutputstream.toByteArray();
        
        if (setdescriptionbytearray == null) {
            byte[] emptyarray = new byte[0];
            issue.setDescription(emptyarray);
        } else {
            issue.setDescription(setdescriptionbytearray);
        }
        
        if (programmerComboBox.getSelectedItem() == null) {
            issue.setProgrammer("");
        } else {
            issue.setProgrammer(programmerComboBox.getSelectedItem().toString());
        }
        
        if (dateOpenedText.getText() == null) {
            issue.setDateOpened("");
        } else {
            issue.setDateOpened(dateOpenedText.getText());
        }
        
        if (rkComboBox.getSelectedItem() == null) {
            issue.setRk("");
        } else {
            issue.setRk(rkComboBox.getSelectedItem().toString());
        }
                
        if (versionText.getText() == null) {
            issue.setVersion("");
        } else {
            issue.setVersion(versionText.getText());
        }
        
        if (dateClosedText.getText() == null) {
            issue.setDateClosed("");
        } else {
            issue.setDateClosed(dateClosedText.getText());
        }
        
        if (comboBoxIssueType.getSelectedItem() == null) {
            issue.setIssueType("");
        } else {
            issue.setIssueType(comboBoxIssueType.getSelectedItem().toString());
        }
        
        if (submitterText.getText() == null) {
            issue.setSubmitter("");
        } else {
            issue.setSubmitter(submitterText.getText());
        }
        
        if (lockCheckBox.isSelected()) {
            issue.setLocked("");
        } else {
            issue.setLocked((lockCheckBox.isSelected())?"Y":"");
        }

    }

    /**
     * Sets the components and fields on issue window from the issue object.
     */
    private void setComponentValuesFromIssue(IssueWindow aThis) {

            idText.setText(Integer.toString(issue.getId()));
            appComboBox.setSelectedItem(issue.getApp());
            titleText.setText(issue.getTitle());
            programmerComboBox.setSelectedItem(issue.getProgrammer());
            dateOpenedText.setText(issue.getDateOpened());
            rkComboBox.setSelectedItem(issue.getRk());
            versionText.setText(issue.getVersion());
            dateClosedText.setText(issue.getDateClosed());
            comboBoxIssueType.setSelectedItem(issue.getIssueType());
            submitterText.setText(issue.getSubmitter());
            lastmodTime.setText(issue.getLastmodtime());
            setOpenCloseIssueBtnText(); // set button text to Open/Close issue
        try {    
            rtftext.setText("");
            
            byte[] descriptionbytesout;
            if (issue.getDescription() == null) {
                descriptionbytesout = new byte[0];
            } else {
                descriptionbytesout = issue.getDescription();
            }
            
            InputStream rtfstream = new ByteArrayInputStream(descriptionbytesout);
            String convertedstrings = convertStreamToString(rtfstream);
            String rtfsign = "{\\rtf1\\ansi";
            boolean rtfornot = convertedstrings.contains(rtfsign);
            
            if (rtfornot) {
                byte[] descriptionbytesout2 = issue.getDescription();
                InputStream rtfstream2 = new ByteArrayInputStream(descriptionbytesout2);
                
                rtftext.getEditorKit().read(rtfstream2, rtftext.getDocument(), 0);
                 
            } else {
                String displaystringinrtf = "{\\rtf1\\ansi\n" +
                        "{\\fonttbl\\f0\\fnil Monospaced;\\f1\\fnil sansserif;}\n" +
                        "{\\colortbl\\red0\\green0\\blue0;\\red0\\green0\\blue0;}\n" +
                        "\n" +
                        "\\li0\\ri0\\fi0\\f1\\fs24\\i0\\b0\\cf1 " + convertedstrings + "\\par\n" +
                        "}";
                byte[] displaystringinrtfbyte = displaystringinrtf.getBytes(Charset.forName("UTF-8"));
                InputStream displaystringinrtfintput = new ByteArrayInputStream(displaystringinrtfbyte);
                
                rtftext.getEditorKit().read(displaystringinrtfintput, rtftext.getDocument(), 0);       
            }
        
            
        } catch (IOException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public String convertStreamToString(InputStream is) throws IOException {
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
    
    /**
     * This method compares the values of the issue with the component values
     * and returns true or false.
     * @return boolean true if there is a change in any of the component values
     */
    private boolean hasChange(){
        
        ByteArrayOutputStream str = new ByteArrayOutputStream();                   
        try {
            rtftext.getEditorKit().write(str, rtftext.getDocument(), 0, rtftext.getDocument().getLength());
        } catch (IOException | BadLocationException ex) {
            Logger.getLogger(IssueWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte[] bkey = str.toByteArray();
        String rtfstring = new String(bkey);
        
        String rtfstringindb;
        
        if (issue.getDescription() == null) {
            rtfstringindb = "";
        } else {
            byte[] bkey1 = issue.getDescription();
            rtfstringindb = new String(bkey1);
        }

        
        return ( appComboBox.getSelectedItem().equals(issue.getApp())
                &&titleText.getText().equals(issue.getTitle())
            && rtfstring.equals(rtfstringindb)
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
        if (dateClosedText.getText().isEmpty() && versionText.getText().isEmpty()) {
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
                checkForChangeAndSetBtnsEnabled();            
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
        
        return tab.loadingDropdownList();

    }
    
    
    //should update offline values
   
    private void updateComboList(String colName) {
        //create a combo box model
        DefaultComboBoxModel comboBoxSearchModel = new DefaultComboBoxModel();
        if (colName.equalsIgnoreCase("programmer")) {
            programmerComboBox.setModel(comboBoxSearchModel);
        } else if (colName.equalsIgnoreCase("rk")) {
            rkComboBox.setModel(comboBoxSearchModel);
        } else if (colName.equalsIgnoreCase("app")) {
            appComboBox.setModel(comboBoxSearchModel);
        }
        
        //offline values hard coded in
        //[, 1, 2, 3, 4, 5]
        //[, SKYPE, Analyster, Other, ELLEGUI, VBA, TOOLS, DOVISLEX, SQL, PM]
        //[, Shenrui, Corinne, Azoacha, Youzhi, Ying, Bektur, Xiao, Anthea, Carlos, Professor, Yi, Wei, Yiqi]

        Map comboBoxForSearchValue = loadingDropdownList();
       
        for (int col = 0; col < table.getColumnCount(); col++) {
            
            if (table.getColumnName(col).equalsIgnoreCase(colName)) {
                ArrayList<Object> dropDownList = (ArrayList<Object>) comboBoxForSearchValue.get(col);
                
                if (colName.equalsIgnoreCase("rk")) {
                    if (dropDownList.get(0) == "") {
                        ArrayList<Object> list = new ArrayList<Object>();

                        for (int i = 1; i < dropDownList.size(); i++) {
                            list.add(dropDownList.get(i));
                        }
                        list.add(dropDownList.get(0));

                        dropDownList = list;
                    }
                   
                }   
                    
                if (colName.equalsIgnoreCase("programmer") || colName.equalsIgnoreCase("app") ) {
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
                for (Object item : dropDownList) {
 
                    comboBoxSearchModel.addElement(item);

                }  
            }
            
        }
        
        
    }
    
    
    private void setComboBoxValue() {
        int row = table.getSelectedRow();
        String programmer = "";
        String rk = "";
        String app = "";
        if (table.getModel().getValueAt(row, 1) != null) {
            app = table.getModel().getValueAt(row, 1).toString();
        }
        if (table.getModel().getValueAt(row, 4) != null) {
            programmer = table.getModel().getValueAt(row, 4).toString();
        }
        if (table.getModel().getValueAt(row, 6) != null) {
            rk = table.getModel().getValueAt(row, 6).toString();
        }
        programmerComboBox.setSelectedItem(programmer);
        rkComboBox.setSelectedItem(rk);
        appComboBox.setSelectedItem(app);

    }
    

/*
    ** getters and setters
    
*/

    public ProjectManagerWindow getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManagerWindow projectManager) {
        this.projectManager = projectManager;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    
    public boolean isAddIssueMode() {
        return addIssueMode;
    }

    public void setAddIssueMode(boolean addIssueMode) {
        this.addIssueMode = addIssueMode;
    }

    public ShortCutSetting getShortCutSetting() {
        return ShortCutSetting;
    }

    public void setShortCutSetting(ShortCutSetting ShortCutSetting) {
        this.ShortCutSetting = ShortCutSetting;
    }

   
    public Map<Integer, Tab> getTabs() {
        return tabs;
    }

    public void setTabs(Map<Integer, Tab> tabs) {
        this.tabs = tabs;
    }

    public JButton getBtnNext() {
        return BtnNext;
    }

    public void setBtnNext(JButton BtnNext) {
        this.BtnNext = BtnNext;
    }

    public JButton getBtnPrevious() {
        return BtnPrevious;
    }

    public void setBtnPrevious(JButton BtnPrevious) {
        this.BtnPrevious = BtnPrevious;
    }

    public JLabel getApp() {
        return app;
    }

    public void setApp(JLabel app) {
        this.app = app;
    }

    public JComboBox getAppComboBox() {
        return appComboBox;
    }

    public void setAppComboBox(JComboBox appComboBox) {
        this.appComboBox = appComboBox;
    }

    public JButton getBtnCloseIssue() {
        return btnCloseIssue;
    }

    public void setBtnCloseIssue(JButton btnCloseIssue) {
        this.btnCloseIssue = btnCloseIssue;
    }

    public JButton getButtonCancel() {
        return buttonCancel;
    }

    public void setButtonCancel(JButton buttonCancel) {
        this.buttonCancel = buttonCancel;
    }

    public JButton getButtonConfirm() {
        return buttonConfirm;
    }

    public void setButtonConfirm(JButton buttonConfirm) {
        this.buttonConfirm = buttonConfirm;
    }

    public JButton getButtonSubmit() {
        return buttonSubmit;
    }

    public void setButtonSubmit(JButton buttonSubmit) {
        this.buttonSubmit = buttonSubmit;
    }

    public JComboBox<String> getComboBoxIssueType() {
        return comboBoxIssueType;
    }

    public void setComboBoxIssueType(JComboBox<String> comboBoxIssueType) {
        this.comboBoxIssueType = comboBoxIssueType;
    }

    public JLabel getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(JLabel dateClosed) {
        this.dateClosed = dateClosed;
    }

    public JTextField getDateClosedText() {
        return dateClosedText;
    }

    public void setDateClosedText(JTextField dateClosedText) {
        this.dateClosedText = dateClosedText;
    }

    public JLabel getDateOpened() {
        return dateOpened;
    }

    public void setDateOpened(JLabel dateOpened) {
        this.dateOpened = dateOpened;
    }

    public JTextField getDateOpenedText() {
        return dateOpenedText;
    }

    public void setDateOpenedText(JTextField dateOpenedText) {
        this.dateOpenedText = dateOpenedText;
    }

    public JLabel getDescription() {
        return description;
    }

    public void setDescription(JLabel description) {
        this.description = description;
    }

    public JTextPane getDescriptionText() {
        return rtftext;
    }

    public void setDescriptionText(JTextPane descriptionText) {
        this.rtftext = rtftext;
    }

    public JPanel getFormPane() {
        return formPane;
    }

    public void setFormPane(JPanel formPane) {
        this.formPane = formPane;
    }

    public JLabel getId() {
        return id;
    }

    public void setId(JLabel id) {
        this.id = id;
    }

    public JLabel getIdText() {
        return idText;
    }

    public void setIdText(JLabel idText) {
        this.idText = idText;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public void setjPanel1(JPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public void setjScrollPane1(JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    public JLabel getLock() {
        return lock;
    }

    public void setLock(JLabel lock) {
        this.lock = lock;
    }

    public JCheckBox getLockCheckBox() {
        return lockCheckBox;
    }

    public void setLockCheckBox(JCheckBox lockCheckBox) {
        this.lockCheckBox = lockCheckBox;
    }

    public JLabel getProgrammer() {
        return programmer;
    }

    public void setProgrammer(JLabel programmer) {
        this.programmer = programmer;
    }

    public JComboBox getProgrammerComboBox() {
        return programmerComboBox;
    }

    public void setProgrammerComboBox(JComboBox programmerComboBox) {
        this.programmerComboBox = programmerComboBox;
    }

    public JLabel getRk() {
        return rk;
    }

    public void setRk(JLabel rk) {
        this.rk = rk;
    }

    public JComboBox getRkComboBox() {
        return rkComboBox;
    }

    public void setRkComboBox(JComboBox rkComboBox) {
        this.rkComboBox = rkComboBox;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public JLabel getSubmitter() {
        return submitter;
    }

    public void setSubmitter(JLabel submitter) {
        this.submitter = submitter;
    }

    public JTextField getSubmitterText() {
        return submitterText;
    }

    public void setSubmitterText(JTextField submitterText) {
        this.submitterText = submitterText;
    }

    public void setTitle(JLabel title) {
        this.title = title;
    }

    public JTextField getTitleText() {
        return titleText;
    }

    public void setTitleText(JTextField titleText) {
        this.titleText = titleText;
    }

    public JLabel getVersion() {
        return version;
    }

    public void setVersion(JLabel version) {
        this.version = version;
    }

    public JTextField getVersionText() {
        return versionText;
    }
    
        //Wei 2016-06-23 codes to fix long-word warpping problem 
    public class WrapEditorKit extends RTFEditorKit {

        ViewFactory defaultFactory;

        public WrapEditorKit() {
            this.defaultFactory = new WrapColumnFactory();
        }
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }

    }

    public class WrapColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }
    }

    public class WrapLabelView extends LabelView {
        public WrapLabelView(Element elem) {
            super(elem);
        }

        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton B_Bold;
    private javax.swing.JButton BtnNext;
    private javax.swing.JButton BtnPrevious;
    private javax.swing.JButton Fsize;
    private javax.swing.JButton Hyperlink;
    private javax.swing.JButton Italic;
    private javax.swing.JButton Plain;
    private javax.swing.JButton StrikethroughBotton;
    private javax.swing.JButton UnderlineBotton;
    private javax.swing.JLabel app;
    private javax.swing.JComboBox appComboBox;
    private javax.swing.JButton btnCloseIssue;
    private javax.swing.JButton buttonCancel;
    private javax.swing.JButton buttonConfirm;
    private javax.swing.JButton buttonSubmit;
    private javax.swing.JButton colorButton1;
    private javax.swing.JComboBox<String> comboBoxIssueType;
    private javax.swing.JLabel dateClosed;
    private javax.swing.JTextField dateClosedText;
    private javax.swing.JLabel dateOpened;
    private javax.swing.JTextField dateOpenedText;
    private javax.swing.JLabel description;
    private javax.swing.JPanel formPane;
    private javax.swing.JLabel id;
    private javax.swing.JLabel idText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lastmodTime;
    private javax.swing.JLabel lock;
    private javax.swing.JCheckBox lockCheckBox;
    private javax.swing.JLabel programmer;
    private javax.swing.JComboBox programmerComboBox;
    private javax.swing.JLabel rk;
    private javax.swing.JComboBox rkComboBox;
    private javax.swing.JTextPane rtftext;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JLabel submitter;
    private javax.swing.JTextField submitterText;
    private javax.swing.JLabel title;
    private javax.swing.JTextField titleText;
    private javax.swing.JLabel version;
    private javax.swing.JTextField versionText;
    // End of variables declaration//GEN-END:variables

}
