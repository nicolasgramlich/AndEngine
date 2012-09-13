package org.andengine.opengl;

import org.andengine.util.exception.AndEngineRuntimeException;
import org.andengine.util.system.SystemUtils;

import android.opengl.GLES20;
import android.os.Build;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:44:43 - 04.09.2011
 */
public class GLES20Fix {
	// ===========================================================
	// Constants
	// ===========================================================

	private static boolean NATIVE_LIB_LOADED;

	/** Android issue 8931. */
	private static final boolean WORKAROUND_MISSING_GLES20_METHODS;

	/** Android Issue 9953. */
	private static final boolean WORKAROUND_EMPTY_SHADER_INFO_LOG;

	static {
		boolean loadLibrarySuccess;
		try {
			System.loadLibrary("andengine");
			loadLibrarySuccess = true;
		} catch (final UnsatisfiedLinkError e) {
			loadLibrarySuccess = false;
		}
		NATIVE_LIB_LOADED = loadLibrarySuccess;

		if(SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.FROYO)) {
			if(loadLibrarySuccess) {
				WORKAROUND_MISSING_GLES20_METHODS = true;
			} else {
				throw new AndEngineRuntimeException("Inherently incompatible device detected.");
			}
		} else {
			WORKAROUND_MISSING_GLES20_METHODS = false;
		}

		if(SystemUtils.isAndroidVersionOrLower(Build.VERSION_CODES.GINGERBREAD_MR1)) {
			if(loadLibrarySuccess) {
				WORKAROUND_EMPTY_SHADER_INFO_LOG = true;
			} else {
				WORKAROUND_EMPTY_SHADER_INFO_LOG = false;
			}
		} else {
			WORKAROUND_EMPTY_SHADER_INFO_LOG = false;
		}
	}

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	private GLES20Fix() {

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

	public static native void glVertexAttribPointer(final int pIndex, final int pSize, final int pType, final boolean pNormalized, final int pStride, final int pOffset);
	public static native void glDrawElements(final int pMode, final int pCount, final int pType, final int pOffset);
	public static native String glGetShaderInfoLog(final int pShaderID);

	public static String glGetShaderInfoLogFix(final int pShaderID) {
		if(GLES20Fix.WORKAROUND_EMPTY_SHADER_INFO_LOG) {
			return GLES20Fix.glGetShaderInfoLog(pShaderID);
		} else {
			return GLES20.glGetShaderInfoLog(pShaderID);
		}
	}

	public static void glVertexAttribPointerFix(final int pIndex, final int pSize, final int pType, final boolean pNormalized, final int pStride, final int pOffset) {
		if(GLES20Fix.WORKAROUND_MISSING_GLES20_METHODS) {
			GLES20Fix.glVertexAttribPointerFix(pIndex, pSize, pType, pNormalized, pStride, pOffset);
		} else {
			GLES20.glVertexAttribPointer(pIndex, pSize, pType, pNormalized, pStride, pOffset);
		}
	}

	public static void glDrawElementsFix(final int pMode, final int pCount, final int pType, final int pOffset) {
		if(GLES20Fix.WORKAROUND_MISSING_GLES20_METHODS) {
			GLES20Fix.glDrawElements(pMode, pCount, pType, pOffset);
		} else {
			GLES20.glDrawElements(pMode, pCount, pType, pOffset);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
