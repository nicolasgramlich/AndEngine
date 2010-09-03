package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.modifier.BaseModifier;

/**
 * @author Nicolas Gramlich
 * @since 10:53:16 - 03.09.2010
 */
public abstract class ShapeModifier extends BaseModifier<IShape> implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public ShapeModifier() {
		super();
	}

	public ShapeModifier(final IShapeModifierListener pShapeModiferListener) {
		super(pShapeModiferListener);
	}

	protected ShapeModifier(final ShapeModifier pShapeModifier) {
		super(pShapeModifier);
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
