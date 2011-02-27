package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.IMatcher;
import org.anddev.andengine.util.modifier.IModifier;

/**
 * @author Nicolas Gramlich
 * @since 11:17:50 - 19.03.2010
 */
public interface IEntityModifier extends IModifier<IEntity> {
	// ===========================================================
	// Final Fields
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IEntityModifierListener extends IModifierListener<IEntity>{
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
	
	public interface IEntityModifierMatcher extends IMatcher<IModifier<IEntity>> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
