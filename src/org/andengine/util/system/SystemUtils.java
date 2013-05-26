package org.andengine.util.system;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.regex.MatchResult;

import org.andengine.util.StreamUtils;
import org.andengine.util.adt.data.constants.DataConstants;
import org.andengine.util.exception.AndEngineException;
import org.andengine.util.exception.MethodNotFoundException;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Debug.MemoryInfo;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 15:50:31 - 14.07.2010
 */
public final class SystemUtils {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final boolean SDK_VERSION_ECLAIR_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	public static final boolean SDK_VERSION_FROYO_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	public static final boolean SDK_VERSION_GINGERBREAD_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	public static final boolean SDK_VERSION_HONEYCOMB_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	public static final boolean SDK_VERSION_ICE_CREAM_SANDWICH_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;

	private static final String BOGOMIPS_PATTERN = "BogoMIPS[\\s]*:[\\s]*(\\d+\\.\\d+)[\\s]*\n";
	private static final String MEMTOTAL_PATTERN = "MemTotal[\\s]*:[\\s]*(\\d+)[\\s]*kB\n";
	private static final String MEMFREE_PATTERN = "MemFree[\\s]*:[\\s]*(\\d+)[\\s]*kB\n";

	// ===========================================================
	// Fields
	// ===========================================================

	private static MemoryInfo sMemoryInfo;

	// ===========================================================
	// Constructors
	// ===========================================================

	private SystemUtils() {

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

	public static boolean isEmulator() {
		if (Build.PRODUCT != null && Build.PRODUCT.equals("google_sdk")) {
			return true;
		} else if (Build.MODEL != null && Build.MODEL.equals("google_sdk")) {
			return true;
		} else if (Build.BRAND != null && Build.BRAND.startsWith("generic")){
			return true;
		} else if (Build.DEVICE != null && Build.DEVICE.startsWith("generic")) {
			return true;
		} else {
			return false;
		}
	}

	public static MemoryInfo getMemoryInfo() {
		/* Lazy allocation. */
		if (SystemUtils.sMemoryInfo == null) {
			SystemUtils.sMemoryInfo = new MemoryInfo();
		}

		Debug.getMemoryInfo(SystemUtils.sMemoryInfo);

		return SystemUtils.sMemoryInfo;
	}

	public static boolean isGoogleTV(final Context pContext) throws SystemUtilsException {
		return SystemUtils.hasSystemFeature(pContext, "com.google.android.tv");
	}

	public static boolean isGoogleTV(final Context pContext, final boolean pDefault) {
		try {
			return SystemUtils.isGoogleTV(pContext);
		} catch (final SystemUtilsException e) {
			return pDefault;
		}
	}

	public static boolean hasCamera(final Context pContext) throws SystemUtilsException {
		return SystemUtils.hasSystemFeature(pContext, PackageManager.FEATURE_CAMERA);
	}

	public static boolean hasCamera(final Context pContext, final boolean pDefault) {
		try {
			return SystemUtils.hasCamera(pContext);
		} catch (final SystemUtilsException e) {
			return pDefault;
		}
	}

	public static boolean isNDKSupported(final Context pContext, final boolean pDefault) {
		try {
			if (SystemUtils.isGoogleTV(pContext)) {
				if (SystemUtils.isAndroidVersionOrHigher(Build.VERSION_CODES.JELLY_BEAN_MR1)) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} catch (final SystemUtilsException e) {
			return pDefault;
		}
	}

	public static String getApplicationLabel(final Context pContext) throws SystemUtilsException {
	    final int labelResID = SystemUtils.getApplicationInfo(pContext).labelRes;
	    return pContext.getString(labelResID);
	}

	public static int getPackageVersionCode(final Context pContext) throws SystemUtilsException {
		return SystemUtils.getPackageInfo(pContext).versionCode;
	}

	public static String getPackageVersionName(final Context pContext) throws SystemUtilsException {
		return SystemUtils.getPackageInfo(pContext).versionName;
	}

	public static String getPackageName(final Context pContext) {
		return pContext.getPackageName();
	}

	public static String getApkFilePath(final Context pContext) throws SystemUtilsException {
		return SystemUtils.getApplicationInfo(pContext, 0).sourceDir;
	}

	public static ApplicationInfo getApplicationInfo(final Context pContext) throws SystemUtilsException {
		return SystemUtils.getApplicationInfo(pContext, 0);
	}

	public static ApplicationInfo getApplicationInfo(final Context pContext, final int pFlags) throws SystemUtilsException {
		try {
			return pContext.getPackageManager().getApplicationInfo(pContext.getPackageName(), pFlags);
		} catch (final NameNotFoundException e) {
			throw new SystemUtilsException(e);
		}
	}

	public static PackageInfo getPackageInfo(final Context pContext) throws SystemUtilsException {
		return SystemUtils.getPackageInfo(pContext, 0);
	}

	public static PackageInfo getPackageInfo(final Context pContext, final int pFlags) throws SystemUtilsException {
		try {
			return pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), pFlags);
		} catch (final NameNotFoundException e) {
			throw new SystemUtilsException(e);
		}
	}

