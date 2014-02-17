package org.andengine;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import org.andengine.opengl.view.ConfigChooser;
import org.andengine.util.exception.DeviceNotSupportedException;
import org.andengine.util.exception.DeviceNotSupportedException.DeviceNotSupportedCause;
import org.andengine.util.system.SystemUtils;

import android.os.Build;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 14:03:59 - 19.03.2012
 */
public class AndEngine {
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

	public static boolean isDeviceSupported() {
		try {
			AndEngine.checkDeviceSupported();
			return true;
		} catch (final DeviceNotSupportedException e) {
			return false;
		}
	}

	public static void checkDeviceSupported() throws DeviceNotSupportedException {
		AndEngine.checkCodePathSupport();

		AndEngine.checkOpenGLSupport();
	}

	private static void checkCodePathSupport() throws DeviceNotSupportedException {
		if(SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.FROYO)) {
			try {
				System.loadLibrary("andengine");
			} catch (final UnsatisfiedLinkError e) {
				throw new DeviceNotSupportedException(DeviceNotSupportedCause.CODEPATH_INCOMPLETE, e);
			}
		}
	}

	private static void checkOpenGLSupport() throws DeviceNotSupportedException {
		AndEngine.checkEGLConfigChooserSupport();
	}

	private static void checkEGLConfigChooserSupport() throws DeviceNotSupportedException {
		/* Get an EGL instance. */
		final EGL10 egl = (EGL10) EGLContext.getEGL();

		/* Get to the default display. */
		final EGLDisplay eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

		/* We can now initialize EGL for that display. */
		final int[] version = new int[2];
		egl.eglInitialize(eglDisplay, version);

		final ConfigChooser configChooser = new ConfigChooser(false); // TODO Doesn't correlate to possible multisampling request in EngineOptions...

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
