package org.anddev.andengine.audio.sound;

import java.util.ArrayList;

import android.media.AudioManager;
import android.media.SoundPool;

/**
 * @author Nicolas Gramlich
 * @since 13:22:59 - 11.03.2010
 */
public class SoundManager {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int DEFAULT_MAX_SIMULTANEOUS_STREAMS = 2;

	// ===========================================================
	// Fields
	// ===========================================================
	
	private final SoundPool mSoundPool;
	private final ArrayList<Sound> mSounds = new ArrayList<Sound>();
	private float mMasterVolume = 1.0f;

	// ===========================================================
	// Constructors
	// ===========================================================

	public SoundManager() {
		this(DEFAULT_MAX_SIMULTANEOUS_STREAMS);
	}
	
	public SoundManager(final int pMaxSimultaneousStreams) {
		 this.mSoundPool = new SoundPool(pMaxSimultaneousStreams, AudioManager.STREAM_MUSIC, 0);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	SoundPool getSoundPool() {
		return this.mSoundPool;
	}

	public float getMasterVolume() {
		return this.mMasterVolume;
	}
	
	public void setMasterVolume(final float pMasterVolume) {
		this.mMasterVolume = pMasterVolume;
		final ArrayList<Sound> sounds = this.mSounds;
		for(int i = sounds.size() - 1; i >= 0; i--)
			sounds.get(i).onMasterVolumeChanged();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	
	public void releaseAll() {
		final ArrayList<Sound> sounds = this.mSounds;
		for(int i = sounds.size() - 1; i >= 0; i--)
			sounds.get(i).stop();
		
		this.mSoundPool.release();
	}

	void addSound(final Sound pSound) {
		this.mSounds.add(pSound);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
