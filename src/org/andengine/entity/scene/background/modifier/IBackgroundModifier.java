package org.andengine.entity.scene.background.modifier;

import org.andengine.entity.scene.background.IBackground;
import org.andengine.util.modifier.IModifier;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:55:54 - 03.09.2010
 */
public interface IBackgroundModifier extends IModifier<IBackground> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	public IBackgroundModifier deepCopy() throws DeepCopyNotSupportedException;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IBackgroundModifierListener extends IModifierListener<IBackground> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
