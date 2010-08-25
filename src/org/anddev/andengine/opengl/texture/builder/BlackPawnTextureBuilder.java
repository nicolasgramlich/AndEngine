package org.anddev.andengine.opengl.texture.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.BuildableTexture.TextureSourceWithWithLocationCallback;
import org.anddev.andengine.opengl.texture.Texture.TextureSourceWithLocation;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 16:03:01 - 12.08.2010
 * @see http://www.blackpawn.com/texts/lightmaps/default.html
 */
public class BlackPawnTextureBuilder implements ITextureBuilder {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final Comparator<ITextureSource> TEXTURESOURCE_COMPARATOR = new Comparator<ITextureSource>() {
		@Override
		public int compare(final ITextureSource pTextureSourceA, final ITextureSource pTextureSourceB) {
			final int deltaWidth = pTextureSourceA.getWidth() - pTextureSourceB.getWidth();
			if(deltaWidth != 0) {
				return deltaWidth;
			} else {
				return pTextureSourceA.getHeight() - pTextureSourceB.getHeight();
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

	@SuppressWarnings("deprecation")
	@Override
	public void pack(final BuildableTexture pBuildableTexture, final ArrayList<TextureSourceWithWithLocationCallback> pTextureSourcesWithLocationCallback) throws IllegalArgumentException {
		Collections.sort(pTextureSourcesWithLocationCallback, TEXTURESOURCE_COMPARATOR);

		final Node root = new Node(new Rect(0, 0, pBuildableTexture.getWidth(), pBuildableTexture.getHeight()));

		final int textureSourceCount = pTextureSourcesWithLocationCallback.size();

		for(int i = 0; i < textureSourceCount; i++) {
			final TextureSourceWithWithLocationCallback textureSourceWithLocationCallback = pTextureSourcesWithLocationCallback.get(i);
			final ITextureSource textureSource = textureSourceWithLocationCallback.getTextureSource();

			final Node inserted = root.insert(textureSource, pBuildableTexture.getWidth(), pBuildableTexture.getHeight(), this.mTextureSourceSpacing);

			if(inserted == null) {
				throw new IllegalArgumentException("Could not pack: " + textureSource.toString());
			}
			final TextureSourceWithLocation textureSourceWithLocation = pBuildableTexture.addTextureSource(textureSource, inserted.mRect.mLeft, inserted.mRect.mTop);
			textureSourceWithLocationCallback.getCallback().onCallback(textureSourceWithLocation);
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
				
				final int textureSourceWidthWithSpacing = textureSourceWidth + pTextureSpacing;
				final int textureSourceHeightWithSpacing = textureSourceHeight + pTextureSpacing;

				final int rectWidth = this.mRect.getWidth();
				final int rectHeight = this.mRect.getHeight();

				final int rectLeft = this.mRect.getLeft();
				final int rectTop = this.mRect.getTop();
				
				if(textureSourceWidthWithSpacing == rectWidth){
					if(textureSourceHeightWithSpacing == rectHeight) { /* Normal case with padding. */
						this.mTextureSource = pTextureSource;
						return this;
					} else if(textureSourceHeight == rectHeight && rectTop + textureSourceHeight == pTextureHeight) { /* Bottom edge of the Texture. */
						this.mTextureSource = pTextureSource;
						return this;
					}
				} 
				
				if(textureSourceWidth == rectWidth && rectLeft + textureSourceWidth == pTextureWidth) { /* Right edge of the Texture. */
					if(textureSourceHeightWithSpacing == rectHeight) {
						this.mTextureSource = pTextureSource;
						return this;
					} else if(textureSourceHeight == rectHeight && rectTop + textureSourceHeight == pTextureHeight) { /* Bottom edge of the Texture. */
						this.mTextureSource = pTextureSource;
						return this;
					}
				} 
				
				if(textureSourceWidth + pTextureSpacing > rectWidth || textureSourceHeight + pTextureSpacing > rectHeight) {
					return null;
				}

				final int deltaWidth = rectWidth - textureSourceWidthWithSpacing;
				final int deltaHeight = rectHeight - textureSourceHeightWithSpacing;

				if(deltaWidth >= deltaHeight) {
					/* Split using a vertical axis. */
					this.mChildA = new Node(
							rectLeft,
							rectTop,
							textureSourceWidthWithSpacing,
							rectHeight
					);

					this.mChildB = new Node(
							rectLeft + textureSourceWidthWithSpacing,
							rectTop,
							rectWidth - textureSourceWidthWithSpacing,
							rectHeight
					);
				} else {
					/* Split using a horizontal axis. */
					this.mChildA = new Node(
							rectLeft,
							rectTop,
							rectWidth,
							textureSourceHeightWithSpacing
					);

					this.mChildB = new Node(
							rectLeft,
							rectTop + textureSourceHeightWithSpacing,
							rectWidth,
							rectHeight - textureSourceHeightWithSpacing
					);
				}

				return this.mChildA.insert(pTextureSource, pTextureWidth, pTextureHeight, pTextureSpacing);
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
