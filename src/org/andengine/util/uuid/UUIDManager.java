package org.andengine.util.uuid;

import java.util.UUID;

import org.andengine.util.preferences.SimplePreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * (c) 2013 Nicolas Gramlich
 * 
 * @author Nicolas Gramlich
 * @since 01:15:12 - 22.03.2013
 */
public class UUIDManager {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String PREFERENCES_UUIDMANAGER_UUID_KEY = "preferences.uuidmanager.uuid";

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

	public static synchronized UUID getUUID(final Context pContext) {
		final SharedPreferences preferences = SimplePreferences.getInstance(pContext);
		return UUIDManager.getUUID(preferences);
	}

	public static UUID getUUID(final SharedPreferences pSharedPreferences) {
		final String uuidPreference = pSharedPreferences.getString(UUIDManager.PREFERENCES_UUIDMANAGER_UUID_KEY, null);

		if (uuidPreference != null) {
			return UUID.fromString(uuidPreference);
		} else {
			final UUID uuid = UUID.randomUUID();

			final Editor editor = pSharedPreferences.edit();
			editor.putString(UUIDManager.PREFERENCES_UUIDMANAGER_UUID_KEY, uuid.toString());
			editor.commit();

			return uuid;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
