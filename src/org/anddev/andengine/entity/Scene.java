package org.anddev.andengine.entity;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.input.touch.IOnAreaTouchListener;
import org.anddev.andengine.input.touch.IOnSceneTouchListener;
import org.anddev.andengine.input.touch.ITouchArea;

import android.view.MotionEvent;

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

	private final ArrayList<ITouchArea> mTouchAreas = new ArrayList<ITouchArea>();

	private float mRed;
	private float mGreen;
	private float mBlue;

	private final UpdateHandlerList mPreFrameHandlers = new UpdateHandlerList();
	private final UpdateHandlerList mPostFrameHandlers = new UpdateHandlerList();

	private IOnSceneTouchListener mOnSceneTouchListener;

	private IOnAreaTouchListener mOnAreaTouchListener;

	private boolean mBackgroundEnabled = true;

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

	public int getLayerCount() {
		return this.mLayers.length;
	}

	public Layer getBottomLayer() {
		return this.mLayers[0];
	}

	public Layer getTopLayer() {
		return this.mLayers[this.mLayers.length - 1];
	}

	public boolean isBackgroundEnabled() {
		return this.mBackgroundEnabled;
	}

	public void setBackgroundEnabled(final boolean pEnabled) {
		this.mBackgroundEnabled  = pEnabled;
	}

	public void setBackgroundColor(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
	}

	public void clearTouchAreas() {
		this.mTouchAreas.clear();
	}

	public void clearPreFrameHandlers() {
		this.mPreFrameHandlers.clear();
	}

	public void clearPostFrameHandlers() {
		this.mPostFrameHandlers.clear();
	}

	public void registerTouchArea(final ITouchArea pTouchArea) {
		this.mTouchAreas.add(pTouchArea);
	}

	public void registerPreFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPreFrameHandlers.add(pUpdateHandler);
	}

	public void registerPostFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPostFrameHandlers.add(pUpdateHandler);
	}

	public void unregisterTouchArea(final ITouchArea pTouchArea) {
		this.mTouchAreas.remove(pTouchArea);
	}

	public void unregisterPreFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPreFrameHandlers.remove(pUpdateHandler);
	}

	public void unregisterPostFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPostFrameHandlers.remove(pUpdateHandler);
	}

	public void setOnSceneTouchListener(final IOnSceneTouchListener pOnSceneTouchListener) {
		this.mOnSceneTouchListener = pOnSceneTouchListener;
	}

	public IOnSceneTouchListener getOnSceneTouchListener() {
		return this.mOnSceneTouchListener;
	}

	public boolean hasOnSceneTouchListener() {
		return this.mOnSceneTouchListener != null;
	}

	public void setOnAreaTouchListener(final IOnAreaTouchListener pOnAreaTouchListener) {
		this.mOnAreaTouchListener = pOnAreaTouchListener;
	}

	public IOnAreaTouchListener getOnAreaTouchListener() {
		return this.mOnAreaTouchListener;
	}

	public boolean hasOnAreaTouchListener() {
		return this.mOnAreaTouchListener != null;
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

	public boolean onSceneTouchEvent(final MotionEvent pSceneMotionEvent) {
		if(this.mOnAreaTouchListener != null) {
			final ArrayList<ITouchArea> touchAreas = this.mTouchAreas;
			final int touchAreaCount = touchAreas.size();
			if(touchAreaCount > 0) {
				for(int i = 0; i < touchAreaCount; i++) {
					final ITouchArea touchArea = touchAreas.get(i);
					if(touchArea.contains(pSceneMotionEvent.getX(), pSceneMotionEvent.getY())) {
						return this.mOnAreaTouchListener.onAreaTouched(touchArea, pSceneMotionEvent);
					}
				}
			}
		}
		/* If no area was touched, the Scene itself was touched as a fallback. */
		if(this.mOnSceneTouchListener != null){
			return this.mOnSceneTouchListener.onSceneTouchEvent(this, pSceneMotionEvent);
		} else {
			return false;
		}
	}

	@Override
	public void reset() {
		super.reset();

		final Layer[] layers = this.mLayers;
		for(int i = layers.length - 1; i >= 0; i--) {
			layers[i].reset();
		}
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
		if(this.mBackgroundEnabled) {
			pGL.glClearColor(this.mRed, this.mGreen, this.mBlue, 1.0f);
			pGL.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
	}

	private void drawLayers(final GL10 pGL) {
		final Layer[] layers = this.mLayers;
		final int layerCount = layers.length;
		for(int i = 0; i < layerCount; i++) {
			layers[i].onDraw(pGL);
		}
	}

	public void updatePreFrameHandlers(final float pSecondsElapsed) {
		this.mPreFrameHandlers.onUpdate(pSecondsElapsed);
	}

	public void updatePostFrameHandlers(final float pSecondsElapsed) {
		this.mPostFrameHandlers.onUpdate(pSecondsElapsed);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
