package org.andengine.util;

import java.lang.reflect.Method;

import org.andengine.util.exception.MethodNotFoundException;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

/**
 * (c) 2013 Nicolas Gramlich
 * 
 * @author Nicolas Gramlich
 * @since 14:49:11 - 17.03.2013
 */
public final class WifiUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private WifiUtils() {

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

	public static boolean isWifiAccessPointEnabled(final Context pContext) throws WifiUtilsException {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);

		try {
			final Method WifiManager_isWifiApEnabled = wifiManager.getClass().getMethod("isWifiApEnabled");
			if (WifiManager_isWifiApEnabled == null) {
				throw new WifiUtilsException(new MethodNotFoundException(WifiManager.class.getSimpleName() + ".isWifiApEnabled()"));
			} else {
				final boolean result = (Boolean) WifiManager_isWifiApEnabled.invoke(wifiManager);
				return result;
			}
		} catch (final Throwable t) {
			throw new WifiUtilsException(t);
		}
	}

	public static boolean isWifiAccessPointEnabled(final Context pContext, final boolean pDefault) {
		try {
			return WifiUtils.isWifiAccessPointEnabled(pContext);
		} catch (final WifiUtilsException e) {
			return pDefault;
		}
	}

	public static boolean setWifiAccessPointEnabled(final Context pContext, final boolean pEnabled) throws WifiUtilsException {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);

		try {
			final Method WifiManager_setWifiApEnabled = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			if (WifiManager_setWifiApEnabled == null) {
				throw new WifiUtilsException(new MethodNotFoundException(WifiManager.class.getSimpleName() + ".setWifiApEnabled(" + WifiConfiguration.class.getSimpleName() + ", " + boolean.class.getSimpleName() + ")"));
			} else {
				final boolean result = (Boolean) WifiManager_setWifiApEnabled.invoke(wifiManager, null, pEnabled);
				return result;
			}
		} catch (final Throwable t) {
			throw new WifiUtilsException(t);
		}
	}

	public static int getWifiAccessPointState(final Context pContext) throws WifiUtilsException {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);

		try {
			final Method WifiManager_getWifiApState = wifiManager.getClass().getMethod("getWifiApState");
			if (WifiManager_getWifiApState == null) {
				throw new WifiUtilsException(new MethodNotFoundException(WifiManager.class.getSimpleName() + ".getWifiApState()"));
			} else {
				final int result = (Integer) WifiManager_getWifiApState.invoke(wifiManager);
				return result;
			}
		} catch (final Throwable t) {
			throw new WifiUtilsException(t);
		}
	}

	private static WifiManager getWifiManager(final Context pContext) {
		return (WifiManager) pContext.getSystemService(Context.WIFI_SERVICE);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class WifiUtilsException extends Exception {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 1108697754015225179L;

		// ===========================================================
		// Methods
		// ===========================================================

		public WifiUtilsException() {

		}

		public WifiUtilsException(final Throwable pThrowable) {
			super(pThrowable);
		}
	}
}
