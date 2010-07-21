package org.anddev.andengine.entity.shape;

import org.anddev.andengine.entity.IBaseEntity;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.shape.modifier.IShapeModifier;
import org.anddev.andengine.opengl.vertex.VertexBuffer;

/**
 * @author Nicolas Gramlich
 * @since 13:32:52 - 07.07.2010
 */
public interface IShape extends IBaseEntity, ITouchArea  {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getWidth();
	public float getHeight();

	public float getBaseWidth();
	public float getBaseHeight();

	public float getWidthScaled();
	public float getHeightScaled();

	public void addShapeModifier(final IShapeModifier pShapeModifier);
	public void removeShapeModifier(final IShapeModifier pShapeModifier);

	public boolean collidesWith(final IShape pOtherShape);

	public VertexBuffer getVertexBuffer();

	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction);
}