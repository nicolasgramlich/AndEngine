package org.andengine.util.adt.io.out;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * TODO Instead of having mMaximumGrow there could be some kind of AllocationStrategy object.
 *
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 02:19:02 - 14.08.2011
 */
public class ByteBufferOutputStream extends OutputStream {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mMaximumGrow;

	protected byte mData[];
	protected int mCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ByteBufferOutputStream(final int pInitialCapacity, final int pMaximumGrow) {
		this.mMaximumGrow = pMaximumGrow;
		this.mData = new byte[pInitialCapacity];
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void write(final int pByte) {
		this.ensureCapacity(this.mCount + 1);
		this.mData[this.mCount] = (byte) pByte;
		this.mCount++;
	}

	@Override
	public void write(final byte pData[], final int pOffset, final int pLength) {
		this.ensureCapacity(this.mCount + pLength);
		System.arraycopy(pData, pOffset, this.mData, this.mCount, pLength);
		this.mCount += pLength;
	}

	@Override
	public void close() throws IOException {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void ensureCapacity(final int pDesiredCapacity) {
		if(pDesiredCapacity - this.mData.length > 0) {
			this.grow(pDesiredCapacity);
		}
	}

	private void grow(final int pDesiredCapacity) {
		final int oldCapacity = this.mData.length;
		final int grow = Math.min(this.mMaximumGrow, oldCapacity);
		int newCapacity = oldCapacity + grow;

		if(newCapacity - pDesiredCapacity < 0) {
			newCapacity = pDesiredCapacity;
		}
		if(newCapacity < 0) {
			if(pDesiredCapacity < 0) {
				throw new OutOfMemoryError();
			} else {
				newCapacity = Integer.MAX_VALUE;
			}
		}

		final byte[] data = new byte[newCapacity];
		System.arraycopy(this.mData, 0, data, 0, this.mCount);
		this.mData = data;
	}

	public ByteBuffer toByteBuffer() {
		return ByteBuffer.wrap(this.mData, 0, this.mCount).slice();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}