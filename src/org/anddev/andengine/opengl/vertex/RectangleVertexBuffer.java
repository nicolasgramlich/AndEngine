package org.anddev.andengine.opengl.vertex;

import org.anddev.andengine.opengl.util.FastFloatBuffer;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:07:25 - 13.03.2010
 */
public class RectangleVertexBuffer extends VertexBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int VERTICES_PER_RECTANGLE = 4;

	private static final int FLOAT_TO_RAW_INT_BITS_ZERO = Float.floatToRawIntBits(0);

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RectangleVertexBuffer(final int pDrawType, final boolean pManaged) {
		super(2 * VERTICES_PER_RECTANGLE, pDrawType, pManaged);
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

	public synchronized void update(final float pWidth, final float pHeight) {
		final int x = FLOAT_TO_RAW_INT_BITS_ZERO;
		final int y = FLOAT_TO_RAW_INT_BITS_ZERO;
		final int x2 = Float.floatToRawIntBits(pWidth);
		final int y2 = Float.floatToRawIntBits(pHeight);

		final int[] bufferData = this.mBufferData;
		bufferData[0] = x;
		bufferData[1] = y;

		bufferData[2] = x;
		bufferData[3] = y2;

		bufferData[4] = x2;
		bufferData[5] = y;

		bufferData[6] = x2;
		bufferData[7] = y2;

		final FastFloatBuffer buffer = this.getFloatBuffer();
		buffer.position(0);
		buffer.put(bufferData);
		buffer.position(0);

		super.setHardwareBufferNeedsUpdate();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
