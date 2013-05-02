package org.andengine.util.escape;

import org.andengine.util.adt.map.IIntLookupMap;

import android.text.TextUtils;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:20:25 - 26.04.2012
 */
public final class Unescaper {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private Unescaper() {

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

	/**
	 * <p>Unescapes the entities in a {@link String}.</p>
	 *
	 * <p>For example, if you have called addEntity(&quot;foo&quot;, 0xA1),
	 * unescape(&quot;&amp;foo;&quot;) will return &quot;\u00A1&quot;</p>
	 *
	 * @param pString The {@link String} to escape.
	 * @param pIntLookupMap the {@link IIntLookupMap} to look up entities.
	 * @return A new escaped {@link String}.
	 */
	public static String unescape(final CharSequence pCharSequence, final IIntLookupMap<CharSequence> pIntLookupMap) {
		final int stringLength = pCharSequence.length();
		final StringBuilder stringBuilder = new StringBuilder(stringLength);

		int i;
		for (i = 0; i < stringLength; ++i) {
			final char ch = pCharSequence.charAt(i);
			if (ch == '&') {
				final int semi = TextUtils.indexOf(pCharSequence, ';', i + 1);
				if (semi == -1) {
					stringBuilder.append(ch);
					continue;
				}
				final CharSequence entityName = pCharSequence.subSequence(i + 1, semi);
				int entityValue;
				if (entityName.charAt(0) == '#') {
					final char charAt1 = entityName.charAt(1);
					if ((charAt1 == 'x') || (charAt1 == 'X')) {
						entityValue = Integer.valueOf(entityName.subSequence(2, entityName.length()).toString(), 16).intValue();
					} else {
						entityValue = Integer.parseInt(entityName.subSequence(1, entityName.length()).toString());
					}
				} else {
					entityValue = pIntLookupMap.value(entityName);
				}
				if (entityValue == -1) {
					stringBuilder.append('&');
					stringBuilder.append(entityName);
					stringBuilder.append(';');
				} else {
					stringBuilder.append((char) (entityValue));
				}
				i = semi;
			} else {
				stringBuilder.append(ch);
			}
		}

		return stringBuilder.toString();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
