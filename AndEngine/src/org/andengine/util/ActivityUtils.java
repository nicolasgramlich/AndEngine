package org.andengine.util;

import org.andengine.util.call.AsyncCallable;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;
import org.andengine.util.debug.Debug;
import org.andengine.util.exception.CancelledException;
import org.andengine.util.progress.IProgressListener;
import org.andengine.util.progress.ProgressCallable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:11:54 - 07.03.2011
 */
public class ActivityUtils {
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

	public static void requestFullscreen(final Activity pActivity) {
		final Window window = pActivity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * @param pActivity
	 * @param pScreenBrightness [0..1]
	 */
	public static void setScreenBrightness(final Activity pActivity, final float pScreenBrightness) {
		final Window window = pActivity.getWindow();
		final WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
		windowLayoutParams.screenBrightness = pScreenBrightness;
		window.setAttributes(windowLayoutParams);
	}

	public static void keepScreenOn(final Activity pActivity) {
		pActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	public static <T> void doAsync(final Context pContext, final int pTitleResourceID, final int pMessageResourceID, final Callable<T> pCallable, final Callback<T> pCallback) {
		ActivityUtils.doAsync(pContext, pTitleResourceID, pMessageResourceID, pCallable, pCallback, null, false);
	}

	public static <T> void doAsync(final Context pContext, final CharSequence pTitle, final CharSequence pMessage, final Callable<T> pCallable, final Callback<T> pCallback) {
		ActivityUtils.doAsync(pContext, pTitle, pMessage, pCallable, pCallback, null, false);
	}

	public static <T> void doAsync(final Context pContext, final int pTitleResourceID, final int pMessageResourceID, final Callable<T> pCallable, final Callback<T> pCallback, final boolean pCancelable) {
		ActivityUtils.doAsync(pContext, pTitleResourceID, pMessageResourceID, pCallable, pCallback, null, pCancelable);
	}

	public static <T> void doAsync(final Context pContext, final CharSequence pTitle, final CharSequence pMessage, final Callable<T> pCallable, final Callback<T> pCallback, final boolean pCancelable) {
		ActivityUtils.doAsync(pContext, pTitle, pMessage, pCallable, pCallback, null, pCancelable);
	}

	public static <T> void doAsync(final Context pContext, final int pTitleResourceID, final int pMessageResourceID, final Callable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		ActivityUtils.doAsync(pContext, pTitleResourceID, pMessageResourceID, pCallable, pCallback, pExceptionCallback, false);
	}

	public static <T> void doAsync(final Context pContext, final CharSequence pTitle, final CharSequence pMessage, final Callable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		ActivityUtils.doAsync(pContext, pTitle, pMessage, pCallable, pCallback, pExceptionCallback, false);
	}

	public static <T> void doAsync(final Context pContext, final int pTitleResourceID, final int pMessageResourceID, final Callable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback, final boolean pCancelable) {
		ActivityUtils.doAsync(pContext, pContext.getString(pTitleResourceID), pContext.getString(pMessageResourceID), pCallable, pCallback, pExceptionCallback, pCancelable);
	}

	public static <T> void doAsync(final Context pContext, final CharSequence pTitle, final CharSequence pMessage, final Callable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback, final boolean pCancelable) {
		new AsyncTask<Void, Void, T>() {
			private ProgressDialog mPD;
			private Exception mException = null;

			@Override
			public void onPreExecute() {
				this.mPD = ProgressDialog.show(pContext, pTitle, pMessage, true, pCancelable);
				if(pCancelable) {
					this.mPD.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(final DialogInterface pDialogInterface) {
							pExceptionCallback.onCallback(new CancelledException());
							pDialogInterface.dismiss();
						}
					});
				}
				super.onPreExecute();
			}

			@Override
			public T doInBackground(final Void... params) {
				try {
					return pCallable.call();
				} catch (final Exception e) {
					this.mException = e;
				}
				return null;
			}

			@Override
			public void onPostExecute(final T result) {
				try {
					this.mPD.dismiss();
				} catch (final Exception e) {
					Debug.e("Error", e);
				}

				if(this.isCancelled()) {
					this.mException = new CancelledException();
				}

				if(this.mException == null) {
					pCallback.onCallback(result);
				} else {
					if(pExceptionCallback == null) {
						Debug.e("Error", this.mException);
					} else {
						pExceptionCallback.onCallback(this.mException);
					}
				}

				super.onPostExecute(result);
			}
		}.execute((Void[]) null);
	}
	
