package com.elle.ProjectManager.presentation;

import com.elle.ProjectManager.database.DBConnection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Carlos
 */
public class CompIssuesListWindowTest {
    
    public CompIssuesListWindowTest() {
    }

    @Test
    public void testSomeMethod() {
        DBConnection.connect("pupone", "pupone_dummy", "pupone_Carlos", "CarlosCCCC");
        CompIssuesListWindow frame = new CompIssuesListWindow();
        frame.setVisible(true);
        assertTrue(true);
    }
    
}
