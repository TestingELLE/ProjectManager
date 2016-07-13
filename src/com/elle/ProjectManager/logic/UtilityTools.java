/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elle.ProjectManager.logic;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

/**
 *
 * @author Yi
 */
public class UtilityTools {
    //convert rtf description to string for table row
    public static String byteArrayToString(byte[] input) {
        byte[] descriptiontablebytesout;

        if (input == null) {
            descriptiontablebytesout = new byte[0];
        } else {
            descriptiontablebytesout = input;
        }
        
        InputStream descriptiontablestream = new ByteArrayInputStream(descriptiontablebytesout);
        String convertedstrings = "";
        try {
            convertedstrings = convertStreamToString(descriptiontablestream);
        } catch (IOException ex) {
            Logger.getLogger(UtilityTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String rtfsign = "\\par";
        boolean rtfornot = convertedstrings.contains(rtfsign);
        
        if (rtfornot) {
            try {
                RTFEditorKit rtfParser = new RTFEditorKit();
                Document document = rtfParser.createDefaultDocument();
                rtfParser.read(new ByteArrayInputStream(descriptiontablebytesout), document, 0);
                String text = document.getText(0, document.getLength());
                return text;
            } catch (IOException ex) {
                Logger.getLogger(UtilityTools.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadLocationException ex) {
                Logger.getLogger(UtilityTools.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return convertedstrings;
        }
        return null;
    }
    
    private  static String convertStreamToString(InputStream is) throws IOException {
        // To convert the InputStream to String we use the
        // Reader.read(char[] buffer) method. We iterate until the
        // Reader return -1 which means there's no more data to
        // read. We use the StringWriter class to produce the string.
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader;
                reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }
        return "";
    }
    
}
