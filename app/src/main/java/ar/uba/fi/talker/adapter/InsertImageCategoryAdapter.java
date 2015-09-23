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
import ar.uba.fi.talker.dao.CategoryDAO;
import ar.uba.fi.talker.dao.ImageDAO;

public class InsertImageCategoryAdapter extends BaseAdapter {
	private final Context context;
	private final List<CategoryDAO> categories;
	private final LayoutInflater mInflater;

	public InsertImageCategoryAdapter(Context context, List<CategoryDAO> categories) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.categories = categories;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View gridViewItem;

		if (convertView == null) {
			
			gridViewItem = new View(context);
			// get layout from mobile.xml
			gridViewItem = mInflater.inflate(R.layout.insert_image_grid_item, null);

			String categoryName = categories.get(position).getName();
			
			// set value into textview
			TextView textView = (TextView) gridViewItem.findViewById(R.id.grid_item_label);
			textView.setText(categoryName);

			// set image based on selected text
			ImageView imageView = (ImageView) gridViewItem.findViewById(R.id.grid_item_image);

			this.setImageContent(imageView, categories.get(position).getImage());
		} else {
			gridViewItem = convertView;
		}

		return gridViewItem;
	}
	
	private void setImageContent(ImageView imageView, ImageDAO imageDAO) {
		if (imageDAO != null && imageDAO.getPath().contains("/")){
			Uri uri = Uri.parse(imageDAO.getPath());
			imageView.setImageURI(uri);
		} else if (imageDAO != null) {
			int idCode = Integer.valueOf(imageDAO.getPath());
			imageView.setImageResource(idCode);
		} else {
			imageView.setImageResource(R.drawable.history_panel);
		}
	}
	
	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return categories.get(position).getId();
	}

}