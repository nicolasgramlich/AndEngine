package org.andengine.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.andengine.util.exception.AndEngineException;
import org.andengine.util.system.SystemUtils;

import android.annotation.TargetApi;
import android.os.Build;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 16:54:01 - 20.03.2013
 */
public final class EthernetUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String IP_DEFAULT = "0.0.0.0";

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private EthernetUtils() {

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

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static byte[] getEthernetIPAddressRaw() throws EthernetUtilsException {
		try {
			byte[] ipv6Address = null;

			final Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				final NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
				if (SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.FROYO) || !networkInterface.isLoopback()) {
					final Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
					while (inetAddressEnumeration.hasMoreElements()) {
						final InetAddress inetAddress = inetAddressEnumeration.nextElement();
	
						if (!inetAddress.isLoopbackAddress()) {
							final byte[] ipAddress = inetAddress.getAddress();
							if (ipAddress.length == IPUtils.IPV4_LENGTH) {
								return ipAddress;
							} else {
								ipv6Address = ipAddress;
							}
						}
					}
				}
			}

			if (ipv6Address != null) {
				return ipv6Address;
			} else {
				throw new EthernetUtilsException("No ethernet IP found that is not bound to localhost!");
			}
		} catch (final SocketException e) {
			throw new EthernetUtilsException("Unexpected error!", e);
		}
	}

	public static String getEthernetIPAddress() throws EthernetUtilsException {
		try {
			return IPUtils.ipAddressToString(EthernetUtils.getEthernetIPAddressRaw());
		} catch (final UnknownHostException e) {
			throw new EthernetUtilsException("Unexpected error!", e);
		}
	}

	public static boolean getEthernetIPAddressValid() throws EthernetUtilsException { // TODO!
		return !EthernetUtils.IP_DEFAULT.equals(EthernetUtils.getEthernetIPAddress());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class EthernetUtilsException extends AndEngineException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 795214345441314983L;

		// ===========================================================
		// Constructors
		// ===========================================================

		public EthernetUtilsException() {

		}

		public EthernetUtilsException(final Throwable pThrowable) {
			super(pThrowable);
		}

		private EthernetUtilsException(final String pMessage) {
			super(pMessage);
		}

		private EthernetUtilsException(final String pMessage, final Throwable pThrowable) {
			super(pMessage, pThrowable);
		}
	}
}
