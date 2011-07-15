package org.anddev.andengine.audio;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:35:37 - 13.06.2010
 */
public abstract class BaseAudioEntity implements IAudioEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final IAudioManager<? extends IAudioEntity> mAudioManager;

	protected float mLeftVolume = 1.0f;
	protected float mRightVolume = 1.0f;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseAudioEntity(final IAudioManager<? extends IAudioEntity> pAudioManager) {
		this.mAudioManager = pAudioManager;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	protected IAudioManager<? extends IAudioEntity> getAudioManager() {
		return this.mAudioManager;
	}

	public float getActualLeftVolume() {
		return this.mLeftVolume * this.getMasterVolume();
	}

	public float getActualRightVolume() {
		return this.mRightVolume * this.getMasterVolume();
	}

	protected float getMasterVolume() {
		return this.mAudioManager.getMasterVolume();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public float getVolume() {
		return (this.mLeftVolume + this.mRightVolume) * 0.5f;
	}

	@Override
	public float getLeftVolume() {
		return this.mLeftVolume;
	}

	@Override
	public float getRightVolume() {
		return this.mRightVolume;
	}

	@Override
	public final void setVolume(final float pVolume) {
		this.setVolume(pVolume, pVolume);
	}

	@Override
	public void setVolume(final float pLeftVolume, final float pRightVolume) {
		this.mLeftVolume = pLeftVolume;
		this.mRightVolume = pRightVolume;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
