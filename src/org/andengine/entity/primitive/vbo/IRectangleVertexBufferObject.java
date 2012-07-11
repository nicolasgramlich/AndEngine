package org.andengine.entity.primitive.vbo;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.IVertexBufferObject;

/**
 * Specifies the quality of the drawn rectangle<br>
 * Known implementations:<br>
 * <ul>
 * <li> {@link HighPerformanceRectangleVertexBufferObject}
 * <li> {@link LowMemoryRectangleVertexBufferObject}
 * </ul> 
 * (c) Zynga 2012<br>
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 18:48:14 - 28.03.2012
 */
public interface IRectangleVertexBufferObject extends IVertexBufferObject {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onUpdateColor(final Rectangle pRectangle);
	public void onUpdateVertices(final Rectangle pRectangle);
}