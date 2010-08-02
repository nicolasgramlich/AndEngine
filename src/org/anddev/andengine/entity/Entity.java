package org.anddev.andengine.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.camera.Camera;


/**
 * @author Nicolas Gramlich
 * @since 12:00:48 - 08.03.2010
 */
public abstract class Entity implements IEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mVisible = true;
	private boolean mIgnoreUpdate;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isVisible() {
		return this.mVisible;
	}

	public void setVisible(final boolean pVisible) {
		this.mVisible = pVisible;
	}

	public boolean isIgnoreUpdate() {
		return this.mIgnoreUpdate;
	}

	public void setIgnoreUpdate(final boolean pIgnoreUpdate) {
		this.mIgnoreUpdate = pIgnoreUpdate;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onManagedDraw(final GL10 pGL, final Camera pCamera);

	@Override
	public final void onDraw(final GL10 pGL, final Camera pCamera) {
		if(this.mVisible) {
			this.onManagedDraw(pGL, pCamera);
		}
	}

	protected abstract void onManagedUpdate(final float pSecondsElapsed);

	@Override
	public final void onUpdate(final float pSecondsElapsed) {
		if(!this.mIgnoreUpdate) {
			this.onManagedUpdate(pSecondsElapsed);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void reset() {
		this.mVisible = true;
		this.mIgnoreUpdate = false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
