package projectmanager_gui;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tina
 */


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CreateDocumentFilter extends DocumentFilter {

	private int promptPosition;

	public CreateDocumentFilter(int promptPosition) {
		this.promptPosition = promptPosition;
	}

	@Override
	public void insertString(final FilterBypass fb, final int offset,
			final String string, final AttributeSet attr)
			throws BadLocationException {
		if (offset >= promptPosition) {
			super.insertString(fb, offset, string, attr);
		}
	}

	@Override
	public void remove(final FilterBypass fb, final int offset, final int length)
			throws BadLocationException {
		if (offset >= promptPosition) {
			super.remove(fb, offset, length);
		}
	}

	@Override
	public void replace(final FilterBypass fb, final int offset,
			final int length, final String text, final AttributeSet attrs)
			throws BadLocationException {
		if (offset >= promptPosition) {
			super.replace(fb, offset, length, text, attrs);
		}
	}

}

