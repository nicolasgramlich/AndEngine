package org.andengine.util.experiment;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 01:22:34 - 22.03.2013
 */
public class Experiment<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final String mName;
	private final Class<T> mType;
	private final T mValue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Experiment(final String pName, final Class<T> pType, final T pValue) {
		this.mName = pName;
		this.mType = pType;
		this.mValue = pValue;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getName() {
		return this.mName;
	}

	public Class<T> getType() {
		return this.mType;
	}

	public T getValue() {
		return this.mValue;
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
