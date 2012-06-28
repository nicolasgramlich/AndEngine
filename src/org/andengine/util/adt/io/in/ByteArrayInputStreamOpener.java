package org.andengine.util.adt.io.in;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:51:10 - 16.03.2012
 */
public class ByteArrayInputStreamOpener implements IInputStreamOpener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final byte[] mBytes;
	private final int mOffset;
	private final int mLength;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ByteArrayInputStreamOpener(final byte[] pBytes) {
		this(pBytes, 0, pBytes.length);
	}

	public ByteArrayInputStreamOpener(final byte[] pBytes, final int pOffset, final int pLength) {
		this.mBytes = pBytes;
		this.mOffset = pOffset;
		this.mLength = pLength;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public InputStream open() throws IOException {
		return new ByteArrayInputStream(this.mBytes, this.mOffset, this.mLength);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
