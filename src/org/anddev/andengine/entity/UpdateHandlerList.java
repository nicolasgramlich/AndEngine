package org.anddev.andengine.entity;

import java.util.ArrayList;

/**
 * @author Nicolas Gramlich
 * @since 09:45:22 - 31.03.2010
 */
public class UpdateHandlerList extends ArrayList<IUpdateHandler> implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = -8842562717687229277L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		final int handlerCount = this.size();
		for(int i = 0; i < handlerCount; i++) {
			this.get(i).onUpdate(pSecondsElapsed);
		}
	}
	
	@Override
	public void reset() {
		final int handlerCount = this.size();
		for(int i = 0; i < handlerCount; i++) {
			this.get(i).reset();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
