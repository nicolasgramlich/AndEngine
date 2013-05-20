package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Gradient;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:27:22 - 23.04.2012
 */
public interface IGradientVertexBufferObject extends IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateColor(final Gradient pGradient);
	public void onUpdateVertices(final Gradient pGradient);
}