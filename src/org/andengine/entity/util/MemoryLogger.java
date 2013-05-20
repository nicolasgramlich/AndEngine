package org.andengine.entity.util;

import org.andengine.BuildConfig;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.util.TextUtils;
import org.andengine.util.debug.Debug;
import org.andengine.util.debug.Debug.DebugLevel;
import org.andengine.util.system.SystemUtils;
import org.andengine.util.system.SystemUtils.SystemUtilsException;



/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:50:15 - 14.05.2012
 */
public class MemoryLogger implements IUpdateHandler {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final float AVERAGE_DURATION_DEFAULT = 5;

	// ===========================================================
	// Fields
	// ===========================================================

	private final float mAverageDuration;
	private final DebugLevel mDebugLevel;

	private float mSecondsElapsed;

	private boolean mLogSystemMemory;
	private long mPreviousSystemMemorySize;
	private long mPreviousSystemMemoryFreeSize;

	private boolean mLogDalvikHeap;
	private long mPreviousDalvikHeapSize;
	private long mPreviousDalvikHeapFreeSize;
	private long mPreviousDalvikHeapAllocatedSize;

	private boolean mLogDalvikMemoryInfo;
	private long mPreviousDalvikProportionalSetSize;
	private long mPreviousDalvikPrivateDirtyPages;
	private long mPreviousDalvikSharedDirtyPages;

	private boolean mLogNativeHeap;
	private long mPreviousNativeHeapSize;
	private long mPreviousNativeHeapFreeSize;
	private long mPreviousNativeHeapAllocatedSize;

	private boolean mLogNativeMemoryInfo;
	private long mPreviousNativeProportionalSetSize;
	private long mPreviousNativePrivateDirtyPages;
	private long mPreviousNativeSharedDirtyPages;

	// ===========================================================
	// Constructors
	// ===========================================================

	public MemoryLogger() {
		this(DebugLevel.DEBUG);
	}

	public MemoryLogger(final DebugLevel pDebugLevel) {
		this(MemoryLogger.AVERAGE_DURATION_DEFAULT, pDebugLevel);
	}

	public MemoryLogger(final float pAverageDuration) {
		this(pAverageDuration, DebugLevel.DEBUG);
	}

	public MemoryLogger(final float pAverageDuration, final DebugLevel pDebugLevel) {
		this(pAverageDuration, pDebugLevel, true, true, false, true, false);
	}

