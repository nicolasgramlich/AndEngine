package org.andengine.audio.music;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import org.andengine.util.StreamUtils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:05:49 - 13.06.2010
 */
public final class MusicFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static String sAssetBasePath = "";

	// ===========================================================
	// Constructors
	// ===========================================================

	private MusicFactory() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @param pAssetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
	 */
	public static void setAssetBasePath(final String pAssetBasePath) {
		if (pAssetBasePath.endsWith("/") || pAssetBasePath.length() == 0) {
			MusicFactory.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalStateException("pAssetBasePath must end with '/' or be length zero.");
		}
	}

	public static String getAssetBasePath() {
		return MusicFactory.sAssetBasePath;
	}

	public static void onCreate() {
		MusicFactory.setAssetBasePath("");
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static Music createMusicFromFile(final MusicManager pMusicManager, final File pFile) throws IOException {
		final MediaPlayer mediaPlayer = new MediaPlayer();

		final FileInputStream fileInputStream = new FileInputStream(pFile);
		final FileDescriptor fileDescriptor = fileInputStream.getFD();
		StreamUtils.close(fileInputStream);
		mediaPlayer.setDataSource(fileDescriptor);
		mediaPlayer.prepare();

		final Music music = new Music(pMusicManager, mediaPlayer);
		pMusicManager.add(music);

		return music;
	}

	public static Music createMusicFromAsset(final MusicManager pMusicManager, final Context pContext, final String pAssetPath) throws IOException {
		final MediaPlayer mediaPlayer = new MediaPlayer();

		final AssetFileDescriptor assetFileDescritor = pContext.getAssets().openFd(MusicFactory.sAssetBasePath + pAssetPath);
		mediaPlayer.setDataSource(assetFileDescritor.getFileDescriptor(), assetFileDescritor.getStartOffset(), assetFileDescritor.getLength());
		mediaPlayer.prepare();

		final Music music = new Music(pMusicManager, mediaPlayer);
		pMusicManager.add(music);

		return music;
	}

	public static Music createMusicFromResource(final MusicManager pMusicManager, final Context pContext, final int pMusicResID) throws IOException {
		final MediaPlayer mediaPlayer = MediaPlayer.create(pContext, pMusicResID);
		mediaPlayer.prepare();

		final Music music = new Music(pMusicManager, mediaPlayer);
		pMusicManager.add(music);

		return music;
	}

	public static Music createMusicFromAssetFileDescriptor(final MusicManager pMusicManager, final AssetFileDescriptor pAssetFileDescriptor) throws IOException {
		final MediaPlayer mediaPlayer = new MediaPlayer();

		mediaPlayer.setDataSource(pAssetFileDescriptor.getFileDescriptor(), pAssetFileDescriptor.getStartOffset(), pAssetFileDescriptor.getLength());
		mediaPlayer.prepare();

		final Music music = new Music(pMusicManager, mediaPlayer);
		pMusicManager.add(music);

		return music;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