	public static <T> void doProgressAsync(final Context pContext, final int pTitleResourceID, final int pIconResourceID, final ProgressCallable<T> pCallable, final Callback<T> pCallback) {
		ActivityUtils.doProgressAsync(pContext, pTitleResourceID, pIconResourceID, pCallable, pCallback, null);
	}

	public static <T> void doProgressAsync(final Context pContext, final CharSequence pTitle, final int pIconResourceID, final ProgressCallable<T> pCallable, final Callback<T> pCallback) {
		ActivityUtils.doProgressAsync(pContext, pTitle, pIconResourceID, pCallable, pCallback, null);
	}

	public static <T> void doProgressAsync(final Context pContext, final int pTitleResourceID, final int pIconResourceID, final ProgressCallable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		ActivityUtils.doProgressAsync(pContext, pContext.getString(pTitleResourceID), pIconResourceID, pCallable, pCallback, pExceptionCallback);
	}

	public static <T> void doProgressAsync(final Context pContext, final CharSequence pTitle, final int pIconResourceID, final ProgressCallable<T> pCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		new AsyncTask<Void, Integer, T>() {
			private ProgressDialog mPD;
			private Exception mException = null;

			@Override
			public void onPreExecute() {
				this.mPD = new ProgressDialog(pContext);
				this.mPD.setTitle(pTitle);
				this.mPD.setIcon(pIconResourceID);
				this.mPD.setIndeterminate(false);
				this.mPD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				this.mPD.show();
				super.onPreExecute();
			}

			@Override
			public T doInBackground(final Void... params) {
				try {
					return pCallable.call(new IProgressListener() {
						@Override
						public void onProgressChanged(final int pProgress) {
							onProgressUpdate(pProgress);
						}
					});
				} catch (final Exception e) {
					this.mException = e;
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
				} catch (final Exception e) {
					Debug.e("Error", e);
					/* Nothing. */
				}

				if(this.isCancelled()) {
					this.mException = new CancelledException();
				}

				if(this.mException == null) {
					pCallback.onCallback(result);
				} else {
					if(pExceptionCallback == null) {
						Debug.e("Error", this.mException);
					} else {
						pExceptionCallback.onCallback(this.mException);
					}
				}

				super.onPostExecute(result);
			}
		}.execute((Void[]) null);
	}

	public static <T> void doAsync(final Context pContext, final int pTitleResourceID, final int pMessageResourceID, final AsyncCallable<T> pAsyncCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		ActivityUtils.doAsync(pContext, pContext.getString(pTitleResourceID), pContext.getString(pMessageResourceID), pAsyncCallable, pCallback, pExceptionCallback);
	}

	public static <T> void doAsync(final Context pContext, final CharSequence pTitle, final CharSequence pMessage, final AsyncCallable<T> pAsyncCallable, final Callback<T> pCallback, final Callback<Exception> pExceptionCallback) {
		final ProgressDialog pd = ProgressDialog.show(pContext, pTitle, pMessage);
		pAsyncCallable.call(new Callback<T>() {
			@Override
			public void onCallback(final T result) {
				try {
					pd.dismiss();
				} catch (final Exception e) {
					Debug.e("Error", e);
					/* Nothing. */
				}

				pCallback.onCallback(result);
			}
		}, pExceptionCallback);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
