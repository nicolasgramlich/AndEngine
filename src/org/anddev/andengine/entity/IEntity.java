package org.anddev.andengine.entity;

import org.anddev.andengine.opengl.IRenderable;


/**
 * @author Nicolas Gramlich
 * @since 11:20:25 - 08.03.2010
 */
public interface IEntity extends IRenderable, IUpdateHandler {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset();
}
