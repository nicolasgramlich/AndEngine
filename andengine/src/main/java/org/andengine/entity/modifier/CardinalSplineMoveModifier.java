package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:47:24 - 20.03.2012
 * @see {@link http://en.wikipedia.org/wiki/Cubic_Hermite_spline#Cardinal_spline}
 * @see {@link http://algorithmist.wordpress.com/2009/10/06/cardinal-splines-part-4/}
 */
public class CardinalSplineMoveModifier extends DurationEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final CardinalSplineMoveModifierConfig mCardinalSplineMoveModifierConfig;

	private final IEaseFunction mEaseFunction;

	private final int mControlSegmentCount;
	private final float mControlSegmentCountInverse;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CardinalSplineMoveModifier(final float pDuration, final CardinalSplineMoveModifierConfig pCardinalSplineMoveModifierConfig) {
		this(pDuration, pCardinalSplineMoveModifierConfig, null, EaseLinear.getInstance());
	}

	public CardinalSplineMoveModifier(final float pDuration, final CardinalSplineMoveModifierConfig pCardinalSplineMoveModifierConfig, final IEaseFunction pEaseFunction) {
		this(pDuration, pCardinalSplineMoveModifierConfig, null, pEaseFunction);
	}

	public CardinalSplineMoveModifier(final float pDuration, final CardinalSplineMoveModifierConfig pCardinalSplineMoveModifierConfig, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pCardinalSplineMoveModifierConfig, pEntityModifierListener, EaseLinear.getInstance());
	}

	public CardinalSplineMoveModifier(final float pDuration, final CardinalSplineMoveModifierConfig pCardinalSplineMoveModifierConfig, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pEntityModifierListener);

		this.mCardinalSplineMoveModifierConfig = pCardinalSplineMoveModifierConfig;
		this.mEaseFunction = pEaseFunction;

		this.mControlSegmentCount = pCardinalSplineMoveModifierConfig.getControlPointCount() - 1;
		this.mControlSegmentCountInverse = 1.0f / this.mControlSegmentCount;
	}

	@Override
	public CardinalSplineMoveModifier deepCopy() {
		return new CardinalSplineMoveModifier(this.mDuration, this.mCardinalSplineMoveModifierConfig.deepCopy(), this.mEaseFunction);
	}

	public CardinalSplineMoveModifier reverse() {
		return new CardinalSplineMoveModifier(this.mDuration, this.mCardinalSplineMoveModifierConfig.deepCopyReverse(), this.mEaseFunction);
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

		/* Calculate active control point. */
		final int p;
		if(percentageDone == 1) {
			p = this.mControlSegmentCount;
		} else {
			p = (int) (percentageDone / this.mControlSegmentCountInverse);
		}

		/* Retrieve control points. */
		final int p0 = MathUtils.bringToBounds(0, this.mControlSegmentCount, p - 1);
		final float pX0 = this.mCardinalSplineMoveModifierConfig.mControlPointXs[p0];
		final float pY0 = this.mCardinalSplineMoveModifierConfig.mControlPointYs[p0];

		final int p1 = MathUtils.bringToBounds(0, this.mControlSegmentCount, p);
		final float pX1 = this.mCardinalSplineMoveModifierConfig.mControlPointXs[p1];
		final float pY1 = this.mCardinalSplineMoveModifierConfig.mControlPointYs[p1];

		final int p2 = MathUtils.bringToBounds(0, this.mControlSegmentCount, p + 1);
		final float pX2 = this.mCardinalSplineMoveModifierConfig.mControlPointXs[p2];
		final float pY2 = this.mCardinalSplineMoveModifierConfig.mControlPointYs[p2];

		final int p3 = MathUtils.bringToBounds(0, this.mControlSegmentCount, p + 2);
		final float pX3 = this.mCardinalSplineMoveModifierConfig.mControlPointXs[p3];
		final float pY3 = this.mCardinalSplineMoveModifierConfig.mControlPointYs[p3];

		/* Calculate new position. */
		final float t = (percentageDone - (p * this.mControlSegmentCountInverse)) / this.mControlSegmentCountInverse;
		final float tt = t * t;
		final float ttt = tt * t;


		/*
		 * Formula: s * (-ttt + 2tt – t) * P1 + s * (-ttt + tt) * P2 + (2ttt – 3tt + 1) * P2 + s(ttt – 2tt + t) * P3 + (-2ttt + 3tt) * P3 + s * (ttt – tt) * P4
		 */
		final float s = (1 - this.mCardinalSplineMoveModifierConfig.mTension) / 2;

		final float b1 = s * ((-ttt + (2 * tt)) - t); // (s * (-ttt + 2tt – t)) * P1
		final float b2 = (s * (-ttt + tt)) + (((2 * ttt) - (3 * tt)) + 1); // (s * (-ttt + tt) + (2ttt – 3tt + 1)) * P2
		final float b3 = (s * ((ttt - (2 * tt)) + t)) + ((-2 * ttt) + (3 * tt)); // (s * (ttt – 2tt + t) + (-2ttt + 3tt)) * P3
		final float b4 = s * (ttt - tt); // (s * (ttt – tt)) * P4

		final float x = ((pX0 * b1) + (pX1 * b2) + (pX2 * b3) + (pX3 * b4));
		final float y = ((pY0 * b1) + (pY1 * b2) + (pY2 * b3) + (pY3 * b4));

		pEntity.setPosition(x, y);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static final float cardinalSplineX(final float pX0, final float pX1, final float pX2, final float pX3, final float pT, final float pTension) {
		final float t = pT;
		final float tt = t * t;
		final float ttt = tt * t;

		final float s = (1 - pTension) / 2;

		final float b1 = s * ((-ttt + (2 * tt)) - t); // (s * (-ttt + 2tt – t)) * P1
		final float b2 = (s * (-ttt + tt)) + (((2 * ttt) - (3 * tt)) + 1); // (s * (-ttt + tt) + (2ttt – 3tt + 1)) * P2
		final float b3 = (s * ((ttt - (2 * tt)) + t)) + ((-2 * ttt) + (3 * tt)); // (s * (ttt – 2tt + t) + (-2ttt + 3tt)) * P3
		final float b4 = s * (ttt - tt); // (s * (ttt – tt)) * P4

		return ((pX0 * b1) + (pX1 * b2) + (pX2 * b3) + (pX3 * b4));
	}

	public static final float cardinalSplineY(final float pY0, final float pY1, final float pY2, final float pY3, final float pT, final float pTension) {
		final float t = pT;
		final float tt = t * t;
		final float ttt = tt * t;

		final float s = (1 - pTension) / 2;

		final float b1 = s * ((-ttt + (2 * tt)) - t); // (s * (-ttt + 2tt – t)) * P1
		final float b2 = (s * (-ttt + tt)) + (((2 * ttt) - (3 * tt)) + 1); // (s * (-ttt + tt) + (2ttt – 3tt + 1)) * P2
		final float b3 = (s * ((ttt - (2 * tt)) + t)) + ((-2 * ttt) + (3 * tt)); // (s * (ttt – 2tt + t) + (-2ttt + 3tt)) * P3
		final float b4 = s * (ttt - tt); // (s * (ttt – tt)) * P4

		return ((pY0 * b1) + (pY1 * b2) + (pY2 * b3) + (pY3 * b4));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class CardinalSplineMoveModifierConfig {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final int CONTROLPOINT_COUNT_MINIMUM = 4;

		// ===========================================================
		// Fields
		// ===========================================================

		/* package */ private final float[] mControlPointXs;
		/* package */ private final float[] mControlPointYs;

		/* package */ final float mTension;

		// ===========================================================
		// Constructors
		// ===========================================================

		/**
		 * @param pControlPointCount
		 * @param pTension [-1, 1]
		 */
		public CardinalSplineMoveModifierConfig(final int pControlPointCount, final float pTension) {
			if(pControlPointCount < CardinalSplineMoveModifierConfig.CONTROLPOINT_COUNT_MINIMUM) {
				throw new IllegalArgumentException("A " + CardinalSplineMoveModifierConfig.class.getSimpleName() + " needs at least " + CardinalSplineMoveModifierConfig.CONTROLPOINT_COUNT_MINIMUM + " control points.");
			}

			this.mTension = pTension;

			this.mControlPointXs = new float[pControlPointCount];
			this.mControlPointYs = new float[pControlPointCount];
		}

		public CardinalSplineMoveModifierConfig deepCopy() {
			final int controlPointCount = this.getControlPointCount();

			final CardinalSplineMoveModifierConfig copy = new CardinalSplineMoveModifierConfig(controlPointCount, this.mTension);

			System.arraycopy(this.mControlPointXs, 0, copy.mControlPointXs, 0, controlPointCount);
			System.arraycopy(this.mControlPointYs, 0, copy.mControlPointYs, 0, controlPointCount);

			return copy;
		}

		public CardinalSplineMoveModifierConfig deepCopyReverse() {
			final CardinalSplineMoveModifierConfig copy = this.deepCopy();

			ArrayUtils.reverse(copy.mControlPointXs);
			ArrayUtils.reverse(copy.mControlPointYs);

			return copy;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getControlPointCount() {
			return this.mControlPointXs.length;
		}

		public void setControlPoint(final int pIndex, final float pX, final float pY) {
			this.mControlPointXs[pIndex] = pX;
			this.mControlPointYs[pIndex] = pY;
		}

		public float getControlPointX(final int pIndex) {
			return this.mControlPointXs[pIndex];
		}

		public float getControlPointY(final int pIndex) {
			return this.mControlPointYs[pIndex];
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
