package org.andengine.extension.multiplayer.protocol.client.connector;

import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.client.IServerMessageReader;
import org.andengine.extension.multiplayer.protocol.shared.BluetoothSocketConnection;
import org.andengine.util.debug.Debug;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:45:57 - 04.03.2011
 */
public class BluetoothSocketConnectionServerConnector extends ServerConnector<BluetoothSocketConnection> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public BluetoothSocketConnectionServerConnector(final BluetoothSocketConnection pBluetoothSocketConnection, final IBluetoothSocketConnectionServerConnectorListener pBlutetoothSocketConnectionServerConnectorListener) throws IOException {
		super(pBluetoothSocketConnection, pBlutetoothSocketConnectionServerConnectorListener);
	}

	public BluetoothSocketConnectionServerConnector(final BluetoothSocketConnection pBluetoothSocketConnection, final IServerMessageReader<BluetoothSocketConnection> pServerMessageReader, final IBluetoothSocketConnectionServerConnectorListener pBlutetoothSocketConnectionServerConnectorListener) throws IOException {
		super(pBluetoothSocketConnection, pServerMessageReader, pBlutetoothSocketConnectionServerConnectorListener);
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

	public static interface IBluetoothSocketConnectionServerConnectorListener extends IServerConnectorListener<BluetoothSocketConnection> {

	}

	public static class DefaultBluetoothConnectionSocketServerConnectorListener implements IBluetoothSocketConnectionServerConnectorListener {
		@Override
		public void onStarted(final ServerConnector<BluetoothSocketConnection> pServerConnector) {
			Debug.d("Accepted Server-Connection from: '" + pServerConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
		}

		@Override
		public void onTerminated(final ServerConnector<BluetoothSocketConnection> pServerConnector) {
			Debug.d("Closed Server-Connection from: '" + pServerConnector.getConnection().getBluetoothSocket().getRemoteDevice().getAddress());
		}
	}
}
