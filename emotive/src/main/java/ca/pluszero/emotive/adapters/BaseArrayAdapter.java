package ca.pluszero.emotive.adapters;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BaseArrayAdapter<BaseModel> extends ArrayAdapter<BaseModel>{

    private List<BaseModel> items;
    protected Activity context;

    public BaseArrayAdapter(Activity activity, int resId, List<BaseModel> items) {
        super(activity, resId, items); // Change layout
        this.context = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public BaseModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(BaseModel item) {
        this.items.add(item);
    }

    public void addItems(List<BaseModel> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        super.clear();
        this.items.clear();
    }
}
