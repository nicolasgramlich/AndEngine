package org.andengine.util;

import java.util.GregorianCalendar;

import org.andengine.util.preferences.SimplePreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 22:43:32 - 02.11.2010
 */
public final class BetaUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String PREFERENCES_BETAUTILS_ID = "preferences.betautils.lastuse";

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private BetaUtils() {

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

	public static boolean finishWhenExpired(final Activity pActivity, final GregorianCalendar pExpirationDate, final int pTitleResourceID, final int pMessageResourceID) {
		return BetaUtils.finishWhenExpired(pActivity, pExpirationDate, pTitleResourceID, pMessageResourceID, null, null);
	}

	public static boolean finishWhenExpired(final Activity pActivity, final GregorianCalendar pExpirationDate, final int pTitleResourceID, final int pMessageResourceID, final Intent pOkIntent, final Intent pCancelIntent) {
		final SharedPreferences spref = SimplePreferences.getInstance(pActivity);

		final long now = System.currentTimeMillis();
		final long lastuse = Math.max(now, spref.getLong(PREFERENCES_BETAUTILS_ID, -1));
		spref.edit().putLong(PREFERENCES_BETAUTILS_ID, lastuse).commit();

		final GregorianCalendar lastuseDate = new GregorianCalendar();
		lastuseDate.setTimeInMillis(lastuse);

		if (lastuseDate.after(pExpirationDate)) {
			final Builder alertDialogBuilder = new AlertDialog.Builder(pActivity)
			.setTitle(pTitleResourceID)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage(pMessageResourceID);

			alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(final DialogInterface pDialog, final int pWhich) {
					if (pOkIntent != null) {
						pActivity.startActivity(pOkIntent);
					}
					pActivity.finish();
				}
			});
			alertDialogBuilder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
				@Override
				public void onClick(final DialogInterface pDialog, final int pWhich) {
					if (pCancelIntent != null) {
						pActivity.startActivity(pCancelIntent);
					}
					pActivity.finish();
				}
			})
			.create().show();
			return true;
		} else {
			return false;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
