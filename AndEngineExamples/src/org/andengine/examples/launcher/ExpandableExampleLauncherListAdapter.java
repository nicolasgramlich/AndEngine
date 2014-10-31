package org.andengine.examples.launcher;

import org.andengine.examples.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 20:43:54 - 16.06.2010
 */
class ExpandableExampleLauncherListAdapter extends BaseExpandableListAdapter {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final ExampleGroup[] EXAMPLEGROUPS = {
		ExampleGroup.SIMPLE,
		ExampleGroup.MODIFIER_AND_ANIMATION,
		ExampleGroup.TOUCH,
		ExampleGroup.PARTICLESYSTEM,
		ExampleGroup.MULTIPLAYER,
		ExampleGroup.PHYSICS,
		ExampleGroup.TEXT,
		ExampleGroup.AUDIO,
		ExampleGroup.ADVANCED,
		ExampleGroup.POSTPROCESSING,
		ExampleGroup.BACKGROUND,
		ExampleGroup.OTHER,
		ExampleGroup.APP,
		ExampleGroup.GAME,
		ExampleGroup.BENCHMARK
	};

	// ===========================================================
	// Fields
	// ===========================================================

	private final Context mContext;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ExpandableExampleLauncherListAdapter(final Context pContext) {
		this.mContext = pContext;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Example getChild(final int pGroupPosition, final int pChildPosition) {
		return EXAMPLEGROUPS[pGroupPosition].mExamples[pChildPosition];
	}

	@Override
	public long getChildId(final int pGroupPosition, final int pChildPosition) {
		return pChildPosition;
	}

	@Override
	public int getChildrenCount(final int pGroupPosition) {
		return EXAMPLEGROUPS[pGroupPosition].mExamples.length;
	}

	@Override
	public View getChildView(final int pGroupPosition, final int pChildPosition, final boolean pIsLastChild, final View pConvertView, final ViewGroup pParent) {
		final View childView;
		if (pConvertView != null){
			childView = pConvertView;
		}else{
			childView = LayoutInflater.from(this.mContext).inflate(R.layout.listrow_example, null);
		}

		((TextView)childView.findViewById(R.id.tv_listrow_example_name)).setText(this.getChild(pGroupPosition, pChildPosition).NAMERESID);
		return childView;
	}

	@Override
	public View getGroupView(final int pGroupPosition, final boolean pIsExpanded, final View pConvertView, final ViewGroup pParent) {
		final View groupView;
		if (pConvertView != null){
			groupView = pConvertView;
		}else{
			groupView = LayoutInflater.from(this.mContext).inflate(R.layout.listrow_examplegroup, null);
		}

		((TextView)groupView.findViewById(R.id.tv_listrow_examplegroup_name)).setText(this.getGroup(pGroupPosition).mNameResourceID);
		return groupView;
	}

	@Override
	public ExampleGroup getGroup(final int pGroupPosition) {
		return EXAMPLEGROUPS[pGroupPosition];
	}

	@Override
	public int getGroupCount() {
		return EXAMPLEGROUPS.length;
	}

	@Override
	public long getGroupId(final int pGroupPosition) {
		return pGroupPosition;
	}

	@Override
	public boolean isChildSelectable(final int pGroupPosition, final int pChildPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}