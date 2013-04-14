package org.andengine.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 12:30:32 - 14.04.2013
 */
public final class IntentUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private IntentUtils() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static boolean isIntentResolvable(final Context pContext, final Intent pIntent) {
		final List<ResolveInfo> resolveInfo = pContext.getPackageManager().queryIntentActivities(pIntent, 0);
		return (resolveInfo != null) && !resolveInfo.isEmpty();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
