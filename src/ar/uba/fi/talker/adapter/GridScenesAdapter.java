package ar.uba.fi.talker.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.fragment.SceneActionFragment;
import ar.uba.fi.talker.utils.GridItems;
import ar.uba.fi.talker.utils.ScenarioView;

public class GridScenesAdapter extends ArrayAdapter<GridItems> {

    private static long itemSelectedId;

	public GridScenesAdapter(Context context, int resource, List<GridItems> gridItems) {
    	super(context, resource, gridItems);
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = (View) convertView.getTag();
		} else {
			final ScenarioView scenarioView = this.getItem(position).getScenarioView();
			
			view = View.inflate(getContext(), R.layout.row_grid, null);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for (int i = 0; i < parent.getChildCount(); i++) {
						parent.getChildAt(i).setBackgroundColor(Color.WHITE);	
					}
					v.setBackgroundColor(Color.CYAN);
					
					SceneActionFragment fragment = new SceneActionFragment(v, scenarioView);
					itemSelectedId = scenarioView.getId();
					ActionBarActivity activity = (ActionBarActivity) v.getContext();
					fragment.onAttach(activity);
					fragment.show(activity.getSupportFragmentManager(), "action-scene");
				}
			});
			this.setCatImage(view, scenarioView);
		}

		return view;
	}

	private void setCatImage(View viewHolder, ScenarioView scenarioView) {
		ImageView imageView = (ImageView) viewHolder.findViewById(R.id.image);
		if (scenarioView.getIdCode() != 0){
			imageView.setImageResource(scenarioView.getIdCode());
		} else {
			Uri uri = Uri.parse(scenarioView.getPath());
			imageView.setImageURI(uri);
		}
		TextView textTitle = (TextView) viewHolder.findViewById(R.id.text);
		textTitle .setText(scenarioView.getName());
	}

	public static Long getItemSelectedId() {
		return itemSelectedId;
	}

	public void removeItem(int position) {
		this.removeItem(position);
	}
}
