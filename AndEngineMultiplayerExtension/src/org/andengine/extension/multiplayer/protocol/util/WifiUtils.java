package org.andengine.extension.multiplayer.protocol.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.andengine.extension.multiplayer.protocol.exception.WifiException;
import org.andengine.util.system.SystemUtils;

import android.content.Context;
import android.net.DhcpInfo;
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
public class WifiUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String IP_DEFAULT = "0.0.0.0";
	private static final String HOTSPOT_NETWORKINTERFACE_NAME_DEFAULT = "wl0.1";
	private static final String MULTICASTLOCK_NAME_DEFAULT = "AndEngineMultiplayerExtensionMulticastLock";

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public static WifiManager getWifiManager(final Context pContext) {
		return (WifiManager) pContext.getSystemService(Context.WIFI_SERVICE);
	}

	public static boolean isWifiEnabled(final Context pContext) {
		return WifiUtils.getWifiManager(pContext).isWifiEnabled();
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
	public static boolean isHotspotSupported() {
		return SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.FROYO);
	}

	public static boolean isHotspotRunning() throws WifiException {
		try {
			final Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while(networkInterfaceEnumeration.hasMoreElements()) {
				final NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
				final String networkInterfaceNname = networkInterface.getName();

				if (networkInterfaceNname.equals(HOTSPOT_NETWORKINTERFACE_NAME_DEFAULT)) {
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
	public static byte[] getHotspotIPAddressRaw() throws WifiException {
		try {
			final Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while(networkInterfaceEnumeration.hasMoreElements()) {
				final NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
				final String networkInterfaceNname = networkInterface.getName();

				if (networkInterfaceNname.equals(HOTSPOT_NETWORKINTERFACE_NAME_DEFAULT)) {
					byte[] ipv6Address = null;
					final Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
					while(inetAddressEnumeration.hasMoreElements()) {
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
						throw new WifiException("No IP bound to '" + HOTSPOT_NETWORKINTERFACE_NAME_DEFAULT + "'!");
					}
				}
			}
			throw new WifiException("No NetworInterface '" + HOTSPOT_NETWORKINTERFACE_NAME_DEFAULT + "' found!");
		} catch (final SocketException e) {
			throw new WifiException("Unexpected error!", e);
		}
	}

	public static String getHotspotIPAddress() throws WifiException {
		try {
			return IPUtils.ipAddressToString(WifiUtils.getHotspotIPAddressRaw());
		} catch (final UnknownHostException e) {
			throw new WifiException("Unexpected error!", e);
		}
	}

	public static boolean isHotspotIPAddressValid() throws WifiException { // TODO!
		return !IP_DEFAULT.equals(WifiUtils.getHotspotIPAddress());
	}

	public static byte[] getBroadcastIPAddressRaw(final Context pContext) throws WifiException {
		final WifiManager wifiManager = WifiUtils.getWifiManager(pContext);
		final DhcpInfo dhcp = wifiManager.getDhcpInfo();
		// TODO handle null somehow...

		final int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		final byte[] broadcastIP = new byte[4];
		for (int k = 0; k < 4; k++) {
			broadcastIP[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		}
		return broadcastIP;
	}

	public static MulticastLock aquireMulticastLock(final Context pContext) {
		return aquireMulticastLock(pContext, MULTICASTLOCK_NAME_DEFAULT);
	}

	public static MulticastLock aquireMulticastLock(final Context pContext, final String pMulticastLockName) {
		MulticastLock multicastLock = getWifiManager(pContext).createMulticastLock(pMulticastLockName);
		multicastLock.setReferenceCounted(true);
		multicastLock.acquire();
		return multicastLock;
	}

	public static void releaseMulticastLock(final MulticastLock pMulticastLock) {
		if (pMulticastLock.isHeld()) {
			pMulticastLock.release();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
