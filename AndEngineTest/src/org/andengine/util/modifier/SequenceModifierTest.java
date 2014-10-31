package org.andengine.util.modifier;

import junit.framework.Assert;

import org.andengine.util.modifier.SequenceModifier.ISubSequenceModifierListener;

import android.test.AndroidTestCase;

/**
 * @author Nicolas Gramlich
 * @since 15:14:05 - 16.06.2011
 */
@SuppressWarnings("unchecked")
public class SequenceModifierTest extends AndroidTestCase implements ISubSequenceModifierListener<Object> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DELTA = 0.001f;

	private static final float DURATION = 10;
	private static final float DURATION_DELTA = 0.001f;

	// ===========================================================
	// Fields
	// ===========================================================

	private SequenceModifier<Object> mSequenceModifier;

	private int mSubSequenceStartedCount;
	private int mSubSequenceFinishedCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mSubSequenceStartedCount = 0;
		this.mSubSequenceFinishedCount = 0;
	}

	@Override
	public void tearDown() throws Exception {

	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onSubSequenceStarted(IModifier<Object> pModifier, Object pItem, int pIndex) {
		this.mSubSequenceStartedCount++;
	}

	@Override
	public void onSubSequenceFinished(IModifier<Object> pModifier, Object pItem, int pIndex) {
		this.mSubSequenceFinishedCount++;
	}

	// ===========================================================
	// Test-Methods
	// ===========================================================

	public void testDuration() throws Exception {
		float elapsed;
		this.mSequenceModifier = new SequenceModifier<Object>(this, new DummyModifier(DURATION), new DummyModifier(DURATION));

		Assert.assertEquals(0, this.mSubSequenceStartedCount);
		Assert.assertEquals(0, this.mSubSequenceFinishedCount);

		elapsed = this.mSequenceModifier.onUpdate(DURATION, null);
		Assert.assertEquals(DURATION, elapsed, DELTA);

		Assert.assertEquals(1, this.mSubSequenceStartedCount);
		Assert.assertEquals(1, this.mSubSequenceFinishedCount);
	}

	public void testDurationAlmost() throws Exception {
		float elapsed;
		this.mSequenceModifier = new SequenceModifier<Object>(this, new DummyModifier(DURATION), new DummyModifier(DURATION));

		Assert.assertEquals(0, this.mSubSequenceStartedCount);
		Assert.assertEquals(0, this.mSubSequenceFinishedCount);

		elapsed = this.mSequenceModifier.onUpdate(DURATION - DURATION_DELTA, null);
		Assert.assertEquals(DURATION - DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mSubSequenceStartedCount);
		Assert.assertEquals(0, this.mSubSequenceFinishedCount);

		elapsed = this.mSequenceModifier.onUpdate(2 * DURATION_DELTA, null);
		Assert.assertEquals(2 * DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(2, this.mSubSequenceStartedCount);
		Assert.assertEquals(1, this.mSubSequenceFinishedCount);
	}

	public void testDurationDouble() throws Exception {
		float elapsed;
		this.mSequenceModifier = new SequenceModifier<Object>(this, new DummyModifier(DURATION), new DummyModifier(DURATION));

		Assert.assertEquals(0, this.mSubSequenceStartedCount);
		Assert.assertEquals(0, this.mSubSequenceFinishedCount);

		elapsed = this.mSequenceModifier.onUpdate(2 * DURATION, null);
		Assert.assertEquals(2 * DURATION, elapsed, DELTA);

		Assert.assertEquals(2, this.mSubSequenceStartedCount);
		Assert.assertEquals(2, this.mSubSequenceFinishedCount);

		elapsed = this.mSequenceModifier.onUpdate(DURATION_DELTA, null);
		Assert.assertEquals(0, elapsed, DELTA);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class DummyModifier extends BaseDurationModifier<Object> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		private DummyModifier(final float pDuration) {
			super(pDuration);
		}

		@Override
		public IModifier<Object> deepCopy() throws DeepCopyNotSupportedException {
			return null;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		protected void onManagedUpdate(final float pSecondsElapsed, final Object pItem) {

		}

		@Override
		protected void onManagedInitialize(final Object pItem) {

		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
