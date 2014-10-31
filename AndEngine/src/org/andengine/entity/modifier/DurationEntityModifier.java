package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.BaseDurationModifier;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:10:42 - 19.03.2010
 */
public abstract class DurationEntityModifier extends BaseDurationModifier<IEntity> implements IEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public DurationEntityModifier(final float pDuration) {
		super(pDuration);
	}

	public DurationEntityModifier(final float pDuration, final IEntityModifierListener pEntityModifierListener) {
		super(pDuration, pEntityModifierListener);
	}

	protected DurationEntityModifier(final DurationEntityModifier pDurationEntityModifier) {
		super(pDurationEntityModifier);
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
