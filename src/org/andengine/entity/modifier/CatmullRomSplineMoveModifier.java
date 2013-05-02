package org.andengine.entity.modifier;

import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:51:22 - 21.03.2012
 */
public class CatmullRomSplineMoveModifier extends CardinalSplineMoveModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public CatmullRomSplineMoveModifier(final float pDuration, final CatmullRomMoveModifierConfig pCatmullRomMoveModifierConfig) {
		super(pDuration, pCatmullRomMoveModifierConfig);
	}

	public CatmullRomSplineMoveModifier(final float pDuration, final CatmullRomMoveModifierConfig pCatmullRomMoveModifierConfig, final IEaseFunction pEaseFunction) {
		super(pDuration, pCatmullRomMoveModifierConfig, pEaseFunction);
	}

	public CatmullRomSplineMoveModifier(final float pDuration, final CatmullRomMoveModifierConfig pCatmullRomMoveModifierConfig, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pCatmullRomMoveModifierConfig, pEntityModifierListener);
	}

	public CatmullRomSplineMoveModifier(final float pDuration, final CatmullRomMoveModifierConfig pCatmullRomMoveModifierConfig, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pCatmullRomMoveModifierConfig, pEntityModifierListener, pEaseFunction);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class CatmullRomMoveModifierConfig extends CardinalSplineMoveModifierConfig {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final int CARDINALSPLINE_CATMULLROM_TENSION = 0;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public CatmullRomMoveModifierConfig(final int pControlPointCount) {
			super(pControlPointCount, CatmullRomMoveModifierConfig.CARDINALSPLINE_CATMULLROM_TENSION);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

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
}
