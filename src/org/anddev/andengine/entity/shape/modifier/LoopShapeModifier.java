package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.util.modifier.LoopModifier;

/**
 * @author Nicolas Gramlich
 * @since 12:42:13 - 03.09.2010
 */
public class LoopShapeModifier extends LoopModifier<IShape> implements IShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public LoopShapeModifier(final IShapeModifier pShapeModifier) {
		super(pShapeModifier);
	}

	public LoopShapeModifier(final IShapeModifierListener pShapeModiferListener, final int pLoopCount, final ILoopShapeModifierListener pLoopModifierListener, final IShapeModifier pShapeModifier) {
		super(pShapeModiferListener, pLoopCount, pLoopModifierListener, pShapeModifier);
	}

	public LoopShapeModifier(final IShapeModifierListener pShapeModiferListener, final int pLoopCount, final IShapeModifier pShapeModifier) {
		super(pShapeModiferListener, pLoopCount, pShapeModifier);
	}

	public LoopShapeModifier(final int pLoopCount, final IShapeModifier pShapeModifier) {
		super(pLoopCount, pShapeModifier);
	}

	protected LoopShapeModifier(final LoopShapeModifier pLoopShapeModifier) {
		super(pLoopShapeModifier);
	}

	@Override
	public LoopShapeModifier clone() {
		return new LoopShapeModifier(this);
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

	public interface ILoopShapeModifierListener extends ILoopModifierListener<IShape> {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================
	}
}
