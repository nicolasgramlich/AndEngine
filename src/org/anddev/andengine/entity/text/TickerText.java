package org.anddev.andengine.entity.text;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.vertex.TextVertexBuffer;
import org.anddev.andengine.util.HorizontalAlign;

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

	private int mCharactersVisible = 0;
	private float mSecondsElapsed = 0;

	private boolean mReverse = false;

	private float mDuration;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TickerText(final float pX, final float pY, final Font pFont, final String pText, final HorizontalAlign pHorizontalAlign, final float pCharactersPerSecond) {
		super(pX, pY, pFont, pText, pHorizontalAlign);
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
	protected void drawVertices(final GL10 pGL, final Camera pCamera) {
		pGL.glDrawArrays(GL10.GL_TRIANGLES, 0, this.mCharactersVisible * TextVertexBuffer.VERTICES_PER_CHARACTER);
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
