package org.anddev.andengine.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Nicolas Gramlich
 * @since 14:42:15 - 18.09.2009
 */
public class SocketUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String SOCKETEXCEPTION_MESSAGE_SOCKET_CLOSED = "socket closed";
	public static final String SOCKETEXCEPTION_MESSAGE_SOCKET_IS_CLOSED = "Socket is closed";

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

	public static void closeSocket(final Socket pSocket) {
		if(pSocket != null && !pSocket.isClosed()) {
			try {
				pSocket.close();
			} catch (final IOException e) {
				System.err.println(e.getStackTrace());
			}
		}
	}

	public static void closeSocket(final ServerSocket pSocket) {
		if(pSocket != null && !pSocket.isClosed()) {
			try {
				pSocket.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
