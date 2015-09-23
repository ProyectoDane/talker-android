package ar.uba.fi.talker.listener;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dataSource.TalkerDataSource;
import ar.uba.fi.talker.fragment.SceneActionFragment;
import ar.uba.fi.talker.utils.GridItems;

public class OnClickListenerGridElementSettings implements OnClickListener {

	private final Context context;
	private final GridItems gridItem;
	private final BaseAdapter baseAdapter;
	private final TalkerDataSource dao;
		
	public OnClickListenerGridElementSettings(
			final Context context,
			final GridItems gridItem,
			final BaseAdapter baseAdapter,
			TalkerDataSource dao
	){
		this.context = context;
		this.gridItem = gridItem;
		this.baseAdapter = baseAdapter;
		this.dao = dao;
	}
	
	@Override
	public void onClick(View view) {
		view.setBackgroundColor(context.getResources().getColor(R.color.selectionViolet));
		
		FragmentActivity activity = (FragmentActivity) context;		
		SceneActionFragment fragment = new SceneActionFragment();
		fragment.init(gridItem, view, baseAdapter, dao);
		
		OnClickListener onClickListener = new OnClickStartActionSettings(activity, gridItem, fragment);
		fragment.setOnClickStartAction(onClickListener);
		fragment.onAttach(activity);
		fragment.show(activity.getSupportFragmentManager(), "action-scene");
		
	}

}
