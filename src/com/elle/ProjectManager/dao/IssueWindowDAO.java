//
//package com.elle.ProjectManager.dao;
//
//import com.elle.ProjectManager.database.DBConnection;
//import com.elle.ProjectManager.logic.Field;
//import static com.elle.ProjectManager.logic.ITableConstants.TASKS_TABLE_NAME;
//import com.elle.ProjectManager.logic.Issue;
//import com.elle.ProjectManager.logic.LoggingAspect;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// * BackupDBTableDAO
// *
// * @author Xiaoqian Fu
// * @since Mar 31, 2016
// */
//public class IssueWindowDAO {
//
//    // database table information
//    private final String TABLE_NAME = TASKS_TABLE_NAME;
//    // components
//    private Connection connection;
//    private Statement statement;
//
//    public IssueWindowDAO() {
//        // open new connection
//        DBConnection.close(); // connection might be timed out on server
//        if (DBConnection.open()) {  // open a new connection
//            this.connection = DBConnection.getConnection();
//        } else {
//            String text = "Issue window cannot connect to database!";
//            LoggingAspect.addLogMsgWthDate(text);
//            System.out.println(text);
//        }
//        try {
//            this.statement = connection.createStatement();
//        } catch (SQLException ex) {
//            Logger.getLogger(IssueWindowDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public boolean updateChanges(Issue issue) {
//
//        DBConnection.close();
//        if (DBConnection.open()) {
//            String id = issue.getID();
//
//            String sqlChange = "UPDATE " + TABLE_NAME + " SET ";
//
//            for (int i = 0; i < issue.getIssue().size(); i++) {
//                Field field = issue.getIssueData(i);
//                if (field.isValueChanged()) {
//                    String columnName = field.getName();
//                    String value = field.getValue();
//                    if (value.equals("")) {
//                        value = null;
//                    } else {
//                        this.processCellValue(value);
//                        value = "'" + value + "'";
//                    }
//                    if (sqlChange.endsWith("SET ")) {
//                        sqlChange += columnName + " = " + value;
//                    } else {
//                        sqlChange += ", " + columnName + " = " + value;
//                    }
//                }
//            }
//            sqlChange += " WHERE ID = " + id + ";";
//            System.out.println(sqlChange);
//
//            statement = DBConnection.getStatement();
//            try {
//                statement.executeUpdate(sqlChange);
//                LoggingAspect.afterReturn(sqlChange);
//                DBConnection.close();
//                return true;
//            } catch (SQLException e) {
//                LoggingAspect.afterThrown(e);
//                DBConnection.close();
//                return false;
//            }
//        } else {
//            DBConnection.close();
//            return false;
//        }
//
//    }
//
//    public boolean submitNewIssue(Issue issue) {
//        DBConnection.close();
//        if (DBConnection.open()) {
//            // once data checked, execute sql statement
//            // first get the insert statement for the tableSelected
//            String insertInto = "INSERT INTO issues (";
//            String values = "VALUES (";
//
//            // this tableSelected should already not include the primary key
//            for (int i = 0; i < issue.getIssue().size(); i++) {
//                Field field = issue.getIssueData(i);
//                String columnName = field.getName();
//                String value = field.getValue();
//                if (value.equals("")) {
//                    value = null;
//                } else {
//                    value = "'" + processCellValue(value) + "'";
//                }
//                if (i != issue.getIssue().size() - 1) {
//                    insertInto += columnName + ", ";
//                    values += value + ", ";
//                } else {
//                    insertInto += columnName;
//                    values += value;
//                }
//            }
//            insertInto += ") ";
//            values += ");";
//            System.out.println(insertInto + values);
//
//            statement = DBConnection.getStatement();
//            try {
//                // execute the sql statement
//                if (!values.equals("VALUES (")) {      //skip if nothing was added
//                    statement.executeUpdate(insertInto + values);
//                    LoggingAspect.afterReturn("submit new issue successfully!");
//                    DBConnection.close();
//                    return true;
//                }
//            } catch (SQLException e) {
//                LoggingAspect.addLogMsgWthDate("2: " + e.getMessage());
//                LoggingAspect.addLogMsgWthDate("2:add issue submit failed!");
//                LoggingAspect.afterThrown(e);
//                DBConnection.close();
//                return false;
//            }
//        } else {
//            DBConnection.close();
//            return false;
//        }
//        return false;
//    }
//
//    private String processCellValue(String cellValue) {
//        return cellValue.replaceAll("'", "''");
//    }
//
//    public String getValueFromDataBase(String colName, Issue issue) {
//        String ID = issue.getID();
//        String sql = "select " + colName + " from issues where ID = '" + ID + "';";
//        ResultSet rs = null;
//        String value = "";
//        DBConnection.close();
////        DBConnection.open();
//        if (DBConnection.open()) {
//            statement = DBConnection.getStatement();
//            try {
//                rs = statement.executeQuery(sql);
//
//                while (rs.next()) {
//                    value = rs.getString(colName);
//
//                }
//
//            } catch (Exception ex) {
//                LoggingAspect.afterThrown(ex);
//            }
//            if (value == null) {
//                value = "";
//            }
//            System.out.println("get: " + colName + " " + value);
//            DBConnection.close();
//        } else {
//            DBConnection.close();
//        }
//        return value;
//    }
//
//}
