package org.andengine.util;

import java.util.List;

import org.andengine.util.exception.AndEngineException;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 12:30:32 - 14.04.2013
 */
public final class IntentUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private IntentUtils() {

	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public static boolean isIntentResolvable(final Context pContext, final Intent pIntent) {
		final List<ResolveInfo> resolveInfo = pContext.getPackageManager().queryIntentActivities(pIntent, 0);
		return (resolveInfo != null) && !resolveInfo.isEmpty();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class IntentNotResolveableException extends AndEngineException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = 249698759677552332L;

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public IntentNotResolveableException() {

		}

		public IntentNotResolveableException(final String pMessage) {
			super(pMessage);
		}

		public IntentNotResolveableException(final Throwable pThrowable) {
			super(pThrowable);
		}

		public IntentNotResolveableException(final String pMessage, final Throwable pThrowable) {
			super(pMessage, pThrowable);
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

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
