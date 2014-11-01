package org.anddev.andengine.extension.input.touch.controller;

import org.andengine.util.system.SystemUtils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:00:38 - 14.07.2010
 */
public class MultiTouch {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static Boolean SUPPORTED = null;
	private static Boolean SUPPORTED_DISTINCT = null;

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

	public static boolean isSupportedByAndroidVersion() {
		return SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.ECLAIR);
	}

	public static boolean isSupported(final Context pContext) {
		if (SUPPORTED == null) {
			SUPPORTED = SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.ECLAIR) && SystemUtils.hasSystemFeature(pContext, PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
		}

		return SUPPORTED;
	}

	public static boolean isSupportedDistinct(final Context pContext) {
		if (SUPPORTED_DISTINCT == null) {
			SUPPORTED_DISTINCT = SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.ECLAIR_MR1) && SystemUtils.hasSystemFeature(pContext, PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT);
		}

		return SUPPORTED_DISTINCT;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
