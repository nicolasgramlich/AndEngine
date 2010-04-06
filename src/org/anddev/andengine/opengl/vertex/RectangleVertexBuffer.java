package org.anddev.andengine.opengl.vertex;

import java.nio.FloatBuffer;

/**
 * @author Nicolas Gramlich
 * @since 13:07:25 - 13.03.2010
 */
public class RectangleVertexBuffer extends VertexBuffer {
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final int VERTICES_PER_RECTANGLE = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public RectangleVertexBuffer() {
		super(2 * VERTICES_PER_RECTANGLE * BYTES_PER_FLOAT);
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

	public void update(final float pX, final float pY, final float pWidth, final float pHeight) {
		final FloatBuffer buffer = this.getFloatBuffer();
		buffer.position(0);

		buffer.put(pX);
		buffer.put(pY);

		buffer.put(pX + pWidth);
		buffer.put(pY);

		buffer.put(pX);
		buffer.put(pY + pHeight);

		buffer.put(pX + pWidth);
		buffer.put(pY + pHeight);

		buffer.position(0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
