package org.andengine.input.touch.controller;

import org.andengine.util.system.SystemUtils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
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

	public static boolean isSupported(final Context pContext) {
		if(MultiTouch.SUPPORTED == null) {
			MultiTouch.SUPPORTED = SystemUtils.hasSystemFeature(pContext, PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
		}

		return MultiTouch.SUPPORTED;
	}

	public static boolean isSupportedDistinct(final Context pContext) {
		if(MultiTouch.SUPPORTED_DISTINCT == null) {
			MultiTouch.SUPPORTED_DISTINCT = SystemUtils.hasSystemFeature(pContext, PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT);
		}

		return MultiTouch.SUPPORTED_DISTINCT;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
