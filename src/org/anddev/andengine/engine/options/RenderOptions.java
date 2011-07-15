package org.anddev.andengine.engine.options;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
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
	private boolean mDisableExtensionDrawTexture = false;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * <u><b>Default:</b></u> <code>false</code>
	 */
	public boolean isDisableExtensionVertexBufferObjects() {
		return this.mDisableExtensionVertexBufferObjects;
	}

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
	public boolean isDisableExtensionDrawTexture() {
		return this.mDisableExtensionDrawTexture;
	}

	public RenderOptions enableExtensionDrawTexture() {
		return this.setDisableExtensionDrawTexture(false);
	}

	public RenderOptions disableExtensionDrawTexture() {
		return this.setDisableExtensionDrawTexture(true);
	}

	public RenderOptions setDisableExtensionDrawTexture(final boolean pDisableExtensionDrawTexture) {
		this.mDisableExtensionDrawTexture = pDisableExtensionDrawTexture;
		return this;
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
