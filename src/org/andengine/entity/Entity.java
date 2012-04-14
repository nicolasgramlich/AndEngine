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
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.adt.transformation.Transformation;
import org.andengine.util.algorithm.collision.EntityCollisionChecker;
import org.andengine.util.call.ParameterCallable;
import org.andengine.util.color.Color;


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

	protected Color mColor = new Color(1, 1, 1, 1);

	protected float mX;
	protected float mY;

	protected float mOffsetCenterX = IEntity.OFFSET_CENTER_X_DEFAULT;
	protected float mOffsetCenterY = IEntity.OFFSET_CENTER_Y_DEFAULT;
	private float mAbsoluteOffsetCenterX;
	private float mAbsoluteOffsetCenterY;

	private float mWidth;
	private float mHeight;

	protected float mRotation = IEntity.ROTATION_DEFAULT;

	protected float mRotationCenterX = IEntity.ROTATION_CENTER_X_DEFAULT;
	protected float mRotationCenterY = IEntity.ROTATION_CENTER_Y_DEFAULT;
	private float mAbsoluteRotationCenterX;
	private float mAbsoluteRotationCenterY;

	protected float mScaleX = IEntity.SCALE_X_DEFAULT;
	protected float mScaleY = IEntity.SCALE_Y_DEFAULT;

	protected float mScaleCenterX = IEntity.SCALE_CENTER_X_DEFAULT;
	protected float mScaleCenterY = IEntity.SCALE_CENTER_Y_DEFAULT;
	private float mAbsoluteScaleCenterX;
	private float mAbsoluteScaleCenterY;

	protected float mSkewX = IEntity.SKEW_X_DEFAULT;
	protected float mSkewY = IEntity.SKEW_Y_DEFAULT;

	protected float mSkewCenterX = IEntity.SKEW_CENTER_X_DEFAULT;
	protected float mSkewCenterY = IEntity.SKEW_CENTER_Y_DEFAULT;
	private float mAbsoluteSkewCenterX;
	private float mAbsoluteSkewCenterY;

	private boolean mLocalToParentTransformationDirty = true;
	private boolean mParentToLocalTransformationDirty = true;

	private final Transformation mLocalToParentTransformation = new Transformation(); // TODO Lazy init?
	private final Transformation mParentToLocalTransformation = new Transformation(); // TODO Lazy init?

	private final Transformation mLocalToSceneTransformation = new Transformation(); // TODO Lazy init!
	private final Transformation mSceneToLocalTransformation = new Transformation(); // TODO Lazy init!

	private Object mUserData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Entity() {
		this(0, 0);
	}

	public Entity(final float pX, final float pY) {
		this.mX = pX;
		this.mY = pY;

		this.updateAbsoluteOffsetCenter();
		this.updateAbsoluteRotationCenter();
		this.updateAbsoluteScaleCenter();
		this.updateAbsoluteSkewCenter();
	}

	public Entity(final float pX, final float pY, final float pWidth, final float pHeight) {
		this.mX = pX;
		this.mY = pY;

		this.setSize(pWidth, pHeight);
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
		if(pOtherEntity instanceof Line) {
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

		this.updateAbsoluteOffsetCenterX();
		this.updateAbsoluteRotationCenterX();
		this.updateAbsoluteScaleCenterX();
		this.updateAbsoluteSkewCenterX();
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

		this.updateAbsoluteOffsetCenterY();
		this.updateAbsoluteRotationCenterY();
		this.updateAbsoluteScaleCenterY();
		this.updateAbsoluteSkewCenterY();
	}

	@Override
	public void setSize(final float pWidth, final float pHeight) {
		this.mWidth = pWidth;
		this.mHeight = pHeight;

		this.updateAbsoluteOffsetCenter();
		this.updateAbsoluteRotationCenter();
		this.updateAbsoluteScaleCenter();
		this.updateAbsoluteSkewCenter();
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

		this.updateAbsoluteOffsetCenterX();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setOffsetCenterY(final float pOffsetCenterY) {
		this.mOffsetCenterY = pOffsetCenterY;

		this.updateAbsoluteOffsetCenterY();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setOffsetCenter(final float pOffsetCenterX, final float pOffsetCenterY) {
		this.mOffsetCenterX = pOffsetCenterX;
		this.mOffsetCenterY = pOffsetCenterY;

		this.updateAbsoluteOffsetCenter();

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

		this.updateAbsoluteRotationCenterX();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setRotationCenterY(final float pRotationCenterY) {
		this.mRotationCenterY = pRotationCenterY;

		this.updateAbsoluteRotationCenterY();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setRotationCenter(final float pRotationCenterX, final float pRotationCenterY) {
		this.mRotationCenterX = pRotationCenterX;
		this.mRotationCenterY = pRotationCenterY;

		this.updateAbsoluteRotationCenter();

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

		this.updateAbsoluteScaleCenterX();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setScaleCenterY(final float pScaleCenterY) {
		this.mScaleCenterY = pScaleCenterY;

		this.updateAbsoluteScaleCenterY();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setScaleCenter(final float pScaleCenterX, final float pScaleCenterY) {
		this.mScaleCenterX = pScaleCenterX;
		this.mScaleCenterY = pScaleCenterY;

		this.updateAbsoluteScaleCenter();

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

		this.updateAbsoluteSkewCenterX();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkewCenterY(final float pSkewCenterY) {
		this.mSkewCenterY = pSkewCenterY;

		this.updateAbsoluteSkewCenterY();

		this.mLocalToParentTransformationDirty = true;
		this.mParentToLocalTransformationDirty = true;
	}

	@Override
	public void setSkewCenter(final float pSkewCenterX, final float pSkewCenterY) {
		this.mSkewCenterX = pSkewCenterX;
		this.mSkewCenterY = pSkewCenterY;

		this.updateAbsoluteSkewCenter();

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

	/**
	 * @param pRed from <code>0.0f</code> to <code>1.0f</code>
	 */
	@Override
	public void setRed(final float pRed) {
		if(this.mColor.setRedChecking(pRed)) {
			this.onUpdateColor();
		}
	}

	/**
	 * @param pGreen from <code>0.0f</code> to <code>1.0f</code>
	 */
	@Override
	public void setGreen(final float pGreen) {
		if(this.mColor.setGreenChecking(pGreen)) {
			this.onUpdateColor();
		}
	}

	/**
	 * @param pBlue from <code>0.0f</code> to <code>1.0f</code>
	 */
	@Override
	public void setBlue(final float pBlue) {
		if(this.mColor.setBlueChecking(pBlue)) {
			this.onUpdateColor();
		}
	}

	/**
	 * @param pAlpha from <code>0.0f</code> (transparent) to <code>1.0f</code> (opaque)
	 */
	@Override
	public void setAlpha(final float pAlpha) {
		if(this.mColor.setAlphaChecking(pAlpha)) {
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
		if(this.mColor.setChecking(pRed, pGreen, pBlue)) { // TODO Is this check worth it?
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
		if(this.mColor.setChecking(pRed, pGreen, pBlue, pAlpha)) { // TODO Is this check worth it?
			this.onUpdateColor();
		}
	}

	@Override
	public int getChildCount() {
		if(this.mChildren == null) {
			return 0;
		}
		return this.mChildren.size();
	}

	@Override
	public IEntity getChild(final int pTag) {
		if(this.mChildren == null) {
			return null;
		}
		for(int i = this.mChildren.size() - 1; i >= 0; i--) {
			final IEntity child = this.mChildren.get(i);
			if(child.getTag() == pTag) {
				return child;
			}
		}
		return null;
	}

	@Override
	public IEntity getChild(final IEntityMatcher pEntityMatcher) {
		if(this.mChildren == null) {
			return null;
		}
		return this.mChildren.get(pEntityMatcher);
	}

	@Override
	public IEntity getFirstChild() {
		if(this.mChildren == null) {
			return null;
		}
		return this.mChildren.get(0);
	}

	@Override
	public IEntity getLastChild() {
		if(this.mChildren == null) {
			return null;
		}
		return this.mChildren.get(this.mChildren.size() - 1);
	}

	@Override
	public ArrayList<IEntity> query(final IEntityMatcher pEntityMatcher) {
		return this.query(pEntityMatcher, new ArrayList<IEntity>());
	}

	@Override
	public <L extends List<IEntity>> L query(final IEntityMatcher pEntityMatcher, final L pResult) {
		final int childCount = this.getChildCount();
		for(int i = 0; i < childCount; i++) {
			final IEntity item = this.mChildren.get(i);
			if(pEntityMatcher.matches(item)) {
				pResult.add(item);
			}

			item.query(pEntityMatcher, pResult);
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
		for(int i = 0; i < childCount; i++) {
			final IEntity item = this.mChildren.get(i);
			if(pEntityMatcher.matches(item)) {
				pResult.add((S)item);
			}

			item.queryForSubclass(pEntityMatcher, pResult);
		}

		return pResult;
	}

	@Override
	public boolean detachSelf() {
		final IEntity parent = this.mParent;
		if(parent != null) {
			return parent.detachChild(this);
		} else {
			return false;
		}
	}

	@Override
	public void detachChildren() {
		if(this.mChildren == null) {
			return;
		}
		this.mChildren.clear(Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public void attachChild(final IEntity pEntity) throws IllegalStateException {
		this.assertEntityHasNoParent(pEntity);

		if(this.mChildren == null) {
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
		if(this.mChildren == null) {
			return;
		}
		if(pImmediate) {
			ZIndexSorter.getInstance().sort(this.mChildren);
		} else {
			this.mChildrenSortPending = true;
		}
	}

	@Override
	public void sortChildren(final IEntityComparator pEntityComparator) {
		if(this.mChildren == null) {
			return;
		}
		ZIndexSorter.getInstance().sort(this.mChildren, pEntityComparator);
	}

	@Override
	public boolean detachChild(final IEntity pEntity) {
		if(this.mChildren == null) {
			return false;
		}
		return this.mChildren.remove(pEntity, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public IEntity detachChild(final int pTag) {
		if(this.mChildren == null) {
			return null;
		}
		for(int i = this.mChildren.size() - 1; i >= 0; i--) {
			if(this.mChildren.get(i).getTag() == pTag) {
				final IEntity removed = this.mChildren.remove(i);
				Entity.PARAMETERCALLABLE_DETACHCHILD.call(removed);
				return removed;
			}
		}
		return null;
	}

	@Override
	public IEntity detachChild(final IEntityMatcher pEntityMatcher) {
		if(this.mChildren == null) {
			return null;
		}
		return this.mChildren.remove(pEntityMatcher, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public boolean detachChildren(final IEntityMatcher pEntityMatcher) {
		if(this.mChildren == null) {
			return false;
		}
		return this.mChildren.removeAll(pEntityMatcher, Entity.PARAMETERCALLABLE_DETACHCHILD);
	}

	@Override
	public void callOnChildren(final IEntityParameterCallable pEntityParameterCallable) {
		if(this.mChildren == null) {
			return;
		}
		this.mChildren.call(pEntityParameterCallable);
	}

	@Override
	public void callOnChildren(final IEntityParameterCallable pEntityParameterCallable, final IEntityMatcher pEntityMatcher) {
		if(this.mChildren == null) {
			return;
		}
		this.mChildren.call(pEntityMatcher, pEntityParameterCallable);
	}

	@Override
	public void registerUpdateHandler(final IUpdateHandler pUpdateHandler) {
		if(this.mUpdateHandlers == null) {
			this.allocateUpdateHandlers();
		}
		this.mUpdateHandlers.add(pUpdateHandler);
	}

	@Override
	public boolean unregisterUpdateHandler(final IUpdateHandler pUpdateHandler) {
		if(this.mUpdateHandlers == null) {
			return false;
		}
		return this.mUpdateHandlers.remove(pUpdateHandler);
	}

	@Override
	public boolean unregisterUpdateHandlers(final IUpdateHandlerMatcher pUpdateHandlerMatcher) {
		if(this.mUpdateHandlers == null) {
			return false;
		}
		return this.mUpdateHandlers.removeAll(pUpdateHandlerMatcher);
	}

	@Override
	public void clearUpdateHandlers() {
		if(this.mUpdateHandlers == null) {
			return;
		}
		this.mUpdateHandlers.clear();
	}

	@Override
	public void registerEntityModifier(final IEntityModifier pEntityModifier) {
		if(this.mEntityModifiers == null) {
			this.allocateEntityModifiers();
		}
		this.mEntityModifiers.add(pEntityModifier);
	}

	@Override
	public boolean unregisterEntityModifier(final IEntityModifier pEntityModifier) {
		if(this.mEntityModifiers == null) {
			return false;
		}
		return this.mEntityModifiers.remove(pEntityModifier);
	}

	@Override
	public boolean unregisterEntityModifiers(final IEntityModifierMatcher pEntityModifierMatcher) {
		if(this.mEntityModifiers == null) {
			return false;
		}
		return this.mEntityModifiers.removeAll(pEntityModifierMatcher);
	}

	@Override
	public void clearEntityModifiers() {
		if(this.mEntityModifiers == null) {
			return;
		}
		this.mEntityModifiers.clear();
	}

	@Override
	public float[] getSceneCenterCoordinates() {
		return this.convertLocalToSceneCoordinates(this.mWidth * 0.5f, this.mHeight * 0.5f);
	}

	@Override
	public Transformation getLocalToParentTransformation() {
		final Transformation localToParentTransformation = this.mLocalToParentTransformation;
		if(this.mLocalToParentTransformationDirty) {
			localToParentTransformation.setToIdentity();

			/* Offset. */
			localToParentTransformation.postTranslate(this.mAbsoluteOffsetCenterX, this.mAbsoluteOffsetCenterY);

			/* Scale. */
			final float scaleX = this.mScaleX;
			final float scaleY = this.mScaleY;
			if((scaleX != 1) || (scaleY != 1)) {
				final float scaleCenterX = this.mAbsoluteScaleCenterX;
				final float scaleCenterY = this.mAbsoluteScaleCenterY;

				/* TODO Check if it is worth to check for scaleCenterX == 0 && scaleCenterY == 0 as the two postTranslate can be saved.
				 * The same obviously applies for all similar occurrences of this pattern in this class. */

				localToParentTransformation.postTranslate(-scaleCenterX, -scaleCenterY);
				localToParentTransformation.postScale(scaleX, scaleY);
				localToParentTransformation.postTranslate(scaleCenterX, scaleCenterY);
			}

			/* Skew. */
			final float skewX = this.mSkewX;
			final float skewY = this.mSkewY;
			if((skewX != 0) || (skewY != 0)) {
				final float skewCenterX = this.mAbsoluteSkewCenterX;
				final float skewCenterY = this.mAbsoluteSkewCenterY;

				localToParentTransformation.postTranslate(-skewCenterX, -skewCenterY);
				localToParentTransformation.postSkew(skewX, skewY);
				localToParentTransformation.postTranslate(skewCenterX, skewCenterY);
			}

			/* Rotation. */
			final float rotation = this.mRotation;
			if(rotation != 0) {
				final float rotationCenterX = this.mAbsoluteRotationCenterX;
				final float rotationCenterY = this.mAbsoluteRotationCenterY;

				localToParentTransformation.postTranslate(-rotationCenterX, -rotationCenterY);
				localToParentTransformation.postRotate(rotation);
				localToParentTransformation.postTranslate(rotationCenterX, rotationCenterY);
			}

			/* Translation. */
			localToParentTransformation.postTranslate(this.mX, this.mY);

			this.mLocalToParentTransformationDirty = false;
		}
		return localToParentTransformation;
	}

	@Override
	public Transformation getParentToLocalTransformation() {
		final Transformation parentToLocalTransformation = this.mParentToLocalTransformation;
		if(this.mParentToLocalTransformationDirty) {
			parentToLocalTransformation.setToIdentity();

			/* Translation. */
			parentToLocalTransformation.postTranslate(-this.mX, -this.mY);

			/* Rotation. */
			final float rotation = this.mRotation;
			if(rotation != 0) {
				final float absoluteRotationCenterX = this.mAbsoluteRotationCenterX;
				final float absoluteRotationCenterY = this.mAbsoluteRotationCenterY;

				parentToLocalTransformation.postTranslate(-absoluteRotationCenterX, -absoluteRotationCenterY);
				parentToLocalTransformation.postRotate(-rotation);
				parentToLocalTransformation.postTranslate(absoluteRotationCenterX, absoluteRotationCenterY);
			}

			/* Skew. */
			final float skewX = this.mSkewX;
			final float skewY = this.mSkewY;
			if((skewX != 0) || (skewY != 0)) {
				final float absoluteSkewCenterX = this.mAbsoluteSkewCenterX;
				final float absoluteSkewCenterY = this.mAbsoluteSkewCenterY;

				parentToLocalTransformation.postTranslate(-absoluteSkewCenterX, -absoluteSkewCenterY);
				parentToLocalTransformation.postSkew(-skewX, -skewY);
				parentToLocalTransformation.postTranslate(absoluteSkewCenterX, absoluteSkewCenterY);
			}

			/* Scale. */
			final float scaleX = this.mScaleX;
			final float scaleY = this.mScaleY;
			if((scaleX != 1) || (scaleY != 1)) {
				final float absoluteScaleCenterX = this.mAbsoluteScaleCenterX;
				final float absoluteScaleCenterY = this.mAbsoluteScaleCenterY;

				parentToLocalTransformation.postTranslate(-absoluteScaleCenterX, -absoluteScaleCenterY);
				parentToLocalTransformation.postScale(1 / scaleX, 1 / scaleY); // TODO Division could be replaced by a multiplication of 'scale(X/Y)Inverse'...
				parentToLocalTransformation.postTranslate(absoluteScaleCenterX, absoluteScaleCenterY);
			}

			/* Offset. */
			parentToLocalTransformation.postTranslate(-this.mAbsoluteOffsetCenterX, -this.mAbsoluteOffsetCenterY);

			this.mParentToLocalTransformationDirty = false;
		}
		return parentToLocalTransformation;
	}

	@Override
	public Transformation getLocalToSceneTransformation() {
		// TODO Cache if parent(recursive) not dirty.
		final Transformation localToSceneTransformation = this.mLocalToSceneTransformation;
		localToSceneTransformation.setTo(this.getLocalToParentTransformation());

		final IEntity parent = this.mParent;
		if(parent != null) {
			localToSceneTransformation.postConcat(parent.getLocalToSceneTransformation());
		}

		return localToSceneTransformation;
	}

	@Override
	public Transformation getSceneToLocalTransformation() {
		// TODO Cache if parent(recursive) not dirty.
		final Transformation sceneToLocalTransformation = this.mSceneToLocalTransformation;
		sceneToLocalTransformation.setTo(this.getParentToLocalTransformation());

		final IEntity parent = this.mParent;
		if(parent != null) {
			sceneToLocalTransformation.preConcat(parent.getSceneToLocalTransformation());
		}

		return sceneToLocalTransformation;
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.IEntity#convertLocalToSceneCoordinates(float, float)
	 */
	@Override
	public float[] convertLocalToSceneCoordinates(final float pX, final float pY) {
		return this.convertLocalToSceneCoordinates(pX, pY, Entity.VERTICES_LOCAL_TO_SCENE_TMP);
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.IEntity#convertLocalToSceneCoordinates(float, float, float[])
	 */
	@Override
	public float[] convertLocalToSceneCoordinates(final float pX, final float pY, final float[] pReuse) {
		final Transformation localToSceneTransformation = this.getLocalToSceneTransformation();

		pReuse[Constants.VERTEX_INDEX_X] = pX;
		pReuse[Constants.VERTEX_INDEX_Y] = pY;

		localToSceneTransformation.transform(pReuse);

		return pReuse;
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.IEntity#convertLocalToSceneCoordinates(float[])
	 */
	@Override
	public float[] convertLocalToSceneCoordinates(final float[] pCoordinates) {
		return this.convertSceneToLocalCoordinates(pCoordinates, Entity.VERTICES_LOCAL_TO_SCENE_TMP);
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.IEntity#convertLocalToSceneCoordinates(float[], float[])
	 */
	@Override
	public float[] convertLocalToSceneCoordinates(final float[] pCoordinates, final float[] pReuse) {
		final Transformation localToSceneTransformation = this.getLocalToSceneTransformation();

		pReuse[Constants.VERTEX_INDEX_X] = pCoordinates[Constants.VERTEX_INDEX_X];
		pReuse[Constants.VERTEX_INDEX_Y] = pCoordinates[Constants.VERTEX_INDEX_Y];

		localToSceneTransformation.transform(pReuse);

		return pReuse;
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.IEntity#convertSceneToLocalCoordinates(float, float)
	 */
	@Override
	public float[] convertSceneToLocalCoordinates(final float pX, final float pY) {
		return this.convertSceneToLocalCoordinates(pX, pY, Entity.VERTICES_SCENE_TO_LOCAL_TMP);
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.IEntity#convertSceneToLocalCoordinates(float, float, float[])
	 */
	@Override
	public float[] convertSceneToLocalCoordinates(final float pX, final float pY, final float[] pReuse) {
		pReuse[Constants.VERTEX_INDEX_X] = pX;
		pReuse[Constants.VERTEX_INDEX_Y] = pY;

		this.getSceneToLocalTransformation().transform(pReuse);

		return pReuse;
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.IEntity#convertSceneToLocalCoordinates(float[])
	 */
	@Override
	public float[] convertSceneToLocalCoordinates(final float[] pCoordinates) {
		return this.convertSceneToLocalCoordinates(pCoordinates, Entity.VERTICES_SCENE_TO_LOCAL_TMP);
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.IEntity#convertSceneToLocalCoordinates(float[], float[])
	 */
	@Override
	public float[] convertSceneToLocalCoordinates(final float[] pCoordinates, final float[] pReuse) {
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
		if(this.mVisible && !(this.mCullingEnabled && this.isCulled(pCamera))) {
			this.onManagedDraw(pGLState, pCamera);
		}
	}

	@Override
	public final void onUpdate(final float pSecondsElapsed) {
		if(!this.mIgnoreUpdate) {
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

		if(this.mEntityModifiers != null) {
			this.mEntityModifiers.reset();
		}

		if(this.mChildren != null) {
			final SmartList<IEntity> entities = this.mChildren;
			for(int i = entities.size() - 1; i >= 0; i--) {
				entities.get(i).reset();
			}
		}
	}

	@Override
	public void dispose() {
		if(!this.mDisposed) {
			this.mDisposed = true;
		} else {
			throw new AlreadyDisposedException();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		if(!this.mDisposed) {
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

		if((this.mChildren != null) && (this.mChildren.size() > 0)) {
			pStringBuilder.append(" [");
			final SmartList<IEntity> entities = this.mChildren;
			for(int i = 0; i < entities.size(); i++) {
				entities.get(i).toString(pStringBuilder);
				if(i < (entities.size() - 1)) {
					pStringBuilder.append(", ");
				}
			}
			pStringBuilder.append("]");
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
		pGLState.translateModelViewGLMatrixf(-this.mAbsoluteOffsetCenterX, -this.mAbsoluteOffsetCenterY, 0);
	}

	protected void applyTranslation(final GLState pGLState) {
		pGLState.translateModelViewGLMatrixf(this.mX, this.mY, 0);
	}

	protected void applyRotation(final GLState pGLState) {
		final float rotation = this.mRotation;

		if(rotation != 0) {
			final float absoluteRotationCenterX = this.mAbsoluteRotationCenterX;
			final float absoluteRotationCenterY = this.mAbsoluteRotationCenterY;

			pGLState.translateModelViewGLMatrixf(absoluteRotationCenterX, absoluteRotationCenterY, 0);
			pGLState.rotateModelViewGLMatrixf(rotation, 0, 0, 1);
			pGLState.translateModelViewGLMatrixf(-absoluteRotationCenterX, -absoluteRotationCenterY, 0);

			/* TODO There is a special, but very likely case when mRotationCenter and mScaleCenter are the same.
			 * In that case the last glTranslatef of the rotation and the first glTranslatef of the scale is superfluous.
			 * The problem is that applyRotation and applyScale would need to be "merged" in order to efficiently check for that condition.  */
		}
	}

	protected void applySkew(final GLState pGLState) {
		final float skewX = this.mSkewX;
		final float skewY = this.mSkewY;

		if((skewX != 0) || (skewY != 0)) {
			final float absoluteSkewCenterX = this.mAbsoluteSkewCenterX;
			final float absoluteSkewCenterY = this.mAbsoluteSkewCenterY;

			pGLState.translateModelViewGLMatrixf(absoluteSkewCenterX, absoluteSkewCenterY, 0);
			pGLState.skewModelViewGLMatrixf(skewX, skewY);
			pGLState.translateModelViewGLMatrixf(-absoluteSkewCenterX, -absoluteSkewCenterY, 0);
		}
	}

	protected void applyScale(final GLState pGLState) {
		final float scaleX = this.mScaleX;
		final float scaleY = this.mScaleY;

		if((scaleX != 1) || (scaleY != 1)) {
			final float absoluteScaleCenterX = this.mAbsoluteScaleCenterX;
			final float absoluteScaleCenterY = this.mAbsoluteScaleCenterY;

			pGLState.translateModelViewGLMatrixf(absoluteScaleCenterX, absoluteScaleCenterY, 0);
			pGLState.scaleModelViewGLMatrixf(scaleX, scaleY, 1);
			pGLState.translateModelViewGLMatrixf(-absoluteScaleCenterX, -absoluteScaleCenterY, 0);
		}
	}

	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		pGLState.pushModelViewGLMatrix();
		{
			this.onApplyTransformations(pGLState);

			final SmartList<IEntity> children = this.mChildren;
			if((children == null) || !this.mChildrenVisible) {
				/* Draw only self. */
				this.preDraw(pGLState, pCamera);
				this.draw(pGLState, pCamera);
				this.postDraw(pGLState, pCamera);
			} else {
				if(this.mChildrenSortPending) {
					ZIndexSorter.getInstance().sort(this.mChildren);
					this.mChildrenSortPending = false;
				}

				final int childCount = children.size();
				int i = 0;

				{ /* Draw children behind this Entity. */
					for(; i < childCount; i++) {
						final IEntity child = children.get(i);
						if(child.getZIndex() < 0) {
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
					for(; i < childCount; i++) {
						children.get(i).onDraw(pGLState, pCamera);
					}
				}
			}
		}
		pGLState.popModelViewGLMatrix();
	}

	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(this.mEntityModifiers != null) {
			this.mEntityModifiers.onUpdate(pSecondsElapsed);
		}
		if(this.mUpdateHandlers != null) {
			this.mUpdateHandlers.onUpdate(pSecondsElapsed);
		}

		if((this.mChildren != null) && !this.mChildrenIgnoreUpdate) {
			final SmartList<IEntity> entities = this.mChildren;
			final int entityCount = entities.size();
			for(int i = 0; i < entityCount; i++) {
				entities.get(i).onUpdate(pSecondsElapsed);
			}
		}
	}
	
	private void updateAbsoluteOffsetCenter() {
		this.updateAbsoluteOffsetCenterX();
		this.updateAbsoluteOffsetCenterY();
	}
	
	private void updateAbsoluteOffsetCenterX() {
		this.mAbsoluteOffsetCenterX = this.mOffsetCenterX * this.mWidth;
	}
	
	private void updateAbsoluteOffsetCenterY() {
		this.mAbsoluteOffsetCenterY = this.mOffsetCenterY * this.mHeight;
	}

	private void updateAbsoluteRotationCenter() {
		this.updateAbsoluteRotationCenterX();
		this.updateAbsoluteRotationCenterY();
	}

	private void updateAbsoluteRotationCenterX() {
		this.mAbsoluteRotationCenterX = this.mRotationCenterX * this.mWidth;
	}

	private void updateAbsoluteRotationCenterY() {
		this.mAbsoluteRotationCenterY = this.mRotationCenterY * this.mHeight;
	}

	private void updateAbsoluteScaleCenter() {
		this.updateAbsoluteScaleCenterX();
		this.updateAbsoluteScaleCenterY();
	}

	private void updateAbsoluteScaleCenterX() {
		this.mAbsoluteScaleCenterX = this.mScaleCenterX * this.mWidth;
	}

	private void updateAbsoluteScaleCenterY() {
		this.mAbsoluteScaleCenterY = this.mScaleCenterY * this.mHeight;
	}

	private void updateAbsoluteSkewCenter() {
		this.updateAbsoluteSkewCenterX();
		this.updateAbsoluteSkewCenterY();
	}

	private void updateAbsoluteSkewCenterX() {
		this.mAbsoluteSkewCenterX = this.mSkewCenterX * this.mWidth;
	}

	private void updateAbsoluteSkewCenterY() {
		this.mAbsoluteSkewCenterY = this.mSkewCenterY * this.mHeight;
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
		if(pEntity.hasParent()) {
			final String entityName = pEntity.getClass().getSimpleName();
			final String currentParentName = pEntity.getParent().getClass().getSimpleName();
			final String newParentName = this.getClass().getSimpleName();
			throw new IllegalStateException("pEntity '" + entityName +"' already has a parent '" + currentParentName + ". New parent: '" + newParentName + "'!");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
