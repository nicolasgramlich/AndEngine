package org.anddev.andengine.entity;

import org.anddev.andengine.entity.scene.Scene.ITouchArea;

/**
 * @author Nicolas Gramlich
 * @since 13:42:48 - 07.07.2010
 */
public interface IStaticEntity extends IEntity, ITouchArea {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public abstract float getX();
	public abstract float getY();

	public abstract float getBaseX();
	public abstract float getBaseY();

	public abstract float getCenterX();
	public abstract float getCenterY();

	public abstract float getOffsetX();
	public abstract float getOffsetY();

	public abstract void setOffsetX(final float pOffsetX);
	public abstract void setOffsetY(final float pOffsetY);
	public abstract void setOffset(final float pOffsetX, final float pOffsetY);
}