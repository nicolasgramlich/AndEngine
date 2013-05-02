package org.andengine.opengl.view;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:34:13 - 12.05.2012
 */
public abstract class ConfigChooserMatcher {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final ConfigChooserMatcher STRICT = new ConfigChooserMatcher() {
		@Override
		public boolean matches(final int pRequestedRedSize, final int pRedSize, final int pRequestedGreenSize, final int pGreenSize, final int pRequestedBlueSize, final int pBlueSize, final int pRequestedAlphaSize, final int pAlphaSize, final int pRequestedDepthSize, final int pDepthSize, final int pRequestedStencilSize, final int pStencilSize) {
			if (pDepthSize == pRequestedDepthSize && pStencilSize == pRequestedStencilSize) {
				if (pRedSize == pRequestedRedSize && pGreenSize == pRequestedGreenSize && pBlueSize == pRequestedBlueSize && pAlphaSize == pRequestedAlphaSize) {
					return true;
				}
			}
			return false;
		}
	};

	public static final ConfigChooserMatcher LOOSE_STENCIL = new ConfigChooserMatcher() {
		@Override
		public boolean matches(final int pRequestedRedSize, final int pRedSize, final int pRequestedGreenSize, final int pGreenSize, final int pRequestedBlueSize, final int pBlueSize, final int pRequestedAlphaSize, final int pAlphaSize, final int pRequestedDepthSize, final int pDepthSize, final int pRequestedStencilSize, final int pStencilSize) {
			if (pDepthSize == pRequestedDepthSize && pStencilSize >= pRequestedStencilSize) {
				if (pRedSize == pRequestedRedSize && pGreenSize == pRequestedGreenSize && pBlueSize == pRequestedBlueSize && pAlphaSize == pRequestedAlphaSize) {
					return true;
				}
			}
			return false;
		}
	};

	public static final ConfigChooserMatcher LOOSE_DEPTH_AND_STENCIL = new ConfigChooserMatcher() {
		@Override
		public boolean matches(final int pRequestedRedSize, final int pRedSize, final int pRequestedGreenSize, final int pGreenSize, final int pRequestedBlueSize, final int pBlueSize, final int pRequestedAlphaSize, final int pAlphaSize, final int pRequestedDepthSize, final int pDepthSize, final int pRequestedStencilSize, final int pStencilSize) {
			if (pDepthSize >= pRequestedDepthSize && pStencilSize >= pRequestedStencilSize) {
				if (pRedSize == pRequestedRedSize && pGreenSize == pRequestedGreenSize && pBlueSize == pRequestedBlueSize && pAlphaSize == pRequestedAlphaSize) {
					return true;
				}
			}
			return false;
		}
	};

	public static final ConfigChooserMatcher ANY = new ConfigChooserMatcher() {
		@Override
		public boolean matches(final int pRequestedRedSize, final int pRedSize, final int pRequestedGreenSize, final int pGreenSize, final int pRequestedBlueSize, final int pBlueSize, final int pRequestedAlphaSize, final int pAlphaSize, final int pRequestedDepthSize, final int pDepthSize, final int pRequestedStencilSize, final int pStencilSize) {
			return true;
		}
	};

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public abstract boolean matches(final int pRequestedRedSize, final int pRedSize, final int pRequestedGreenSize, final int pGreenSize, final int pRequestedBlueSize, final int pBlueSize, final int pRequestedAlphaSize, final int pAlphaSize, final int pRequestedDepthSize, final int pDepthSize, final int pRequestedStencilSize, final int pStencilSize);

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}