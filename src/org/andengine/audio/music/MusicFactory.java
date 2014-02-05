package org.andengine.audio.music;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 15:05:49 - 13.06.2010
 */
public class MusicFactory {
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

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @param pAssetBasePath must end with '<code>/</code>' or have <code>.length() == 0</code>.
	 */
	public static void setAssetBasePath(final String pAssetBasePath) {
		if(pAssetBasePath.endsWith("/") || pAssetBasePath.length() == 0) {
			MusicFactory.sAssetBasePath = pAssetBasePath;
		} else {
			throw new IllegalStateException("pAssetBasePath must end with '/' or be lenght zero.");
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
		mediaPlayer.setDataSource(fileInputStream.getFD());
		fileInputStream.close();
		mediaPlayer.prepare();

		final Music music = new Music(pMusicManager, mediaPlayer);
		pMusicManager.add(music);

		return music;
	}

	public static Music createMusicFromAsset(final MusicManager pMusicManager, final Context pContext, final String pAssetPath) throws IOException {
		final MediaPlayer mediaPlayer = new MediaPlayer();

		final AssetFileDescriptor assetFileDescritor = pContext.getAssets().openFd(MusicFactory.sAssetBasePath + pAssetPath);
		mediaPlayer.setDataSource(assetFileDescritor.getFileDescriptor(), assetFileDescritor.getStartOffset(), assetFileDescritor.getLength());
		assetFileDescritor.close();
		mediaPlayer.prepare();

		final Music music = new Music(pMusicManager, mediaPlayer);
		pMusicManager.add(music);

		return music;
	}
	
	public static void createMusicFromAssetAsync(final MusicManager pMusicManager, final Context pContext, final String pAssetPath, final IMusicLoadedListener pListener) throws IOException {
		final MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(final MediaPlayer player, final int what, final int extra) {
				boolean managed = false;
				if (pListener != null) {
					managed = pListener.onError(player, what, extra);
				}
				return managed;
			}
		});
		
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
		    @Override
			public void onPrepared(final MediaPlayer player) {
		    	final Music music = new Music(pMusicManager, mediaPlayer);
				pMusicManager.add(music);
				if (pListener != null) {
					pListener.onMusicLoaded(music);
				}
		    }
		});

		final AssetFileDescriptor assetFileDescritor = pContext.getAssets().openFd(MusicFactory.sAssetBasePath + pAssetPath);
		if (assetFileDescritor != null) {
			mediaPlayer.setDataSource(assetFileDescritor.getFileDescriptor(), assetFileDescritor.getStartOffset(), assetFileDescritor.getLength());
			assetFileDescritor.close();
			mediaPlayer.prepareAsync();
		}
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
	public interface IMusicLoadedListener {
		/**
		 * @see {@link android.media.MediaPlayer.OnErrorListener#onError(MediaPlayer, int, int)}
		 */
		boolean onError(final MediaPlayer player, final int what, final int extra);
		void onMusicLoaded(final Music pMusic);
	}
}
