package com;

import com.parser.AtlasParser;
import com.spine.Animation;
import com.spine.Bone;
import com.spine.Skeleton;
import com.spine.SkeletonData;
import com.spine.SkeletonJson;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import java.io.IOException;

public class GameActivity extends SimpleBaseGameActivity implements IUpdateHandler {

	private Camera mCamera;
	private Skeleton skeleton;
	private Bone root;
	private Animation animation;
	private SkeletonData skeletonData;
	private SkeletonJson json;
	private float time;
	private AtlasParser atlasParser;
	private SpriteBatch batch;
	
	public static VertexBufferObjectManager vertex;

	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, 800, 480);
		EngineOptions mEngineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
														new FillResolutionPolicy(), mCamera);
        return mEngineOptions;
	}

	@Override
	protected void onCreateResources()
	{
	    vertex = this.getVertexBufferObjectManager();
	    
		try {
			
			atlasParser = new AtlasParser(this.getAssets().open("spineboy.atlas"), this.getTextureManager(), this);
			json = new SkeletonJson(atlasParser, this);
			
			skeletonData = json.readSkeletonData(this.getAssets().open("spineboy-skeleton.json"));
			animation = json.readAnimation(this.getAssets().open("spineboy-walk.json"), skeletonData);
	
			skeleton = new Skeleton(skeletonData);
	
			root = skeleton.getRootBone();
			root.setX(150);
			root.setY(100);
	
			skeleton.updateWorldTransform();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Scene onCreateScene()
	{
		Scene pScene = new Scene();
		
		pScene.registerUpdateHandler(this);
		
		pScene.setBackground(new Background(Color.CYAN));
		
		skeleton.drawDebug(pScene, this.getVertexBufferObjectManager());
		
		batch = new SpriteBatch(atlasParser.getAtlas(), 50, this.getVertexBufferObjectManager());
		
		skeleton.draw(batch);
		
		/*LinkedList<TextureRegion> regions = json.getRegions();
		int count = 0;
		for(TextureRegion pReg : regions) {
			batch.draw(pReg, (float)(10 * count), (float)(10 * count), pReg.getWidth(), pReg.getHeight(), Color.RED_ABGR_PACKED_FLOAT);
			//Sprite sprite = new Sprite(10 * count, 10 * count, pReg, this.getVertexBufferObjectManager());
			//pScene.attachChild(sprite);
			count++;
		}*/
		
		//batch.draw(pTextureRegion, pX, pY, pWidth, pHeight, pRotation, pScaleX, pScaleY, pRed, pGreen, pBlue, pAlpha);
		
		batch.submit();
		batch.setPosition(0, 0);
		
		pScene.attachChild(batch);
		
		return pScene;
	}

	public void onUpdate(float pSecondsElapsed) {
		//final long nanoSecondsElapsed = (long) (pSecondsElapsed * TimeConstants.NANOSECONDS_PER_SECOND);
		time += 0.001f; //nanoSecondsElapsed;
		/*float x = root.getX();
		if(x > this.mEngine.getSurfaceWidth()) {
			x = 0;
		}
		root.setX(x + time);*/
		//Debug.d("root: " + root.getX());
		animation.apply(skeleton, time, true);
		skeleton.updateWorldTransform();
		//skeleton.draw(batch);
		skeleton.drawDebug(this.mEngine.getScene(), this.getVertexBufferObjectManager());
		
		batch.submit();
	}

	public void reset() {
		
	}

}
