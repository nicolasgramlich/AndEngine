package org.andengine.extension.multiplayer.protocol.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 17:16:20 - 20.06.2010
 */
public class IPUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String LOCALHOST_IP = "127.0.0.1";

	private static final String REGEXP_255 = "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)"; // 25(<=5) or 2(<=4)(<=9) or (0|1)(<=9)(<=9)
	public static final String REGEXP_IPv4 = REGEXP_255 + "\\." + REGEXP_255 + "\\." + REGEXP_255 + "\\." + REGEXP_255;

	private static final Pattern IPv4_PATTERN = Pattern.compile(REGEXP_IPv4);

	public static final int IPV4_LENGTH = 4;
	public static final int IPV6_LENGTH = 16;

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

	// ===========================================================
	// Methods
	// ===========================================================

	public static byte[] ipv4AddressToIPAddress(final int pIPv4Address) {
		final byte[] ip = new byte[4];
		ip[0] = (byte)((pIPv4Address >> 0) & 0xff);
		ip[1] = (byte)((pIPv4Address >> 8) & 0xff);
		ip[2] = (byte)((pIPv4Address >> 16) & 0xff);
		ip[3] = (byte)((pIPv4Address >> 24) & 0xff);
		return ip;
	}

	public static String ipAddressToString(final byte[] pIPAddress) throws UnknownHostException {
		return InetAddress.getByAddress(pIPAddress).getHostAddress();
	}

	public static byte[] stringToIPAddress(final String pString) throws UnknownHostException {
		return InetAddress.getByName(pString).getAddress();
	}

	public static boolean isValidIPv4(final String pIPv4Address) {
		return IPv4_PATTERN.matcher(pIPv4Address).matches();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
