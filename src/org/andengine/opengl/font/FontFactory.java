package org.andengine.opengl.font;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture.BitmapTextureFormat;

import android.content.Context;
import android.graphics.Color;
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

	private static final boolean ANTIALIAS_DEFAULT = true;
	private static final int COLOR_DEFAULT = Color.BLACK;

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

	public static void onCreate() {
		FontFactory.setAssetBasePath("");
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static Font create(final ITexture pTexture, final float pSize) {
		return FontFactory.create(pTexture, pSize, FontFactory.ANTIALIAS_DEFAULT, FontFactory.COLOR_DEFAULT);
	}

	public static Font create(final ITexture pTexture, final float pSize, final boolean pAntiAlias) {
		return FontFactory.create(pTexture, pSize, pAntiAlias, FontFactory.COLOR_DEFAULT);
	}

	public static Font create(final ITexture pTexture, final float pSize, final int pColor) {
		return FontFactory.create(pTexture, pSize, FontFactory.ANTIALIAS_DEFAULT, pColor);
	}

	public static Font create(final ITexture pTexture, final float pSize, final boolean pAntiAlias, final int pColor) {
		return FontFactory.create(pTexture, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), pSize, pAntiAlias, pColor);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final float pSize, final boolean pAntiAlias, final int pColor) {
		return FontFactory.create(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pSize, pAntiAlias, pColor);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final float pSize, final boolean pAntiAlias, final int pColor) {
		return FontFactory.create(pTextureWidth, pTextureHeight, pTextureOptions, Typeface.create(Typeface.DEFAULT, Typeface.NORMAL), pSize, pAntiAlias, pColor);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize) {
		return FontFactory.create(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, FontFactory.COLOR_DEFAULT);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize, final boolean pAntiAlias) {
		return FontFactory.create(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pTypeface, pSize, pAntiAlias, FontFactory.COLOR_DEFAULT);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize, final int pColor) {
		return FontFactory.create(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, pColor);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		return FontFactory.create(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pTypeface, pSize, pAntiAlias, pColor);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize) {
		return FontFactory.create(pTextureWidth, pTextureHeight, pTextureOptions, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, FontFactory.COLOR_DEFAULT);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias) {
		return FontFactory.create(pTextureWidth, pTextureHeight, pTextureOptions, pTypeface, pSize, pAntiAlias, FontFactory.COLOR_DEFAULT);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize, final int pColor) {
		return FontFactory.create(pTextureWidth, pTextureHeight, pTextureOptions, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, pColor);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		return FontFactory.create(pTextureWidth, pTextureHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pTypeface, pSize, pAntiAlias, pColor);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize) {
		return FontFactory.create(pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, FontFactory.COLOR_DEFAULT);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias) {
		return FontFactory.create(pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions, pTypeface, pSize, pAntiAlias, FontFactory.COLOR_DEFAULT);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize, final int pColor) {
		return FontFactory.create(pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions, pTypeface, pSize, FontFactory.ANTIALIAS_DEFAULT, pColor);
	}

	public static Font create(final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		return FontFactory.create(new BitmapTextureAtlas(pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions), pTypeface, pSize, pAntiAlias, pColor);
	}

	public static Font create(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		return new Font(pTexture, pTypeface, pSize, pAntiAlias, pColor);
	}


	public static Font createFromAsset(final ITexture pTexture, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor) {
		return new Font(pTexture, Typeface.createFromAsset(pContext.getAssets(), FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor);
	}

	public static Font createFromAsset(final int pTextureWidth, final int pTextureHeight, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor) {
		return FontFactory.createFromAsset(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pContext, pAssetPath, pSize, pAntiAlias, pColor);
	}

	public static Font createFromAsset(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor) {
		return FontFactory.createFromAsset(pTextureWidth, pTextureHeight, BitmapTextureFormat.RGBA_8888, pTextureOptions, pContext, pAssetPath, pSize, pAntiAlias, pColor);
	}

	public static Font createFromAsset(final int pTextureWidth, final int pTextureHeight, final BitmapTextureFormat pBitmapTextureFormat, final TextureOptions pTextureOptions, final Context pContext, final String pAssetPath, final float pSize, final boolean pAntiAlias, final int pColor) {
		return new Font(new BitmapTextureAtlas(pTextureWidth, pTextureHeight, pBitmapTextureFormat, pTextureOptions), Typeface.createFromAsset(pContext.getAssets(), FontFactory.sAssetBasePath + pAssetPath), pSize, pAntiAlias, pColor);
	}


	public static StrokeFont createStroke(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor) {
		return new StrokeFont(pTexture, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor);
	}

	public static StrokeFont createStroke(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor, final boolean pStrokeOnly) {
		return new StrokeFont(pTexture, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor, pStrokeOnly);
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
