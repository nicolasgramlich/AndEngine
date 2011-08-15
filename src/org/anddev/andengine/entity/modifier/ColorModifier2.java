package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.color.Color;
import org.anddev.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 11:05:24 - 12.08.2011
 */
public class ColorModifier2 extends DurationEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IEaseFunction mEaseFunction;
	private final Color mFromColor;
	private final Color mToColor;
	private Color mInterpolatedColor;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ColorModifier2(final float pDuration, final Color pFromColor, final Color pToColor, final IEaseFunction pEaseFunction) {
		super(pDuration);
		this.mFromColor = pFromColor;
		this.mToColor = pToColor;
		this.mEaseFunction = pEaseFunction;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed, final IEntity pItem) {
		final float percentageDone = this.mEaseFunction.getPercentage(this.getSecondsElapsed(), this.mDuration);

		this.mInterpolatedColor.mix(this.mFromColor,percentageDone,  this.mToColor, 1 - percentageDone);
	}

	@Override
	protected void onManagedInitialize(final IEntity pItem) {
	}

	@Override
	public ColorModifier2 clone() {
		return null;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
