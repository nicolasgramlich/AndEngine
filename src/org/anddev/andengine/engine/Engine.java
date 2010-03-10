package org.anddev.andengine.engine;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.Scene;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureManager;
import org.anddev.andengine.util.constants.TimeConstants;


/**
 * @author Nicolas Gramlich
 * @since 12:21:31 - 08.03.2010
 */
public class Engine {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final EngineOptions mEngineOptions;
	
	private Scene mScene;
	
	private TextureManager mTextureManager = new TextureManager();

	private long mLastTick;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public Engine(final EngineOptions pEngineOptions) {
		this.mEngineOptions = pEngineOptions;		
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setScene(final Scene pScene) {
		this.mScene = pScene;
	}
	
	public EngineOptions getEngineOptions() {
		return this.mEngineOptions;
	}

	public int getGameWidth() {
		return this.mEngineOptions.mGameWidth;
	}

	public int getGameHeight() {
		return this.mEngineOptions.mGameHeight;
	}	

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void onDrawFrame(final GL10 pGL) {
		this.mTextureManager.loadPendingTextureToHardware(pGL);
		final float secondsElapsed = getSecondsElapsed();
		
		if(this.mScene != null){
			this.mScene.onUpdate(secondsElapsed);
		}
		
		if(this.mScene != null){
			this.mScene.onDraw(pGL);
		}
	}

	private float getSecondsElapsed() {
		final long now = System.nanoTime();
		final float secondsElapsed = (float)(now  - this.mLastTick) / TimeConstants.NANOSECONDSPERSECOND;
		this.mLastTick = now;
		return secondsElapsed;
	}
	
	public void loadTexture(final Texture pTexture) {
		this.mTextureManager.addTexturePendingForBeingLoadedToHardware(pTexture);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
