package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.ModifierList;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:19:18 - 24.12.2010
 */
public class EntityModifierList extends ModifierList<IEntity> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final long serialVersionUID = 161652765736600082L;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public EntityModifierList(final IEntity pTarget) {
		super(pTarget);
	}

	public EntityModifierList(final IEntity pTarget, final int pCapacity) {
		super(pTarget, pCapacity);
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
