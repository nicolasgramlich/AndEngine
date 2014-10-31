package org.andengine.examples.launcher;

import java.util.Arrays;

import org.andengine.AndEngine;
import org.andengine.examples.R;
import org.andengine.util.debug.Debug;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:56:46 - 16.06.2010
 */
public class ExampleLauncher extends ExpandableListActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final String PREF_LAST_APP_LAUNCH_VERSIONCODE_ID = "last.app.launch.versioncode";

	private static final int DIALOG_FIRST_APP_LAUNCH = 0;
	private static final int DIALOG_NEW_IN_THIS_VERSION = ExampleLauncher.DIALOG_FIRST_APP_LAUNCH + 1;
	private static final int DIALOG_BENCHMARKS_SUBMIT_PLEASE = ExampleLauncher.DIALOG_NEW_IN_THIS_VERSION + 1;
	private static final int DIALOG_DEVICE_NOT_SUPPORTED = ExampleLauncher.DIALOG_BENCHMARKS_SUBMIT_PLEASE + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private ExpandableExampleLauncherListAdapter mExpandableExampleLauncherListAdapter;

	private int mVersionCodeCurrent;

	private int mVersionCodeLastLaunch;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(!AndEngine.isDeviceSupported()) {
			this.showDialog(ExampleLauncher.DIALOG_DEVICE_NOT_SUPPORTED);
		}

		this.setContentView(R.layout.list_examples);

		this.mExpandableExampleLauncherListAdapter = new ExpandableExampleLauncherListAdapter(this);

		this.setListAdapter(this.mExpandableExampleLauncherListAdapter);

		this.findViewById(R.id.btn_get_involved).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View pView) {
				ExampleLauncher.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.andengine.org")));
			}
		});

		final SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);

		this.mVersionCodeCurrent = this.getVersionCode();
		this.mVersionCodeLastLaunch = prefs.getInt(ExampleLauncher.PREF_LAST_APP_LAUNCH_VERSIONCODE_ID, -1);

		if(this.isFirstTime("first.app.launch")) {
			this.showDialog(ExampleLauncher.DIALOG_FIRST_APP_LAUNCH);
		} else if((this.mVersionCodeLastLaunch != -1) && (this.mVersionCodeLastLaunch < this.mVersionCodeCurrent)){
			this.showDialog(ExampleLauncher.DIALOG_NEW_IN_THIS_VERSION);
		} else if(this.isFirstTime("please.submit.benchmarks")){
			this.showDialog(ExampleLauncher.DIALOG_BENCHMARKS_SUBMIT_PLEASE);
		}

		prefs.edit().putInt(ExampleLauncher.PREF_LAST_APP_LAUNCH_VERSIONCODE_ID, this.mVersionCodeCurrent).commit();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected Dialog onCreateDialog(final int pId) {
		switch(pId) {
			case DIALOG_DEVICE_NOT_SUPPORTED:
				return new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_device_not_supported_title)
					.setMessage(R.string.dialog_device_not_supported_message)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.ok, null)
					.create();
			case DIALOG_FIRST_APP_LAUNCH:
				return new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_first_app_launch_title)
					.setMessage(R.string.dialog_first_app_launch_message)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(android.R.string.ok, null)
					.create();
			case DIALOG_BENCHMARKS_SUBMIT_PLEASE:
				return new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_benchmarks_submit_please_title)
					.setMessage(R.string.dialog_benchmarks_submit_please_message)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(android.R.string.ok, null)
					.create();
			case DIALOG_NEW_IN_THIS_VERSION:
				final int[] versionCodes = this.getResources().getIntArray(R.array.new_in_version_versioncode);
				final int versionDescriptionsStartIndex = Math.max(0, Arrays.binarySearch(versionCodes, this.mVersionCodeLastLaunch) + 1);

				final String[] versionDescriptions = this.getResources().getStringArray(R.array.new_in_version_changes);

				final StringBuilder sb = new StringBuilder();
				for(int i = versionDescriptions.length - 1; i >= versionDescriptionsStartIndex; i--) {
					sb.append("--------------------------\n");
					sb.append(">>>  Version: " + versionCodes[i] + "\n");
					sb.append("--------------------------\n");
					sb.append(versionDescriptions[i]);

					if(i > versionDescriptionsStartIndex){
						sb.append("\n\n");
					}
				}

				return new AlertDialog.Builder(this)
					.setTitle(R.string.dialog_new_in_this_version_title)
					.setMessage(sb.toString())
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton(android.R.string.ok, null)
					.create();
			default:
				return super.onCreateDialog(pId);
		}
	}

	@Override
	public void onGroupExpand(final int pGroupPosition) {
		switch(this.mExpandableExampleLauncherListAdapter.getGroup(pGroupPosition)){
			case BENCHMARK:
				Toast.makeText(this, "When running a benchmark, a dialog with the results will appear after some seconds.", Toast.LENGTH_SHORT).show();
		}
		super.onGroupExpand(pGroupPosition);
	}

	@Override
	public boolean onChildClick(final ExpandableListView pParent, final View pV, final int pGroupPosition, final int pChildPosition, final long pId) {
		final Example example = this.mExpandableExampleLauncherListAdapter.getChild(pGroupPosition, pChildPosition);

		this.startActivity(new Intent(this, example.CLASS));

		return super.onChildClick(pParent, pV, pGroupPosition, pChildPosition, pId);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public boolean isFirstTime(final String pKey){
		final SharedPreferences prefs = this.getPreferences(Context.MODE_PRIVATE);
		if(prefs.getBoolean(pKey, true)){
			prefs.edit().putBoolean(pKey, false).commit();
			return true;
		}
		return false;
	}

	public int getVersionCode() {
		try {
			final PackageInfo pi = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			return pi.versionCode;
		} catch (final PackageManager.NameNotFoundException e) {
			Debug.e("Package name not found", e);
			return -1;
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}