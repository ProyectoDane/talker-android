package ar.uba.fi.talker.adapter;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.utils.ScenarioView;

public class SceneAdapter extends BaseAdapter {

	private List<ScenarioView> scenes;
	private Context context;

	public SceneAdapter(Context context, List<ScenarioView> scenes) {
		this.scenes = scenes;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return scenes.size();
	}

	@Override
	public Object getItem(int position) {
		return scenes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return scenes.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View viewHolder = convertView == null 
				? View.inflate(context, R.layout.row_grid, null)
				: convertView; 

		ImageView imageView = (ImageView) viewHolder.findViewById(R.id.image);
		ScenarioView scenarioView = scenes.get(position);
		if (scenarioView .getIdCode() != 0){
			imageView.setImageResource(scenarioView.getIdCode());
		} else {
			Uri uri = Uri.parse(scenarioView.getPath());
			imageView.setImageURI(uri);
		}
		TextView textTitle = (TextView) viewHolder.findViewById(R.id.text);
		textTitle .setText(scenarioView.getName());
		
		return viewHolder;
	}

}
