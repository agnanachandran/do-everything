package ca.pluszero.emotive;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;

import java.io.File;

import ca.pluszero.emotive.fragments.MainSectionFragment;

public class MusicLauncher implements LoaderManager.LoaderCallbacks<Cursor> {

    // Identifies a particular Loader
    private static final int URL_LOADER = 0;
    private static MusicLauncher instance = null;
    private final Fragment fragment;
    private String[] mSelectionArgs;
    private String[] mProjection;
    private String mSelectionClause;

    private MusicLauncher(Fragment fragment) {
        this.fragment = fragment;
    }

    public static MusicLauncher getInstance(Fragment fragment) {
        if (instance == null) {
            instance = new MusicLauncher(fragment);
        }
        return instance;
    }

    public void startMusic(String songFileName) {
        File file = new File(songFileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "audio/*");
        fragment.startActivity(intent);
    }

    public void searchMusic(String query) {
        mSelectionArgs = new String[1];
        mProjection = new String[]{MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID};
        mSelectionClause = MediaStore.Audio.Media.TITLE + " LIKE ? COLLATE NOCASE";
        mSelectionArgs[0] = "%" + query + "%";

        this.fragment.getLoaderManager().initLoader(URL_LOADER, null, this);
        // Cursor mCursor =
        // getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        // projection, selectionClause, selectionArgs, );
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
            case URL_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(fragment.getActivity(), // Parent activity context
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // Table to
                        // query
                        mProjection, // Projection to return
                        mSelectionClause, // selection clause
                        mSelectionArgs, // selection arguments
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER // Default sort
                        // order
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

            SimpleCursorAdapter adapter = ((MainSectionFragment) fragment).getAdapter();
            adapter.changeCursor(cursor);
            int index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                if (cursor.getPosition() == 1) {
                    String songPath = cursor.getString(index);
                    startMusic(songPath);
                }
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
                break;

            }
        }
    }

    /*
     * Invoked when the CursorLoader is being reset. For example, this is called
     * if the data in the provider changes and the Cursor becomes stale.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        /*
         * Clears out the adapter's reference to the Cursor. This prevents
         * memory leaks.
         */
        ((MainSectionFragment) fragment).getAdapter().changeCursor(null);
    }

}
