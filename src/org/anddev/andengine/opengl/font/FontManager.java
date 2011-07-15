package org.anddev.andengine.opengl.font;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:48:46 - 08.03.2010
 */
public class FontManager {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ArrayList<Font> mFontsManaged = new ArrayList<Font>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void clear() {
		this.mFontsManaged.clear();
	}

	public void loadFont(final Font pFont) {
		if(pFont == null) {
			throw new IllegalArgumentException("pFont must not be null!");
		}
		this.mFontsManaged.add(pFont);
	}

	public void loadFonts(final FontLibrary pFontLibrary) {
		pFontLibrary.loadFonts(this);
	}

	public void loadFonts(final Font ... pFonts) {
		for(int i = pFonts.length - 1; i >= 0; i--) {
			this.loadFont(pFonts[i]);
		}
	}

	public void updateFonts(final GL10 pGL) {
		final ArrayList<Font> fonts = this.mFontsManaged;
		final int fontCount = fonts.size();
		if(fontCount > 0){
			for(int i = fontCount - 1; i >= 0; i--){
				fonts.get(i).update(pGL);
			}
		}
	}

	public void reloadFonts() {
		final ArrayList<Font> managedFonts = this.mFontsManaged;
		for(int i = managedFonts.size() - 1; i >= 0; i--) {
			managedFonts.get(i).reload();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
