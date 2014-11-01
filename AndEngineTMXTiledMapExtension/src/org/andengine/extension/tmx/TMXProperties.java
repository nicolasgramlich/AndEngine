package org.andengine.extension.tmx;

import java.util.ArrayList;

import org.andengine.extension.tmx.util.constants.TMXConstants;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 10:14:06 - 27.07.2010
 */
public class TMXProperties<T extends TMXProperty> extends ArrayList<T> implements TMXConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 8912773556975105201L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean containsTMXProperty(final String pName, final String pValue) {
		for(int i = this.size() - 1; i >= 0; i--) {
			final T tmxProperty = this.get(i);
			if(tmxProperty.getName().equals(pName) && tmxProperty.getValue().equals(pValue)) {
				return true;
			}
		}
		return false;
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
