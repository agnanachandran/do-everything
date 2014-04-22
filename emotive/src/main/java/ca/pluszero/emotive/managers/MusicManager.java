package ca.pluszero.emotive.managers;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.io.File;

public class MusicManager implements LoaderManager.LoaderCallbacks<Cursor> {

    // Identifies a particular Loader
    private static final int MUSIC_URL_LOADER = 0;
    private final Fragment fragment;
    private final IMusicLoadedListener listener;
    private String[] mSelectionArgs = new String[3];
    private String[] mProjection = new String[]{
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media._ID};
    private String mSelectionClause;

    public MusicManager(Fragment fragment, IMusicLoadedListener listener) {
        this.fragment = fragment;
        this.listener = listener;
    }

    public void startMusic(String songFileName) {
        File file = new File(songFileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "audio/*");
        fragment.startActivity(intent);
    }

    public void searchMusic(String query) {
        mSelectionClause = "((" + MediaStore.Audio.Media.TITLE + " LIKE ? COLLATE NOCASE) OR " +
                "(" + MediaStore.Audio.Media.ALBUM + " LIKE ? COLLATE NOCASE) OR " +
                "(" + MediaStore.Audio.Media.ARTIST + " LIKE ? COLLATE NOCASE))";
        mSelectionArgs[0] = "%" + query + "%";
        mSelectionArgs[1] = "%" + query + "%";
        mSelectionArgs[2] = "%" + query + "%";

        this.fragment.getLoaderManager().initLoader(MUSIC_URL_LOADER, null, this);
        // TODO: investigate how this should really work (with the restart, etc.
        this.fragment.getLoaderManager().restartLoader(MUSIC_URL_LOADER, null, this);
    }

    /*
     * Callback that's invoked when the system has initialized the Loader and is
     * ready to start the query. This usually happens when initLoader() is
     * called. The loaderID argument contains the ID value passed to the
     * initLoader() call.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        /*
         * Takes action based on the ID of the Loader that's being created
         */
        switch (loaderID) {
            case MUSIC_URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(fragment.getActivity(), // Parent activity context
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // Table to query
                        mProjection, // Projection to return
                        mSelectionClause, // selection clause
                        mSelectionArgs, // selection arguments
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            // uh oh?
            Log.e(fragment.getTag(), "provider returned null cursor :(");
        } else if (cursor.getCount() < 1) {
            Log.d(fragment.getTag(), "provider gave 0 results");
        } else {
            this.listener.onLoadFinished(cursor);

            int index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                // If there's only one result, play it
//                if (cursor.getPosition() == 0 && cursor.getCount() == 1) {
//                    String songPath = cursor.getString(index);
//                    startMusic(songPath);
//                    break;
//                }
                // MediaPlayer mp = new MediaPlayer();
                // try {
                // mp.setDataSource(songPath);
                // mp.prepare();
                // } catch (IllegalArgumentException e) {
                // e.printStackTrace();
                // } catch (SecurityException e) {
                // e.printStackTrace();
                // } catch (IllegalStateException e) {
                // e.printStackTrace();
                // } catch (IOException e) {
                // e.printStackTrace();
                // }
                // mp.start();
            }
        }
    }

    /*
     * Invoked when the CursorLoader is being reset. For example, this is called
     * if the data in the provider changes and the Cursor becomes stale.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.listener.onLoaderReset();
    }

    public static interface IMusicLoadedListener {
        public void onLoaderReset();
        public void onLoadFinished(Cursor cursor);
    }

}
