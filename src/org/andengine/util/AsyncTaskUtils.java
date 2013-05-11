package org.andengine.util;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;


/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 12:42:12 - 11.05.2013
 */
public final class AsyncTaskUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private AsyncTaskUtils() {

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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static <T> void executeParallel(final AsyncTask<T, ?, ?> pAsyncTask, final T ... pParameters) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
			pAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pParameters);
		} else {
			pAsyncTask.execute(pParameters);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
