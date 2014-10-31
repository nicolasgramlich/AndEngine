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
public abstract class LongMessage extends Message {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected long mLong;

	// ===========================================================
	// Constructors
	// ===========================================================

	public LongMessage(final long pLong) {
		this.mLong = pLong;
	}

	public LongMessage(final DataInputStream pDataInputStream) throws IOException {
		this.read(pDataInputStream);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public long getLong() {
		return this.mLong;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		this.mLong = pDataInputStream.readLong();
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getLong()=").append(this.getLong());
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeLong(this.getLong());
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

		final LongMessage other = (LongMessage) obj;

		return this.getFlag() == other.getFlag() && this.getLong() == other.getLong();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
