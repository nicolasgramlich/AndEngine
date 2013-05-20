package org.andengine.opengl.texture.atlas.buildable.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.andengine.opengl.texture.atlas.ITextureAtlas;
import org.andengine.opengl.texture.atlas.buildable.BuildableTextureAtlas.TextureAtlasSourceWithWithLocationCallback;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @author Jim Scott (BlackPawn)
 * @since 16:03:01 - 12.08.2010
 *
 * @see <a href="http://www.blackpawn.com/texts/lightmaps/default.html">blackpawn.com/texts/lightmaps/default.html</a>
 */
public class BlackPawnTextureAtlasBuilder<T extends ITextureAtlasSource, A extends ITextureAtlas<T>> implements ITextureAtlasBuilder<T, A> {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final Comparator<TextureAtlasSourceWithWithLocationCallback<?>> TEXTURESOURCE_COMPARATOR = new Comparator<TextureAtlasSourceWithWithLocationCallback<?>>() {
		@Override
		public int compare(final TextureAtlasSourceWithWithLocationCallback<?> pTextureAtlasSourceWithWithLocationCallbackA, final TextureAtlasSourceWithWithLocationCallback<?> pTextureAtlasSourceWithWithLocationCallbackB) {
			final int deltaWidth = pTextureAtlasSourceWithWithLocationCallbackB.getTextureAtlasSource().getTextureWidth() - pTextureAtlasSourceWithWithLocationCallbackA.getTextureAtlasSource().getTextureWidth();
			if (deltaWidth != 0) {
				return deltaWidth;
			} else {
				return pTextureAtlasSourceWithWithLocationCallbackB.getTextureAtlasSource().getTextureHeight() - pTextureAtlasSourceWithWithLocationCallbackA.getTextureAtlasSource().getTextureHeight();
			}
		}
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final int mTextureAtlasBorderSpacing;
	private final int mTextureAtlasSourceSpacing;
	private final int mTextureAtlasSourcePadding;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * @param pTextureAtlasBorderSpacing the minimum spacing between the border of the texture and the {@link ITextureAtlasSource}s.
	 * @param pTextureAtlasSourceSpacing the spacing between the different {@link ITextureAtlasSource}s.
	 * @param pTextureAtlasSourcePadding the transparent padding around each {@link ITextureAtlasSource} (prevents texture bleeding).
	 */
	public BlackPawnTextureAtlasBuilder(final int pTextureAtlasBorderSpacing, final int pTextureAtlasSourceSpacing, final int pTextureAtlasSourcePadding) {
		this.mTextureAtlasBorderSpacing = pTextureAtlasBorderSpacing;
		this.mTextureAtlasSourceSpacing = pTextureAtlasSourceSpacing;
		this.mTextureAtlasSourcePadding = pTextureAtlasSourcePadding;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void build(final A pTextureAtlas, final ArrayList<TextureAtlasSourceWithWithLocationCallback<T>> pTextureAtlasSourcesWithLocationCallback) throws TextureAtlasBuilderException {
		Collections.sort(pTextureAtlasSourcesWithLocationCallback, TEXTURESOURCE_COMPARATOR);

		final int rootX = 0;
		final int rootY = 0;
		final int rootWidth = pTextureAtlas.getWidth() - 2 * this.mTextureAtlasBorderSpacing;
		final int rootHeight = pTextureAtlas.getHeight() - 2 * this.mTextureAtlasBorderSpacing;
		final Node root = new Node(new Rect(rootX, rootY, rootWidth, rootHeight));

		final int textureSourceCount = pTextureAtlasSourcesWithLocationCallback.size();

		for (int i = 0; i < textureSourceCount; i++) {
			final TextureAtlasSourceWithWithLocationCallback<T> textureSourceWithLocationCallback = pTextureAtlasSourcesWithLocationCallback.get(i);
			final T textureAtlasSource = textureSourceWithLocationCallback.getTextureAtlasSource();

			final Node inserted = root.insert(textureAtlasSource, rootWidth, rootHeight, this.mTextureAtlasSourceSpacing, this.mTextureAtlasSourcePadding);

			if (inserted == null) {
				throw new TextureAtlasBuilderException("Could not build: '" + textureAtlasSource.toString() + "' into: '" + pTextureAtlas.getClass().getSimpleName() + "'.");
			}

			final int textureAtlasSourceLeft = inserted.mRect.mLeft + this.mTextureAtlasBorderSpacing + this.mTextureAtlasSourcePadding;
			final int textureAtlasSourceTop = inserted.mRect.mTop + this.mTextureAtlasBorderSpacing + this.mTextureAtlasSourcePadding;
			if (this.mTextureAtlasSourcePadding == 0) {
				pTextureAtlas.addTextureAtlasSource(textureAtlasSource, textureAtlasSourceLeft, textureAtlasSourceTop);
			} else {
				pTextureAtlas.addTextureAtlasSource(textureAtlasSource, textureAtlasSourceLeft, textureAtlasSourceTop, this.mTextureAtlasSourcePadding);
			}

			textureSourceWithLocationCallback.getCallback().onCallback(textureAtlasSource);
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

		public Node insert(final ITextureAtlasSource pTextureAtlasSource, final int pTextureWidth, final int pTextureHeight, final int pTextureAtlasSourceSpacing, final int pTextureAtlasSourcePadding) throws IllegalArgumentException {
			if (this.mChildA != null && this.mChildB != null) {
				final Node newNode = this.mChildA.insert(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureAtlasSourceSpacing, pTextureAtlasSourcePadding);
				if (newNode != null) {
					return newNode;
				} else {
					return this.mChildB.insert(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureAtlasSourceSpacing, pTextureAtlasSourcePadding);
				}
			} else {
				if (this.mTextureAtlasSource != null) {
					return null;
				}

				final int textureSourceWidth = pTextureAtlasSource.getTextureWidth() + 2 * pTextureAtlasSourcePadding;
				final int textureSourceHeight = pTextureAtlasSource.getTextureHeight() + 2 * pTextureAtlasSourcePadding;

				final int rectWidth = this.mRect.getWidth();
				final int rectHeight = this.mRect.getHeight();

				if (textureSourceWidth > rectWidth || textureSourceHeight > rectHeight) {
					return null;
				}

				final int textureSourceWidthWithSpacing = textureSourceWidth + pTextureAtlasSourceSpacing;
				final int textureSourceHeightWithSpacing = textureSourceHeight + pTextureAtlasSourceSpacing;

				final int rectLeft = this.mRect.getLeft();
				final int rectTop = this.mRect.getTop();

				final boolean fitToBottomWithoutSpacing = textureSourceHeight == rectHeight && rectTop + textureSourceHeight == pTextureHeight;
				final boolean fitToRightWithoutSpacing = textureSourceWidth == rectWidth && rectLeft + textureSourceWidth == pTextureWidth;

				if (textureSourceWidthWithSpacing == rectWidth) {
					if (textureSourceHeightWithSpacing == rectHeight) { /* Normal case with padding. */
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					} else if (fitToBottomWithoutSpacing) { /* Bottom edge of the BitmapTexture. */
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					}
				}

				if (fitToRightWithoutSpacing) { /* Right edge of the BitmapTexture. */
					if (textureSourceHeightWithSpacing == rectHeight) {
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					} else if (fitToBottomWithoutSpacing) { /* Bottom edge of the BitmapTexture. */
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					} else if (textureSourceHeightWithSpacing > rectHeight) {
						return null;
					} else {
						return this.createChildren(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureAtlasSourceSpacing, pTextureAtlasSourcePadding, rectWidth - textureSourceWidth, rectHeight - textureSourceHeightWithSpacing);
					}
				}

				if (fitToBottomWithoutSpacing) {
					if (textureSourceWidthWithSpacing == rectWidth) {
						this.mTextureAtlasSource = pTextureAtlasSource;
						return this;
					} else if (textureSourceWidthWithSpacing > rectWidth) {
						return null;
					} else {
						return this.createChildren(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureAtlasSourceSpacing, pTextureAtlasSourcePadding, rectWidth - textureSourceWidthWithSpacing, rectHeight - textureSourceHeight);
					}
				} else if (textureSourceWidthWithSpacing > rectWidth || textureSourceHeightWithSpacing > rectHeight) {
					return null;
				} else {
					return this.createChildren(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureAtlasSourceSpacing, pTextureAtlasSourcePadding, rectWidth - textureSourceWidthWithSpacing, rectHeight - textureSourceHeightWithSpacing);
				}
			}
		}

		private Node createChildren(final ITextureAtlasSource pTextureAtlasSource, final int pTextureWidth, final int pTextureHeight, final int pTextureAtlasSourceSpacing, final int pTextureAtlasSourcePadding, final int pDeltaWidth, final int pDeltaHeight) {
			final Rect rect = this.mRect;

			if (pDeltaWidth >= pDeltaHeight) {
				/* Split using a vertical axis. */
				this.mChildA = new Node(
						rect.getLeft(),
						rect.getTop(),
						pTextureAtlasSource.getTextureWidth() + pTextureAtlasSourceSpacing + 2 * pTextureAtlasSourcePadding,
						rect.getHeight()
				);

				this.mChildB = new Node(
						rect.getLeft() + (pTextureAtlasSource.getTextureWidth() + pTextureAtlasSourceSpacing + 2 * pTextureAtlasSourcePadding),
						rect.getTop(),
						rect.getWidth() - (pTextureAtlasSource.getTextureWidth() + pTextureAtlasSourceSpacing + 2 * pTextureAtlasSourcePadding),
						rect.getHeight()
				);
			} else {
				/* Split using a horizontal axis. */
				this.mChildA = new Node(
						rect.getLeft(),
						rect.getTop(),
						rect.getWidth(),
						pTextureAtlasSource.getTextureHeight() + pTextureAtlasSourceSpacing + 2 * pTextureAtlasSourcePadding
				);

				this.mChildB = new Node(
						rect.getLeft(),
						rect.getTop() + (pTextureAtlasSource.getTextureHeight() + pTextureAtlasSourceSpacing + 2 * pTextureAtlasSourcePadding),
						rect.getWidth(),
						rect.getHeight() - (pTextureAtlasSource.getTextureHeight() + pTextureAtlasSourceSpacing + 2 * pTextureAtlasSourcePadding)
				);
			}

			return this.mChildA.insert(pTextureAtlasSource, pTextureWidth, pTextureHeight, pTextureAtlasSourceSpacing, pTextureAtlasSourcePadding);
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
