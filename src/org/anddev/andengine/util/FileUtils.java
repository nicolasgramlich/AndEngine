package org.anddev.andengine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
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

	public static void copyFile(final File pIn, final File pOut) throws IOException {
		final FileInputStream fis = new FileInputStream(pIn);
		final FileOutputStream fos = new FileOutputStream(pOut);
		try {
			StreamUtils.copy(fis, fos);
		} finally {
			StreamUtils.closeStream(fis);
			StreamUtils.closeStream(fos);
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
				final boolean success = deleteDirectory(new File(pFileOrDirectory, children[i]));
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
