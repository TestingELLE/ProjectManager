package com.elle.ProjectManager.logic;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * TableFilter This class takes a JTable and filters the view for searching
 * data. It also applies colors to the column headers when filtering.
 *
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class TableFilter extends RowFilter<TableModel, Integer> {

    // attributes
    private Tab tab;
    private TableRowSorter<TableModel> sorter;             // the table sorter
    private Map<Integer, ArrayList<Object>> filterItems;    // distinct items to filter
    private CustomIDList customIdListFilter;
    private Color color;                                   // color to paint header
    private boolean isFiltering;

    /**
     * CONSTRUCTOR TableFilter
     *
     * @param table // the table to apply the filter
     */
    public TableFilter(Tab tab) {

        this.tab = tab;
        
        customIdListFilter = new CustomIDList();

        // initialize the color for the table header when it is filtering
        color = Color.GREEN; // default color is green

        isFiltering = false;
        
        
    }

    /**
     * initFilterItems Initializes the filter item arrays. This should be called
     * once at the startup of the application and after a table model has been
     * set to the table.
     */
    public void initFilterItems() {

        // initialize filterItems
        filterItems = new HashMap<>();
        for (int i = 0; i < tab.getTable().getColumnCount(); i++) {
            filterItems.put(i, new ArrayList<>());
        }
        
        
    }

    /**
     * addDistinctItem
     *
     * @param col
     * @param selectedField
     */
    public void addFilterItem(int col, Object selectedField) {

        // if not filtering then all filters are cleared (full table)
        // this is to not clear other filtered columns
        if (isFiltering == false) {
            removeAllFilterItems();                // this empties all column filters
            isFiltering = true;
        } else {
            filterItems.get(col).clear();              // remove all items from this column
        }

        if (selectedField == null) // check for null just in case
        {
            selectedField = "";                    // no reason not to
        }
        System.out.println("add " + selectedField + " to filter");
        filterItems.get(col).add(selectedField);   // add passed item

        addColorHeader(col);                       // highlight header
    }

    /**
     * addDistinctItems
     *
     * @param col
     * @param items
     */
    public void addFilterItems(int col, ArrayList<Object> items) {

        if (!items.isEmpty()) {
            // if not filtering then all filters are cleared (full table)
            // this is to not clear other filtered columns
            if (isFiltering == false) {
                removeAllFilterItems();                // this empties all column filters
                isFiltering = true;
            } else {
                filterItems.get(col).clear();              // remove all items from this column
            }

            for (Object item : items) {

                if (item == null) // check for null just in case
                {
                    item = "";                        // no reason not to
                }
                System.out.println("add " + item + " to filter");
                filterItems.get(col).add(item);       // add item to list
            }

            addColorHeader(col);                      // highlight header
        }
    }

 
    public void addCustomIdListToFilterItem(CustomIDList customIdList) {
        this.customIdListFilter = customIdList;
        if (!customIdList.isEmpty()) {
            addColorHeader(0);
        }
    }

    /**
     * removeDistinctItems
     *
     * @param col
     */
    public void removeFilterItems(int col) {

        filterItems.get(col).clear();
        removeColorHeader(col);
    }

    /**
     * removeAllDistinctItems
     *
     */
    public void removeAllFilterItems() {

        for (int i = 0; i < filterItems.size(); i++) {
            filterItems.get(i).clear();
        }
    }

    /**
     * applyFilter
     */
    public void applyFilter() {
        JTable table = tab.getTable();
        sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        sorter.setRowFilter(this);
        //each time , after filter applied, reset PM window labelRecords
        tab.setLabelRecords();
    }

    /**
     * applyFilter
     */
    public void applyFilter(EditableTableModel model) {
        JTable table = tab.getTable();
        sorter = new TableRowSorter<TableModel>(model);
        sorter.setRowFilter(this);
        table.setRowSorter(sorter);
        
        //each time , after filter applied, reset PM window labelRecords
        tab.setLabelRecords();
    }

    /**
     * addColorHeader
     *
     * @param columnIndex
     */
    public void addColorHeader(int columnIndex) {
        JTable table = tab.getTable();
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setBackground(color);
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(cellRenderer);
        table.getTableHeader().repaint();
    }

    /**
     * applyColorHeaders
     *
     */
    public void applyColorHeaders() {
        JTable table = tab.getTable();
        for (int i = 0; i < filterItems.size(); i++) {
            if (filterItems.get(i).isEmpty()) {
                removeColorHeader(i);
            } else {
                addColorHeader(i);
            }
        }
        table.getTableHeader().repaint();
    }

    /**
     * removeColorHeader
     *
     * @param columnIndex
     */
    public void removeColorHeader(int columnIndex) {
        JTable table = tab.getTable();
        table.getColumnModel().getColumn(columnIndex)
                .setHeaderRenderer(table.getTableHeader().getDefaultRenderer());
        table.getTableHeader().repaint();
    }

    /**
     * removeAllColorHeaders
     *
     */
    public void removeAllColorHeaders() {
        JTable table = tab.getTable();
        for (int i = 0; i < filterItems.size(); i++) {
            removeColorHeader(i);
        }
        table.getTableHeader().repaint();
    }

    /**
     * getColor
     *
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * setColor
     *
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * getTable
     *
     * @return
     */
    public Tab getTab() {
        return tab;
    }

    /**
     * setTable
     *
     * @param table
     */
    public void setTab(Tab tab) {
        this.tab = tab;
    }

    /**
     * getSorter
     *
     * @return
     */
    public TableRowSorter<TableModel> getSorter() {
        return sorter;
    }

    /**
     * setSorter
     *
     * @param sorter
     */
    public void setSorter(TableRowSorter<TableModel> sorter) {
        this.sorter = sorter;
    }

    public CustomIDList getCustomIdListFilter() {
        return customIdListFilter;
    }

    public void setCustomIdListFilter(CustomIDList customIdListFilter) {
        this.customIdListFilter = customIdListFilter;
    }
    
    
    

    /**
     * getFilterItems
     *
     * @return
     */
    public Map<Integer, ArrayList<Object>> getFilterItems() {
        return filterItems;
    }

    /**
     * setFilterItems
     *
     * @param distinctColumnItems
     */
    public void setFilterItems(Map<Integer, ArrayList<Object>> distinctColumnItems) {
        this.filterItems = distinctColumnItems;
    }

    /**
     * clearAllFilters
     * clears filters for the specified column
     * @param columnIndex
     */
    public void clearColFilter(int columnIndex) {
        removeFilterItems(columnIndex);    // remove the filters for the column
        removeColorHeader(columnIndex);    // remove the header highlight
    }

    /**
     * clearAllFilters Loads all rows, applys the filter, and removes the color
     * highlights
     */
    public void clearAllFilters() {

        removeAllFilterItems();      // load all rows
        removeAllColorHeaders();     // remove all header highlighted Colors
        isFiltering = false;         // no filters are applied 
    }

    /**
     * include
     *
     * @param entry
     * @return
     */
    @Override
    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {

        if (isFiltering) {
            TableModel model = entry.getModel();
            int row = entry.getIdentifier();
            int numColsfiltered = filterItems.size();     // number of cols filtered
            int itemsFound = 0;                           // items found must match the total columns filtered
            int emptyFilterCols = 0;                             // number of empty filter columns

            // check every column 
            for (int col = 0; col < model.getColumnCount(); col++) {

                if (filterItems.get(col).isEmpty()) {
                    numColsfiltered--;
                    emptyFilterCols++;
                    continue;                          // the column is empty
                }
                // get filter values
                ArrayList<Object> distinctItems = filterItems.get(col);

                // get value
                Object cellValue = model.getValueAt(row, col);

                // handle null exception
                if (cellValue == null) {
                    cellValue = "";
                }

                // if contains any of the filter items then include
                if (distinctItems.contains(cellValue.toString())) {
                    itemsFound++;
                } else {
                    // search for a match and ignore case
                    for (Object distinctItem : distinctItems) {
                       
                            if (cellValue.toString().toLowerCase().contains(distinctItem.toString().toLowerCase())) {
                                itemsFound++;
                            }
                         
                    }
                }
            }
            
            System.out.println("itemsFound = " + itemsFound);
            System.out.println("numColsfiltered = "+ numColsfiltered);
            if (emptyFilterCols == model.getColumnCount()) {
                isFiltering = false;
                return true;
            } else if (itemsFound == numColsfiltered) {
                
                return true;
            } else {
                
                // if customIDListFilter exists, and its size > 0 , do the filter
                if (this.customIdListFilter != null &&!this.customIdListFilter.isEmpty()) {
                    if (this.customIdListFilter.contains((Integer) model.getValueAt(row, 0))) {
                        return true;
                    }
                }
                
                return false;

            }
        }
        return true;  // not filtering
    }
}
