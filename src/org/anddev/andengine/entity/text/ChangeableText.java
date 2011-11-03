package org.anddev.andengine.entity.text;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.opengl.font.IFont;
import org.anddev.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.anddev.andengine.util.HorizontalAlign;
import org.anddev.andengine.util.StringUtils;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:07:06 - 08.07.2010
 */
public class ChangeableText extends Text {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String ELLIPSIS = "...";
	private static final int ELLIPSIS_CHARACTER_COUNT = ChangeableText.ELLIPSIS.length();

	// ===========================================================
	// Fields
	// ===========================================================

	private int mCharacterCountCurrentText;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ChangeableText(final float pX, final float pY, final IFont pFont, final String pText) {
		this(pX, pY, pFont, pText, DrawType.DYNAMIC);
	}

	public ChangeableText(final float pX, final float pY, final IFont pFont, final String pText, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, pText.length() - StringUtils.countOccurrences(pText, '\n'), pDrawType);
	}

	public ChangeableText(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum) {
		this(pX, pY, pFont, pText, HorizontalAlign.LEFT, pCharactersMaximum, DrawType.DYNAMIC);
	}

	public ChangeableText(final float pX, final float pY, final IFont pFont, final String pText, final int pCharactersMaximum, final DrawType pDrawType) {
		this(pX, pY, pFont, pText, HorizontalAlign.LEFT, pCharactersMaximum, pDrawType);
	}

	public ChangeableText(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final int pCharactersMaximum, final DrawType pDrawType) {
		super(pX, pY, pFont, pText, pHorizontalAlign, pCharactersMaximum, pDrawType);
		this.mCharacterCountCurrentText = pText.length() - StringUtils.countOccurrences(pText, '\n');
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setText(final String pText) {
		this.setText(pText, false);
	}

	/**
	 * @param pText
	 * @param pAllowEllipsis in the case pText is longer than <code>pCharactersMaximum</code>,
	 *	which was passed to the constructor, the displayed text will end with an ellipsis ("...").
	 */
	public void setText(final String pText, final boolean pAllowEllipsis) {
		final int textCharacterCount = pText.length() - StringUtils.countOccurrences(pText, '\n');
		if(textCharacterCount > this.mCharactersMaximum) {
			if(pAllowEllipsis && this.mCharactersMaximum > ChangeableText.ELLIPSIS_CHARACTER_COUNT) {
				this.updateText(pText.substring(0, this.mCharactersMaximum - ChangeableText.ELLIPSIS_CHARACTER_COUNT).concat(ChangeableText.ELLIPSIS)); // TODO This allocation could maybe be avoided...
			} else {
				this.updateText(pText.substring(0, this.mCharactersMaximum)); // TODO This allocation could be avoided...
			}
			this.mCharacterCountCurrentText = this.mCharactersMaximum;
		} else {
			this.updateText(pText);
			this.mCharacterCountCurrentText = textCharacterCount;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void draw(final Camera pCamera) {
		this.mMesh.draw(this.mShaderProgram, GLES20.GL_TRIANGLES, this.mCharacterCountCurrentText * Text.VERTICES_PER_LETTER);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
