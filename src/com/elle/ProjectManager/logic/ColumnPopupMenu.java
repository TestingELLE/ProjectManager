package com.elle.ProjectManager.logic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 * ColumnPopupMenu This class is the JPopupMenu used for the column filtering
 * with the CheckBoxlist.
 *
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class ColumnPopupMenu extends JPopupMenu {

    // attributes
    private Tab tab;
    private TableFilter filter;
    private JTable table;
    private CheckBoxList checkBoxList;
    private Map<Integer, ArrayList<CheckBoxItem>> checkBoxItems; // distinct items for options
    private int columnIndex; // selected colunm

   
    
    /**
     * CONSTRUCTOR ColumnPopupMenu creates a ColumnPopupMenu
     */
    public ColumnPopupMenu(Tab tab) {
        this.tab = tab;
        this.filter = tab.getFilter();
        this.table = tab.getTable();
        
        initComponents();
       
        // load all check box items
        loadAllCheckBoxItems();


    }

    /**
     * initComponents initialize the components of the ColumnPopupMenu
     */
    private void initComponents() {

        // create a new JPanel
        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panel.setPreferredSize(new Dimension(250, 300)); // default popup size

        // create the checkbox JList 
        checkBoxList = new CheckBoxList(); // JList

        // add mouseListener to the list
        checkBoxList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                // get the checkbox item index
                int index = checkBoxList.locationToIndex(e.getPoint());

                // index cannot be null
                if (index != -1) {

                    // get the check box item at this index
                    JCheckBox checkbox = (JCheckBox) checkBoxList.getModel().getElementAt(index);

                    // check if the (All) selection was checked
                    if (checkbox.getText().equals("(All)")) {
                        if (checkbox.isSelected()) {
                            removeAllChecks(getColumnIndex());
                        } else {
                            checkAll(getColumnIndex());
                        }
                    } else {
                        // toogle the check for the checkbox item
                        checkbox.setSelected(!checkbox.isSelected());
                    }
                    repaint(); // redraw graphics
                }
            }
        });

        // add the check box list to the panel
        panel.add(new JScrollPane(checkBoxList), BorderLayout.CENTER); // add list to center

        // create a new Box for the buttons
        Box boxButtons = new Box(BoxLayout.LINE_AXIS);

        // add horizontal glue to the box
        boxButtons.add(Box.createHorizontalGlue());

        // create Apply button
        JButton btnApply = new JButton("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyColumnFilter(getColumnIndex());
                setVisible(false);
            }
        });

        // create Cancel button
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
            }
        });

        // add buttons and look and feel to box of commands
        boxButtons.add(btnApply);
        boxButtons.add(Box.createHorizontalStrut(5));
        boxButtons.add(btnCancel);
        boxButtons.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        boxButtons.setBackground(UIManager.getColor("Panel.background"));
        boxButtons.setOpaque(true);

        // add the box of buttons to the panel
        panel.add(boxButtons, BorderLayout.SOUTH); // add command buttons to south of panel

        // add the panel to the ColumnPopupMenu
        add(panel);
    }

    /**
     * showPopupMenu This method determines what column was clicked and displays
     * the popup under it according to the location of the column.
     *
     * @param e
     */
    public void showPopupMenu(MouseEvent e) {

        //each time, load all check box items to update
        loadAllCheckBoxItems();
        // Determine the header and column model that was clicked
        JTableHeader header = (JTableHeader) (e.getSource());
        TableColumnModel colModel = header.getTable().getColumnModel();

        // The index of the column whose header was clicked
        int vColumnIndex = colModel.getColumnIndexAtX(e.getX());
        System.out.println("column index: " + vColumnIndex);
        if (vColumnIndex < 0) {
            return;
        }

        // Determine if mouse was clicked between column heads
        Rectangle headerRect = header.getTable().getTableHeader().getHeaderRect(vColumnIndex);
        System.out.println(header.getTable().getTableHeader().getHeaderRect(vColumnIndex));
        if (vColumnIndex == 0) {
            headerRect.width -= 2;
        } else {
            headerRect.grow(-2, 0);
        }

        // Mouse was clicked between column heads
        if (!headerRect.contains(e.getX(), e.getY())) {
            return;
        }

        // we do not include the pop up for the primary key column (ID)
        // it is not used to filter
        if (vColumnIndex > 0) {
            setColumnIndex(vColumnIndex);
            loadList(vColumnIndex);

            // show pop-up
            this.show(header, headerRect.x, header.getHeight());
        }
    }

    /**
     * loadList
     *
     * @param col
     */
    public void loadList(int col) {

        // apply checks to filtered items
        applyChecksToFilteredItems(col);

        // load JList with checkbox items
        checkBoxList.setListData(checkBoxItems.get(col).toArray());
    }

    /**
     * applyChecksToFilteredItems
     */
    public void applyChecksToFilteredItems(int col) {

        // get filtered items
        ArrayList<Object> fItems = filter.getFilterItems().get(col);
        ArrayList<CheckBoxItem> cbItems = checkBoxItems.get(col);
        CustomIDList idList = filter.getCustomIdListFilter();
        ArrayList<Object> idItems = new ArrayList<Object>(idList.size());

        for (int row = 0; row < table.getRowCount(); row++) {
            if (idList.has(((Integer) table.getValueAt(row, 0))) != -1) {
                idItems.add(table.getValueAt(row, col));
                
            }
        }

        // reset all checks to false
        removeAllChecks(col);

        // apply checks to filtered items
        for (CheckBoxItem cbItem : cbItems) {
            for (Object fItem : fItems) {

                if (cbItem.getDistinctItems().contains(fItem.toString())) {
                    cbItem.setSelected(true);
                }
            }
            for (Object idItem : idItems) {
                if (cbItem.getDistinctItems().contains(idItem.toString())) {
                    cbItem.setSelected(true);
                }
            }
        }

    }

    /**
     * loadAllCheckBoxItems This is used to load all column data.
     */
    public void loadAllCheckBoxItems() {

        // this is just items to search for
        // we decided to cap long values - notes for example
        int cap = 20;              // cap the String length of list options
        Object cellValue = null;   // cell value
        String cappedValue = "";   // capped string value
        int col = 0;               // column index
        int row = 0;               // row index

        // initialize checkBoxItems
        checkBoxItems = new HashMap<>();

        // check every column except first because it is the primary key and not used to filter
        for (col = 1; col < table.getColumnCount(); col++) {

            // load capped items and add checkbox items to the list
            loadCappedItems(col, cap);

            // now go through every row and add each disctinct item and count
            for (row = 0; row < table.getModel().getRowCount(); row++) {

                cellValue = table.getModel().getValueAt(row, col);
                
                

                // handle null exceptions
                if (cellValue == null) {
                    cellValue = "";
                }
                
                

                // cap the String length of list options
                if (cellValue.toString().length() > cap) {
                    cappedValue = cellValue.toString().substring(0, cap);
                } else {
                    cappedValue = cellValue.toString();
                }

                // check every checkboxItem
                for (CheckBoxItem item : checkBoxItems.get(col)) {

                    // find the checkbox item
                    if (item.getCapped().equals(cappedValue)) {
                        if (!item.getDistinctItems().contains(cellValue.toString())) {
                            item.getDistinctItems().add(cellValue.toString());
                            item.incrementCount();
                            break;
                        } else {
                            item.incrementCount();
                            break;
                        }
                    }
                }
            }

            // now change the text for each checkbox to include the counts
            for (CheckBoxItem item : checkBoxItems.get(col)) {

                // for checkbox item (All) add to distinctinct item list for filtering
                if (item.getText().equals("(All)")) {
                    item.getDistinctItems().add("(All)");
                } else if (!item.getText().equals("(All)")) {
                    String capped = item.getCapped();      // the capped value
                    int count = item.getCount();           // the count of values
                    item.setText(capped + " (" + count + ")");
                }
            }
        }
    }

    /**
     * loadCappedItems This caps the lengths of the selections and loads them up
     * on the arrayList for that column.
     *
     * @param col // column index
     * @param cap // cap length for strings
     */
    public void loadCappedItems(int col, int cap) {

        // String value of object
        String value = "";

        // get disctinct items
        ArrayList<Object> filterItems = new ArrayList<>(getDistinctItems(col));

        // create an array list for the capped values
        ArrayList<String> cappedItems = new ArrayList<>();

        // get all the capped items
        for (Object fItem : filterItems) {

            // cap the String length of list options
            if (fItem.toString().length() > cap) {
                value = fItem.toString().substring(0, cap);
            } else {
                value = fItem.toString();
            }

            // store all capped values in an array
            if (cappedItems.isEmpty()) {
                cappedItems.add(value);
            } else if (!cappedItems.contains(value)) {
                cappedItems.add(value);
            }
        }

        // new checkbox item ArrayList
        checkBoxItems.put(col, new ArrayList<>());

        // add the (All) selection
        CheckBoxItem checkAll = new CheckBoxItem("(All)");
        checkAll.getDistinctItems().add("(All)");
        checkBoxItems.get(col).add(checkAll);

        // fill the array with checkbox items
        for (String cappedItem : cappedItems) {
            checkBoxItems.get(col).add(new CheckBoxItem(cappedItem));
        }
    }

    /**
     * getDistinctItems This returns an array of all the distinctItems in a
     * column
     *
     * @param col
     * @return
     */
    public ArrayList<String> getDistinctItems(int col) {

        ArrayList<String> distinctItems = new ArrayList<>();
        Object cellValue = null;

        for (int row = 0; row < table.getModel().getRowCount(); row++) {

            cellValue = table.getModel().getValueAt(row, col);

            if (cellValue == null) {
                cellValue = "";
            }

            if (!distinctItems.contains(cellValue.toString())) {
                distinctItems.add(cellValue.toString());
            }
        }
        return distinctItems;
       
    }

    /**
     * getCheckBoxList
     *
     * @return
     */
    public CheckBoxList getCheckBoxList() {
        return checkBoxList;
    }

    /**
     * setCheckBoxList
     *
     * @param checkBoxList
     */
    public void setCheckBoxList(CheckBoxList checkBoxList) {
        this.checkBoxList = checkBoxList;
    }

    /**
     * getFilter
     *
     * @return
     */
