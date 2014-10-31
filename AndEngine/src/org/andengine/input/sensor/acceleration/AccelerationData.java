package org.andengine.input.sensor.acceleration;

import java.util.Arrays;

import org.andengine.input.sensor.BaseSensorData;

import android.hardware.SensorManager;
import android.view.Surface;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:50:44 - 10.03.2010
 */
public class AccelerationData extends BaseSensorData {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final IAxisSwap AXISSWAPS[] = new IAxisSwap[4];

	static {
		AXISSWAPS[Surface.ROTATION_0] = new IAxisSwap() {
			@Override
			public void swapAxis(final float[] pValues) {
				final float x = -pValues[SensorManager.DATA_X];
				final float y = pValues[SensorManager.DATA_Y];
				pValues[SensorManager.DATA_X] = x;
				pValues[SensorManager.DATA_Y] = y;
			}
		};

		AXISSWAPS[Surface.ROTATION_90] = new IAxisSwap() {
			@Override
			public void swapAxis(final float[] pValues) {
				final float x = pValues[SensorManager.DATA_Y];
				final float y = pValues[SensorManager.DATA_X];
				pValues[SensorManager.DATA_X] = x;
				pValues[SensorManager.DATA_Y] = y;
			}
		};

		AXISSWAPS[Surface.ROTATION_180] = new IAxisSwap() {
			@Override
			public void swapAxis(final float[] pValues) {
				final float x = pValues[SensorManager.DATA_X];
				final float y = -pValues[SensorManager.DATA_Y];
				pValues[SensorManager.DATA_X] = x;
				pValues[SensorManager.DATA_Y] = y;
			}
		};

		AXISSWAPS[Surface.ROTATION_270] = new IAxisSwap() {
			@Override
			public void swapAxis(final float[] pValues) {
				final float x = -pValues[SensorManager.DATA_Y];
				final float y = -pValues[SensorManager.DATA_X];
				pValues[SensorManager.DATA_X] = x;
				pValues[SensorManager.DATA_Y] = y;
			}
		};
	}

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AccelerationData(final int pDisplayOrientation) {
		super(3, pDisplayOrientation);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getX() {
		return this.mValues[SensorManager.DATA_X];
	}

	public float getY() {
		return this.mValues[SensorManager.DATA_Y];
	}

	public float getZ() {
		return this.mValues[SensorManager.DATA_Z];
	}

	public void setX(final float pX) {
		this.mValues[SensorManager.DATA_X] = pX;
	}

	public void setY(final float pY) {
		this.mValues[SensorManager.DATA_Y] = pY;
	}

	public void setZ(final float pZ) {
		this.mValues[SensorManager.DATA_Z]  = pZ;
	}

	@Override
	public void setValues(final float[] pValues) {
		super.setValues(pValues);

		AccelerationData.AXISSWAPS[this.mDisplayRotation].swapAxis(this.mValues);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String toString() {
		return "Acceleration: " + Arrays.toString(this.mValues);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static interface IAxisSwap {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void swapAxis(final float[] pValues);
	}
}
