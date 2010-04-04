package org.anddev.andengine.opengl.vertex;

import java.nio.ByteBuffer;

/**
 * @author Nicolas Gramlich
 * @since 13:07:25 - 13.03.2010
 */
public class LineVertexBuffer extends VertexBuffer {
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final int VERTICES_PER_LINE = 2;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public LineVertexBuffer() {
		super(2 * VERTICES_PER_LINE * BYTES_PER_FLOAT);
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

	public void update(final float pX1, final float pY1, final float pX2, final float pY2) {
		final ByteBuffer buffer = this.getByteBuffer();
		buffer.position(0);

		buffer.putFloat(pX1);
		buffer.putFloat(pY1);

		buffer.putFloat(pX2);
		buffer.putFloat(pY2);

		buffer.position(0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
