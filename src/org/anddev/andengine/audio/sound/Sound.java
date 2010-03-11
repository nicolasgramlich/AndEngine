package org.anddev.andengine.audio.sound;

/**
 * @author Nicolas Gramlich
 * @since 13:22:15 - 11.03.2010
 */
public class Sound {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mSoundID;
	private final SoundManager mSoundManager;
	private float mLocalVolume = 1.0f;
	private int mStreamID = 0;
	private int mLoopCount = 0;
	private float mRate = 1.0f;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Sound(final SoundManager pSoundManager, final int pSoundID) {
		this.mSoundManager = pSoundManager;
		this.mSoundID = pSoundID;		
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void play() {
		final float actualVolume = this.getActualVolume();
		this.mStreamID = this.mSoundManager.getSoundPool().play(this.mSoundID, actualVolume, actualVolume, 0, this.mLoopCount, this.mRate);
	}
	
	public void pause() {
		if(this.mStreamID != 0)
			this.mSoundManager.getSoundPool().pause(this.mStreamID);
	}
	
	public void stop() {
		if(this.mStreamID != 0)
			this.mSoundManager.getSoundPool().stop(this.mStreamID);
	}
	
	public void resume() {
		if(this.mStreamID != 0)
			this.mSoundManager.getSoundPool().resume(this.mStreamID);
	}

	public void setLooping() {
		setLoopCount(-1);
	}
	
	public void setLoopCount(final int pLoopCount) {
		this.mLoopCount = pLoopCount;
		if(this.mStreamID != 0)
			this.mSoundManager.getSoundPool().setLoop(this.mStreamID, pLoopCount);
	}
	
	public void setRate(final float pRate) {
		this.mRate  = pRate;
		if(this.mStreamID != 0)
			this.mSoundManager.getSoundPool().setRate(this.mStreamID, pRate);
	}
	
	public void setVolume(final float pVolume) {
		this.mLocalVolume = pVolume;
		if(this.mStreamID != 0){
			final float actualVolume = this.getActualVolume();
			this.mSoundManager.getSoundPool().setVolume(this.mStreamID, actualVolume, actualVolume);
		}
	}	
	
	private float getActualVolume() {
		return this.mLocalVolume * this.mSoundManager.getMasterVolume();
	}
	
	void onMasterVolumeChanged() {
		this.setVolume(this.mLocalVolume);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
