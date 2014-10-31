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
 * @since 12:00:31 - 21.05.2011
 */
public class ConnectionEstablishClientMessage extends ClientMessage implements ClientMessageFlags {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private short mProtocolVersion;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Deprecated
	public ConnectionEstablishClientMessage() {

	}

	public ConnectionEstablishClientMessage(final short pProtocolVersion) {
		this.mProtocolVersion = pProtocolVersion;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public short getProtocolVersion() {
		return this.mProtocolVersion;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return ClientMessageFlags.FLAG_MESSAGE_CLIENT_CONNECTION_ESTABLISH;
	}

	@Override
	protected void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException {
		this.mProtocolVersion = pDataInputStream.readShort();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeShort(this.mProtocolVersion);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
