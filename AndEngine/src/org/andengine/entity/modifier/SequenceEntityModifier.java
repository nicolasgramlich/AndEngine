package org.andengine.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.util.modifier.SequenceModifier;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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

	public SequenceEntityModifier(final ISubSequenceShapeModifierListener pSubSequenceShapeModifierListener, final IEntityModifier... pEntityModifiers) throws IllegalArgumentException {
		super(pSubSequenceShapeModifierListener, pEntityModifiers);
	}

	public SequenceEntityModifier(final IEntityModifierListener pEntityModifierListener, final IEntityModifier... pEntityModifiers) throws IllegalArgumentException {
		super(pEntityModifierListener, pEntityModifiers);
	}

	public SequenceEntityModifier(final ISubSequenceShapeModifierListener pSubSequenceShapeModifierListener, final IEntityModifierListener pEntityModifierListener, final IEntityModifier... pEntityModifiers) throws IllegalArgumentException {
		super(pSubSequenceShapeModifierListener, pEntityModifierListener, pEntityModifiers);
	}

	protected SequenceEntityModifier(final SequenceEntityModifier pSequenceShapeModifier) throws DeepCopyNotSupportedException {
		super(pSequenceShapeModifier);
	}

	@Override
	public SequenceEntityModifier deepCopy() throws DeepCopyNotSupportedException {
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
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
