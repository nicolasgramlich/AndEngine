package org.andengine.engine.camera;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:50:42 - 03.07.2010
 */
public class CameraFactory {
	// ===========================================================
	// Constants
	// ===========================================================

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

	// ===========================================================
	// Methods
	// ===========================================================

	public static Camera createPixelPerfectCamera(final Context pContext, final float pCenterX, final float pCenterY) {
		final DisplayMetrics displayMetrics = CameraFactory.getDisplayMetrics(pContext);

		final float width = displayMetrics.widthPixels;
		final float height = displayMetrics.heightPixels;

		return new Camera(pCenterX - width * 0.5f, pCenterY - height * 0.5f, width, height);
	}

	private static DisplayMetrics getDisplayMetrics(final Context pContext) {
		return pContext.getResources().getDisplayMetrics();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
