package org.andengine.entity.sprite;

import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

/**
 * Note: {@link ButtonSprite} needs to be registered as a {@link ITouchArea} to the {@link Scene} via {@link Scene#registerTouchArea(ITouchArea)}, otherwise it won't be clickable.
 * To make {@link ButtonSprite} function properly, you should consider setting {@link Scene#setTouchAreaBindingOnActionDownEnabled(boolean)} to <code>true</code>.
 *
 * (c) Zynga 2012
 *
 * @author Scott Kennedy <skennedy@zynga.com>
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:51:57 - 05.01.2012
 */
public class ButtonSprite extends TiledSprite {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mStateCount;
	private OnClickListener mOnClickListener;

	private boolean mEnabled = true;
	private State mState;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ButtonSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pNormalTextureRegion, pVertexBufferObjectManager, (OnClickListener) null);
	}

	public ButtonSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
		this(pX, pY, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion), pVertexBufferObjectManager, pOnClickListener);
	}

	public ButtonSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pVertexBufferObjectManager, (OnClickListener) null);
	}

	public ButtonSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
		this(pX, pY, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion, pPressedTextureRegion), pVertexBufferObjectManager, pOnClickListener);
	}

	public ButtonSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final ITextureRegion pDisabledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pNormalTextureRegion, pPressedTextureRegion, pDisabledTextureRegion, pVertexBufferObjectManager, (OnClickListener) null);
	}

	public ButtonSprite(final float pX, final float pY, final ITextureRegion pNormalTextureRegion, final ITextureRegion pPressedTextureRegion, final ITextureRegion pDisabledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
		this(pX, pY, new TiledTextureRegion(pNormalTextureRegion.getTexture(), pNormalTextureRegion, pPressedTextureRegion, pDisabledTextureRegion), pVertexBufferObjectManager, pOnClickListener);
	}

	public ButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		this(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager, (OnClickListener) null);
	}

	public ButtonSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, final OnClickListener pOnClickListener) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

		this.mOnClickListener = pOnClickListener;
		this.mStateCount = pTiledTextureRegion.getTileCount();

		switch(this.mStateCount) {
			case 1:
				Debug.w("No " + ITextureRegion.class.getSimpleName() + " supplied for " + State.class.getSimpleName() + "." + State.PRESSED + ".");
			case 2:
				Debug.w("No " + ITextureRegion.class.getSimpleName() + " supplied for " + State.class.getSimpleName() + "." + State.DISABLED + ".");
				break;
			case 3:
				break;
			default:
				throw new IllegalArgumentException("The supplied " + ITiledTextureRegion.class.getSimpleName() + " has an unexpected amount of states: '" + this.mStateCount + "'.");
		}

		this.changeState(State.NORMAL);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isEnabled() {
		return this.mEnabled;
	}

	public void setEnabled(final boolean pEnabled) {
		this.mEnabled = pEnabled;

		if(this.mEnabled && this.mState == State.DISABLED) {
			this.changeState(State.NORMAL);
		} else if(!this.mEnabled) {
			this.changeState(State.DISABLED);
		}
	}

	public boolean isPressed() {
		return this.mState == State.PRESSED;
	}

	public State getState() {
		return this.mState;
	}

	public void setOnClickListener(final OnClickListener pOnClickListener) {
		this.mOnClickListener = pOnClickListener;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if(!this.isEnabled()) {
			this.changeState(State.DISABLED);
		} else if(pSceneTouchEvent.isActionDown()) {
			this.changeState(State.PRESSED);
		} else if(pSceneTouchEvent.isActionCancel() || !this.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())) {
			this.changeState(State.NORMAL);
		} else if(pSceneTouchEvent.isActionUp() && this.mState == State.PRESSED) {
			this.changeState(State.NORMAL);

			if(this.mOnClickListener != null) {
				this.mOnClickListener.onClick(this, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		}

		return true;
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		if(!this.isVisible()) {
			return false;
		} else {
			return super.contains(pX, pY);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void changeState(final State pState) {
		if(pState == this.mState) {
			return;
		}

		this.mState = pState;

		final int stateTiledTextureRegionIndex = this.mState.getTiledTextureRegionIndex();
		if(stateTiledTextureRegionIndex >= this.mStateCount) {
			this.setCurrentTileIndex(0);
			Debug.w(this.getClass().getSimpleName() + " changed its " + State.class.getSimpleName() + " to " + pState.toString() + ", which doesn't have a " + ITextureRegion.class.getSimpleName() + " supplied. Applying default " + ITextureRegion.class.getSimpleName() + ".");
		} else {
			this.setCurrentTileIndex(stateTiledTextureRegionIndex);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public interface OnClickListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onClick(final ButtonSprite pButtonSprite, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
	}

	public static enum State {
		// ===========================================================
		// Elements
		// ===========================================================

		NORMAL(0),
		PRESSED(1),
		DISABLED(2);

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mTiledTextureRegionIndex;

		// ===========================================================
		// Constructors
		// ===========================================================

		private State(final int pTiledTextureRegionIndex) {
			this.mTiledTextureRegionIndex = pTiledTextureRegionIndex;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getTiledTextureRegionIndex() {
			return this.mTiledTextureRegionIndex;
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
}
