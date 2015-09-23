package ar.uba.fi.talker.adapter;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ar.uba.fi.talker.CommonImageSettingsActiviy;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dataSource.TalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.listener.OnClickListenerGridElement;
import ar.uba.fi.talker.listener.OnClickListenerGridElementSettings;
import ar.uba.fi.talker.utils.GridItems;

public class GridScenesAdapter extends ArrayAdapter<GridItems> {

	private class ViewHolder {
		public ImageView imageView;
		public TextView textTitle;
	}

	private TalkerDataSource dao;

	public GridScenesAdapter(Context context, List<GridItems> gridItems) {
		super(context, 0, gridItems);
	}
	
	public void setDao(TalkerDataSource dao){
		this.dao = dao;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return this.getItem(position).getElementGridView().getId();
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {

		ViewHolder mViewHolder = new ViewHolder();
		final GridItems gridItem = this.getItem(position);
		if (convertView == null) {
			Context context = getContext();
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.row_grid, parent, false);
			
			mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
			mViewHolder.textTitle = (TextView) convertView.findViewById(R.id.text);

		    convertView.setTag(mViewHolder);
		    OnClickListener onClickListenerGridElement=null;
			if(context instanceof CommonImageSettingsActiviy){
				onClickListenerGridElement=new OnClickListenerGridElementSettings(context, gridItem, this, dao);
		    } else {
		    	onClickListenerGridElement=new OnClickListenerGridElement(context, gridItem, this, dao);	
		    }		    
		    convertView.setOnClickListener(onClickListenerGridElement);
		    
		    
		    
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		
		this.setViewItemContent(mViewHolder, gridItem.getElementGridView());
	
		return convertView;
	}

	private void setViewItemContent(ViewHolder viewHolder, TalkerDTO scenarioView) {
		if (scenarioView.getPath().contains("/")){
			Uri uri = Uri.parse(scenarioView.getPath());
			viewHolder.imageView.setImageURI(uri);
		} else {
			int idCode = Integer.valueOf(scenarioView.getPath());
			viewHolder.imageView.setImageResource(idCode);
		}
		viewHolder.textTitle.setText(scenarioView.getName());
	}
	
	public void setItem(GridItems gridItem,String text, int location){
		gridItem.getElementGridView().setName(text);
	}
}
