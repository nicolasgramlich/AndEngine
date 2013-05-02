package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ease.EaseLinear;
import org.andengine.util.modifier.ease.IEaseFunction;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:12:55 - 11.01.2012
 */
public class JumpModifier extends MoveModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int JUMPCOUNT_DEFAULT = 1;

	// ===========================================================
	// Fields
	// ===========================================================

	protected final float mJumpHeight;
	protected final int mJumpCount;

	// ===========================================================
	// Constructors
	// ===========================================================

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final float pJumpHeight) {
		this(pDuration, pFromX, pToX, pFromY, pToY, pJumpHeight, JumpModifier.JUMPCOUNT_DEFAULT, EaseLinear.getInstance());
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final float pJumpHeight, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromX, pToX, pFromY, pToY, pJumpHeight, JumpModifier.JUMPCOUNT_DEFAULT, pEaseFunction);
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final float pJumpHeight, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pFromX, pToX, pFromY, pToY, pJumpHeight, JumpModifier.JUMPCOUNT_DEFAULT, pEntityModifierListener, EaseLinear.getInstance());
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final float pJumpHeight, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromX, pToX, pFromY, pToY, pJumpHeight, JumpModifier.JUMPCOUNT_DEFAULT, pEntityModifierListener, pEaseFunction);
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final float pJumpHeight, final int pJumpCount) {
		this(pDuration, pFromX, pToX, pFromY, pToY, pJumpHeight, pJumpCount, EaseLinear.getInstance());
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final float pJumpHeight, final int pJumpCount, final IEaseFunction pEaseFunction) {
		this(pDuration, pFromX, pToX, pFromY, pToY, pJumpHeight, pJumpCount, null, pEaseFunction);
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final float pJumpHeight, final int pJumpCount, final IEntityModifierListener pEntityModifierListener) {
		this(pDuration, pFromX, pToX, pFromY, pToY, pJumpHeight, pJumpCount, pEntityModifierListener, EaseLinear.getInstance());
	}

	public JumpModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final float pJumpHeight, final int pJumpCount, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromX, pFromY, pToX, pToY, pEntityModifierListener, pEaseFunction);

		this.mJumpHeight = pJumpHeight;
		this.mJumpCount = pJumpCount;
	}

	public JumpModifier(final JumpModifier pJumpModifier) {
		super(pJumpModifier);

		this.mJumpHeight = pJumpModifier.mJumpHeight;
		this.mJumpCount = pJumpModifier.mJumpCount;
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
		final float fraction = (pPercentageDone * this.mJumpCount) % 1.0f;
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
