
package com.elle.ProjectManager.logic;

import java.util.ArrayList;
import javax.swing.JCheckBox;

/**
 * CLASS: CheckBoxItem
 * This class to store information about check box item objects.
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class CheckBoxItem extends JCheckBox{
    
    // attributes
    private ArrayList<String> distinctItems;      // original distinct value used by filter
    private String capped;        // capped value that is diplayed for checkbox item selection
    private int count;            // count of distinct items to display along side the check box selections
    private int id;               // this is used to store primary key of item in DB table

    public CheckBoxItem(String capped) {
        super(capped);
        distinctItems = new ArrayList<>();
        this.capped = capped;
        count = 0;
    }

    public ArrayList<String> getDistinctItems() {
        return distinctItems;
    }

    public void setDistinctItems(ArrayList<String> distinctItems) {
        this.distinctItems = distinctItems;
    }

    public String getCapped() {
        return capped;
    }

    public void setCapped(String capped) {
        this.capped = capped;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount(){
        count++;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
}
