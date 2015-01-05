package ca.pluszero.emotive.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import java.io.File;

public class MusicCursorAdapter extends SimpleCursorAdapter implements AdapterView.OnItemClickListener {
    private final Context ctx;
    private MediaPlayer mp;
    private String songPath = null;

    public MusicCursorAdapter(Context ctx, int layout, Cursor c, String[] from, int[] to) {
        super(ctx, layout, c, from, to);
        this.ctx = ctx;
        this.mp = new MediaPlayer();
    }

    private void stopPlaying() {
        mp.stop();
        mp.release();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c = (Cursor) getItem(position);
        int index = c.getColumnIndex(MediaStore.Audio.Media.DATA);
        String songPath = c.getString(index);
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
//        boolean playMusicInBg = preferences.getBoolean("play_music_in_bg_checkbox", false);
//        if (playMusicInBg) {
//            if (!(songPath.equals(this.songPath))) {
//                this.songPath = songPath;
//                try {
//                    mp.setDataSource(songPath);
//                    mp.prepare();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                mp.start();
//            }
//        } else {
            File file = new File(songPath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "audio/*");
            this.ctx.startActivity(intent);
//        }

    }
}
