/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager;

import com.elle.ProjectManager.presentation.ProjectManagerWindow;
import com.elle.ProjectManager.presentation.LoginWindow;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

/**
 * Main This is the class that starts the application from the main method
 *
 * @author Xiaoqian Fu
 * @since AUG 28, 2015
 * @version 0.9.9
 */
public class ProjectManager {

    public static void main(String[] args) {

        // set the look and feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProjectManagerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        loadingMethod();
        mainMethod();

    }
//}

public static SplashScreen loadingScreen;
    public static Double loadingTextArea;
    public static Double loadingProgressArea;
    public static Graphics2D loadingGraphics;

    public static void loadingMethod() {
        loadingScreen = SplashScreen.getSplashScreen();
        if (loadingScreen != null) {
            Dimension dim = loadingScreen.getSize();
            int height = dim.height;
            int width = dim.width;

            loadingTextArea = new Rectangle2D.Double(20, height * 0.7, width * 0.9, 20);
            loadingProgressArea = new Rectangle2D.Double(20, height * 0.8, width * 0.9, 5);

            loadingGraphics = loadingScreen.createGraphics();

        }
    }

    public static void loadingText(String string) {
        if (loadingScreen != null) {
//            loadingGraphics.setPaint(Color.GRAY);
//            loadingGraphics.fill(loadingTextArea);

            loadingGraphics.setPaint(Color.BLACK);

            loadingGraphics.drawString(string, (int) loadingTextArea.getX() + 10, (int) loadingTextArea.getY() + 20);

            loadingScreen.update();
        }
    }

    public static void loadingProgress(int prog) {
        if (loadingScreen != null) {
            loadingGraphics.setPaint(Color.LIGHT_GRAY);
            loadingGraphics.fill(loadingProgressArea);

            loadingGraphics.setPaint(Color.BLACK);

            loadingGraphics.draw(loadingProgressArea);

            int x = (int) loadingProgressArea.getMinX();
            int y = (int) loadingProgressArea.getMinY();

            int wd = (int) loadingProgressArea.getWidth();
            int ht = (int) loadingProgressArea.getHeight();

            int doneProg = prog * wd / 100;

            loadingGraphics.setPaint(Color.GRAY);
            loadingGraphics.fillRect(x, y, doneProg, ht);

            loadingScreen.update();
        }
    }

    public static void mainMethod() {
//        final String[] comps = {"table PM", "table ELLEGUI", "table Analyster", "table Other", "table issue_files",};
        loadingMethod();
        for (int i = 1; i <= 5; i++) {
            loadingText("Loading...");
//            + comps[i-1] + "...");
            loadingProgress(i * 20);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        loadingScreen.close();
        // this is the first window that is shown to log in to the database.
        // Once the database connection is made, then an instance
        // of ProjectManager is created.

        LoginWindow loginWindow = new LoginWindow();
        loginWindow.setLocationRelativeTo(null);
        loginWindow.setVisible(true);
    }

}
//public class ProjectManager {
//    
//    public static void main(String[] args){
//        
//        // set the look and feel
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ProjectManagerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//
//        // this is the first window that is shown to log in to the database.
//        // Once the database connection is made, then an instance
//        // of ProjectManager is created.
//        LoginWindow loginWindow = new LoginWindow();
//        loginWindow.setLocationRelativeTo(null);
//        loginWindow.setVisible(true);
//    }
//}
