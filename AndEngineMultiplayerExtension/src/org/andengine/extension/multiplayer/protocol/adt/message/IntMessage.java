package org.andengine.extension.multiplayer.protocol.adt.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 13:38:26 - 19.09.2009
 */
public abstract class IntMessage extends Message {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected int mInt;

	// ===========================================================
	// Constructors
	// ===========================================================

	public IntMessage(final int pInt) {
		this.mInt = pInt;
	}

	public IntMessage(final DataInputStream pDataInputStream) throws IOException {
		this.read(pDataInputStream);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getInt() {
		return this.mInt;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		this.mInt = pDataInputStream.readInt();
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getInt()=").append(this.getInt());
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeInt(this.getInt());
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

		final IntMessage other = (IntMessage) obj;

		return this.getFlag() == other.getFlag() && this.getInt() == other.getInt();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
