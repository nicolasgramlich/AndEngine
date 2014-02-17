package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Line;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:45:00 - 28.03.2012
 */
public interface ILineVertexBufferObject extends IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateColor(final Line pLine);
	public void onUpdateVertices(final Line pLine);
}