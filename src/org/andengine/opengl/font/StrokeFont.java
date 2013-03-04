package org.andengine.opengl.font;

import org.andengine.opengl.texture.ITexture;
import org.andengine.util.adt.color.Color;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

/**
 * TODO Re-implement with Font changes.
 *
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
	private final float mStrokeWidth;

	// ===========================================================
	// Constructors
	// ===========================================================

	public StrokeFont(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final Color pColor, final float pStrokeWidth, final Color pStrokeColor) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColor.getARGBPackedInt(), pStrokeWidth, pStrokeColor.getARGBPackedInt());
	}

	public StrokeFont(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColorARGBPackedInt, final float pStrokeWidth, final int pStrokeColorARGBPackedInt) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt, pStrokeWidth, pStrokeColorARGBPackedInt, false);
	}

	public StrokeFont(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final Color pColor, final float pStrokeWidth, final Color pStrokeColor, final boolean pStrokeOnly) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColor.getARGBPackedInt(), pStrokeWidth, pStrokeColor.getARGBPackedInt(), pStrokeOnly);
	}

	public StrokeFont(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColorARGBPackedInt, final float pStrokeWidth, final int pStrokeColorARGBPackedInt, final boolean pStrokeOnly) {
		super(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColorARGBPackedInt);

		this.mStrokeWidth = pStrokeWidth;

		this.mStrokePaint = new Paint();
		this.mStrokePaint.setTypeface(pTypeface);
		this.mStrokePaint.setStyle(Style.STROKE);
		this.mStrokePaint.setStrokeWidth(pStrokeWidth);
		this.mStrokePaint.setColor(pStrokeColorARGBPackedInt);
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
	protected void updateTextBounds(final String pCharacterAsString) {
		this.mStrokePaint.getTextBounds(pCharacterAsString, 0, 1, this.mTextBounds);
		final int inset = -(int)Math.ceil(this.mStrokeWidth * 0.5f);
		this.mTextBounds.inset(inset, inset);
	}

	@Override
	protected void drawLetter(final String pCharacterAsString, final float pLeft, final float pTop) {
		if (!this.mStrokeOnly) {
			super.drawLetter(pCharacterAsString, pLeft, pTop);
		}
		this.mCanvas.drawText(pCharacterAsString, pLeft + Font.LETTER_TEXTURE_PADDING, pTop + Font.LETTER_TEXTURE_PADDING, this.mStrokePaint);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
