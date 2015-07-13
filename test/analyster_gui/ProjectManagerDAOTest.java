/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
// */
//package analyster_gui;
//
//import projectmanager_gui.AddRecordsTable;
//import projectmanager_gui.ProjectManagerDAO;
//import java.util.ArrayList;
//import java.util.Vector;
//import javax.swing.JTable;
//import static org.hamcrest.CoreMatchers.is;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// *
// * @author danielabecker
// */
//public class ProjectManagerDAOTest {
//    
//    public ProjectManagerDAOTest() {
//    }
//   
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//
//    /**
//     * Test of checkingDate method, of class ProjectManagerDAO.
//     */
//    @Test
//    public void shouldReturnWellFormatedQuery() {
//        System.out.println("checkingDate");
//        JTable table = mock(JTable.class);
//        long taskid = 0L;
//        String rowData = "";
//        int j = 0;
//        int colNum = 0;
//        Vector columnNames = null;
//        AddRecordsTable info = mock(AddRecordsTable.class);
//        ArrayList<String> rows = new ArrayList<>();
//        ProjectManagerDAO dao = new ProjectManagerDAO();
//        int expResult = 0;
//        
//        when(table.getRowCount()).thenReturn(1);
//        when(table.getValueAt(0, 0)).thenReturn("null");
//
//        int rowNumber = dao.checkingDate(0, table, taskid, rowData, j, colNum, columnNames, info, rows);
//        
//        
//        assertThat(rowNumber,is(1));
//        assertThat(rows.get(0),is("('1','null')"));
//    }
//    
//}
