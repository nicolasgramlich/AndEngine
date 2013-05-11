package org.andengine.util;

import org.andengine.util.system.SystemUtils;

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

	/**
	 * @see <a href="https://groups.google.com/forum/?fromgroups=#!topic/android-developers/8M0RTFfO7-M">groups.google.com/forum/?fromgroups=#!topic/android-developers/8M0RTFfO7-M</a>
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static <T> void execute(final AsyncTask<T, ?, ?> pAsyncTask, final T ... pParameters) {
		if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.HONEYCOMB)) {
			pAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, pParameters);
		} else {
			pAsyncTask.execute(pParameters);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
