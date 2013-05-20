package org.andengine.util;

import org.andengine.util.debug.Debug;
import org.andengine.util.debug.Debug.DebugLevel;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 12:23:06 - 19.01.2012
 */
public final class ThreadUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int STACKTRACE_CALLER_DEPTH = 3;

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private ThreadUtils() {

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

	public static void dumpCurrentThreadInfo() {
		ThreadUtils.dumpCurrentThreadInfo(DebugLevel.DEBUG, Thread.currentThread().getStackTrace()[ThreadUtils.STACKTRACE_CALLER_DEPTH]);
	}

	public static void dumpCurrentThreadInfo(final DebugLevel pDebugLevel) {
		ThreadUtils.dumpCurrentThreadInfo(pDebugLevel, Thread.currentThread().getStackTrace()[ThreadUtils.STACKTRACE_CALLER_DEPTH]);
	}

	private static void dumpCurrentThreadInfo(final DebugLevel pDebugLevel, final StackTraceElement pCaller) {
		Debug.log(pDebugLevel, pCaller.getClassName() + "." + pCaller.getMethodName() + "(" + pCaller.getFileName() + ".java:" + pCaller.getLineNumber() + ") @(Thread: '" + Thread.currentThread().getName() + "')");
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
