package org.andengine.extension.multiplayer.protocol.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.andengine.extension.multiplayer.protocol.exception.BluetoothException;
import org.andengine.extension.multiplayer.protocol.util.Bluetooth;
import org.andengine.util.debug.Debug;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:40:51 - 18.09.2009
 */
public class BluetoothSocketConnection extends Connection {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final BluetoothSocket mBluetoothSocket;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BluetoothSocketConnection(final BluetoothAdapter pBluetoothAdapter, final String pMacAddress, final String pUUID) throws IOException, BluetoothException {
		this(pBluetoothAdapter.getRemoteDevice(pMacAddress), pUUID);
	}

	public BluetoothSocketConnection(final BluetoothDevice pBluetoothDevice, final String pUUID) throws IOException, BluetoothException {
		this(pBluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(pUUID)));
	}

	public BluetoothSocketConnection(final BluetoothSocket pBluetoothSocket) throws IOException, BluetoothException {
		super(new DataInputStream(pBluetoothSocket.getInputStream()), new DataOutputStream(pBluetoothSocket.getOutputStream()));

		this.mBluetoothSocket = pBluetoothSocket;

		if (Bluetooth.isSupportedByAndroidVersion() == false) {
			throw new BluetoothException();
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BluetoothSocket getBluetoothSocket() {
		return this.mBluetoothSocket;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onTerminate() {
		/* Ensure Socket is really closed. */
		try {
			this.mBluetoothSocket.close(); // TODO Put to SocketUtils
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
