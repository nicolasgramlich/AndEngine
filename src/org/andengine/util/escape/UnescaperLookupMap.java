package org.andengine.util.escape;

import org.andengine.util.adt.map.IntLookupMap;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:31:08 - 26.04.2012
 */
public class UnescaperLookupMap extends IntLookupMap<CharSequence> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int LOOKUP_TABLE_SIZE = 256;

	// ===========================================================
	// Fields
	// ===========================================================

	private final CharSequence[] mLookupTable = new CharSequence[UnescaperLookupMap.LOOKUP_TABLE_SIZE];

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

	@Override
	public CharSequence item(final int pValue) {
		if(pValue < UnescaperLookupMap.LOOKUP_TABLE_SIZE) {
			return this.mLookupTable[pValue];
		}
		return super.item(pValue);
	}

	public void init() {
		for(int i = 0; i < UnescaperLookupMap.LOOKUP_TABLE_SIZE; ++i) {
			this.mLookupTable[i] = super.item(i);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}