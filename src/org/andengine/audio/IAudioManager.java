package org.andengine.audio;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:02:06 - 13.06.2010
 */
public interface IAudioManager<T extends IAudioEntity> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public float getMasterVolume();
	public void setMasterVolume(final float pMasterVolume);

	public void add(final T pAudioEntity);
	public boolean remove(final T pAudioEntity);

	public void releaseAll();
}
