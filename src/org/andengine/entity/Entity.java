package org.andengine.entity;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.UpdateHandlerList;
import org.andengine.entity.modifier.EntityModifierList;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierMatcher;
import org.andengine.entity.primitive.Line;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.Constants;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.color.ColorUtils;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.adt.transformation.Transformation;
import org.andengine.util.algorithm.collision.EntityCollisionChecker;
import org.andengine.util.call.ParameterCallable;


/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 12:00:48 - 08.03.2010
 */
public class Entity implements IEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CHILDREN_CAPACITY_DEFAULT = 4;
	private static final int ENTITYMODIFIERS_CAPACITY_DEFAULT = 4;
	private static final int UPDATEHANDLERS_CAPACITY_DEFAULT = 4;

	private static final float[] VERTICES_SCENE_TO_LOCAL_TMP = new float[2];
	private static final float[] VERTICES_LOCAL_TO_SCENE_TMP = new float[2];
	private static final float[] VERTICES_PARENT_TO_LOCAL_TMP = new float[2];
	private static final float[] VERTICES_LOCAL_TO_PARENT_TMP = new float[2];

	private static final ParameterCallable<IEntity> PARAMETERCALLABLE_DETACHCHILD = new ParameterCallable<IEntity>() {
		@Override
		public void call(final IEntity pEntity) {
			pEntity.setParent(null);
			pEntity.onDetached();
		}
	};

	// ===========================================================
	// Fields
	// ===========================================================

	protected boolean mDisposed;
	protected boolean mVisible = true;
	protected boolean mCullingEnabled;
	protected boolean mIgnoreUpdate;
	protected boolean mChildrenVisible = true;
	protected boolean mChildrenIgnoreUpdate;
	protected boolean mChildrenSortPending;

	protected int mTag = IEntity.TAG_DEFAULT;

	protected int mZIndex = IEntity.ZINDEX_DEFAULT;

	private IEntity mParent;

	protected SmartList<IEntity> mChildren;
	private EntityModifierList mEntityModifiers;
	private UpdateHandlerList mUpdateHandlers;

	protected Color mColor = new Color(Color.WHITE);

	protected float mX;
	protected float mY;

	protected float mOffsetCenterX = IEntity.OFFSET_CENTER_X_DEFAULT;
	protected float mOffsetCenterY = IEntity.OFFSET_CENTER_Y_DEFAULT;
	protected float mLocalOffsetCenterX;
	protected float mLocalOffsetCenterY;

	protected float mWidth;
	protected float mHeight;

	protected float mRotation = IEntity.ROTATION_DEFAULT;

	protected float mRotationCenterX = IEntity.ROTATION_CENTER_X_DEFAULT;
	protected float mRotationCenterY = IEntity.ROTATION_CENTER_Y_DEFAULT;
	protected float mLocalRotationCenterX;
	protected float mLocalRotationCenterY;

	protected float mScaleX = IEntity.SCALE_X_DEFAULT;
	protected float mScaleY = IEntity.SCALE_Y_DEFAULT;

	protected float mScaleCenterX = IEntity.SCALE_CENTER_X_DEFAULT;
	protected float mScaleCenterY = IEntity.SCALE_CENTER_Y_DEFAULT;
	protected float mLocalScaleCenterX;
	protected float mLocalScaleCenterY;

	protected float mSkewX = IEntity.SKEW_X_DEFAULT;
	protected float mSkewY = IEntity.SKEW_Y_DEFAULT;

	protected float mSkewCenterX = IEntity.SKEW_CENTER_X_DEFAULT;
	protected float mSkewCenterY = IEntity.SKEW_CENTER_Y_DEFAULT;
	protected float mLocalSkewCenterX;
	protected float mLocalSkewCenterY;

	private boolean mLocalToParentTransformationDirty = true;
	private boolean mParentToLocalTransformationDirty = true;

	private Transformation mLocalToParentTransformation;
	private Transformation mParentToLocalTransformation;

	private Transformation mLocalToSceneTransformation;
	private Transformation mSceneToLocalTransformation;

	private Object mUserData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Entity() {
		this(0, 0);
	}

	public Entity(final float pX, final float pY) {
		this(pX, pY, 0, 0);
	}

	public Entity(final float pX, final float pY, final float pWidth, final float pHeight) {
		this.mX = pX;
		this.mY = pY;

		this.mWidth = pWidth;
		this.mHeight = pHeight;

		this.updateLocalCenters();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onUpdateColor() {

	}

	@Override
	public boolean isDisposed() {
		return this.mDisposed;
	}

	@Override
	public boolean isVisible() {
		return this.mVisible;
	}

	@Override
	public void setVisible(final boolean pVisible) {
		this.mVisible = pVisible;
	}

	@Override
	public boolean isCullingEnabled() {
		return this.mCullingEnabled;
	}

	@Override
	public void setCullingEnabled(final boolean pCullingEnabled) {
		this.mCullingEnabled = pCullingEnabled;
	}

	@Override
	public boolean isCulled(final Camera pCamera) {
		return !EntityCollisionChecker.isVisible(pCamera, this);
	}

	@Override
	public boolean collidesWith(final IEntity pOtherEntity) {
		if (pOtherEntity instanceof Line) {
			return EntityCollisionChecker.checkCollision(this, (Line) pOtherEntity);
		} else {
			return EntityCollisionChecker.checkCollision(this, pOtherEntity);
		}
	}

	@Override
	public boolean contains(final float pX, final float pY) {
		return EntityCollisionChecker.checkContains(this, pX, pY);
	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		return false;
	}

	@Override
	public boolean isChildrenVisible() {
		return this.mChildrenVisible;
	}

	@Override
	public void setChildrenVisible(final boolean pChildrenVisible) {
		this.mChildrenVisible = pChildrenVisible;
	}

	@Override
	public boolean isIgnoreUpdate() {
		return this.mIgnoreUpdate;
	}

	@Override
	public void setIgnoreUpdate(final boolean pIgnoreUpdate) {
		this.mIgnoreUpdate = pIgnoreUpdate;
	}

	@Override
	public boolean isChildrenIgnoreUpdate() {
		return this.mChildrenIgnoreUpdate;
	}

	@Override
	public void setChildrenIgnoreUpdate(final boolean pChildrenIgnoreUpdate) {
		this.mChildrenIgnoreUpdate = pChildrenIgnoreUpdate;
	}

	@Override
	public boolean hasParent() {
		return this.mParent != null;
	}

	@Override
	public IEntity getParent() {
		return this.mParent;
	}

	@Override
	public void setParent(final IEntity pEntity) {
		this.mParent = pEntity;
	}

	@Override
	public IEntity getRootEntity() {
		IEntity rootEntity = this;

		while (rootEntity.hasParent()) {
			rootEntity = rootEntity.getParent();
		}

		return rootEntity;
	}

	@Override
	public int getTag() {
		return this.mTag;
	}

	@Override
	public void setTag(final int pTag) {
		this.mTag = pTag;
	}

	@Override
	public int getZIndex() {
		return this.mZIndex;
	}

	@Override
	public void setZIndex(final int pZIndex) {
		this.mZIndex = pZIndex;
	}

	@Override
	public float getX() {
		return this.mX;
	}

	@Override
	public float getY() {
		return this.mY;
	}

	@Override
	public void setX(final float pX) {
		this.mX = pX;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setY(final float pY) {
		this.mY = pY;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setPosition(final IEntity pOtherEntity) {
		this.setPosition(pOtherEntity.getX(), pOtherEntity.getY());
	}

	@Override
	public void setPosition(final float pX, final float pY) {
		this.mX = pX;
		this.mY = pY;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public float getWidth() {
		return this.mWidth;
	}

	@Deprecated
	@Override
	public float getWidthScaled() {
		return this.mWidth * this.mScaleX;
	}

	@Override
	public void setWidth(final float pWidth) {
		this.mWidth = pWidth;

		this.updateLocalCenterXs();
	}

	@Override
	public float getHeight() {
		return this.mHeight;
	}

	@Deprecated
	@Override
	public float getHeightScaled() {
		return this.mHeight * this.mScaleY;
	}

	@Override
	public void setHeight(final float pHeight) {
		this.mHeight = pHeight;

		this.updateLocalCenterYs();
	}

	@Override
	public void setSize(final float pWidth, final float pHeight) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;

		this.updateLocalCenters();
	}

	@Override
	public float getOffsetCenterX() {
		return this.mOffsetCenterX;
	}

	@Override
	public float getOffsetCenterY() {
		return this.mOffsetCenterY;
	}

	@Override
	public void setOffsetCenterX(final float pOffsetCenterX) {
		this.mOffsetCenterX = pOffsetCenterX;

		this.updateLocalOffsetCenterX();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setOffsetCenterY(final float pOffsetCenterY) {
		this.mOffsetCenterY = pOffsetCenterY;

		this.updateLocalOffsetCenterY();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setOffsetCenter(final float pOffsetCenterX, final float pOffsetCenterY) {
		this.mOffsetCenterX = pOffsetCenterX;
		this.mOffsetCenterY = pOffsetCenterY;

		this.updateLocalOffsetCenter();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public float getRotation() {
		return this.mRotation;
	}

	@Override
	public boolean isRotated() {
		return this.mRotation != 0;
	}

	@Override
	public void setRotation(final float pRotation) {
		this.mRotation = pRotation;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public float getRotationCenterX() {
		return this.mRotationCenterX;
	}

	@Override
	public float getRotationCenterY() {
		return this.mRotationCenterY;
	}

	@Override
	public void setRotationCenterX(final float pRotationCenterX) {
		this.mRotationCenterX = pRotationCenterX;

		this.updateLocalRotationCenterX();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setRotationCenterY(final float pRotationCenterY) {
		this.mRotationCenterY = pRotationCenterY;

		this.updateLocalRotationCenterY();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY) {
		this.mRotationCenterX = pRotationCenterX;
		this.mRotationCenterY = pRotationCenterY;

		this.updateLocalRotationCenter();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public boolean isScaled() {
		return (this.mScaleX != 1) || (this.mScaleY != 1);
	}

	@Override
	public float getScaleX() {
		return this.mScaleX;
	}

	@Override
	public float getScaleY() {
		return this.mScaleY;
	}

	@Override
	public void setScaleX(final float pScaleX) {
		this.mScaleX = pScaleX;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setScaleY(final float pScaleY) {
		this.mScaleY = pScaleY;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setScale(final float pScale) {
		this.setScale(pScale, pScale);
	}

	@Override
	public void setScale(final float pScaleX, final float pScaleY) {
		this.mScaleX = pScaleX;
		this.mScaleY = pScaleY;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public float getScaleCenterX() {
		return this.mScaleCenterX;
	}

	@Override
	public float getScaleCenterY() {
		return this.mScaleCenterY;
	}

	@Override
	public void setScaleCenterX(final float pScaleCenterX) {
		this.mScaleCenterX = pScaleCenterX;

		this.updateLocalScaleCenterX();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setScaleCenterY(final float pScaleCenterY) {
		this.mScaleCenterY = pScaleCenterY;

		this.updateLocalScaleCenterY();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY) {
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;

		this.updateLocalScaleCenter();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public boolean isSkewed() {
		return (this.mSkewX != 0) || (this.mSkewY != 0);
	}

	@Override
	public float getSkewX() {
		return this.mSkewX;
	}

	@Override
	public float getSkewY() {
		return this.mSkewY;
	}

	@Override
	public void setSkewX(final float pSkewX) {
		this.mSkewX = pSkewX;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkewY(final float pSkewY) {
		this.mSkewY = pSkewY;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkew(final float pSkew) {
		this.setSkew(pSkew, pSkew);
	}

	@Override
	public void setSkew(final float pSkewX, final float pSkewY) {
		this.mSkewX = pSkewX;
		this.mSkewY = pSkewY;

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public float getSkewCenterX() {
		return this.mSkewCenterX;
	}

	@Override
	public float getSkewCenterY() {
		return this.mSkewCenterY;
	}

	@Override
	public void setSkewCenterX(final float pSkewCenterX) {
		this.mSkewCenterX = pSkewCenterX;

		this.updateLocalSkewCenterX();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkewCenterY(final float pSkewCenterY) {
		this.mSkewCenterY = pSkewCenterY;

		this.updateLocalSkewCenterY();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkewCenter(final float pSkewCenterX, final float pSkewCenterY) {
		this.mSkewCenterX = pSkewCenterX;
		this.mSkewCenterY = pSkewCenterY;

		this.updateLocalSkewCenter();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public boolean isRotatedOrScaledOrSkewed() {
		return (this.mRotation != 0) || (this.mScaleX != 1) || (this.mScaleY != 1) || (this.mSkewX != 0) || (this.mSkewY != 0);
	}

	@Override
	public void setAnchorCenterX(final float pAnchorCenterX) {
		this.setOffsetCenterX(pAnchorCenterX);
		this.setRotationCenterX(pAnchorCenterX);
		this.setScaleCenterX(pAnchorCenterX);
		this.setSkewCenterX(pAnchorCenterX);
	}

	@Override
	public void setAnchorCenterY(final float pAnchorCenterY) {
		this.setOffsetCenterY(pAnchorCenterY);
		this.setRotationCenterY(pAnchorCenterY);
		this.setScaleCenterY(pAnchorCenterY);
		this.setSkewCenterY(pAnchorCenterY);
	}

	@Override
	public void setAnchorCenter(final float pAnchorCenterX, final float pAnchorCenterY) {
		this.setOffsetCenter(pAnchorCenterX, pAnchorCenterY);
		this.setRotationCenter(pAnchorCenterX, pAnchorCenterY);
		this.setScaleCenter(pAnchorCenterX, pAnchorCenterY);
		this.setSkewCenter(pAnchorCenterX, pAnchorCenterY);
	}

	@Override
	public float getRed() {
		return this.mColor.getRed();
	}

	@Override
	public float getGreen() {
		return this.mColor.getGreen();
	}

	@Override
	public float getBlue() {
		return this.mColor.getBlue();
	}

	@Override
	public float getAlpha() {
		return this.mColor.getAlpha();
	}

	@Override
	public Color getColor() {
		return this.mColor;
	}

	@Override
	public void setColor(final Color pColor) {
		this.mColor.set(pColor);

		this.onUpdateColor();
	}

	@Override
	public void setColor(final int pARGBPackedInt) {
		ColorUtils.convertARGBPackedIntToColor(pARGBPackedInt, this.mColor);

		this.onUpdateColor();
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 */
	@Override
	public void setRed(final float pRed) {
		if (this.mColor.setRedChecking(pRed)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 */
	@Override
	public void setGreen(final float pGreen) {
		if (this.mColor.setGreenChecking(pGreen)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 */
	@Override
	public void setBlue(final float pBlue) {
		if (this.mColor.setBlueChecking(pBlue)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pAlpha from <code>0.0f</code> (transparent) to <code>1.0f</code> (opaque)
	 */
	@Override
	public void setAlpha(final float pAlpha) {
		if (this.mColor.setAlphaChecking(pAlpha)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 */
	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue) {
		if (this.mColor.setChecking(pRed, pGreen, pBlue)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 * @param pAlpha from <code>0.0f</code> (transparent) to <code>1.0f</code> (opaque)
	 */
	@Override
	public void setColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		if (this.mColor.setChecking(pRed, pGreen, pBlue, pAlpha)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	@Override
	public int getChildCount() {
		if (this.mChildren == null) {
			return 0;
		}
		return this.mChildren.size();
	}

	@Override
	public IEntity getChildByTag(final int pTag) {
		if (this.mChildren == null) {
			return null;
		}
		for (int i = this.mChildren.size() - 1; i >= 0; i--) {
			final IEntity child = this.mChildren.get(i);
			if (child.getTag() == pTag) {
				return child;
			}
		}
		return null;
	}

	@Override
	public IEntity getChildByIndex(final int pIndex) {
		if (this.mChildren == null) {
			return null;
		}
		return this.mChildren.get(pIndex);
	}

	@Override
	public IEntity getChildByMatcher(final IEntityMatcher pEntityMatcher) {
		if (this.mChildren == null) {
			return null;
		}
		return this.mChildren.get(pEntityMatcher);
	}

	@Override
	public IEntity getFirstChild() {
		if (this.mChildren == null) {
			return null;
		}
		return this.mChildren.get(0);
	}

	@Override
	public IEntity getLastChild() {
		if (this.mChildren == null) {
			return null;
		}
		return this.mChildren.get(this.mChildren.size() - 1);
	}

	@Override
	public ArrayList<IEntity> query(final IEntityMatcher pEntityMatcher) {
		return this.query(pEntityMatcher, new ArrayList<IEntity>());
	}

	@Override
	public IEntity queryFirst(final IEntityMatcher pEntityMatcher) {
		return this.queryFirstForSubclass(pEntityMatcher);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends IEntity> S queryFirstForSubclass(final IEntityMatcher pEntityMatcher) {
		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final IEntity child = this.mChildren.get(i);
			if (pEntityMatcher.matches(child)) {
				return (S)child;
			}

			final S childQueryFirst = child.queryFirstForSubclass(pEntityMatcher);
			if (childQueryFirst != null) {
				return childQueryFirst;
			}
		}

		return null;
	}

	@Override
	public <L extends List<IEntity>> L query(final IEntityMatcher pEntityMatcher, final L pResult) {
		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final IEntity child = this.mChildren.get(i);
			if (pEntityMatcher.matches(child)) {
				pResult.add(child);
			}

			child.query(pEntityMatcher, pResult);
		}

		return pResult;
	}

	@Override
	public <S extends IEntity> ArrayList<S> queryForSubclass(final IEntityMatcher pEntityMatcher) throws ClassCastException {
		return this.queryForSubclass(pEntityMatcher, new ArrayList<S>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <L extends List<S>, S extends IEntity> L queryForSubclass(final IEntityMatcher pEntityMatcher, final L pResult) throws ClassCastException {
		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final IEntity child = this.mChildren.get(i);
			if (pEntityMatcher.matches(child)) {
				pResult.add((S) child);
			}

			child.queryForSubclass(pEntityMatcher, pResult);
		}

		return pResult;
	}

	@Override
	public boolean detachSelf() {
		final IEntity parent = this.mParent;
		if (parent != null) {
			return parent.detachChild(this);
		} else {
			return false;
		}
	}

	@Override
	public void detachChildren() {
		if (this.mChildren == null) {
			return;
		}
		this.mChildren.clear(Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public void attachChild(final IEntity pEntity) throws IllegalStateException {
		this.assertEntityHasNoParent(pEntity);

		if (this.mChildren == null) {
			this.allocateChildren();
		}
		this.mChildren.add(pEntity);
		pEntity.setParent(this);
		pEntity.onAttached();
	}

	@Override
	public void sortChildren() {
		this.sortChildren(true);
	}

	@Override
	public void sortChildren(final boolean pImmediate) {
		if (this.mChildren == null) {
			return;
		}
		if (pImmediate) {
			ZIndexSorter.getInstance().sort(this.mChildren);
		} else {
			this.mChildrenSortPending = true;
		}
	}

	@Override
	public void sortChildren(final IEntityComparator pEntityComparator) {
		if (this.mChildren == null) {
			return;
		}
		ZIndexSorter.getInstance().sort(this.mChildren, pEntityComparator);
	}

	@Override
	public boolean detachChild(final IEntity pEntity) {
		if (this.mChildren == null) {
			return false;
		}
		return this.mChildren.remove(pEntity, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public IEntity detachChild(final int pTag) {
		if (this.mChildren == null) {
			return null;
		}
		for (int i = this.mChildren.size() - 1; i >= 0; i--) {
			if (this.mChildren.get(i).getTag() == pTag) {
				final IEntity removed = this.mChildren.remove(i);
				Entity.PARAMETERCALLABLE_DETACHCHILD.call(removed);
				return removed;
			}
		}
		return null;
	}

	@Override
	public IEntity detachChild(final IEntityMatcher pEntityMatcher) {
		if (this.mChildren == null) {
			return null;
		}
		return this.mChildren.remove(pEntityMatcher, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public boolean detachChildren(final IEntityMatcher pEntityMatcher) {
		if (this.mChildren == null) {
			return false;
		}
		return this.mChildren.removeAll(pEntityMatcher, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public void callOnChildren(final IEntityParameterCallable pEntityParameterCallable) {
		if (this.mChildren == null) {
			return;
		}
		this.mChildren.call(pEntityParameterCallable);
	}

	@Override
	public void callOnChildren(final IEntityParameterCallable pEntityParameterCallable, final IEntityMatcher pEntityMatcher) {
		if (this.mChildren == null) {
			return;
		}
		this.mChildren.call(pEntityMatcher, pEntityParameterCallable);
	}

	@Override
	public void registerUpdateHandler(final IUpdateHandler pUpdateHandler) {
		if (this.mUpdateHandlers == null) {
			this.allocateUpdateHandlers();
		}
		this.mUpdateHandlers.add(pUpdateHandler);
	}

	@Override
	public boolean unregisterUpdateHandler(final IUpdateHandler pUpdateHandler) {
		if (this.mUpdateHandlers == null) {
			return false;
		}
		return this.mUpdateHandlers.remove(pUpdateHandler);
	}

	@Override
	public boolean unregisterUpdateHandlers(final IUpdateHandlerMatcher pUpdateHandlerMatcher) {
		if (this.mUpdateHandlers == null) {
			return false;
		}
		return this.mUpdateHandlers.removeAll(pUpdateHandlerMatcher);
	}

	@Override
	public int getUpdateHandlerCount() {
		if (this.mUpdateHandlers == null) {
			return 0;
		}
		return this.mUpdateHandlers.size();
	}

	@Override
	public void clearUpdateHandlers() {
		if (this.mUpdateHandlers == null) {
			return;
		}
		this.mUpdateHandlers.clear();
	}

	@Override
	public void registerEntityModifier(final IEntityModifier pEntityModifier) {
		if (this.mEntityModifiers == null) {
			this.allocateEntityModifiers();
		}
		this.mEntityModifiers.add(pEntityModifier);
	}

	@Override
	public boolean unregisterEntityModifier(final IEntityModifier pEntityModifier) {
		if (this.mEntityModifiers == null) {
			return false;
		}
		return this.mEntityModifiers.remove(pEntityModifier);
	}

	@Override
	public boolean unregisterEntityModifiers(final IEntityModifierMatcher pEntityModifierMatcher) {
		if (this.mEntityModifiers == null) {
			return false;
		}
		return this.mEntityModifiers.removeAll(pEntityModifierMatcher);
	}

	@Override
	public int getEntityModifierCount() {
		if (this.mEntityModifiers == null) {
			return 0;
		}
		return this.mEntityModifiers.size();
	}

	@Override
	public void resetEntityModifiers() {
		if (this.mEntityModifiers != null) {
			this.mEntityModifiers.reset();
		}
	}

	@Override
	public void clearEntityModifiers() {
		if (this.mEntityModifiers == null) {
			return;
		}
		this.mEntityModifiers.clear();
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		return this.convertLocalCoordinatesToSceneCoordinates(this.mWidth * 0.5f, this.mHeight * 0.5f);
	}

	@Override
	public float[] getSceneCenterCoordinates(final float[] pReuse) {
		return this.convertLocalCoordinatesToSceneCoordinates(this.mWidth * 0.5f, this.mHeight * 0.5f, pReuse);
	}

	@Override
	public Transformation getLocalToParentTransformation() {
		if (this.mLocalToParentTransformation == null) {
			this.mLocalToParentTransformation = new Transformation();
		}

		final Transformation localToParentTransformation = this.mLocalToParentTransformation;
		if (this.mLocalToParentTransformationDirty) {
			localToParentTransformation.setToIdentity();

			/* Scale. */
			final float scaleX = this.mScaleX;
			final float scaleY = this.mScaleY;
			if ((scaleX != 1) || (scaleY != 1)) {
				final float scaleCenterX = this.mLocalScaleCenterX;
				final float scaleCenterY = this.mLocalScaleCenterY;

				/* TODO Check if it is worth to check for scaleCenterX == 0 && scaleCenterY == 0 as the two postTranslate can be saved.
				 * The same obviously applies for all similar occurrences of this pattern in this class. */

				localToParentTransformation.postTranslate(-scaleCenterX, -scaleCenterY);
				localToParentTransformation.postScale(scaleX, scaleY);
				localToParentTransformation.postTranslate(scaleCenterX, scaleCenterY);
			}

			/* Skew. */
			final float skewX = this.mSkewX;
			final float skewY = this.mSkewY;
			if ((skewX != 0) || (skewY != 0)) {
				final float skewCenterX = this.mLocalSkewCenterX;
				final float skewCenterY = this.mLocalSkewCenterY;

				localToParentTransformation.postTranslate(-skewCenterX, -skewCenterY);
				localToParentTransformation.postSkew(skewX, skewY);
				localToParentTransformation.postTranslate(skewCenterX, skewCenterY);
			}

			/* Rotation. */
			final float rotation = this.mRotation;
			if (rotation != 0) {
				final float rotationCenterX = this.mLocalRotationCenterX;
				final float rotationCenterY = this.mLocalRotationCenterY;

				localToParentTransformation.postTranslate(-rotationCenterX, -rotationCenterY);
				localToParentTransformation.postRotate(-rotation);
				localToParentTransformation.postTranslate(rotationCenterX, rotationCenterY);
			}

			/* Translation. */
			localToParentTransformation.postTranslate(this.mX, this.mY);

			/* Offset. */
			localToParentTransformation.postTranslate(-this.mLocalOffsetCenterX, -this.mLocalOffsetCenterY);

			this.mLocalToParentTransformationDirty = false;
		}
		return localToParentTransformation;
	}

	@Override
	public Transformation getParentToLocalTransformation() {
		if (this.mParentToLocalTransformation == null) {
			this.mParentToLocalTransformation = new Transformation();
		}

		final Transformation parentToLocalTransformation = this.mParentToLocalTransformation;
		if (this.mParentToLocalTransformationDirty) {
			parentToLocalTransformation.setToIdentity();

			/* Offset. */
			parentToLocalTransformation.postTranslate(this.mLocalOffsetCenterX, this.mLocalOffsetCenterY);

			/* Translation. */
			parentToLocalTransformation.postTranslate(-this.mX, -this.mY);

			/* Rotation. */
			final float rotation = this.mRotation;
			if (rotation != 0) {
				final float localRotationCenterX = this.mLocalRotationCenterX;
				final float localRotationCenterY = this.mLocalRotationCenterY;

				parentToLocalTransformation.postTranslate(-localRotationCenterX, -localRotationCenterY);
				parentToLocalTransformation.postRotate(rotation);
				parentToLocalTransformation.postTranslate(localRotationCenterX, localRotationCenterY);
			}

			/* Skew. */
			final float skewX = this.mSkewX;
			final float skewY = this.mSkewY;
			if ((skewX != 0) || (skewY != 0)) {
				final float localSkewCenterX = this.mLocalSkewCenterX;
				final float localSkewCenterY = this.mLocalSkewCenterY;

				parentToLocalTransformation.postTranslate(-localSkewCenterX, -localSkewCenterY);
				parentToLocalTransformation.postSkew(-skewX, -skewY);
				parentToLocalTransformation.postTranslate(localSkewCenterX, localSkewCenterY);
			}

			/* Scale. */
			final float scaleX = this.mScaleX;
			final float scaleY = this.mScaleY;
			if ((scaleX != 1) || (scaleY != 1)) {
				final float localScaleCenterX = this.mLocalScaleCenterX;
				final float localScaleCenterY = this.mLocalScaleCenterY;

				parentToLocalTransformation.postTranslate(-localScaleCenterX, -localScaleCenterY);
				parentToLocalTransformation.postScale(1 / scaleX, 1 / scaleY); // TODO Division could be replaced by a multiplication of 'scale(X/Y)Inverse'...
				parentToLocalTransformation.postTranslate(localScaleCenterX, localScaleCenterY);
			}

			this.mParentToLocalTransformationDirty = false;
		}
		return parentToLocalTransformation;
	}

	@Override
	public Transformation getLocalToSceneTransformation() {
		if (this.mLocalToSceneTransformation == null) {
			this.mLocalToSceneTransformation = new Transformation();
		}

		// TODO Cache if parent(recursive) not dirty.
		final Transformation localToSceneTransformation = this.mLocalToSceneTransformation;
		localToSceneTransformation.setTo(this.getLocalToParentTransformation());

		final IEntity parent = this.mParent;
		if (parent != null) {
			localToSceneTransformation.postConcat(parent.getLocalToSceneTransformation());
		}

		return localToSceneTransformation;
	}

	@Override
	public Transformation getSceneToLocalTransformation() {
		if (this.mSceneToLocalTransformation == null) {
			this.mSceneToLocalTransformation = new Transformation();
		}

		// TODO Cache if parent(recursive) not dirty.
		final Transformation sceneToLocalTransformation = this.mSceneToLocalTransformation;
		sceneToLocalTransformation.setTo(this.getParentToLocalTransformation());

		final IEntity parent = this.mParent;
		if (parent != null) {
			sceneToLocalTransformation.preConcat(parent.getSceneToLocalTransformation());
		}

		return sceneToLocalTransformation;
	}

	@Override
	public float[] convertLocalCoordinatesToParentCoordinates(final float pX, final float pY) {
		return this.convertLocalCoordinatesToParentCoordinates(pX, pY, Entity.VERTICES_LOCAL_TO_PARENT_TMP);
	}

	@Override
	public float[] convertLocalCoordinatesToParentCoordinates(final float pX, final float pY, final float[] pReuse) {
		final Transformation localToParentTransformation = this.getLocalToParentTransformation();

		pReuse[Constants.VERTEX_INDEX_X] = pX;
		pReuse[Constants.VERTEX_INDEX_Y] = pY;

		localToParentTransformation.transform(pReuse);

		return pReuse;
	}

	@Override
	public float[] convertLocalCoordinatesToParentCoordinates(final float[] pCoordinates) {
		return this.convertLocalCoordinatesToParentCoordinates(pCoordinates, Entity.VERTICES_LOCAL_TO_PARENT_TMP);
	}

	@Override
	public float[] convertLocalCoordinatesToParentCoordinates(final float[] pCoordinates, final float[] pReuse) {
		final Transformation localToParentTransformation = this.getLocalToParentTransformation();

		pReuse[Constants.VERTEX_INDEX_X] = pCoordinates[Constants.VERTEX_INDEX_X];
		pReuse[Constants.VERTEX_INDEX_Y] = pCoordinates[Constants.VERTEX_INDEX_Y];

		localToParentTransformation.transform(pReuse);

		return pReuse;
	}

	@Override
	public float[] convertParentCoordinatesToLocalCoordinates(final float pX, final float pY) {
		return this.convertParentCoordinatesToLocalCoordinates(pX, pY, Entity.VERTICES_PARENT_TO_LOCAL_TMP);
	}

	@Override
	public float[] convertParentCoordinatesToLocalCoordinates(final float pX, final float pY, final float[] pReuse) {
		pReuse[Constants.VERTEX_INDEX_X] = pX;
		pReuse[Constants.VERTEX_INDEX_Y] = pY;

		this.getParentToLocalTransformation().transform(pReuse);

		return pReuse;
	}

	@Override
	public float[] convertParentCoordinatesToLocalCoordinates(final float[] pCoordinates) {
		return this.convertParentCoordinatesToLocalCoordinates(pCoordinates, Entity.VERTICES_PARENT_TO_LOCAL_TMP);
	}

	@Override
	public float[] convertParentCoordinatesToLocalCoordinates(final float[] pCoordinates, final float[] pReuse) {
		pReuse[Constants.VERTEX_INDEX_X] = pCoordinates[Constants.VERTEX_INDEX_X];
		pReuse[Constants.VERTEX_INDEX_Y] = pCoordinates[Constants.VERTEX_INDEX_Y];

		this.getParentToLocalTransformation().transform(pReuse);

		return pReuse;
	}

	@Override
	public float[] convertLocalCoordinatesToSceneCoordinates(final float pX, final float pY) {
		return this.convertLocalCoordinatesToSceneCoordinates(pX, pY, Entity.VERTICES_LOCAL_TO_SCENE_TMP);
	}

	@Override
	public float[] convertLocalCoordinatesToSceneCoordinates(final float pX, final float pY, final float[] pReuse) {
		final Transformation localToSceneTransformation = this.getLocalToSceneTransformation();

		pReuse[Constants.VERTEX_INDEX_X] = pX;
		pReuse[Constants.VERTEX_INDEX_Y] = pY;

		localToSceneTransformation.transform(pReuse);

		return pReuse;
	}

	@Override
	public float[] convertLocalCoordinatesToSceneCoordinates(final float[] pCoordinates) {
		return this.convertLocalCoordinatesToSceneCoordinates(pCoordinates, Entity.VERTICES_LOCAL_TO_SCENE_TMP);
	}

	@Override
	public float[] convertLocalCoordinatesToSceneCoordinates(final float[] pCoordinates, final float[] pReuse) {
		final Transformation localToSceneTransformation = this.getLocalToSceneTransformation();

		pReuse[Constants.VERTEX_INDEX_X] = pCoordinates[Constants.VERTEX_INDEX_X];
		pReuse[Constants.VERTEX_INDEX_Y] = pCoordinates[Constants.VERTEX_INDEX_Y];

		localToSceneTransformation.transform(pReuse);

		return pReuse;
	}

	@Override
	public float[] convertSceneCoordinatesToLocalCoordinates(final float pX, final float pY) {
		return this.convertSceneCoordinatesToLocalCoordinates(pX, pY, Entity.VERTICES_SCENE_TO_LOCAL_TMP);
	}

	@Override
	public float[] convertSceneCoordinatesToLocalCoordinates(final float pX, final float pY, final float[] pReuse) {
		pReuse[Constants.VERTEX_INDEX_X] = pX;
		pReuse[Constants.VERTEX_INDEX_Y] = pY;

		this.getSceneToLocalTransformation().transform(pReuse);

		return pReuse;
	}

	@Override
	public float[] convertSceneCoordinatesToLocalCoordinates(final float[] pCoordinates) {
		return this.convertSceneCoordinatesToLocalCoordinates(pCoordinates, Entity.VERTICES_SCENE_TO_LOCAL_TMP);
	}

	@Override
	public float[] convertSceneCoordinatesToLocalCoordinates(final float[] pCoordinates, final float[] pReuse) {
		pReuse[Constants.VERTEX_INDEX_X] = pCoordinates[Constants.VERTEX_INDEX_X];
		pReuse[Constants.VERTEX_INDEX_Y] = pCoordinates[Constants.VERTEX_INDEX_Y];

		this.getSceneToLocalTransformation().transform(pReuse);

		return pReuse;
	}

	@Override
	public void onAttached() {

	}

	@Override
	public void onDetached() {

	}

	@Override
	public Object getUserData() {
		return this.mUserData;
	}

	@Override
	public void setUserData(final Object pUserData) {
		this.mUserData = pUserData;
	}

	@Override
	public final void onDraw(final GLState pGLState, final Camera pCamera) {
		if (this.mVisible && !(this.mCullingEnabled && this.isCulled(pCamera))) {
			this.onManagedDraw(pGLState, pCamera);
		}
	}

	@Override
	public final void onUpdate(final float pSecondsElapsed) {
		if (!this.mIgnoreUpdate) {
			this.onManagedUpdate(pSecondsElapsed);
		}
	}

	@Override
	public void reset() {
		this.mVisible = true;
		this.mCullingEnabled = false;
		this.mIgnoreUpdate = false;
		this.mChildrenVisible = true;
		this.mChildrenIgnoreUpdate = false;

		this.mRotation = 0;
		this.mScaleX = 1;
		this.mScaleY = 1;
		this.mSkewX = 0;
		this.mSkewY = 0;

		this.resetRotationCenter();
		this.resetSkewCenter();
		this.resetScaleCenter();

		this.mColor.reset();

		this.resetEntityModifiers();

		if (this.mChildren != null) {
			final SmartList<IEntity> children = this.mChildren;
			for (int i = children.size() - 1; i >= 0; i--) {
				final IEntity child = children.get(i);
				child.reset();
			}
		}
	}

	@Override
	public void dispose() {
		if (!this.mDisposed) {
			this.mDisposed = true;
		} else {
			throw new AlreadyDisposedException();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		if (!this.mDisposed) {
			this.dispose();
		}
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		this.toString(stringBuilder);
		return stringBuilder.toString();
	}

	@Override
	public void toString(final StringBuilder pStringBuilder) {
		pStringBuilder.append(this.getClass().getSimpleName());

		if ((this.mChildren != null) && (this.mChildren.size() > 0)) {
			final SmartList<IEntity> children = this.mChildren;

			pStringBuilder.append(" [");

			final int childCount = children.size();
			for (int i = 0; i < childCount; i++) {
				final IEntity child = children.get(i);
				child.toString(pStringBuilder);

				if (i < (childCount - 1)) {
					pStringBuilder.append(", ");
				}
			}
			pStringBuilder.append(']');
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * @param pGLState the currently active {@link GLState} i.e. to apply transformations to.
	 * @param pCamera the currently active {@link Camera} i.e. to be used for culling.
	 */
	protected void preDraw(final GLState pGLState, final Camera pCamera) {

	}

	/**
	 * @param pGLState the currently active {@link GLState} i.e. to apply transformations to.
	 * @param pCamera the currently active {@link Camera} i.e. to be used for culling.
	 */
	protected void draw(final GLState pGLState, final Camera pCamera) {

	}

	/**
	 * @param pGLState the currently active {@link GLState} i.e. to apply transformations to.
	 * @param pCamera the currently active {@link Camera} i.e. to be used for culling.
	 */
	protected void postDraw(final GLState pGLState, final Camera pCamera) {

	}

	private void allocateEntityModifiers() {
		this.mEntityModifiers = new EntityModifierList(this, Entity.ENTITYMODIFIERS_CAPACITY_DEFAULT);
	}

	private void allocateChildren() {
		this.mChildren = new SmartList<IEntity>(Entity.CHILDREN_CAPACITY_DEFAULT);
	}

	private void allocateUpdateHandlers() {
		this.mUpdateHandlers = new UpdateHandlerList(Entity.UPDATEHANDLERS_CAPACITY_DEFAULT);
	}

	protected void onApplyTransformations(final GLState pGLState) {
		/* Offset. */
		this.applyOffset(pGLState);

		/* Translation. */
		this.applyTranslation(pGLState);

		/* Rotation. */
		this.applyRotation(pGLState);

		/* Skew. */
		this.applySkew(pGLState);

		/* Scale. */
		this.applyScale(pGLState);
	}

	protected void applyOffset(final GLState pGLState) {
		pGLState.translateModelViewGLMatrixf(-this.mLocalOffsetCenterX, -this.mLocalOffsetCenterY, 0);
	}

	protected void applyTranslation(final GLState pGLState) {
		pGLState.translateModelViewGLMatrixf(this.mX, this.mY, 0);
	}

	protected void applyRotation(final GLState pGLState) {
		final float rotation = this.mRotation;

		if (rotation != 0) {
			final float localRotationCenterX = this.mLocalRotationCenterX;
			final float localRotationCenterY = this.mLocalRotationCenterY;

			pGLState.translateModelViewGLMatrixf(localRotationCenterX, localRotationCenterY, 0);
			pGLState.rotateModelViewGLMatrixf(-rotation, 0, 0, 1);
			pGLState.translateModelViewGLMatrixf(-localRotationCenterX, -localRotationCenterY, 0);

			/* TODO There is a special, but very likely case when mRotationCenter and mScaleCenter are the same.
			 * In that case the last glTranslatef of the rotation and the first glTranslatef of the scale is superfluous.
			 * The problem is that applyRotation and applyScale would need to be "merged" in order to efficiently check for that condition.  */
		}
	}

	protected void applySkew(final GLState pGLState) {
		final float skewX = this.mSkewX;
		final float skewY = this.mSkewY;

		if ((skewX != 0) || (skewY != 0)) {
			final float localSkewCenterX = this.mLocalSkewCenterX;
			final float localSkewCenterY = this.mLocalSkewCenterY;

			pGLState.translateModelViewGLMatrixf(localSkewCenterX, localSkewCenterY, 0);
			pGLState.skewModelViewGLMatrixf(skewX, skewY);
			pGLState.translateModelViewGLMatrixf(-localSkewCenterX, -localSkewCenterY, 0);
		}
	}

	protected void applyScale(final GLState pGLState) {
		final float scaleX = this.mScaleX;
		final float scaleY = this.mScaleY;

		if ((scaleX != 1) || (scaleY != 1)) {
			final float localScaleCenterX = this.mLocalScaleCenterX;
			final float localScaleCenterY = this.mLocalScaleCenterY;

			pGLState.translateModelViewGLMatrixf(localScaleCenterX, localScaleCenterY, 0);
			pGLState.scaleModelViewGLMatrixf(scaleX, scaleY, 1);
			pGLState.translateModelViewGLMatrixf(-localScaleCenterX, -localScaleCenterY, 0);
		}
	}

	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		pGLState.pushModelViewGLMatrix();
		{
			this.onApplyTransformations(pGLState);

			final SmartList<IEntity> children = this.mChildren;
			if ((children == null) || !this.mChildrenVisible) {
				/* Draw only self. */
				this.preDraw(pGLState, pCamera);
				this.draw(pGLState, pCamera);
				this.postDraw(pGLState, pCamera);
			} else {
				if (this.mChildrenSortPending) {
					ZIndexSorter.getInstance().sort(this.mChildren);
					this.mChildrenSortPending = false;
				}

				final int childCount = children.size();
				int i = 0;

				{ /* Draw children behind this Entity. */
					for (; i < childCount; i++) {
						final IEntity child = children.get(i);
						if (child.getZIndex() < 0) {
							child.onDraw(pGLState, pCamera);
						} else {
							break;
						}
					}
				}

				/* Draw self. */
				this.preDraw(pGLState, pCamera);
				this.draw(pGLState, pCamera);
				this.postDraw(pGLState, pCamera);

				{ /* Draw children in front of this Entity. */
					for (; i < childCount; i++) {
						children.get(i).onDraw(pGLState, pCamera);
					}
				}
			}
		}
		pGLState.popModelViewGLMatrix();
	}

	protected void onManagedUpdate(final float pSecondsElapsed) {
		if (this.mEntityModifiers != null) {
			this.mEntityModifiers.onUpdate(pSecondsElapsed);
		}
		if (this.mUpdateHandlers != null) {
			this.mUpdateHandlers.onUpdate(pSecondsElapsed);
		}

		if ((this.mChildren != null) && !this.mChildrenIgnoreUpdate) {
			final SmartList<IEntity> children = this.mChildren;
			final int entityCount = children.size();
			for (int i = 0; i < entityCount; i++) {
				final IEntity child = children.get(i);
				child.onUpdate(pSecondsElapsed);
			}
		}
	}

	protected void updateLocalCenters() {
		this.updateLocalCenterXs();
		this.updateLocalCenterYs();
	}

	protected void updateLocalCenterXs() {
		this.updateLocalOffsetCenterX();
		this.updateLocalRotationCenterX();
		this.updateLocalScaleCenterX();
		this.updateLocalSkewCenterX();
	}

	protected void updateLocalCenterYs() {
		this.updateLocalOffsetCenterY();
		this.updateLocalRotationCenterY();
		this.updateLocalScaleCenterY();
		this.updateLocalSkewCenterY();
	}

	protected void updateLocalOffsetCenter() {
		this.updateLocalOffsetCenterX();
		this.updateLocalOffsetCenterY();
	}

	protected void updateLocalOffsetCenterX() {
		this.mLocalOffsetCenterX = this.mOffsetCenterX * this.mWidth;
	}

	protected void updateLocalOffsetCenterY() {
		this.mLocalOffsetCenterY = this.mOffsetCenterY * this.mHeight;
	}

	protected void updateLocalRotationCenter() {
		this.updateLocalRotationCenterX();
		this.updateLocalRotationCenterY();
	}

	protected void updateLocalRotationCenterX() {
		this.mLocalRotationCenterX = this.mRotationCenterX * this.mWidth;
	}

	protected void updateLocalRotationCenterY() {
		this.mLocalRotationCenterY = this.mRotationCenterY * this.mHeight;
	}

	protected void updateLocalScaleCenter() {
		this.updateLocalScaleCenterX();
		this.updateLocalScaleCenterY();
	}

	protected void updateLocalScaleCenterX() {
		this.mLocalScaleCenterX = this.mScaleCenterX * this.mWidth;
	}

	protected void updateLocalScaleCenterY() {
		this.mLocalScaleCenterY = this.mScaleCenterY * this.mHeight;
	}

	protected void updateLocalSkewCenter() {
		this.updateLocalSkewCenterX();
		this.updateLocalSkewCenterY();
	}

	protected void updateLocalSkewCenterX() {
		this.mLocalSkewCenterX = this.mSkewCenterX * this.mWidth;
	}

	protected void updateLocalSkewCenterY() {
		this.mLocalSkewCenterY = this.mSkewCenterY * this.mHeight;
	}

	public void resetRotationCenter() {
		this.setRotationCenter(IEntity.ROTATION_CENTER_X_DEFAULT, IEntity.ROTATION_CENTER_Y_DEFAULT);
	}

	public void resetScaleCenter() {
		this.setScaleCenter(IEntity.SCALE_CENTER_X_DEFAULT, IEntity.SCALE_CENTER_Y_DEFAULT);
	}

	public void resetSkewCenter() {
		this.setSkewCenter(IEntity.SKEW_CENTER_X_DEFAULT, IEntity.SKEW_CENTER_Y_DEFAULT);
	}

	private void assertEntityHasNoParent(final IEntity pEntity) throws IllegalStateException {
		if (pEntity.hasParent()) {
			final String entityClassName = pEntity.getClass().getSimpleName();
			final String currentParentClassName = pEntity.getParent().getClass().getSimpleName();
			final String newParentClassName = this.getClass().getSimpleName();
			throw new IllegalStateException("pEntity '" + entityClassName + "' already has a parent: '" + currentParentClassName + "'. New parent: '" + newParentClassName + "'!");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
