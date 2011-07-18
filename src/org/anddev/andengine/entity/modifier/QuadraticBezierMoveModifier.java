package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 23:24:26 - 16.07.2011
 */
public class QuadraticBezierMoveModifier extends DurationEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mX1;
	private final float mY1;
	private final float mX2;
	private final float mY2;
	private final float mX3;
	private final float mY3;

	private final IEaseFunction mEaseFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	public QuadraticBezierMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final IEaseFunction pEaseFunction) {
		super(pDuration);

		this.mX1 = pX1;
		this.mY1 = pY1;
		this.mX2 = pX2;
		this.mY2 = pY2;
		this.mX3 = pX3;
		this.mY3 = pY3;

		this.mEaseFunction = pEaseFunction;
	}

	@Override
	public QuadraticBezierMoveModifier clone() {
		return new QuadraticBezierMoveModifier(this.mDuration, this.mX1, this.mY1, this.mX2, this.mY2, this.mX3, this.mY3, this.mEaseFunction);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed, final IEntity pEntity) {
		final float percentageDone = this.mEaseFunction.getPercentage(this.getSecondsElapsed(), this.mDuration);

		final float u = 1 - percentageDone;
		final float tt = percentageDone*percentageDone;
		final float uu = u*u;
		
		final float ut2 = 2 * u * percentageDone;
		
		/* Formula:
		 * ((1-t)^2 * p1) + (2*(t)*(1-t) * p2) + ((t^2) * p3) */
		final float x = (uu * this.mX1) + (ut2 * this.mX2) + (tt * this.mX3);
		final float y = (uu * this.mY1) + (ut2 * this.mY2) + (tt * this.mY3);
		
		pEntity.setPosition(x, y);
	}

	@Override
	protected void onManagedInitialize(final IEntity pEntity) {

	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public void updatePosition(final float pPercentageDone, final IEntity pEntity) {
		final float u = 1 - pPercentageDone;
		final float tt = pPercentageDone*pPercentageDone;
		final float uu = u*u;

		final float ut2 = 2 * u * pPercentageDone;

		/* Formula:
		 * ((1-t)^2 * p1) + (2*(t)*(1-t) * p2) + ((t^2) * p3) */
		final float x = (uu * this.mX1) + (ut2 * this.mX2) + (tt * this.mX3);
		final float y = (uu * this.mY1) + (ut2 * this.mY2) + (tt * this.mY3);

		pEntity.setPosition(x, y);
	}
}