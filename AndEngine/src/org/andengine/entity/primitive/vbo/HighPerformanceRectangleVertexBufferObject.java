package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:48:14 - 28.03.2012
 */
public class HighPerformanceRectangleVertexBufferObject extends HighPerformanceVertexBufferObject implements IRectangleVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceRectangleVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdateColor(final Rectangle pRectangle) {
		final float[] bufferData = this.mBufferData;

		final float packedColor = pRectangle.getColor().getABGRPackedFloat();

		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = packedColor;
		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = packedColor;
		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = packedColor;
		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.COLOR_INDEX] = packedColor;

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(final Rectangle pRectangle) {
		final float[] bufferData = this.mBufferData;

		final float x = 0;
		final float y = 0;
		final float x2 = pRectangle.getWidth(); // TODO Optimize with field access?
		final float y2 = pRectangle.getHeight(); // TODO Optimize with field access?

		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x;
		bufferData[0 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y;

		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x;
		bufferData[1 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y2;

		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x2;
		bufferData[2 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y;

		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_X] = x2;
		bufferData[3 * Rectangle.VERTEX_SIZE + Rectangle.VERTEX_INDEX_Y] = y2;

		this.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}