package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.LineChain;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:49:01 - 30.05.2013
 */
public interface ILineChainVertexBufferObject extends IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateColor(final LineChain pLineChain);
	public void onUpdateVertices(final LineChain pLineChain);
}