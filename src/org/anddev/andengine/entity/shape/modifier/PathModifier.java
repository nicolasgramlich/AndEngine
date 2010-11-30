package org.anddev.andengine.entity.shape.modifier;

import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.modifier.ease.IEaseFunction;
import org.anddev.andengine.util.modifier.IModifier;
import org.anddev.andengine.util.modifier.SequenceModifier;
import org.anddev.andengine.util.modifier.SequenceModifier.ISubSequenceModifierListener;

import android.util.FloatMath;

/**
 * @author Nicolas Gramlich
 * @since 16:50:02 - 16.06.2010
 */
public class PathModifier extends ShapeModifier {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final SequenceModifier<IShape> mSequenceModifier;

	private IPathModifierListener mPathModifierListener;

	private final Path mPath;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PathModifier(final float pDuration, final Path pPath) {
		this(pDuration, pPath, null, IEaseFunction.DEFAULT);
	}

	public PathModifier(final float pDuration, final Path pPath, final IEaseFunction pEaseFunction) {
		this(pDuration, pPath, null, pEaseFunction);
	}

	public PathModifier(final float pDuration, final Path pPath, final IShapeModifierListener pShapeModiferListener) {
		this(pDuration, pPath, pShapeModiferListener, null, IEaseFunction.DEFAULT);
	}

	public PathModifier(final float pDuration, final Path pPath, final IShapeModifierListener pShapeModiferListener, final IEaseFunction pEaseFunction) {
		this(pDuration, pPath, pShapeModiferListener, null, pEaseFunction);
	}

	public PathModifier(final float pDuration, final Path pPath, final IShapeModifierListener pShapeModiferListener, final IPathModifierListener pPathModifierListener) throws IllegalArgumentException {
		this(pDuration, pPath, pShapeModiferListener, pPathModifierListener, IEaseFunction.DEFAULT);
	}

	public PathModifier(final float pDuration, final Path pPath, final IShapeModifierListener pShapeModiferListener, final IPathModifierListener pPathModifierListener, final IEaseFunction pEaseFunction) throws IllegalArgumentException {
		final int pathSize = pPath.getSize();

		if (pathSize < 2) {
			throw new IllegalArgumentException("Path needs at least 2 waypoints!");
		}

		this.mPath = pPath;
		this.mModifierListener = pShapeModiferListener;
		this.mPathModifierListener = pPathModifierListener;

		final MoveModifier[] moveModifiers = new MoveModifier[pathSize - 1];

		final float[] coordinatesX = pPath.getCoordinatesX();
		final float[] coordinatesY = pPath.getCoordinatesY();

		final float velocity = pPath.getLength() / pDuration;

		final int modifierCount = moveModifiers.length;
		for(int i = 0; i < modifierCount; i++) {
			final float duration = pPath.getSegmentLength(i) / velocity;

			if(i == 0) {
				/* When the first modifier is initialized, we have to
				 * fire onWaypointPassed of mPathModifierListener. */
				moveModifiers[i] = new MoveModifier(duration, coordinatesX[i], coordinatesX[i + 1], coordinatesY[i], coordinatesY[i + 1], null, pEaseFunction){
					@Override
					protected void onManagedInitialize(final IShape pShape) {
						super.onManagedInitialize(pShape);
						if(PathModifier.this.mPathModifierListener != null) {
							PathModifier.this.mPathModifierListener.onWaypointPassed(PathModifier.this, pShape, 0);
						}
					}
				};
			} else {
				moveModifiers[i] = new MoveModifier(duration, coordinatesX[i], coordinatesX[i + 1], coordinatesY[i], coordinatesY[i + 1], null, pEaseFunction);
			}
		}


		/* Create a new SequenceModifier and register the listeners that
		 * call through to mShapeModifierListener and mPathModifierListener. */
		this.mSequenceModifier = new SequenceModifier<IShape>(
				new IShapeModifierListener() {
					@Override
					public void onModifierFinished(final IModifier<IShape> pShapeModifier, final IShape pShape) {
						if(PathModifier.this.mPathModifierListener != null) {
							PathModifier.this.mPathModifierListener.onWaypointPassed(PathModifier.this, pShape, modifierCount);
						}
						if(PathModifier.this.mModifierListener != null) {
							PathModifier.this.mModifierListener.onModifierFinished(PathModifier.this, pShape);
						}
					}
				},
				new ISubSequenceModifierListener<IShape>() {
					@Override
					public void onSubSequenceFinished(final IModifier<IShape> pShapeModifier, final IShape pShape, final int pIndex) {
						if(PathModifier.this.mPathModifierListener != null) {
							PathModifier.this.mPathModifierListener.onWaypointPassed(PathModifier.this, pShape, pIndex);
						}
					}
				},
				moveModifiers
		);
	}

