package com.elle.ProjectManager;

import com.elle.ProjectManager.logic.FilePathFormat;
import com.elle.ProjectManager.presentation.LoginWindow;
import com.elle.ProjectManager.logic.LoggingAspect;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static com.elle.ProjectManager.presentation.ProjectManagerWindow.version;
import static com.elle.ProjectManager.presentation.ProjectManagerWindow.creationDate;

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
            LoggingAspect.afterThrown(ex);
            JOptionPane.showMessageDialog(null, "error: " + ex.getMessage());
        }
        
        // get the creation date and version from the manifest
        Manifest mf = new Manifest();
        Attributes atts;
        String s = "MANIFEST.MF";
        InputStream inputStream = ProjectManager.class.getResourceAsStream(s);
        try {
            mf.read(inputStream);
            atts = mf.getMainAttributes();
            creationDate = atts.getValue("creation-date");
            version = atts.getValue("version");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }

        loadingMethod();
        mainMethod();
    }
//}

    public static SplashScreen loadingScreen;
    public static Double loadingTextArea;
    public static Double loadingProgressArea;
    public static Graphics2D loadingGraphics;
    public static BufferedImage image;
    public static Graphics2D g;

    public static void loadingMethod() {
        loadingScreen = SplashScreen.getSplashScreen();
        if (loadingScreen != null) {
            Dimension dim = loadingScreen.getSize();
            int height = dim.height;
            int width = dim.width;

            loadingTextArea = new Rectangle2D.Double(20, height * 0.7, width * 0.9, 20);
            loadingProgressArea = new Rectangle2D.Double(20, height * 0.8, width * 0.9, 5);

            loadingGraphics = loadingScreen.createGraphics();

            image = new BufferedImage(loadingScreen.getSize().width,
                    loadingScreen.getSize().height, BufferedImage.TYPE_INT_ARGB);

            g = image.createGraphics();
            BufferedImage img = null;
            try {
                img = ImageIO.read(loadingScreen.getImageURL());
            } catch (IOException e) {
                LoggingAspect.afterThrown(e);
            }
            g.drawImage(img, 0, 0, null);

        }
    }

    public static void loadingText(String string) {
        if (loadingScreen != null) {

            loadingGraphics.setPaint(Color.BLUE);
            g.setColor(Color.BLUE);
            Font font = new Font("Courier", Font.BOLD, 16);
            loadingGraphics.setFont(font);
            g.setFont(font);

            String information = "Version:" + version + "        CreationDate: " + creationDate;
            loadingGraphics.drawString(information, (int) loadingTextArea.getX() + 5, (int) loadingTextArea.getY());
            g.drawString(information, (int) loadingTextArea.getX() + 5, (int) loadingTextArea.getY());

            loadingGraphics.drawString(string, (int) loadingTextArea.getX() + 5, (int) loadingTextArea.getY() + 20);
            g.drawString(string, (int) loadingTextArea.getX() + 5, (int) loadingTextArea.getY() + 20);
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
            g.setPaint(Color.GRAY);

            loadingGraphics.fillRect(x, y, doneProg, ht);
            g.fillRect(x, y, doneProg, ht);

            loadingScreen.update();
        }
    }

    public static void mainMethod() {
        loadingMethod();
        
        for (int i = 1; i <= 5; i++) {
            loadingText("Loading...");
            loadingProgress(i * 20);
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                LoggingAspect.afterThrown(e);
                JOptionPane.showMessageDialog(null, "error: " + e.getMessage());
            }
        }

        saveImage();
        loadingScreen.close();
        // this is the first window that is shown to log in to the database.
        // Once the database connection is made, then an instance
        // of ProjectManager is created.

        LoginWindow loginWindow = new LoginWindow();
        loginWindow.setLocationRelativeTo(null);
        loginWindow.setVisible(true);
    }

    private static void saveImage() {
        String fileName = FilePathFormat.supportFilePath();
        try {
            if (!fileName.equals(" ")) {
                fileName += "splashImage.png";
                File file = new File(fileName);
                if(file.exists()){
                    ImageIO.write(image, "png", file);
                }
            }
        } catch (IOException ex) {
            LoggingAspect.addLogMsgWthDate("image save failed!" + " Due to: " + ex.getMessage());
            LoggingAspect.afterThrown(ex);
            JOptionPane.showMessageDialog(null, "error: " + ex.getMessage());
        }

    }
}
