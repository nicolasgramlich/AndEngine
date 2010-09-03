package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.modifier.SequenceModifier;

/**
 * @author Nicolas Gramlich
 * @since 12:41:15 - 03.09.2010
 */
public class SequenceShapeModifier extends SequenceModifier<IShape> implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SequenceShapeModifier(final IShapeModifier... pShapeModifiers) throws IllegalArgumentException {
		super(pShapeModifiers);
	}

	public SequenceShapeModifier(final IShapeModifierListener pShapeModiferListener, final IShapeModifier... pShapeModifiers) throws IllegalArgumentException {
		super(pShapeModiferListener, pShapeModifiers);
	}

	public SequenceShapeModifier(final IShapeModifierListener pShapeModiferListener, final ISubSequenceShapeModifierListener pSubSequenceShapeModifierListener, final IShapeModifier... pShapeModifiers) throws IllegalArgumentException {
		super(pShapeModiferListener, pSubSequenceShapeModifierListener, pShapeModifiers);
	}

	protected SequenceShapeModifier(final SequenceShapeModifier pSequenceShapeModifier) {
		super(pSequenceShapeModifier);
	}

	@Override
	public SequenceShapeModifier clone() {
		return new SequenceShapeModifier(this);
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

	public interface ISubSequenceShapeModifierListener extends ISubSequenceModifierListener<IShape> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
