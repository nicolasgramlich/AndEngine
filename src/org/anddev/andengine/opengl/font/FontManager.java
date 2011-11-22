package org.anddev.andengine.opengl.font;

import java.util.ArrayList;


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

	private static final ArrayList<Font> sFontsManaged = new ArrayList<Font>();

	// ===========================================================
	// Constructors
	// ===========================================================

	private FontManager() {

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

	public static void onCreate() {

	}

	public static synchronized void onDestroy() {
		final ArrayList<Font> managedFonts = FontManager.sFontsManaged;
		for(int i = managedFonts.size() - 1; i >= 0; i--) {
			managedFonts.get(i).invalidateLetters();
		}

		FontManager.sFontsManaged.clear();
	}

	static synchronized void loadFont(final Font pFont) {
		if(pFont == null) {
			throw new IllegalArgumentException("pFont must not be null!");
		}
		FontManager.sFontsManaged.add(pFont);
	}

	static synchronized void unloadFont(final Font pFont) {
		if(pFont == null) {
			throw new IllegalArgumentException("pFont must not be null!");
		}
		FontManager.sFontsManaged.remove(pFont);
	}

	public static synchronized void updateFonts() {
		final ArrayList<Font> fonts = FontManager.sFontsManaged;
		final int fontCount = fonts.size();
		if(fontCount > 0){
			for(int i = fontCount - 1; i >= 0; i--){
				fonts.get(i).update();
			}
		}
	}

	public static synchronized void onReload() {
		final ArrayList<Font> managedFonts = FontManager.sFontsManaged;
		for(int i = managedFonts.size() - 1; i >= 0; i--) {
			managedFonts.get(i).invalidateLetters();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
