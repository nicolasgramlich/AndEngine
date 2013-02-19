package org.andengine.entity.scene.background.modifier;

import org.andengine.entity.scene.background.IBackground;
import org.andengine.util.modifier.SequenceModifier;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:04:02 - 03.09.2010
 */
public class SequenceBackgroundModifier extends SequenceModifier<IBackground> implements IBackgroundModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SequenceBackgroundModifier(final IBackgroundModifier... pBackgroundModifiers) throws IllegalArgumentException {
		super(pBackgroundModifiers);
	}

	public SequenceBackgroundModifier(final ISubSequenceBackgroundModifierListener pSubSequenceBackgroundModifierListener, final IBackgroundModifier... pBackgroundModifiers) throws IllegalArgumentException {
		super(pSubSequenceBackgroundModifierListener, pBackgroundModifiers);
	}

	public SequenceBackgroundModifier(final IBackgroundModifierListener pBackgroundModifierListener, final IBackgroundModifier... pBackgroundModifiers) throws IllegalArgumentException {
		super(pBackgroundModifierListener, pBackgroundModifiers);
	}

	public SequenceBackgroundModifier(final ISubSequenceBackgroundModifierListener pSubSequenceBackgroundModifierListener, final IBackgroundModifierListener pBackgroundModifierListener, final IBackgroundModifier... pBackgroundModifiers) throws IllegalArgumentException {
		super(pSubSequenceBackgroundModifierListener, pBackgroundModifierListener, pBackgroundModifiers);
	}

	protected SequenceBackgroundModifier(final SequenceBackgroundModifier pSequenceBackgroundModifier) throws DeepCopyNotSupportedException {
		super(pSequenceBackgroundModifier);
	}

	@Override
	public SequenceBackgroundModifier deepCopy() throws DeepCopyNotSupportedException {
		return new SequenceBackgroundModifier(this);
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

	public interface ISubSequenceBackgroundModifierListener extends ISubSequenceModifierListener<IBackground> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
