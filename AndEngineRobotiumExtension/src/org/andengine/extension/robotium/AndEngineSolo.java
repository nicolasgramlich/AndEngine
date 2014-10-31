package org.andengine.extension.robotium;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityMatcher;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.State;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.ITouchController;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.Constants;
import org.andengine.util.color.Color;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.jayway.android.robotium.solo.Solo;

/**
 * TODO Are tests deterministic? (Since UpdateThread and DrawThread are running,
 * the EngineLock might have to be aquired.)
 * 
 * (c) Zynga 2012
 * 
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 13:20:29 - 14.02.2012
 */
public class AndEngineSolo extends Solo {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public AndEngineSolo(final Instrumentation pInstrumentation) {
		super(pInstrumentation);
	}

	public AndEngineSolo(final Instrumentation pInstrumentation, final Activity pActivity) {
		super(pInstrumentation, pActivity);

		if (!(pActivity instanceof BaseGameActivity)) {
			Assert.fail("The supplied " + Activity.class.getSimpleName() + " does not subclass '" + BaseGameActivity.class.getSimpleName() + "' but is a '" + pActivity.getClass().getSimpleName()
					+ "'.");
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public BaseGameActivity getGameActivity() {
		return (BaseGameActivity) this.getCurrentActivity();
	}

	public Engine getEngine() {
		return this.getGameActivity().getEngine();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Asserts
	// ===========================================================

	public void assertEntityVisible(final Object pTag) {
		Assert.assertTrue(this.isEntityVisible(pTag));
	}

	public void assertEntityVisible(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertTrue(this.isEntityVisible(pClass, pTag));
	}

	public void assertEntityInvisible(final Object pTag) {
		Assert.assertFalse(this.isEntityVisible(pTag));
	}

	public void assertEntityInvisible(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertFalse(this.isEntityVisible(pClass, pTag));
	}

	public void assertEntityPosition(final float pExpectedPositionX, final float pExpectedPositionY, final Object pTag) {
		Assert.assertEquals(pExpectedPositionX, this.getEntityPositionX(pTag));
		Assert.assertEquals(pExpectedPositionY, this.getEntityPositionY(pTag));
	}

	public void assertEntityPosition(final float pExpectedPositionX, final float pExpectedPositionY, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedPositionX, this.getEntityPositionX(pTag), pDelta);
		Assert.assertEquals(pExpectedPositionY, this.getEntityPositionY(pTag), pDelta);
	}

	public void assertEntityPosition(final float pExpectedPositionX, final float pExpectedPositionY, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedPositionX, this.getEntityPositionX(pClass, pTag));
		Assert.assertEquals(pExpectedPositionY, this.getEntityPositionY(pClass, pTag));
	}

	public void assertEntityPosition(final float pExpectedPositionX, final float pExpectedPositionY, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedPositionX, this.getEntityPositionX(pClass, pTag), pDelta);
		Assert.assertEquals(pExpectedPositionY, this.getEntityPositionY(pClass, pTag), pDelta);
	}

	public void assertEntityRotated(final Object pTag) {
		Assert.assertTrue(this.isEntityRotated(pTag));
	}

	public void assertEntityRotated(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertTrue(this.isEntityRotated(pClass, pTag));
	}

	public void assertEntityNotRotated(final Object pTag) {
		Assert.assertFalse(this.isEntityRotated(pTag));
	}

	public void assertEntityNotRotated(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertFalse(this.isEntityRotated(pClass, pTag));
	}

	public void assertEntityRotation(final float pExpectedRotation, final Object pTag) {
		Assert.assertEquals(pExpectedRotation, this.getEntityRotation(pTag));
	}

