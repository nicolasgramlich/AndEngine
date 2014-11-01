package org.andengine.util.adt.cache;

import junit.framework.Assert;

import org.andengine.util.adt.array.ArrayUtils;
import org.andengine.util.math.MathUtils;

import android.test.AndroidTestCase;

/**
 * @author Nicolas Gramlich
 * @since 13:47:55 - 16.06.2011
 */
public class IntLRUCacheTest extends AndroidTestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int[] KEYS = { 564707459, 825147574, 885279350, 1764732973, 802982945, 112847945, 46992802, 536122490, 414684432, 1341383647 };
	private static final String[] VALUES = { "564707459", "825147574", "885279350", "1764732973", "802982945", "112847945", "46992802", "536122490", "414684432", "1341383647" };

	private static final int CORRECTNESS_ITERATIONS = 100;
	private static final int CORRECTNESS_ITERATIONS_CLEAR = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void setUp() throws Exception {
		MathUtils.RANDOM.setSeed(9876543210L);
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

	public void testCorrectness() throws Exception {
		final IntLRUCache<String> intLRUCache = new IntLRUCache<String>(5);
		final LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(5);
		
		for(int i = 0; i < CORRECTNESS_ITERATIONS; i++) {
			final int randomKey = ArrayUtils.random(KEYS);
			final String randomValue = ArrayUtils.random(VALUES);

			intLRUCache.put(randomKey, randomValue);
			lruCache.put(randomKey, randomValue);

			final int randomGetKey = ArrayUtils.random(KEYS);
			Assert.assertEquals(lruCache.get(randomGetKey), intLRUCache.get(randomGetKey));
		}
	}

	public void testClear() throws Exception {
		final IntLRUCache<String> intLRUCache = new IntLRUCache<String>(5);
		final LRUCache<Integer, String> lruCache = new LRUCache<Integer, String>(5);
		
		for(int i = 0; i < CORRECTNESS_ITERATIONS; i++) {
			final int randomKey = ArrayUtils.random(KEYS);
			final String randomValue = ArrayUtils.random(VALUES);
			
			intLRUCache.put(randomKey, randomValue);
			lruCache.put(randomKey, randomValue);
			
			final int randomGetKey = ArrayUtils.random(KEYS);
			Assert.assertEquals(lruCache.get(randomGetKey), intLRUCache.get(randomGetKey));
			
			if(i % CORRECTNESS_ITERATIONS_CLEAR == 0) {
				intLRUCache.clear();
				lruCache.clear();
			}
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
