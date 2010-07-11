package org.anddev.andengine.opengl.font;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.util.GLHelper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.opengl.GLUtils;
import android.util.FloatMath;

/**
 * @author Nicolas Gramlich
 * @since 10:39:33 - 03.04.2010
 */
public class Font {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Texture mTexture;
	private float mTextureWidth;
	private float mTextureHeight;
	private int mCurrentTextureX = 0;
	private int mCurrentTextureY = 0;

	private final HashMap<Character, Letter> mCharacterToLetterMap = new HashMap<Character, Letter>();
	private final HashMap<Letter, Bitmap> mLettersPendingToBeDrawnToTexture = new HashMap<Letter, Bitmap>();

	private final Paint mPaint;
	private final Paint mBackgroundPaint;

	private final Typeface mTypeface;
	private final FontMetrics mFontMetrics;
	private final int mLineHeight;
	private final int mLineGap;

	private final Size mCreateLetterTemporarySize = new Size();
	private final Rect mGetLetterBitmapTemporaryRect = new Rect();
	private final Rect mGetStringWidthTemporaryRect = new Rect();
	private final Rect mGetLetterBoundsTemporaryRect = new Rect();
	private final float[] mTemporaryTextWidthFetchers = new float[1];

	private final Canvas mCanvas = new Canvas();

	// ===========================================================
	// Constructors
	// ===========================================================

	public Font(final Texture pTexture, final Typeface pTypeFace, final float pSize, final boolean pAntiAlias, final int pColor) {
		this.mTexture = pTexture;
		this.mTextureWidth = pTexture.getWidth();
		this.mTextureHeight = pTexture.getHeight();
		
		this.mTypeface = pTypeFace;

		this.mPaint = new Paint();
		this.mPaint.setTypeface(this.mTypeface);
		this.mPaint.setColor(pColor);
		this.mPaint.setTextSize(pSize);
		this.mPaint.setAntiAlias(pAntiAlias);

		this.mBackgroundPaint = new Paint();
		this.mBackgroundPaint.setColor(Color.TRANSPARENT);
		this.mBackgroundPaint.setStyle(Style.FILL);

		this.mFontMetrics = this.mPaint.getFontMetrics();
		this.mLineHeight = (int) FloatMath.ceil(Math.abs(this.mFontMetrics.ascent) + Math.abs(this.mFontMetrics.descent));
		this.mLineGap = (int) (FloatMath.ceil(this.mFontMetrics.leading));
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

	private int getLetterAdvance(final char pCharacter) {
		this.mPaint.getTextWidths(String.valueOf(pCharacter), this.mTemporaryTextWidthFetchers);
		return (int) (FloatMath.ceil(this.mTemporaryTextWidthFetchers[0]));
	}

	private Bitmap getLetterBitmap(final char pCharacter) {
		final Rect getLetterBitmapTemporaryRect = this.mGetLetterBitmapTemporaryRect;
		final String characterAsString = String.valueOf(pCharacter);
		this.mPaint.getTextBounds(characterAsString, 0, 1, getLetterBitmapTemporaryRect);

		final int lineHeight = this.getLineHeight();
		final Bitmap bitmap = Bitmap.createBitmap(getLetterBitmapTemporaryRect.width() == 0 ? 1 : getLetterBitmapTemporaryRect.width() + 5, lineHeight, Bitmap.Config.ARGB_8888);
		this.mCanvas.setBitmap(bitmap);

		/* Make background transparent. */
		this.mCanvas.drawRect(0, 0, getLetterBitmapTemporaryRect.width() + 5, lineHeight, this.mBackgroundPaint);

		this.mCanvas.drawText(characterAsString, 0, -this.mFontMetrics.ascent, this.mPaint);

		// TODO Canvas cannot be cleared, but the bitmap is recycled once it is loaded, so this should be no problem.
		// this.mCanvas.setBitmap(null);
		
		return bitmap;
	}

	public int getLineGap() {
		return this.mLineGap;
	}

	public int getLineHeight() {
		return this.mLineHeight;
	}

	public int getStringWidth(final String pText) {
		this.mPaint.getTextBounds(pText, 0, pText.length(), this.mGetStringWidthTemporaryRect);
		return this.mGetStringWidthTemporaryRect.width();
	}

	private void getLetterBounds(final char pCharacter, final Size pSize) {
		this.mPaint.getTextBounds(String.valueOf(pCharacter), 0, 1, this.mGetLetterBoundsTemporaryRect);
		pSize.set(this.mGetLetterBoundsTemporaryRect.width() + 5, this.getLineHeight());
	}

	public Texture getTexture() {
		return this.mTexture;
	}

	public Letter getLetter(final char pCharacter) {
		final HashMap<Character, Letter> characterToLetterMap = this.mCharacterToLetterMap;
		Letter letter = characterToLetterMap.get(pCharacter);
		if (letter == null) {
			letter = this.createLetter(pCharacter);
			characterToLetterMap.put(pCharacter, letter);
		}
		return letter;
	}

	private Letter createLetter(final char pCharacter) {
		final float textureWidth = this.mTextureWidth;
		final float textureHeight = this.mTextureHeight;
		
		final Size createLetterTemporarySize = this.mCreateLetterTemporarySize;

		final Bitmap bitmap = this.getLetterBitmap(pCharacter);
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

		final Letter letter = new Letter(this.getLetterAdvance(pCharacter), (int)letterWidth, (int)letterHeight, letterTextureX, letterTextureY, letterTextureWidth, letterTextureHeight);
		this.mCurrentTextureX += letterWidth;

		this.mLettersPendingToBeDrawnToTexture.put(letter, bitmap);

		return letter;
	}

	public void update(final GL10 pGL) {
		final HashMap<Letter, Bitmap> lettersPendingToBeDrawnToTexture = this.mLettersPendingToBeDrawnToTexture;
		if(lettersPendingToBeDrawnToTexture.size() > 0) {
			final int hardwareTextureID = this.mTexture.getHardwareTextureID();
			
			final float textureWidth = this.mTextureWidth;
			final float textureHeight = this.mTextureHeight;

			// TODO Can the use of this iterator be avoided somehow?
			for (final Entry<Letter, Bitmap> entry : lettersPendingToBeDrawnToTexture.entrySet()) {
				final Letter letter = entry.getKey();
				final Bitmap bitmap = entry.getValue();

				GLHelper.bindTexture(pGL, hardwareTextureID);
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
