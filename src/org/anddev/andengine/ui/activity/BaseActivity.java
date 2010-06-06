package org.anddev.andengine.ui.activity;

import java.util.concurrent.Callable;

import org.anddev.andengine.util.AsyncCallable;
import org.anddev.andengine.util.Callback;
import org.anddev.andengine.util.Debug;
import org.anddev.andengine.util.ProgressCallable;
import org.anddev.progressmonitor.IProgressListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author Nicolas Gramlich
 * @since 18:35:28 - 29.08.2009
 */
public abstract class BaseActivity extends Activity {
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

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Performs a task in the background, showing a {@link ProgressDialog}, while the {@link Callable} is being processed.
	 * @param <T>
	 * @param pTitleResID
	 * @param pMessageResID
	 * @param pErrorMessageResID
	 * @param pCallable
	 * @param pCallback
	 */
	protected <T> void doAsync(final int pTitleResID, final int pMessageResID, final int pErrorMessageResID, final Callable<T> pCallable, final Callback<T> pCallback){
		new AsyncTask<Void, Void, T>() {
			private ProgressDialog mPD;

			@Override
			public void onPreExecute() {
				this.mPD = ProgressDialog.show(BaseActivity.this, getString(pTitleResID), getString(pMessageResID));
				super.onPreExecute();
			}

			@Override
			public T doInBackground(final Void ... params) {
				try {
					return pCallable.call();
				} catch (final Throwable t) {
					Debug.e("Error", t);
					cancel(true);
				}
				return null;
			}

			@Override
			public void onPostExecute(final T result) {
				try {
					this.mPD.dismiss();
				} catch (final Throwable t) {
					Debug.e("Error", t);
					/* Nothing. */
				}

				if(result == null){
					Toast.makeText(BaseActivity.this, pErrorMessageResID, Toast.LENGTH_LONG).show();
				}else{
					pCallback.onCallback(result);
				}

				super.onPostExecute(result);
			}
		}.execute((Void[])null);
	}
	
	/**
	 * Performs a task in the background, showing a {@link ProgressDialog}, while the {@link Callable} is being processed.
	 * @param <T>
	 * @param pTitleResID
	 * @param pMessageResID
	 * @param pErrorMessageResID
	 * @param pCallable
	 * @param pCallback
	 */
	protected <T> void doAsync(final int pTitleResID, final int pMessageResID, final int pErrorMessageResID, final AsyncCallable<T> pCallable, final Callback<T> pCallback){
		final ProgressDialog pd = ProgressDialog.show(BaseActivity.this, getString(pTitleResID), getString(pMessageResID));
		pCallable.call(new Callback<T>() {
			@Override
			public void onCallback(T result) {
				try {
					pd.dismiss();
				} catch (final Throwable t) {
					Debug.e("Error", t);
					/* Nothing. */
				}

				if(result == null){
					Toast.makeText(BaseActivity.this, pErrorMessageResID, Toast.LENGTH_LONG).show();
				}else{
					pCallback.onCallback(result);
				}
			}
		});
	}	

	protected <T> void doProgressAsync(final int pTitleResID, final int pErrorMessageResID, final ProgressCallable<T> pCallable, final Callback<T> pCallback){
		new AsyncTask<Void, Integer, T>() {
			private ProgressDialog mPD;

			@Override
			public void onPreExecute() {
				this.mPD = new ProgressDialog(BaseActivity.this);
				this.mPD.setTitle(pTitleResID);
				this.mPD.setIcon(android.R.drawable.ic_menu_save);
				this.mPD.setIndeterminate(false);
				this.mPD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				this.mPD.show();
				super.onPreExecute();
			}

			@Override
			public T doInBackground(final Void ... params) {
				try {
					return pCallable.call(new IProgressListener() {
						@Override
						public void onProgressChanged(final int pProgress) {
							onProgressUpdate(pProgress);
						}
					});
				} catch (final Throwable t) {
					Debug.e("Error", t);
					cancel(true);
				}
				return null;
			}
			
			@Override
			public void onProgressUpdate(final Integer... values) {
				this.mPD.setProgress(values[0]);
			}

			@Override
			public void onPostExecute(final T result) {
				try {
					this.mPD.dismiss();
				} catch (final Throwable t) {
					Debug.e("Error", t);
					/* Nothing. */
				}

				if(result == null){
					Toast.makeText(BaseActivity.this, pErrorMessageResID, Toast.LENGTH_LONG).show();
				}else{
					pCallback.onCallback(result);
				}

				super.onPostExecute(result);
			}
		}.execute((Void[])null);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
