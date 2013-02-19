package org.andengine.opengl.font;

import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.opengl.util.GLState;


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
	private final ArrayList<BitmapFont> mBitmapFontsManaged = new ArrayList<BitmapFont>();
	private final HashMap<String, IFont> mFontsMapped = new HashMap<String, IFont>();

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

	public void onCreate() {

	}

	public synchronized void onDestroy() {
		final ArrayList<Font> managedFonts = this.mFontsManaged;
		for (int i = managedFonts.size() - 1; i >= 0; i--) {
			managedFonts.get(i).invalidateLetters();
		}

		this.mFontsManaged.clear();
		this.mBitmapFontsManaged.clear();
		this.mFontsMapped.clear();
	}

	public synchronized boolean hasMappedFont(final String pID) {
		if (pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return this.mFontsMapped.containsKey(pID);
	}

	public synchronized IFont getMappedFont(final String pID) {
		if (pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return this.mFontsMapped.get(pID);
	}

	public synchronized void addMappedFont(final String pID, final IFont pFont) throws IllegalArgumentException {
		if (pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		} else if (pFont == null) {
			throw new IllegalArgumentException("pFont must not be null!");
		} else if (this.mFontsMapped.containsKey(pID)) {
			throw new IllegalArgumentException("Collision for pID: '" + pID + "'.");
		}
		this.mFontsMapped.put(pID, pFont);
	}

	public synchronized IFont removedMappedFont(final String pID) {
		if (pID == null) {
			throw new IllegalArgumentException("pID must not be null!");
		}
		return this.mFontsMapped.remove(pID);
	}

	public synchronized void loadFont(final Font pFont) {
		if (pFont == null) {
			throw new IllegalArgumentException("pFont must not be null!");
		}
		this.mFontsManaged.add(pFont);
	}

	public synchronized void loadFont(final BitmapFont pBitmapFont) {
		if (pBitmapFont == null) {
			throw new IllegalArgumentException("pBitmapFont must not be null!");
		}
		this.mBitmapFontsManaged.add(pBitmapFont);
	}

	public synchronized void loadFonts(final Font ... pFonts) {
		for (int i = 0; i < pFonts.length; i++) {
			this.loadFont(pFonts[i]);
		}
	}

	public synchronized void loadFonts(final BitmapFont ... pBitmapFonts) {
		for (int i = 0; i < pBitmapFonts.length; i++) {
			this.loadFont(pBitmapFonts[i]);
		}
	}

	public synchronized void unloadFont(final Font pFont) {
		if (pFont == null) {
			throw new IllegalArgumentException("pFont must not be null!");
		}
		this.mFontsManaged.remove(pFont);
	}

	public synchronized void unloadFont(final BitmapFont pBitmapFont) {
		if (pBitmapFont == null) {
			throw new IllegalArgumentException("pBitmapFont must not be null!");
		}
		this.mBitmapFontsManaged.remove(pBitmapFont);
	}

	public synchronized void unloadFonts(final Font ... pFonts) {
		for (int i = 0; i < pFonts.length; i++) {
			this.unloadFont(pFonts[i]);
		}
	}

	public synchronized void unloadFonts(final BitmapFont ... pBitmapFonts) {
		for (int i = 0; i < pBitmapFonts.length; i++) {
			this.unloadFont(pBitmapFonts[i]);
		}
	}

	public synchronized void updateFonts(final GLState pGLState) {
		final ArrayList<Font> fonts = this.mFontsManaged;
		final int fontCount = fonts.size();
		if (fontCount > 0) {
			for (int i = fontCount - 1; i >= 0; i--) {
				fonts.get(i).update(pGLState);
			}
		}
	}

	public synchronized void onReload() {
		final ArrayList<Font> managedFonts = this.mFontsManaged;
		for (int i = managedFonts.size() - 1; i >= 0; i--) {
			managedFonts.get(i).invalidateLetters();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
