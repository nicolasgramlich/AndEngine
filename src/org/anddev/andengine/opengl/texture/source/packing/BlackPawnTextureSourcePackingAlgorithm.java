package org.anddev.andengine.opengl.texture.source.packing;

import java.util.Arrays;
import java.util.Comparator;

import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.source.ITextureSource;

/**
 * @author Nicolas Gramlich
 * @since 16:03:01 - 12.08.2010
 * @see http://www.blackpawn.com/texts/lightmaps/default.html
 */
public class BlackPawnTextureSourcePackingAlgorithm implements ITextureSourcePackingAlgorithm {
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

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void pack(final Texture pTexture, final ITextureSource[] pTextureSources) {
		Arrays.sort(pTextureSources, TEXTURESOURCE_COMPARATOR);

		final Node root = new Node(new Rect(0, 0, pTexture.getWidth(), pTexture.getHeight()));

		final int textureSourceCount = pTextureSources.length;

		for(int i = 0; i < textureSourceCount; i++) {
			root.insert(pTextureSources[i]);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	static class Rect{

		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final int mLeft;
		private final int mRight;
		private final int mWidth;
		private final int mHeight;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Rect(final int pLeft, final int pTop, final int pWidth, final int pHeight) {
			this.mLeft = pLeft;
			this.mRight = pTop;
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
			return this.mRight;
		}

		public int getRight() {
			return this.mLeft + this.mWidth;
		}

		public int getBottom() {
			return this.mRight + this.mHeight;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public String toString() {
			return "@: " + this.mLeft + "/" + this.mRight + " * " + this.mWidth + "x" + this.mHeight;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	static class Node {
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

		public Node insert(final ITextureSource pTextureSource) throws IllegalArgumentException {
			if(this.mChildA != null && this.mChildB != null) {
				final Node newNode = this.mChildA.insert(pTextureSource);
				if(newNode != null){
					return newNode;
				} else {
					return this.mChildB.insert(pTextureSource);
				}
			} else {
				if(this.mTextureSource != null) {
					return null;
				}

				if(pTextureSource.getWidth() > this.mRect.getWidth() || pTextureSource.getHeight() > this.mRect.getHeight()) {
					return null;
				}

				if(pTextureSource.getWidth() == this.mRect.getWidth() && pTextureSource.getHeight() == this.mRect.getHeight()) {
					this.mTextureSource = pTextureSource;
					return this;
				}


				final int deltaWidth = this.mRect.getWidth() - pTextureSource.getWidth();
				final int deltaHeight = this.mRect.getHeight() - pTextureSource.getHeight();

				if(deltaWidth >= deltaHeight) {
					/* Split using a vertical axis. */
					this.mChildA = new Node(
							this.mRect.getLeft(),
							this.mRect.getTop(),
							pTextureSource.getWidth(),
							this.mRect.getHeight()
					);

					this.mChildB = new Node(
							this.mRect.getLeft() + pTextureSource.getWidth(),
							this.mRect.getTop(),
							this.mRect.getWidth() - pTextureSource.getWidth(),
							this.mRect.getHeight()
					);
				} else {
					/* Split using a horizontal axis. */
					this.mChildA = new Node(
							this.mRect.getLeft(),
							this.mRect.getTop(),
							this.mRect.getWidth(),
							pTextureSource.getHeight()
					);

					this.mChildB = new Node(
							this.mRect.getLeft(),
							this.mRect.getTop() + pTextureSource.getHeight(),
							this.mRect.getWidth(),
							this.mRect.getHeight() - pTextureSource.getHeight()
					);
				}

				return this.mChildA.insert(pTextureSource);
			}
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
