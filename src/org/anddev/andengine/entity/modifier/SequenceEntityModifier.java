package org.anddev.andengine.entity.modifier;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.util.modifier.SequenceModifier;

/**
 * @author Nicolas Gramlich
 * @since 12:41:15 - 03.09.2010
 */
public class SequenceEntityModifier extends SequenceModifier<IEntity> implements IEntityModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public SequenceEntityModifier(final IEntityModifier... pEntityModifiers) throws IllegalArgumentException {
		super(pEntityModifiers);
	}

	public SequenceEntityModifier(final IEntityModifierListener pEntityModifierListener, final IEntityModifier... pEntityModifiers) throws IllegalArgumentException {
		super(pEntityModifierListener, pEntityModifiers);
	}

	public SequenceEntityModifier(final IEntityModifierListener pEntityModifierListener, final ISubSequenceShapeModifierListener pSubSequenceShapeModifierListener, final IEntityModifier... pEntityModifiers) throws IllegalArgumentException {
		super(pEntityModifierListener, pSubSequenceShapeModifierListener, pEntityModifiers);
	}

	protected SequenceEntityModifier(final SequenceEntityModifier pSequenceShapeModifier) {
		super(pSequenceShapeModifier);
	}

	@Override
	public SequenceEntityModifier clone() {
		return new SequenceEntityModifier(this);
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

	public interface ISubSequenceShapeModifierListener extends ISubSequenceModifierListener<IEntity> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
