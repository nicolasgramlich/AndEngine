package org.anddev.andengine.sensor.orientation;

import java.util.Arrays;

import org.anddev.andengine.sensor.BaseSensorData;
import org.anddev.andengine.util.constants.MathConstants;

import android.hardware.SensorManager;

/**
 * @author Nicolas Gramlich
 * @since 11:30:33 - 25.05.2010
 */
public class OrientationData extends BaseSensorData {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float[] mAccelerometerValues = new float[3];
	private final float[] mMagneticFieldValues = new float[3];
	private final float[] mRotationMatrix = new float[16];

	private int mMagneticFieldAccuracy;

	// ===========================================================
	// Constructors
	// ===========================================================

	public OrientationData() {
		super(3);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getRoll() {
		return super.mValues[SensorManager.DATA_Z];
	}

	public float getPitch() {
		return super.mValues[SensorManager.DATA_Y];
	}

	public float getYaw() {
		return super.mValues[SensorManager.DATA_X];
	}

	@Override
	@Deprecated
	public void setValues(final float[] pValues) {
		super.setValues(pValues);
	}

	@Override
	@Deprecated
	public void setAccuracy(final int pAccuracy) {
		super.setAccuracy(pAccuracy);
	}

	public void setAccelerometerValues(final float[] pValues) {
		System.arraycopy(pValues, 0, this.mAccelerometerValues, 0, pValues.length);
		this.updateOrientation();
	}

	public void setMagneticFieldValues(final float[] pValues) {
		System.arraycopy(pValues, 0, this.mMagneticFieldValues, 0, pValues.length);
		this.updateOrientation();
	}

	private void updateOrientation() {
		SensorManager.getRotationMatrix(this.mRotationMatrix, null, this.mAccelerometerValues, this.mMagneticFieldValues);

		final float[] values = this.mValues;
		SensorManager.getOrientation(this.mRotationMatrix, values);

		for(int i = values.length - 1; i >= 0; i--) {
			values[i] = values[i] * MathConstants.RAD_TO_DEG;
		}
	}

	public int getAccelerometerAccuracy() {
		return this.getAccuracy();
	}

	public void setAccelerometerAccuracy(final int pAccelerometerAccuracy) {
		super.setAccuracy(pAccelerometerAccuracy);
	}

	public int getMagneticFieldAccuracy() {
		return this.mMagneticFieldAccuracy;
	}

	public void setMagneticFieldAccuracy(final int pMagneticFieldAccuracy) {
		this.mMagneticFieldAccuracy = pMagneticFieldAccuracy;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return "Orientation: " + Arrays.toString(this.mValues);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
