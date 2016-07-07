package org.andengine.opengl.util;

import org.andengine.util.adt.pool.GenericPool;

public class BufferSubset
{
	private int mOffset;
	private int mSize;

	private static final BufferSubsetPool SUBSET_POOL = new BufferSubsetPool();
	
	public static BufferSubset obtain(int pOffset, int pSize)
	{
		final BufferSubset subset = SUBSET_POOL.obtainPoolItem();
		subset.set(pOffset, pSize);
		return subset;
	}

	private void set(int pOffset, int pSize) {
		this.mOffset = pOffset;
		this.mSize = pSize;
	}
	
	public void recycle() {
		SUBSET_POOL.recyclePoolItem(this);
	}

	public static void recycle(final BufferSubset pBufferSubset) {
		SUBSET_POOL.recyclePoolItem(pBufferSubset);
	}
	
	public int getOffset() {
		return mOffset;
	}

	public int getSize() {
		return mSize;
	}
	
	private static final class BufferSubsetPool extends GenericPool<BufferSubset> {
		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		protected BufferSubset onAllocatePoolItem() {
			return new BufferSubset();
		}
	}	
}
