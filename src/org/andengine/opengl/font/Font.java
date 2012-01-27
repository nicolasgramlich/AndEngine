package org.andengine.opengl.font;

import java.util.ArrayList;

import org.andengine.opengl.font.exception.FontException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.util.GLState;
import org.andengine.util.math.MathUtils;

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

	protected static final int LETTER_TEXTURE_PADDING = 1;

	protected static final Rect TEXTBOUNDS_TMP = new Rect();
	protected static final float[] TEXTWIDTH_CONTAINER_TMP = new float[1];

	// ===========================================================
	// Fields
	// ===========================================================

	private final ITexture mTexture;
	private final int mTextureWidth;
	private final int mTextureHeight;
	private int mCurrentTextureX = Font.LETTER_TEXTURE_PADDING;
	private int mCurrentTextureY = Font.LETTER_TEXTURE_PADDING;
	private int mCurrentTextureYHeightMax;

	private final SparseArray<Letter> mManagedCharacterToLetterMap = new SparseArray<Letter>();
	private final ArrayList<Letter> mLettersPendingToBeDrawnToTexture = new ArrayList<Letter>();

	protected final Paint mPaint;
	private final Paint mBackgroundPaint;

	protected final FontMetrics mFontMetrics;

	protected final Canvas mCanvas = new Canvas();

	// ===========================================================
	// Constructors
	// ===========================================================

	public Font(final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColor) {
		this.mTexture = pTexture;
		this.mTextureWidth = pTexture.getWidth();
		this.mTextureHeight = pTexture.getHeight();

		this.mBackgroundPaint = new Paint();
		this.mBackgroundPaint.setColor(Color.TRANSPARENT);
		this.mBackgroundPaint.setStyle(Style.FILL);

		this.mPaint = new Paint();
		this.mPaint.setTypeface(pTypeface);
		this.mPaint.setColor(pColor);
		this.mPaint.setTextSize(pSize);
		this.mPaint.setAntiAlias(pAntiAlias);

		this.mFontMetrics = this.mPaint.getFontMetrics();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @return the gap between the lines.
	 */
	public float getLeading() {
		return this.mFontMetrics.leading;
	}

	/**
	 * @return the distance from the baseline to the top, which is usually negative.
	 */
	public float getAscent() {
		return this.mFontMetrics.ascent;
	}

	/**
	 * @return the distance from the baseline to the bottom, which is usually positive.
	 */
	public float getDescent() {
		return this.mFontMetrics.descent;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public ITexture getTexture() {
		return this.mTexture;
	}

	@Override
	public Font load(final TextureManager pTextureManager, final FontManager pFontManager) {
		this.mTexture.load(pTextureManager);
		pFontManager.loadFont(this);

		return this;
	}

	@Override
	public Font unload(final TextureManager pTextureManager, final FontManager pFontManager) {
		this.mTexture.unload(pTextureManager);
		pFontManager.unloadFont(this);

		return this;
	}

	@Override
	public float getLineHeight() {
		return -this.getAscent() + this.getDescent();
	}

	@Override
	public synchronized Letter getLetter(final char pCharacter) throws FontException {
		Letter letter = this.mManagedCharacterToLetterMap.get(pCharacter);
		if(letter == null) {
			letter = this.createLetter(pCharacter);

			this.mLettersPendingToBeDrawnToTexture.add(letter);
			this.mManagedCharacterToLetterMap.put(pCharacter, letter);
		}
		return letter;
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

	protected Bitmap getLetterBitmap(final char pCharacter) throws IllegalArgumentException {
		final String characterAsString = String.valueOf(pCharacter);

		this.updateTextBounds(characterAsString);

		final int letterLeft = Font.TEXTBOUNDS_TMP.left;
		final int letterTop = Font.TEXTBOUNDS_TMP.top;
		final int letterWidth = Font.TEXTBOUNDS_TMP.width() + 2 * Font.LETTER_TEXTURE_PADDING;
		final int letterHeight = Font.TEXTBOUNDS_TMP.height() + 2 * Font.LETTER_TEXTURE_PADDING;

		if(letterWidth == 0 || letterHeight == 0) {
			throw new IllegalArgumentException("Character '" + pCharacter + "' cannot be drawn, because it has no extent (width='" + letterWidth + "', height='" + letterHeight + "')");
		}
		final Bitmap bitmap = Bitmap.createBitmap(letterWidth, letterHeight, Config.ARGB_8888);
		this.mCanvas.setBitmap(bitmap);

		/* Make background transparent. */
		this.mCanvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), this.mBackgroundPaint);

		/* Actually draw the character. */
		this.drawLetter(characterAsString, -letterLeft, -letterTop);

		return bitmap;
	}

	protected void drawLetter(final String pCharacterAsString, final int pLeft, final int pTop) {
		this.mCanvas.drawText(pCharacterAsString, pLeft + Font.LETTER_TEXTURE_PADDING, pTop + Font.LETTER_TEXTURE_PADDING, this.mPaint);
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

		this.updateTextBounds(characterAsString);
		final int letterLeft = Font.TEXTBOUNDS_TMP.left;
		final int letterTop = Font.TEXTBOUNDS_TMP.top;
		final int letterWidth = Font.TEXTBOUNDS_TMP.width();
		final int letterHeight = Font.TEXTBOUNDS_TMP.height();

		if(this.mCurrentTextureX + Font.LETTER_TEXTURE_PADDING + letterWidth >= textureWidth) {
			this.mCurrentTextureX = 0;
			this.mCurrentTextureY += this.mCurrentTextureYHeightMax + 2 * Font.LETTER_TEXTURE_PADDING;
			this.mCurrentTextureYHeightMax = 0;
		}

		if(this.mCurrentTextureY + letterHeight >= textureHeight) {
			throw new FontException("Not enough space for Letter: '" + pCharacter + "' on the Texture");
		}

		this.mCurrentTextureYHeightMax = Math.max(letterHeight, this.mCurrentTextureYHeightMax);

		this.mCurrentTextureX += Font.LETTER_TEXTURE_PADDING;

		final float u = this.mCurrentTextureX / textureWidth;
		final float v = this.mCurrentTextureY / textureHeight;
		final float u2 = (this.mCurrentTextureX + letterWidth) / textureWidth;
		final float v2 = (this.mCurrentTextureY + letterHeight) / textureHeight;

		final float advance = this.getLetterAdvance(characterAsString);
		final Letter letter = new Letter(pCharacter, this.mCurrentTextureX - Font.LETTER_TEXTURE_PADDING, this.mCurrentTextureY - Font.LETTER_TEXTURE_PADDING, letterWidth, letterHeight, letterLeft, letterTop - this.getAscent(), advance, u, v, u2, v2);
		this.mCurrentTextureX += letterWidth + Font.LETTER_TEXTURE_PADDING;

		return letter;
	}

	protected void updateTextBounds(final String pCharacterAsString) {
		this.mPaint.getTextBounds(pCharacterAsString, 0, 1, Font.TEXTBOUNDS_TMP);
	}

	public synchronized void update(final GLState pGLState) {
		final ArrayList<Letter> lettersPendingToBeDrawnToTexture = this.mLettersPendingToBeDrawnToTexture;
		if(lettersPendingToBeDrawnToTexture.size() > 0) {
			this.mTexture.bind(pGLState);
			final PixelFormat pixelFormat = this.mTexture.getPixelFormat();

			final boolean preMultipyAlpha = this.mTexture.getTextureOptions().mPreMultipyAlpha;
			for(int i = lettersPendingToBeDrawnToTexture.size() - 1; i >= 0; i--) {
				final Letter letter = lettersPendingToBeDrawnToTexture.get(i);
				final Bitmap bitmap = this.getLetterBitmap(letter.mCharacter);

				final boolean useDefaultAlignment = MathUtils.isPowerOfTwo(bitmap.getWidth()) && MathUtils.isPowerOfTwo(bitmap.getHeight()) && pixelFormat == PixelFormat.RGBA_8888;
				if(!useDefaultAlignment) {
					GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
				}

				if(preMultipyAlpha) {
					GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, letter.mTextureX, letter.mTextureY, bitmap);
				} else {
					pGLState.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, letter.mTextureX, letter.mTextureY, bitmap, pixelFormat);
				}

				/* Restore default alignment. */
				if(!useDefaultAlignment) {
					GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLState.GL_UNPACK_ALIGNMENT_DEFAULT);
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