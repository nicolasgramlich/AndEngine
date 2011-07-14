package org.anddev.andengine.opengl.texture.buildable.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.anddev.andengine.opengl.texture.atlas.ITextureAtlas;
import org.anddev.andengine.opengl.texture.buildable.BuildableTextureAtlas.TextureAtlasSourceWithWithLocationCallback;
import org.anddev.andengine.opengl.texture.source.ITextureAtlasSource;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @author Jim Scott (BlackPawn)
 * @since 16:03:01 - 12.08.2010
 * @see http://www.blackpawn.com/texts/lightmaps/default.html
 */
public class BlackPawnTextureBuilder<T extends ITextureAtlasSource, A extends ITextureAtlas<T>> implements ITextureBuilder<T, A> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final Comparator<TextureAtlasSourceWithWithLocationCallback<?>> TEXTURESOURCE_COMPARATOR = new Comparator<TextureAtlasSourceWithWithLocationCallback<?>>() {
		@Override
		public int compare(final TextureAtlasSourceWithWithLocationCallback<?> pTextureAtlasSourceWithWithLocationCallbackA, final TextureAtlasSourceWithWithLocationCallback<?> pTextureAtlasSourceWithWithLocationCallbackB) {
			final int deltaWidth = pTextureAtlasSourceWithWithLocationCallbackB.getTextureAtlasSource().getWidth() - pTextureAtlasSourceWithWithLocationCallbackA.getTextureAtlasSource().getWidth();
			if(deltaWidth != 0) {
				return deltaWidth;
			} else {
				return pTextureAtlasSourceWithWithLocationCallbackB.getTextureAtlasSource().getHeight() - pTextureAtlasSourceWithWithLocationCallbackA.getTextureAtlasSource().getHeight();
			}
		}
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mTextureAtlasSourceSpacing;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BlackPawnTextureBuilder(final int pTextureAtlasSourceSpacing) {
		this.mTextureAtlasSourceSpacing = pTextureAtlasSourceSpacing;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void pack(final A pTextureAtlas, final ArrayList<TextureAtlasSourceWithWithLocationCallback<T>> pTextureAtlasSourcesWithLocationCallback) throws TextureAtlasSourcePackingException {
		Collections.sort(pTextureAtlasSourcesWithLocationCallback, TEXTURESOURCE_COMPARATOR);

		final Node root = new Node(new Rect(0, 0, pTextureAtlas.getWidth(), pTextureAtlas.getHeight()));

		final int textureSourceCount = pTextureAtlasSourcesWithLocationCallback.size();

		for(int i = 0; i < textureSourceCount; i++) {
			final TextureAtlasSourceWithWithLocationCallback<T> textureSourceWithLocationCallback = pTextureAtlasSourcesWithLocationCallback.get(i);
			final T textureSource = textureSourceWithLocationCallback.getTextureAtlasSource();

			final Node inserted = root.insert(textureSource, pTextureAtlas.getWidth(), pTextureAtlas.getHeight(), this.mTextureAtlasSourceSpacing);

			if(inserted == null) {
				throw new TextureAtlasSourcePackingException("Could not pack: " + textureSource.toString());
			}
			pTextureAtlas.addTextureAtlasSource(textureSource, inserted.mRect.mLeft, inserted.mRect.mTop);
			textureSourceWithLocationCallback.getCallback().onCallback(textureSource);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	protected static class Rect {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mLeft;
		private final int mTop;
		private final int mWidth;
		private final int mHeight;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Rect(final int pLeft, final int pTop, final int pWidth, final int pHeight) {
			this.mLeft = pLeft;
			this.mTop = pTop;
			this.mWidth = pWidth;
			this.mHeight = pHeight;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public int getWidth() {
			return this.mWidth;
		}

		public int getHeight() {
			return this.mHeight;
		}

		public int getLeft() {
			return this.mLeft;
		}

		public int getTop() {
			return this.mTop;
		}

		public int getRight() {
			return this.mLeft + this.mWidth;
		}

		public int getBottom() {
			return this.mTop + this.mHeight;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public String toString() {
			return "@: " + this.mLeft + "/" + this.mTop + " * " + this.mWidth + "x" + this.mHeight;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	protected static class Node {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private Node mChildA;
		private Node mChildB;
		private final Rect mRect;
		private ITextureAtlasSource mTextureAtlasSource;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Node(final int pLeft, final int pTop, final int pWidth, final int pHeight) {
			this(new Rect(pLeft, pTop, pWidth, pHeight));
		}

		public Node(final Rect pRect) {
			this.mRect = pRect;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public Rect getRect() {
			return this.mRect;
		}

		public Node getChildA() {
			return this.mChildA;
		}

		public Node getChildB() {
			return this.mChildB;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public Node insert(final ITextureAtlasSource pTextureAtlasSource, final int pTextureWidth, final int pTextureHeight, final int pTextureSpacing) throws IllegalArgumentException {
			if(this.mChildA != null && this.mChildB != null) {
				final Node newNode = this.mChildA.insert(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureSpacing);
				if(newNode != null){
					return newNode;
				} else {
					return this.mChildB.insert(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureSpacing);
				}
			} else {
				if(this.mTextureAtlasSource != null) {
					return null;
				}

				final int textureSourceWidth = pTextureAtlasSource.getWidth();
				final int textureSourceHeight = pTextureAtlasSource.getHeight();

				final int rectWidth = this.mRect.getWidth();
				final int rectHeight = this.mRect.getHeight();

				if(textureSourceWidth > rectWidth || textureSourceHeight > rectHeight) {
					return null;
				}

				final int textureSourceWidthWithSpacing = textureSourceWidth + pTextureSpacing;
				final int textureSourceHeightWithSpacing = textureSourceHeight + pTextureSpacing;

				final int rectLeft = this.mRect.getLeft();
				final int rectTop = this.mRect.getTop();

				final boolean fitToBottomWithoutSpacing = textureSourceHeight == rectHeight && rectTop + textureSourceHeight == pTextureHeight;
				final boolean fitToRightWithoutSpacing = textureSourceWidth == rectWidth && rectLeft + textureSourceWidth == pTextureWidth;

				if(textureSourceWidthWithSpacing == rectWidth){
					if(textureSourceHeightWithSpacing == rectHeight) { /* Normal case with padding. */
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					} else if(fitToBottomWithoutSpacing) { /* Bottom edge of the BitmapTexture. */
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					}
				}

				if(fitToRightWithoutSpacing) { /* Right edge of the BitmapTexture. */
					if(textureSourceHeightWithSpacing == rectHeight) {
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					} else if(fitToBottomWithoutSpacing) { /* Bottom edge of the BitmapTexture. */
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					} else if(textureSourceHeightWithSpacing > rectHeight) {
						return null;
					} else {

						return this.createChildren(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureSpacing, rectWidth - textureSourceWidth, rectHeight - textureSourceHeightWithSpacing);
					}
				}

				if(fitToBottomWithoutSpacing) {
					if(textureSourceWidthWithSpacing == rectWidth) {
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					} else if(textureSourceWidthWithSpacing > rectWidth) {
						return null;
					} else {
						return this.createChildren(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureSpacing, rectWidth - textureSourceWidthWithSpacing, rectHeight - textureSourceHeight);
					}
				} else if(textureSourceWidthWithSpacing > rectWidth || textureSourceHeightWithSpacing > rectHeight) {
					return null;
				} else {
					return this.createChildren(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureSpacing, rectWidth - textureSourceWidthWithSpacing, rectHeight - textureSourceHeightWithSpacing);
				}
			}
		}

		private Node createChildren(final ITextureAtlasSource pTextureAtlasSource, final int pTextureWidth, final int pTextureHeight, final int pTextureSpacing, final int pDeltaWidth, final int pDeltaHeight) {
			final Rect rect = this.mRect;

			if(pDeltaWidth >= pDeltaHeight) {
				/* Split using a vertical axis. */
				this.mChildA = new Node(
						rect.getLeft(),
						rect.getTop(),
						pTextureAtlasSource.getWidth() + pTextureSpacing,
						rect.getHeight()
				);

				this.mChildB = new Node(
						rect.getLeft() + (pTextureAtlasSource.getWidth() + pTextureSpacing),
						rect.getTop(),
						rect.getWidth() - (pTextureAtlasSource.getWidth() + pTextureSpacing),
						rect.getHeight()
				);
			} else {
				/* Split using a horizontal axis. */
				this.mChildA = new Node(
						rect.getLeft(),
						rect.getTop(),
						rect.getWidth(),
						pTextureAtlasSource.getHeight() + pTextureSpacing
				);

				this.mChildB = new Node(
						rect.getLeft(),
						rect.getTop() + (pTextureAtlasSource.getHeight() + pTextureSpacing),
						rect.getWidth(),
						rect.getHeight() - (pTextureAtlasSource.getHeight() + pTextureSpacing)
				);
			}

			return this.mChildA.insert(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureSpacing);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