	public static int getTargetSDKVersion(final Context pContext) throws SystemUtilsException {
		return SystemUtils.getApplicationInfo(pContext).targetSdkVersion;
	}

	public static boolean hasSystemFeature(final Context pContext, final String pFeature) throws SystemUtilsException {
		final PackageManager packageManager = pContext.getPackageManager();
		try {
			try {
				return packageManager.hasSystemFeature(pFeature);
			} catch (final Throwable t) {
				final Method PackageManager_hasSystemFeatures = PackageManager.class.getMethod("hasSystemFeature", String.class);
				if (PackageManager_hasSystemFeatures == null) {
					throw new SystemUtilsException(new MethodNotFoundException(PackageManager.class.getSimpleName() + ".hasSystemFeature(String)"));
				} else {
					final boolean result = (Boolean) PackageManager_hasSystemFeatures.invoke(packageManager, pFeature);
					return result;
				}
			}
		} catch (final Throwable t) {
			throw new SystemUtilsException(t);
		}
	}

	public static boolean hasSystemFeature(final Context pContext, final String pFeature, final boolean pDefault) {
		try {
			return SystemUtils.hasSystemFeature(pContext, pFeature);
		} catch (final SystemUtilsException e) {
			return pDefault;
		}
	}

	public static boolean optMetaDataBoolean(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getBoolean(pKey);
	}

