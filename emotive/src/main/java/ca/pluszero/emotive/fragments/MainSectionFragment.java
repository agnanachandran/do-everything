package ca.pluszero.emotive.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeIntents;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ca.pluszero.emotive.R;
import ca.pluszero.emotive.YouTubeClient;
import ca.pluszero.emotive.adapters.PlacesAutoCompleteAdapter;
import ca.pluszero.emotive.adapters.PrimaryListAdapter;
import ca.pluszero.emotive.adapters.YouTubeListAdapter;
import ca.pluszero.emotive.models.Option;
import ca.pluszero.emotive.models.PrimaryOption;
import ca.pluszero.emotive.models.YouTubeVideo;

public class MainSectionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    // Identifies a particular Loader
    private static final int URL_LOADER = 0;

    private TextView tvSecondaryOptionsTitle;
    private TextView tvSearchQuery;
    private ListView lvPrimaryOptions;
    private Button bFirstButton;
    private Button bSecondButton;
    private Button bThirdButton;
    private ListView lvQueryResults;
    private PrimaryListAdapter mPrimaryListAdapter;

    private SimpleCursorAdapter mAdapter;
    private String[] mSelectionArgs;
    private String[] mProjection;
    private String mSelectionClause;

    private int mPrimaryOption = 0;
    private int mSecondaryOption = 0;

    private Button[] secondaryButtons;

    private Animation inAnimate;
    private Animation outAnimate;

    private static final Typeface LIGHT_TYPE_FACE = Typeface.create("sans-serif-light",
            Typeface.NORMAL);
    private static final Typeface BOLD_TYPE_FACE = Typeface.create("sans-serif", Typeface.BOLD);

    private AutoCompleteTextView etSearchView;

    public MainSectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inAnimate = new AlphaAnimation(0.0f, 1.0f);
        inAnimate.setDuration(500);
        outAnimate = new AlphaAnimation(1.0f, 0.0f);
        outAnimate.setDuration(500);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        lvQueryResults = (ListView) rootView.findViewById(R.id.lvQueryResults);

        tvSecondaryOptionsTitle = (TextView) rootView.findViewById(R.id.tvSecondaryOptionsTitle);
        tvSearchQuery = (TextView) rootView.findViewById(R.id.tvSection_searchQuery);
        // tvSearchQuery.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));

        // Set up listview
        lvPrimaryOptions = (ListView) rootView.findViewById(R.id.lvPrimaryOptions);
        mPrimaryListAdapter = new PrimaryListAdapter(getActivity());
        lvPrimaryOptions.setAdapter(mPrimaryListAdapter);
        // Set up 3 primary search buttons
        bFirstButton = (Button) rootView.findViewById(R.id.bFirstOption);
        bSecondButton = (Button) rootView.findViewById(R.id.bSecondOption);
        bThirdButton = (Button) rootView.findViewById(R.id.bThirdOption);

        secondaryButtons = new Button[] { bFirstButton, bSecondButton, bThirdButton };

        lvPrimaryOptions.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                PrimaryOption selectedOption = (PrimaryOption) mPrimaryListAdapter.getItem(pos);

                if (pos != mPrimaryOption) {
                    selectedOption.getUnselectedRes();
                    selectedOption.getSelectedRes();
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        if (i == pos) {
                        } else {
                            parent.getChildAt(i)
                                    .findViewById(R.id.primary_simple_main_divider)
                                    .setBackgroundColor(
                                            getResources()
                                                    .getColor(selectedOption.getSelectedRes()));
                        }
                    }
                    mPrimaryListAdapter.setCheckedItem(pos);
                    lvPrimaryOptions.invalidateViews();
                    // If we're now dealing with a search string
                    if (pos == Option.Verb.SEARCH.val) {
                        placeSearchOptions();
                    } else if (pos == Option.Verb.FIND_ME.val) {
                        placeFindMeOptions();
                    } else if (pos == Option.Verb.WATCH_A_MOVIE.val) {
                        placeWatchAMovieOptions();
                    } else if (pos == Option.Verb.LISTEN_TO.val) {
                        placeListenToOptions();
                    }
                    mSecondaryOption = 0;
                    turnOffOtherButtons();
                    setSelectedButton(bFirstButton);
                    mPrimaryOption = pos;
                }
            }
        });

        for (int i = 0; i < secondaryButtons.length; i++) {
            final Button b = secondaryButtons[i];
            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Button clickedButton = (Button) v;
                    if (clickedButton.isSelected()) {
                    } else {
                        turnOffOtherButtons();
                        setSelectedButton(clickedButton);
                    }
                    determineSelectedButton(clickedButton);
                }

                public void determineSelectedButton(Button clickedButton) {
                    for (int i = 0; i < secondaryButtons.length; i++) {
                        if (secondaryButtons[i] == clickedButton) {
                            mSecondaryOption = i;
                        }
                    }
                }

            });

        }

        etSearchView = (AutoCompleteTextView) rootView.findViewById(R.id.mainSearchView);
        etSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(v.getText());
                    return true;
                }
                return false;
            }

        });

        setupButtons();
        setHasOptionsMenu(true);
        return rootView;
    }

    public void turnOffOtherButtons() {
        for (final Button b : secondaryButtons) {
            b.setSelected(false);
            b.setTypeface(LIGHT_TYPE_FACE);
            // b.setBackgroundColor(getResources().getColor(mCurrentUnselectedColor));
        }
    }

    private void setSelectedButton(Button clickedButton) {
        clickedButton.setSelected(true);
        clickedButton.setTypeface(BOLD_TYPE_FACE);
        // clickedButton.setBackgroundColor(getResources().getColor(mCurrentSelectedColor));
    }

    // Sets up initial state of buttons by setting a default secondary button
    // and a primary option from primary listview
    private void setupButtons() {
        placeSearchOptions();
        setSelectedButton(secondaryButtons[0]);
    }

    private void placeSearchOptions() {
        setupSecondaryOptions(getResources().getStringArray(R.array.search_options), R.string.search_options_title_label);
        etSearchView.setOnItemClickListener(null);
    }

    private void placeFindMeOptions() {
        setupSecondaryOptions(getResources().getStringArray(R.array.find_me_options), R.string.find_options_title_label);
        ((AutoCompleteTextView) etSearchView).setAdapter(new PlacesAutoCompleteAdapter(
                getActivity(), R.layout.simple_list_item));
        ((AutoCompleteTextView) etSearchView).setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                etSearchView.setText((String) adapterView.getItemAtPosition(position));
            }
        });
    }
    
    private void placeListenToOptions() {
        setupSecondaryOptions(getResources().getStringArray(R.array.listen_to_options), R.string.listen_options_title_label);
        etSearchView.setOnItemClickListener(null);
    }

    private void placeWatchAMovieOptions() {
        setupSecondaryOptions(getResources().getStringArray(R.array.watch_a_movie_options), R.string.watch_options_title_label);
        etSearchView.setOnItemClickListener(null);
    }

    private void setupSecondaryOptions(String[] secondaryOptions, int secondaryOptionsTitleResId) {
        tvSecondaryOptionsTitle.setText(secondaryOptionsTitleResId);
        bFirstButton.setText(secondaryOptions[0]);
        bSecondButton.setText(secondaryOptions[1]);
        bThirdButton.setText(secondaryOptions[2]);
    }

    private void performSearch(CharSequence query) {
        int leftPos = mPrimaryOption;
        Log.d(getTag(), leftPos + "left Pos");
        int rightPos = mSecondaryOption;
        if (leftPos == Option.Verb.SEARCH.val) {
            if (rightPos == Option.Search.ANYTHING.val) {
            } else if (rightPos == Option.Search.WEB.val) {
                startGoogleSearchAnything(query);
            } else {

            }
        } else if (leftPos == Option.Verb.LISTEN_TO.val) {
            if (rightPos == Option.ListenTo.ON_MY_DEVICE.val) {
                startMusicSearchDevice(query);
            } else if (rightPos == Option.ListenTo.ON_YOUTUBE.val) {
                startYouTubeSearch(query);
            }
        } else if (leftPos == Option.Verb.FIND_ME.val) {
            if (rightPos == Option.FindMe.ANYTHING.val) {
                startMapsSearch(query);
            }
        }
    }

    private void startYouTubeSearch(CharSequence query) {
        if (isConnected()) {
            YouTubeClient.getYouTubeSearch(query.toString(), new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                }

                @Override
                public void onSuccess(JSONObject response) {
                    final List<YouTubeVideo> videos = new ArrayList<YouTubeVideo>();
                    try {

                        final JSONArray searchJsonItems = response.getJSONArray("items");
                        for (int i = 0; i < searchJsonItems.length(); i++) {
                            final JSONObject videoObject = searchJsonItems.getJSONObject(i);
                            final String videoId = videoObject.getJSONObject("id").getString(
                                    "videoId");
                            // Get statistics; specifically # of views
                            YouTubeClient.getYouTubeVideo(videoId, new JsonHttpResponseHandler() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public synchronized void onSuccess(JSONObject response) {
                                    try {
                                        JSONArray jsonItems = response.getJSONArray("items");
                                        JSONObject item = jsonItems.getJSONObject(0);
                                        int viewCount = Integer.parseInt(item.getJSONObject(
                                                "statistics").getString("viewCount"));
                                        JSONObject snippet = videoObject.getJSONObject("snippet");
                                        String videoName = snippet.getString("title");
                                        String channelName = snippet.getString("channelTitle");
                                        // get # of views
                                        String thumbnailUrl = snippet.getJSONObject("thumbnails")
                                                .getJSONObject("medium").getString("url");
                                        videos.add(new YouTubeVideo(videoId, videoName,
                                                thumbnailUrl, viewCount, channelName));
                                        if (videoObject.equals(searchJsonItems.getJSONObject(searchJsonItems.length()-1))) {

                                            bringUpListviewAndDismissKeyboard();
                                            lvQueryResults.setVisibility(View.VISIBLE);
                                            lvQueryResults.setAdapter(new YouTubeListAdapter(
                                                    getActivity(), videos));
                                            lvQueryResults
                                                    .setOnItemClickListener(new OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(
                                                                AdapterView<?> parent, View view,
                                                                int position, long id) {
                                                            String videoId = ((YouTubeVideo) lvQueryResults
                                                                    .getItemAtPosition(position))
                                                                    .getId();
                                                            Intent intent = YouTubeIntents
                                                                    .createPlayVideoIntent(
                                                                            getActivity(), videoId);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
            });
        } else {
            // No interwebs; display Toast. TODO
        }
        // Intent intent = new Intent(Intent.ACTION_SEARCH);
        // intent.setPackage("com.google.android.youtube");
        // intent.putExtra("search_query", query);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);
    }

    private void startMapsSearch(CharSequence query) {
        String url = "geo:0,0?q=" + query;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    // launches an activity that searches Google for the user's query in a
    // browser
    private void startGoogleSearchAnything(CharSequence query) {
        tvSearchQuery.setText(query);
        Intent browserIntent = new Intent(Intent.ACTION_WEB_SEARCH);
        browserIntent.putExtra(SearchManager.QUERY, query.toString());
        startActivity(browserIntent);
    }

    @SuppressWarnings("deprecation")
    private void startMusicSearchDevice(CharSequence query) {
        mSelectionArgs = new String[1];
        mProjection = new String[] { MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media._ID };
        mSelectionClause = MediaStore.Audio.Media.TITLE + " LIKE ? COLLATE NOCASE";
        mSelectionArgs[0] = "%" + query.toString() + "%";

        getLoaderManager().initLoader(URL_LOADER, null, this);
        // Cursor mCursor =
        // getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        // projection, selectionClause, selectionArgs, );
        String[] artistColumns = { MediaStore.Audio.Media.ARTIST };
        int[] mSongListItems = { R.id.tvQueryTitleCard };

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.row_card, null, artistColumns,
                mSongListItems);
        lvQueryResults.setVisibility(View.VISIBLE);
        lvQueryResults.setAdapter(mAdapter);
        lvQueryResults.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) mAdapter.getItem(position);
                int index = c.getColumnIndex(MediaStore.Audio.Media.DATA);
                String songPath = c.getString(index);
                Log.d(getTag(), songPath);
                File file = new File(songPath.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "audio/*");
                startActivity(intent);
            }
        });
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
            return new CursorLoader(getActivity(), // Parent activity context
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
            Log.e(getTag(), "provider returned null cursor :(");
        } else if (cursor.getCount() < 1) {
            Log.d(getTag(), "provider gave 0 results");
        } else {
            mAdapter.changeCursor(cursor);
            int index = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                String songPath = cursor.getString(index);
                Log.d(getTag(), songPath);
                // MusicLauncher musicLauncher =
                // MusicLauncher.getInstance(getActivity());
                // musicLauncher.startMusic(songPath.toString());
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
        mAdapter.changeCursor(null);
    }

    private void bringUpListviewAndDismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearchView.getWindowToken(), 0);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int pixelHeight = Math.round(0.1f * size.y);

        // TODO: Show listview
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
