package org.anddev.andengine.entity.shape;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 11:51:27 - 13.03.2010
 */
public abstract class GLShape extends Shape {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public GLShape(final float pX, final float pY) {
		super(pX, pY);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	protected abstract void onUpdateVertexBuffer();
	protected abstract VertexBuffer getVertexBuffer();

	@Override
	protected void onApplyVertices(final GL10 pGL) {
		if(GLHelper.EXTENSIONS_VERTEXBUFFEROBJECTS) {
			final GL11 gl11 = (GL11)pGL;

			this.getVertexBuffer().selectOnHardware(gl11);
			GLHelper.vertexZeroPointer(gl11);
		} else {
			GLHelper.vertexPointer(pGL, this.getVertexBuffer().getFloatBuffer());
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	protected void updateVertexBuffer() {
		this.onUpdateVertexBuffer();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
