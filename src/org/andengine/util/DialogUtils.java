package org.andengine.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:04:09 - 12.05.2011
 */
public final class DialogUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private DialogUtils() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void keepScreenOn(final Dialog pDialog) {
		pDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Deprecated
	public static void showDialogOnUiThread(final Activity pActivity, final int pDialogID) {
		DialogUtils.showDialogOnUiThread(pActivity, pDialogID, null);
	}

	@Deprecated
	public static void showDialogOnUiThread(final Activity pActivity, final int pDialogID, final Bundle pBundle) {
		if (ActivityUtils.isOnUiThread()) {
			pActivity.showDialog(pDialogID, pBundle);
		} else {
			pActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					pActivity.showDialog(pDialogID, pBundle);
				}
			});
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
