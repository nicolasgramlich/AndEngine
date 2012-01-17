package org.andengine.entity.text;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

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

	private float mCharactersPerSecond;

	private int mCharactersVisible;
	private float mSecondsElapsed;

	private boolean mReverse;

	private float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TickerText(final float pX, final float pY, final IFont pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pCharactersPerSecond, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pFont, pText, pHorizontalAlign, pVertexBufferObjectManager);

		this.setCharactersPerSecond(pCharactersPerSecond);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isReverse() {
		return this.mReverse;
	}

	public void setReverse(final boolean pReverse) {
		this.mReverse = pReverse;
	}

	public float getCharactersPerSecond() {
		return this.mCharactersPerSecond;
	}

	public void setCharactersPerSecond(final float pCharactersPerSecond) {
		this.mCharactersPerSecond = pCharactersPerSecond;
		this.mDuration = this.mCharactersMaximum * this.mCharactersPerSecond;
	}

	public int getCharactersVisible() {
		return this.mCharactersVisible;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if(this.mReverse){
			if(this.mCharactersVisible < this.mCharactersMaximum){
				this.mSecondsElapsed = Math.max(0, this.mSecondsElapsed - pSecondsElapsed);
				this.mCharactersVisible = (int)(this.mSecondsElapsed * this.mCharactersPerSecond);
			}
		} else {
			if(this.mCharactersVisible < this.mCharactersMaximum){
				this.mSecondsElapsed = Math.min(this.mDuration, this.mSecondsElapsed + pSecondsElapsed);
				this.mCharactersVisible = (int)(this.mSecondsElapsed * this.mCharactersPerSecond);
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
		this.mReverse = false;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
