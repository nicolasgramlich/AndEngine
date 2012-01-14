package org.andengine.opengl.texture.atlas;

import java.util.ArrayList;

import org.andengine.opengl.texture.PixelFormat;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;

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

	@Deprecated
	@Override
	public boolean hasTextureStateListener() {
		return super.hasTextureStateListener();
	}

	@Override
	public boolean hasTextureAtlasStateListener() {
		return super.hasTextureStateListener();
	}

	/**
	 * @see {@link ITextureAtlas#setTextureStateListener(ITextureAtlasStateListener)}
	 */
	@Deprecated
	@Override
	public ITextureAtlasStateListener<T> getTextureStateListener() {
		return this.getTextureAtlasStateListener();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ITextureAtlasStateListener<T> getTextureAtlasStateListener() {
		return (ITextureAtlasStateListener<T>) super.getTextureStateListener();
	}

	/**
	 * @see {@link ITextureAtlas#setTextureStateListener(ITextureAtlasStateListener)}
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	@Override
	public void setTextureStateListener(final ITextureStateListener pTextureStateListener) {
		super.setTextureStateListener((ITextureAtlasStateListener<T>)pTextureStateListener);
	}

	@Override
	public void setTextureAtlasStateListener(final ITextureAtlasStateListener<T> pTextureAtlasStateListener) {
		super.setTextureStateListener(pTextureAtlasStateListener);
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public TextureAtlas<T> load(final TextureManager pTextureManager) {
		super.load(pTextureManager);

		return this;
	}

	@Override
	public TextureAtlas<T> unload(final TextureManager pTextureManager) {
		super.unload(pTextureManager);

		return this;
	}

	@Override
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY) throws IllegalArgumentException {
		this.checkTextureAtlasSourcePosition(pTextureAtlasSource, pTexturePositionX, pTexturePositionY);
		pTextureAtlasSource.setTexturePositionX(pTexturePositionX);
		pTextureAtlasSource.setTexturePositionY(pTexturePositionY);
		this.mTextureAtlasSources.add(pTextureAtlasSource);
		this.mUpdateOnHardwareNeeded = true;
	}

	@Override
	public void addTextureAtlasSource(final T pTextureAtlasSource, final int pTexturePositionX, final int pTexturePositionY, final int pTextureAtlasSourcePadding) throws IllegalArgumentException {
		this.addTextureAtlasSource(pTextureAtlasSource, pTexturePositionX, pTexturePositionY);

		if(pTextureAtlasSourcePadding > 0) {
			/* Left padding. */
			if(pTexturePositionX >= pTextureAtlasSourcePadding) {
				this.addEmptyTextureAtlasSource(pTexturePositionX - pTextureAtlasSourcePadding, pTexturePositionY, pTextureAtlasSourcePadding, pTextureAtlasSource.getHeight());
			}

			/* Top padding. */
			if(pTexturePositionY >= pTextureAtlasSourcePadding) {
				this.addEmptyTextureAtlasSource(pTexturePositionX, pTexturePositionY - pTextureAtlasSourcePadding, pTextureAtlasSource.getWidth(), pTextureAtlasSourcePadding);
			}

			/* Right padding. */
			if(pTexturePositionX + pTextureAtlasSource.getWidth() - 1 + pTextureAtlasSourcePadding <= this.getWidth()) {
				this.addEmptyTextureAtlasSource(pTexturePositionX + pTextureAtlasSource.getWidth(), pTexturePositionY, pTextureAtlasSourcePadding, pTextureAtlasSource.getHeight());
			}

			/* Bottom padding. */
			if(pTexturePositionY + pTextureAtlasSource.getHeight() - 1 + pTextureAtlasSourcePadding <= this.getHeight()) {
				this.addEmptyTextureAtlasSource(pTexturePositionX, pTexturePositionY + pTextureAtlasSource.getHeight(), pTextureAtlasSource.getWidth(), pTextureAtlasSourcePadding);
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
