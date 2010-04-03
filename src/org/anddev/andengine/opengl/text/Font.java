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

	private final HashMap<Character, Glyph> mCharacterToGlyphMap = new HashMap<Character, Glyph>();
	private final HashMap<Glyph, Bitmap> mGlyhpsPendingToBeDrawnToTexture = new HashMap<Glyph, Bitmap>();

	private final Typeface mTypeface;
	private final Paint mPaint;
	private final FontMetrics mFontMetrics;
	private final Texture mTexture;

	private final Rect mTemporaryRect = new Rect();

	private int mCurrentTextureX = 0;
	private int mCurrentTextureY = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Font(final Texture pTexture, final int size, final Typeface pTypeFace) {
		this.mTexture = pTexture;
		this.mTypeface = pTypeFace;
		this.mPaint = new Paint();
		this.mPaint.setTypeface(this.mTypeface);
		this.mPaint.setTextSize(size);
		this.mPaint.setAntiAlias(false);
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

	public int getGlyphAdvance(final char pCharacter) {
		final float[] width = new float[1]; // TODO Kill allocation
		this.mPaint.getTextWidths("" + pCharacter, width);
		return (int) (Math.ceil(width[0]));
	}

	public Bitmap getGlyphBitmap(final char pCharacter) {
		final Rect rect = new Rect(); // TODO Kill allocation
		this.mPaint.getTextBounds("" + pCharacter, 0, 1, rect);

		final Bitmap bitmap = Bitmap.createBitmap(rect.width() == 0 ? 1 : rect.width() + 5, this.getLineHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);

		/* Make background transparent. */
		this.mPaint.setColor(Color.TRANSPARENT);
		this.mPaint.setStyle(Style.FILL);
		canvas.drawRect(new Rect(0, 0, rect.width() + 5, this.getLineHeight()), this.mPaint); // TODO Kill allocation

		this.mPaint.setColor(Color.BLACK);
		canvas.drawText("" + pCharacter, 0, -this.mFontMetrics.ascent, this.mPaint);
		return bitmap;
	}

	public int getLineGap() {
		return (int) (Math.ceil(this.mFontMetrics.leading));
	}

	public int getLineHeight() {
		return (int) Math.ceil(Math.abs(this.mFontMetrics.ascent) + Math.abs(this.mFontMetrics.descent));
	}

	public int getStringWidth(final String pText) {
		final Rect rect = new Rect();// TODO Kill allocation
		this.mPaint.getTextBounds(pText, 0, pText.length(), rect);
		return rect.width();
	}

	public void getGlyphBounds(final char pCharacter, final Rectangle pRectangle) {
		this.mPaint.getTextBounds("" + pCharacter, 0, 1, this.mTemporaryRect);
		pRectangle.mWidth = this.mTemporaryRect.width() + 5;
		pRectangle.mHeight = this.getLineHeight();
	}

	public Texture getTexture() {
		return this.mTexture;
	}

	public Glyph getGlyph(final char pCharacter) {
		Glyph glyph = this.mCharacterToGlyphMap.get(pCharacter);
		if (glyph == null) {
			glyph = this.createGlyph(pCharacter);
			this.mCharacterToGlyphMap.put(pCharacter, glyph);
		}
		return glyph;
	}

	private Glyph createGlyph(final char pCharacter) {
		final Bitmap bitmap = this.getGlyphBitmap(pCharacter);
		final Rectangle rect = new Rectangle(); // TODO Kill allocation
		this.getGlyphBounds(pCharacter, rect);

		if (this.mCurrentTextureX + rect.mWidth >= TEXTURE_SIZE) {
			this.mCurrentTextureX = 0;
			this.mCurrentTextureY += this.getLineGap() + this.getLineHeight();
		}

		final Glyph glyph = new Glyph(this.getGlyphAdvance(pCharacter), (int) rect.mWidth, (int) rect.mHeight, this.mCurrentTextureX / TEXTURE_SIZE, this.mCurrentTextureY / TEXTURE_SIZE, rect.mWidth / TEXTURE_SIZE, rect.mHeight / TEXTURE_SIZE);
		this.mCurrentTextureX += rect.mWidth;
		
		this.mGlyhpsPendingToBeDrawnToTexture.put(glyph, bitmap);
		
		return glyph;
	}

	public void update(final GL10 pGL) {
		if(this.mGlyhpsPendingToBeDrawnToTexture.size() > 0) {

			for (Entry<Glyph, Bitmap> entry : this.mGlyhpsPendingToBeDrawnToTexture.entrySet()) {  
				final Glyph glyph = entry.getKey();  
				GLHelper.bindTexture(pGL, this.mTexture.getHardwareTextureID());		
				GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, (int)(glyph.mTextureX * TEXTURE_SIZE), (int)(glyph.mTextureY * TEXTURE_SIZE), entry.getValue());	
			}
			this.mGlyhpsPendingToBeDrawnToTexture.clear();
			System.gc();
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
