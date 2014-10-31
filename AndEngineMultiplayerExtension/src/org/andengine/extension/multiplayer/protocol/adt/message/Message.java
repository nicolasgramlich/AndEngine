package org.andengine.extension.multiplayer.protocol.adt.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:27:13 - 18.09.2009
 */
public abstract class Message implements IMessage {
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

	protected abstract void onReadTransmissionData(final DataInputStream pDataInputStream) throws IOException;
	protected abstract void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException;

	/**
	 * For debugging purposes, append all data of this {@link Message} to the {@link StringBuilder}.
	 * @param pStringBuilder
	 */
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		/* Nothing by default. */
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append(this.getClass().getSimpleName()).append("[getFlag()=").append(this.getFlag());

		this.onAppendTransmissionDataForToString(sb);

		sb.append("]");

		return sb.toString();
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}

		final Message other = (Message) obj;

		return this.getFlag() == other.getFlag();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public void write(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeShort(this.getFlag());
		this.onWriteTransmissionData(pDataOutputStream);
	}

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		this.onReadTransmissionData(pDataInputStream);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
