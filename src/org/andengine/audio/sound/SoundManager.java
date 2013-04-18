package org.andengine.audio.sound;

import org.andengine.audio.BaseAudioManager;
import org.andengine.audio.sound.exception.SoundException;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.SparseArray;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 13:22:59 - 11.03.2010
 */
public class SoundManager extends BaseAudioManager<Sound> implements OnLoadCompleteListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int SOUND_STATUS_OK = 0;

	public static final int MAX_SIMULTANEOUS_STREAMS_DEFAULT = 5;

	// ===========================================================
	// Fields
	// ===========================================================

	private final SoundPool mSoundPool;
	private final SparseArray<Sound> mSoundMap = new SparseArray<Sound>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public SoundManager() {
		this(MAX_SIMULTANEOUS_STREAMS_DEFAULT);
	}

	public SoundManager(final int pMaxSimultaneousStreams) {
		this.mSoundPool = new SoundPool(pMaxSimultaneousStreams, AudioManager.STREAM_MUSIC, 0);
		this.mSoundPool.setOnLoadCompleteListener(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	SoundPool getSoundPool() {
		return this.mSoundPool;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void add(final Sound pSound) {
		super.add(pSound);

		this.mSoundMap.put(pSound.getSoundID(), pSound);
	}

	@Override
	public boolean remove(final Sound pSound) {
		final boolean removed = super.remove(pSound);
		if (removed) {
			this.mSoundMap.remove(pSound.getSoundID());
		}
		return removed;

	}

	@Override
	public void releaseAll() {
		super.releaseAll();

		this.mSoundPool.release();
	}

	@Override
	public synchronized void onLoadComplete(final SoundPool pSoundPool, final int pSoundID, final int pStatus) {
		if (pStatus == SoundManager.SOUND_STATUS_OK) {
			final Sound sound = this.mSoundMap.get(pSoundID);
			if (sound == null) {
				throw new SoundException("Unexpected soundID: '" + pSoundID + "'.");
			} else {
				sound.setLoaded(true);
			}
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void onPause() {
		this.mSoundPool.autoPause();
	}

	public void onResume() {
		this.mSoundPool.autoResume();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
