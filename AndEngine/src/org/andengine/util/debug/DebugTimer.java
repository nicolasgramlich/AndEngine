package org.andengine.util.debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import org.andengine.util.debug.Debug.DebugLevel;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:52:42 - 02.11.2011
 */
public class DebugTimer {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String SPLIT_STRING = "  Split: ";
	private static final int INDENT_SPACES = SPLIT_STRING.length();

	// ===========================================================
	// Fields
	// ===========================================================

	private final Stack<DebugTime> mDebugTimes = new Stack<DebugTime>();
	private final DebugLevel mDebugLevel;

	// ===========================================================
	// Constructors
	// ===========================================================

	public DebugTimer(final String pLabel) {
		this(DebugLevel.DEBUG, pLabel);
	}

	public DebugTimer(final DebugLevel pDebugLevel, final String pLabel) {
		this.mDebugLevel = pDebugLevel;
		this.init(pLabel);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	private void init(final String pLabel) {
		final long now = System.currentTimeMillis();
		final DebugTime debugTime = new DebugTime(now, pLabel);
		this.mDebugTimes.add(debugTime);
	}

	public void begin(final String pLabel) {
		final long now = System.currentTimeMillis();
		final DebugTime debugTime = new DebugTime(now, pLabel);
		this.mDebugTimes.peek().begin(debugTime);
		this.mDebugTimes.add(debugTime);
	}

	public void split(final String pLabel) {
		this.mDebugTimes.peek().split(pLabel);
	}

	public void end() {
		final long now = System.currentTimeMillis();
		if(this.mDebugTimes.size() == 1) {
			throw new IllegalStateException("Cannot end the root of this " + this.getClass().getSimpleName());
		} else {
			this.mDebugTimes.pop().end(now);
		}
	}

	public void dump() {
		this.dump(false);
	}

	public void dump(final boolean pClear) {
		final long now = System.currentTimeMillis();
		if(this.mDebugTimes.size() > 1) {
			Debug.w(this.getClass().getSimpleName() + " not all ended!");
		}

		final DebugTime root = this.mDebugTimes.firstElement();
		root.end(now);
		root.dump(0);

		if(pClear) {
			this.clear();
		}
	}

	public void clear() {
		final DebugTime root = this.mDebugTimes.firstElement();
		this.mDebugTimes.clear();
		this.init(root.mLabel);
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

		private final long mStartTime;
		private final String mLabel;
		private final boolean mSplit;

		private long mEndTime;
		private ArrayList<DebugTime> mChildren;
		private DebugTime mLastSplit;

		// ===========================================================
		// Constructors
		// ===========================================================

		public DebugTime(final long pStartTime, final String pLabel) {
			this(pStartTime, pLabel, false);
		}

		protected DebugTime(final long pStartTime, final String pLabel, final boolean pSplit) {
			this.mStartTime = pStartTime;
			this.mLabel = pLabel;
			this.mSplit = pSplit;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void begin(final DebugTime pDebugTime) {
			this.ensureChildrenAllocated();
			this.mChildren.add(pDebugTime);
		}

		public void split(final String pLabel) {
			final long now = System.currentTimeMillis();

			final DebugTime split;
			if(this.mLastSplit == null) {
				split = new DebugTime(this.mStartTime, pLabel, true);
			} else {
				split = new DebugTime(this.mLastSplit.mEndTime, pLabel, true);
			}
			split.end(now);

			this.ensureChildrenAllocated();
			this.mChildren.add(split);

			this.mLastSplit = split;
		}

		public void end(final long pEndTime) {
			this.mEndTime = pEndTime;
		}

		public void dump(final int pIndent) {
			this.dump(pIndent, "");
		}

		public void dump(final int pIndent, final String pPostfix) {
			if(this.mSplit) {
				final char[] indent = new char[(pIndent - 1) * INDENT_SPACES];
				Arrays.fill(indent, ' ');
				Debug.log(DebugTimer.this.mDebugLevel, new String(indent) + SPLIT_STRING + "'" + this.mLabel + "'" + " @( " + (this.mEndTime - this.mStartTime) + "ms )" + pPostfix);
			} else {
				final char[] indent = new char[pIndent * INDENT_SPACES];
				Arrays.fill(indent, ' ');
				if(this.mChildren == null) {
					Debug.log(DebugTimer.this.mDebugLevel, new String(indent) + "'" + this.mLabel + "' @( " + (this.mEndTime - this.mStartTime) + "ms )" + pPostfix);
				} else {
					final ArrayList<DebugTime> children = this.mChildren;
					final int childCount = children.size();
					
					Debug.log(DebugTimer.this.mDebugLevel, new String(indent) + "'" + this.mLabel + "' {");
					for(int i = 0; i < childCount - 1; i++) {
						children.get(i).dump(pIndent + 1, ",");
					}
					children.get(childCount - 1).dump(pIndent + 1);
					Debug.log(DebugTimer.this.mDebugLevel, new String(indent) + "}@( " + (this.mEndTime - this.mStartTime) + "ms )" + pPostfix);
				}
			}
		}

		private void ensureChildrenAllocated() {
			if(this.mChildren == null) {
				this.mChildren = new ArrayList<DebugTime>();
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}