	public MemoryLogger(final float pAverageDuration, final DebugLevel pDebugLevel, final boolean pLogSystemMemory, final boolean pLogDalvikHeap, final boolean pLogDalvikMemoryInfo, final boolean pLogNativeHeap, final boolean pLogNativeMemoryInfo) {
		this.mAverageDuration = pAverageDuration;
		this.mDebugLevel = pDebugLevel;

		this.mLogSystemMemory = pLogSystemMemory;
		this.mLogDalvikHeap = pLogDalvikHeap;
		this.mLogDalvikMemoryInfo = pLogDalvikMemoryInfo;
		this.mLogNativeHeap = pLogNativeHeap;
		this.mLogNativeMemoryInfo = pLogNativeMemoryInfo;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onUpdate(final float pSecondsElapsed) {
		this.mSecondsElapsed += pSecondsElapsed;

		if (this.mSecondsElapsed > this.mAverageDuration) {
			this.onHandleLogDurationElapsed();

			this.mSecondsElapsed -= this.mAverageDuration;
		}
	}

	@Override
	public void reset() {
		this.mSecondsElapsed = 0;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onHandleLogDurationElapsed() {
		if (BuildConfig.DEBUG) {
			/* Execute GC. */
			System.gc();
			try {
				final StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("+------------------------------+---------------+-----------------+\n");
				stringBuilder.append("|         Memory Stat          |    Current    |      Change     |\n");
				stringBuilder.append("+------------------------------+---------------+-----------------+\n");

				if (this.mLogSystemMemory) {
					final long systemMemorySize = SystemUtils.getSystemMemorySize();
					final long systemMemoryFreeSize = SystemUtils.getSystemMemoryFreeSize();

					final long systemMemorySizeDiff = systemMemorySize - this.mPreviousSystemMemorySize;
					final long systemMemoryFreeSizeDiff = systemMemoryFreeSize - this.mPreviousSystemMemoryFreeSize;

					this.mPreviousSystemMemorySize = systemMemorySize;
					this.mPreviousSystemMemoryFreeSize = systemMemoryFreeSize;

					stringBuilder.append("| System memory size           | " + MemoryLogger.formatRight(systemMemorySize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(systemMemorySizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| System memory free size      | " + MemoryLogger.formatRight(systemMemoryFreeSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(systemMemoryFreeSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");
				}

				if (this.mLogDalvikHeap) {
					final long dalvikHeapSize = SystemUtils.getDalvikHeapSize();
					final long dalvikHeapFreeSize = SystemUtils.getDalvikHeapFreeSize();
					final long dalvikHeapAllocatedSize = SystemUtils.getDalvikHeapAllocatedSize();

					final long dalvikHeapSizeDiff = dalvikHeapSize - this.mPreviousDalvikHeapSize;
					final long dalvikHeapFreeSizeDiff = dalvikHeapFreeSize - this.mPreviousDalvikHeapFreeSize;
					final long dalvikHeapAllocatedSizeDiff = dalvikHeapAllocatedSize - this.mPreviousDalvikHeapAllocatedSize;

					stringBuilder.append("| Dalvik memory size           | " + MemoryLogger.formatRight(dalvikHeapSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(dalvikHeapSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| Dalvik memory free size      | " + MemoryLogger.formatRight(dalvikHeapFreeSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(dalvikHeapFreeSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| Dalvik memory allocated size | " + MemoryLogger.formatRight(dalvikHeapAllocatedSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(dalvikHeapAllocatedSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");

					this.mPreviousDalvikHeapSize = dalvikHeapSize;
					this.mPreviousDalvikHeapFreeSize = dalvikHeapFreeSize;
					this.mPreviousDalvikHeapAllocatedSize = dalvikHeapAllocatedSize;
				}

				if (this.mLogDalvikMemoryInfo) {
					final long dalvikProportionalSetSize = SystemUtils.getDalvikProportionalSetSize();
					final long dalvikPrivateDirtyPages = SystemUtils.getDalvikPrivateDirtyPages();
					final long dalvikSharedDirtyPages = SystemUtils.getDalvikSharedDirtyPages();

					final long dalvikProportionalSetSizeDiff = dalvikProportionalSetSize - this.mPreviousDalvikProportionalSetSize;
					final long dalvikPrivateDirtyPagesDiff = dalvikPrivateDirtyPages - this.mPreviousDalvikPrivateDirtyPages;
					final long dalvikSharedDirtyPagesDiff = dalvikSharedDirtyPages - this.mPreviousDalvikSharedDirtyPages;

					stringBuilder.append("| Dalvik proportional set size | " + MemoryLogger.formatRight(dalvikProportionalSetSize, ' ', 10) + "    | (" + MemoryLogger.formatRight(dalvikProportionalSetSizeDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("| Dalvik private dirty pages   | " + MemoryLogger.formatRight(dalvikPrivateDirtyPages, ' ', 10) + "    | (" + MemoryLogger.formatRight(dalvikPrivateDirtyPagesDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("| Dalvik shared dirty pages    | " + MemoryLogger.formatRight(dalvikSharedDirtyPages, ' ', 10) + "    | (" + MemoryLogger.formatRight(dalvikSharedDirtyPagesDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");

					this.mPreviousDalvikProportionalSetSize = dalvikProportionalSetSize;
					this.mPreviousDalvikPrivateDirtyPages = dalvikPrivateDirtyPages;
					this.mPreviousDalvikSharedDirtyPages = dalvikSharedDirtyPages;
				}

				if (this.mLogNativeHeap) {
					final long nativeHeapSize = SystemUtils.getNativeHeapSize();
					final long nativeHeapFreeSize = SystemUtils.getNativeHeapFreeSize();
					final long nativeHeapAllocatedSize = SystemUtils.getNativeHeapAllocatedSize();

					final long nativeHeapSizeDiff = nativeHeapSize - this.mPreviousNativeHeapSize;
					final long nativeHeapFreeSizeDiff = nativeHeapFreeSize - this.mPreviousNativeHeapFreeSize;
					final long nativeHeapAllocatedSizeDiff = nativeHeapAllocatedSize - this.mPreviousNativeHeapAllocatedSize;

					stringBuilder.append("| Native memory size           | " + MemoryLogger.formatRight(nativeHeapSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(nativeHeapSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| Native memory free size      | " + MemoryLogger.formatRight(nativeHeapFreeSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(nativeHeapFreeSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("| Native memory allocated size | " + MemoryLogger.formatRight(nativeHeapAllocatedSize, ' ', 10) + " kB | (" + MemoryLogger.formatRight(nativeHeapAllocatedSizeDiff, ' ', 10, true) + " kB) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");

					this.mPreviousNativeHeapSize = nativeHeapSize;
					this.mPreviousNativeHeapFreeSize = nativeHeapFreeSize;
					this.mPreviousNativeHeapAllocatedSize = nativeHeapAllocatedSize;
				}

				if (this.mLogNativeMemoryInfo) {
					final long nativeProportionalSetSize = SystemUtils.getNativeProportionalSetSize();
					final long nativePrivateDirtyPages = SystemUtils.getNativePrivateDirtyPages();
					final long nativeSharedDirtyPages = SystemUtils.getNativeSharedDirtyPages();

					final long nativeProportionalSetSizeDiff = nativeProportionalSetSize - this.mPreviousNativeProportionalSetSize;
					final long nativePrivateDirtyPagesDiff = nativePrivateDirtyPages - this.mPreviousNativePrivateDirtyPages;
					final long nativeSharedDirtyPagesDiff = nativeSharedDirtyPages - this.mPreviousNativeSharedDirtyPages;

					stringBuilder.append("| Native proportional set size | " + MemoryLogger.formatRight(nativeProportionalSetSize, ' ', 10) + "    | (" + MemoryLogger.formatRight(nativeProportionalSetSizeDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("| Native private dirty pages   | " + MemoryLogger.formatRight(nativePrivateDirtyPages, ' ', 10) + "    | (" + MemoryLogger.formatRight(nativePrivateDirtyPagesDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("| Native shared dirty pages    | " + MemoryLogger.formatRight(nativeSharedDirtyPages, ' ', 10) + "    | (" + MemoryLogger.formatRight(nativeSharedDirtyPagesDiff, ' ', 10, true) + "   ) |\n");
					stringBuilder.append("+------------------------------+---------------+-----------------+\n");

					this.mPreviousNativeProportionalSetSize = nativeProportionalSetSize;
					this.mPreviousNativePrivateDirtyPages = nativePrivateDirtyPages;
					this.mPreviousNativeSharedDirtyPages = nativeSharedDirtyPages;
				}

				Debug.log(this.mDebugLevel, stringBuilder.toString());
			} catch (final SystemUtilsException e) {
				Debug.e(e);
			}
			System.gc();
		}
	}

	public static final CharSequence formatRight(final long pLong, final char pPadChar, final int pLength) {
		return MemoryLogger.formatRight(pLong, pPadChar, pLength, false);
	}

	public static final CharSequence formatRight(final long pLong, final char pPadChar, final int pLength, final boolean pAddPositiveSign) {
		if ((pLong > 0) && pAddPositiveSign) {
			return TextUtils.padFront("+" + String.valueOf(pLong), pPadChar, pLength);
		} else {
			return TextUtils.padFront(String.valueOf(pLong), pPadChar, pLength);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
