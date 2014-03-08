package ca.pluszero.emotive.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.pluszero.emotive.R;
import ca.pluszero.emotive.models.DrawerItem;

public class DrawerListAdapter extends ArrayAdapter<DrawerItem> {

	// Initialize drawer items with all appropriate drawer titles and their image res IDs
	private final List<DrawerItem> drawerItems;

	public DrawerListAdapter(Context context, int textViewResourceId, List<DrawerItem> drawerItems) {
		super(context, textViewResourceId, drawerItems);
		this.drawerItems = drawerItems;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.drawer_list_item, null, true);
		}
		DrawerItem item = drawerItems.get(position);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.tvName);
		txtTitle.setText(item.getDrawerText());
		Drawable leftDrawable = item.getDrawable();
		ImageView imgView = (ImageView) rowView.findViewById(R.id.imgIcon);
		imgView.setImageDrawable(leftDrawable);
		LayoutParams params = (LayoutParams) imgView.getLayoutParams();
		params.height = 75;
		imgView.setLayoutParams(params);
		return rowView;
	}

	@Override
	public int getCount() {
		return drawerItems.size();
	}

	@Override
	public DrawerItem getItem(int position) {
		return drawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
