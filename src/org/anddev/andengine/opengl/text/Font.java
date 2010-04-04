package org.anddev.andengine.opengl.text;

import java.util.HashMap;
import java.util.Map.Entry;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.Texture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.opengl.GLUtils;

/**
 * @author Nicolas Gramlich
 * @since 10:39:33 - 03.04.2010
 */
public class Font {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final float TEXTURE_SIZE = 256.0f;

	// ===========================================================
	// Fields
	// ===========================================================

	private final HashMap<Character, Letter> mCharacterToLetterMap = new HashMap<Character, Letter>();
	private final HashMap<Letter, Bitmap> mLettersPendingToBeDrawnToTexture = new HashMap<Letter, Bitmap>();

	private final Typeface mTypeface;
	private final Paint mPaint;
	private final Paint mBackgroundPaint;
	private final FontMetrics mFontMetrics;
	private final Texture mTexture;

	private int mCurrentTextureX = 0;
	private int mCurrentTextureY = 0;


	private final Size mCreateLetterTemporarySize = new Size();
	private final Rect mGetLetterBitmapTemporaryRect = new Rect();
	private final Rect mGetStringWidthTemporaryRect = new Rect();
	private final Rect mGetLetterBoundsTemporaryRect = new Rect();
	private final float[] mTemporaryTextWidthFetchers = new float[1];

	// ===========================================================
	// Constructors
	// ===========================================================

	public Font(final Texture pTexture, final Typeface pTypeFace, final float pSize, final boolean pAntiAlias, final int pColor) {
		this.mTexture = pTexture;
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
		this.mPaint.getTextWidths("" + pCharacter, this.mTemporaryTextWidthFetchers);
		return (int) (Math.ceil(this.mTemporaryTextWidthFetchers[0]));
	}

	private Bitmap getLetterBitmap(final char pCharacter) {		
		final String characterAsString = "" + pCharacter;
		this.mPaint.getTextBounds(characterAsString, 0, 1, this.mGetLetterBitmapTemporaryRect);

		final Bitmap bitmap = Bitmap.createBitmap(this.mGetLetterBitmapTemporaryRect.width() == 0 ? 1 : this.mGetLetterBitmapTemporaryRect.width() + 5, this.getLineHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);

		/* Make background transparent. */
		canvas.drawRect(0, 0, this.mGetLetterBitmapTemporaryRect.width() + 5, this.getLineHeight(), this.mBackgroundPaint);

		canvas.drawText(characterAsString, 0, -this.mFontMetrics.ascent, this.mPaint);
		return bitmap;
	}

	public int getLineGap() {
		return (int) (Math.ceil(this.mFontMetrics.leading));
	}

	public int getLineHeight() {
		return (int) Math.ceil(Math.abs(this.mFontMetrics.ascent) + Math.abs(this.mFontMetrics.descent));
	}

	public int getStringWidth(final String pText) {
		this.mPaint.getTextBounds(pText, 0, pText.length(), this.mGetStringWidthTemporaryRect);
		return this.mGetStringWidthTemporaryRect.width();
	}

	private void getLetterBounds(final char pCharacter, final Size pSize) {
		this.mPaint.getTextBounds("" + pCharacter, 0, 1, this.mGetLetterBoundsTemporaryRect);
		pSize.mWidth = this.mGetLetterBoundsTemporaryRect.width() + 5;
		pSize.mHeight = this.getLineHeight();
	}

	public Texture getTexture() {
		return this.mTexture;
	}

	public Letter getLetter(final char pCharacter) {
		Letter letter = this.mCharacterToLetterMap.get(pCharacter);
		if (letter == null) {
			letter = this.createLetter(pCharacter);
			this.mCharacterToLetterMap.put(pCharacter, letter);
		}
		return letter;
	}

	private Letter createLetter(final char pCharacter) {
		final Bitmap bitmap = this.getLetterBitmap(pCharacter);
		this.getLetterBounds(pCharacter, this.mCreateLetterTemporarySize);

		if (this.mCurrentTextureX + mCreateLetterTemporarySize.mWidth >= TEXTURE_SIZE) {
			this.mCurrentTextureX = 0;
			this.mCurrentTextureY += this.getLineGap() + this.getLineHeight();
		}

		final int letterWidth = (int) this.mCreateLetterTemporarySize.mWidth;
		final int letterHeight = (int) this.mCreateLetterTemporarySize.mHeight;
		final float textureX = this.mCurrentTextureX / TEXTURE_SIZE;
		final float textureY = this.mCurrentTextureY / TEXTURE_SIZE;
		final float textureWidth = this.mCreateLetterTemporarySize.mWidth / TEXTURE_SIZE;
		final float textureHeight = this.mCreateLetterTemporarySize.mHeight / TEXTURE_SIZE;
		
		final Letter letter = new Letter(this.getLetterAdvance(pCharacter), letterWidth, letterHeight, textureX, textureY, textureWidth, textureHeight);
		this.mCurrentTextureX += this.mCreateLetterTemporarySize.mWidth;

		this.mLettersPendingToBeDrawnToTexture.put(letter, bitmap);

		return letter;
	}

	public void update(final GL10 pGL) {
		if(this.mLettersPendingToBeDrawnToTexture.size() > 0) {

			for (final Entry<Letter, Bitmap> entry : this.mLettersPendingToBeDrawnToTexture.entrySet()) {
				final Letter letter = entry.getKey();
				GLHelper.bindTexture(pGL, this.mTexture.getHardwareTextureID());
				GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, (int)(letter.mTextureX * TEXTURE_SIZE), (int)(letter.mTextureY * TEXTURE_SIZE), entry.getValue());
			}
			this.mLettersPendingToBeDrawnToTexture.clear();
			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
