package org.andengine.util.modifier;

import junit.framework.Assert;

import org.andengine.util.modifier.LoopModifier.ILoopModifierListener;

import android.test.AndroidTestCase;

/**
 * @author Nicolas Gramlich
 * @since 13:47:55 - 16.06.2011
 */
public class LoopModifierTest extends AndroidTestCase implements ILoopModifierListener<Object> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DELTA = 0.001f;

	private static final float DURATION = 10;
	private static final float DURATION_DELTA = 0.001f;

	// ===========================================================
	// Fields
	// ===========================================================

	private int mLoopsStarted;
	private int mLoopsFinished;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mLoopsStarted = 0;
		this.mLoopsFinished = 0;
	}

	@Override
	public void tearDown() throws Exception {

	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onLoopStarted(final LoopModifier<Object> pLoopModifier, final int pLoop, final int pLoopCount) {
		this.mLoopsStarted++;
	}

	@Override
	public void onLoopFinished(final LoopModifier<Object> pLoopModifier, final int pLoop, final int pLoopCount) {
		this.mLoopsFinished++;
	}

	// ===========================================================
	// Test-Methods
	// ===========================================================

	public void testDuration() throws Exception {
		float elapsed;
		final LoopModifier<Object> loopModifier = new LoopModifier<Object>(new DummyModifier(DURATION), LoopModifier.LOOP_CONTINUOUS, this);

		Assert.assertEquals(0, this.mLoopsStarted);
		Assert.assertEquals(0, this.mLoopsFinished);

		elapsed = loopModifier.onUpdate(DURATION, null);
		Assert.assertEquals(DURATION, elapsed, DELTA);

		Assert.assertEquals(1, this.mLoopsStarted);
		Assert.assertEquals(1, this.mLoopsFinished);
	}

	public void testDurationNonContinuous() throws Exception {
		float elapsed;
		final LoopModifier<Object> loopModifier = new LoopModifier<Object>(new DummyModifier(DURATION), 1, this);

		Assert.assertEquals(0, this.mLoopsStarted);
		Assert.assertEquals(0, this.mLoopsFinished);

		elapsed = loopModifier.onUpdate(DURATION, null);
		Assert.assertEquals(DURATION, elapsed, DELTA);

		Assert.assertEquals(1, this.mLoopsStarted);
		Assert.assertEquals(1, this.mLoopsFinished);

		elapsed = loopModifier.onUpdate(DURATION, null);
		Assert.assertEquals(0, elapsed, DELTA);
		
		Assert.assertEquals(1, this.mLoopsStarted);
		Assert.assertEquals(1, this.mLoopsFinished);
	}


	public void testDurationNonContinuousAlmost() throws Exception {
		float elapsed;
		final LoopModifier<Object> loopModifier = new LoopModifier<Object>(new DummyModifier(DURATION), 1, this);

		Assert.assertEquals(0, this.mLoopsStarted);
		Assert.assertEquals(0, this.mLoopsFinished);

		elapsed = loopModifier.onUpdate(DURATION - DURATION_DELTA, null);
		Assert.assertEquals(DURATION - DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mLoopsStarted);
		Assert.assertEquals(0, this.mLoopsFinished);

		elapsed = loopModifier.onUpdate(DURATION_DELTA, null);
		Assert.assertEquals(DURATION_DELTA, elapsed, DELTA);
	}

	public void testDurationAlmost() throws Exception {
		float elapsed;
		final LoopModifier<Object> loopModifier = new LoopModifier<Object>(new DummyModifier(DURATION), LoopModifier.LOOP_CONTINUOUS, this);

		Assert.assertEquals(0, this.mLoopsStarted);
		Assert.assertEquals(0, this.mLoopsFinished);

		elapsed = loopModifier.onUpdate(DURATION - DURATION_DELTA, null);
		Assert.assertEquals(DURATION - DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mLoopsStarted);
		Assert.assertEquals(0, this.mLoopsFinished);

		elapsed = loopModifier.onUpdate(2 * DURATION_DELTA, null);
		Assert.assertEquals(2 * DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(2, this.mLoopsStarted);
		Assert.assertEquals(1, this.mLoopsFinished);
	}

	public void testDurationDouble() throws Exception {
		float elapsed;
		final LoopModifier<Object> loopModifier = new LoopModifier<Object>(new DummyModifier(DURATION), LoopModifier.LOOP_CONTINUOUS, this);

		Assert.assertEquals(0, this.mLoopsStarted);
		Assert.assertEquals(0, this.mLoopsFinished);

		elapsed = loopModifier.onUpdate(2 * DURATION, null);
		Assert.assertEquals(2 * DURATION, elapsed, DELTA);

		Assert.assertEquals(2, this.mLoopsStarted);
		Assert.assertEquals(2, this.mLoopsFinished);
	}

	@SuppressWarnings("unchecked")
	public void testLoopSequenceLoopSequenceNesting() throws Exception {
		final SequenceModifier<Object> innerSequenceModifier = new SequenceModifier<Object>(new DummyModifier(DURATION), new DummyModifier(DURATION));
		final LoopModifier<Object> innerLoopModifier = new LoopModifier<Object>(innerSequenceModifier, 3);
		final SequenceModifier<Object> outerSequenceModifier = new SequenceModifier<Object>(new IModifier[]{innerLoopModifier, new DummyModifier(DURATION)});
		final LoopModifier<Object> outerLoopModifier = new LoopModifier<Object>(outerSequenceModifier);
		
		/* Should not cause an infinite loop. */
		outerLoopModifier.onUpdate(1000, null);
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
