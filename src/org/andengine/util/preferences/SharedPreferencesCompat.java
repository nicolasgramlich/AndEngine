package org.andengine.util.preferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.SharedPreferences.Editor;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 20:41:56 - 07.04.2013
 */
public class SharedPreferencesCompat {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final Method WORKAROUND_APPLY_METHOD;

	static {
		Method applyMethod;
		try {
			final Class<Editor> cls = Editor.class;
			applyMethod = cls.getMethod("apply");
		} catch (final NoSuchMethodException unused) {
			applyMethod = null;
		}

		WORKAROUND_APPLY_METHOD = applyMethod;
	}

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

	public static void apply(final Editor pEditor) {
		if (SharedPreferencesCompat.WORKAROUND_APPLY_METHOD != null) {
			try {
				SharedPreferencesCompat.WORKAROUND_APPLY_METHOD.invoke(pEditor);
				return;
			} catch (final InvocationTargetException e) {
				/* Nothing. */
			} catch (final IllegalAccessException e) {
				/* Nothing. */
			}
		}
		pEditor.commit();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
