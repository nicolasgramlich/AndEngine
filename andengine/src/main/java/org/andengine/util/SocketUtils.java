package org.andengine.util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:42:15 - 18.09.2009
 */
public final class SocketUtils {
	// ===========================================================
	// Constants
	// ===========================================================

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

	public static final void closeSocket(final DatagramSocket pDatagramSocket) {
		if(pDatagramSocket != null && !pDatagramSocket.isClosed()) {
			pDatagramSocket.close();
		}
	}

	public static final void closeSocket(final Socket pSocket) {
		if(pSocket != null && !pSocket.isClosed()) {
			try {
				pSocket.close();
			} catch (final IOException e) {
				Debug.e(e);
			}
		}
	}

	public static final void closeSocket(final ServerSocket pServerSocket) {
		if(pServerSocket != null && !pServerSocket.isClosed()) {
			try {
				pServerSocket.close();
			} catch (final IOException e) {
				Debug.e(e);
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
