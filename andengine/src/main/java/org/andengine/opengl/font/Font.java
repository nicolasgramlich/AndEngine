package org.andengine.opengl.font;

import java.util.ArrayList;

import org.andengine.opengl.font.exception.FontException;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.map.SparseArrayUtils;
import org.andengine.util.color.Color;
import org.andengine.util.math.MathUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
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

	// ===========================================================
	// Fields
	// ===========================================================

	private final FontManager mFontManager;

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
	protected final Rect mTextBounds = new Rect();
	protected final float[] mTextWidthContainer = new float[1];

	// ===========================================================
	// Constructors
	// ===========================================================

	public Font(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final Color pColor) {
		this(pFontManager, pTexture, pTypeface, pSize, pAntiAlias, pColor.getARGBPackedInt());
	}

	public Font(final FontManager pFontManager, final ITexture pTexture, final Typeface pTypeface, final float pSize, final boolean pAntiAlias, final int pColorARGBPackedInt) {
		this.mFontManager = pFontManager;
		this.mTexture = pTexture;
		this.mTextureWidth = pTexture.getWidth();
		this.mTextureHeight = pTexture.getHeight();

		this.mBackgroundPaint = new Paint();
		this.mBackgroundPaint.setColor(Color.TRANSPARENT_ARGB_PACKED_INT);
		this.mBackgroundPaint.setStyle(Style.FILL);

		this.mPaint = new Paint();
		this.mPaint.setTypeface(pTypeface);
		this.mPaint.setColor(pColorARGBPackedInt);
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
	public void load() {
		this.mTexture.load();
		this.mFontManager.loadFont(this);
	}

	@Override
	public void unload() {
		this.mTexture.unload();
		this.mFontManager.unloadFont(this);
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
		this.mPaint.getTextWidths(pCharacterAsString, this.mTextWidthContainer);
		return this.mTextWidthContainer[0];
	}

	protected Bitmap getLetterBitmap(final Letter pLetter) throws FontException {
		final char character = pLetter.mCharacter;
		final String characterAsString = String.valueOf(character);

		final int width = pLetter.mWidth + (2 * Font.LETTER_TEXTURE_PADDING);
		final int height = pLetter.mHeight + (2 * Font.LETTER_TEXTURE_PADDING);

		final Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		this.mCanvas.setBitmap(bitmap);

		/* Make background transparent. */
		this.mCanvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), this.mBackgroundPaint);

		/* Actually draw the character. */
		final float drawLetterLeft = -pLetter.mOffsetX;
		final float drawLetterTop = -(pLetter.mOffsetY + this.getAscent());

		this.drawLetter(characterAsString, drawLetterLeft, drawLetterTop);

		return bitmap;
	}

	protected void drawLetter(final String pCharacterAsString, final float pLeft, final float pTop) {
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
		final int letterLeft = this.mTextBounds.left;
		final int letterTop = this.mTextBounds.top;
		final int letterWidth = this.mTextBounds.width();
		final int letterHeight = this.mTextBounds.height();

		final Letter letter;

		final float advance = this.getLetterAdvance(characterAsString);

		final boolean whitespace = Character.isWhitespace(pCharacter) || (letterWidth == 0) || (letterHeight == 0);
		if(whitespace) {
			letter = new Letter(pCharacter, advance);
		} else {
			if((this.mCurrentTextureX + Font.LETTER_TEXTURE_PADDING + letterWidth) >= textureWidth) {
				this.mCurrentTextureX = 0;
				this.mCurrentTextureY += this.mCurrentTextureYHeightMax + (2 * Font.LETTER_TEXTURE_PADDING);
				this.mCurrentTextureYHeightMax = 0;
			}

			if((this.mCurrentTextureY + letterHeight) >= textureHeight) {
				throw new FontException("Not enough space for " + Letter.class.getSimpleName() + ": '" + pCharacter + "' on the " + this.mTexture.getClass().getSimpleName() + ". Existing Letters: " + SparseArrayUtils.toString(this.mManagedCharacterToLetterMap));
			}

			this.mCurrentTextureYHeightMax = Math.max(letterHeight, this.mCurrentTextureYHeightMax);

			this.mCurrentTextureX += Font.LETTER_TEXTURE_PADDING;

			final float u = this.mCurrentTextureX / textureWidth;
			final float v = this.mCurrentTextureY / textureHeight;
			final float u2 = (this.mCurrentTextureX + letterWidth) / textureWidth;
			final float v2 = (this.mCurrentTextureY + letterHeight) / textureHeight;

			letter = new Letter(pCharacter, this.mCurrentTextureX - Font.LETTER_TEXTURE_PADDING, this.mCurrentTextureY - Font.LETTER_TEXTURE_PADDING, letterWidth, letterHeight, letterLeft, letterTop - this.getAscent(), advance, u, v, u2, v2);
			this.mCurrentTextureX += letterWidth + Font.LETTER_TEXTURE_PADDING;
		}

		return letter;
	}

	protected void updateTextBounds(final String pCharacterAsString) {
		this.mPaint.getTextBounds(pCharacterAsString, 0, 1, this.mTextBounds);
	}

	public synchronized void update(final GLState pGLState) {
		if(this.mTexture.isLoadedToHardware()) {
			final ArrayList<Letter> lettersPendingToBeDrawnToTexture = this.mLettersPendingToBeDrawnToTexture;
			if(lettersPendingToBeDrawnToTexture.size() > 0) {
				this.mTexture.bind(pGLState);
				final PixelFormat pixelFormat = this.mTexture.getPixelFormat();

				final boolean preMultipyAlpha = this.mTexture.getTextureOptions().mPreMultiplyAlpha;
				for(int i = lettersPendingToBeDrawnToTexture.size() - 1; i >= 0; i--) {
					final Letter letter = lettersPendingToBeDrawnToTexture.get(i);
					if(!letter.isWhitespace()) {
						final Bitmap bitmap = this.getLetterBitmap(letter);

						final boolean useDefaultAlignment = MathUtils.isPowerOfTwo(bitmap.getWidth()) && MathUtils.isPowerOfTwo(bitmap.getHeight()) && (pixelFormat == PixelFormat.RGBA_8888);
						if(!useDefaultAlignment) {
							/* Adjust unpack alignment. */
							GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
						}

						if(preMultipyAlpha) {
							GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, letter.mTextureX, letter.mTextureY, bitmap);
						} else {
							pGLState.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, letter.mTextureX, letter.mTextureY, bitmap, pixelFormat);
						}

						if(!useDefaultAlignment) {
							/* Restore default unpack alignment. */
							GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, GLState.GL_UNPACK_ALIGNMENT_DEFAULT);
						}

						bitmap.recycle();
					}
				}
				lettersPendingToBeDrawnToTexture.clear();

				System.gc();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}