package org.anddev.andengine.opengl.font;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.ITexture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLUtils;
import android.util.FloatMath;
import android.util.SparseArray;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:39:33 - 03.04.2010
 */
public class Font {
	// ===========================================================
	// Constants
	// ===========================================================

	protected static final float LETTER_LEFT_OFFSET = 0;
	protected static final int LETTER_EXTRA_WIDTH = 10;

	protected final static int PADDING = 1; 

	// ===========================================================
	// Fields
	// ===========================================================

	private final ITexture mTexture;
	private final float mTextureWidth;
	private final float mTextureHeight;
	private int mCurrentTextureX = 0;
	private int mCurrentTextureY = 0;

	private final SparseArray<Letter> mManagedCharacterToLetterMap = new SparseArray<Letter>();
	private final ArrayList<Letter> mLettersPendingToBeDrawnToTexture = new ArrayList<Letter>();

	protected final Paint mPaint;
	private final Paint mBackgroundPaint;

	protected final FontMetrics mFontMetrics;
	private final int mLineHeight;
	private final int mLineGap;

	private final Size mCreateLetterTemporarySize = new Size();
	private final Rect mGetLetterBitmapTemporaryRect = new Rect();
	private final Rect mGetStringWidthTemporaryRect = new Rect();
	private final Rect mGetLetterBoundsTemporaryRect = new Rect();
	private final float[] mTemporaryTextWidthFetchers = new float[1];

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
		this.mLineHeight = (int) FloatMath.ceil(Math.abs(this.mFontMetrics.ascent) + Math.abs(this.mFontMetrics.descent)) + (PADDING * 2);
		this.mLineGap = (int) (FloatMath.ceil(this.mFontMetrics.leading));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getLineGap() {
		return this.mLineGap;
	}

	public int getLineHeight() {
		return this.mLineHeight;
	}

	public ITexture getTexture() {
		return this.mTexture;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public synchronized void reload() {
		final ArrayList<Letter> lettersPendingToBeDrawnToTexture = this.mLettersPendingToBeDrawnToTexture;
		final SparseArray<Letter> managedCharacterToLetterMap = this.mManagedCharacterToLetterMap;

		/* Make all letters redraw to the texture. */
		for(int i = managedCharacterToLetterMap.size() - 1; i >= 0; i--) {
			lettersPendingToBeDrawnToTexture.add(managedCharacterToLetterMap.valueAt(i));
		}
	}

	private int getLetterAdvance(final char pCharacter) {
		this.mPaint.getTextWidths(String.valueOf(pCharacter), this.mTemporaryTextWidthFetchers);
		return (int) (FloatMath.ceil(this.mTemporaryTextWidthFetchers[0]));
	}
	
	private Bitmap getLetterBitmap(final char pCharacter) {
		final Rect getLetterBitmapTemporaryRect = this.mGetLetterBitmapTemporaryRect;
		final String characterAsString = String.valueOf(pCharacter);
		this.mPaint.getTextBounds(characterAsString, 0, 1, getLetterBitmapTemporaryRect);
		
		getLetterBitmapTemporaryRect.right += PADDING * 2;

		final int lineHeight = this.getLineHeight();
		final Bitmap bitmap = Bitmap.createBitmap(getLetterBitmapTemporaryRect.width() == 0 ? 1 + (2 * PADDING) : getLetterBitmapTemporaryRect.width() + LETTER_EXTRA_WIDTH, lineHeight, Bitmap.Config.ARGB_8888);
		this.mCanvas.setBitmap(bitmap);

		/* Make background transparent. */
		this.mCanvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), this.mBackgroundPaint);

		/* Actually draw the character. */
		this.drawCharacterString(characterAsString);

		return bitmap;
	}

	protected void drawCharacterString(final String pCharacterAsString) {
		this.mCanvas.drawText(pCharacterAsString, LETTER_LEFT_OFFSET + PADDING, -this.mFontMetrics.ascent + PADDING, this.mPaint);
	}

	public int getStringWidth(final String pText) {
		this.mPaint.getTextBounds(pText, 0, pText.length(), this.mGetStringWidthTemporaryRect);
		return this.mGetStringWidthTemporaryRect.width();
	}

	private void getLetterBounds(final char pCharacter, final Size pSize) {
		this.mPaint.getTextBounds(String.valueOf(pCharacter), 0, 1, this.mGetLetterBoundsTemporaryRect);
		pSize.set(this.mGetLetterBoundsTemporaryRect.width() + LETTER_EXTRA_WIDTH + (2 * PADDING), this.getLineHeight());
	}

	public void prepareLetters(final char ... pCharacters) {
		for(final char character : pCharacters) {
			this.getLetter(character);
		}
	}

	public synchronized Letter getLetter(final char pCharacter) {
		final SparseArray<Letter> managedCharacterToLetterMap = this.mManagedCharacterToLetterMap;
		Letter letter = managedCharacterToLetterMap.get(pCharacter);
		if (letter == null) {
			letter = this.createLetter(pCharacter);

			this.mLettersPendingToBeDrawnToTexture.add(letter);
			managedCharacterToLetterMap.put(pCharacter, letter);
		}
		return letter;
	}

	private Letter createLetter(final char pCharacter) {
		final float textureWidth = this.mTextureWidth;
		final float textureHeight = this.mTextureHeight;

		final Size createLetterTemporarySize = this.mCreateLetterTemporarySize;
		this.getLetterBounds(pCharacter, createLetterTemporarySize);

		final float letterWidth = createLetterTemporarySize.getWidth();
		final float letterHeight = createLetterTemporarySize.getHeight();

		if (this.mCurrentTextureX + letterWidth >= textureWidth) {
			this.mCurrentTextureX = 0;
			this.mCurrentTextureY += this.getLineGap() + this.getLineHeight();
		}

		final float letterTextureX = this.mCurrentTextureX / textureWidth;
		final float letterTextureY = this.mCurrentTextureY / textureHeight;
		final float letterTextureWidth = letterWidth / textureWidth;
		final float letterTextureHeight = letterHeight / textureHeight;

		final Letter letter = new Letter(pCharacter, this.getLetterAdvance(pCharacter), (int)letterWidth, (int)letterHeight, letterTextureX, letterTextureY, letterTextureWidth, letterTextureHeight);
		this.mCurrentTextureX += letterWidth;

		return letter;
	}

	public synchronized void update(final GL10 pGL) {
		final ArrayList<Letter> lettersPendingToBeDrawnToTexture = this.mLettersPendingToBeDrawnToTexture;
		if(lettersPendingToBeDrawnToTexture.size() > 0) {
			this.mTexture.bind(pGL);

			final float textureWidth = this.mTextureWidth;
			final float textureHeight = this.mTextureHeight;

			for(int i = lettersPendingToBeDrawnToTexture.size() - 1; i >= 0; i--) {
				final Letter letter = lettersPendingToBeDrawnToTexture.get(i);
				final Bitmap bitmap = this.getLetterBitmap(letter.mCharacter);

				// TODO What about premultiplyalpha of the textureOptions?
				GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, (int)(letter.mTextureX * textureWidth), (int)(letter.mTextureY * textureHeight), bitmap);

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