package org.anddev.andengine.opengl.font;

import org.anddev.andengine.opengl.texture.ITexture;

import android.content.Context;
import android.graphics.Typeface;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 17:17:28 - 16.06.2010
 */
public class FontFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static String sAssetBasePath = "";

	// ===========================================================
	// Constructors
	// ===========================================================

	private FontFactory() {}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @param pAssetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
	 */
	public static void setAssetBasePath(final String pAssetBasePath) {
		if(pAssetBasePath.endsWith("/") || pAssetBasePath.length() == 0) {
			FontFactory.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalStateException("pAssetBasePath must end with '/' or be lenght zero.");
		}
	}

	public static void reset() {
		FontFactory.setAssetBasePath("");
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static Font create(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		return new Font(pTexture, pTypeface, pSize, pAntiAlias, pColor);
	}

	public static StrokeFont createStroke(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor) {
		return new StrokeFont(pTexture, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor);
	}

	public static StrokeFont createStroke(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor, final boolean pStrokeOnly) {
		return new StrokeFont(pTexture, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor, pStrokeOnly);
	}

	public static Font createFromAsset(final ITexture pTexture, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor) {
		return new Font(pTexture, Typeface.createFromAsset(pContext.getAssets(), FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor);
	}

	public static StrokeFont createStrokeFromAsset(final ITexture pTexture, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor) {
		return new StrokeFont(pTexture, Typeface.createFromAsset(pContext.getAssets(), FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor);
	}

	public static StrokeFont createStrokeFromAsset(final ITexture pTexture, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor, final boolean pStrokeOnly) {
		return new StrokeFont(pTexture, Typeface.createFromAsset(pContext.getAssets(), FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor, pStrokeOnly);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
