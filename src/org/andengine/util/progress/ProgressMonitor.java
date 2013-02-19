package org.andengine.util.progress;
import java.util.ArrayList;
import java.util.HashMap;

import org.andengine.util.math.MathUtils;


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

	private final ArrayList<IProgressListener> mProgressListeners = new ArrayList<IProgressListener>();
	private final HashMap<ProgressMonitor, IProgressListener> mChildProgressMonitorToProgressListenerMap = new HashMap<ProgressMonitor, IProgressListener>();

	// ===========================================================
	// Constructors
	// ===========================================================

	public ProgressMonitor() {

	}

	public ProgressMonitor(final IProgressListener pProgressListener) {
		this.mProgressListeners.add(pProgressListener);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public void onProgressChanged(final int pProgress) {
		final int progressListenerCount = this.mProgressListeners.size();
		for (int i = 0; i < progressListenerCount; i++) {
			this.mProgressListeners.get(i).onProgressChanged(pProgress);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void registerChildProgressMonitor(final ProgressMonitor pChildProgressMonitor, final int pChildProgressMonitorRangeFrom, final int pChildProgressMonitorRangeTo) {
		final IProgressListener childProgressMonitorListener = new IProgressListener() {
			@Override
			public void onProgressChanged(final int pProgress) {
				final int progress = MathUtils.mix(pChildProgressMonitorRangeFrom, pChildProgressMonitorRangeTo, (float)pProgress / IProgressListener.PROGRESS_MAX);
				ProgressMonitor.this.onProgressChanged(progress);
			}
		};
		pChildProgressMonitor.addProgressListener(childProgressMonitorListener);
		this.mChildProgressMonitorToProgressListenerMap.put(pChildProgressMonitor, childProgressMonitorListener);
	}

	public void unregisterChildProgressMonitor(final ProgressMonitor pChildProgressMonitor) {
		pChildProgressMonitor.removeProgressListener(this.mChildProgressMonitorToProgressListenerMap.get(pChildProgressMonitor));
	}

	private void addProgressListener(final IProgressListener pProgressListener) {
		this.mProgressListeners.add(pProgressListener);
	}

	private void removeProgressListener(final IProgressListener pProgressListener) {
		this.mProgressListeners.add(pProgressListener);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
