package org.andengine.util;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;

import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.debug.Debug;
import org.andengine.util.exception.AndEngineException;
import org.andengine.util.exception.MethodNotFoundException;
import org.andengine.util.exception.WifiException;
import org.andengine.util.system.SystemUtils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Build;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:54:01 - 20.03.2011
 */
public final class WifiUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String IP_DEFAULT = "0.0.0.0";
	private static final String[] HOTSPOT_NETWORKINTERFACE_NAMES = { "wl0.1", "wlan0" };
	private static final String MULTICASTLOCK_NAME_DEFAULT = "AndEngineMulticastLock";

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

	public static WifiManager getWifiManager(final Context pContext) {
		return (WifiManager) pContext.getSystemService(Context.WIFI_SERVICE);
	}

	public static boolean isWifiEnabled(final Context pContext) {
		return WifiUtils.getWifiManager(pContext).isWifiEnabled();
	}

	private static void setWifiEnabled(final Context pContext, final boolean pEnabled) {
		WifiUtils.getWifiManager(pContext).setWifiEnabled(pEnabled);
	}

	public static String getWifiSSID(final Context pContext) {
		return WifiUtils.getWifiManager(pContext).getConnectionInfo().getSSID();
	}

	public static byte[] getWifiIPv4AddressRaw(final Context pContext) {
		return IPUtils.ipv4AddressToIPAddress(WifiUtils.getWifiManager(pContext).getConnectionInfo().getIpAddress());
	}

	public static String getWifiIPv4Address(final Context pContext) throws UnknownHostException {
		return IPUtils.ipAddressToString(WifiUtils.getWifiIPv4AddressRaw(pContext));
	}

	public static boolean isWifiIPAddressValid(final Context pContext) {
		return WifiUtils.getWifiManager(pContext).getConnectionInfo().getIpAddress() != 0;
	}


	/**
	 * The check currently performed is not sufficient, as some carriers disabled this feature manually!
	 */
	public static boolean isWifiHotspotSupported(final Context pContext) {
		if (SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.ECLAIR_MR1)) {
			return false;
		} else {
			final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);
			for (Method m : wifiManager.getClass().getMethods()) {
				Debug.d("WifiManager." + m.getName());
			}

			try {
				final Method WifiManager_isWifiApEnabled = wifiManager.getClass().getMethod("isWifiApEnabled");
				return WifiManager_isWifiApEnabled != null;
			} catch (final Throwable t) {
				return false;
			}
		}
	}

	public static boolean isWifiHotspotEnabled(final Context pContext) throws WifiUtilsException {
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

	public static boolean isWifiHotspotEnabled(final Context pContext, final boolean pDefault) {
		try {
			return WifiUtils.isWifiHotspotEnabled(pContext);
		} catch (final WifiUtilsException e) {
			return pDefault;
		}
	}

	public static boolean setWifiHostpotEnabled(final Context pContext, final boolean pEnabled) throws WifiUtilsException {
		return WifiUtils.setWifiHostpotEnabled(pContext, null, pEnabled);
	}

	public static boolean setWifiHostpotEnabled(final Context pContext, final WifiConfiguration pWifiConfiguration, final boolean pEnabled) throws WifiUtilsException {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);

		try {
			if (pEnabled) {
				WifiUtils.setWifiEnabled(pContext, false);
			}

			final Method WifiManager_setWifiApEnabled = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
			if (WifiManager_setWifiApEnabled == null) {
				throw new WifiUtilsException(new MethodNotFoundException(WifiManager.class.getSimpleName() + ".setWifiApEnabled(" + WifiConfiguration.class.getSimpleName() + ", " + boolean.class.getSimpleName() + ")"));
			} else {
				final boolean result = (Boolean) WifiManager_setWifiApEnabled.invoke(wifiManager, pWifiConfiguration, pEnabled);
				return result;
			}
		} catch (final Throwable t) {
			throw new WifiUtilsException(t);
		}
	}

	public static WifiHotspotState getWifiHotspotState(final Context pContext) throws WifiUtilsException {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);

		try {
			final Method WifiManager_getWifiApState = wifiManager.getClass().getMethod("getWifiApState");
			if (WifiManager_getWifiApState == null) {
				throw new WifiUtilsException(new MethodNotFoundException(WifiManager.class.getSimpleName() + ".getWifiApState()"));
			} else {
				final int result = (Integer) WifiManager_getWifiApState.invoke(wifiManager);

				return WifiHotspotState.fromWifiApState(result);
			}
		} catch (final Throwable t) {
			throw new WifiUtilsException(t);
		}
	}

	public static boolean isWifiHotspotRunning() throws WifiException {
		try {
			final Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				final NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
				final String networkInterfaceName = networkInterface.getName();

				if (ArrayUtils.contains(WifiUtils.HOTSPOT_NETWORKINTERFACE_NAMES, networkInterfaceName)) {
					return true;
				}
			}
			return false;
		} catch (final SocketException e) {
			throw new WifiException("Unexpected error!", e);
		}
	}

	/**
	 * @return prefers to return an IPv4 address if found, otherwise an IPv6 address.
	 * @throws WifiException
	 */
	public static byte[] getWifiHotspotIPAddressRaw() throws WifiException {
		try {
			final Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				final NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
				final String networkInterfaceName = networkInterface.getName();

				if (ArrayUtils.contains(WifiUtils.HOTSPOT_NETWORKINTERFACE_NAMES, networkInterfaceName)) {
					byte[] ipv6Address = null;
					final Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
					while (inetAddressEnumeration.hasMoreElements()) {
						final byte[] ipAddress = inetAddressEnumeration.nextElement().getAddress();
						if (ipAddress.length == 4) { // TODO Constant!
							return ipAddress;
						} else {
							ipv6Address = ipAddress;
						}
					}
					if (ipv6Address != null) {
						return ipv6Address;
					} else {
						throw new WifiException("No IP bound to '" + Arrays.toString(WifiUtils.HOTSPOT_NETWORKINTERFACE_NAMES) + "'!");
					}
				}
			}
			throw new WifiException("No NetworInterface '" + Arrays.toString(WifiUtils.HOTSPOT_NETWORKINTERFACE_NAMES) + "' found!");
		} catch (final SocketException e) {
			throw new WifiException("Unexpected error!", e);
		}
	}

	public static String getWifiHotspotIPAddress() throws WifiException {
		try {
			return IPUtils.ipAddressToString(WifiUtils.getWifiHotspotIPAddressRaw());
		} catch (final UnknownHostException e) {
			throw new WifiException("Unexpected error!", e);
		}
	}

	public static boolean isWifiHotspotIPAddressValid() throws WifiException { // TODO!
		return !WifiUtils.IP_DEFAULT.equals(WifiUtils.getWifiHotspotIPAddress());
	}


	public static boolean isMulticastEnabled(final Context pContext) throws WifiUtilsException {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);

		try {
			final Method WifiManager_isMulticastEnabled = wifiManager.getClass().getMethod("isMulticastEnabled");
			if (WifiManager_isMulticastEnabled == null) {
				throw new WifiUtilsException(new MethodNotFoundException(WifiManager.class.getSimpleName() + ".isMulticastEnabled()"));
			} else {
				final boolean result = (Boolean) WifiManager_isMulticastEnabled.invoke(wifiManager);
				return result;
			}
		} catch (final Throwable t) {
			throw new WifiUtilsException(t);
		}
	}

	public static boolean isMulticastEnabled(final Context pContext, final boolean pDefault) {
		try {
			return WifiUtils.isMulticastEnabled(pContext);
		} catch (final WifiUtilsException e) {
			return pDefault;
		}
	}

	public static MulticastLock aquireMulticastLock(final Context pContext) {
		return WifiUtils.aquireMulticastLock(pContext, WifiUtils.MULTICASTLOCK_NAME_DEFAULT);
	}

	public static MulticastLock aquireMulticastLock(final Context pContext, final String pMulticastLockName) {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);
		final MulticastLock multicastLock = wifiManager.createMulticastLock(pMulticastLockName);
		multicastLock.setReferenceCounted(true);
		multicastLock.acquire();
		return multicastLock;
	}

	public static void releaseMulticastLock(final MulticastLock pMulticastLock) {
		if (pMulticastLock.isHeld()) {
			pMulticastLock.release();
		}
	}


	public static byte[] getBroadcastIPAddressRaw(final Context pContext) throws WifiException {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);
		final DhcpInfo dhcp = wifiManager.getDhcpInfo();
		// TODO handle null somehow...

		final int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		final byte[] broadcastIP = new byte[4];
		for (int k = 0; k < 4; k++) {
			broadcastIP[k] = (byte) ((broadcast >> (k * 8)) & 0xFF);
		}
		return broadcastIP;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public enum WifiHotspotState {
		// ===========================================================
		// Elements
		// ===========================================================

		DISABLING,
		DISABLED,
		ENABLING,
		ENABLED,
		FAILED;

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public static WifiHotspotState fromWifiApState(final int pWifiApState) throws WifiException {
			if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
				switch (pWifiApState) {
					case 0:
						return DISABLING;
					case 1:
						return DISABLED;
					case 2:
						return ENABLING;
					case 3:
						return ENABLED;
					case 4:
						return FAILED;
					default:
						throw new WifiException("TODO...");
				}
			} else {
				switch (pWifiApState) {
					case 0:
						return DISABLING;
					case 1:
						return DISABLED;
					case 2:
						return ENABLING;
					case 3:
						return ENABLED;
					case 4:
						return FAILED;
					default:
						throw new WifiException("TODO...");
				}
			}
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

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class WifiUtilsException extends AndEngineException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 1108697754015225179L;

		// ===========================================================
		// COnstructors
		// ===========================================================

		public WifiUtilsException() {

		}

		public WifiUtilsException(final Throwable pThrowable) {
			super(pThrowable);
		}
	}
}