	public void assertEntityRotation(final float pExpectedRotation, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedRotation, this.getEntityRotation(pTag), pDelta);
	}

	public void assertEntityRotation(final float pExpectedRotation, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedRotation, this.getEntityRotation(pClass, pTag));
	}

	public void assertEntityRotation(final float pExpectedRotation, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedRotation, this.getEntityRotation(pClass, pTag), pDelta);
	}

	public void assertEntityRotationCenter(final float pExpectedRotationCenterX, final float pExpectedRotationCenterY, final Object pTag) {
		Assert.assertEquals(pExpectedRotationCenterX, this.getEntityRotationCenterX(pTag));
		Assert.assertEquals(pExpectedRotationCenterY, this.getEntityRotationCenterY(pTag));
	}

	public void assertEntityRotationCenter(final float pExpectedRotationCenterX, final float pExpectedRotationCenterY, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedRotationCenterX, this.getEntityRotationCenterX(pTag), pDelta);
		Assert.assertEquals(pExpectedRotationCenterY, this.getEntityRotationCenterY(pTag), pDelta);
	}

	public void assertEntityRotationCenter(final float pExpectedRotationCenterX, final float pExpectedRotationCenterY, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedRotationCenterX, this.getEntityRotationCenterX(pClass, pTag));
		Assert.assertEquals(pExpectedRotationCenterY, this.getEntityRotationCenterY(pClass, pTag));
	}

	public void assertEntityRotationCenter(final float pExpectedRotationCenterX, final float pExpectedRotationCenterY, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedRotationCenterX, this.getEntityRotationCenterX(pClass, pTag), pDelta);
		Assert.assertEquals(pExpectedRotationCenterY, this.getEntityRotationCenterY(pClass, pTag), pDelta);
	}

	public void assertEntityScaled(final Object pTag) {
		Assert.assertTrue(this.isEntityScaled(pTag));
	}

	public void assertEntityScaled(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertTrue(this.isEntityScaled(pClass, pTag));
	}

	public void assertEntityNotScaled(final Object pTag) {
		Assert.assertFalse(this.isEntityScaled(pTag));
	}

	public void assertEntityNotScaled(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertFalse(this.isEntityScaled(pClass, pTag));
	}

	public void assertEntityScaleX(final float pExpectedScaleX, final Object pTag) {
		Assert.assertEquals(pExpectedScaleX, this.getEntityScaleX(pTag));
	}

	public void assertEntityScaleX(final float pExpectedScaleX, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedScaleX, this.getEntityScaleX(pTag), pDelta);
	}

	public void assertEntityScaleX(final float pExpectedScaleX, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedScaleX, this.getEntityScaleX(pClass, pTag));
	}

	public void assertEntityScaleX(final float pExpectedScaleX, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedScaleX, this.getEntityScaleX(pClass, pTag), pDelta);
	}

	public void assertEntityScaleY(final float pExpectedScaleY, final Object pTag) {
		Assert.assertEquals(pExpectedScaleY, this.getEntityScaleY(pTag));
	}

	public void assertEntityScaleY(final float pExpectedScaleY, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedScaleY, this.getEntityScaleY(pTag), pDelta);
	}

	public void assertEntityScaleY(final float pExpectedScaleY, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedScaleY, this.getEntityScaleY(pClass, pTag));
	}

	public void assertEntityScaleY(final float pExpectedScaleY, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedScaleY, this.getEntityScaleY(pClass, pTag), pDelta);
	}

	public void assertEntityScaleCenter(final float pExpectedScaleCenterX, final float pExpectedScaleCenterY, final Object pTag) {
		Assert.assertEquals(pExpectedScaleCenterX, this.getEntityScaleCenterX(pTag));
		Assert.assertEquals(pExpectedScaleCenterY, this.getEntityScaleCenterY(pTag));
	}

	public void assertEntityScaleCenter(final float pExpectedScaleCenterX, final float pExpectedScaleCenterY, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedScaleCenterX, this.getEntityScaleCenterX(pTag), pDelta);
		Assert.assertEquals(pExpectedScaleCenterY, this.getEntityScaleCenterY(pTag), pDelta);
	}

	public void assertEntityScaleCenter(final float pExpectedScaleCenterX, final float pExpectedScaleCenterY, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedScaleCenterX, this.getEntityScaleCenterX(pClass, pTag));
		Assert.assertEquals(pExpectedScaleCenterY, this.getEntityScaleCenterY(pClass, pTag));
	}

	public void assertEntityScaleCenter(final float pExpectedScaleCenterX, final float pExpectedScaleCenterY, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedScaleCenterX, this.getEntityScaleCenterX(pClass, pTag), pDelta);
		Assert.assertEquals(pExpectedScaleCenterY, this.getEntityScaleCenterY(pClass, pTag), pDelta);
	}

	public void assertEntitySkewed(final Object pTag) {
		Assert.assertTrue(this.isEntitySkewed(pTag));
	}

	public void assertEntitySkewed(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertTrue(this.isEntitySkewed(pClass, pTag));
	}

	public void assertEntityNotSkewed(final Object pTag) {
		Assert.assertFalse(this.isEntitySkewed(pTag));
	}

	public void assertEntityNotSkewed(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertFalse(this.isEntitySkewed(pClass, pTag));
	}

	public void assertEntitySkewX(final float pExpectedSkewX, final Object pTag) {
		Assert.assertEquals(pExpectedSkewX, this.getEntitySkewX(pTag));
	}

	public void assertEntitySkewX(final float pExpectedSkewX, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedSkewX, this.getEntitySkewX(pTag), pDelta);
	}

	public void assertEntitySkewX(final float pExpectedSkewX, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedSkewX, this.getEntitySkewX(pClass, pTag));
	}

	public void assertEntitySkewX(final float pExpectedSkewX, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedSkewX, this.getEntitySkewX(pClass, pTag), pDelta);
	}

	public void assertEntitySkewY(final float pExpectedSkewY, final Object pTag) {
		Assert.assertEquals(pExpectedSkewY, this.getEntitySkewY(pTag));
	}

	public void assertEntitySkewY(final float pExpectedSkewY, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedSkewY, this.getEntitySkewY(pTag), pDelta);
	}

	public void assertEntitySkewY(final float pExpectedSkewY, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedSkewY, this.getEntitySkewY(pClass, pTag));
	}

	public void assertEntitySkewY(final float pExpectedSkewY, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedSkewY, this.getEntitySkewY(pClass, pTag), pDelta);
	}

	public void assertEntitySkewCenter(final float pExpectedSkewCenterX, final float pExpectedSkewCenterY, final Object pTag) {
		Assert.assertEquals(pExpectedSkewCenterX, this.getEntitySkewCenterX(pTag));
		Assert.assertEquals(pExpectedSkewCenterY, this.getEntitySkewCenterY(pTag));
	}

	public void assertEntitySkewCenter(final float pExpectedSkewCenterX, final float pExpectedSkewCenterY, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedSkewCenterX, this.getEntitySkewCenterX(pTag), pDelta);
		Assert.assertEquals(pExpectedSkewCenterY, this.getEntitySkewCenterY(pTag), pDelta);
	}

	public void assertEntitySkewCenter(final float pExpectedSkewCenterX, final float pExpectedSkewCenterY, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedSkewCenterX, this.getEntitySkewCenterX(pClass, pTag));
		Assert.assertEquals(pExpectedSkewCenterY, this.getEntitySkewCenterY(pClass, pTag));
	}

	public void assertEntitySkewCenter(final float pExpectedSkewCenterX, final float pExpectedSkewCenterY, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedSkewCenterX, this.getEntitySkewCenterX(pClass, pTag), pDelta);
		Assert.assertEquals(pExpectedSkewCenterY, this.getEntitySkewCenterY(pClass, pTag), pDelta);
	}

	public void assertEntityColorRed(final float pExpectedColorRed, final Object pTag) {
		Assert.assertEquals(pExpectedColorRed, this.getEntityColorRed(pTag));
	}

	public void assertEntityColorRed(final float pExpectedColorRed, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedColorRed, this.getEntityColorRed(pTag), pDelta);
	}

	public void assertEntityColorRed(final float pExpectedColorRed, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedColorRed, this.getEntityColorRed(pClass, pTag));
	}

	public void assertEntityColorRed(final float pExpectedColorRed, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedColorRed, this.getEntityColorRed(pClass, pTag), pDelta);
	}

	public void assertEntityColorGreen(final float pExpectedColorGreen, final Object pTag) {
		Assert.assertEquals(pExpectedColorGreen, this.getEntityColorGreen(pTag));
	}

	public void assertEntityColorGreen(final float pExpectedColorGreen, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedColorGreen, this.getEntityColorGreen(pTag), pDelta);
	}

	public void assertEntityColorGreen(final float pExpectedColorGreen, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedColorGreen, this.getEntityColorGreen(pClass, pTag));
	}

	public void assertEntityColorGreen(final float pExpectedColorGreen, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedColorGreen, this.getEntityColorGreen(pClass, pTag), pDelta);
	}

	public void assertEntityColorBlue(final float pExpectedColorBlue, final Object pTag) {
		Assert.assertEquals(pExpectedColorBlue, this.getEntityColorBlue(pTag));
	}

	public void assertEntityColorBlue(final float pExpectedColorBlue, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedColorBlue, this.getEntityColorBlue(pTag), pDelta);
	}

	public void assertEntityColorBlue(final float pExpectedColorBlue, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedColorBlue, this.getEntityColorBlue(pClass, pTag));
	}

	public void assertEntityColorBlue(final float pExpectedColorBlue, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedColorBlue, this.getEntityColorBlue(pClass, pTag), pDelta);
	}

	public void assertEntityColorAlpha(final float pExpectedColorAlpha, final Object pTag) {
		Assert.assertEquals(pExpectedColorAlpha, this.getEntityColorAlpha(pTag));
	}

	public void assertEntityColorAlpha(final float pExpectedColorAlpha, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedColorAlpha, this.getEntityColorAlpha(pTag), pDelta);
	}

	public void assertEntityColorAlpha(final float pExpectedColorAlpha, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedColorAlpha, this.getEntityColorAlpha(pClass, pTag));
	}

	public void assertEntityColorAlpha(final float pExpectedColorAlpha, final Class<? extends IEntity> pClass, final Object pTag, final float pDelta) {
		Assert.assertEquals(pExpectedColorAlpha, this.getEntityColorAlpha(pClass, pTag), pDelta);
	}

	public void assertEntityColor(final Color pExpectedColor, final Object pTag) {
		Assert.assertEquals(pExpectedColor, this.getEntityColor(pTag));
	}

	public void assertEntityColor(final Color pExpectedColor, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedColor, this.getEntityColor(pClass, pTag));
	}

	public void assertEntityChildCount(final int pExpectedChildCount, final Object pTag) {
		Assert.assertEquals(pExpectedChildCount, this.getEntityChildCount(pTag));
	}

	public void assertEntityChildCount(final int pExpectedChildCount, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertEquals(pExpectedChildCount, this.getEntityChildCount(pClass, pTag));
	}

	public void assertEntityParent(final IEntity pExpectedParentTag, final Object pTag) {
		Assert.assertSame(this.getUniqueEntityByTag(pExpectedParentTag), this.getEntityParent(pTag));
	}

	public void assertEntityParent(final IEntity pExpectedParentTag, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertSame(this.getUniqueEntityByTag(pExpectedParentTag), this.getEntityParent(pClass, pTag));
	}

	public void assertEntityParent(final Class<? extends IEntity> pExpectedParentClass, final Object pExpectedParentTag, final Object pTag) {
		Assert.assertSame(this.getUniqueEntityByTag(pExpectedParentClass, pExpectedParentTag), this.getEntityParent(pTag));
	}

	public void assertEntityParent(final Class<? extends IEntity> pExpectedParentClass, final Object pExpectedParentTag, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertSame(this.getUniqueEntityByTag(pExpectedParentClass, pExpectedParentTag), this.getEntityParent(pClass, pTag));
	}

	public void assertEntityAttached(final Object pTag) {
		Assert.assertTrue(this.getUniqueEntityByTag(pTag).hasParent());
	}

	public void assertEntityAttached(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertTrue(this.getUniqueEntityByTag(pClass, pTag).hasParent());
	}

	public void assertEntityDetached(final Object pTag) {
		Assert.assertFalse(this.getUniqueEntityByTag(pTag).hasParent());
	}

	public void assertEntityDetached(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertFalse(this.getUniqueEntityByTag(pClass, pTag).hasParent());
	}

	public void assertEntityChild(final Object pExpectedChildTag, final Object pTag) {
		Assert.assertSame(this.getEntityParent(pExpectedChildTag), this.getUniqueEntityByTag(pTag));
	}

	public void assertEntityChild(final int pIndex, final Object pExpectedChildTag, final Object pTag) {
		Assert.assertSame(this.getUniqueEntityByTag(pExpectedChildTag), this.getEntityChild(pIndex, pTag));
	}

	public void assertEntityChild(final Object pExpectedChildTag, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertSame(this.getEntityParent(pExpectedChildTag), this.getUniqueEntityByTag(pClass, pTag));
	}

	public void assertEntityChild(final int pIndex, final Object pExpectedChildTag, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertSame(this.getUniqueEntityByTag(pExpectedChildTag), this.getEntityChild(pIndex, pClass, pTag));
	}

	public void assertEntityChild(final Class<? extends IEntity> pExpectedChildClass, final Object pExpectedChildTag, final Object pTag) {
		Assert.assertSame(this.getEntityParent(pExpectedChildClass, pExpectedChildTag), this.getUniqueEntityByTag(pTag));
	}

	public void assertEntityChild(final int pIndex, final Class<? extends IEntity> pExpectedChildClass, final Object pExpectedChildTag, final Object pTag) {
		Assert.assertSame(this.getUniqueEntityByTag(pExpectedChildClass, pExpectedChildTag), this.getEntityChild(pIndex, pTag));
	}

	public void assertEntityChild(final Class<? extends IEntity> pExpectedChildClass, final Object pExpectedChildTag, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertSame(this.getEntityParent(pExpectedChildClass, pExpectedChildTag), this.getUniqueEntityByTag(pClass, pTag));
	}

	public void assertEntityChild(final int pIndex, final Class<? extends IEntity> pExpectedChildClass, final Object pExpectedChildTag, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertSame(this.getUniqueEntityByTag(pExpectedChildClass, pExpectedChildTag), this.getEntityChild(pIndex, pClass, pTag));
	}

	public void assertEntityCullingEnabled(final Object pTag) {
		Assert.assertTrue(this.getUniqueEntityByTag(pTag).isCullingEnabled());
	}

	public void assertEntityCullingEnabled(final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertTrue(this.getUniqueEntityByTag(pClass, pTag).isCullingEnabled());
	}

	public void assertEntityCulled(final Camera pCamera, final Object pTag) {
		Assert.assertTrue(this.getUniqueEntityByTag(pTag).isCulled(pCamera));
	}

	public void assertEntityCulled(final Camera pCamera, final Class<? extends IEntity> pClass, final Object pTag) {
		Assert.assertTrue(this.getUniqueEntityByTag(pClass, pTag).isCulled(pCamera));
	}

	public void assertButtonSpriteEnabled(final Object pTag) {
		Assert.assertTrue(this.isButtonSpriteEnabled(pTag));
	}

	public void assertButtonSpriteEnabled(final Class<? extends ButtonSprite> pClass, final Object pTag) {
		Assert.assertTrue(this.isButtonSpriteEnabled(pClass, pTag));
	}

	public void assertButtonSpriteDisabled(final Object pTag) {
		Assert.assertFalse(this.isButtonSpriteEnabled(pTag));
	}

	public void assertButtonSpriteDisabled(final Class<? extends ButtonSprite> pClass, final Object pTag) {
		Assert.assertFalse(this.isButtonSpriteEnabled(pClass, pTag));
	}

	public void assertButtonSpriteState(final State pExpecedState, final Object pTag) {
		Assert.assertEquals(pExpecedState, this.getButtonSpriteState(ButtonSprite.class, pTag));
	}

	public void assertButtonSpriteState(final State pExpecedState, final Class<? extends ButtonSprite> pClass, final Object pTag) {
		Assert.assertEquals(pExpecedState, this.getButtonSpriteState(pClass, pTag));
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Inject a click on the center of an {@link IEntity}.
	 *
	 * @param pTag
	 */
	public void clickOnEntity(final Object pTag) {
		this.clickOnEntity(IEntity.class, pTag);
	}

	/**
	 * Inject a click on the center of an {@link IEntity}.
	 * 
	 * @param pClass
	 * @param pTag
	 */
	public void clickOnEntity(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pTag);

		final float[] sceneCenterCoordinate = result.getSceneCenterCoordinates();
		final float sceneX = sceneCenterCoordinate[Constants.VERTEX_INDEX_X];
		final float sceneY = sceneCenterCoordinate[Constants.VERTEX_INDEX_Y];

		this.clickOnScene(sceneX, sceneY);
	}

	public void clickOnEntity(final Object pTag, final float pLocalX, final float pLocalY) {
		this.clickOnEntity(IEntity.class, pTag, pLocalX, pLocalY);
	}

	public void clickOnEntity(final Class<? extends IEntity> pClass, final Object pTag, final float pLocalX, final float pLocalY) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		final float[] sceneCenterCoordinate = result.convertLocalToSceneCoordinates(pLocalX, pLocalY);
		final float sceneX = sceneCenterCoordinate[Constants.VERTEX_INDEX_X];
		final float sceneY = sceneCenterCoordinate[Constants.VERTEX_INDEX_Y];

		this.clickOnScene(sceneX, sceneY);
	}

	public boolean isEntityVisible(final Object pTag) {
		return this.isEntityVisible(IEntity.class, pTag);
	}

	public boolean isEntityVisible(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.isVisible();
	}

	public float getEntityPositionX(final Object pTag) {
		return this.getEntityPositionX(IEntity.class, pTag);
	}

	public float getEntityPositionX(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getX();
	}

	public float getEntityPositionY(final Object pTag) {
		return this.getEntityPositionY(IEntity.class, pTag);
	}

	public float getEntityPositionY(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getY();
	}

	public int getEntityZIndex(final Object pTag) {
		return this.getEntityZIndex(IEntity.class, pTag);
	}

	public int getEntityZIndex(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getZIndex();
	}

	public boolean isEntityRotated(final Object pTag) {
		return this.isEntityRotated(IEntity.class, pTag);
	}

	public boolean isEntityRotated(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.isRotated();
	}

	public float getEntityRotation(final Object pTag) {
		return this.getEntityRotation(IEntity.class, pTag);
	}

	public float getEntityRotation(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getRotation();
	}

	public float getEntityRotationCenterX(final Object pTag) {
		return this.getEntityRotationCenterX(IEntity.class, pTag);
	}

	public float getEntityRotationCenterX(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getRotationCenterX();
	}

	public float getEntityRotationCenterY(final Object pTag) {
		return this.getEntityRotationCenterX(IEntity.class, pTag);
	}

	public float getEntityRotationCenterY(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getRotationCenterY();
	}

	public boolean isEntityScaled(final Object pTag) {
		return this.isEntityScaled(IEntity.class, pTag);
	}

	public boolean isEntityScaled(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.isScaled();
	}

	public float getEntityScaleX(final Object pTag) {
		return this.getEntityScaleX(IEntity.class, pTag);
	}

	public float getEntityScaleX(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getScaleX();
	}

	public float getEntityScaleY(final Object pTag) {
		return this.getEntityScaleY(IEntity.class, pTag);
	}

	public float getEntityScaleY(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getScaleY();
	}

	public float getEntityScaleCenterX(final Object pTag) {
		return this.getEntityScaleCenterX(IEntity.class, pTag);
	}

	public float getEntityScaleCenterX(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getScaleCenterX();
	}

	public float getEntityScaleCenterY(final Object pTag) {
		return this.getEntityScaleCenterX(IEntity.class, pTag);
	}

	public float getEntityScaleCenterY(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getScaleCenterY();
	}

	public boolean isEntitySkewed(final Object pTag) {
		return this.isEntitySkewed(IEntity.class, pTag);
	}

	public boolean isEntitySkewed(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.isSkewed();
	}

	public float getEntitySkewX(final Object pTag) {
		return this.getEntitySkewX(IEntity.class, pTag);
	}

	public float getEntitySkewX(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getSkewX();
	}

	public float getEntitySkewY(final Object pTag) {
		return this.getEntitySkewY(IEntity.class, pTag);
	}

	public float getEntitySkewY(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getSkewY();
	}

	public float getEntitySkewCenterX(final Object pTag) {
		return this.getEntitySkewCenterX(IEntity.class, pTag);
	}

	public float getEntitySkewCenterX(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getSkewCenterX();
	}

	public float getEntitySkewCenterY(final Object pTag) {
		return this.getEntitySkewCenterX(IEntity.class, pTag);
	}

	public float getEntitySkewCenterY(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getSkewCenterY();
	}

	public float getEntityColorRed(final Object pTag) {
		return this.getEntityColorRed(IEntity.class, pTag);
	}

	public float getEntityColorRed(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getRed();
	}

	public float getEntityColorGreen(final Object pTag) {
		return this.getEntityColorGreen(IEntity.class, pTag);
	}

	public float getEntityColorGreen(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getGreen();
	}

	public float getEntityColorBlue(final Object pTag) {
		return this.getEntityColorBlue(IEntity.class, pTag);
	}

	public float getEntityColorBlue(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getBlue();
	}

	public float getEntityColorAlpha(final Object pTag) {
		return this.getEntityColorAlpha(IEntity.class, pTag);
	}

	public float getEntityColorAlpha(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getAlpha();
	}

	public Color getEntityColor(final Object pTag) {
		return this.getEntityColor(IEntity.class, pTag);
	}

	public Color getEntityColor(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getColor();
	}

	public int getEntityChildCount(final Object pTag) {
		return this.getEntityChildCount(IEntity.class, pTag);
	}

	public int getEntityChildCount(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getChildCount();
	}

	public IEntity getEntityParent(final Object pTag) {
		return this.getEntityParent(IEntity.class, pTag);
	}

	public IEntity getEntityParent(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getParent();
	}

	public IEntity getEntityChild(final int pIndex, final Object pTag) {
		return this.getEntityChild(pIndex, IEntity.class, pTag);
	}

	public IEntity getEntityChild(final int pIndex, final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getChildByIndex(pIndex);
	}

	public boolean isEntityCullingEnabled(final Object pTag) {
		return this.isEntityCullingEnabled(IEntity.class, pTag);
	}

	public boolean isEntityCullingEnabled(final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.isCullingEnabled();
	}

	public boolean isEntityCulled(final Camera pCamera, final Object pTag) {
		return this.isEntityCulled(pCamera, IEntity.class, pTag);
	}

	public boolean isEntityCulled(final Camera pCamera, final Class<? extends IEntity> pClass, final Object pTag) {
		final IEntity result = this.getUniqueEntityByTag(pClass, pTag);

		return result.isCulled(pCamera);
	}

	public boolean isButtonSpriteEnabled(final Object pTag) {
		return this.isButtonSpriteEnabled(ButtonSprite.class, pTag);
	}

	public boolean isButtonSpriteEnabled(final Class<? extends ButtonSprite> pClass, final Object pTag) {
		final ButtonSprite result = this.getUniqueEntityByTag(pClass, pTag);

		return result.isEnabled();
	}

	public State getButtonSpriteState(final Object pTag) {
		return this.getButtonSpriteState(ButtonSprite.class, pTag);
	}

	public State getButtonSpriteState(final Class<? extends ButtonSprite> pClass, final Object pTag) {
		final ButtonSprite result = this.getUniqueEntityByTag(pClass, pTag);

		return result.getState();
	}

	public void clickOnScene(final float pSceneX, final float pSceneY) {
		final TouchEvent sceneTouchEvent = TouchEvent.obtain(pSceneX, pSceneY, TouchEvent.ACTION_DOWN, 0, null);

		final Camera camera = this.getEngine().getCamera();
		camera.convertSceneToSurfaceTouchEvent(sceneTouchEvent, camera.getSurfaceWidth(), camera.getSurfaceHeight());

		final float surfaceX = sceneTouchEvent.getX();
		final float surfaceY = sceneTouchEvent.getY();

		sceneTouchEvent.recycle();

		this.clickOnSurface(surfaceX, surfaceY);
	}

	public void clickOnSurface(final float pSurfaceX, final float pSurfaceY) {
		final long downTime = SystemClock.uptimeMillis();
		final long eventTime = SystemClock.uptimeMillis();

		final MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, pSurfaceX, pSurfaceY, 0);
		final MotionEvent upEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, pSurfaceX, pSurfaceY, 0);

		final ITouchController touchController = this.getEngine().getTouchController();

		touchController.onHandleMotionEvent(downEvent);
		touchController.onHandleMotionEvent(upEvent);
	}

	public IEntity getUniqueEntityByTag(final Object pTag) {
		final ArrayList<IEntity> result = this.querySceneByTag(pTag);
		this.assertListSize(1, result);
		return result.get(0);
	}

	public <T extends IEntity> T getUniqueEntityByTag(final Class<T> pClass, final Object pTag) {
		final ArrayList<IEntity> result = this.querySceneByTag(pClass, pTag);
		this.assertListSize(1, result);
		return pClass.cast(result.get(0));
	}

	public ArrayList<IEntity> querySceneByTag(final Class<? extends IEntity> pClass, final Object pTag) {
		return this.getEngine().getScene().query(new IEntityMatcher() {
			@Override
			public boolean matches(final IEntity pEntity) {
				return pClass.isInstance(pEntity) && pTag.equals(pEntity.getUserData());
			}
		});
	}

	public ArrayList<IEntity> querySceneByTag(final Object pTag) {
		return this.getEngine().getScene().query(new IEntityMatcher() {
			@Override
			public boolean matches(final IEntity pEntity) {
				return pTag.equals(pEntity.getUserData());
			}
		});
	}

	private void assertListSize(final int pSize, final List<IEntity> pEntityList) {
		Assert.assertEquals(pSize, pEntityList.size());
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
