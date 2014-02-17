package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Line;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:45:00 - 28.03.2012
 */
public class HighPerformanceLineVertexBufferObject extends HighPerformanceVertexBufferObject implements ILineVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceLineVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdateColor(final Line pLine) {
		final float[] bufferData = this.mBufferData;

		final float packedColor = pLine.getColor().getABGRPackedFloat();

		bufferData[0 * Line.VERTEX_SIZE + Line.COLOR_INDEX] = packedColor;
		bufferData[1 * Line.VERTEX_SIZE + Line.COLOR_INDEX] = packedColor;

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(final Line pLine) {
		final float[] bufferData = this.mBufferData;

		bufferData[0 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_X] = 0;
		bufferData[0 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_Y] = 0;

		bufferData[1 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_X] = pLine.getX2() - pLine.getX1();
		bufferData[1 * Line.VERTEX_SIZE + Line.VERTEX_INDEX_Y] = pLine.getY2() - pLine.getY1();

		this.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}