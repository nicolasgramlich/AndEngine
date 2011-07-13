package org.anddev.andengine.entity.scene.background;

import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.ModifierList;



/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:08:17 - 19.07.2010
 */
public abstract class BaseBackground implements IBackground {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int BACKGROUNDMODIFIERS_CAPACITY_DEFAULT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private final ModifierList<IBackground> mBackgroundModifiers = new ModifierList<IBackground>(this, BACKGROUNDMODIFIERS_CAPACITY_DEFAULT);

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void addBackgroundModifier(final IModifier<IBackground> pBackgroundModifier) {
		this.mBackgroundModifiers.add(pBackgroundModifier);
	}

	@Override
	public boolean removeBackgroundModifier(final IModifier<IBackground> pBackgroundModifier) {
		return this.mBackgroundModifiers.remove(pBackgroundModifier);
	}

	@Override
	public void clearBackgroundModifiers() {
		this.mBackgroundModifiers.clear();
	}

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mBackgroundModifiers.onUpdate(pSecondsElapsed);
	}

	@Override
	public void reset() {
		this.mBackgroundModifiers.reset();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
