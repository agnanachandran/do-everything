package ca.pluszero.emotive.adapters;

import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;

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
    
}
