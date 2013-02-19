package org.andengine.entity.scene.menu.item;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.vbo.ITextVertexBufferObject;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 20:15:20 - 01.04.2010
 */
public class TextMenuItem extends Text implements IMenuItem {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pFont, pText, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pFont, pText, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pFont, pText, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pFont, pText, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pFont, pText, pTextOptions, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pFont, pText, pTextOptions, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pFont, pText, pTextOptions, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pFont, pText, pTextOptions, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final VertexBufferObjectManager pVertexBufferObjectManager, final ShaderProgram pShaderProgram) {
		super(0, 0, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager, pShaderProgram);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pFont, pText, pCharactersMaximum, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(0, 0, pFont, pText, pCharactersMaximum, pTextOptions, pVertexBufferObjectManager);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType) {
		super(0, 0, pFont, pText, pCharactersMaximum, pTextOptions, pVertexBufferObjectManager, pDrawType);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final TextOptions pTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, final ShaderProgram pShaderProgram) {
		super(0, 0, pFont, pText, pCharactersMaximum, pTextOptions, pVertexBufferObjectManager, pDrawType, pShaderProgram);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final TextOptions pTextOptions, final ITextVertexBufferObject pTextVertexBufferObject) {
		super(0, 0, pFont, pText, pCharactersMaximum, pTextOptions, pTextVertexBufferObject);

		this.mID = pID;
	}

	public TextMenuItem(final int pID, final IFont pFont, final CharSequence pText, final int pCharactersMaximum, final TextOptions pTextOptions, final ITextVertexBufferObject pTextVertexBufferObject, final ShaderProgram pShaderProgram) {
		super(0, 0, pFont, pText, pCharactersMaximum, pTextOptions, pTextVertexBufferObject, pShaderProgram);

		this.mID = pID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public int getID() {
		return this.mID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onSelected() {
		/* Nothing. */
	}

	@Override
	public void onUnselected() {
		/* Nothing. */
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
