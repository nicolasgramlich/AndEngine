package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 23:24:26 - 16.07.2011
 */
public class QuadraticBezierCurveMoveModifier extends DurationEntityModifier {
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

	/**
	 * @param pDuration
	 * @param pX1 x coordinate of the start point.
	 * @param pY1 y coordinate of the start point.
	 * @param pX2 x coordinate of the control point.
	 * @param pY2 y coordinate of the ontrol point.
	 * @param pX3 x coordinate of the end point.
	 * @param pY3 y coordinate of the end point.
	 */
	public QuadraticBezierCurveMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
		this(pDuration, pX1, pY1, pX2, pY2, pX3, pY3, EaseLinear.getInstance(), null);
	}

	/**
	 * @param pDuration
	 * @param pX1 x coordinate of the start point.
	 * @param pY1 y coordinate of the start point.
	 * @param pX2 x coordinate of the control point.
	 * @param pY2 y coordinate of the ontrol point.
	 * @param pX3 x coordinate of the end point.
	 * @param pY3 y coordinate of the end point.
	 * @param pEaseFunction
	 */
	public QuadraticBezierCurveMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final IEaseFunction pEaseFunction) {
		this(pDuration, pX1, pY1, pX2, pY2, pX3, pY3, pEaseFunction, null);
	}

	/**
	 * @param pDuration
	 * @param pX1 x coordinate of the start point.
	 * @param pY1 y coordinate of the start point.
	 * @param pX2 x coordinate of the control point.
	 * @param pY2 y coordinate of the ontrol point.
	 * @param pX3 x coordinate of the end point.
	 * @param pY3 y coordinate of the end point.
	 * @param pEntityModifierListener
	 */
	public QuadraticBezierCurveMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pX1, pY1, pX2, pY2, pX3, pY3, EaseLinear.getInstance(), pEntityModifierListener);
	}

	/**
	 * @param pDuration
	 * @param pX1 x coordinate of the start point.
	 * @param pY1 y coordinate of the start point.
	 * @param pX2 x coordinate of the control point.
	 * @param pY2 y coordinate of the ontrol point.
	 * @param pX3 x coordinate of the end point.
	 * @param pY3 y coordinate of the end point.
	 * @param pEaseFunction
	 * @param pEntityModifierListener
	 */
	public QuadraticBezierCurveMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final IEaseFunction pEaseFunction, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pEntityModifierListener);

		this.mX1 = pX1;
		this.mY1 = pY1;
		this.mX2 = pX2;
		this.mY2 = pY2;
		this.mX3 = pX3;
		this.mY3 = pY3;

		this.mEaseFunction = pEaseFunction;
	}

	@Override
	public QuadraticBezierCurveMoveModifier deepCopy() {
		return new QuadraticBezierCurveMoveModifier(this.mDuration, this.mX1, this.mY1, this.mX2, this.mY2, this.mX3, this.mY3, this.mEaseFunction);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedInitialize(final IEntity pEntity) {

	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed, final IEntity pEntity) {
		final float percentageDone = this.mEaseFunction.getPercentage(this.getSecondsElapsed(), this.mDuration);

		final float u = 1 - percentageDone;
		final float tt = percentageDone * percentageDone;
		final float uu = u * u;

		final float ut2 = 2 * u * percentageDone;

		/* Formula:
		 * ((1-t)^2 * P1) + (2*(t)*(1-t) * P2) + ((tt) * P3) */
		final float x = (uu * this.mX1) + (ut2 * this.mX2) + (tt * this.mX3);
		final float y = (uu * this.mY1) + (ut2 * this.mY2) + (tt * this.mY3);

		pEntity.setPosition(x, y);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}