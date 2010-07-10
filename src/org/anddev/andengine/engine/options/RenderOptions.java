package org.anddev.andengine.engine.options;

/**
 * @author Nicolas Gramlich
 * @since 13:01:40 - 02.07.2010
 */
public class RenderOptions {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private boolean mDisableExtensionVertexBufferObjects = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public RenderOptions disableExtensionVertexBufferObjects() {
		this.mDisableExtensionVertexBufferObjects = true;
		return this;
	}

	public boolean isDisableExtensionVertexBufferObjects() {
		return this.mDisableExtensionVertexBufferObjects;
	}

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
