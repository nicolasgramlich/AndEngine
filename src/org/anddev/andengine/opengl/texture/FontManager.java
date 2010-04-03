package org.anddev.andengine.opengl.texture;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.text.Font;

/**
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

	private final ArrayList<Font> mFonts = new ArrayList<Font>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public FontManager() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void addFont(final Font pFont) {
		this.mFonts.add(pFont);
	}

	public void updateFonts(final GL10 pGL) {
		final ArrayList<Font> fonts = this.mFonts;
		final int fontCount = fonts.size();
		if(fontCount > 0){
			for(int i = 0; i < fontCount; i++){
				fonts.get(i).update(pGL);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
