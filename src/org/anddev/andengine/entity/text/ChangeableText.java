package org.anddev.andengine.entity.text;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.vertex.TextVertexBuffer;
import org.anddev.andengine.util.HorizontalAlign;
import org.anddev.andengine.util.StringUtils;

/**
 * @author Nicolas Gramlich
 * @since 18:07:06 - 08.07.2010
 */
public class ChangeableText extends Text {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private int mCharacterCountCurrentText;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ChangeableText(final float pX, final float pY, final Font pFont, final String pText) {
		this(pX, pY, pFont, pText, pText.length() - StringUtils.countOccurrences(pText, '\n'));
	}

	public ChangeableText(final float pX, final float pY, final Font pFont, final String pText, final int pCharactersMaximum) {
		super(pX, pY, pFont, pText, HorizontalAlign.LEFT, pCharactersMaximum);
		this.mCharacterCountCurrentText = pText.length() - StringUtils.countOccurrences(pText, '\n');
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public void setText(final String pText) {
		final int newCharacterCount = pText.length() - StringUtils.countOccurrences(pText, '\n');
		if(newCharacterCount > this.mCharactersMaximum) {
			this.setText(pText.substring(0, this.mCharactersMaximum));
			this.mCharacterCountCurrentText = this.mCharactersMaximum;
		} else {
			this.updateText(pText);
			this.mCharacterCountCurrentText = newCharacterCount;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void drawVertices(final GL10 pGL) {
		pGL.glDrawArrays(GL10.GL_TRIANGLES, 0, this.mCharacterCountCurrentText * TextVertexBuffer.VERTICES_PER_CHARACTER);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
