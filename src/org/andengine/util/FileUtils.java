package org.andengine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 13:53:33 - 20.06.2010
 */
public class FileUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static void copyToExternalStorage(final Context pContext, final int pSourceResourceID, final String pFilename) throws FileNotFoundException {
		FileUtils.copyToExternalStorage(pContext, pContext.getResources().openRawResource(pSourceResourceID), pFilename);
	}

	public static void copyToInternalStorage(final Context pContext, final int pSourceResourceID, final String pFilename) throws FileNotFoundException {
		FileUtils.copyToInternalStorage(pContext, pContext.getResources().openRawResource(pSourceResourceID), pFilename);
	}

	public static void copyToExternalStorage(final Context pContext, final String pSourceAssetPath, final String pFilename) throws IOException {
		FileUtils.copyToExternalStorage(pContext, pContext.getAssets().open(pSourceAssetPath), pFilename);
	}

	public static void copyToInternalStorage(final Context pContext, final String pSourceAssetPath, final String pFilename) throws IOException {
		FileUtils.copyToInternalStorage(pContext, pContext.getAssets().open(pSourceAssetPath), pFilename);
	}

	private static void copyToInternalStorage(final Context pContext, final InputStream pInputStream, final String pFilename) throws FileNotFoundException {
		StreamUtils.copyAndClose(pInputStream, new FileOutputStream(new File(pContext.getFilesDir(), pFilename)));
	}

	public static void copyToExternalStorage(final InputStream pInputStream, final String pFilePath) throws FileNotFoundException {
		if (FileUtils.isExternalStorageWriteable()) {
			final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pFilePath);
			StreamUtils.copyAndClose(pInputStream, new FileOutputStream(absoluteFilePath));
		} else {
			throw new IllegalStateException("External Storage is not writeable.");
		}
	}

	public static void copyToExternalStorage(final Context pContext, final InputStream pInputStream, final String pFilePath) throws FileNotFoundException {
		if (FileUtils.isExternalStorageWriteable()) {
			final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pContext, pFilePath);
			StreamUtils.copyAndClose(pInputStream, new FileOutputStream(absoluteFilePath));
		} else {
			throw new IllegalStateException("External Storage is not writeable.");
		}
	}

	public static boolean isFileExistingOnExternalStorage(final String pFilePath) {
		if (FileUtils.isExternalStorageReadable()) {
			final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pFilePath);
			final File file = new File(absoluteFilePath);
			return file.exists() && file.isFile();
		} else {
			throw new IllegalStateException("External Storage is not readable.");
		}
	}

	public static boolean isFileExistingOnExternalStorage(final Context pContext, final String pFilePath) {
		if (FileUtils.isExternalStorageReadable()) {
			final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pContext, pFilePath);
			final File file = new File(absoluteFilePath);
			return file.exists() && file.isFile();
		} else {
			throw new IllegalStateException("External Storage is not readable.");
		}
	}

	public static boolean isDirectoryExistingOnExternalStorage(final Context pContext, final String pDirectory) {
		if (FileUtils.isExternalStorageReadable()) {
			final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pContext, pDirectory);
			final File file = new File(absoluteFilePath);
			return file.exists() && file.isDirectory();
		} else {
			throw new IllegalStateException("External Storage is not readable.");
		}
	}

	public static boolean ensureDirectoriesExistOnExternalStorage(final Context pContext, final String pDirectory) {
		if (FileUtils.isDirectoryExistingOnExternalStorage(pContext, pDirectory)) {
			return true;
		}

		if (FileUtils.isExternalStorageWriteable()) {
			final String absoluteDirectoryPath = FileUtils.getAbsolutePathOnExternalStorage(pContext, pDirectory);
			return new File(absoluteDirectoryPath).mkdirs();
		} else {
			throw new IllegalStateException("External Storage is not writeable.");
		}
	}

	public static InputStream openOnExternalStorage(final String pFilePath) throws FileNotFoundException {
		final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pFilePath);
		return new FileInputStream(absoluteFilePath);
	}

	public static InputStream openOnExternalStorage(final Context pContext, final String pFilePath) throws FileNotFoundException {
		final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pContext, pFilePath);
		return new FileInputStream(absoluteFilePath);
	}

	public static String[] getDirectoryListOnExternalStorage(final Context pContext, final String pFilePath) throws FileNotFoundException {
		final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pContext, pFilePath);
		return new File(absoluteFilePath).list();
	}

	public static String[] getDirectoryListOnExternalStorage(final Context pContext, final String pFilePath, final FilenameFilter pFilenameFilter) throws FileNotFoundException {
		final String absoluteFilePath = FileUtils.getAbsolutePathOnExternalStorage(pContext, pFilePath);
		return new File(absoluteFilePath).list(pFilenameFilter);
	}

	public static String getAbsolutePathOnInternalStorage(final Context pContext, final String pFilePath) {
		return pContext.getFilesDir().getAbsolutePath() + pFilePath;
	}

	public static String getAbsolutePathOnExternalStorage(final String pFilePath) {
		return Environment.getExternalStorageDirectory() + "/" + pFilePath;
	}

	public static String getAbsolutePathOnExternalStorage(final Context pContext, final String pFilePath) {
		return Environment.getExternalStorageDirectory() + "/Android/data/" + pContext.getApplicationInfo().packageName + "/files/" + pFilePath;
	}

	public static boolean isExternalStorageWriteable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static boolean isExternalStorageReadable() {
		final String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
	}

	public static void copyFile(final File pSourceFile, final File pDestinationFile) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(pSourceFile);
			out = new FileOutputStream(pDestinationFile);
			StreamUtils.copy(in, out);
		} finally {
			StreamUtils.close(in);
			StreamUtils.close(out);
		}
	}

	/**
	 * Deletes all files and sub-directories under <code>dir</code>. Returns
	 * true if all deletions were successful. If a deletion fails, the method
	 * stops attempting to delete and returns false.
	 * 
	 * @param pFileOrDirectory
	 * @return
	 */
	public static boolean deleteDirectory(final File pFileOrDirectory) {
		if(pFileOrDirectory.isDirectory()) {
			final String[] children = pFileOrDirectory.list();
			final int childrenCount = children.length;
			for(int i = 0; i < childrenCount; i++) {
				final boolean success = FileUtils.deleteDirectory(new File(pFileOrDirectory, children[i]));
				if(!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return pFileOrDirectory.delete();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
