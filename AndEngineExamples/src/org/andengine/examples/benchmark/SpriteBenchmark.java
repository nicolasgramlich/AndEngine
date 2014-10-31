package org.andengine.examples.benchmark;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.entity.sprite.batch.vbo.HighPerformanceSpriteBatchVertexBufferObject;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemorySpriteVertexBufferObject;
import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

import android.opengl.GLES20;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 10:34:22 - 27.06.2010
 */
public class SpriteBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	private static final int SPRITE_COUNT = 5000;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;

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
	protected int getBenchmarkID() {
		return BaseBenchmark.SPRITEBENCHMARK_ID;
	}

	@Override
	protected float getBenchmarkStartOffset() {
		return 2;
	}

	@Override
	protected float getBenchmarkDuration() {
		return 10;
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, SpriteBenchmark.CAMERA_WIDTH, SpriteBenchmark.CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(SpriteBenchmark.CAMERA_WIDTH, SpriteBenchmark.CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);

		this.mBitmapTextureAtlas.load();
	}

	@Override
	public Scene onCreateScene() {
		final Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

//		this.drawUsingSprites(scene);
//		this.drawUsingSpritesWithSharedVertexBuffer(scene);
		this.drawUsingSpriteBatch(scene);

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void drawUsingSprites(final Scene pScene) {
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		for(int i = 0; i < SpriteBenchmark.SPRITE_COUNT; i++) {
			final Sprite face = new Sprite(this.mRandom.nextFloat() * (SpriteBenchmark.CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (SpriteBenchmark.CAMERA_HEIGHT - 32), this.mFaceTextureRegion, vertexBufferObjectManager);
			face.registerEntityModifier(new AlphaModifier(this.getBenchmarkDuration(), 1, 0));
			pScene.attachChild(face);
		}
	}

	private void drawUsingSpritesWithSharedVertexBuffer(final Scene pScene) {
		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
		final ISpriteVertexBufferObject sharedVertexBuffer = new LowMemorySpriteVertexBufferObject(this.getVertexBufferObjectManager(), Sprite.SPRITE_SIZE, DrawType.STATIC, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);

		for(int i = 0; i < SPRITE_COUNT; i++) {
			final Sprite face = new Sprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion, sharedVertexBuffer);
			face.setIgnoreUpdate(true);
			pScene.attachChild(face);
		}
	}

	private void drawUsingSpriteBatch(final Scene pScene) {
		final float width = this.mFaceTextureRegion.getWidth();
		final float height = this.mFaceTextureRegion.getHeight();

		final SpriteBatchWithoutColor spriteBatch = new SpriteBatchWithoutColor(this.getVertexBufferObjectManager(), this.mBitmapTextureAtlas, SpriteBenchmark.SPRITE_COUNT, DrawType.STATIC);

		for(int i = 0; i < SpriteBenchmark.SPRITE_COUNT; i++) {
			final float x = this.mRandom.nextFloat() * (SpriteBenchmark.CAMERA_WIDTH - 32);
			final float y = this.mRandom.nextFloat() * (SpriteBenchmark.CAMERA_HEIGHT - 32);
			spriteBatch.draw(this.mFaceTextureRegion, x, y, width, height);
		}
		spriteBatch.submit();

		pScene.attachChild(spriteBatch);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class SpriteBatchWithoutColor extends SpriteBatch {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final int VERTEX_INDEX_X = 0;
		public static final int VERTEX_INDEX_Y = SpriteBatchWithoutColor.VERTEX_INDEX_X + 1;
		public static final int TEXTURECOORDINATES_INDEX_U = SpriteBatchWithoutColor.VERTEX_INDEX_Y + 1;
		public static final int TEXTURECOORDINATES_INDEX_V = SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U + 1;

		public static final int VERTEX_SIZE = 2 + 2;
		public static final int VERTICES_PER_SPRITE = 6;
		public static final int SPRITE_SIZE = SpriteBatchWithoutColor.VERTEX_SIZE * SpriteBatchWithoutColor.VERTICES_PER_SPRITE;

		public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_WITHOUT_COLOR = new VertexBufferObjectAttributesBuilder(2)
			.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
			.add(ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES_LOCATION, ShaderProgramConstants.ATTRIBUTE_TEXTURECOORDINATES, 2, GLES20.GL_FLOAT, false)
			.build();
		
		// ===========================================================
		// Fields
		// ===========================================================

		private final SpriteBatchVertexBufferObjectWithoutColor mSpriteBatchVertexBufferObjectWithoutColor;

		// ===========================================================
		// Constructors
		// ===========================================================

		private SpriteBatchWithoutColor(final VertexBufferObjectManager pVertexBufferObjectManager, final ITexture pTexture, final int pCapacity, DrawType pDrawType) {
			super(pTexture, pCapacity, new SpriteBatchVertexBufferObjectWithoutColor(pVertexBufferObjectManager, pCapacity, pDrawType, true, SpriteBatchWithoutColor.VERTEXBUFFEROBJECTATTRIBUTES_WITHOUT_COLOR), PositionTextureCoordinatesShaderProgram.getInstance());

			this.mSpriteBatchVertexBufferObjectWithoutColor = (SpriteBatchVertexBufferObjectWithoutColor) this.mSpriteBatchVertexBufferObject;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		public void draw(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
			this.mSpriteBatchVertexBufferObjectWithoutColor.addWithoutColor(pTextureRegion, pX, pY, pWidth, pHeight);
			this.mIndex++;
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

	public static class SpriteBatchVertexBufferObjectWithoutColor extends HighPerformanceSpriteBatchVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public SpriteBatchVertexBufferObjectWithoutColor(final VertexBufferObjectManager pVertexBufferObjectManager, final int pCapacity, final DrawType pDrawType, final boolean pManaged, final VertexBufferObjectAttributes pVertexBufferObjectAttributes) {
			super(pVertexBufferObjectManager, pCapacity * SpriteBatchWithoutColor.SPRITE_SIZE, pDrawType, pManaged, pVertexBufferObjectAttributes);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void addWithoutColor(final ITextureRegion pTextureRegion, final float pX, final float pY, final float pWidth, final float pHeight) {
			final float[] bufferData = this.mBufferData;
			final int bufferDataOffset = this.mBufferDataOffset;

			final float x1 = pX;
			final float y1 = pY;
			final float x2 = pX + pWidth;
			final float y2 = pY + pHeight;
			final float u = pTextureRegion.getU();
			final float v = pTextureRegion.getV();
			final float u2 = pTextureRegion.getU2();
			final float v2 = pTextureRegion.getV2();

			bufferData[bufferDataOffset + 0 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 0 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 0 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 0 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 1 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 1 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 1 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 1 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 2 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 2 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 2 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 2 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 3 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 3 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 3 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 3 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 4 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 4 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 4 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 4 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 5 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 5 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 5 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 5 * SpriteBatchWithoutColor.VERTEX_SIZE + SpriteBatchWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			this.mBufferDataOffset += SpriteBatchWithoutColor.SPRITE_SIZE;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
