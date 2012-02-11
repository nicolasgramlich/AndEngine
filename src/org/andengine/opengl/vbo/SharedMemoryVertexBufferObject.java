package org.andengine.opengl.vbo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.locks.ReentrantLock;

import org.andengine.opengl.util.BufferUtils;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.util.system.SystemUtils;

import android.os.Build;

/**
 * Compared to {@link ZeroMemoryVertexBufferObject}, all {@link SharedMemoryVertexBufferObject}s share a single {@link ByteBuffer} which is used by whichever {@link SharedMemoryVertexBufferObject} instance is currently buffering data,
 * at the cost of expensive data buffering (<b>up to <u>5x</u> slower!</b>) and a little synchronization overhead.
 * <p/>
 * Usually a {@link SharedMemoryVertexBufferObject} is preferred to a {@link ZeroMemoryVertexBufferObject} when the following conditions need to be met:
 * <ol>
 * <li>Minimum amount of runtime GarbageCollector activity.</li>
 * </ol>
 * <p/>
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @author Greg Haynes
 * @since 19:22:13 - 10.02.2012
 */
public abstract class SharedMemoryVertexBufferObject extends ZeroMemoryVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	private static ReentrantLock sSharedByteBufferLock = new ReentrantLock(true);
	private static ByteBuffer sSharedByteBuffer;

	public static int getSharedByteBufferByteCapacity() {
		final int byteCapacity;
		try {
			SharedMemoryVertexBufferObject.sSharedByteBufferLock.lock();

			final ByteBuffer sharedByteBuffer = SharedMemoryVertexBufferObject.sSharedByteBuffer;
			if(sharedByteBuffer == null) {
				byteCapacity = 0;
			} else {
				byteCapacity = sharedByteBuffer.capacity();
			}
		} finally {
			SharedMemoryVertexBufferObject.sSharedByteBufferLock.unlock();
		}

		return byteCapacity;
	}

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SharedMemoryVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, false, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void dispose() {
		super.dispose();

		try {
			SharedMemoryVertexBufferObject.sSharedByteBufferLock.lock();
	
			if(SharedMemoryVertexBufferObject.sSharedByteBuffer != null) {
				if(SystemUtils.isAndroidVersion(Build.VERSION_CODES.HONEYCOMB, Build.VERSION_CODES.HONEYCOMB_MR2)) {
					/* Cleanup due to 'Honeycomb workaround for issue 16941' in constructor. */
					BufferUtils.freeDirect(SharedMemoryVertexBufferObject.sSharedByteBuffer);
				}

				SharedMemoryVertexBufferObject.sSharedByteBuffer = null;
			}
		} finally {
			SharedMemoryVertexBufferObject.sSharedByteBufferLock.unlock();
		}
	}

	@Override
	protected ByteBuffer aquireByteBuffer() {
		SharedMemoryVertexBufferObject.sSharedByteBufferLock.lock();

		final int byteCapacity = this.getByteCapacity();

		if(SharedMemoryVertexBufferObject.sSharedByteBuffer == null || SharedMemoryVertexBufferObject.sSharedByteBuffer.capacity() < byteCapacity) {
			if(SystemUtils.isAndroidVersion(Build.VERSION_CODES.HONEYCOMB, Build.VERSION_CODES.HONEYCOMB_MR2)) {
				/* Cleanup due to 'Honeycomb workaround for issue 16941' in constructor. */
				if(SharedMemoryVertexBufferObject.sSharedByteBuffer != null) {
					BufferUtils.freeDirect(SharedMemoryVertexBufferObject.sSharedByteBuffer);
				}

				/* Honeycomb workaround for issue 16941. */
				SharedMemoryVertexBufferObject.sSharedByteBuffer = BufferUtils.allocateDirect(byteCapacity);
			} else {
				/* Other SDK versions. */
				SharedMemoryVertexBufferObject.sSharedByteBuffer = ByteBuffer.allocateDirect(byteCapacity);
			}

			SharedMemoryVertexBufferObject.sSharedByteBuffer.order(ByteOrder.nativeOrder());
		}

		SharedMemoryVertexBufferObject.sSharedByteBuffer.limit(byteCapacity);

		return SharedMemoryVertexBufferObject.sSharedByteBuffer;
	}

	@Override
	protected void releaseByteBuffer(final ByteBuffer byteBuffer) {
		SharedMemoryVertexBufferObject.sSharedByteBufferLock.unlock();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
