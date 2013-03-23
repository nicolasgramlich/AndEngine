package org.andengine.util.experiment;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.andengine.util.StreamUtils;
import org.andengine.util.call.Callback;
import org.andengine.util.debug.Debug;
import org.andengine.util.experiment.exception.ExperimentException;
import org.andengine.util.experiment.exception.ExperimentNotFoundException;
import org.andengine.util.experiment.exception.ExperimentTypeException;
import org.andengine.util.system.SystemUtils;
import org.andengine.util.system.SystemUtils.SystemUtilsException;
import org.andengine.util.uuid.UUIDManager;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

/**
 * (c) 2013 Nicolas Gramlich
 * 
 * @author Nicolas Gramlich
 * @since 00:25:13 - 22.03.2013
 */
public class ExperimentManager {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;
	private final String mServerURL;
	private final IExperimentFactory mExperimentFactory;

	private final String mPackageName;
	private final int mPackageVersionCode;
	private final UUID mUUID;

	private Map<String, Experiment<?>> mExperiments;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExperimentManager(final Context pContext, final String pServerURL) throws SystemUtilsException {
		this(pContext, pServerURL, new ExperimentFactory());
	}

	public ExperimentManager(final Context pContext, final String pServerURL, final IExperimentFactory pExperimentFactory) throws SystemUtilsException {
		this.mContext = pContext;
		this.mServerURL = pServerURL;
		this.mExperimentFactory = pExperimentFactory;

		this.mPackageName = SystemUtils.getPackageName(pContext);
		this.mPackageVersionCode = SystemUtils.getPackageVersionCode(pContext);

		this.mUUID = UUIDManager.getUUID(pContext);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Experiment<?> getExperiment(final String pExperimentName) throws ExperimentNotFoundException {
		final Experiment<?> experiment = this.mExperiments.get(pExperimentName);
		if (experiment != null) {
			return experiment;
		} else {
			throw new ExperimentNotFoundException("Unexpected experiment: '" + pExperimentName + "'.");
		}
	}

	public <T> T getExperimentValue(final String pExperimentName, final Class<T> pExperimentType) throws ExperimentTypeException, ExperimentNotFoundException {
		final Experiment<?> experiment = this.getExperiment(pExperimentName);
		final Object value = experiment.getValue();

		final Class<?> type = experiment.getType();
		if (type == pExperimentType) {
			return pExperimentType.cast(value);
		} else {
			throw new ExperimentTypeException("Unexpected type for experiment: '" + pExperimentName + "'. Requested: '" + pExperimentType.getSimpleName() + "' Actual: '" + type.getSimpleName() + "'.");
		}
	}

	public boolean getExperimentBooleanValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Boolean.class);
	}

	public boolean getExperimentBooleanValue(final String pExperimentName, final boolean pDefaultValue) {
		try {
			return this.getExperimentBooleanValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public byte getExperimentByteValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Byte.class);
	}

	public byte getExperimentByteValue(final String pExperimentName, final byte pDefaultValue) {
		try {
			return this.getExperimentByteValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public short getExperimentShortValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Short.class);
	}

	public short getExperimentShortValue(final String pExperimentName, final short pDefaultValue) {
		try {
			return this.getExperimentShortValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public char getExperimentCharacterValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Character.class);
	}

	public char getExperimentCharacterValue(final String pExperimentName, final char pDefaultValue) {
		try {
			return this.getExperimentCharacterValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public int getExperimentIntegerValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Integer.class);
	}

	public int getExperimentIntegerValue(final String pExperimentName, final int pDefaultValue) {
		try {
			return this.getExperimentIntegerValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public long getExperimentLongValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Long.class);
	}

	public long getExperimentLongValue(final String pExperimentName, final long pDefaultValue) {
		try {
			return this.getExperimentLongValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public float getExperimentFloatValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Float.class);
	}

	public float getExperimentFloatValue(final String pExperimentName, final float pDefaultValue) {
		try {
			return this.getExperimentFloatValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public double getExperimentDoubleValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Double.class);
	}

	public double getExperimentDoubleValue(final String pExperimentName, final double pDefaultValue) {
		try {
			return this.getExperimentDoubleValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public String getExperimentStringValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, String.class);
	}

	public String getExperimentStringValue(final String pExperimentName, final String pDefaultValue) {
		try {
			return this.getExperimentStringValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public JSONObject getExperimentJSONObjectValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, JSONObject.class);
	}

	public JSONObject getExperimentJSONObjectValue(final String pExperimentName, final JSONObject pDefaultValue) {
		try {
			return this.getExperimentJSONObjectValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	public JSONArray getExperimentJSONArrayValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, JSONArray.class);
	}

	public JSONArray getExperimentJSONArrayValue(final String pExperimentName, final JSONArray pDefaultValue) {
		try {
			return this.getExperimentJSONArrayValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	@Deprecated
	public Object getExperimentValue(final String pExperimentName) throws ExperimentTypeException, ExperimentNotFoundException {
		return this.getExperimentValue(pExperimentName, Object.class);
	}

	@Deprecated
	public Object getExperimentValue(final String pExperimentName, final Object pDefaultValue) {
		try {
			return this.getExperimentValue(pExperimentName);
		} catch (final ExperimentException e) {
			return pDefaultValue;
		}
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean fetchExperiments() throws IOException, URISyntaxException {
		final HttpClient httpClient = new DefaultHttpClient();

		final List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", this.mPackageName));
		params.add(new BasicNameValuePair("version", String.valueOf(this.mPackageVersionCode)));
		params.add(new BasicNameValuePair("uuid", this.mUUID.toString()));

		final URI uri = new URI(this.mServerURL + "?" + URLEncodedUtils.format(params, "utf-8"));
		final HttpGet httpGet = new HttpGet(uri);

		final HttpResponse response = httpClient.execute(httpGet);

		final int statusCode = response.getStatusLine().getStatusCode();
		if ((statusCode < HttpStatus.SC_OK) || (statusCode > HttpStatus.SC_MULTIPLE_CHOICES)) {
			return false;
		} else {
			final String serverResponse = StreamUtils.readFully(response.getEntity().getContent());

			try {
				this.mExperiments = this.parseExperiments(serverResponse);
				return true;
			} catch (final JSONException e) {
				Debug.e(e);
				return false;
			}
		}
	}

	public void fetchExperimentsAsync(final Callback<Boolean> pCallback) {
		final FetchExperimentsAsyncTask fetchExperimentsAsyncTask = new FetchExperimentsAsyncTask(pCallback);
		fetchExperimentsAsyncTask.execute((Void[]) null);
	}

	private Map<String, Experiment<?>> parseExperiments(final String pServerResponse) throws JSONException {
		final Map<String, Experiment<?>> experiments = new HashMap<String, Experiment<?>>();
		final JSONArray experimentsJSONArray = new JSONArray(pServerResponse);

		for (int i = 0; i < experimentsJSONArray.length(); i++) {
			final JSONObject experimentJSONObject = experimentsJSONArray.getJSONObject(i);

			final Experiment<?> experiment = this.mExperimentFactory.parseExperiment(experimentJSONObject);

			experiments.put(experiment.getName(), experiment);
		}

		return experiments;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public class FetchExperimentsAsyncTask extends AsyncTask<Void, Void, Boolean> {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final Callback<Boolean> mCallback;

		// ===========================================================
		// Constructors
		// ===========================================================

		public FetchExperimentsAsyncTask(final Callback<Boolean> pCallback) {
			this.mCallback = pCallback;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		protected Boolean doInBackground(final Void ... pParams) {
			try {
				return ExperimentManager.this.fetchExperiments();
			} catch (final Throwable t) {
				Debug.e(t);
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean pResult) {
			this.mCallback.onCallback(pResult);
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
