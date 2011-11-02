package org.anddev.andengine.util.debug;

import java.util.ArrayList;

import org.anddev.andengine.util.constants.Constants;

import android.os.SystemClock;

/**
 * TODO Support custom format passed dump or constructor.
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:52:42 - 02.11.2011
 */
public class DebugTimer implements Constants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private String mTag;
	private String mLabel;
	ArrayList<DebugTime> mDebugTimes = new ArrayList<DebugTime>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public DebugTimer(final String pLabel) {
		this(Constants.DEBUGTAG, pLabel);
	}

	public DebugTimer(final String pTag, final String pLabel) {
		this.mTag = pTag;
		this.mLabel = pLabel;
		this.split(null);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public String getTag() {
		return this.mTag;
	}

	public void setTag(final String pTag) {
		this.mTag = pTag;
	}

	public String getLabel() {
		return this.mLabel;
	}

	public void setLabel(final String pLabel) {
		this.mLabel = pLabel;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void reset() {
		this.mDebugTimes.clear();
		this.split();
	}

	public void split() {
		this.split(null);
	}

	public void split(final String pLabel) {
		final long now = SystemClock.elapsedRealtime();
		this.mDebugTimes.add(new DebugTime(now, pLabel));
	}

	public void dump() {
		Debug.d(this.mTag, this.mLabel + ": begin");
		
		final long first = this.mDebugTimes.get(0).getTime();
		long current = first;
		for (int i = 1; i < this.mDebugTimes.size(); i++) {
			current = this.mDebugTimes.get(i).getTime();
			final String label = this.mDebugTimes.get(i).getLabel();
			if(label != null) {
				final long prev = this.mDebugTimes.get(i - 1).getTime();
	
				Debug.d(this.mTag, this.mLabel + ":\t" + (current - prev) + " ms, " + label);
			}
		}
		Debug.d(this.mTag, this.mLabel + ": end, " + (current - first) + " ms");
	}
	
	public void dump(final String pFormat) {
		Debug.d(this.mTag, this.mLabel + ": begin");
		
		final long first = this.mDebugTimes.get(0).getTime();
		long current = first;
		for (int i = 1; i < this.mDebugTimes.size(); i++) {
			current = this.mDebugTimes.get(i).getTime();
			final String label = this.mDebugTimes.get(i).getLabel();
			if(label != null) {
				final long prev = this.mDebugTimes.get(i - 1).getTime();
				
				Debug.d(this.mTag, String.format(pFormat, this.mLabel, (current - prev), label));
			}
		}
		Debug.d(this.mTag, this.mLabel + ": end, " + (current - first) + " ms");
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class DebugTime {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final long mTime;
		private final String mLabel;

		// ===========================================================
		// Constructors
		// ===========================================================

		public DebugTime(final long pTime, final String pLabel) {
			this.mTime = pTime;
			this.mLabel = pLabel;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public long getTime() {
			return this.mTime;
		}

		public String getLabel() {
			return this.mLabel;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}