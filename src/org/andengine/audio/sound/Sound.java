package org.andengine.audio.sound;

import org.andengine.audio.BaseAudioEntity;
import org.andengine.audio.sound.exception.SoundReleasedException;

import android.media.SoundPool;

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

	private int mSoundID;
	private int mStreamID;

	private boolean mLoaded;

	private int mLoopCount;
	private float mRate = 1.0f;
	private int mPriority = 1;

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

	public int getSoundID() {
		return this.mSoundID;
	}

	public int getStreamID() {
		return this.mStreamID;
	}

	public boolean isLoaded() {
		return this.mLoaded;
	}

	public void setLoaded(final boolean pLoaded) {
		this.mLoaded = pLoaded;
	}

	public void setLoopCount(final int pLoopCount) throws SoundReleasedException {
		this.assertNotReleased();

		this.mLoopCount = pLoopCount;
		if (this.mStreamID != 0) {
			this.getSoundPool().setLoop(this.mStreamID, pLoopCount);
		}
	}

	public float getRate() {
		return this.mRate;
	}

	public void setRate(final float pRate) throws SoundReleasedException {
		this.assertNotReleased();

		this.mRate = pRate;
		if (this.mStreamID != 0) {
			this.getSoundPool().setRate(this.mStreamID, pRate);
		}
	}

	public int getPriority() {
		return this.mPriority;
	}

	public void setPriority(final int pPriority) throws SoundReleasedException {
		this.assertNotReleased();

		this.mPriority = pPriority;
		if(this.mStreamID != 0) {
			this.getSoundPool().setPriority(this.mStreamID, pPriority);
		}
	}

	private SoundPool getSoundPool() throws SoundReleasedException {
		return this.getAudioManager().getSoundPool();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected SoundManager getAudioManager() throws SoundReleasedException {
		return (SoundManager) super.getAudioManager();
	}

	@Override
	protected void throwOnReleased() throws SoundReleasedException {
		throw new SoundReleasedException();
	}

	@Override
	public void play() throws SoundReleasedException {
		super.play();

		final float masterVolume = this.getMasterVolume();
		final float leftVolume = this.mLeftVolume * masterVolume;
		final float rightVolume = this.mRightVolume * masterVolume;

		this.mStreamID = this.getSoundPool().play(this.mSoundID, leftVolume, rightVolume, this.mPriority, this.mLoopCount, this.mRate);
	}

	@Override
	public void stop() throws SoundReleasedException {
		super.stop();

		if (this.mStreamID != 0) {
			this.getSoundPool().stop(this.mStreamID);
		}
	}

	@Override
	public void resume() throws SoundReleasedException {
		super.resume();

		if (this.mStreamID != 0) {
			this.getSoundPool().resume(this.mStreamID);
		}
	}

	@Override
	public void pause() throws SoundReleasedException {
		super.pause();

		if (this.mStreamID != 0) {
			this.getSoundPool().pause(this.mStreamID);
		}
	}

	@Override
	public void release() throws SoundReleasedException {
		this.assertNotReleased();

		this.getSoundPool().unload(this.mSoundID);
		this.mSoundID = 0;
		this.mLoaded = false;

		this.getAudioManager().remove(this);

		super.release();
	}

	@Override
	public void setLooping(final boolean pLooping) throws SoundReleasedException {
		super.setLooping(pLooping);

		this.setLoopCount((pLooping) ? -1 : 0);
	}

	@Override
	public void setVolume(final float pLeftVolume, final float pRightVolume) throws SoundReleasedException {
		super.setVolume(pLeftVolume, pRightVolume);

		if (this.mStreamID != 0) {
			final float masterVolume = this.getMasterVolume();
			final float leftVolume = this.mLeftVolume * masterVolume;
			final float rightVolume = this.mRightVolume * masterVolume;

			this.getSoundPool().setVolume(this.mStreamID, leftVolume, rightVolume);
		}
	}

	@Override
	public void onMasterVolumeChanged(final float pMasterVolume) throws SoundReleasedException {
		this.setVolume(this.mLeftVolume, this.mRightVolume);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
