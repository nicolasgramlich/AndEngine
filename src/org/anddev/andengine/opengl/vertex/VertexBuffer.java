package org.anddev.andengine.opengl.vertex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import android.os.Build;
/**
 * @author Nicolas Gramlich
 * @since 12:16:18 - 09.03.2010
 */
public class VertexBuffer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private ByteBuffer mByteBuffer;

	// ===========================================================
	// Constructors
	// ===========================================================

	public VertexBuffer() {
		allocateByteBuffer();
		this.mByteBuffer.order(ByteOrder.nativeOrder());
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ByteBuffer getByteBuffer() {
		return this.mByteBuffer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private void allocateByteBuffer() {
		if(Build.VERSION.SDK == "3") {
			this.mByteBuffer = ByteBuffer.allocate(8*4);
		} else {
			this.mByteBuffer = ByteBuffer.allocateDirect(8*4);
		}
	}

	public void update(final float pX, final float pY, final float pWidth, final float pHeight) {
		final ByteBuffer buffer = this.mByteBuffer;
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

