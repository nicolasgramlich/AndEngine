package org.anddev.andengine.opengl.font;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;


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

	private static final ArrayList<Font> mFonts = new ArrayList<Font>();

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
	
	public static void clear() {
		FontManager.mFonts.clear();
	}

	public static void loadFont(final Font pFont) {
		FontManager.mFonts.add(pFont);
	}

	public static void ensureFontsLoadedToHardware(final GL10 pGL) {
		final ArrayList<Font> fonts = FontManager.mFonts;
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
