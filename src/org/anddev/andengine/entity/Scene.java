package org.anddev.andengine.entity;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author Nicolas Gramlich
 * @since 12:47:39 - 08.03.2010
 */
public class Scene extends BaseEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Layer[] mLayers;

	private float mRed;
	private float mGreen;
	private float mBlue;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Scene(final int pLayerCount) {
		this.mLayers = new Layer[pLayerCount];
		this.createLayers();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Layer getLayer(final int pLayerIndex) throws ArrayIndexOutOfBoundsException {
		return this.mLayers[pLayerIndex];
	}

	public void setBackgroundColor(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
	}

	public int getLayerCount() {
		return this.mLayers.length;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		this.drawBackground(pGL);
		this.drawLayers(pGL);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		this.updateLayers(pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void createLayers() {
		final Layer[] layers = this.mLayers;
		for(int i = layers.length - 1; i >= 0; i--) {
			layers[i] = new Layer();
		}
	}

	private void updateLayers(final float pSecondsElapsed) {
		final Layer[] layers = this.mLayers;
		final int layerCount = layers.length;
		for(int i = 0; i < layerCount; i++) {
			layers[i].onUpdate(pSecondsElapsed);
		}
	}

	protected void drawBackground(final GL10 pGL) {
		pGL.glClearColor(this.mRed, this.mGreen, this.mBlue, 1.0f);
		pGL.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}

	private void drawLayers(final GL10 pGL) {
		final Layer[] layers = this.mLayers;
		final int layerCount = layers.length;
		for(int i = 0; i < layerCount; i++) {
			layers[i].onDraw(pGL);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
