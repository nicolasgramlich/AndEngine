package org.anddev.andengine.opengl.texture.buildable.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.anddev.andengine.opengl.texture.ITextureAtlas;
import org.anddev.andengine.opengl.texture.buildable.BuildableTextureAtlas.TextureSourceWithWithLocationCallback;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @author Jim Scott (BlackPawn)
 * @since 16:03:01 - 12.08.2010
 * @see http://www.blackpawn.com/texts/lightmaps/default.html
 */
public class BlackPawnTextureBuilder<T extends ITextureSource, A extends ITextureAtlas<T>> implements ITextureBuilder<T, A> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final Comparator<TextureSourceWithWithLocationCallback<?>> TEXTURESOURCE_COMPARATOR = new Comparator<TextureSourceWithWithLocationCallback<?>>() {
		@Override
		public int compare(final TextureSourceWithWithLocationCallback<?> pTextureSourceWithWithLocationCallbackA, final TextureSourceWithWithLocationCallback<?> pTextureSourceWithWithLocationCallbackB) {
			final int deltaWidth = pTextureSourceWithWithLocationCallbackB.getTextureSource().getWidth() - pTextureSourceWithWithLocationCallbackA.getTextureSource().getWidth();
			if(deltaWidth != 0) {
				return deltaWidth;
			} else {
				return pTextureSourceWithWithLocationCallbackB.getTextureSource().getHeight() - pTextureSourceWithWithLocationCallbackA.getTextureSource().getHeight();
			}
		}
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mTextureSourceSpacing;

	// ===========================================================
	// Constructors
	// ===========================================================

	public BlackPawnTextureBuilder(final int pTextureSourceSpacing) {
		this.mTextureSourceSpacing = pTextureSourceSpacing;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void pack(final A pTextureAtlas, final ArrayList<TextureSourceWithWithLocationCallback<T>> pTextureSourcesWithLocationCallback) throws TextureSourcePackingException {
		Collections.sort(pTextureSourcesWithLocationCallback, TEXTURESOURCE_COMPARATOR);

		final Node root = new Node(new Rect(0, 0, pTextureAtlas.getWidth(), pTextureAtlas.getHeight()));

		final int textureSourceCount = pTextureSourcesWithLocationCallback.size();

		for(int i = 0; i < textureSourceCount; i++) {
			final TextureSourceWithWithLocationCallback<T> textureSourceWithLocationCallback = pTextureSourcesWithLocationCallback.get(i);
			final T textureSource = textureSourceWithLocationCallback.getTextureSource();

			final Node inserted = root.insert(textureSource, pTextureAtlas.getWidth(), pTextureAtlas.getHeight(), this.mTextureSourceSpacing);

			if(inserted == null) {
				throw new TextureSourcePackingException("Could not pack: " + textureSource.toString());
			}
			pTextureAtlas.addTextureSource(textureSource, inserted.mRect.mLeft, inserted.mRect.mTop);
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
		private ITextureSource mTextureSource;

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

		public Node insert(final ITextureSource pTextureSource, final int pTextureWidth, final int pTextureHeight, final int pTextureSpacing) throws IllegalArgumentException {
			if(this.mChildA != null && this.mChildB != null) {
				final Node newNode = this.mChildA.insert(pTextureSource, pTextureWidth, pTextureHeight, pTextureSpacing);
				if(newNode != null){
					return newNode;
				} else {
					return this.mChildB.insert(pTextureSource, pTextureWidth, pTextureHeight, pTextureSpacing);
				}
			} else {
				if(this.mTextureSource != null) {
					return null;
				}

				final int textureSourceWidth = pTextureSource.getWidth();
				final int textureSourceHeight = pTextureSource.getHeight();

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
						this.mTextureSource = pTextureSource;
						return this;
					} else if(fitToBottomWithoutSpacing) { /* Bottom edge of the BitmapTexture. */
						this.mTextureSource = pTextureSource;
						return this;
					}
				}

				if(fitToRightWithoutSpacing) { /* Right edge of the BitmapTexture. */
					if(textureSourceHeightWithSpacing == rectHeight) {
						this.mTextureSource = pTextureSource;
						return this;
					} else if(fitToBottomWithoutSpacing) { /* Bottom edge of the BitmapTexture. */
						this.mTextureSource = pTextureSource;
						return this;
					} else if(textureSourceHeightWithSpacing > rectHeight) {
						return null;
					} else {

						return this.createChildren(pTextureSource, pTextureWidth, pTextureHeight, pTextureSpacing, rectWidth - textureSourceWidth, rectHeight - textureSourceHeightWithSpacing);
					}
				}

				if(fitToBottomWithoutSpacing) {
					if(textureSourceWidthWithSpacing == rectWidth) {
						this.mTextureSource = pTextureSource;
						return this;
					} else if(textureSourceWidthWithSpacing > rectWidth) {
						return null;
					} else {
						return this.createChildren(pTextureSource, pTextureWidth, pTextureHeight, pTextureSpacing, rectWidth - textureSourceWidthWithSpacing, rectHeight - textureSourceHeight);
					}
				} else if(textureSourceWidthWithSpacing > rectWidth || textureSourceHeightWithSpacing > rectHeight) {
					return null;
				} else {
					return this.createChildren(pTextureSource, pTextureWidth, pTextureHeight, pTextureSpacing, rectWidth - textureSourceWidthWithSpacing, rectHeight - textureSourceHeightWithSpacing);
				}
			}
		}

		private Node createChildren(final ITextureSource pTextureSource, final int pTextureWidth, final int pTextureHeight, final int pTextureSpacing, final int pDeltaWidth, final int pDeltaHeight) {
			final Rect rect = this.mRect;

			if(pDeltaWidth >= pDeltaHeight) {
				/* Split using a vertical axis. */
				this.mChildA = new Node(
						rect.getLeft(),
						rect.getTop(),
						pTextureSource.getWidth() + pTextureSpacing,
						rect.getHeight()
				);

				this.mChildB = new Node(
						rect.getLeft() + (pTextureSource.getWidth() + pTextureSpacing),
						rect.getTop(),
						rect.getWidth() - (pTextureSource.getWidth() + pTextureSpacing),
						rect.getHeight()
				);
			} else {
				/* Split using a horizontal axis. */
				this.mChildA = new Node(
						rect.getLeft(),
						rect.getTop(),
						rect.getWidth(),
						pTextureSource.getHeight() + pTextureSpacing
				);

				this.mChildB = new Node(
						rect.getLeft(),
						rect.getTop() + (pTextureSource.getHeight() + pTextureSpacing),
						rect.getWidth(),
						rect.getHeight() - (pTextureSource.getHeight() + pTextureSpacing)
				);
			}

			return this.mChildA.insert(pTextureSource, pTextureWidth, pTextureHeight, pTextureSpacing);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
