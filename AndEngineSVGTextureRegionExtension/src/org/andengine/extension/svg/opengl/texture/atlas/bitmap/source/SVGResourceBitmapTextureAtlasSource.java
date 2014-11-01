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
public class SVGResourceBitmapTextureAtlasSource extends SVGBaseBitmapTextureAtlasSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final int mRawResourceID;
	private final ISVGColorMapper mSVGColorMapper;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTextureX, final int pTextureY) {
		this(pContext, pRawResourceID, pTextureX, pTextureY, null);
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTextureX, final int pTextureY, final float pScale) {
		this(pContext, pRawResourceID, pTextureX, pTextureY, pScale, null);
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight) {
		this(pContext, pRawResourceID, pTextureX, pTextureY, pTextureWidth, pTextureHeight, null);
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTextureX, final int pTextureY, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureAtlasSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTextureX, pTextureY);

		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTextureX, final int pTextureY, final float pScale, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureAtlasSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTextureX, pTextureY, pScale);

		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}

	public SVGResourceBitmapTextureAtlasSource(final Context pContext, final int pRawResourceID, final int pTextureX, final int pTextureY, final int pTextureWidth, final int pTextureHeight, final ISVGColorMapper pSVGColorMapper) {
		super(SVGResourceBitmapTextureAtlasSource.getSVG(pContext, pRawResourceID, pSVGColorMapper), pTextureX, pTextureY, pTextureWidth, pTextureHeight);

		this.mContext = pContext;
		this.mRawResourceID = pRawResourceID;
		this.mSVGColorMapper = pSVGColorMapper;
	}
	
	@Override
	public SVGResourceBitmapTextureAtlasSource deepCopy() {
		return new SVGResourceBitmapTextureAtlasSource(this.mContext, this.mRawResourceID, this.mTextureX, this.mTextureY, this.mTextureWidth, this.mTextureHeight, this.mSVGColorMapper);
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

	private static SVG getSVG(final Context pContext, final int pRawResourceID, final ISVGColorMapper pSVGColorMapper) {
		try {
			return SVGParser.parseSVGFromResource(pContext.getResources(), pRawResourceID, pSVGColorMapper);
		} catch (final Throwable t) {
			Debug.e("Failed loading SVG in SVGResourceBitmapTextureAtlasSource. RawResourceID: " + pRawResourceID, t);
			return null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