	public static boolean optMetaDataBoolean(final Context pContext, final String pKey, final boolean pDefaultValue) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getBoolean(pKey, pDefaultValue);
	}

	public static int optMetaDataInt(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getInt(pKey);
	}

	public static int optMetaDataInt(final Context pContext, final String pKey, final int pDefaultValue) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getInt(pKey, pDefaultValue);
	}

	public static float optMetaDataFloat(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getFloat(pKey);
	}

	public static float optMetaDataFloat(final Context pContext, final String pKey, final float pDefaultValue) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getFloat(pKey, pDefaultValue);
	}

	public static String optMetaDataString(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return bundle.getString(pKey);
	}

	public static String optMetaDataString(final Context pContext, final String pKey, final String pDefaultValue) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getString(pKey);
		} else {
			return pDefaultValue;
		}
	}

	public static int optMetaDataColor(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		return Color.parseColor(bundle.getString(pKey));
	}

	public static int optMetaDataColor(final Context pContext, final String pKey, final int pDefaultValue) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return Color.parseColor(bundle.getString(pKey));
		} else {
			return pDefaultValue;
		}
	}

	public static boolean getMetaDataBoolean(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getBoolean(pKey);
		} else {
			throw new SystemUtilsException(new IllegalArgumentException("Could not find meta data with key: '" + pKey + "'."));
		}
	}

	public static int getMetaDataInt(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getInt(pKey);
		} else {
			throw new SystemUtilsException(new IllegalArgumentException("Could not find meta data with key: '" + pKey + "'."));
		}
	}

	public static float getMetaDataFloat(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getFloat(pKey);
		} else {
			throw new SystemUtilsException(new IllegalArgumentException("Could not find meta data with key: '" + pKey + "'."));
		}
	}

	public static String getMetaDataString(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return bundle.getString(pKey);
		} else {
			throw new SystemUtilsException(new IllegalArgumentException("Could not find meta data with key: '" + pKey + "'."));
		}
	}

	public static int getMetaDataColor(final Context pContext, final String pKey) throws SystemUtilsException {
		final Bundle bundle = SystemUtils.getMetaData(pContext);
		if (bundle.containsKey(pKey)) {
			return Color.parseColor(bundle.getString(pKey));
		} else {
			throw new SystemUtilsException(new IllegalArgumentException("Could not find meta data with key: '" + pKey + "'."));
		}
	}

	public static Bundle getMetaData(final Context pContext) throws SystemUtilsException {
		try {
			final ApplicationInfo applicationInfo = SystemUtils.getApplicationInfo(pContext, PackageManager.GET_META_DATA);
			return applicationInfo.metaData;
		} catch (final Throwable t) {
			throw new SystemUtilsException(t);
		}
	}

	/**
	 * @param pBuildVersionCode taken from {@link Build.VERSION_CODES}.
	 */
	public static boolean isAndroidVersionOrLower(final int pBuildVersionCode) {
		return Build.VERSION.SDK_INT <= pBuildVersionCode;
	}

	/**
	 * @param pBuildVersionCode taken from {@link Build.VERSION_CODES}.
	 */
	public static boolean isAndroidVersionOrHigher(final int pBuildVersionCode) {
		return Build.VERSION.SDK_INT >= pBuildVersionCode;
	}

	/**
	 * @param pBuildVersionCodeMin taken from {@link Build.VERSION_CODES}.
	 * @param pBuildVersionCodeMax taken from {@link Build.VERSION_CODES}.
	 */
	public static boolean isAndroidVersion(final int pBuildVersionCodeMin, final int pBuildVersionCodeMax) {
		return (Build.VERSION.SDK_INT >= pBuildVersionCodeMin) && (Build.VERSION.SDK_INT <= pBuildVersionCodeMax);
	}

	public static float getCPUBogoMips() throws SystemUtilsException {
		final MatchResult matchResult = SystemUtils.matchSystemFile("/proc/cpuinfo", SystemUtils.BOGOMIPS_PATTERN, 1000);

		try {
			if (matchResult.groupCount() > 0) {
				return Float.parseFloat(matchResult.group(1));
			} else {
				throw new SystemUtilsException();
			}
		} catch (final NumberFormatException e) {
			throw new SystemUtilsException(e);
		}
	}

	/**
	 * @return in kiloBytes.
	 * @throws SystemUtilsException
	 */
	public static long getSystemMemorySize() throws SystemUtilsException {
		final MatchResult matchResult = SystemUtils.matchSystemFile("/proc/meminfo", SystemUtils.MEMTOTAL_PATTERN, 1000);

		try {
			if (matchResult.groupCount() > 0) {
				return Long.parseLong(matchResult.group(1));
			} else {
				throw new SystemUtilsException();
			}
		} catch (final NumberFormatException e) {
			throw new SystemUtilsException(e);
		}
	}

	/**
	 * @return in kiloBytes.
	 * @throws SystemUtilsException
	 */
	public static long getSystemMemoryFreeSize() throws SystemUtilsException {
		final MatchResult matchResult = SystemUtils.matchSystemFile("/proc/meminfo", SystemUtils.MEMFREE_PATTERN, 1000);

		try {
			if (matchResult.groupCount() > 0) {
				return Long.parseLong(matchResult.group(1));
			} else {
				throw new SystemUtilsException();
			}
		} catch (final NumberFormatException e) {
			throw new SystemUtilsException(e);
		}
	}

	/**
	 * @return in kiloHertz.
	 * @throws SystemUtilsException
	 */
	public static long getCPUFrequencyCurrent() throws SystemUtilsException {
		return SystemUtils.readSystemFileAsLong("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
	}

	/**
	 * @return in kiloHertz.
	 * @throws SystemUtilsException
	 */
	public static long getCPUFrequencyMin() throws SystemUtilsException {
		return SystemUtils.readSystemFileAsLong("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq");
	}

	/**
	 * @return in kiloHertz.
	 * @throws SystemUtilsException
	 */
	public static long getCPUFrequencyMax() throws SystemUtilsException {
		return SystemUtils.readSystemFileAsLong("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
	}

	/**
	 * @return in kiloHertz.
	 * @throws SystemUtilsException
	 */
	public static long getCPUFrequencyMinScaling() throws SystemUtilsException {
		return SystemUtils.readSystemFileAsLong("/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq");
	}

	/**
	 * @return in kiloHertz.
	 * @throws SystemUtilsException
	 */
	public static long getCPUFrequencyMaxScaling() throws SystemUtilsException {
		return SystemUtils.readSystemFileAsLong("/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq");
	}

	private static MatchResult matchSystemFile(final String pSystemFile, final String pPattern, final int pHorizon) throws SystemUtilsException {
		InputStream in = null;
		try {
			final Process process = new ProcessBuilder(new String[] {"/system/bin/cat", pSystemFile}).start();

			in = process.getInputStream();
			final Scanner scanner = new Scanner(in);

			final boolean matchFound = scanner.findWithinHorizon(pPattern, pHorizon) != null;
			if (matchFound) {
				return scanner.match();
			} else {
				throw new SystemUtilsException();
			}
		} catch (final IOException e) {
			throw new SystemUtilsException(e);
		} finally {
			StreamUtils.close(in);
		}
	}

	private static long readSystemFileAsLong(final String pSystemFile) throws SystemUtilsException {
		InputStream in = null;
		try {
			final Process process = new ProcessBuilder(new String[] {"/system/bin/cat", pSystemFile}).start();

			in = process.getInputStream();
			final String content = StreamUtils.readFully(in);
			return Long.parseLong(content);
		} catch (final IOException e) {
			throw new SystemUtilsException(e);
		} catch (final NumberFormatException e) {
			throw new SystemUtilsException(e);
		} finally {
			StreamUtils.close(in);
		}
	}

	public static long getNativeHeapSize() {
		return Debug.getNativeHeapSize() / DataConstants.BYTES_PER_KILOBYTE;
	}

	public static long getNativeHeapFreeSize() {
		return Debug.getNativeHeapFreeSize() / DataConstants.BYTES_PER_KILOBYTE;
	}

	public static long getNativeHeapAllocatedSize() {
		return Debug.getNativeHeapAllocatedSize() / DataConstants.BYTES_PER_KILOBYTE;
	}

	public static long getDalvikHeapSize() {
		return Runtime.getRuntime().totalMemory() / DataConstants.BYTES_PER_KILOBYTE;
	}

	public static long getDalvikHeapFreeSize() {
		return Runtime.getRuntime().freeMemory() / DataConstants.BYTES_PER_KILOBYTE;
	}

	public static long getDalvikHeapAllocatedSize() {
		return SystemUtils.getDalvikHeapSize() - SystemUtils.getDalvikHeapFreeSize();
	}

	public static long getNativeProportionalSetSize() {
		return SystemUtils.getMemoryInfo().nativePss;
	}

	public static long getNativePrivateDirtyPages() {
		return SystemUtils.getMemoryInfo().nativePrivateDirty;
	}

	public static long getNativeSharedDirtyPages() {
		return SystemUtils.getMemoryInfo().nativeSharedDirty;
	}

	public static long getDalvikProportionalSetSize() {
		return SystemUtils.getMemoryInfo().dalvikPss;
	}

	public static long getDalvikPrivateDirtyPages() {
		return SystemUtils.getMemoryInfo().dalvikPrivateDirty;
	}

	public static long getDalvikSharedDirtyPages() {
		return SystemUtils.getMemoryInfo().dalvikSharedDirty;
	}

	public static long getOtherProportionalSetSize() {
		return SystemUtils.getMemoryInfo().otherPss;
	}

	public static long getOtherPrivateDirtyPages() {
		return SystemUtils.getMemoryInfo().otherPrivateDirty;
	}

	public static long getOtherSharedDirtyPages() {
		return SystemUtils.getMemoryInfo().otherSharedDirty;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class SystemUtilsException extends AndEngineException {
		// ===========================================================
		// Constants
		// ===========================================================

		private static final long serialVersionUID = -7256483361095147596L;

		// ===========================================================
		// Constructors
		// ===========================================================

		public SystemUtilsException() {

		}

		public SystemUtilsException(final Throwable pThrowable) {
			super(pThrowable);
		}
	}
}
