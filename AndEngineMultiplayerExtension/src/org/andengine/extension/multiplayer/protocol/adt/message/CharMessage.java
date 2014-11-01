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
public abstract class CharMessage extends Message {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected char mChar;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CharMessage(final char pChar) {
		this.mChar = pChar;
	}

	public CharMessage(final DataInputStream pDataInputStream) throws IOException {
		this.read(pDataInputStream);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public char getChar() {
		return this.mChar;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void read(final DataInputStream pDataInputStream) throws IOException {
		this.mChar = pDataInputStream.readChar();
	}

	@Override
	protected void onAppendTransmissionDataForToString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(", getChar()=").append('\'').append(this.getChar()).append('\'');
	}

	@Override
	public void onWriteTransmissionData(final DataOutputStream pDataOutputStream) throws IOException {
		pDataOutputStream.writeChar(this.getChar());
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

		final CharMessage other = (CharMessage) obj;

		return this.getFlag() == other.getFlag() && this.getChar() == other.getChar();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
