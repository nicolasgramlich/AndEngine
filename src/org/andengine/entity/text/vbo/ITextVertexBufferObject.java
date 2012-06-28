package org.andengine.entity.text.vbo;

import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * 
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:38:05 - 29.03.2012
 */
public interface ITextVertexBufferObject extends IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateColor(final Text pText);
	public void onUpdateVertices(final Text pText);
}

