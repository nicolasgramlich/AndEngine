package org.andengine.extension.svg.opengl.texture.atlas.bitmap.source;

import org.andengine.extension.svg.SVGParser;
import org.andengine.extension.svg.adt.ISVGColorMapper;
import org.andengine.extension.svg.adt.SVG;
import org.andengine.util.debug.Debug;

import android.content.Context;


/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:22:48 - 21.05.2011
 */
public class SVGAssetBitmapTextureAtlasSource extends SVGBaseBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final String mAssetPath;
	private final ISVGColorMapper mSVGColorMapper;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY) {
		this(pContext, pAssetPath, pTextureX, pTextureY, null);
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
		this(pContext, pAssetPath, pTextureX, pTextureY, pTextureWidth, pTextureHeight, null);
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY, final float pScale) {
		this(pContext, pAssetPath, pTextureX, pTextureY, pScale, null);
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetBitmapTextureAtlasSource.getSVG(pContext, pAssetPath, pSVGColorMapper), pTextureX, pTextureY);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY, final float pScale, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetBitmapTextureAtlasSource.getSVG(pContext, pAssetPath, pSVGColorMapper), pTextureX, pTextureY, pScale);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGAssetBitmapTextureAtlasSource(final Context pContext, final String pAssetPath, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight, final ISVGColorMapper pSVGColorMapper) {
		super(SVGAssetBitmapTextureAtlasSource.getSVG(pContext, pAssetPath, pSVGColorMapper), pTextureX, pTextureY, pTextureWidth, pTextureHeight);
		this.mContext = pContext;
		this.mAssetPath = pAssetPath;
		this.mSVGColorMapper = pSVGColorMapper;
	}
	
	@Override
	public SVGAssetBitmapTextureAtlasSource deepCopy() {
		return new SVGAssetBitmapTextureAtlasSource(this.mContext, this.mAssetPath, this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight, this.mSVGColorMapper);
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

	private static SVG getSVG(final Context pContext, final String pAssetPath, final ISVGColorMapper pSVGColorMapper) {
		try {
			return SVGParser.parseSVGFromAsset(pContext.getAssets(), pAssetPath, pSVGColorMapper);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGAssetBitmapTextureAtlasSource. AssetPath: " + pAssetPath, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
