package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IModifierListener;
import org.anddev.andengine.entity.shape.Shape;

/**
 * @author Nicolas Gramlich
 * @since 16:12:52 - 19.03.2010
 */
public class RotateByModifier extends BaseModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mAnglePerSecond;

	// ===========================================================
	// Constructors
	// ===========================================================

	public RotateByModifier(final float pDuration, final float pAngle) {
		this(pDuration, pAngle, null);
	}

	public RotateByModifier(final float pDuration, final float pAngle, final IModifierListener pModiferListener) {
		super(pDuration, pModiferListener);
		this.mAnglePerSecond = pAngle / pDuration;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedInitializeShape(final Shape pShape) {

	}

	@Override
	protected void onManagedUpdateShape(final float pSecondsElapsed, final Shape pShape) {
		pShape.setAngle(pShape.getAngle() + this.mAnglePerSecond * pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
