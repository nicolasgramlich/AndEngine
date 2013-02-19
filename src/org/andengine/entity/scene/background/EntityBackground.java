package org.andengine.entity.scene.background;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.opengl.util.GLState;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 18:25:10 - 21.07.2010
 */
public class EntityBackground extends Background {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected IEntity mEntity;

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntityBackground(final IEntity pEntity) {
		this.mEntity = pEntity;
	}

	public EntityBackground(final float pRed, final float pGreen, final float pBlue, final IEntity pEntity) {
		super(pRed, pGreen, pBlue);
		this.mEntity = pEntity;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onDraw(final GLState pGLState, final Camera pCamera) {
		super.onDraw(pGLState, pCamera);

		this.mEntity.onDraw(pGLState, pCamera);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
