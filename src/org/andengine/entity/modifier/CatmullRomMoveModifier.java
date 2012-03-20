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
 */
public class CatmullRomMoveModifier extends DurationEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final CatmullRomMoveModifierConfig mCatmullRomMoveModifierConfig;

	private final IEaseFunction mEaseFunction;

	private final int mControlSegmentCount;
	private final float mControlSegmentCountInverse;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CatmullRomMoveModifier(final float pDuration, final CatmullRomMoveModifierConfig pCatmullRomMoveModifierConfig) {
		this(pDuration, pCatmullRomMoveModifierConfig, null, EaseLinear.getInstance());
	}

	public CatmullRomMoveModifier(final float pDuration, final CatmullRomMoveModifierConfig pCatmullRomMoveModifierConfig, final IEaseFunction pEaseFunction) {
		this(pDuration, pCatmullRomMoveModifierConfig, null, pEaseFunction);
	}

	public CatmullRomMoveModifier(final float pDuration, final CatmullRomMoveModifierConfig pCatmullRomMoveModifierConfig, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pCatmullRomMoveModifierConfig, pEntityModifierListener, EaseLinear.getInstance());
	}

	public CatmullRomMoveModifier(final float pDuration, final CatmullRomMoveModifierConfig pCatmullRomMoveModifierConfig, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pEntityModifierListener);

		this.mCatmullRomMoveModifierConfig = pCatmullRomMoveModifierConfig;
		this.mEaseFunction = pEaseFunction;

		this.mControlSegmentCount = pCatmullRomMoveModifierConfig.getControlPointCount() - 1;
		this.mControlSegmentCountInverse = 1.0f / this.mControlSegmentCount;
	}

	@Override
	public CatmullRomMoveModifier deepCopy() {
		return new CatmullRomMoveModifier(this.mDuration, this.mCatmullRomMoveModifierConfig.deepCopy(), this.mEaseFunction);
	}

	public CatmullRomMoveModifier reverse() {
		return new CatmullRomMoveModifier(this.mDuration, this.mCatmullRomMoveModifierConfig.deepCopyReverse(), this.mEaseFunction);
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
		final float pX0 = this.mCatmullRomMoveModifierConfig.mControlPointXs[p0];
		final float pY0 = this.mCatmullRomMoveModifierConfig.mControlPointYs[p0];

		final int p1 = MathUtils.bringToBounds(0, this.mControlSegmentCount, p);
		final float pX1 = this.mCatmullRomMoveModifierConfig.mControlPointXs[p1];
		final float pY1 = this.mCatmullRomMoveModifierConfig.mControlPointYs[p1];

		final int p2 = MathUtils.bringToBounds(0, this.mControlSegmentCount, p + 1);
		final float pX2 = this.mCatmullRomMoveModifierConfig.mControlPointXs[p2];
		final float pY2 = this.mCatmullRomMoveModifierConfig.mControlPointYs[p2];

		final int p3 = MathUtils.bringToBounds(0, this.mControlSegmentCount, p + 2);
		final float pX3 = this.mCatmullRomMoveModifierConfig.mControlPointXs[p3];
		final float pY3 = this.mCatmullRomMoveModifierConfig.mControlPointYs[p3];

		/* Calculate: */
		final float t = (percentageDone - (p * this.mControlSegmentCountInverse)) / this.mControlSegmentCountInverse;
		final float tt = t * t;
		final float ttt = tt * t;

		final float b1 = 0.5f * ((-ttt + (2 * tt)) - t);
		final float b2 = 0.5f * (((3 * ttt) - (5 * tt)) + 2);
		final float b3 = 0.5f * ((-3 * ttt) + (4 * tt) + t);
		final float b4 = 0.5f * (ttt - tt);

		final float x = ((pX0 * b1) + (pX1 * b2) + (pX2 * b3) + (pX3 * b4));
		final float y = ((pY0 * b1) + (pY1 * b2) + (pY2 * b3) + (pY3 * b4));

		pEntity.setPosition(x, y);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static final float catmullRomX(final float pX0, final float pX1, final float pX2, final float pX3, final float pT) {
		final float t = pT;
		final float tt = t * t;
		final float ttt = tt * t;

		final float b1 = 0.5f * ((-ttt + (2 * tt)) - t);
		final float b2 = 0.5f * (((3 * ttt) - (5 * tt)) + 2);
		final float b3 = 0.5f * ((-3 * ttt) + (4 * tt) + t);
		final float b4 = 0.5f * (ttt - tt);

		return ((pX0 * b1) + (pX1 * b2) + (pX2 * b3) + (pX3 * b4));
	}

	public static final float catmullRomY(final float pY0, final float pY1, final float pY2, final float pY3, final float pT) {
		final float t = pT;
		final float tt = t * t;
		final float ttt = tt * t;

		final float b1 = 0.5f * ((-ttt + (2 * tt)) - t);
		final float b2 = 0.5f * (((3 * ttt) - (5 * tt)) + 2);
		final float b3 = 0.5f * ((-3 * ttt) + (4 * tt) + t);
		final float b4 = 0.5f * (ttt - tt);

		return ((pY0 * b1) + (pY1 * b2) + (pY2 * b3) + (pY3 * b4));
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class CatmullRomMoveModifierConfig {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final int CONTROLPOINT_COUNT_MINIMUM = 0;

		// ===========================================================
		// Fields
		// ===========================================================

		private final float[] mControlPointXs;
		private final float[] mControlPointYs;

		// ===========================================================
		// Constructors
		// ===========================================================

		public CatmullRomMoveModifierConfig(final int pControlPointCount) {
			if(pControlPointCount < CatmullRomMoveModifierConfig.CONTROLPOINT_COUNT_MINIMUM) {
				throw new IllegalArgumentException("A " + CatmullRomMoveModifierConfig.class.getSimpleName() + " needs at least " + CatmullRomMoveModifierConfig.CONTROLPOINT_COUNT_MINIMUM + " control points.");
			}

			this.mControlPointXs = new float[pControlPointCount];
			this.mControlPointYs = new float[pControlPointCount];
		}

		public CatmullRomMoveModifierConfig deepCopy() {
			final int controlPointCount = this.getControlPointCount();

			final CatmullRomMoveModifierConfig copy = new CatmullRomMoveModifierConfig(controlPointCount);

			System.arraycopy(this.mControlPointXs, 0, copy.mControlPointXs, 0, controlPointCount);
			System.arraycopy(this.mControlPointYs, 0, copy.mControlPointYs, 0, controlPointCount);

			return copy;
		}

		public CatmullRomMoveModifierConfig deepCopyReverse() {
			final CatmullRomMoveModifierConfig copy = this.deepCopy();

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
