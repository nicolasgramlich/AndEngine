package org.anddev.andengine.opengl.texture.atlas;

import java.util.ArrayList;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;
import org.anddev.andengine.util.MathUtils;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 14:22:55 - 14.07.2011
 */
public abstract class TextureAtlas<T extends ITextureAtlasSource> extends Texture implements ITextureAtlas<T> {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	protected final int mWidth;
	protected final int mHeight;

	protected final ArrayList<T> mTextureAtlasSources = new ArrayList<T>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextureAtlas(final int pWidth, final int pHeight, final PixelFormat pPixelFormat, final TextureOptions pTextureOptions, final ITextureAtlasStateListener<T> pTextureAtlasStateListener) {
		super(pPixelFormat, pTextureOptions, pTextureAtlasStateListener);
		
		if(!MathUtils.isPowerOfTwo(pWidth) || !MathUtils.isPowerOfTwo(pHeight)) { // TODO GLHelper.EXTENSIONS_NON_POWER_OF_TWO
			throw new IllegalArgumentException("pWidth and pHeight must be a power of 2!");
		}
		this.mWidth = pWidth;
		this.mHeight = pHeight;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	@Override
	public int getWidth() {
		return this.mWidth;
	}
	
	@Override
	public int getHeight() {
		return this.mHeight;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ITextureAtlasStateListener<T> getTextureStateListener() {
		return (ITextureAtlasStateListener<T>) super.getTextureStateListener();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		this.checkTextureAtlasSourcePosition(pTextureAtlasSource, pTexturePositionX, pTexturePositionY);
		pTextureAtlasSource.setTexturePositionX(pTexturePositionX);
		pTextureAtlasSource.setTexturePositionY(pTexturePositionY);
		this.mTextureAtlasSources.add(pTextureAtlasSource);
		this.mUpdateOnHardwareNeeded = true;
	}

	@Override
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTextureX, final int pTextureY, final int pTextureAtlasSourcePadding) throws IllegalArgumentException {
		this.addTextureAtlasSource(pTextureAtlasSource, pTextureX, pTextureY);

		if(pTextureAtlasSourcePadding > 0) {
			/* Left padding. */
			if(pTextureX >= pTextureAtlasSourcePadding) {
				this.addEmptyTextureAtlasSource(pTextureX - pTextureAtlasSourcePadding, pTextureY, pTextureAtlasSourcePadding, pTextureAtlasSource.getHeight());
			}

			/* Top padding. */
			if(pTextureY >= pTextureAtlasSourcePadding) {
				this.addEmptyTextureAtlasSource(pTextureX, pTextureY - pTextureAtlasSourcePadding, pTextureAtlasSource.getWidth(), pTextureAtlasSourcePadding);
			}

			/* Right padding. */
			if(pTextureX + pTextureAtlasSource.getWidth() - 1 + pTextureAtlasSourcePadding <= this.getWidth()) {
				this.addEmptyTextureAtlasSource(pTextureX + pTextureAtlasSource.getWidth(), pTextureY, pTextureAtlasSourcePadding, pTextureAtlasSource.getHeight());
			}

			/* Bottom padding. */
			if(pTextureY + pTextureAtlasSource.getHeight() - 1 + pTextureAtlasSourcePadding <= this.getHeight()) {
				this.addEmptyTextureAtlasSource(pTextureX, pTextureY + pTextureAtlasSource.getHeight(), pTextureAtlasSource.getWidth(), pTextureAtlasSourcePadding);
			}
		}
	}

	@Override
	public void removeTextureAtlasSource(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) {
		final ArrayList<T> textureSources = this.mTextureAtlasSources;
		for(int i = textureSources.size() - 1; i >= 0; i--) {
			final T textureSource = textureSources.get(i);
			if(textureSource == pTextureAtlasSource && textureSource.getTexturePositionX() == pTexturePositionX && textureSource.getTexturePositionY() == pTexturePositionY) {
				textureSources.remove(i);
				this.mUpdateOnHardwareNeeded = true;
				return;
			}
		}
	}

	@Override
	public void clearTextureAtlasSources() {
		this.mTextureAtlasSources.clear();
		this.mUpdateOnHardwareNeeded = true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void checkTextureAtlasSourcePosition(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		if(pTexturePositionX < 0) {
			throw new IllegalArgumentException("Illegal negative pTexturePositionX supplied: '" + pTexturePositionX + "'");
		} else if(pTexturePositionY < 0) {
			throw new IllegalArgumentException("Illegal negative pTexturePositionY supplied: '" + pTexturePositionY + "'");
		} else if(pTexturePositionX + pTextureAtlasSource.getWidth() > this.getWidth() || pTexturePositionY + pTextureAtlasSource.getHeight() > this.getHeight()) {
			throw new IllegalArgumentException("Supplied pTextureAtlasSource must not exceed bounds of Texture.");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
