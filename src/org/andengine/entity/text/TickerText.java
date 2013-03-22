package org.andengine.entity.text;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.exception.OutOfCharactersException;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 10:02:04 - 05.05.2010
 */
public class TickerText extends Text {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final TickerTextOptions mTickerTextOptions;

	private int mCharactersVisible;
	private float mSecondsElapsed;

	private float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TickerText(final float pX, final float pY, final IFont pFont, final String pText, final TickerTextOptions pTickerTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pFont, pText, pTickerTextOptions, pVertexBufferObjectManager);

		this.mTickerTextOptions = pTickerTextOptions;

		this.mDuration = this.mCharactersToDraw * this.mTickerTextOptions.mCharactersPerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public TickerTextOptions getTextOptions() {
		return (TickerTextOptions) super.getTextOptions();
	}

	public boolean isReverse() {
		return this.getTextOptions().mReverse;
	}

	public void setReverse(final boolean pReverse) {
		this.mTickerTextOptions.mReverse = pReverse;
	}

	public float getCharactersPerSecond() {
		return this.mTickerTextOptions.mCharactersPerSecond;
	}

	public void setCharactersPerSecond(final float pCharactersPerSecond) {
		this.mTickerTextOptions.mCharactersPerSecond = pCharactersPerSecond;

		this.mDuration = this.mCharactersToDraw * pCharactersPerSecond;
	}

	public int getCharactersVisible() {
		return this.mCharactersVisible;
	}

	@Override
	public void setText(final CharSequence pText) throws OutOfCharactersException {
		super.setText(pText);

		if (this.mTickerTextOptions != null) {
			this.mDuration = this.mCharactersToDraw * this.mTickerTextOptions.mCharactersPerSecond;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (this.mTickerTextOptions.mReverse) {
			if (this.mCharactersVisible < this.mCharactersToDraw) {
				this.mSecondsElapsed = Math.max(0, this.mSecondsElapsed - pSecondsElapsed);
				this.mCharactersVisible = (int) (this.mSecondsElapsed * this.mTickerTextOptions.mCharactersPerSecond);
			}
		} else {
			if (this.mCharactersVisible < this.mCharactersToDraw) {
				this.mSecondsElapsed = Math.min(this.mDuration, this.mSecondsElapsed + pSecondsElapsed);
				this.mCharactersVisible = (int) (this.mSecondsElapsed * this.mTickerTextOptions.mCharactersPerSecond);
			}
		}
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.mTextVertexBufferObject.draw(GLES20.GL_TRIANGLES, this.mCharactersVisible * Text.VERTICES_PER_LETTER);
	}

	@Override
	public void reset() {
		super.reset();

		this.mCharactersVisible = 0;
		this.mSecondsElapsed = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class TickerTextOptions extends TextOptions {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		/* package */ float mCharactersPerSecond;
		/* package */ boolean mReverse;

		// ===========================================================
		// Constructors
		// ===========================================================

		public TickerTextOptions() {

		}

		public TickerTextOptions(final float pCharactersPerSecond) {
			this(pCharactersPerSecond, false);
		}

		public TickerTextOptions(final float pCharactersPerSecond, final boolean pReverse) {
			this(HorizontalAlign.LEFT, pCharactersPerSecond, pReverse);
		}

		public TickerTextOptions(final HorizontalAlign pHorizontalAlign, final float pCharactersPerSecond) {
			this(AutoWrap.NONE, 0, pHorizontalAlign, Text.LEADING_DEFAULT, pCharactersPerSecond, false);
		}

		public TickerTextOptions(final HorizontalAlign pHorizontalAlign, final float pCharactersPerSecond, final boolean pReverse) {
			this(AutoWrap.NONE, 0, pHorizontalAlign, Text.LEADING_DEFAULT, pCharactersPerSecond, pReverse);
		}

		public TickerTextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth, final HorizontalAlign pHorizontalAlign, final float pCharactersPerSecond) {
			this(pAutoWrap, pAutoWrapWidth, pHorizontalAlign, Text.LEADING_DEFAULT, pCharactersPerSecond, false);
		}

		public TickerTextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth, final HorizontalAlign pHorizontalAlign, final float pLeading, final float pCharactersPerSecond) {
			this(pAutoWrap, pAutoWrapWidth, pHorizontalAlign, pLeading, pCharactersPerSecond, false);
		}

		public TickerTextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth, final HorizontalAlign pHorizontalAlign, final float pLeading, final float pCharactersPerSecond, final boolean pReverse) {
			super(pAutoWrap, pAutoWrapWidth, pHorizontalAlign, pLeading);

			this.mCharactersPerSecond = pCharactersPerSecond;
			this.mReverse = pReverse;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public float getCharactersPerSecond() {
			return this.mCharactersPerSecond;
		}

		public void setCharactersPerSecond(final float pCharactersPerSecond) {
			this.mCharactersPerSecond = pCharactersPerSecond;
		}

		public boolean isReverse() {
			return this.mReverse;
		}

		public void setReverse(final boolean pReverse) {
			this.mReverse = pReverse;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
