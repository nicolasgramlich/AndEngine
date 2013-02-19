package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.LoopModifier;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 12:42:13 - 03.09.2010
 */
public class LoopEntityModifier extends LoopModifier<IEntity> implements IEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LoopEntityModifier(final IEntityModifier pEntityModifier) {
		super(pEntityModifier);
	}

	public LoopEntityModifier(final IEntityModifier pEntityModifier, final int pLoopCount) {
		super(pEntityModifier, pLoopCount);
	}

	public LoopEntityModifier(final IEntityModifier pEntityModifier, final int pLoopCount, final ILoopEntityModifierListener pLoopModifierListener) {
		super(pEntityModifier, pLoopCount, pLoopModifierListener);
	}

	public LoopEntityModifier(final IEntityModifier pEntityModifier, final int pLoopCount, final IEntityModifierListener pEntityModifierListener) {
		super(pEntityModifier, pLoopCount, pEntityModifierListener);
	}

	public LoopEntityModifier(final IEntityModifierListener pEntityModifierListener, final int pLoopCount, final ILoopEntityModifierListener pLoopModifierListener, final IEntityModifier pEntityModifier) {
		super(pEntityModifier, pLoopCount, pLoopModifierListener, pEntityModifierListener);
	}

	protected LoopEntityModifier(final LoopEntityModifier pLoopEntityModifier) throws DeepCopyNotSupportedException {
		super(pLoopEntityModifier);
	}

	@Override
	public LoopEntityModifier deepCopy() throws DeepCopyNotSupportedException {
		return new LoopEntityModifier(this);
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

	public interface ILoopEntityModifierListener extends ILoopModifierListener<IEntity> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
