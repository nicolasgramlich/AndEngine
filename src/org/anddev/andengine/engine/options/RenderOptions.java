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

	public RenderOptions enableExtensionVertexBufferObjects() {
		return this.setDisableExtensionVertexBufferObjects(false);
	}

	public RenderOptions disableExtensionVertexBufferObjects() {
		return this.setDisableExtensionVertexBufferObjects(true);
	}

	public RenderOptions setDisableExtensionVertexBufferObjects(final boolean pDisableExtensionVertexBufferObjects) {
		this.mDisableExtensionVertexBufferObjects = pDisableExtensionVertexBufferObjects;
		return this;
	}

	/**
	 * <u><b>Default:</b></u> <code>false</code>
	 */
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
