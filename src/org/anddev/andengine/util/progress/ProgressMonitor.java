package org.anddev.andengine.util.progress;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:07:35 - 09.07.2009
 */
public class ProgressMonitor implements IProgressListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ProgressMonitor mParentProgressMonitor;
	private final IProgressListener mListener;

	private int mSubMonitorRangeFrom = 0;
	private int mSubMonitorRangeTo = 100;

	private int mProgress = 0;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ProgressMonitor(final IProgressListener pListener) {
		this.mListener = pListener;
		this.mParentProgressMonitor = null;
	}

	public ProgressMonitor(final ProgressMonitor pParent){
		this.mListener = null;
		this.mParentProgressMonitor = pParent;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ProgressMonitor getParentProgressMonitor() {
		return this.mParentProgressMonitor;
	}

	public int getProgress() {
		return this.mProgress;
	}

	public void setSubMonitorRange(final int pSubMonitorRangeFrom, final int pSubMonitorRangeTo) {
		this.mSubMonitorRangeFrom = pSubMonitorRangeFrom;
		this.mSubMonitorRangeTo = pSubMonitorRangeTo;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * @param pProgress between 0 and 100.
	 */
	@Override
	public void onProgressChanged(final int pProgress){
		this.mProgress = pProgress;
		if(this.mParentProgressMonitor != null) {
			this.mParentProgressMonitor.onSubProgressChanged(pProgress);
		} else {
			this.mListener.onProgressChanged(pProgress);
		}
	}

	private void onSubProgressChanged(final int pSubProgress){
		final int subRange = this.mSubMonitorRangeTo- this.mSubMonitorRangeFrom;
		final int subProgressInRange = this.mSubMonitorRangeFrom + (int)(subRange * pSubProgress / 100f);

		if(this.mParentProgressMonitor != null) {
			this.mParentProgressMonitor.onSubProgressChanged(subProgressInRange);
		}else{
			this.mListener.onProgressChanged(subProgressInRange);
		}
	}
}
