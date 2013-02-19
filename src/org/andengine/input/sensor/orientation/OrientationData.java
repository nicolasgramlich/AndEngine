package org.andengine.input.sensor.orientation;

import java.util.Arrays;

import org.andengine.input.sensor.BaseSensorData;
import org.andengine.util.math.MathConstants;

import android.hardware.SensorManager;
import android.view.Surface;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
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

	private final float[] mAccelerationValues = new float[3];
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

	public void setAccelerationValues(final float[] pValues) {
		System.arraycopy(pValues, 0, this.mAccelerationValues, 0, pValues.length);

		this.updateOrientation();
	}

	public void setMagneticFieldValues(final float[] pValues) {
		System.arraycopy(pValues, 0, this.mMagneticFieldValues, 0, pValues.length);

		this.updateOrientation();
	}

	private void updateOrientation() {
		SensorManager.getRotationMatrix(this.mRotationMatrix, null, this.mAccelerationValues, this.mMagneticFieldValues);

		// TODO Use dont't use identical matrixes in remapCoordinateSystem, due to performance reasons.
		switch (this.mDisplayRotation) {
			case Surface.ROTATION_0:
				/* Nothing. */
				break;
			case Surface.ROTATION_90:
				SensorManager.remapCoordinateSystem(this.mRotationMatrix, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, this.mRotationMatrix);
				break;
//			case Surface.ROTATION_180:
//				SensorManager.remapCoordinateSystem(this.mRotationMatrix, SensorManager.AXIS_?, SensorManager.AXIS_?, this.mRotationMatrix);
//				break;
//			case Surface.ROTATION_270:
//				SensorManager.remapCoordinateSystem(this.mRotationMatrix, SensorManager.AXIS_?, SensorManager.AXIS_?, this.mRotationMatrix);
//				break;
		}

		final float[] values = this.mValues;
		SensorManager.getOrientation(this.mRotationMatrix, values);

		for (int i = values.length - 1; i >= 0; i--) {
			values[i] = values[i] * MathConstants.RAD_TO_DEG;
		}
	}

	public int getAccelerationAccuracy() {
		return this.getAccuracy();
	}

	public void setAccelerationAccuracy(final int pAccelerationAccuracy) {
		super.setAccuracy(pAccelerationAccuracy);
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
