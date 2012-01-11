package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:12:55 - 11.01.2012
 */
public class JumpModifier extends MoveModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected float mJumpHeight = 50;
	protected int mJumps = 4;

	// ===========================================================
	// Constructors
	// ===========================================================

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pEaseFunction);
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pEntityModifierListener, pEaseFunction);
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pEntityModifierListener);
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY) {
		super(pDuration, pFromX, pToX, pFromY, pToY);
	}

	public JumpModifier(final JumpModifier pJumpModifier) {
		super(pJumpModifier);
	}

	@Override
	public JumpModifier deepCopy() throws DeepCopyNotSupportedException {
		return new JumpModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onSetValues(final IEntity pEntity, final float pPercentageDone, final float pX, final float pY) {
		final float fraction = (pPercentageDone * this.mJumps) % 1.0f;
		final float deltaY = this.mJumpHeight * 4 * fraction * (1 - fraction);

		super.onSetValues(pEntity, pPercentageDone, pX, pY - deltaY);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
