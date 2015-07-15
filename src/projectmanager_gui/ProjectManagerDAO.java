/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectmanager_gui;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author danielabecker
 */
public class ProjectManagerDAO {

    public void insert(JTable table, Vector columnNames, AddRecordsTable info, String tableName, ProjectManager ana, LogWindow log) 
            throws SQLException {
        Vector data, row = new Vector();
        String title = " (";    // element of Sql statement
        ArrayList<String> rows = new ArrayList<String>();
        String rowData = "";
        boolean flag = false;// if add successfully, set it true
        String sqlChange = "";

        int i = 0, j = 0, num = 0;
        int colNum = table.getColumnCount();
        long taskid = info.getIdNum();
//        long id = ana.tableState.getRecordsNumber(ana.getTable(jTables.getSelectedItem().toString()));
        // complete title from columnNames
        if (tableName.equals("tasks")) {
            title += "taskID" + ",";
        } else if (tableName.equals("task_notes")) {
            title += "noteID" + ",";
        } else if (tableName.equals("task_files")) {
            title += "fileID" + ",";
        }
        // The extra column is the ID which will not display
        for (i = 0; i < colNum-1; i++) {
            title += columnNames.get(i).toString() + ",";
        }
        title += columnNames.get(colNum - 1).toString() + ") ";

        // rows comprise all the new information for inserting
        i = checkingDate(0, table, taskid, rowData, j, colNum, columnNames, info, rows);
        num = i;

        // insert the new rows one by one
        for (i = 0; i < num; i++) {
            try {
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                String date = format.format(new Date()).toString();

                sqlChange = "INSERT INTO " + tableName + title + "VALUES " + rows.get(i);
                //System.out.println(sqlChange);
//                sqlChange = "INSERT INTO Assignments (symbol,analyst,priority,dateAssigned,note) values ('w','w',null,null,null)";
//                JOptionPane.showMessageDialog(null, sqlChange);
                // execute insert SQL stetement
                System.out.println(sqlChange);
                GUI.stmt.executeUpdate(sqlChange);
                log.sendMessages(sqlChange);
                flag = true;
//                JOptionPane.showMessageDialog(null, "Add successfully!");
            } catch (SQLException ex) {
//    			logwind.sendMessages(ex.getMessage());
//              			logwind.sendMessages(ex.getSQLState() + "\n");
//            System.out.println("Error: " + ex);
                JOptionPane.showMessageDialog(null, "Upload failed! ");
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
                //logwind.sendMessages(ex.getMessage());
                JOptionPane.showMessageDialog(null, "Error!");
            }
        }

        if (flag) {
            JOptionPane.showMessageDialog(null, "Add successfully!");
            ana.loadData();
            ana.setLastUpdateTime();
        }

    }

    public int checkingDate(int rowNum, JTable table, long taskid, String rowData, int j, int colNum, Vector columnNames, AddRecordsTable info, ArrayList<String> rows) throws HeadlessException {
        while (rowNum != table.getRowCount() && !table.getValueAt(rowNum, 0).equals("")) {    // within accessible rows && not null next line
//            JOptionPane.showMessageDialog(null, table.getValueAt(i, 0));
            taskid++;   // go to the last line plus one
            rowData = "('" + taskid + "',";
            while (j < colNum - 1) {
                if (columnNames.get(j).toString().equals(info.getDateName())) {     // first, check date format if it's date column
                    if (table.getValueAt(rowNum, j).toString().matches("([0-9]{4})-([0-9]{2})-([0-9]{2})")) {
                        rowData += "'" + table.getValueAt(rowNum, j).toString() + "',";
                    } else {
                        rowData += null + ",";    // default date for null input
                        JOptionPane.showMessageDialog(null, "Date format is incorrect!");
                    }
                } else if (table.getValueAt(rowNum, j).toString().equals("")) {      // second, check null
                    rowData += null + ",";
                } else {
                    rowData += "'" + table.getValueAt(rowNum, j).toString() + "',";
                }
                j++;
               
            }
            if (table.getValueAt(rowNum, j)==null || "".equals(table.getValueAt(rowNum, j).toString()) ) {      // second, check null
                rowData += null + ")";
            } else {
                rowData += "'" + table.getValueAt(rowNum, j).toString() + "')";
            }
            rows.add(rowData);
            rowNum++;
            j = 0;
        }
        return rowNum;
    }

}
