package org.anddev.andengine.audio.sound;

import org.anddev.andengine.audio.BaseAudioEntity;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:22:15 - 11.03.2010
 */
public class Sound extends BaseAudioEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mSoundID;
	private int mStreamID = 0;

	private int mLoopCount = 0;
	private float mRate = 1.0f;

	// ===========================================================
	// Constructors
	// ===========================================================

	Sound(final SoundManager pSoundManager, final int pSoundID) {
		super(pSoundManager);
		this.mSoundID = pSoundID;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setLoopCount(final int pLoopCount) {
		this.mLoopCount = pLoopCount;
		if(this.mStreamID != 0) {
			this.getAudioManager().getSoundPool().setLoop(this.mStreamID, pLoopCount);
		}
	}

	public void setRate(final float pRate) {
		this.mRate  = pRate;
		if(this.mStreamID != 0) {
			this.getAudioManager().getSoundPool().setRate(this.mStreamID, pRate);
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	@Override
	protected SoundManager getAudioManager() {
		return (SoundManager)super.getAudioManager();
	}

	@Override
	public void play() {
		final float masterVolume = this.getMasterVolume();
		final float leftVolume = this.mLeftVolume * masterVolume;
		final float rightVolume = this.mRightVolume * masterVolume;
		this.mStreamID = this.getAudioManager().getSoundPool().play(this.mSoundID, leftVolume, rightVolume, 1, this.mLoopCount, this.mRate);
	}

	@Override
	public void stop() {
		if(this.mStreamID != 0) {
			this.getAudioManager().getSoundPool().stop(this.mStreamID);
		}
	}

	@Override
	public void resume() {
		if(this.mStreamID != 0) {
			this.getAudioManager().getSoundPool().resume(this.mStreamID);
		}
	}

	@Override
	public void pause() {
		if(this.mStreamID != 0) {
			this.getAudioManager().getSoundPool().pause(this.mStreamID);
		}
	}

	@Override
	public void release() {

	}

	@Override
	public void setLooping(final boolean pLooping) {
		this.setLoopCount((pLooping) ? -1 : 0);
	}

	@Override
	public void setVolume(final float pLeftVolume, final float pRightVolume) {
		super.setVolume(pLeftVolume, pRightVolume);
		if(this.mStreamID != 0){
			final float masterVolume = this.getMasterVolume();
			final float leftVolume = this.mLeftVolume * masterVolume;
			final float rightVolume = this.mRightVolume * masterVolume;

			this.getAudioManager().getSoundPool().setVolume(this.mStreamID, leftVolume, rightVolume);
		}
	}

	@Override
	public void onMasterVolumeChanged(final float pMasterVolume) {
		this.setVolume(this.mLeftVolume, this.mRightVolume);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
