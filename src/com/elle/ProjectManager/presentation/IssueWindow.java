
package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.admissions.Authorization;
import com.elle.ProjectManager.dao.IssueDAO;
import com.elle.ProjectManager.entities.Issue;
import com.elle.ProjectManager.logic.OfflineIssueManager;
import com.elle.ProjectManager.logic.ShortCutSetting;
import com.elle.ProjectManager.logic.Tab;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    private OfflineIssueManager mgr;

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

    public IssueDAO getDao() {
        return dao;
    }

    public void setDao(IssueDAO dao) {
        this.dao = dao;
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

    public String[] getDropdownlist() {
        return dropdownlist;
    }

    public void setDropdownlist(String[] dropdownlist) {
        this.dropdownlist = dropdownlist;
    }

    public Map<String, Tab> getTabs() {
        return tabs;
    }

    public void setTabs(Map<String, Tab> tabs) {
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

    public JTextArea getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(JTextArea descriptionText) {
        this.descriptionText = descriptionText;
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



    public JScrollPane getjScrollPane7() {
        return jScrollPane7;
    }

    public void setjScrollPane7(JScrollPane jScrollPane7) {
        this.jScrollPane7 = jScrollPane7;
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

    public void setVersionText(JTextField versionText) {
        this.versionText = versionText;
    }
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
        mgr = projectManager.offlineIssueMgr;

        // new issue initialization, including set app, dateopened and 
        if (this.row == -1) {
            addIssueMode = true;
            issue.setId(-1);
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
        submitterText.setText(projectManager.getUserName());
        
        //setComponentValuesFromIssue();
        
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
        dateOpened = new javax.swing.JLabel();
        lockCheckBox = new javax.swing.JCheckBox();
        lock = new javax.swing.JLabel();
        idText = new javax.swing.JLabel();
        rkComboBox = new javax.swing.JComboBox();
        submitterText = new javax.swing.JTextField();
        programmerComboBox = new javax.swing.JComboBox();
        dateOpenedText = new javax.swing.JTextField();
        submitter = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        comboBoxIssueType = new javax.swing.JComboBox<>();
        programmer = new javax.swing.JLabel();
        rk = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        titleText = new javax.swing.JTextField();
        title = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        BtnNext = new javax.swing.JButton();
        BtnPrevious = new javax.swing.JButton();
        description = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        descriptionText = new javax.swing.JTextArea();

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
        appComboBox.setPreferredSize(new java.awt.Dimension(80, 28));
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

        dateOpened.setText(" dateOpened");
        dateOpened.setPreferredSize(new java.awt.Dimension(79, 12));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(dateOpened, gridBagConstraints);

        lockCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel3.add(lockCheckBox, gridBagConstraints);

        lock.setText(" lock");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(lock, gridBagConstraints);

        idText.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel3.add(idText, gridBagConstraints);

        rkComboBox.setEditable(true);
        rkComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        rkComboBox.setPreferredSize(new java.awt.Dimension(40, 28));
        rkComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rkComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(rkComboBox, gridBagConstraints);

        submitterText.setMinimumSize(new java.awt.Dimension(90, 20));
        submitterText.setPreferredSize(new java.awt.Dimension(90, 28));
        submitterText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitterTextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(submitterText, gridBagConstraints);

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
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(programmerComboBox, gridBagConstraints);

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
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(dateOpenedText, gridBagConstraints);

        submitter.setText(" submitter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(submitter, gridBagConstraints);

        id.setText(" id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel3.add(id, gridBagConstraints);

        comboBoxIssueType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FEATURE", "BUG", "REFERENCE" }));
        comboBoxIssueType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxIssueTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(comboBoxIssueType, gridBagConstraints);

        programmer.setText(" programmer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(programmer, gridBagConstraints);

        rk.setText(" rk");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel3.add(rk, gridBagConstraints);

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
        formPane.add(jPanel4, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        BtnNext.setText(">");
        BtnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnNextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel5.add(BtnNext, gridBagConstraints);

        BtnPrevious.setText("<");
        BtnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPreviousActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(BtnPrevious, gridBagConstraints);

        description.setText(" description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel5.add(description, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jScrollPane7, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 10, 20);
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
    
    // get current timestamp for datetimeLastMod
//    private String currentTimeStamp() {
//        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
//    }
    

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
            row--;
            setIssueValuesFromTable(row,table);
            setComponentValuesFromIssue();
            table.setRowSelectionInterval(row, row);
            updateCustomIdList(row);
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
            row++;
            setIssueValuesFromTable(row,table);
            setComponentValuesFromIssue();
            table.setRowSelectionInterval(row, row);
            updateCustomIdList(row);
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
        int originalId = issue.getId();
        
        
        if (projectManager.isOnline()) {
            //if it is offline updated issue, reset id by -9000
            if (originalId > 9000) issue.setId(originalId - 9000);
            boolean onlineUpdateSuccess =  issue.getId() > 0 ? dao.update(issue) :  dao.insert(issue);
            if(onlineUpdateSuccess){
                if (originalId < 0) {//for new issues
                    projectManager.insertTableRow(table, issue);
                 //remove original row
                    projectManager.removeTableRow(table, originalId);
                    
                    mgr.removeIssue(mgr.getIssue(originalId));
                    
                }
                else {
                    // for updating issues, it could be >9000 or <9000, >9000 update requires extra removing the 9xxx table row
                    
                    if (originalId > 9000) {
                        projectManager.removeTableRow(table, originalId);
                        mgr.removeIssue(mgr.getIssue(originalId));
                    
                    }
                    projectManager.updateTableRow(table,issue);
                    
                }
              
                //update offlineIssueMgr
                projectManager.makeTableEditable(false);
                
            }
            else { //offline actions
                
                JOptionPane.showMessageDialog(this,
                    "Fail to connect to server.\n Data will be saved saved locally.",
                    "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            
                //save the issue to local computer
                if (mgr.updateIssue(issue)){
                    JOptionPane.showMessageDialog(this,
                     "Data saved successfully.");
                    
                    //if new offline issue or update offline issue, then update
                    if (originalId < 0 || originalId > 9000) projectManager.updateTableRow(table, issue);
                    
                    //if update some online issue
                    else
                        projectManager.insertTableRow(table,issue);
                    projectManager.makeTableEditable(false);
               
                }
                else {
                    JOptionPane.showMessageDialog(this,
                        "Fail to save issue locally.",
                        "I/O Error Message",
                        JOptionPane.ERROR_MESSAGE);
                
                }
           
            }
            
            
        }
        
        else {
            
            JOptionPane.showMessageDialog(this,"Offline Mode : data is saved locally.");
            
            if (mgr.updateIssue(issue)){
                
                    JOptionPane.showMessageDialog(this,
                     "Data saved successfully.");
                    //if already in table, update, else insert
                    int row = projectManager.findTableModelRow(table,issue);
                    if (row != -1)
                        projectManager.updateTableRow(table,issue);
                    else
                        projectManager.insertTableRow(table,issue);
                    
                        
                    projectManager.makeTableEditable(false);
               
                }
                else {
                    JOptionPane.showMessageDialog(this,
                        "Fail to save issue locally.",
                        "I/O Error Message",
                        JOptionPane.ERROR_MESSAGE);
                
                }      
        }
        
        issue.setId(originalId);
        issueWindowClosing();
    }//GEN-LAST:event_buttonConfirmActionPerformed

    
    
    
    /**
     * This method is called when the submit button is pressed.
     * @param evt 
     */
    private void buttonSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSubmitActionPerformed
        
       
        setIssueValuesFromComponents();
        int originalId = issue.getId();
        
        if (projectManager.isOnline() && dao.insert(issue)) {
            
            projectManager.insertTableRow(table,issue);
            projectManager.makeTableEditable(false); 
        
        }
        
        else {
            String msg = "Fail to connect to server.\nData will be saved saved locally.";
            if (!projectManager.isOnline()) msg = "Offline Mode : data is saved locally.";
            
            JOptionPane.showMessageDialog(this,msg);
            
            //save the issue to local computer
            if (mgr.addIssue(issue)) {
                JOptionPane.showMessageDialog(this,
                    "Data saved successfully.");
                projectManager.insertTableRow(table,issue);
                projectManager.makeTableEditable(false);  
            }
            else {
                JOptionPane.showMessageDialog(this,
                    "Fail to save issue locally.",
                    "I/O Error Message",
                    JOptionPane.ERROR_MESSAGE);
                
            }
            
            
            
            
        }
        
//        if(dao.insert(issue)){
//            projectManager.inserTableRow(table,issue);
//            projectManager.makeTableEditable(false);
//            
//        }
//        else {//offline actions
//            
//            JOptionPane.showMessageDialog(this,
//                    "Fail to connect to server.\nData will be saved saved locally.",
//                    "Error Message",
//                    JOptionPane.ERROR_MESSAGE);
//            
//            
//            //save the issue to local computer
//            if (mgr.addIssue(issue)) {
//                JOptionPane.showMessageDialog(this,
//                    "Data saved successfully.");
//                projectManager.inserTableRow(table,issue);
//                projectManager.makeTableEditable(false);
//                
//            }
//            else {
//                JOptionPane.showMessageDialog(this,
//                    "Fail to save issue locally.",
//                    "I/O Error Message",
//                    JOptionPane.ERROR_MESSAGE);
//                
//            }
//        }
        
        issue.setId(originalId);
        issueWindowClosing();
    }//GEN-LAST:event_buttonSubmitActionPerformed
    
    
    
    private void buttonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelActionPerformed
        //        System.out.println(selectedTable.getValueAt(0, 0));

        //        projectManager.getOpeningIssuesList().remove(issue.getID(), this);
        issueWindowClosing();
    }//GEN-LAST:event_buttonCancelActionPerformed

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

    private void dateOpenedTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateOpenedTextKeyReleased
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_D) {
            this.FillItWithDate((JTextField) evt.getComponent());
        }
    }//GEN-LAST:event_dateOpenedTextKeyReleased

    private void dateOpenedTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateOpenedTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateOpenedTextActionPerformed


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
        issue.setLastmodtime(getTableValueAt(row, 12).toString());
        
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
        lastmodTime.setText(issue.getLastmodtime());
        
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
    
    
    //should update offline values
   
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
        
        //offline values hard coded in
        //[, 1, 2, 3, 4, 5]
        //[, SKYPE, Analyster, Other, ELLEGUI, VBA, TOOLS, DOVISLEX, SQL, PM]
        //[, Shenrui, Corinne, Azoacha, Youzhi, Ying, Bektur, Xiao, Anthea, Carlos, Professor, Yi, Wei, Yiqi]


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
                    if (projectManager.isOnline() && dropDownList.get(0) == "") {
                        ArrayList<Object> list = new ArrayList<Object>();

                        for (int i = 1; i < dropDownList.size(); i++) {
                            list.add(dropDownList.get(i));
                        }
                        list.add(dropDownList.get(0));

                        dropDownList = list;
                    }
                    else {
                      
                       Object[] defaultArray = {"1","2","3","4","5",""};
                       dropDownList.addAll(Arrays.asList(defaultArray));
                       
                        
                    }
                    
                    
                } else if (colName.equalsIgnoreCase("programmer") || colName.equalsIgnoreCase("app") ) {
                    Object nullValue = "";
                    
                    if (!projectManager.isOnline() && colName.equalsIgnoreCase("programmer") ) {
                        
                        Object[] defaultArray = {"", "Shenrui", "Corinne", "Azoacha", "Youzhi", "Ying", "Bektur", "Xiao", "Anthea", "Carlos", "Professor", "Yi", "Wei", "Yiqi"};
                        dropDownList = new ArrayList<Object>();
                        dropDownList.addAll(Arrays.asList(defaultArray));
                        
                    }
                    
                    if (!projectManager.isOnline() && colName.equalsIgnoreCase("app") ) {
                        Object[] defaultArray = {"", "SKYPE", "Analyster", "Other", "ELLEGUI", "VBA", "TOOLS", "DOVISLEX", "SQL", "PM"};   
                        dropDownList = new ArrayList<Object>();
                        dropDownList.addAll(Arrays.asList(defaultArray));
                   
                    }
                    
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lastmodTime;
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
