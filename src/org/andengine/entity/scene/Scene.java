package org.andengine.entity.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.runnable.RunnableHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.ITouchArea.ITouchAreaMatcher;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.Constants;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.color.Color;

import android.R.string;
import android.hardware.Camera.Area;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.util.SparseArray;

/**
 * The Scene class is the root container for all objects to be drawn on the
 * screen. A Scene has a specific amount of layers, which themselves can contain
 * a (fixed or dynamic) amount of {@link Entity} objects. There are subclasses,
 * like the {@link CameraScene}, {@link HUD} and {@link MenuScene} that are
 * drawing themselves to the same position of the Scene no matter where the
 * camera is positioned to. <br>
 * (c) 2010 Nicolas Gramlich <br>
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:47:39 - 08.03.2010
 */
public class Scene extends Entity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int TOUCHAREAS_CAPACITY_DEFAULT = 4;

	// ===========================================================
	// Fields
	// ===========================================================

	private float mSecondsElapsedTotal;

	protected Scene mParentScene;
	protected Scene mChildScene;
	private boolean mChildSceneModalDraw;
	private boolean mChildSceneModalUpdate;
	private boolean mChildSceneModalTouch;

	protected SmartList<ITouchArea> mTouchAreas = new SmartList<ITouchArea>(
			Scene.TOUCHAREAS_CAPACITY_DEFAULT);

	private final RunnableHandler mRunnableHandler = new RunnableHandler();

	private IOnSceneTouchListener mOnSceneTouchListener;

	private IOnAreaTouchListener mOnAreaTouchListener;

	private IBackground mBackground = new Background(Color.BLACK);
	private boolean mBackgroundEnabled = true;

	private boolean mOnAreaTouchTraversalBackToFront = true;

	private boolean mTouchAreaBindingOnActionDownEnabled = false;
	private boolean mTouchAreaBindingOnActionMoveEnabled = false;
	private final SparseArray<ITouchArea> mTouchAreaBindings = new SparseArray<ITouchArea>();
	private boolean mOnSceneTouchListenerBindingOnActionDownEnabled = false;
	private final SparseArray<IOnSceneTouchListener> mOnSceneTouchListenerBindings = new SparseArray<IOnSceneTouchListener>();

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Creates an empty Scene
	 */
	public Scene() {

	}

	/**
	 * @deprecated Use {@link Scene#Scene()} constructor
	 */

	public Scene(final int pChildCount) {
		for (int i = 0; i < pChildCount; i++) {
			this.attachChild(new Entity());
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @return The number of seconds that have elapsed since this Scene started
	 *         updating
	 */
	public float getSecondsElapsedTotal() {
		return this.mSecondsElapsedTotal;
	}

	/**
	 * @return The {@link IBackground} that serves as the background for this
	 *         Scene
	 */
	public IBackground getBackground() {
		return this.mBackground;
	}

	/**
	 * @param pBackground
	 *            The {@link IBackground} that serves as the background for this
	 *            Scene
	 */
	public void setBackground(final IBackground pBackground) {
		this.mBackground = pBackground;
	}

	/**
	 * @return Whether this Scene show a background
	 */
	public boolean isBackgroundEnabled() {
		return this.mBackgroundEnabled;
	}

	/**
	 * @param pEnabled
	 *            <code>true</code> to show a background, <code>false</code>
	 *            otherwise
	 */
	public void setBackgroundEnabled(final boolean pEnabled) {
		this.mBackgroundEnabled = pEnabled;
	}

	/**
	 * @param pOnSceneTouchListener
	 *            An {@link IOnSceneTouchListener} that specifies what happens
	 *            when a touch event happens within the Scene
	 */
	public void setOnSceneTouchListener(
			final IOnSceneTouchListener pOnSceneTouchListener) {
		this.mOnSceneTouchListener = pOnSceneTouchListener;
	}

	/**
	 * @return The {@link IOnSceneTouchListener} that specifies what happens
	 *         when a touch event happens within the Scene. <code>null</code> if
	 *         there's none
	 * @see {@link #getOnAreaTouchListener()}
	 */
	public IOnSceneTouchListener getOnSceneTouchListener() {
		return this.mOnSceneTouchListener;
	}

	/**
	 * @return Whether there is an {@link IOnSceneTouchListener} that specifies
	 *         what happens when a touch event happens within the Scene.
	 * @see {@link #hasOnAreaTouchListener()}
	 */
	public boolean hasOnSceneTouchListener() {
		return this.mOnSceneTouchListener != null;
	}

	/**
	 * The {@link IOnAreaTouchListener} that specifies what happens when a touch
	 * event happens to any registered {@link Area} registered in this Scene.
	 * The {@link Area}s are registered by calling
	 * {@link #registerTouchArea(ITouchArea)}.
	 * 
	 * @param pOnAreaTouchListener
	 *            An {@link IOnAreaTouchListener}
	 * @see #setOnSceneTouchListener(IOnSceneTouchListener)
	 */
	public void setOnAreaTouchListener(
			final IOnAreaTouchListener pOnAreaTouchListener) {
		this.mOnAreaTouchListener = pOnAreaTouchListener;
	}

	/**
	 * @return The {@link IOnAreaTouchListener} that specifies what happens when
	 *         a touch event happens to any registered {@link Area} registered
	 *         in this Scene
	 */
	public IOnAreaTouchListener getOnAreaTouchListener() {
		return this.mOnAreaTouchListener;
	}

	/**
	 * 
	 * @return Whether this Scene has an {@link IOnAreaTouchListener} that
	 *         specifies what happens when a touch event happens to any
	 *         registered {@link Area} registered in this Scene
	 */
	public boolean hasOnAreaTouchListener() {
		return this.mOnAreaTouchListener != null;
	}

	/**
	 * @param pParentScene
	 *            The Scene that acts as a parent of the current Scene. An
	 *            example is a game world scene which is be the parent of a
	 *            {@link HUD}.
	 */
	private void setParentScene(final Scene pParentScene) {
		this.mParentScene = pParentScene;
	}

	/**
	 * @return Whether this Scene has a scene that acts as its child, like a
	 *         {@link HUD} to a game screen.
	 */
	public boolean hasChildScene() {
		return this.mChildScene != null;
	}

	/**
	 * 
	 * @return The scene that acts as the child of this one, like a {@link HUD}
	 *         to a game screen. <code>null</code> if there's none.
	 */
	public Scene getChildScene() {
		return this.mChildScene;
	}

	/**
	 * Sets the child scene with all the modal options to true. (Which means
	 * draw, update and touch events of the child will be passed to the parent.)
	 * 
	 * @param pChildScene
	 * @see #setChildScene(Scene)
	 * @see #setChildScene(Scene, boolean, boolean, boolean)
	 */
	public void setChildSceneModal(final Scene pChildScene) {
		this.setChildScene(pChildScene, true, true, true);
	}

	/**
	 * Sets the child scene with all the modal options to false. (Which means
	 * draw, update and touch events of the child will not be passed to the
	 * parent.)
	 * 
	 * @param pChildScene
	 * @see #setChildSceneModal(Scene)
	 * @see #setChildScene(Scene, boolean, boolean, boolean)
	 */
	public void setChildScene(final Scene pChildScene) {
		this.setChildScene(pChildScene, false, false, false);
	}

	/**
	 * Sets the child scene, given a Scene and booleans that correspond to modal
	 * options. The modal options are used to decide whether to pass events to
	 * the parent. For example: you don't want the game to proceed while a
	 * superimposed menu is open, but you do want to make it draw in the
	 * background.
	 * 
	 * @param pChildScene
	 *            The child scene
	 * @param pModalDraw
	 *            Whether the parent should be drawed
	 * @param pModalUpdate
	 *            Whether the parent should update its logic
	 * @param pModalTouch
	 *            Whether the parent should process touch events
	 * @see #setChildSceneModal(Scene)
	 * @see #setChildScene()
	 */
	public void setChildScene(final Scene pChildScene,
			final boolean pModalDraw, final boolean pModalUpdate,
			final boolean pModalTouch) {
		pChildScene.setParentScene(this);
		this.mChildScene = pChildScene;
		this.mChildSceneModalDraw = pModalDraw;
		this.mChildSceneModalUpdate = pModalUpdate;
		this.mChildSceneModalTouch = pModalTouch;
	}

	/**
	 * Removes child scene, equivalent to <code>setChildScene(null)</code>
	 * 
	 * @see #setChildScene(Scene)
	 */
	public void clearChildScene() {
		this.mChildScene = null;
	}

	/**
	 * Option to make sure that when the scene is touched, the area touch
	 * listeners that were registered last will be executed first. (This is the default.)
	 * @see #setOnAreaTouchTraversalFrontToBack()
	 */
	public void setOnAreaTouchTraversalBackToFront() {
		this.mOnAreaTouchTraversalBackToFront = true;
	}

	/**
	 * Option to make sure that when the scene is touched, the area touch
	 * listeners that were registered first will be executed first. (Default is back to front.)
	 * @see #setOnAreaTouchTraversalBackToFront()
	 */
	public void setOnAreaTouchTraversalFrontToBack() {
		this.mOnAreaTouchTraversalBackToFront = false;
	}

	public boolean isTouchAreaBindingOnActionDownEnabled() {
		return this.mTouchAreaBindingOnActionDownEnabled;
	}

	public boolean isTouchAreaBindingOnActionMoveEnabled() {
		return this.mTouchAreaBindingOnActionMoveEnabled;
	}

	/**
	 * Enable or disable the binding of TouchAreas to PointerIDs (fingers). When
	 * enabled: TouchAreas get bound to a PointerID (finger) when returning true
	 * in {@link IShape#onAreaTouched(TouchEvent, float, float)} or
	 * {@link IOnAreaTouchListener#onAreaTouched(TouchEvent, ITouchArea, float, float)}
	 * with {@link TouchEvent#ACTION_DOWN}, they will receive all subsequent
	 * {@link TouchEvent}s that are made with the same PointerID (finger)
	 * <b>even if the {@link TouchEvent} is outside of the actual
	 * {@link ITouchArea}</b>!
	 * 
	 * @param pTouchAreaBindingOnActionDownEnabled
	 */
	public void setTouchAreaBindingOnActionDownEnabled(
			final boolean pTouchAreaBindingOnActionDownEnabled) {
		if (this.mTouchAreaBindingOnActionDownEnabled
				&& !pTouchAreaBindingOnActionDownEnabled) {
			this.mTouchAreaBindings.clear();
		}
		this.mTouchAreaBindingOnActionDownEnabled = pTouchAreaBindingOnActionDownEnabled;
	}

	/**
	 * Enable or disable the binding of TouchAreas to PointerIDs (fingers). When
	 * enabled: TouchAreas get bound to a PointerID (finger) when returning true
	 * in {@link IShape#onAreaTouched(TouchEvent, float, float)} or
	 * {@link IOnAreaTouchListener#onAreaTouched(TouchEvent, ITouchArea, float, float)}
	 * with {@link TouchEvent#ACTION_MOVE}, they will receive all subsequent
	 * {@link TouchEvent}s that are made with the same PointerID (finger)
	 * <b>even if the {@link TouchEvent} is outside of the actual
	 * {@link ITouchArea}</b>!
	 * 
	 * @param pTouchAreaBindingOnActionMoveEnabled
	 */
	public void setTouchAreaBindingOnActionMoveEnabled(
			final boolean pTouchAreaBindingOnActionMoveEnabled) {
		if (this.mTouchAreaBindingOnActionMoveEnabled
				&& !pTouchAreaBindingOnActionMoveEnabled) {
			this.mTouchAreaBindings.clear();
		}
		this.mTouchAreaBindingOnActionMoveEnabled = pTouchAreaBindingOnActionMoveEnabled;
	}

	public boolean isOnSceneTouchListenerBindingOnActionDownEnabled() {
		return this.mOnSceneTouchListenerBindingOnActionDownEnabled;
	}

	/**
	 * Enable or disable the binding of TouchAreas to PointerIDs (fingers). When
	 * enabled: The OnSceneTouchListener gets bound to a PointerID (finger) when
	 * returning true in {@link Shape#onAreaTouched(TouchEvent, float, float)}
	 * or
	 * {@link IOnAreaTouchListener#onAreaTouched(TouchEvent, ITouchArea, float, float)}
	 * with {@link TouchEvent#ACTION_DOWN}, it will receive all subsequent
	 * {@link TouchEvent}s that are made with the same PointerID (finger)
	 * <b>even if the {@link TouchEvent} is would belong to an overlaying
	 * {@link ITouchArea}</b>!
	 * 
	 * @param pOnSceneTouchListenerBindingOnActionDownEnabled
	 */
	public void setOnSceneTouchListenerBindingOnActionDownEnabled(
			final boolean pOnSceneTouchListenerBindingOnActionDownEnabled) {
		if (this.mOnSceneTouchListenerBindingOnActionDownEnabled
				&& !pOnSceneTouchListenerBindingOnActionDownEnabled) {
			this.mOnSceneTouchListenerBindings.clear();
		}
		this.mOnSceneTouchListenerBindingOnActionDownEnabled = pOnSceneTouchListenerBindingOnActionDownEnabled;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		final Scene childScene = this.mChildScene;

		if (childScene == null || !this.mChildSceneModalDraw) {
			if (this.mBackgroundEnabled) {
				pGLState.pushProjectionGLMatrix();

				pCamera.onApplySceneBackgroundMatrix(pGLState);
				pGLState.loadModelViewGLMatrixIdentity();

				this.mBackground.onDraw(pGLState, pCamera);

				pGLState.popProjectionGLMatrix();
			}

			{
				pGLState.pushProjectionGLMatrix();

				this.onApplyMatrix(pGLState, pCamera);
				pGLState.loadModelViewGLMatrixIdentity();

				super.onManagedDraw(pGLState, pCamera);

				pGLState.popProjectionGLMatrix();
			}
		}

		if (childScene != null) {
			childScene.onDraw(pGLState, pCamera);
		}
	}

	protected void onApplyMatrix(final GLState pGLState, final Camera pCamera) {
		pCamera.onApplySceneMatrix(pGLState);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		this.mSecondsElapsedTotal += pSecondsElapsed;

		this.mRunnableHandler.onUpdate(pSecondsElapsed);

		final Scene childScene = this.mChildScene;
		if (childScene == null || !this.mChildSceneModalUpdate) {
			this.mBackground.onUpdate(pSecondsElapsed);
			super.onManagedUpdate(pSecondsElapsed);
		}

		if (childScene != null) {
			childScene.onUpdate(pSecondsElapsed);
		}
	}

	public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		final int action = pSceneTouchEvent.getAction();
		final boolean isActionDown = pSceneTouchEvent.isActionDown();
		final boolean isActionMove = pSceneTouchEvent.isActionMove();

		if (!isActionDown) {
			if (this.mOnSceneTouchListenerBindingOnActionDownEnabled) {
				final IOnSceneTouchListener boundOnSceneTouchListener = this.mOnSceneTouchListenerBindings
						.get(pSceneTouchEvent.getPointerID());
				if (boundOnSceneTouchListener != null) {
					/* Check if boundTouchArea needs to be removed. */
					switch (action) {
					case TouchEvent.ACTION_UP:
					case TouchEvent.ACTION_CANCEL:
						this.mOnSceneTouchListenerBindings
								.remove(pSceneTouchEvent.getPointerID());
					}
					final Boolean handled = this.mOnSceneTouchListener
							.onSceneTouchEvent(this, pSceneTouchEvent);
					if (handled != null && handled) {
						return true;
					}
				}
			}
			if (this.mTouchAreaBindingOnActionDownEnabled) {
				final SparseArray<ITouchArea> touchAreaBindings = this.mTouchAreaBindings;
				final ITouchArea boundTouchArea = touchAreaBindings
						.get(pSceneTouchEvent.getPointerID());
				/*
				 * In the case a ITouchArea has been bound to this PointerID,
				 * we'll pass this this TouchEvent to the same ITouchArea.
				 */
				if (boundTouchArea != null) {
					final float sceneTouchEventX = pSceneTouchEvent.getX();
					final float sceneTouchEventY = pSceneTouchEvent.getY();

					/* Check if boundTouchArea needs to be removed. */
					switch (action) {
					case TouchEvent.ACTION_UP:
					case TouchEvent.ACTION_CANCEL:
						touchAreaBindings.remove(pSceneTouchEvent
								.getPointerID());
					}
					final Boolean handled = this.onAreaTouchEvent(
							pSceneTouchEvent, sceneTouchEventX,
							sceneTouchEventY, boundTouchArea);
					if (handled != null && handled) {
						return true;
					}
				}
			}
		}

		final Scene childScene = this.mChildScene;
		if (childScene != null) {
			final boolean handledByChild = this
					.onChildSceneTouchEvent(pSceneTouchEvent);
			if (handledByChild) {
				return true;
			} else if (this.mChildSceneModalTouch) {
				return false;
			}
		}

		final float sceneTouchEventX = pSceneTouchEvent.getX();
		final float sceneTouchEventY = pSceneTouchEvent.getY();

		final SmartList<ITouchArea> touchAreas = this.mTouchAreas;
		if (touchAreas != null) {
			final int touchAreaCount = touchAreas.size();
			if (touchAreaCount > 0) {
				if (this.mOnAreaTouchTraversalBackToFront) { /* Back to Front. */
					for (int i = 0; i < touchAreaCount; i++) {
						final ITouchArea touchArea = touchAreas.get(i);
						if (touchArea.contains(sceneTouchEventX,
								sceneTouchEventY)) {
							final Boolean handled = this.onAreaTouchEvent(
									pSceneTouchEvent, sceneTouchEventX,
									sceneTouchEventY, touchArea);
							if (handled != null && handled) {
								/*
								 * If binding of ITouchAreas is enabled and this
								 * is an ACTION_DOWN event, bind this ITouchArea
								 * to the PointerID.
								 */
								if ((this.mTouchAreaBindingOnActionDownEnabled && isActionDown)
										|| (this.mTouchAreaBindingOnActionMoveEnabled && isActionMove)) {
									this.mTouchAreaBindings.put(
											pSceneTouchEvent.getPointerID(),
											touchArea);
								}
								return true;
							}
						}
					}
				} else { /* Front to back. */
					for (int i = touchAreaCount - 1; i >= 0; i--) {
						final ITouchArea touchArea = touchAreas.get(i);
						if (touchArea.contains(sceneTouchEventX,
								sceneTouchEventY)) {
							final Boolean handled = this.onAreaTouchEvent(
									pSceneTouchEvent, sceneTouchEventX,
									sceneTouchEventY, touchArea);
							if (handled != null && handled) {
								/*
								 * If binding of ITouchAreas is enabled and this
								 * is an ACTION_DOWN event, bind this ITouchArea
								 * to the PointerID.
								 */
								if ((this.mTouchAreaBindingOnActionDownEnabled && isActionDown)
										|| (this.mTouchAreaBindingOnActionMoveEnabled && isActionMove)) {
									this.mTouchAreaBindings.put(
											pSceneTouchEvent.getPointerID(),
											touchArea);
								}
								return true;
							}
						}
					}
				}
			}
		}
		/* If no area was touched, the Scene itself was touched as a fallback. */
		if (this.mOnSceneTouchListener != null) {
			final Boolean handled = this.mOnSceneTouchListener
					.onSceneTouchEvent(this, pSceneTouchEvent);
			if (handled != null && handled) {
				/*
				 * If binding of ITouchAreas is enabled and this is an
				 * ACTION_DOWN event, bind the active OnSceneTouchListener to
				 * the PointerID.
				 */
				if (this.mOnSceneTouchListenerBindingOnActionDownEnabled
						&& isActionDown) {
					this.mOnSceneTouchListenerBindings.put(
							pSceneTouchEvent.getPointerID(),
							this.mOnSceneTouchListener);
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private Boolean onAreaTouchEvent(final TouchEvent pSceneTouchEvent,
			final float sceneTouchEventX, final float sceneTouchEventY,
			final ITouchArea touchArea) {
		final float[] touchAreaLocalCoordinates = touchArea
				.convertSceneToLocalCoordinates(sceneTouchEventX,
						sceneTouchEventY);
		final float touchAreaLocalX = touchAreaLocalCoordinates[Constants.VERTEX_INDEX_X];
		final float touchAreaLocalY = touchAreaLocalCoordinates[Constants.VERTEX_INDEX_Y];

		final boolean handledSelf = touchArea.onAreaTouched(pSceneTouchEvent,
				touchAreaLocalX, touchAreaLocalY);
		if (handledSelf) {
			return Boolean.TRUE;
		} else if (this.mOnAreaTouchListener != null) {
			return this.mOnAreaTouchListener.onAreaTouched(pSceneTouchEvent,
					touchArea, touchAreaLocalX, touchAreaLocalY);
		} else {
			return null;
		}
	}

	protected boolean onChildSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		return this.mChildScene.onSceneTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void reset() {
		super.reset();

		this.clearChildScene();
	}

	@Override
	public void setParent(final IEntity pEntity) {
		// super.setParent(pEntity);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void postRunnable(final Runnable pRunnable) {
		this.mRunnableHandler.postRunnable(pRunnable);
	}

	/**
	 * @param pTouchArea
	 *            The {@link ITouchArea} that should be added to the list of
	 *            areas that respond to touch events
	 */
	public void registerTouchArea(final ITouchArea pTouchArea) {
		this.mTouchAreas.add(pTouchArea);
	}

	/**
	 * 
	 * @param pTouchArea
	 *            The {@link ITouchArea} that should be removed from the list of
	 *            areas that respond to touch events
	 * @return <code>true</code> if the list of touch areas was modified,
	 *         <code>false</code> otherwise
	 */
	public boolean unregisterTouchArea(final ITouchArea pTouchArea) {
		return this.mTouchAreas.remove(pTouchArea);
	}

	/**
	 * @param pTouchAreaMatcher
	 *            The {@link ITouchAreaMatcher} matches all the
	 *            {@link ITouchArea}s that should be removed from the list of
	 *            areas that respond to touch events
	 * @return <code>true</code> if the list of touch areas was modified,
	 *         <code>false</code> otherwise
	 */
	public boolean unregisterTouchAreas(
			final ITouchAreaMatcher pTouchAreaMatcher) {
		return this.mTouchAreas.removeAll(pTouchAreaMatcher);
	}

	/**
	 * Remove all areas that respond to touch events
	 */
	public void clearTouchAreas() {
		this.mTouchAreas.clear();
	}

	/**
	 * 
	 * @return A {@link SmartList} of {@link ITouchArea}s that contain all the touch areas that are associated with this scene
	 */
	public SmartList<ITouchArea> getTouchAreas() {
		return this.mTouchAreas;
	}

	/**
	 * If they exist, removes this scene from parent scene and also removes the child scene from this screen.
	 * @see #clearChildScene()
	 */
	public void back() {
		this.clearChildScene();

		if (this.mParentScene != null) {
			this.mParentScene.clearChildScene();
			this.mParentScene = null;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
