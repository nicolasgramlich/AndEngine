package org.andengine.audio.music;

import org.andengine.audio.BaseAudioEntity;
import org.andengine.audio.music.exception.MusicReleasedException;

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

	private MediaPlayer mMediaPlayer;

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

	public boolean isPlaying() throws MusicReleasedException {
		this.assertNotReleased();

		return this.mMediaPlayer.isPlaying();
	}

	public MediaPlayer getMediaPlayer() throws MusicReleasedException {
		this.assertNotReleased();

		return this.mMediaPlayer;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected MusicManager getAudioManager() throws MusicReleasedException {
		return (MusicManager)super.getAudioManager();
	}

	@Override
	protected void throwOnReleased() throws MusicReleasedException {
		throw new MusicReleasedException();
	}

	@Override
	public void play() throws MusicReleasedException {
		super.play();

		this.mMediaPlayer.start();
	}

	@Override
	public void stop() throws MusicReleasedException {
		super.stop();

		this.mMediaPlayer.stop();
	}

	@Override
	public void resume() throws MusicReleasedException {
		super.resume();

		this.mMediaPlayer.start();
	}

	@Override
	public void pause() throws MusicReleasedException {
		super.pause();

		this.mMediaPlayer.pause();
	}

	@Override
	public void setLooping(final boolean pLooping) throws MusicReleasedException {
		super.setLooping(pLooping);

		this.mMediaPlayer.setLooping(pLooping);
	}

	@Override
	public void setVolume(final float pLeftVolume, final float pRightVolume) throws MusicReleasedException {
		super.setVolume(pLeftVolume, pRightVolume);

		final float masterVolume = this.getAudioManager().getMasterVolume();
		final float actualLeftVolume = pLeftVolume * masterVolume;
		final float actualRightVolume = pRightVolume * masterVolume;

		this.mMediaPlayer.setVolume(actualLeftVolume, actualRightVolume);
	}

	@Override
	public void onMasterVolumeChanged(final float pMasterVolume) throws MusicReleasedException {
		this.setVolume(this.mLeftVolume, this.mRightVolume);
	}

	@Override
	public void release() throws MusicReleasedException {
		this.assertNotReleased();

		this.mMediaPlayer.release();
		this.mMediaPlayer = null;

		this.getAudioManager().remove(this);

		super.release();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void seekTo(final int pMilliseconds) throws MusicReleasedException {
		this.assertNotReleased();

		this.mMediaPlayer.seekTo(pMilliseconds);
	}

	public void setOnCompletionListener(final OnCompletionListener pOnCompletionListener) throws MusicReleasedException {
		this.assertNotReleased();

		this.mMediaPlayer.setOnCompletionListener(pOnCompletionListener);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
