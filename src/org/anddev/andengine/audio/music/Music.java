package org.anddev.andengine.audio.music;

import org.anddev.andengine.audio.BaseAudioEntity;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:53:12 - 13.06.2010
 */
public class Music extends BaseAudioEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final MediaPlayer mMediaPlayer;

	// ===========================================================
	// Constructors
	// ===========================================================

	Music(final MusicManager pMusicManager, final MediaPlayer pMediaPlayer) {
		super(pMusicManager);
		this.mMediaPlayer = pMediaPlayer;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public boolean isPlaying() {
		return this.mMediaPlayer.isPlaying();
	}

	public MediaPlayer getMediaPlayer() {
		return this.mMediaPlayer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected MusicManager getAudioManager() {
		return (MusicManager)super.getAudioManager();
	}

	@Override
	public void play() {
		this.mMediaPlayer.start();
	}

	@Override
	public void stop() {
		this.mMediaPlayer.stop();
	}

	@Override
	public void resume() {
		this.mMediaPlayer.start();
	}

	@Override
	public void pause() {
		this.mMediaPlayer.pause();
	}

	@Override
	public void release() {
		this.mMediaPlayer.release();
	}

	@Override
	public void setLooping(final boolean pLooping) {
		this.mMediaPlayer.setLooping(pLooping);
	}

	@Override
	public void setVolume(final float pLeftVolume, final float pRightVolume) {
		super.setVolume(pLeftVolume, pRightVolume);

		final float masterVolume = this.getAudioManager().getMasterVolume();
		final float actualLeftVolume = pLeftVolume * masterVolume;
		final float actualRightVolume = pRightVolume * masterVolume;

		this.mMediaPlayer.setVolume(actualLeftVolume, actualRightVolume);
	}

	@Override
	public void onMasterVolumeChanged(final float pMasterVolume) {
		this.setVolume(this.mLeftVolume, this.mRightVolume);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void seekTo(final int pMilliseconds) {
		this.mMediaPlayer.seekTo(pMilliseconds);
	}

	public void setOnCompletionListener(final OnCompletionListener pOnCompletionListener) {
		this.mMediaPlayer.setOnCompletionListener(pOnCompletionListener);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
