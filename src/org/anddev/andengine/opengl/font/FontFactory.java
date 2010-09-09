package org.anddev.andengine.opengl.font;

import org.anddev.andengine.opengl.texture.Texture;

import android.content.Context;
import android.graphics.Typeface;

/**
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

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public static void setAssetBasePath(final String pAssetBasePath) {
		FontFactory.sAssetBasePath = pAssetBasePath;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static Font create(final Texture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		return new Font(pTexture, pTypeface, pSize, pAntiAlias, pColor);
	}
	
	public static StrokeFont createStroke(final Texture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final int pStrokeWidth, final int pStrokeColor) {
		return new StrokeFont(pTexture, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor);
	}
	
	public static StrokeFont createStroke(final Texture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final int pStrokeWidth, final int pStrokeColor, final boolean pStrokeOnly) {
		return new StrokeFont(pTexture, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor, pStrokeOnly);
	}

	public static Font createFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor) {
		return new Font(pTexture, Typeface.createFromAsset(pContext.getAssets(), FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor);
	}
	
	public static StrokeFont createStrokeFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor, final int pStrokeWidth, final int pStrokeColor) {
		return new StrokeFont(pTexture, Typeface.createFromAsset(pContext.getAssets(), FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor);
	}
	
	public static StrokeFont createStrokeFromAsset(final Texture pTexture, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor, final int pStrokeWidth, final int pStrokeColor, final boolean pStrokeOnly) {
		return new StrokeFont(pTexture, Typeface.createFromAsset(pContext.getAssets(), FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor, pStrokeOnly);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
