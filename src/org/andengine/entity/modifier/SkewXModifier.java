package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:46:10 - 05.10.2011
 */
public class SkewXModifier extends SingleValueSpanEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SkewXModifier(final float pDuration, final float pFromSkewX, final float pToSkewX) {
		this(pDuration, pFromSkewX, pToSkewX, null, EaseLinear.getInstance());
	}

	public SkewXModifier(final float pDuration, final float pFromSkewX, final float pToSkewX, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromSkewX, pToSkewX, null, pEaseFunction);
	}

	public SkewXModifier(final float pDuration, final float pFromSkewX, final float pToSkewX, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pFromSkewX, pToSkewX, pEntityModifierListener, EaseLinear.getInstance());
	}

	public SkewXModifier(final float pDuration, final float pFromSkewX, final float pToSkewX, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromSkewX, pToSkewX, pEntityModifierListener, pEaseFunction);
	}

	protected SkewXModifier(final SkewXModifier pSkewXModifier) {
		super(pSkewXModifier);
	}

	@Override
	public SkewXModifier deepCopy() {
		return new SkewXModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValue(final IEntity pEntity, final float pSkewX) {
		pEntity.setSkewX(pSkewX);
	}

	@Override
	protected void onSetValue(final IEntity pEntity, final float pPercentageDone, final float pSkewX) {
		pEntity.setSkewX(pSkewX);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
