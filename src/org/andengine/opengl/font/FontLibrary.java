package org.andengine.opengl.font;

import org.andengine.util.adt.map.Library;

import android.util.SparseArray;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:52:26 - 20.08.2010
 */
public class FontLibrary extends Library<Font> {
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

	public void loadFonts(final FontManager pFontManager) {
		final SparseArray<Font> items = this.mItems;
		for (int i = items.size() - 1; i >= 0; i--) {
			final Font font = items.valueAt(i);
			if (font != null) {
				pFontManager.loadFont(font);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
