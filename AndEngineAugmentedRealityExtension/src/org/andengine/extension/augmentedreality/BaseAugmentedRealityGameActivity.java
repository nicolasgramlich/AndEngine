package org.andengine.extension.augmentedreality;


import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:38:32 - 24.05.2010
 */
public abstract class BaseAugmentedRealityGameActivity extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private CameraPreviewSurfaceView mCameraPreviewSurfaceView;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mCameraPreviewSurfaceView = new CameraPreviewSurfaceView(this);
		this.addContentView(this.mCameraPreviewSurfaceView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
//		this.mRenderSurfaceView.setZOrderMediaOverlay(true);
		this.mRenderSurfaceView.bringToFront();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	protected void onSetContentView() {
		this.mRenderSurfaceView = new RenderSurfaceView(this);	

		this.mRenderSurfaceView.setEGLConfigChooser(4, 4, 4, 4, 16, 0); 
		this.mRenderSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		this.mRenderSurfaceView.setRenderer(this.mEngine, this);

		this.setContentView(this.mRenderSurfaceView, createSurfaceViewLayoutParams());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		finish();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}