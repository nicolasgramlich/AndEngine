package org.andengine.input.sensor;

import java.util.Arrays;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:50:44 - 10.03.2010
 */
public class BaseSensorData {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float[] mValues;
	protected int mAccuracy;
	protected int mDisplayRotation;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSensorData(final int pValueCount, int pDisplayRotation) {
		this.mValues = new float[pValueCount];

		this.mDisplayRotation = pDisplayRotation;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float[] getValues() {
		return this.mValues;
	}

	public void setValues(final float[] pValues) {
		System.arraycopy(pValues, 0, this.mValues, 0, pValues.length);
	}

	public void setAccuracy(final int pAccuracy) {
		this.mAccuracy = pAccuracy;
	}

	public int getAccuracy() {
		return this.mAccuracy;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return "Values: " + Arrays.toString(this.mValues);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
