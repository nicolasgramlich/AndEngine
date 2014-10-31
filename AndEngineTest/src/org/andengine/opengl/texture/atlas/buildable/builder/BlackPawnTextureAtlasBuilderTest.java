package org.andengine.opengl.texture.atlas.buildable.builder;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder.Node;
import org.andengine.opengl.texture.atlas.source.ITextureAtlasSource;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

/**
 * @author Nicolas Gramlich
 * @since 16:08:32 - 12.08.2010
 */
public class BlackPawnTextureAtlasBuilderTest extends TestCase {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int SPACING_NONE = 0;
	private static final int SPACING_ONE = 1;
	private static final int PADDING_NONE = 0;
	private static final int PADDING_ONE = 1;

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

	public void testSingle() {
		final Node node = new Node(0, 0, 10, 10);
		final Node inserted = node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 0, 0, 4, 2);
	}

	public void testDouble() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 4, 0, 4, 2);
	}

	public void testTriple() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 0, 2, 4, 2);
	}

	public void testQuadrupel() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(2, 2), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 8, 0, 2, 2);
	}

	public void testQuadrupel2() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(2, 3), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 0, 4, 2, 3);
	}

	public void testQuintupel() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(2, 3), 10, 10, SPACING_NONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(2, 3), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 2, 4, 2, 3);
	}

	public void testQuintupel2() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(2, 3), 10, 10, SPACING_NONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(2, 8), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 4, 2, 2, 8);
	}

	public void testQuintupel3() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 2), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(2, 3), 10, 10, SPACING_NONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(2, 10), 10, 10, SPACING_NONE, PADDING_NONE);

		Assert.assertNull(inserted);
	}

	public void testFullSize() {
		final Node node = new Node(0, 0, 10, 10);

		final Node inserted = node.insert(new DummyTextureSource(10, 10), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 0, 0, 10, 10);
	}

	public void testPerfectFit() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(5, 5), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(5, 5), 10, 10, SPACING_NONE, PADDING_NONE);
		node.insert(new DummyTextureSource(5, 5), 10, 10, SPACING_NONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(5, 5), 10, 10, SPACING_NONE, PADDING_NONE);

		this.assertNodeRect(inserted, 5, 5, 5, 5);
	}

	public void testPerfectFit2() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(4, 4), 10, 10, SPACING_ONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 4), 10, 10, SPACING_ONE, PADDING_NONE);
		node.insert(new DummyTextureSource(4, 4), 10, 10, SPACING_ONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(5, 5), 10, 10, SPACING_ONE, PADDING_NONE);

		this.assertNodeRect(inserted, 5, 5, 5, 5);
	}

	public void testNoFitDueToSpacing() {
		final Node node = new Node(0, 0, 10, 10);
		node.insert(new DummyTextureSource(5, 5), 10, 10, SPACING_ONE, PADDING_NONE);

		final Node inserted = node.insert(new DummyTextureSource(5, 5), 10, 10, SPACING_ONE, PADDING_NONE);

		Assert.assertNull(inserted);
	}

	public void testSinglePadding() {
		final Node node = new Node(0, 0, 10, 10);
		final Node inserted = node.insert(new DummyTextureSource(8, 8), 10, 10, SPACING_NONE, PADDING_ONE);

		this.assertNodeRect(inserted, 0, 0, 10, 10);
	}

	private void assertNodeRect(final Node pNode, final int pLeft, final int pTop, final int pWidth, final int pHeight) {
		Assert.assertEquals(pLeft, pNode.getRect().getLeft());
		Assert.assertEquals(pTop, pNode.getRect().getTop());
		Assert.assertEquals(pWidth, pNode.getRect().getWidth());
		Assert.assertEquals(pHeight, pNode.getRect().getHeight());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	protected static class DummyTextureSource implements ITextureAtlasSource {
		private final int mWidth;
		private final int mHeight;

		public DummyTextureSource(final int pWidth, final int pHeight) {
			this.mWidth = pWidth;
			this.mHeight = pHeight;
		}

		@Override
		public int getTextureHeight() {
			return this.mHeight;
		}

		@Override
		public int getTextureWidth() {
			return this.mWidth;
		}

		@Override
		public DummyTextureSource clone() {
			return new DummyTextureSource(this.mWidth, this.mHeight);
		}

		@Override
		public int getTextureX() {
			return 0;
		}

		@Override
		public int getTextureY() {
			return 0;
		}

		@Override
		public void setTextureX(final int pTextureX) {
		}

		@Override
		public void setTextureY(final int pTextureY) {
		}

		@Override
		public void setTextureWidth(final int pTextureWidth) {
		}

		@Override
		public void setTextureHeight(final int pTextureHeight) {
		}
		
		@Override
		public ITextureAtlasSource deepCopy() throws DeepCopyNotSupportedException {
			return null;
		}
	}
}
