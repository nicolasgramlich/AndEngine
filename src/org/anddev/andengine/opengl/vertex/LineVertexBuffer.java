package org.anddev.andengine.opengl.vertex;

import java.nio.FloatBuffer;

/**
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

	public LineVertexBuffer(final int pDrawType) {
		super(2 * VERTICES_PER_LINE * BYTES_PER_FLOAT, pDrawType);
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
		final FloatBuffer buffer = this.getFloatBuffer();
		buffer.position(0);
		// TODO Maybe use put(float []) instead of put(float) ...
		//
		buffer.put(pX1);
		buffer.put(pY1);

		buffer.put(pX2);
		buffer.put(pY2);

		buffer.position(0);

		super.update();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
