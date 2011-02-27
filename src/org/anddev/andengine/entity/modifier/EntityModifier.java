package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.BaseModifier;

/**
 * @author Nicolas Gramlich
 * @since 10:53:16 - 03.09.2010
 */
public abstract class EntityModifier extends BaseModifier<IEntity> implements IEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntityModifier() {
		super();
	}

	public EntityModifier(final IEntityModifierListener pEntityModifierListener) {
		super(pEntityModifierListener);
	}

	protected EntityModifier(final EntityModifier pEntityModifier) {
		super(pEntityModifier);
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
