package org.anddev.andengine.entity.sprite;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.entity.primitives.Rectangle;
import org.anddev.andengine.opengl.GLHelper;
import org.anddev.andengine.opengl.texture.TextureRegion;

/**
 * @author Nicolas Gramlich
 * @since 11:38:53 - 08.03.2010
 */
public abstract class BaseSprite extends Rectangle {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int BLENDFUNCTION_SOURCE_DEFAULT = GL10.GL_SRC_ALPHA;
	public static final int BLENDFUNCTION_DESTINATION_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA;

	// ===========================================================
	// Fields
	// ===========================================================

	protected TextureRegion mTextureRegion;

	protected int mSourceBlendFunction = BLENDFUNCTION_SOURCE_DEFAULT;
	protected int mDestinationBlendFunction = BLENDFUNCTION_DESTINATION_DEFAULT;

	private final ArrayList<ISpriteModifier> mSpriteModifiers = new ArrayList<ISpriteModifier>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseSprite(final float pX, final float pY, final float pWidth, final float pHeight, final TextureRegion pTextureRegion) {
		super(pX, pY, pWidth, pHeight);

		assert(pTextureRegion != null);
		this.mTextureRegion = pTextureRegion;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public TextureRegion getTextureRegion() {
		return this.mTextureRegion;
	}

	public void setTextureRegion(final TextureRegion pTextureRegion){
		this.mTextureRegion = pTextureRegion;
	}

	public void setBlendFunction(final int pSourceBlendFunction, final int pDestinationBlendFunction) {
		this.mSourceBlendFunction = pSourceBlendFunction;
		this.mDestinationBlendFunction = pDestinationBlendFunction;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	@Override
	public void reset() {
		super.reset();

		this.mSourceBlendFunction = BLENDFUNCTION_SOURCE_DEFAULT;
		this.mDestinationBlendFunction = BLENDFUNCTION_DESTINATION_DEFAULT;
		
		final ArrayList<ISpriteModifier> spriteModifiers = this.mSpriteModifiers;
		for(int i = spriteModifiers.size() - 1; i >= 0; i--) {
			spriteModifiers.get(i).reset();
		}
	}

	@Override
	protected void onInitDraw(final GL10 pGL) {
		super.onInitDraw(pGL);
		GLHelper.enableTextures(pGL);
		GLHelper.enableTexCoordArray(pGL);
		GLHelper.blendFunction(pGL, this.mSourceBlendFunction, this.mDestinationBlendFunction);
	}

	@Override
	protected void onPostTransformations(final GL10 pGL) {
		super.onPostTransformations(pGL);
		this.applyTexture(pGL);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		this.applySpriteModifiers(pSecondsElapsed);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void addSpriteModifier(final ISpriteModifier pSpriteModifier) {
		this.mSpriteModifiers.add(pSpriteModifier);
	}

	public void removeSpriteModifier(final ISpriteModifier pSpriteModifier) {
		this.mSpriteModifiers.remove(pSpriteModifier);
	}

	private void applySpriteModifiers(final float pSecondsElapsed) {
		final ArrayList<ISpriteModifier> spriteModifiers = this.mSpriteModifiers;
		final int spriteModifierCount = spriteModifiers.size();
		if(spriteModifierCount > 0) {
			for(int i = spriteModifierCount - 1; i >= 0; i--) {
				spriteModifiers.get(i).onUpdateSprite(pSecondsElapsed, this);
			}
		}
	}

	protected void applyTexture(final GL10 pGL) {
		GLHelper.bindTexture(pGL, this.mTextureRegion.getTexture().getHardwareTextureID());
		GLHelper.texCoordPointer(pGL, this.mTextureRegion.getTextureBuffer().getByteBuffer(), GL10.GL_FLOAT);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
