package org.andengine.examples.adt.cityradar;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:32:16 - 28.10.2010
 */
public class City {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mName;
	private final double mLatitude;
	private final double mLongitude;
	private double mDistanceToUser;
	private double mBearingToUser;

	// ===========================================================
	// Constructors
	// ===========================================================

	public City(final String pName, final double pLatitude, final double pLongitude) {
		this.mName = pName;
		this.mLatitude = pLatitude;
		this.mLongitude = pLongitude;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public final String getName() {
		return this.mName;
	}

	public final double getLatitude() {
		return this.mLatitude;
	}

	public final double getLongitude() {
		return this.mLongitude;
	}
	
	public double getDistanceToUser() {
		return this.mDistanceToUser;
	}

	public void setDistanceToUser(final double pDistanceToUser) {
		this.mDistanceToUser = pDistanceToUser;
	}
	
	public double getBearingToUser() {
		return this.mBearingToUser;
	}

	public void setBearingToUser(final double pBearingToUser) {
		this.mBearingToUser = pBearingToUser;
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