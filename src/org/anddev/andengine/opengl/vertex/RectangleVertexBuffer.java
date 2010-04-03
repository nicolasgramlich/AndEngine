package org.anddev.andengine.opengl.vertex;

import java.nio.ByteBuffer;

/**
 * @author Nicolas Gramlich
 * @since 13:07:25 - 13.03.2010
 */
public class RectangleVertexBuffer extends VertexBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public RectangleVertexBuffer() {
		super(8 * BYTES_PER_FLOAT);
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
		final ByteBuffer buffer = this.getByteBuffer();
		buffer.position(0);

		buffer.putFloat(pX);
		buffer.putFloat(pY);

		buffer.putFloat(pX + pWidth);
		buffer.putFloat(pY);

		buffer.putFloat(pX);
		buffer.putFloat(pY + pHeight);

		buffer.putFloat(pX + pWidth);
		buffer.putFloat(pY + pHeight);

		buffer.position(0);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
