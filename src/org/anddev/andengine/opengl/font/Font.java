package org.anddev.andengine.opengl.font;

import java.util.ArrayList;

import org.anddev.andengine.opengl.font.exception.FontException;
import org.anddev.andengine.opengl.texture.ITexture;
import org.anddev.andengine.opengl.texture.Texture.PixelFormat;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.util.GLState;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.SparseArray;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:39:33 - 03.04.2010
 */
public class Font implements IFont {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final Rect RECT_TMP = new Rect();
	private static final float[] TEXTWIDTH_CONTAINER_TMP = new float[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private final ITexture mTexture;
	private final int mTextureWidth;
	private final int mTextureHeight;
	private int mCurrentTextureX;
	private int mCurrentTextureY;
	private int mCurrentTextureYHeightMax;

	private final SparseArray<Letter> mManagedCharacterToLetterMap = new SparseArray<Letter>();
	private final ArrayList<Letter> mLettersPendingToBeDrawnToTexture = new ArrayList<Letter>();

	protected final Paint mPaint;
	private final Paint mBackgroundPaint;

	protected final FontMetrics mFontMetrics;
	private final float mAscent;
	private final float mDescent;
	private final float mLeading;
	private final float mLineHeight;

	protected final Canvas mCanvas = new Canvas();

	// ===========================================================
	// Constructors
	// ===========================================================

	public Font(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize) {
		this(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pTypeface, pSize, true);
	}

	public Font(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize, final boolean pAntiAlias) {
		this(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pTypeface, pSize, pAntiAlias, Color.BLACK);
	}

	public Font(final int pTextureWidth, final int pTextureHeight, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		this(pTextureWidth, pTextureHeight, TextureOptions.DEFAULT, pTypeface, pSize, pAntiAlias, pColor);
	}

	public Font(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize) {
		this(new BitmapTextureAtlas(pTextureWidth, pTextureHeight, pTextureOptions), pTypeface, pSize, true);
	}

	public Font(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias) {
		this(new BitmapTextureAtlas(pTextureWidth, pTextureHeight, pTextureOptions), pTypeface, pSize, pAntiAlias, Color.BLACK);
	}

	public Font(final int pTextureWidth, final int pTextureHeight, final TextureOptions pTextureOptions, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		this(new BitmapTextureAtlas(pTextureWidth, pTextureHeight, pTextureOptions), pTypeface, pSize, pAntiAlias, pColor);
	}

	public Font(final ITexture pTexture, final Typeface pTypeface, final float pSize) {
		this(pTexture, pTypeface, pSize, true);
	}

	public Font(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias) {
		this(pTexture, pTypeface, pSize, pAntiAlias, Color.BLACK);
	}

	public Font(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		this.mTexture = pTexture;
		this.mTextureWidth = pTexture.getWidth();
		this.mTextureHeight = pTexture.getHeight();

		this.mPaint = new Paint();
		this.mPaint.setTypeface(pTypeface);
		this.mPaint.setColor(pColor);
		this.mPaint.setTextSize(pSize);
		this.mPaint.setAntiAlias(pAntiAlias);

		this.mBackgroundPaint = new Paint();
		this.mBackgroundPaint.setColor(Color.TRANSPARENT);
		this.mBackgroundPaint.setStyle(Style.FILL);

		this.mFontMetrics = this.mPaint.getFontMetrics();
		this.mAscent = this.mFontMetrics.ascent;
		this.mDescent = this.mFontMetrics.descent;
		this.mLineHeight = -this.mAscent + this.mDescent;
		this.mLeading = this.mFontMetrics.leading;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @return the gap between the lines.
	 */
	public float getLeading() {
		return this.mLeading;
	}

	/**
	 * @return the distance from the baseline to the top, which is usually negative.
	 */
	public float getAscent() {
		return this.mAscent;
	}

	/**
	 * @return the distance from the baseline to the bottom, which is usually positive.
	 */
	public float getDescent() {
		return this.mDescent;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ITexture getTexture() {
		return this.mTexture;
	}

	@Override
	public Font load() {
		this.mTexture.load();
		FontManager.loadFont(this);

		return this;
	}

	@Override
	public Font unload() {
		this.mTexture.unload();
		FontManager.unloadFont(this);

		return this;
	}

	@Override
	public float getLineHeight() {
		return this.mLineHeight;
	}

	@Override
	public synchronized Letter getLetter(final char pCharacter) throws FontException {
		final SparseArray<Letter> managedCharacterToLetterMap = this.mManagedCharacterToLetterMap;
		Letter letter = managedCharacterToLetterMap.get(pCharacter);
		if(letter == null) {
			letter = this.createLetter(pCharacter);

			this.mLettersPendingToBeDrawnToTexture.add(letter);
			managedCharacterToLetterMap.put(pCharacter, letter);
		}
		return letter;
	}

	@Override
	public float getStringWidth(final String pText) {
		this.mPaint.getTextBounds(pText, 0, pText.length(), Font.RECT_TMP);
		return Font.RECT_TMP.width();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized void invalidateLetters() {
		final ArrayList<Letter> lettersPendingToBeDrawnToTexture = this.mLettersPendingToBeDrawnToTexture;
		final SparseArray<Letter> managedCharacterToLetterMap = this.mManagedCharacterToLetterMap;

		/* Make all letters redraw to the texture. */
		for(int i = managedCharacterToLetterMap.size() - 1; i >= 0; i--) {
			lettersPendingToBeDrawnToTexture.add(managedCharacterToLetterMap.valueAt(i));
		}
	}

	private float getLetterAdvance(final String pCharacterAsString) {
		this.mPaint.getTextWidths(pCharacterAsString, Font.TEXTWIDTH_CONTAINER_TMP);
		return Font.TEXTWIDTH_CONTAINER_TMP[0];
	}

	private Bitmap getLetterBitmap(final char pCharacter) {
		final String characterAsString = String.valueOf(pCharacter);

		this.mPaint.getTextBounds(characterAsString, 0, 1, Font.RECT_TMP);
		final int letterLeft = Font.RECT_TMP.left;
		final int letterTop = Font.RECT_TMP.top;
		final int letterWidth = Font.RECT_TMP.width();
		final int letterHeight = Font.RECT_TMP.height();

		final Bitmap bitmap;
		if(letterWidth == 0) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
		} else {
			bitmap = Bitmap.createBitmap(letterWidth, letterHeight, Config.ARGB_8888);
		}
		this.mCanvas.setBitmap(bitmap);

		/* Make background transparent. */
		this.mCanvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), this.mBackgroundPaint);

		/* Actually draw the character. */
		this.mCanvas.drawText(characterAsString, -letterLeft, -letterTop, this.mPaint);

		return bitmap;
	}

	public void prepareLetters(final char... pCharacters) throws FontException {
		for(final char character : pCharacters) {
			this.getLetter(character);
		}
	}

	private Letter createLetter(final char pCharacter) throws FontException {
		final String characterAsString = String.valueOf(pCharacter);

		final float textureWidth = this.mTextureWidth;
		final float textureHeight = this.mTextureHeight;

		this.mPaint.getTextBounds(characterAsString, 0, 1, Font.RECT_TMP);
		final int letterLeft = Font.RECT_TMP.left;
		final int letterTop = Font.RECT_TMP.top;
		final int letterWidth = Font.RECT_TMP.width();
		final int letterHeight = Font.RECT_TMP.height();

		if(this.mCurrentTextureX + letterWidth >= textureWidth) {
			this.mCurrentTextureX = 0;
			this.mCurrentTextureY += this.mCurrentTextureYHeightMax;
			this.mCurrentTextureYHeightMax = 0;
		}

		if(this.mCurrentTextureY + letterHeight >= textureHeight) {
			throw new FontException("Not enough space for Letter: '" + pCharacter + "' on the Texture");
		}

		this.mCurrentTextureYHeightMax = Math.max(letterHeight, this.mCurrentTextureYHeightMax);

		final float u = this.mCurrentTextureX / textureWidth;
		final float v = this.mCurrentTextureY / textureHeight;
		final float u2 = (this.mCurrentTextureX + letterWidth) / textureWidth;
		final float v2 = (this.mCurrentTextureY + letterHeight) / textureHeight;

		final float advance = this.getLetterAdvance(characterAsString);
		final Letter letter = new Letter(pCharacter, this.mCurrentTextureX, this.mCurrentTextureY, letterWidth, letterHeight, letterLeft, letterTop, advance, u, v, u2, v2);
		this.mCurrentTextureX += letterWidth;

		return letter;
	}

	public synchronized void update() {
		final ArrayList<Letter> lettersPendingToBeDrawnToTexture = this.mLettersPendingToBeDrawnToTexture;
		if(lettersPendingToBeDrawnToTexture.size() > 0) {
			this.mTexture.bind();

			for(int i = lettersPendingToBeDrawnToTexture.size() - 1; i >= 0; i--) {
				final Letter letter = lettersPendingToBeDrawnToTexture.get(i);
				final Bitmap bitmap = this.getLetterBitmap(letter.mCharacter);

				// TODO What about premultiplyalpha of the textureOptions?
				if(this.mTexture.getTextureOptions().mPreMultipyAlpha) {
				}
				final boolean preMultipyAlpha = this.mTexture.getTextureOptions().mPreMultipyAlpha;
				if(preMultipyAlpha) {
					GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, letter.mTextureX, letter.mTextureY, bitmap);
				} else {
					GLState.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, letter.mTextureX, letter.mTextureY, bitmap, PixelFormat.RGBA_8888);
				}

				bitmap.recycle();
			}
			lettersPendingToBeDrawnToTexture.clear();
			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}