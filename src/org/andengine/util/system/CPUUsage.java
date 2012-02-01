package org.andengine.util.system;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.andengine.util.StreamUtils;
import org.andengine.util.TextUtils;
import org.andengine.util.debug.Debug;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 21:48:14 - 22.10.2011
 */
public class CPUUsage {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private long mTotal = 0;
	private long mIdle = 0;
	private float mUsage = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CPUUsage() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * @return in percent.
	 */
	public float getUsage() {
		return this.mUsage;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void update() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), StreamUtils.IO_BUFFER_SIZE);
			final String procStatString = reader.readLine();

			final String[] parts = TextUtils.SPLITPATTERN_SPACE.split(procStatString);

			final long user = Long.parseLong(parts[2]);
			final long nice = Long.parseLong(parts[3]);
			final long system = Long.parseLong(parts[4]);
			final long idle = Long.parseLong(parts[5]);

			final long total = user + nice + system;

			this.mUsage = 100.0f * (total - this.mTotal) / (total - this.mTotal + idle - this.mIdle);

			this.mTotal = total;
			this.mIdle = idle;
		} catch (final IOException e) {
			Debug.e(e);
		} finally {
			StreamUtils.close(reader);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}