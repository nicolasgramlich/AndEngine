package org.andengine.examples.game.pong.adt.messages.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.andengine.examples.game.pong.util.constants.PongConstants;
import org.andengine.extension.multiplayer.protocol.adt.message.server.ServerMessage;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 19:48:32 - 28.02.2011
 */
public class SetPaddleIDServerMessage extends ServerMessage implements PongConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	public int mPaddleID;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SetPaddleIDServerMessage() {

	}

	public SetPaddleIDServerMessage(final int pPaddleID) {
		this.mPaddleID = pPaddleID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void set(final int pPaddleID) {
		this.mPaddleID = pPaddleID;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public short getFlag() {
		return FLAG_MESSAGE_SERVER_SET_PADDLEID;
	}

	@Override
	protected void onReadTransmissionData(DataInputStream pDataInputStream) throws IOException {
		this.mPaddleID = pDataInputStream.readInt();
	}

	@Override
	protected void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.mPaddleID);
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}