package org.andengine.extension.multiplayer.protocol.client.connector;

import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.client.IServerMessageReader;
import org.andengine.extension.multiplayer.protocol.shared.SocketConnection;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:45:57 - 04.03.2011
 */
public class SocketConnectionServerConnector extends ServerConnector<SocketConnection> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SocketConnectionServerConnector(final SocketConnection pConnection, final ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
		super(pConnection, pSocketConnectionServerConnectorListener);
	}

	public SocketConnectionServerConnector(final SocketConnection pConnection, final IServerMessageReader<SocketConnection> pServerMessageReader, final ISocketConnectionServerConnectorListener pSocketConnectionServerConnectorListener) throws IOException {
		super(pConnection, pServerMessageReader, pSocketConnectionServerConnectorListener);
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

	public static interface ISocketConnectionServerConnectorListener extends IServerConnectorListener<SocketConnection> {

	}

	public static class DefaultSocketConnectionServerConnectorListener implements ISocketConnectionServerConnectorListener {
		@Override
		public void onStarted(final ServerConnector<SocketConnection> pServerConnector) {
			Debug.d("Accepted Server-Connection from: '" + pServerConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}

		@Override
		public void onTerminated(final ServerConnector<SocketConnection> pServerConnector) {
			Debug.d("Closed Server-Connection from: '" + pServerConnector.getConnection().getSocket().getInetAddress().getHostAddress());
		}
	}
}
