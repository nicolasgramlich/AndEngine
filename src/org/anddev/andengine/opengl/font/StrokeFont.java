package org.anddev.andengine.opengl.font;

import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:39:33 - 03.04.2010
 */
public class StrokeFont extends Font {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Paint mStrokePaint;
	private final boolean mStrokeOnly;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StrokeFont(final BitmapTextureAtlas pBitmapTextureAtlas, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor) {
		this(pBitmapTextureAtlas, pTypeface, pSize, pAntiAlias, pColor, pStrokeWidth, pStrokeColor, false);
	}

	public StrokeFont(final BitmapTextureAtlas pBitmapTextureAtlas, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor, final float pStrokeWidth, final int pStrokeColor, final boolean pStrokeOnly) {
		super(pBitmapTextureAtlas, pTypeface, pSize, pAntiAlias, pColor);
		this.mStrokePaint = new Paint();
		this.mStrokePaint.setTypeface(pTypeface);
		this.mStrokePaint.setStyle(Style.STROKE);
		this.mStrokePaint.setStrokeWidth(pStrokeWidth);
		this.mStrokePaint.setColor(pStrokeColor);
		this.mStrokePaint.setTextSize(pSize);
		this.mStrokePaint.setAntiAlias(pAntiAlias);

		this.mStrokeOnly = pStrokeOnly;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void drawCharacterString(final String pCharacterAsString) {
		if(!this.mStrokeOnly) {
			super.drawCharacterString(pCharacterAsString);
		}
		this.mCanvas.drawText(pCharacterAsString, LETTER_LEFT_OFFSET, -this.mFontMetrics.ascent, this.mStrokePaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
