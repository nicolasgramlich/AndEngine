package org.andengine.util.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 19:20:32 - 03.05.2013
 */
public final class HttpClientUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private HttpClientUtils() {

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

	public static List<NameValuePair> convertParametersToNameValuePairs(final Map<String, String> pParameters) {
		final List<NameValuePair> result = new ArrayList<NameValuePair>();

		for (final Entry<String, String> entry : pParameters.entrySet()) {
			result.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return result;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
