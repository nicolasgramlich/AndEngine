package org.andengine.util.experiment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * (c) 2013 Nicolas Gramlich
 * 
 * @author Nicolas Gramlich
 * @since 08:22:34 - 22.03.2013
 */
public class ExperimentFactory implements IExperimentFactory {
	// ===========================================================
	// Constants
	// ===========================================================

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

	@Override
	public Experiment<?> parseExperiment(final JSONObject pExperimentJSONObject) throws JSONException {
		final String name = pExperimentJSONObject.getString("name");
		final String type = pExperimentJSONObject.getString("type");

		if (type.equals(Boolean.class.getSimpleName())) {
			final Boolean value;
			if (pExperimentJSONObject.has("value")) {
				value = pExperimentJSONObject.getBoolean("value");
			} else {
				value = null;
			}
			return new Experiment<Boolean>(name, Boolean.class, value);
		} else if (type.equals(Byte.class.getSimpleName())) {
			final Byte value;
			if (pExperimentJSONObject.has("value")) {
				value = (byte) pExperimentJSONObject.getInt("value");
			} else {
				value = null;
			}
			return new Experiment<Byte>(name, Byte.class, value);
		} else if (type.equals(Character.class.getSimpleName())) {
			final Character value;
			if (pExperimentJSONObject.has("value")) {
				value = (char) pExperimentJSONObject.getInt("value");
			} else {
				value = null;
			}
			return new Experiment<Character>(name, Character.class, value);
		} else if (type.equals(Short.class.getSimpleName())) {
			final Short value;
			if (pExperimentJSONObject.has("value")) {
				value = (short) pExperimentJSONObject.getInt("value");
			} else {
				value = null;
			}
			return new Experiment<Short>(name, Short.class, value);
		} else if (type.equals(Integer.class.getSimpleName())) {
			final Integer value;
			if (pExperimentJSONObject.has("value")) {
				value = pExperimentJSONObject.getInt("value");
			} else {
				value = null;
			}
			return new Experiment<Integer>(name, Integer.class, value);
		} else if (type.equals(Long.class.getSimpleName())) {
			final Long value;
			if (pExperimentJSONObject.has("value")) {
				value = pExperimentJSONObject.getLong("value");
			} else {
				value = null;
			}
			return new Experiment<Long>(name, Long.class, value);
		} else if (type.equals(Float.class.getSimpleName())) {
			final Float value;
			if (pExperimentJSONObject.has("value")) {
				value = (float) pExperimentJSONObject.getDouble("value");
			} else {
				value = null;
			}
			return new Experiment<Float>(name, Float.class, value);
		} else if (type.equals(Double.class.getSimpleName())) {
			final Double value;
			if (pExperimentJSONObject.has("value")) {
				value = pExperimentJSONObject.getDouble("value");
			} else {
				value = null;
			}
			return new Experiment<Double>(name, Double.class, value);
		} else if (type.equals(String.class.getSimpleName())) {
			final String value = pExperimentJSONObject.optString("value", null);
			return new Experiment<String>(name, String.class, value);
		} else if (type.equals(JSONObject.class.getSimpleName())) {
			final JSONObject value = pExperimentJSONObject.optJSONObject("value");
			return new Experiment<JSONObject>(name, JSONObject.class, value);
		} else if (type.equals(JSONArray.class.getSimpleName())) {
			final JSONArray value = pExperimentJSONObject.optJSONArray("value");
			return new Experiment<JSONArray>(name, JSONArray.class, value);
		} else {
			final Object value = pExperimentJSONObject.opt("value");
			return new Experiment<Object>(name, Object.class, value);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
