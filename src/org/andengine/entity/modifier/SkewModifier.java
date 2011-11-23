package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 10:46:10 - 05.10.2011
 */
public class SkewModifier extends DoubleValueSpanEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SkewModifier(final float pDuration, final float pFromSkewX, final float pToSkewX, final float pFromSkewY, final float pToSkewY) {
		this(pDuration, pFromSkewX, pToSkewX, pFromSkewY, pToSkewY, null, EaseLinear.getInstance());
	}

	public SkewModifier(final float pDuration, final float pFromSkewX, final float pToSkewX, final float pFromSkewY, final float pToSkewY, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromSkewX, pToSkewX, pFromSkewY, pToSkewY, null, pEaseFunction);
	}

	public SkewModifier(final float pDuration, final float pFromSkewX, final float pToSkewX, final float pFromSkewY, final float pToSkewY, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pFromSkewX, pToSkewX, pFromSkewY, pToSkewY, pEntityModifierListener, EaseLinear.getInstance());
	}

	public SkewModifier(final float pDuration, final float pFromSkewX, final float pToSkewX, final float pFromSkewY, final float pToSkewY, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromSkewX, pToSkewX, pFromSkewY, pToSkewY, pEntityModifierListener, pEaseFunction);
	}

	protected SkewModifier(final SkewModifier pSkewXModifier) {
		super(pSkewXModifier);
	}

	@Override
	public SkewModifier deepCopy(){
		return new SkewModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetInitialValues(final IEntity pEntity, final float pSkewX, final float pSkewY) {
		pEntity.setSkew(pSkewX, pSkewY);
	}

	@Override
	protected void onSetValues(final IEntity pEntity, final float pPercentageDone, final float pSkewX, final float pSkewY) {
		pEntity.setSkew(pSkewX, pSkewY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
