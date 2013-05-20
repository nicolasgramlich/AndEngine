package org.andengine.input.sensor.location;

import org.andengine.util.time.TimeConstants;

import android.location.Criteria;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 11:02:12 - 31.10.2010
 */
public class LocationSensorOptions extends Criteria {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long MINIMUMTRIGGERTIME_DEFAULT = 1 * TimeConstants.MILLISECONDS_PER_SECOND;
	private static final long MINIMUMTRIGGERDISTANCE_DEFAULT = 10;

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mEnabledOnly = true;

	private long mMinimumTriggerTime = MINIMUMTRIGGERTIME_DEFAULT;
	private long mMinimumTriggerDistance = MINIMUMTRIGGERDISTANCE_DEFAULT;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @see {@link #setAccuracy(int)}, {@link #setAltitudeRequired(boolean)}, {@link #setBearingRequired(boolean)}, {@link #setCostAllowed(boolean)}, {@link #setEnabledOnly(boolean)}, {@link #setMinimumTriggerDistance(long)}, {@link #setMinimumTriggerTime(long)}, {@link #setPowerRequirement(int)}, {@link #setSpeedRequired(boolean)}.
	 */
	public LocationSensorOptions() {

	}

	/**
	 * @param pAccuracy
	 * @param pAltitudeRequired
	 * @param pBearingRequired
	 * @param pCostAllowed
	 * @param pPowerRequirement
	 * @param pSpeedRequired
	 * @param pEnabledOnly
	 * @param pMinimumTriggerTime
	 * @param pMinimumTriggerDistance
	 */
	public LocationSensorOptions(final int pAccuracy, final boolean pAltitudeRequired, final boolean pBearingRequired, final boolean pCostAllowed, final int pPowerRequirement, final boolean pSpeedRequired, final boolean pEnabledOnly, final long pMinimumTriggerTime, final long pMinimumTriggerDistance) {
		this.mEnabledOnly = pEnabledOnly;
		this.mMinimumTriggerTime = pMinimumTriggerTime;
		this.mMinimumTriggerDistance = pMinimumTriggerDistance;

		this.setAccuracy(pAccuracy);
		this.setAltitudeRequired(pAltitudeRequired);
		this.setBearingRequired(pBearingRequired);
		this.setCostAllowed(pCostAllowed);
		this.setPowerRequirement(pPowerRequirement);
		this.setSpeedRequired(pSpeedRequired);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setEnabledOnly(final boolean pEnabledOnly) {
		this.mEnabledOnly = pEnabledOnly;
	}

	public boolean isEnabledOnly() {
		return this.mEnabledOnly;
	}

	public long getMinimumTriggerTime() {
		return this.mMinimumTriggerTime;
	}

	public void setMinimumTriggerTime(final long pMinimumTriggerTime) {
		this.mMinimumTriggerTime = pMinimumTriggerTime;
	}

	public long getMinimumTriggerDistance() {
		return this.mMinimumTriggerDistance;
	}

	public void setMinimumTriggerDistance(final long pMinimumTriggerDistance) {
		this.mMinimumTriggerDistance = pMinimumTriggerDistance;
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