	protected PathModifier(final PathModifier pPathModifier) {
		this.mPath = pPathModifier.mPath.clone();
		this.mSequenceModifier = pPathModifier.mSequenceModifier.clone();
	}

	@Override
	public PathModifier clone() {
		return new PathModifier(this);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public Path getPath() {
		return this.mPath;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean isFinished() {
		return this.mSequenceModifier.isFinished();
	}

	@Override
	public float getDuration() {
		return this.mSequenceModifier.getDuration();
	}

	public IPathModifierListener getPathModifierListener() {
		return this.mPathModifierListener;
	}

	public void setPathModifierListener(final IPathModifierListener pPathModifierListener) {
		this.mPathModifierListener = pPathModifierListener;
	}

	@Override
	public void reset() {
		this.mSequenceModifier.reset();
	}

	@Override
	public void onUpdate(final float pSecondsElapsed, final IShape pShape) {
		this.mSequenceModifier.onUpdate(pSecondsElapsed, pShape);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IPathModifierListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		public void onWaypointPassed(final PathModifier pPathModifier, final IShape pShape, final int pWaypointIndex);
	}
	
	public static class Path {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final float[] mCoordinatesX;
		private final float[] mCoordinatesY;

		private int mIndex;
		private boolean mLengthChanged = false;
		private float mLength;

		// ===========================================================
		// Constructors
		// ===========================================================

		public Path(final int pLength) {
			this.mCoordinatesX = new float[pLength];
			this.mCoordinatesY = new float[pLength];

			this.mIndex = 0;
			this.mLengthChanged = false;
		}

		public Path(final float[] pCoordinatesX, final float[] pCoordinatesY) throws IllegalArgumentException {
			if (pCoordinatesX.length != pCoordinatesY.length) {
				throw new IllegalArgumentException("Coordinate-Arrays must have the same length.");
			}

			this.mCoordinatesX = pCoordinatesX;
			this.mCoordinatesY = pCoordinatesY;

			this.mIndex = pCoordinatesX.length;
			this.mLengthChanged = true;
		}

		public Path(final Path pPath) {
			final int size = pPath.getSize();
			this.mCoordinatesX = new float[size];
			this.mCoordinatesY = new float[size];

			System.arraycopy(pPath.mCoordinatesX, 0, this.mCoordinatesX, 0, size);
			System.arraycopy(pPath.mCoordinatesY, 0, this.mCoordinatesY, 0, size);

			this.mIndex = pPath.mIndex;
			this.mLengthChanged = pPath.mLengthChanged;
			this.mLength = pPath.mLength;
		}

		@Override
		public Path clone() {
			return new Path(this);
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public Path to(final float pX, final float pY) {
			this.mCoordinatesX[this.mIndex] = pX;
			this.mCoordinatesY[this.mIndex] = pY;

			this.mIndex++;

			this.mLengthChanged = true;

			return this;
		}

		public float[] getCoordinatesX() {
			return this.mCoordinatesX;
		}

		public float[] getCoordinatesY() {
			return this.mCoordinatesY;
		}

		public int getSize() {
			return this.mCoordinatesX.length;
		}

		public float getLength() {
			if(this.mLengthChanged) {
				this.updateLength();
			}
			return this.mLength;
		}

		public float getSegmentLength(final int pSegmentIndex) {
			final float[] coordinatesX = this.mCoordinatesX;
			final float[] coordinatesY = this.mCoordinatesY;

			final int nextSegmentIndex = pSegmentIndex + 1;

			final float dx = coordinatesX[pSegmentIndex] - coordinatesX[nextSegmentIndex];
			final float dy = coordinatesY[pSegmentIndex] - coordinatesY[nextSegmentIndex];

			return FloatMath.sqrt(dx * dx + dy * dy);
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		private void updateLength() {
			float length = 0.0f;

			for(int i = this.mIndex - 2; i >= 0; i--) {
				length += this.getSegmentLength(i);
			}
			this.mLength = length;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

}
