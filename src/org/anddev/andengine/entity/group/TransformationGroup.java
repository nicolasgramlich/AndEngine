package org.anddev.andengine.entity.group;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.layer.DynamicCapacityLayer;
import org.anddev.andengine.entity.layer.ILayer;

/**
 * @author Nicolas Gramlich
 * @since 23:04:49 - 10.07.2010
 */
public class TransformationGroup extends DynamicCapacityLayer implements ILayer {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	protected float mTranslationX;
	protected float mTranslationY;

	protected float mRotation = 0;

	protected float mRotationCenterX = 0;
	protected float mRotationCenterY = 0;

	protected float mScaleX = 1;
	protected float mScaleY = 1;

	protected float mScaleCenterX = 0;
	protected float mScaleCenterY = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public float getRotation() {
		return this.mRotation;
	}

	public void setRotation(final float pRotation) {
		this.mRotation = pRotation;
	}

	public float getRotationCenterX() {
		return this.mRotationCenterX;
	}

	public float getRotationCenterY() {
		return this.mRotationCenterY;
	}

	public void setRotationCenterX(final float pRotationCenterX) {
		this.mRotationCenterX = pRotationCenterX;
	}

	public void setRotationCenterY(final float pRotationCenterY) {
		this.mRotationCenterY = pRotationCenterY;
	}

	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY) {
		this.mRotationCenterX = pRotationCenterX;
		this.mRotationCenterY = pRotationCenterY;
	}

	public float getScaleX() {
		return this.mScaleX;
	}

	public float getScaleY() {
		return this.mScaleY;
	}

	public void setScaleX(final float pScaleX) {
		this.mScaleX = pScaleX;
	}

	public void setScaleY(final float pScaleY) {
		this.mScaleY = pScaleY;
	}

	public void setScale(final float pScale) {
		this.mScaleX = pScale;
		this.mScaleY = pScale;
	}

	public void setScale(final float pScaleX, final float pScaleY) {
		this.mScaleX = pScaleX;
		this.mScaleY = pScaleY;
	}

	public float getScaleCenterX() {
		return this.mScaleCenterX;
	}

	public float getScaleCenterY() {
		return this.mScaleCenterY;
	}

	public void setScaleCenterX(final float pScaleCenterX) {
		this.mScaleCenterX = pScaleCenterX;
	}

	
	public void setScaleCenterY(final float pScaleCenterY) {
		this.mScaleCenterY = pScaleCenterY;
	}

	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY) {
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	protected void onManagedDraw(GL10 pGL) {
		pGL.glPushMatrix();

		this.onApplyTransformations(pGL);
		
		super.onManagedDraw(pGL);
		
		pGL.glPushMatrix();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onApplyTransformations(final GL10 pGL) {
		this.applyTranslation(pGL);

		this.applyRotation(pGL);

		this.applyScale(pGL);
	}

	protected void applyTranslation(final GL10 pGL) {
		pGL.glTranslatef(this.mTranslationX, this.mTranslationY, 0);
	}

	protected void applyRotation(final GL10 pGL) {
		// TODO Offset needs to be taken into account.
		final float rotation = this.mRotation;
		
		if(rotation != 0) {
			final float rotationCenterX = this.mRotationCenterX;
			final float rotationCenterY = this.mRotationCenterY;

			pGL.glTranslatef(rotationCenterX, rotationCenterY, 0);
			pGL.glRotatef(rotation, 0, 0, 1);
			pGL.glTranslatef(-rotationCenterX, -rotationCenterY, 0);
		}
	}

	protected void applyScale(final GL10 pGL) {
		final float scaleX = this.mScaleX;
		final float scaleY = this.mScaleY;
		
		if(scaleX != 1 || scaleY != 1) {
			final float scaleCenterX = this.mScaleCenterX;
			final float scaleCenterY = this.mScaleCenterY;

			pGL.glTranslatef(scaleCenterX, scaleCenterY, 0);
			pGL.glScalef(scaleX, scaleY, 1);
			pGL.glTranslatef(-scaleCenterX, -scaleCenterY, 0);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
