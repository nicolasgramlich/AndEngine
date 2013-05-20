package org.andengine.engine.handler;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.list.SmartList;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:42:49 - 22.11.2011
 */
public class DrawHandlerList extends SmartList<IDrawHandler> implements IDrawHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 1767324757143199934L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DrawHandlerList() {

	}

	public DrawHandlerList(final int pCapacity) {
		super(pCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDraw(final GLState pGLState, final Camera pCamera) {
		final int handlerCount = this.size();
		for (int i = handlerCount - 1; i >= 0; i--) {
			this.get(i).onDraw(pGLState, pCamera);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
