package org.andengine.examples.benchmark;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteGroup;
import org.andengine.entity.sprite.batch.vbo.HighPerformanceSpriteBatchVertexBufferObject;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.entity.sprite.vbo.LowMemoryTiledSpriteVertexBufferObject;
import org.andengine.opengl.shader.PositionTextureCoordinatesShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
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
 * @since 11:28:45 - 28.06.2010
 */
public class AnimationBenchmark extends BaseBenchmark {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private static final int SPRITE_COUNT = 500;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion mSnapdragonTextureRegion;
	private TiledTextureRegion mHelicopterTextureRegion;
	private TiledTextureRegion mBananaTextureRegion;
	private TiledTextureRegion mFaceTextureRegion;

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
		return ANIMATIONBENCHMARK_ID;
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
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mSnapdragonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "snapdragon_tiled.png", 0, 0, 4, 3);
		this.mHelicopterTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "helicopter_tiled.png", 400, 0, 2, 2);
		this.mBananaTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "banana_tiled.png", 0, 180, 4, 2);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 132, 180, 2, 1);

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

	private void drawUsingSprites(Scene pScene) {
		for(int i = 0; i < SPRITE_COUNT; i++) {
			/* Quickly twinkling face. */
			final AnimatedSprite face = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion, this.getVertexBufferObjectManager());
			face.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(face);

			/* Continuously flying helicopter. */
			final AnimatedSprite helicopter = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 48), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 48), this.mHelicopterTextureRegion, this.getVertexBufferObjectManager());
			helicopter.animate(new long[] { 50 + this.mRandom.nextInt(100), 50 + this.mRandom.nextInt(100) }, 1, 2, true);
			pScene.attachChild(helicopter);

			/* Snapdragon. */
			final AnimatedSprite snapdragon = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 100), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 60), this.mSnapdragonTextureRegion, this.getVertexBufferObjectManager());
			snapdragon.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(snapdragon);

			/* Funny banana. */
			final AnimatedSprite banana = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mBananaTextureRegion, this.getVertexBufferObjectManager());
			banana.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(banana);
		}
	}

	private void drawUsingSpritesWithSharedVertexBuffer(Scene pScene) {
		/* As we are creating quite a lot of the same Sprites, we can let them share a VertexBuffer to significantly increase performance. */
		final ITiledSpriteVertexBufferObject faceSharedVertexBuffer = new LowMemoryTiledSpriteVertexBufferObject(this.getVertexBufferObjectManager(), Sprite.SPRITE_SIZE, DrawType.STATIC, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
		final ITiledSpriteVertexBufferObject helicopterSharedVertexBuffer = new LowMemoryTiledSpriteVertexBufferObject(this.getVertexBufferObjectManager(), Sprite.SPRITE_SIZE, DrawType.STATIC, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
		final ITiledSpriteVertexBufferObject snapdragonSharedVertexBuffer = new LowMemoryTiledSpriteVertexBufferObject(this.getVertexBufferObjectManager(), Sprite.SPRITE_SIZE, DrawType.STATIC, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
		final ITiledSpriteVertexBufferObject bananaSharedVertexBuffer = new LowMemoryTiledSpriteVertexBufferObject(this.getVertexBufferObjectManager(), Sprite.SPRITE_SIZE, DrawType.STATIC, true, Sprite.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT);
		
		for(int i = 0; i < SPRITE_COUNT; i++) {
			/* Quickly twinkling face. */
			final AnimatedSprite face = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion, faceSharedVertexBuffer);
			face.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(face);

			/* Continuously flying helicopter. */
			final AnimatedSprite helicopter = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 48), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 48), this.mHelicopterTextureRegion, helicopterSharedVertexBuffer);
			helicopter.animate(new long[] { 50 + this.mRandom.nextInt(100), 50 + this.mRandom.nextInt(100) }, 1, 2, true);
			pScene.attachChild(helicopter);

			/* Snapdragon. */
			final AnimatedSprite snapdragon = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 100), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 60), this.mSnapdragonTextureRegion, snapdragonSharedVertexBuffer);
			snapdragon.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(snapdragon);

			/* Funny banana. */
			final AnimatedSprite banana = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mBananaTextureRegion, bananaSharedVertexBuffer);
			banana.animate(50 + this.mRandom.nextInt(100));
			pScene.attachChild(banana);
		}
	}

	private void drawUsingSpriteBatch(Scene pScene) {
		final SpriteGroupWithoutColor spriteGroup = new SpriteGroupWithoutColor(this.getVertexBufferObjectManager(), this.mBitmapTextureAtlas, 4 * SPRITE_COUNT, DrawType.DYNAMIC);
		for(int i = 0; i < SPRITE_COUNT; i++) {
			/* Quickly twinkling face. */
			final AnimatedSprite face = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mFaceTextureRegion.deepCopy(), this.getVertexBufferObjectManager());
			face.animate(50 + this.mRandom.nextInt(100));
			spriteGroup.attachChild(face);

			/* Continuously flying helicopter. */
			final AnimatedSprite helicopter = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 48), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 48), this.mHelicopterTextureRegion, this.getVertexBufferObjectManager());
			helicopter.animate(new long[] { 50 + this.mRandom.nextInt(100), 50 + this.mRandom.nextInt(100) }, 1, 2, true);
			spriteGroup.attachChild(helicopter);

			/* Snapdragon. */
			final AnimatedSprite snapdragon = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 100), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 60), this.mSnapdragonTextureRegion, this.getVertexBufferObjectManager());
			snapdragon.animate(50 + this.mRandom.nextInt(100));
			spriteGroup.attachChild(snapdragon);

			/* Funny banana. */
			final AnimatedSprite banana = new AnimatedSprite(this.mRandom.nextFloat() * (CAMERA_WIDTH - 32), this.mRandom.nextFloat() * (CAMERA_HEIGHT - 32), this.mBananaTextureRegion, this.getVertexBufferObjectManager());
			banana.animate(50 + this.mRandom.nextInt(100));
			spriteGroup.attachChild(banana);
		}
		
		pScene.attachChild(spriteGroup);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class SpriteGroupWithoutColor extends SpriteGroup {
		// ===========================================================
		// Constants
		// ===========================================================

		public static final int VERTEX_INDEX_X = 0;
		public static final int VERTEX_INDEX_Y = SpriteGroupWithoutColor.VERTEX_INDEX_X + 1;
		public static final int TEXTURECOORDINATES_INDEX_U = SpriteGroupWithoutColor.VERTEX_INDEX_Y + 1;
		public static final int TEXTURECOORDINATES_INDEX_V = SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U + 1;

		public static final int VERTEX_SIZE = 2 + 2;
		public static final int VERTICES_PER_SPRITE = 6;
		public static final int SPRITE_SIZE = SpriteGroupWithoutColor.VERTEX_SIZE * SpriteGroupWithoutColor.VERTICES_PER_SPRITE;

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

		private SpriteGroupWithoutColor(final VertexBufferObjectManager pVertexBufferObjectManager, final ITexture pTexture, final int pCapacity, DrawType pDrawType) {
			super(pTexture, pCapacity, new SpriteBatchVertexBufferObjectWithoutColor(pVertexBufferObjectManager, pCapacity, pDrawType, true, SpriteGroupWithoutColor.VERTEXBUFFEROBJECTATTRIBUTES_WITHOUT_COLOR), PositionTextureCoordinatesShaderProgram.getInstance());

			this.mSpriteBatchVertexBufferObjectWithoutColor = (SpriteBatchVertexBufferObjectWithoutColor) this.mSpriteBatchVertexBufferObject;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================
		
		@Override
		public void drawWithoutChecks(final Sprite pSprite) {
			this.mSpriteBatchVertexBufferObjectWithoutColor.addWithoutColor(pSprite.getTextureRegion(), pSprite.getX(), pSprite.getY(), pSprite.getWidth(), pSprite.getHeight());
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
			super(pVertexBufferObjectManager, pCapacity * SpriteGroupWithoutColor.SPRITE_SIZE, pDrawType, pManaged, pVertexBufferObjectAttributes);
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

			bufferData[bufferDataOffset + 0 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 0 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 0 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 0 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 1 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 1 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 1 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 1 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 2 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 2 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 2 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 2 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 3 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 3 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y1;
			bufferData[bufferDataOffset + 3 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 3 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v;

			bufferData[bufferDataOffset + 4 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x1;
			bufferData[bufferDataOffset + 4 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 4 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u;
			bufferData[bufferDataOffset + 4 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			bufferData[bufferDataOffset + 5 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_X] = x2;
			bufferData[bufferDataOffset + 5 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.VERTEX_INDEX_Y] = y2;
			bufferData[bufferDataOffset + 5 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_U] = u2;
			bufferData[bufferDataOffset + 5 * SpriteGroupWithoutColor.VERTEX_SIZE + SpriteGroupWithoutColor.TEXTURECOORDINATES_INDEX_V] = v2;

			this.mBufferDataOffset += SpriteGroupWithoutColor.SPRITE_SIZE;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
