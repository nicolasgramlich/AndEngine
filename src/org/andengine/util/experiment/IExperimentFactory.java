package org.andengine.util.experiment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * (c) 2013 Nicolas Gramlich
 *
 * @author Nicolas Gramlich
 * @since 08:22:34 - 22.03.2013
 */
public interface IExperimentFactory {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public Experiment<?> parseExperiment(final JSONObject pExperimentJSONObject) throws JSONException;

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
