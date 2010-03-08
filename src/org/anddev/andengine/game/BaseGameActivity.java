package org.anddev.andengine.game;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.opengl.view.RenderSurfaceView;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Nicolas Gramlich
 * @since 11:27:06 - 08.03.2010
 */
public abstract class BaseGameActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Engine mEngine;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	@Override
	protected void onCreate(final Bundle pSavedInstanceState) {
		super.onCreate(pSavedInstanceState);
		
		this.mEngine = this.onCreateEngine();
		this.setContentView(new RenderSurfaceView(this, this.mEngine));
		this.onLoadResources();
		this.mEngine.setScene(this.onCreateScene());
		this.onLoadComplete();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Engine getEngine() {
		return this.mEngine;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	protected abstract Scene onCreateScene();

	protected abstract void onLoadResources();
	
	protected abstract void onLoadComplete();

	protected abstract Engine onCreateEngine();

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
