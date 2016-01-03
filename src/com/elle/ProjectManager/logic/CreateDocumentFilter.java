package com.elle.ProjectManager.logic;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * CreateDocumentFilter
 * - Not sure about this class. It has something to do with the sql text area. 
 * The problem was that it is used in generated code so I was unable to find out 
 * how to disable it to test it any further. I did not see anything in properties.
 * @author Tina
 * @author Carlos Igreja
 * @since June 10, 2015
 * @version 0.6.3
 */
public class CreateDocumentFilter extends DocumentFilter {

    // attributes
    private int promptPosition;

    /**
     * CreateDocumentFilter
     * @param promptPosition 
     */
    public CreateDocumentFilter(int promptPosition) {
        this.promptPosition = promptPosition;
    }

    /**
     * insertString
     * @param fb
     * @param offset
     * @param string
     * @param attr
     * @throws BadLocationException 
     */
    @Override
    public void insertString(final FilterBypass fb, final int offset,
                    final String string, final AttributeSet attr)
                    throws BadLocationException {
        
        if (offset >= promptPosition) {
                super.insertString(fb, offset, string, attr);
        }
    }

    /**
     * remove
     * @param fb
     * @param offset
     * @param length
     * @throws BadLocationException 
     */
    @Override
    public void remove(final FilterBypass fb, final int offset, final int length)
                    throws BadLocationException {
        
        if (offset >= promptPosition) {
                super.remove(fb, offset, length);
        }
    }

    /**
     * replace
     * @param fb
     * @param offset
     * @param length
     * @param text
     * @param attrs
     * @throws BadLocationException 
     */
    @Override
    public void replace(final FilterBypass fb, final int offset,
                    final int length, final String text, final AttributeSet attrs)
                    throws BadLocationException {
        
        if (offset >= promptPosition) {
                super.replace(fb, offset, length, text, attrs);
        }
    }

}

