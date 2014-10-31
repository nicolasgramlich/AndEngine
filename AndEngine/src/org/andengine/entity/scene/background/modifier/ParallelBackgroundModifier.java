package org.andengine.entity.scene.background.modifier;

import org.andengine.entity.scene.background.IBackground;
import org.andengine.util.modifier.ParallelModifier;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:03:57 - 03.09.2010
 */
public class ParallelBackgroundModifier extends ParallelModifier<IBackground> implements IBackgroundModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ParallelBackgroundModifier(final IBackgroundModifier... pBackgroundModifiers) throws IllegalArgumentException {
		super(pBackgroundModifiers);
	}

	public ParallelBackgroundModifier(final IBackgroundModifierListener pBackgroundModifierListener, final IBackgroundModifier... pBackgroundModifiers) throws IllegalArgumentException {
		super(pBackgroundModifierListener, pBackgroundModifiers);
	}

	protected ParallelBackgroundModifier(final ParallelBackgroundModifier pParallelBackgroundModifier) throws DeepCopyNotSupportedException {
		super(pParallelBackgroundModifier);
	}

	@Override
	public ParallelBackgroundModifier deepCopy() throws DeepCopyNotSupportedException {
		return new ParallelBackgroundModifier(this);
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
