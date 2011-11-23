package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:37:53 - 19.03.2010
 */
public class ScaleModifier extends DoubleValueSpanEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale) {
		this(pDuration, pFromScale, pToScale, null, EaseLinear.getInstance());
	}

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScale, pToScale, null, pEaseFunction);
	}

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pFromScale, pToScale, pFromScale, pToScale, pEntityModifierListener, EaseLinear.getInstance());
	}

	public ScaleModifier(final float pDuration, final float pFromScale, final float pToScale, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScale, pToScale, pFromScale, pToScale, pEntityModifierListener, pEaseFunction);
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, null, EaseLinear.getInstance());
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, null, pEaseFunction);
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pEntityModifierListener, EaseLinear.getInstance());
	}

	public ScaleModifier(final float pDuration, final float pFromScaleX, final float pToScaleX, final float pFromScaleY, final float pToScaleY, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromScaleX, pToScaleX, pFromScaleY, pToScaleY, pEntityModifierListener, pEaseFunction);
	}

	protected ScaleModifier(final ScaleModifier pScaleModifier) {
		super(pScaleModifier);
	}

	@Override
	public ScaleModifier deepCopy(){
		return new ScaleModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final IEntity pEntity, final float pScaleA, final float pScaleB) {
		pEntity.setScale(pScaleA, pScaleB);
	}

	@Override
	protected void onSetValues(final IEntity pEntity, final float pPercentageDone, final float pScaleA, final float pScaleB) {
		pEntity.setScale(pScaleA, pScaleB);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
