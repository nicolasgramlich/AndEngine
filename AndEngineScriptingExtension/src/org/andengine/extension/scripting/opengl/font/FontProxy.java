package org.andengine.extension.scripting.opengl.font;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.ITexture;

import android.graphics.Typeface;


/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:07:16 - 05.03.2012
 */
public class FontProxy extends Font {
	// ===========================================================
	// Constants
	// ===========================================================

	public static native void nativeInitClass();

	// ===========================================================
	// Fields
	// ===========================================================

	private final long mAddress;

	// ===========================================================
	// Constructors
	// ===========================================================

	public FontProxy(final long pAddress, final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColorARGBPackedInt) {
		super(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt);

		this.mAddress = pAddress;
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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
