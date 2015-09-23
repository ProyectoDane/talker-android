package ar.uba.fi.talker.adapter;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dao.ImageDAO;

public class InsertImageAdapter extends BaseAdapter {
	private final Context context;
	private final List<ImageDAO> innerImages;
	private final LayoutInflater mInflater;

	public InsertImageAdapter(Context context, List<ImageDAO> categoryImages) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.innerImages = categoryImages;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View gridViewItem;

		if (convertView == null) {
			
			gridViewItem = new View(context);
			// get layout from mobile.xml
			gridViewItem = mInflater.inflate(R.layout.insert_image_grid_item, null);

			String imageName = innerImages.get(position).getName();
			
			// set value into textview
			TextView textView = (TextView) gridViewItem.findViewById(R.id.grid_item_label);
			textView.setText(imageName);

			// set image based on selected text
			ImageView imageView = (ImageView) gridViewItem.findViewById(R.id.grid_item_image);
			if (innerImages.get(position).getPath().contains("/")){
				Uri uri = Uri.parse(innerImages.get(position).getPath());
				imageView.setImageURI(uri);
			} else {
				int idCode = Integer.valueOf(innerImages.get(position).getPath());
				imageView.setImageResource(idCode);
			}

		} else {
			gridViewItem = convertView;
		}

		return gridViewItem;
	}

	@Override
	public int getCount() {
		return innerImages.size();
	}

	@Override
	public Object getItem(int position) {
		return innerImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return innerImages.get(position).getId();
	}

}