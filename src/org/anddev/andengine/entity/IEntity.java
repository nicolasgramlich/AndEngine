package org.anddev.andengine.entity;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.IUpdateHandler;


/**
 * @author Nicolas Gramlich
 * @since 11:20:25 - 08.03.2010
 */
public interface IEntity extends IUpdateHandler {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDraw(final GL10 pGL);
}
