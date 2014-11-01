package org.andengine.examples;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */
public class PhysicsRevoluteJointExample extends BasePhysicsJointExample {
	// ===========================================================
	// Constants
	// ===========================================================

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
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "In this example, the revolute joints have their motor enabled.", Toast.LENGTH_LONG).show();
		return super.onCreateEngineOptions();
	}

	@Override
	public Scene onCreateScene() {
		final Scene scene = super.onCreateScene();
		this.initJoints(scene);
		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initJoints(final Scene pScene) {
		final float centerX = CAMERA_WIDTH / 2;
		final float centerY = CAMERA_HEIGHT / 2;

		final float spriteWidth = this.mBoxFaceTextureRegion.getWidth();
		final float spriteHeight = this.mBoxFaceTextureRegion.getHeight();

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(10, 0.2f, 0.5f);

		for(int i = 0; i < 3; i++) {
			final float anchorFaceX = centerX - spriteWidth * 0.5f + 220 * (i - 1);
			final float anchorFaceY = centerY - spriteHeight * 0.5f;

			final AnimatedSprite anchorFace = new AnimatedSprite(anchorFaceX, anchorFaceY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
			final Body anchorBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, anchorFace, BodyType.StaticBody, objectFixtureDef);

			final AnimatedSprite movingFace = new AnimatedSprite(anchorFaceX, anchorFaceY + 90, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
			final Body movingBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, movingFace, BodyType.DynamicBody, objectFixtureDef);

			anchorFace.animate(200);
			anchorFace.animate(200);

			final Line connectionLine = new Line(anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2, anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2, this.getVertexBufferObjectManager());

			pScene.attachChild(connectionLine);
			pScene.attachChild(anchorFace);
			pScene.attachChild(movingFace);

			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(anchorFace, anchorBody, true, true){
				@Override
				public void onUpdate(final float pSecondsElapsed) {
					super.onUpdate(pSecondsElapsed);
					final Vector2 movingBodyWorldCenter = movingBody.getWorldCenter();
					connectionLine.setPosition(connectionLine.getX1(), connectionLine.getY1(), movingBodyWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, movingBodyWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				}
			});
			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(movingFace, movingBody, true, true));


			final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
			revoluteJointDef.initialize(anchorBody, movingBody, anchorBody.getWorldCenter());
			revoluteJointDef.enableMotor = true;
			revoluteJointDef.motorSpeed = 10;
			revoluteJointDef.maxMotorTorque = 200;

			this.mPhysicsWorld.createJoint(revoluteJointDef);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
