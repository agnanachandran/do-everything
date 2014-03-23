package ca.pluszero.emotive.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;
import java.util.List;

import ca.pluszero.emotive.R;
import ca.pluszero.emotive.models.YouTubeVideo;

public class YouTubeListAdapter extends BaseArrayAdapter<YouTubeVideo> {
    
    private final ImageLoader imageLoader;

    public YouTubeListAdapter(Activity context, List<YouTubeVideo> items) {
        super(context, R.layout.youtube_list_item, items);
        this.imageLoader = ImageLoader.getInstance();
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.youtube_list_item, null, true);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.videoName = (TextView) rowView.findViewById(R.id.tvVideoName);
            viewHolder.thumbnail = (ImageView) rowView.findViewById(R.id.ivVideoThumbnail);
            viewHolder.channelName = (TextView) rowView.findViewById(R.id.tvChannelName);
            viewHolder.viewCount = (TextView) rowView.findViewById(R.id.tvViewCount);
            viewHolder.duration = (TextView) rowView.findViewById(R.id.tvVideoTime);
            viewHolder.publishedDate = (TextView) rowView.findViewById(R.id.tvDateUploaded);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        YouTubeVideo video = getItem(position);
        holder.videoName.setText(video.getName());
        imageLoader.displayImage(video.getThumbnailUrl(), holder.thumbnail);
        holder.channelName.setText(video.getChannelName());
        holder.viewCount.setText(NumberFormat.getIntegerInstance().format(video.getViewCount()));
        holder.duration.setText(video.getDuration());
        holder.publishedDate.setText(video.getPublishedDate());
        return rowView;
    }
    
    private static class ViewHolder {
        TextView videoName;
        ImageView thumbnail;
        TextView channelName;
        TextView viewCount;
        TextView duration;
        TextView publishedDate;
      }
}
