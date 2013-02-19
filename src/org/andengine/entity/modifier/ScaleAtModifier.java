package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 21:53:30 - 06.07.2010
 */
public class ScaleAtModifier extends ScaleModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mScaleCenterX;
	private final float mScaleCenterY;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, final float pScaleCenterX, final float pScaleCenterY) {
		this(pDuration, pFromScale, pToScale, pScaleCenterX, pScaleCenterY, EaseLinear.getInstance());
	}

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, final float pScaleCenterX, final float pScaleCenterY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScale, pToScale, pScaleCenterX, pScaleCenterY, null, pEaseFunction);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, final float pScaleCenterX, final float pScaleCenterY, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pFromScale, pToScale, pScaleCenterX, pScaleCenterY, pEntityModifierListener, EaseLinear.getInstance());
	}

	public ScaleAtModifier(final float pDuration, final float pFromScale, final float pToScale, final float pScaleCenterX, final float pScaleCenterY, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScale, pToScale, pFromScale, pToScale, pScaleCenterX, pScaleCenterY, pEntityModifierListener, pEaseFunction);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pScaleCenterX, final float pScaleCenterY) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pScaleCenterX, pScaleCenterY, EaseLinear.getInstance());
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pScaleCenterX, final float pScaleCenterY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pScaleCenterX, pScaleCenterY, null, pEaseFunction);
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pScaleCenterX, final float pScaleCenterY, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pScaleCenterX, pScaleCenterY, pEntityModifierListener, EaseLinear.getInstance());
	}

	public ScaleAtModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final float pScaleCenterX, final float pScaleCenterY, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pEntityModifierListener, pEaseFunction);

		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;
	}

	protected ScaleAtModifier(final ScaleAtModifier pScaleAtModifier) {
		super(pScaleAtModifier);

		this.mScaleCenterX = pScaleAtModifier.mScaleCenterX;
		this.mScaleCenterY = pScaleAtModifier.mScaleCenterY;
	}

	@Override
	public ScaleAtModifier deepCopy() {
		return new ScaleAtModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedInitialize(final IEntity pEntity) {
		super.onManagedInitialize(pEntity);
		pEntity.setScaleCenter(this.mScaleCenterX, this.mScaleCenterY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
