package ar.uba.fi.talker.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.adapter.GridAdapter;
import ar.uba.fi.talker.utils.GridConversationItems;

public class GridFragment extends Fragment {

	private GridView mGridView;
	private GridAdapter mGridAdapter;
	List<GridConversationItems> gridItems;
	private ActionBarActivity activity;

	public GridFragment() {
		this.gridItems = new ArrayList<GridConversationItems>();
	}

	public GridFragment(List<GridConversationItems> gridItems, ActionBarActivity activity) {
		this.gridItems = gridItems;
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.grid_inside_view_pager, container, false);
		int maxColumns = calculateColumns(view);
		
		mGridView.setNumColumns(maxColumns);
		return view;
	}

	private int calculateColumns(View view) {
		float conversationWidth = activity.getResources().getDimension(R.dimen.scenarioWidth);
		DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
		mGridView = (GridView) view.findViewById(R.id.gridView);
		float dpWidth = displayMetrics.widthPixels / (displayMetrics.densityDpi/160);
		int maxImages = Math.round(dpWidth /conversationWidth);
		return maxImages;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (activity != null) {

			mGridAdapter = new GridAdapter(activity, gridItems);
			if (mGridView != null) {
				mGridView.setAdapter(mGridAdapter);
			}
		}
	}

	public GridView getmGridView() {
		return mGridView;
	}
}
