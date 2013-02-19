package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Pawel Plewa
 * @author Nicolas Gramlich
 * @since 23:24:26 - 16.07.2011
 */
public class CubicBezierCurveMoveModifier extends DurationEntityModifier {
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
	private final float mX4;
	private final float mY4;

	private final IEaseFunction mEaseFunction;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pDuration
	 * @param pX1 x coordinate of the start point.
	 * @param pY1 y coordinate of the start point.
	 * @param pX2 x coordinate of the first control point.
	 * @param pY2 y coordinate of the first control point.
	 * @param pX3 x coordinate of the second control point.
	 * @param pY3 y coordinate of the second control point.
	 * @param pX4 x coordinate of the end point.
	 * @param pY4 y coordinate of the end point.
	 */
	public CubicBezierCurveMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4) {
		this(pDuration, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, null, EaseLinear.getInstance());
	}

	/**
	 * @param pDuration
	 * @param pX1 x coordinate of the start point.
	 * @param pY1 y coordinate of the start point.
	 * @param pX2 x coordinate of the first control point.
	 * @param pY2 y coordinate of the first control point.
	 * @param pX3 x coordinate of the second control point.
	 * @param pY3 y coordinate of the second control point.
	 * @param pX4 x coordinate of the end point.
	 * @param pY4 y coordinate of the end point.
	 * @param pEaseFunction
	 */
	public CubicBezierCurveMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final IEaseFunction pEaseFunction) {
		this(pDuration, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, null, pEaseFunction);
	}

	/**
	 * @param pDuration
	 * @param pX1 x coordinate of the start point.
	 * @param pY1 y coordinate of the start point.
	 * @param pX2 x coordinate of the first control point.
	 * @param pY2 y coordinate of the first control point.
	 * @param pX3 x coordinate of the second control point.
	 * @param pY3 y coordinate of the second control point.
	 * @param pX4 x coordinate of the end point.
	 * @param pY4 y coordinate of the end point.
	 * @param pEntityModifierListener
	 */
	public CubicBezierCurveMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pX1, pY1, pX2, pY2, pX3, pY3, pX4, pY4, pEntityModifierListener, EaseLinear.getInstance());
	}

	/**
	 * @param pDuration
	 * @param pX1 x coordinate of the start point.
	 * @param pY1 y coordinate of the start point.
	 * @param pX2 x coordinate of the first control point.
	 * @param pY2 y coordinate of the first control point.
	 * @param pX3 x coordinate of the second control point.
	 * @param pY3 y coordinate of the second control point.
	 * @param pX4 x coordinate of the end point.
	 * @param pY4 y coordinate of the end point.
	 * @param pEntityModifierListener
	 * @param pEaseFunction
	 */
	public CubicBezierCurveMoveModifier(final float pDuration, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3, final float pX4, final float pY4, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pEntityModifierListener);

		this.mX1 = pX1;
		this.mY1 = pY1;
		this.mX2 = pX2;
		this.mY2 = pY2;
		this.mX3 = pX3;
		this.mY3 = pY3;
		this.mX4 = pX4;
		this.mY4 = pY4;

		this.mEaseFunction = pEaseFunction;
	}

	@Override
	public CubicBezierCurveMoveModifier deepCopy() {
		return new CubicBezierCurveMoveModifier(this.mDuration, this.mX1, this.mY1, this.mX2, this.mY2, this.mX3, this.mY3, this.mX4, this.mY4, this.mEaseFunction);
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
		final float uuu = uu * u;
		final float ttt = tt * percentageDone;

		final float ut3 = 3 * uu * percentageDone;
		final float utt3 = 3 * u * tt;

		/*
		 * Formula: ((1-t)^3 * P1) + (3*(t)*(1-t)^2 * P2) + (3*(tt)*(1-t) * P3) + (ttt * P4)
		 */
		final float x = (uuu * this.mX1) + (ut3 * this.mX2) + (utt3 * this.mX3) + (ttt * this.mX4);
		final float y = (uuu * this.mY1) + (ut3 * this.mY2) + (utt3 * this.mY3) + (ttt * this.mY4);

		pEntity.setPosition(x, y);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}