//    public TableFilter getFilter() {
//        return filter;
//    }

    /**
     * setFilter
     *
     * @param filter
     */
    public void setFilter(TableFilter filter) {
        this.filter = filter;
    }

    /**
     * getCheckBoxItems
     *
     * @return
     */
    public Map<Integer, ArrayList<CheckBoxItem>> getCheckBoxItems() {
        return checkBoxItems;
    }

    /**
     * setCheckBoxItems
     *
     * @param checkBoxItems
     */
    public void setCheckBoxItems(Map<Integer, ArrayList<CheckBoxItem>> checkBoxItems) {
        this.checkBoxItems = checkBoxItems;
    }

    /**
     * getColumnIndex
     *
     * @return
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * setColumnIndex
     *
     * @param columnIndex
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /**
     * applyColumnFilter
     *
     * @param columnIndex
     */
    public void applyColumnFilter(int columnIndex) {

        filter.removeFilterItems(columnIndex);
        ArrayList<CheckBoxItem> dItems = checkBoxItems.get(columnIndex);
        ArrayList<Object> filterItems = new ArrayList<>();
        for (CheckBoxItem item : dItems) {
            if (item.isSelected()) {
                for (String dItem : item.getDistinctItems()) {
                    filterItems.add(dItem);
                }
            }
        }
        filter.addFilterItems(columnIndex, filterItems);
        filter.applyFilter();

    }

    /**
     * removeAllChecks
     *
     * @param columnIndex
     */
    public void removeAllChecks(int columnIndex) {
        ArrayList<CheckBoxItem> dItems = checkBoxItems.get(columnIndex);
        for (CheckBoxItem item : dItems) {
            item.setSelected(false);
        }
    }

    /**
     * checkAll
     *
     * @param columnIndex
     */
    public void checkAll(int columnIndex) {
        ArrayList<CheckBoxItem> dItems = checkBoxItems.get(columnIndex);
        for (CheckBoxItem item : dItems) {
            item.setSelected(true);
        }
    }
}
