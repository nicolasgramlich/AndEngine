package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Mesh;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:46:51 - 28.03.2012
 */
public interface IMeshVertexBufferObject extends IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float[] getBufferData();
	public void onUpdateColor(final Mesh pMesh);
	public void onUpdateVertices(final Mesh pMesh);
}