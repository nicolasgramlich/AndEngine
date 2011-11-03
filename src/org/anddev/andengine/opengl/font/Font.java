package org.anddev.andengine.opengl.font;

import java.util.ArrayList;

import org.anddev.andengine.opengl.font.exception.FontException;
import org.anddev.andengine.opengl.texture.ITexture;

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
	private int mCurrentTextureX = 0;
	private int mCurrentTextureY = 0;

	private final SparseArray<Letter> mManagedCharacterToLetterMap = new SparseArray<Letter>();
	private final ArrayList<Letter> mLettersPendingToBeDrawnToTexture = new ArrayList<Letter>();

	protected final Paint mPaint;
	private final Paint mBackgroundPaint;

	protected final FontMetrics mFontMetrics;
	private final float mHeight;
	private final float mLineGap;


	protected final Canvas mCanvas = new Canvas();

	// ===========================================================
	// Constructors
	// ===========================================================

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
		this.mHeight = Math.abs(this.mFontMetrics.ascent) + Math.abs(this.mFontMetrics.descent);
		this.mLineGap = this.mFontMetrics.leading;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public float getLeading() {
		return this.mLineGap;
	}
	
	public float getHeight() {
		return this.mHeight;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getLineHeight() {
		return this.mLineGap + this.mHeight;
	}

	@Override
	public ITexture getTexture() {
		return this.mTexture;
	}

	@Override
	public synchronized Letter getLetter(final char pCharacter) throws FontException {
		final SparseArray<Letter> managedCharacterToLetterMap = this.mManagedCharacterToLetterMap;
		Letter letter = managedCharacterToLetterMap.get(pCharacter);
		if (letter == null) {
			letter = this.createLetter(pCharacter);

			this.mLettersPendingToBeDrawnToTexture.add(letter);
			managedCharacterToLetterMap.put(pCharacter, letter);
		}
		return letter;
	}

	@Override
	public int getStringWidth(final String pText) {
		this.mPaint.getTextBounds(pText, 0, pText.length(), RECT_TMP);
		return RECT_TMP.width();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void load() {
		FontManager.loadFont(this);
	}

	public synchronized void reloadLetters() {
		final ArrayList<Letter> lettersPendingToBeDrawnToTexture = this.mLettersPendingToBeDrawnToTexture;
		final SparseArray<Letter> managedCharacterToLetterMap = this.mManagedCharacterToLetterMap;

		/* Make all letters redraw to the texture. */
		for(int i = managedCharacterToLetterMap.size() - 1; i >= 0; i--) {
			lettersPendingToBeDrawnToTexture.add(managedCharacterToLetterMap.valueAt(i));
		}
	}

	private float getLetterAdvance(final char pCharacter) {
		this.mPaint.getTextWidths(String.valueOf(pCharacter), TEXTWIDTH_CONTAINER_TMP);
		return TEXTWIDTH_CONTAINER_TMP[0];
	}

	private Bitmap getLetterBitmap(final char pCharacter) {
		final String characterAsString = String.valueOf(pCharacter);

		this.mPaint.getTextBounds(characterAsString, 0, 1, RECT_TMP);
		final int letterWidth = RECT_TMP.width();
		final int letterHeight = RECT_TMP.height();

		final Bitmap bitmap;
		if(letterWidth == 0) {
			bitmap = Bitmap.createBitmap(1, letterHeight, Config.ARGB_8888);
		} else {
			bitmap = Bitmap.createBitmap(letterWidth, letterHeight, Config.ARGB_8888);
		}
		this.mCanvas.setBitmap(bitmap);

		/* Make background transparent. */
		this.mCanvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), this.mBackgroundPaint);

		/* Actually draw the character. */
		this.drawCharacterString(characterAsString);

		return bitmap;
	}

	protected void drawCharacterString(final String pCharacterAsString) {
		this.mCanvas.drawText(pCharacterAsString, 0, -this.mFontMetrics.ascent, this.mPaint);
	}

	public void prepareLetters(final char ... pCharacters) throws FontException {
		for(final char character : pCharacters) {
			this.getLetter(character);
		}
	}

	private Letter createLetter(final char pCharacter) throws FontException {
		final float textureWidth = this.mTextureWidth;
		final float textureHeight = this.mTextureHeight;

		this.mPaint.getTextBounds(String.valueOf(pCharacter), 0, 1, RECT_TMP);
		final int letterWidth = RECT_TMP.width();
		final int letterHeight = RECT_TMP.height();

		if (this.mCurrentTextureX + letterWidth >= textureWidth) {
			this.mCurrentTextureX = 0;
			this.mCurrentTextureY += this.mLineGap + this.mHeight; // TODO Are we wasting space here?
		}

		if(this.mCurrentTextureY + letterHeight >= textureHeight) {
			throw new FontException("Not enough space for Letter: '" + pCharacter + "' on the Texture");
		}

		final float u = this.mCurrentTextureX / textureWidth;
		final float v = this.mCurrentTextureY / textureHeight;
		final float u2 = (this.mCurrentTextureX + letterWidth) / textureWidth;
		final float v2 = (this.mCurrentTextureY + letterHeight) / textureHeight;

		final float advance = this.getLetterAdvance(pCharacter);
		final Letter letter = new Letter(pCharacter, this.mCurrentTextureX, this.mCurrentTextureY, letterWidth, letterHeight, advance, 0, 0, u, v, u2, v2);
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
				GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, letter.mTextureX, letter.mTextureY, bitmap);

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