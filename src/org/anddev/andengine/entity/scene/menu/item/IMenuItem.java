package org.anddev.andengine.entity.scene.menu.item;

import org.anddev.andengine.entity.shape.IAreaShape;
import org.anddev.andengine.opengl.vbo.IVertexBufferObject;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:27:16 - 07.07.2010
 */
public interface IMenuItem<V extends IVertexBufferObject> extends IAreaShape<V> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public int getID();
	public abstract void onSelected();
	public abstract void onUnselected();
}
