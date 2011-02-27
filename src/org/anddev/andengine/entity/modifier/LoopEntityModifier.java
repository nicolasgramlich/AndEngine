package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.LoopModifier;

/**
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

	public LoopEntityModifier(final IEntityModifierListener pEntityModifierListener, final int pLoopCount, final ILoopEntityModifierListener pLoopModifierListener, final IEntityModifier pEntityModifier) {
		super(pEntityModifierListener, pLoopCount, pLoopModifierListener, pEntityModifier);
	}

	public LoopEntityModifier(final IEntityModifierListener pEntityModifierListener, final int pLoopCount, final IEntityModifier pEntityModifier) {
		super(pEntityModifierListener, pLoopCount, pEntityModifier);
	}

	public LoopEntityModifier(final int pLoopCount, final IEntityModifier pEntityModifier) {
		super(pLoopCount, pEntityModifier);
	}

	protected LoopEntityModifier(final LoopEntityModifier pLoopEntityModifier) {
		super(pLoopEntityModifier);
	}

	@Override
	public LoopEntityModifier clone() {
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
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
