package org.andengine.util;

import org.andengine.util.system.SystemUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 20:49:32 - 24.04.2013
 */
public final class GooglePlayUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private GooglePlayUtils() {

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

	public static void startGooglePlayActivity(final Context pContext) {
		GooglePlayUtils.startGooglePlayActivity(pContext, SystemUtils.getPackageName(pContext));
	}

	public static void startGooglePlayActivity(final Context pContext, final String pPackageName) {
		final Intent googlePlayAppIntent = getGooglePlayAppIntent(pPackageName);
		if (IntentUtils.isIntentResolvable(pContext, googlePlayAppIntent)) {
			pContext.startActivity(googlePlayAppIntent);
		} else {
			pContext.startActivity(getGooglePlayWebsiteIntent(pPackageName));
		}
	}

	public static void startGooglePlayActivityForResult(final Activity pActivity, final int pRequestCode) {
		GooglePlayUtils.startGooglePlayActivityForResult(pActivity, SystemUtils.getPackageName(pActivity), pRequestCode);
	}

	public static void startGooglePlayActivityForResult(final Activity pActivity, final String pPackageName, final int pRequestCode) {
		final Intent googlePlayAppIntent = getGooglePlayAppIntent(pPackageName);
		if (IntentUtils.isIntentResolvable(pActivity, googlePlayAppIntent)) {
			pActivity.startActivityForResult(googlePlayAppIntent, pRequestCode);
		} else {
			pActivity.startActivityForResult(getGooglePlayWebsiteIntent(pPackageName), pRequestCode);
		}
	}

	public static Intent getGooglePlayAppIntent(final String pPackageName) {
		return new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pPackageName));
	}

	public static Intent getGooglePlayWebsiteIntent(final String pPackageName) {
		return new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + pPackageName));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
