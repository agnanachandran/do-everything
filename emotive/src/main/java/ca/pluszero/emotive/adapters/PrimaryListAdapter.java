package ca.pluszero.emotive.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ca.pluszero.emotive.R;
import ca.pluszero.emotive.models.PrimaryOption;

public class PrimaryListAdapter extends BaseAdapter {

    private final Activity mContext;
    private final List<PrimaryOption> options;
    private int checkedItem;
    public static final Typeface LIGHT_TYPE_FACE = Typeface.create("sans-serif-light", Typeface.NORMAL);
    public static final Typeface BOLD_TYPE_FACE = Typeface.create("sans-serif", Typeface.NORMAL);

    public PrimaryListAdapter(Activity context) {
        this.mContext = context;
        this.options = loadOptions();
    }

    private List<PrimaryOption> loadOptions() {
        Resources resources = mContext.getResources();
        String[] optionStrings = resources.getStringArray(R.array.verb_titles);
        List<PrimaryOption> options = new ArrayList<PrimaryOption>();
        for (String optionString : optionStrings) {
            if (optionString.contains("Search")) {
                options.add(new PrimaryOption(R.drawable.ic_search, R.color.search_option_unselected_blue,
                        R.color.search_option_selected_blue, optionString));
            } else if (optionString.contains("Find")) {
                options.add(new PrimaryOption(R.drawable.ic_search, R.color.find_option_unselected_green,
                        R.color.find_option_selected_green, optionString));
            } else if (optionString.contains("Listen")) {
                options.add(new PrimaryOption(R.drawable.ic_search, R.color.listen_option_unselected_orange,
                        R.color.listen_option_selected_orange, optionString));
            } else if (optionString.contains("Watch")) {
                options.add(new PrimaryOption(R.drawable.ic_search, R.color.watch_option_unselected_red,
                        R.color.watch_option_selected_red, optionString));
            }
        }
        return options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
              .inflate(R.layout.primary_list_item, parent, false);
        }
        PrimaryOption option = options.get(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.tvPrimaryOption);
        txtTitle.setText(option.getText());
        txtTitle.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(option.getCompoundDrawable()), null, null, null);
        if (position == this.getCheckedItem()) {
            int selectedColor = mContext.getResources().getColor(option.getSelectedRes());
            convertView.findViewById(R.id.primary_simple_main_divider).setBackgroundColor(selectedColor);
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.default_gray_bg));
//            txtTitle.setTextColor(selectedColor);
            txtTitle.setTypeface(BOLD_TYPE_FACE);
        } else {
            int unselectedColor = mContext.getResources().getColor(option.getUnselectedRes());
            convertView.findViewById(R.id.primary_simple_main_divider).setBackgroundColor(unselectedColor);
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//            txtTitle.setTextColor(unselectedColor);
            txtTitle.setTypeface(LIGHT_TYPE_FACE);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Object getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getCheckedItem() {
        return checkedItem;
    }

    public void setCheckedItem(int checkedItem) {
        this.checkedItem = checkedItem;
    }

}
