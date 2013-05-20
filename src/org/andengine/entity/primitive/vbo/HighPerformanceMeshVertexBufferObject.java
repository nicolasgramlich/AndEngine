package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Mesh;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:46:51 - 28.03.2012
 */
public class HighPerformanceMeshVertexBufferObject extends HighPerformanceVertexBufferObject implements IMeshVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mVertexCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public HighPerformanceMeshVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager, final float[] pBufferData, final int pVertexCount, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
		super(pVertexBufferObjectManager, pBufferData, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);

		this.mVertexCount = pVertexCount;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdateColor(final Mesh pMesh) {
		final float[] bufferData = this.mBufferData;

		final float packedColor = pMesh.getColor().getABGRPackedFloat();

		for (int i = 0; i < this.mVertexCount; i++) {
			bufferData[(i * Mesh.VERTEX_SIZE) + Mesh.COLOR_INDEX] = packedColor;
		}

		this.setDirtyOnHardware();
	}

	@Override
	public void onUpdateVertices(final Mesh pMesh) {
		/* Since the buffer data is managed from the caller, we just mark the buffer data as dirty. */

		this.setDirtyOnHardware();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}