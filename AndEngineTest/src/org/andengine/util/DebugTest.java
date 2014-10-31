package org.andengine.util;

import junit.framework.TestCase;

import org.andengine.util.debug.Debug;
import org.andengine.util.debug.Debug.DebugLevel;

/**
 * @author Nicolas Gramlich
 * @since 22:31:38 - 16.09.2010
 */
public class DebugTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void testDebugLevel() {
		Debug.setDebugLevel(DebugLevel.VERBOSE);
//		Debug.v("1");
//		Debug.d("2");
//		Debug.i("3");
//		Debug.w("4");
//		Debug.e("5");
		Debug.setDebugLevel(DebugLevel.DEBUG);
		Debug.v("invisible");
//		Debug.d("6");
//		Debug.i("7");
//		Debug.w("8");
//		Debug.e("9");
		Debug.setDebugLevel(DebugLevel.INFO);
		Debug.v("invisible");
		Debug.d("invisible");
//		Debug.i("10");
//		Debug.w("11");
//		Debug.e("12");
		Debug.setDebugLevel(DebugLevel.WARNING);
		Debug.v("invisible");
		Debug.d("invisible");
		Debug.i("invisible");
//		Debug.w("13");
//		Debug.e("14");
		Debug.setDebugLevel(DebugLevel.ERROR);
		Debug.v("invisible");
		Debug.d("invisible");
		Debug.i("invisible");
		Debug.w("invisible");
//		Debug.e("14");
		Debug.setDebugLevel(DebugLevel.NONE);
		Debug.v("invisible");
		Debug.d("invisible");
		Debug.i("invisible");
		Debug.w("invisible");
		Debug.e("invisible");
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
