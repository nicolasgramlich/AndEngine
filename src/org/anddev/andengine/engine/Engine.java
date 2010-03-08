package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.Scene;


/**
 * @author Nicolas Gramlich
 * @since 12:21:31 - 08.03.2010
 */
public class Engine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mGameWidth;
	private final int mGameHeight;
	
	private Scene mScene;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Engine(final int pGameWidth, final int pGameHeight) {
		this.mGameWidth = pGameWidth;
		this.mGameHeight = pGameHeight;		
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setScene(final Scene pScene) {
		this.mScene = pScene;
	}

	public int getGameWidth() {
		return this.mGameWidth;
	}

	public int getGameHeight() {
		return this.mGameHeight;
	}	

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDrawFrame(final GL10 pGL) {
		if(this.mScene != null)
			this.mScene.onDraw(pGL);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
