package org.andengine.audio;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 14:53:29 - 13.06.2010
 */
public interface IAudioEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void play();
	public void pause();
	public void resume();
	public void stop();

	public float getVolume();
	public void setVolume(final float pVolume);

	public float getLeftVolume();
	public float getRightVolume();
	public void setVolume(final float pLeftVolume, final float pRightVolume);

	public void onMasterVolumeChanged(final float pMasterVolume);

	public void setLooping(final boolean pLooping);

	public void release();
}
