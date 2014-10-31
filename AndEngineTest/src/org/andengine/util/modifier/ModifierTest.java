package org.andengine.util.modifier;

import junit.framework.Assert;
import android.test.AndroidTestCase;

/**
 * @author Nicolas Gramlich
 * @since 13:47:55 - 16.06.2011
 */
public class ModifierTest extends AndroidTestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float DELTA = 0.001f;

	private static final float DURATION = 10;
	private static final float DURATION_DELTA = 0.001f;

	// ===========================================================
	// Fields
	// ===========================================================

	private IModifier<Object> mModifier;

	private int mManagedInitialize;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		this.mManagedInitialize = 0;
	}

	@Override
	public void tearDown() throws Exception {

	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Test-Methods
	// ===========================================================

	@SuppressWarnings("unchecked")
	public void test1() throws Exception {
		float elapsed;
		this.mModifier = new LoopModifier<Object>(
				new SequenceModifier<Object>(
						new DummyModifier(DURATION),
						new ParallelModifier<Object>(
								new CountingModifier(DURATION),
								new DummyModifier(DURATION)
						)
				), 2
		);

		elapsed = this.mModifier.onUpdate(DURATION, null);
		Assert.assertEquals(DURATION, elapsed, DELTA);

		Assert.assertEquals(0, this.mManagedInitialize);

		elapsed = this.mModifier.onUpdate(DURATION_DELTA, null);
		Assert.assertEquals(DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mManagedInitialize);
	}
	
	@SuppressWarnings("unchecked")
	public void test2() throws Exception {
		float elapsed;
		this.mModifier = new LoopModifier<Object>(
				new SequenceModifier<Object>(
						new DummyModifier(DURATION),
						new ParallelModifier<Object>(
								new CountingModifier(DURATION),
								new DummyModifier(DURATION)
						)
				), 2
		) {
			@Override
			public void reset() {
				super.reset();
			}
		};

		elapsed = this.mModifier.onUpdate(DURATION + DURATION - DURATION_DELTA, null);
		Assert.assertEquals(DURATION + DURATION - DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mManagedInitialize);

		elapsed = this.mModifier.onUpdate(DURATION_DELTA + DURATION_DELTA, null);
		Assert.assertEquals(DURATION_DELTA + DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mManagedInitialize);

		elapsed = this.mModifier.onUpdate(DURATION - DURATION_DELTA, null);
		Assert.assertEquals(DURATION - DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mManagedInitialize);

		elapsed = this.mModifier.onUpdate(DURATION_DELTA, null);
		Assert.assertEquals(DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(2, this.mManagedInitialize);
	}
	
	@SuppressWarnings("unchecked")
	public void test3() throws Exception {
		float elapsed;
		this.mModifier = new LoopModifier<Object>(
				new SequenceModifier<Object>(
						new DummyModifier(DURATION),
						new SequenceModifier<Object>(
								new CountingModifier(DURATION),
								new DummyModifier(DURATION)
						)
				), 2
		) {
			@Override
			public void reset() {
				super.reset();
			}
		};

		elapsed = this.mModifier.onUpdate(DURATION + DURATION + DURATION - DURATION_DELTA, null);
		Assert.assertEquals(DURATION + DURATION + DURATION - DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mManagedInitialize);

		elapsed = this.mModifier.onUpdate(DURATION_DELTA + DURATION_DELTA, null);
		Assert.assertEquals(DURATION_DELTA + DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mManagedInitialize);

		elapsed = this.mModifier.onUpdate(DURATION - DURATION_DELTA, null);
		Assert.assertEquals(DURATION - DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(1, this.mManagedInitialize);

		elapsed = this.mModifier.onUpdate(DURATION_DELTA, null);
		Assert.assertEquals(DURATION_DELTA, elapsed, DELTA);

		Assert.assertEquals(2, this.mManagedInitialize);
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
		
		@Override
		public void reset() {
			super.reset();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
	
	public class CountingModifier extends BaseDurationModifier<Object> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		private CountingModifier(final float pDuration) {
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
			mManagedInitialize++;
		}
		
		@Override
		public void reset() {
			super.reset();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
