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

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected abstract void onManagedDraw(final GL10 pGL);

	@Override
	public final void onDraw(final GL10 pGL) {
		if(this.isVisible())
			this.onManagedDraw(pGL);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
