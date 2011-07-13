package org.anddev.andengine.util.cache;

import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.vertex.RectangleVertexBuffer;
import org.anddev.andengine.util.MultiKey;
import org.anddev.andengine.util.MultiKeyHashMap;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 21:10:05 - 09.01.2011
 */
public class RectangleVertexBufferCache {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mDrawType;

	private final MultiKeyHashMap<Integer, RectangleVertexBuffer> mRectangleVertexBufferCache = new MultiKeyHashMap<Integer, RectangleVertexBuffer>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangleVertexBufferCache() {
		this(GL11.GL_STATIC_DRAW);
	}

	public RectangleVertexBufferCache(final int pDrawType) {
		this.mDrawType = pDrawType;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public RectangleVertexBuffer get(final int pWidth, final int pHeight) {
		final RectangleVertexBuffer cachedRectangleVertexBuffer = this.mRectangleVertexBufferCache.get(pWidth, pHeight);
		if(cachedRectangleVertexBuffer != null) {
			return cachedRectangleVertexBuffer;
		} else {
			return this.put(pWidth, pHeight, new RectangleVertexBuffer(this.mDrawType, false));
		}
	}

	public RectangleVertexBuffer put(final int pWidth, final int pHeight, final RectangleVertexBuffer pRectangleVertexBuffer) {
		pRectangleVertexBuffer.update(pWidth, pHeight);
		BufferObjectManager.getActiveInstance().loadBufferObject(pRectangleVertexBuffer);
		this.mRectangleVertexBufferCache.put(new MultiKey<Integer>(pWidth, pHeight), pRectangleVertexBuffer);
		return pRectangleVertexBuffer;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
