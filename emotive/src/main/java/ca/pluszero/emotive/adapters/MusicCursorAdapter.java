package ca.pluszero.emotive.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import java.io.File;

public class MusicCursorAdapter extends SimpleCursorAdapter implements AdapterView.OnItemClickListener {
    private final Context ctx;
    public MusicCursorAdapter(Context ctx, int layout, Cursor c, String[] from, int[] to) {
        super(ctx, layout, c, from, to);
        this.ctx = ctx;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c = (Cursor) getItem(position);
        int index = c.getColumnIndex(MediaStore.Audio.Media.DATA);
        String songPath = c.getString(index);
        File file = new File(songPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "audio/*");
        this.ctx.startActivity(intent);
    }
}
