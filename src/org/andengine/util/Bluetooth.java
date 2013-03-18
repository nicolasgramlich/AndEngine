package org.andengine.util;

import org.andengine.util.system.SystemUtils;
import org.andengine.util.system.SystemUtils.SystemUtilsException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:44:07 - 04.03.2011
 */
public final class Bluetooth {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static Boolean sSupported;

	// ===========================================================
	// Constructors
	// ===========================================================

	private Bluetooth() {

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

	public static boolean isSupported(final Context pContext) {
		if (sSupported == null) {
			if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.ECLAIR_MR1)) {
				try {
					sSupported = SystemUtils.hasSystemFeature(pContext, PackageManager.FEATURE_BLUETOOTH);
				} catch (final SystemUtilsException e) {
					sSupported = Boolean.FALSE;
				}
			} else {
				sSupported = Boolean.FALSE;
			}
		}

		return sSupported;
	}

	public static boolean isSupportedByAndroidVersion() {
		return SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.ECLAIR_MR1);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
