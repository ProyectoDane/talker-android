package ar.uba.fi.talker.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.adapter.GridScenesAdapter;
import ar.uba.fi.talker.dataSource.TalkerDataSource;
import ar.uba.fi.talker.utils.GridItems;

public class ScenesGridFragment extends Fragment {

	private GridView mGridView;
	List<GridItems> gridItems;
	private TalkerDataSource dao;

	public ScenesGridFragment() {
		this.gridItems = new ArrayList<GridItems>();
	}

	public void init(List<GridItems> gridItems, Activity activity, TalkerDataSource dao) {
		this.gridItems = gridItems;
		this.dao = dao;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.grid_inside_view_pager, container, false);
		mGridView = (GridView) view.findViewById(R.id.gridView);

		int maxColumns = this.calculateColumns(view);
		mGridView.setNumColumns(maxColumns);
		return view;
	}

	private int calculateColumns(View view) {
		float scenarioWidth = getActivity().getResources().getDimension(R.dimen.scenarioWidth);
		DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
		
		return Math.round(displayMetrics.widthPixels /scenarioWidth);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getActivity() != null) {

			GridScenesAdapter mGridAdapter = new GridScenesAdapter(getActivity(), gridItems);
			mGridAdapter.setDao(dao);
			if (mGridView != null) {
				mGridView.setAdapter(mGridAdapter);
			}
		}
	}

	public GridView getmGridView() {
		return mGridView;
	}
	
	public List<GridItems> getGridItems() {
		return gridItems;
	}
}
