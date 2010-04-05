package org.anddev.andengine.entity;

import javax.microedition.khronos.opengles.GL10;


/**
 * @author Nicolas Gramlich
 * @since 12:00:48 - 08.03.2010
 */
public abstract class BaseEntity implements IEntity {
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

	public void setIgnoreUpdate(final boolean pIgnoreUpdate) {
		this.mIgnoreUpdate = pIgnoreUpdate;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onManagedDraw(final GL10 pGL);

	@Override
	public final void onDraw(final GL10 pGL) {
		if(this.mVisible) {
			this.onManagedDraw(pGL);
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
