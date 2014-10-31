package org.andengine.examples.adt.messages.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.extension.multiplayer.protocol.adt.message.client.ClientMessage;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:23:37 - 21.05.2011
 */
public class ConnectionPingClientMessage extends ClientMessage implements ClientMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private long mTimestamp;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Deprecated
	public ConnectionPingClientMessage() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public long getTimestamp() {
		return this.mTimestamp;
	}

	public void setTimestamp(final long pTimestamp) {
		this.mTimestamp = pTimestamp;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_CLIENT_CONNECTION_PING;
	}

	@Override
	protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
		this.mTimestamp = pDataInputStream.readLong();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeLong(this.mTimestamp);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
