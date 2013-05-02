package org.andengine;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import org.andengine.engine.options.ConfigChooserOptions;
import org.andengine.opengl.view.ConfigChooser;
import org.andengine.util.exception.DeviceNotSupportedException;
import org.andengine.util.exception.DeviceNotSupportedException.DeviceNotSupportedCause;
import org.andengine.util.system.SystemUtils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Build;

/**
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:03:59 - 19.03.2012
 */
public final class AndEngine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private AndEngine() {

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

	public static boolean isDeviceSupported(final Context pContext) {
		try {
			AndEngine.checkDeviceSupported(pContext);
			return true;
		} catch (final DeviceNotSupportedException e) {
			return false;
		}
	}

	public static void checkDeviceSupported(final Context pContext) throws DeviceNotSupportedException {
		AndEngine.checkCodePathSupport();

		AndEngine.checkOpenGLSupport(pContext);
	}

	private static void checkCodePathSupport() throws DeviceNotSupportedException {
		if (SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.FROYO)) {
			try {
				System.loadLibrary("andengine");
			} catch (final UnsatisfiedLinkError e) {
				throw new DeviceNotSupportedException(DeviceNotSupportedCause.CODEPATH_INCOMPLETE, e);
			}
		}
	}

	private static void checkOpenGLSupport(final Context pContext) throws DeviceNotSupportedException {
		AndEngine.checkGLES20Support(pContext);
		AndEngine.checkEGLConfigChooserSupport();
	}

	private static void checkGLES20Support(final Context pContext) throws DeviceNotSupportedException {
		final ActivityManager activityManager = (ActivityManager) pContext.getSystemService(Context.ACTIVITY_SERVICE);

		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

		if (configurationInfo.reqGlEsVersion < 0x20000) {
			throw new DeviceNotSupportedException(DeviceNotSupportedCause.GLES2_UNSUPPORTED);
		}
	}

	private static void checkEGLConfigChooserSupport() throws DeviceNotSupportedException {
		/* Get an EGL instance. */
		final EGL10 egl = (EGL10) EGLContext.getEGL();

		/* Get to the default display. */
		final EGLDisplay eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

		/* We can now initialize EGL for that display. */
		final int[] version = new int[2];
		egl.eglInitialize(eglDisplay, version);

		final ConfigChooser configChooser = new ConfigChooser(new ConfigChooserOptions());

		try {
			configChooser.chooseConfig(egl, eglDisplay);
		} catch (final IllegalArgumentException e) {
			throw new DeviceNotSupportedException(DeviceNotSupportedCause.EGLCONFIG_NOT_FOUND, e);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
