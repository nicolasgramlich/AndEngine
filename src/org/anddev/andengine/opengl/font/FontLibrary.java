package org.anddev.andengine.opengl.font;

import org.anddev.andengine.util.Library;

import android.util.SparseArray;

/**
 * @author Nicolas Gramlich
 * @since 11:52:26 - 20.08.2010
 */
public class FontLibrary extends Library<Font>{
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public FontLibrary() {
		super();
	}
	
	public FontLibrary(final int pInitialCapacity) {
		super(pInitialCapacity);
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

	void loadFonts(FontManager pFontManager) {
		final SparseArray<Font> items = this.mItems;
		for(int i = items.size() - 1; i >= 0; i--) {
			pFontManager.loadFont(items.get(i));
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
