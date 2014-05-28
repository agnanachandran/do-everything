package ca.pluszero.emotive.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import ca.pluszero.emotive.R;
import ca.pluszero.emotive.models.YelpData;

public class YelpListAdapter extends BaseArrayAdapter<YelpData> {

    private final ImageLoader imageLoader;

    public YelpListAdapter(Activity context, List<YelpData> items) {
        super(context, R.layout.yelp_list_item, items);
        this.imageLoader = ImageLoader.getInstance();
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.yelp_list_item, null, true);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.businessName = (TextView) rowView.findViewById(R.id.tvBusinessName);
            viewHolder.thumbnail = (ImageView) rowView.findViewById(R.id.ivFoodThumbnail);
            viewHolder.phoneNumber = (TextView) rowView.findViewById(R.id.tvPhoneNumber);
            viewHolder.starRating = (ImageView) rowView.findViewById(R.id.ivRatingStars);
            viewHolder.numberOfReviews = (TextView) rowView.findViewById(R.id.tvNumberOfReviews);
            viewHolder.distance = (TextView) rowView.findViewById(R.id.tvDistance);
            viewHolder.address = (TextView) rowView.findViewById(R.id.tvAddress);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        final YelpData data = getItem(position);
        holder.businessName.setText(data.getBusinessName());
        imageLoader.displayImage(data.getThumbnailImageUrl(), holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to location
            }
        });
        holder.phoneNumber.setText(data.getDisplayPhoneNumber());
        holder.phoneNumber.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String uri = "tel:" + data.getPhoneNumber().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                YelpListAdapter.this.context.startActivity(intent);
            }
        });
        imageLoader.displayImage(data.getRatingImageUrl(), holder.starRating);
        if (data.getReviewCount() == 1) {
            holder.numberOfReviews.setText(data.getReviewCount() + " Review");
        } else {
            holder.numberOfReviews.setText(data.getReviewCount() + " Reviews");
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean showMiles = preferences.getBoolean("miles_checkbox", false);
        if (showMiles) {
            holder.distance.setText(data.getFormattedDistanceInMiles() + " miles");
        } else {
            holder.distance.setText(data.getFormattedDistanceInKm() + " km");
        }
        holder.address.setText(data.getAddress());
        return rowView;
    }

    private static class ViewHolder {
        TextView businessName;
        ImageView thumbnail;
        TextView phoneNumber;
        ImageView starRating;
        TextView numberOfReviews;
        TextView distance;
        TextView address;
    }
}

