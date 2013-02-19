package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotationModifier extends SingleValueSpanEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotationModifier(final float pDuration, final float pFromRotation, final float pToRotation) {
		this(pDuration, pFromRotation, pToRotation, null, EaseLinear.getInstance());
	}

	public RotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromRotation, pToRotation, null, pEaseFunction);
	}

	public RotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pFromRotation, pToRotation, pEntityModifierListener, EaseLinear.getInstance());
	}

	public RotationModifier(final float pDuration, final float pFromRotation, final float pToRotation, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromRotation, pToRotation, pEntityModifierListener, pEaseFunction);
	}

	protected RotationModifier(final RotationModifier pRotationModifier) {
		super(pRotationModifier);
	}

	@Override
	public RotationModifier deepCopy() {
		return new RotationModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final IEntity pEntity, final float pRotation) {
		pEntity.setRotation(pRotation);
	}

	@Override
	protected void onSetValue(final IEntity pEntity, final float pPercentageDone, final float pRotation) {
		pEntity.setRotation(pRotation);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
