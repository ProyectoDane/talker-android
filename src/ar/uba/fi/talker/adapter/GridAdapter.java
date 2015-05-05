package ar.uba.fi.talker.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dao.ConversationDAO;
import ar.uba.fi.talker.utils.GridConversationItems;
import ar.uba.fi.talker.utils.GridItems;

public class GridAdapter extends BaseAdapter {

	ActionBarActivity context;
	
	public class ViewHolder {
		public ImageView imageView;
		public TextView textTitle;
	}
	private List<GridConversationItems> items;
	private LayoutInflater mInflater;
    private static Long itemSelectedId;
    private static int pos;
	

	public GridAdapter(ActionBarActivity context, List<GridConversationItems> gridItems) {

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		items = gridItems;

	}

	public List<GridConversationItems> getItems() {
		return items;
	}

	@Override
	public int getCount() {
		if (items != null) {
			return items.size();
		}
		return 0;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		if (items != null && position >= 0 && position < getCount()) {
			return items.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (items != null && position >= 0 && position < getCount()) {
			return items.get(position).getConversationDAO().getId();
		}
		return 0;
	}

	public void setItemsList(List<GridConversationItems> locations) {
		this.items = locations;
	}

	public View getView(final int position, View convertView, final ViewGroup parent) {

		final View view = convertView != null ? convertView : mInflater.inflate(R.layout.row_grid, parent, false);

		LinearLayout viewHolder = (LinearLayout) view.findViewById(R.id.grid_element);

		viewHolder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < parent.getChildCount(); i++) {
					parent.getChildAt(i).setBackgroundColor(Color.WHITE);	
				}
				view.setBackgroundColor(Color.CYAN);
				itemSelectedId = getItemId(position);
				pos = position;
				
				// TODO Comentado para solucionar primero GridSceneAdapter, 
				// una vez funcionando hago funcionar esto.
				
//				SceneActionFragment fragment = new SceneActionFragment(position);
//				fragment.onAttach(context);
//				fragment.show(context.getSupportFragmentManager(), "action-scene");
			}
		});

		GridConversationItems gridItems = items.get(position);
		this.setCatImage(viewHolder, gridItems.getConversationDAO());
		
		return view;
	}

	private void setCatImage(LinearLayout viewHolder, ConversationDAO conversationView) {
		ImageView imageView = (ImageView) viewHolder.findViewById(R.id.image);
		Uri uri = Uri.parse(conversationView.getPathSnapshot());
		imageView.setImageURI(uri);
		TextView textTitle = (TextView) viewHolder.findViewById(R.id.text);
		textTitle .setText(conversationView.getName());
	}

	public static Long getItemSelectedId() {
		return itemSelectedId;
	}
	
	public static long getPosition() {
		return pos;
	}
	
	public void setItem(GridItems gridItem,String text, int location){
		gridItem.getScenarioView().setName(text);
	}

	public void removeItem(int location) {
		items.remove(location);
	}

	public void addItem(GridConversationItems gridItem) {
		items.add(gridItem);
	}
}
