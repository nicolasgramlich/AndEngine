package org.andengine.util;

import org.andengine.util.exception.AndEngineException;
import org.andengine.util.system.SystemUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 19:33:01 - 21.05.2013
 */
public final class ConnectivityUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private ConnectivityUtils() {

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

	public static ConnectivityManager getConnectivityManager(final Context pContext) {
		return (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * @param pContext
	 * @param pNetworkType {@link ConnectivityManager#TYPE_WIFI}, etc...
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(final Context pContext, final int pNetworkType) {
		return ConnectivityUtils.getConnectivityManager(pContext).getNetworkInfo(pNetworkType);
	}

	/**
	 * @param pContext
	 * @param pNetworkType {@link ConnectivityManager#TYPE_WIFI}, etc...
	 * @return
	 */
	public static boolean isNetworkAvailable(final Context pContext, final int pNetworkType) {
		return ConnectivityUtils.getNetworkInfo(pContext, pNetworkType).isAvailable();
	}

	/**
	 * @param pContext
	 * @param pNetworkType {@link ConnectivityManager#TYPE_WIFI}, etc...
	 * @return
	 */
	public static boolean isNetworkConnected(final Context pContext, final int pNetworkType) {
		return ConnectivityUtils.getNetworkInfo(pContext, pNetworkType).isConnected();
	}

	/**
	 * @param pContext
	 * @param pNetworkType {@link ConnectivityManager#TYPE_WIFI}, etc...
	 * @return
	 */
	public static boolean isNetworkConnectedOrConnecting(final Context pContext, final int pNetworkType) {
		return ConnectivityUtils.getNetworkInfo(pContext, pNetworkType).isConnectedOrConnecting();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static boolean isEthernetAvailable(final Context pContext) throws ConnectivityUtilsException {
		if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.HONEYCOMB_MR2)) {
			return ConnectivityUtils.isNetworkAvailable(pContext, ConnectivityManager.TYPE_ETHERNET);
		} else {
			throw new ConnectivityUtilsException();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static boolean isEthernetAvailable(final Context pContext, final boolean pDefault) {
		if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.HONEYCOMB_MR2)) {
			return ConnectivityUtils.isNetworkAvailable(pContext, ConnectivityManager.TYPE_ETHERNET);
		} else {
			return pDefault;
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static boolean isEthernetConnected(final Context pContext) throws ConnectivityUtilsException {
		if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.HONEYCOMB_MR2)) {
			return ConnectivityUtils.isNetworkConnected(pContext, ConnectivityManager.TYPE_ETHERNET);
		} else {
			throw new ConnectivityUtilsException();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static boolean isEthernetConnected(final Context pContext, final boolean pDefault) {
		if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.HONEYCOMB_MR2)) {
			return ConnectivityUtils.isNetworkConnected(pContext, ConnectivityManager.TYPE_ETHERNET);
		} else {
			return pDefault;
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static boolean isEthernetConnectedOrConnecting(final Context pContext) throws ConnectivityUtilsException {
		if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.HONEYCOMB_MR2)) {
			return ConnectivityUtils.isNetworkConnectedOrConnecting(pContext, ConnectivityManager.TYPE_ETHERNET);
		} else {
			throw new ConnectivityUtilsException();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static boolean isEthernetConnectedOrConnecting(final Context pContext, final boolean pDefault) {
		if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.HONEYCOMB_MR2)) {
			return ConnectivityUtils.isNetworkConnectedOrConnecting(pContext, ConnectivityManager.TYPE_ETHERNET);
		} else {
			return pDefault;
		}
	}

	public static boolean isWifiAvailable(final Context pContext) throws ConnectivityUtilsException {
		return ConnectivityUtils.isNetworkAvailable(pContext, ConnectivityManager.TYPE_WIFI);
	}

	public static boolean isWifiConnected(final Context pContext) {
		return ConnectivityUtils.isNetworkConnected(pContext, ConnectivityManager.TYPE_WIFI);
	}

	public static boolean isWifiConnectedOrConnecting(final Context pContext) {
		return ConnectivityUtils.isNetworkConnectedOrConnecting(pContext, ConnectivityManager.TYPE_WIFI);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class ConnectivityUtilsException extends AndEngineException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 4955967914807211384L;

		// ===========================================================
		// Constructors
		// ===========================================================

		public ConnectivityUtilsException() {

		}

		public ConnectivityUtilsException(final Throwable pThrowable) {
			super(pThrowable);
		}
	}
}
