package org.anddev.andengine.entity.scene.menu.item;

import org.anddev.andengine.entity.shape.IAreaShape;
import org.anddev.andengine.opengl.mesh.Mesh;
import org.anddev.andengine.opengl.vbo.VertexBufferObject;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:27:16 - 07.07.2010
 */
public interface IMenuItem<V extends VertexBufferObject, M extends Mesh<V>> extends IAreaShape<V, M> {
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
