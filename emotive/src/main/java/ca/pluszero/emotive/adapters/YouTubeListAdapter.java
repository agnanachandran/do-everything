package ca.pluszero.emotive.adapters;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ca.pluszero.emotive.R;
import ca.pluszero.emotive.models.YouTubeVideo;

import com.nostra13.universalimageloader.core.ImageLoader;

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
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        YouTubeVideo video = getItem(position);
        holder.videoName.setText(video.getName());
        imageLoader.displayImage(video.getThumbnailUrl(), holder.thumbnail);
        holder.channelName.setText(video.getChannelName());
        holder.viewCount.setText(video.getViewCount()+"");
        return rowView;
    }
    
    private static class ViewHolder {
        TextView videoName;
        ImageView thumbnail;
        TextView channelName;
        TextView viewCount;
      }
}
