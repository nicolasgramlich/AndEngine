package org.anddev.andengine.engine.camera;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
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

	public static Camera createPixelPerfectCamera(final Activity pActivity, final float pCenterX, final float pCenterY) {
		final DisplayMetrics displayMetrics = getDisplayMetrics(pActivity);

		final float width = displayMetrics.widthPixels;
		final float height = displayMetrics.heightPixels;
		return new Camera(pCenterX - width * 0.5f, pCenterY - height * 0.5f, width, height);
	}

	private static DisplayMetrics getDisplayMetrics(final Activity pActivity) {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
		pActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
