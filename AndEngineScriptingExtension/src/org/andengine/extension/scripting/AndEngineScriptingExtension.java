package org.andengine.extension.scripting;

import org.andengine.engine.Engine;
import org.andengine.extension.scripting.engine.EngineProxy;
import org.andengine.extension.scripting.entity.EntityProxy;
import org.andengine.extension.scripting.entity.primitive.RectangleProxy;
import org.andengine.extension.scripting.entity.scene.SceneProxy;
import org.andengine.extension.scripting.entity.shape.ShapeProxy;
import org.andengine.extension.scripting.entity.sprite.SpriteProxy;
import org.andengine.extension.scripting.input.touch.TouchEventProxy;
import org.andengine.extension.scripting.opengl.font.FontManagerProxy;
import org.andengine.extension.scripting.opengl.texture.TextureManagerProxy;
import org.andengine.extension.scripting.opengl.texture.TextureOptionsProxy;
import org.andengine.extension.scripting.opengl.texture.TextureProxy;
import org.andengine.extension.scripting.opengl.texture.bitmap.AssetBitmapTextureProxy;
import org.andengine.extension.scripting.opengl.texture.bitmap.BitmapTextureFormatProxy;
import org.andengine.extension.scripting.opengl.texture.bitmap.BitmapTextureProxy;
import org.andengine.extension.scripting.opengl.texture.region.TextureRegionProxy;
import org.andengine.extension.scripting.opengl.vbo.DrawTypeProxy;
import org.andengine.extension.scripting.opengl.vbo.VertexBufferObjectManagerProxy;

import android.content.Context;

/**
 * (c) Zynga 2012
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 17:01:34 - 28.02.2012
 */
public class AndEngineScriptingExtension {
	// ===========================================================
	// Constants
	// ===========================================================

	static {
		System.loadLibrary("gnustl_shared");
		System.loadLibrary("andenginescriptingextension");

		// TODO: Register globals (Context and Engine)
		// TODO: Native Bindings Generator
		// TODO: JS Bindings Generator
	}

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
	 * It is critical from which {@link Thread} this method is called!
	 *
	 * @param pContext
	 * @param pEngine
	 */
	public static void init(final Context pContext, final Engine pEngine) {
		AndEngineScriptingExtension.nativeInitClass();

		/* Setup. */
		ContextProxy.nativeInitClass();
		AssetManagerProxy.nativeInitClass();
		
		EngineProxy.nativeInitClass();

		TouchEventProxy.nativeInitClass();

		/* VBO. */
		DrawTypeProxy.nativeInitClass();
		VertexBufferObjectManagerProxy.nativeInitClass();

		/* Texture. */
		TextureManagerProxy.nativeInitClass();
		TextureProxy.nativeInitClass();
		BitmapTextureProxy.nativeInitClass();
		AssetBitmapTextureProxy.nativeInitClass();
		TextureRegionProxy.nativeInitClass();
		TextureOptionsProxy.nativeInitClass();
		BitmapTextureFormatProxy.nativeInitClass();

		/* Font. */
		FontManagerProxy.nativeInitClass();
//		FontProxy.nativeInitClass();

		/* Entity. */
		EntityProxy.nativeInitClass();
		ShapeProxy.nativeInitClass();
		RectangleProxy.nativeInitClass();
		SpriteProxy.nativeInitClass();
		SceneProxy.nativeInitClass();

		/* Actual init. */
		AndEngineScriptingExtension.nativeInit(pContext, pEngine);
	}

	public static native String getJavaScriptVMVersion();
	public static native int runScript(final String pCode);

	private static native void nativeInitClass();
	private static native void nativeInit(final Context pContext, final Engine pEngine);


	public static void attachCurrentThread() {
		AndEngineScriptingExtension.nativeAttachCurrentThread();
	}

	private static native void nativeAttachCurrentThread();

	public static void detachCurrentThread() {
		AndEngineScriptingExtension.nativeDetachCurrentThread();
	}

	private static native void nativeDetachCurrentThread();

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
