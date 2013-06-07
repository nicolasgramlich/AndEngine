package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.LineChain;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:50:00 - 30.05.2013
 */
public class HighPerformanceLineChainVertexBufferObject extends HighPerformanceVertexBufferObject implements ILineChainVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceLineChainVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdateColor(final LineChain pLineChain) {
		final float[] bufferData = this.mBufferData;

		final float packedColor = pLineChain.getColor().getABGRPackedFloat();

		for (int i = pLineChain.getCapacity() - 1; i >= 0; i--) {
			bufferData[(i * LineChain.VERTEX_SIZE) + LineChain.COLOR_INDEX] = packedColor;
		}

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(final LineChain pLineChain) {
		final float[] bufferData = this.mBufferData;

		bufferData[(0 * LineChain.VERTEX_SIZE) + LineChain.VERTEX_INDEX_X] = 0;
		bufferData[(0 * LineChain.VERTEX_SIZE) + LineChain.VERTEX_INDEX_Y] = 0;

		final float x = pLineChain.getX();
		final float y = pLineChain.getY();

		for (int i = pLineChain.getCapacity() - 1; i >= 0; i--) {
			bufferData[(i * LineChain.VERTEX_SIZE) + LineChain.VERTEX_INDEX_X] = pLineChain.getX(i) - x;
			bufferData[(i * LineChain.VERTEX_SIZE) + LineChain.VERTEX_INDEX_Y] = pLineChain.getY(i) - y;
		}

		this.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}