package org.anddev.andengine.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.MatchResult;

import android.os.Build;

/**
 * @author Nicolas Gramlich
 * @since 15:50:31 - 14.07.2010
 */
public class SystemUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String BOGOMIPS_PATTERN = "BogoMIPS[\\s]*:[\\s]*(\\d+\\.\\d+)[\\s]*\n";

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

	public static boolean isAndroidVersionOrHigher(final int pBuildVersionCode) {
		return Integer.parseInt(Build.VERSION.SDK) >= pBuildVersionCode;
	}

	public static float getBogoMips() throws IllegalStateException {
		InputStream in = null;
		try {
			final Process process = new ProcessBuilder(new String[] { "/system/bin/cat", "/proc/cpuinfo" }).start();

			in = process.getInputStream();
			final Scanner scanner = new Scanner(in);

			final boolean matchFound = scanner.findWithinHorizon(BOGOMIPS_PATTERN, 1000) != null;
			if(matchFound) {
				final MatchResult mr = scanner.match();

				if(mr.groupCount() > 0) {
					return Float.parseFloat(mr.group(1));
				} else {
					throw new IllegalStateException();
				}
			} else {
				throw new IllegalStateException();
			}
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		} finally {
			StreamUtils.closeStream(in);			
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
