package org.anddev.andengine.opengl.vertex;

import org.anddev.andengine.opengl.util.FastFloatBuffer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:07:25 - 13.03.2010
 */
public class LineVertexBuffer extends VertexBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTICES_PER_LINE = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LineVertexBuffer(final int pDrawType, final boolean pManaged) {
		super(2 * VERTICES_PER_LINE, pDrawType, pManaged);
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

	public synchronized void update(final float pX1, final float pY1, final float pX2, final float pY2) {
		final int[] bufferData = this.mBufferData;

		bufferData[0]  = Float.floatToRawIntBits(pX1);
		bufferData[1]  = Float.floatToRawIntBits(pY1);

		bufferData[2]  = Float.floatToRawIntBits(pX2);
		bufferData[3]  = Float.floatToRawIntBits(pY2);

		final FastFloatBuffer buffer = this.mFloatBuffer;
		buffer.position(0);
		buffer.put(bufferData);
		buffer.position(0);

		super.setHardwareBufferNeedsUpdate();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
