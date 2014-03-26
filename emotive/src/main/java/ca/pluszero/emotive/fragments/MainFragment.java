package ca.pluszero.emotive.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.youtube.player.YouTubeIntents;

import java.util.List;

import ca.pluszero.emotive.R;
import ca.pluszero.emotive.activities.MainActivity;
import ca.pluszero.emotive.adapters.BaseArrayAdapter;
import ca.pluszero.emotive.adapters.MusicCursorAdapter;
import ca.pluszero.emotive.adapters.PlacesAutoCompleteAdapter;
import ca.pluszero.emotive.adapters.YouTubeListAdapter;
import ca.pluszero.emotive.listeners.EndlessScrollListener;
import ca.pluszero.emotive.managers.MusicManager;
import ca.pluszero.emotive.managers.NetworkManager;
import ca.pluszero.emotive.managers.YouTubeManager;
import ca.pluszero.emotive.models.PrimaryOption;
import ca.pluszero.emotive.models.YouTubeVideo;
import ca.pluszero.emotive.utils.DateTimeUtils;

public class MainFragment extends Fragment implements View.OnClickListener, YouTubeManager.OnFinishedListener, MusicManager.IMusicLoadedListener {

    public static String FRAGMENT_TAG = "main_fragment"; // set from activity_main xml
    private Button[] primaryButtons;

    private Button bFirstButton;
    private Button bSecondButton;
    private Button bThirdButton;
    private Button bFourthButton;
    private Button bFifthButton;
    private Button bSixthButton;

    private ListView lvQueryResults;

    private PrimaryOption mPrimaryOption;

    private AutoCompleteTextView etSearchView;
    private View rootView;
    private TextSwitcher mSwitcher;
    private Animation slideUp;
    private ImageView imgFirstOption;
    private ImageView imgSecondOption;
    private ImageView imgThirdOption;
    private ImageView imgFourthOption;
    private ImageView imgFifthOption;
    private ImageView imgSixthOption;
    private ImageView[] primaryImages;
    private boolean startedMusicSearch;

    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {

            // Create a new TextView
            TextView t = new TextView(getActivity());
            t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            t.setShadowLayer(3, -3, -3, R.color.text_shadow);
            t.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            t.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            return t;
        }
    };

    private TextWatcher musicTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            startMusicSearchDevice(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        lvQueryResults = (ListView) rootView.findViewById(R.id.lvQueryResults);
        etSearchView = (AutoCompleteTextView) rootView.findViewById(R.id.mainSearchView);
        setupAnimations();

        setup();
        setupPrimaryButtons(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    public void setup() {
        mSwitcher.setText(DateTimeUtils.getGreetingBasedOnTimeOfDay() + ",\n What do you want to do?");
        rootView.findViewById(R.id.scroll_view_main_container).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.ll_panel_container).setVisibility(View.GONE);

        etSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(v.getText().toString());
                    return true;
                }
                return false;
            }

        });
        lvQueryResults.setOnItemClickListener(null);
        lvQueryResults.setOnScrollListener(null);
        if (lvQueryResults.getAdapter() != null) {
            if (lvQueryResults.getAdapter() instanceof BaseArrayAdapter) {
                ((BaseArrayAdapter) lvQueryResults.getAdapter()).clear();
            }
        }
        lvQueryResults.setAdapter(null);

    }

    private void setupAnimations() {
        mSwitcher = (TextSwitcher) rootView.findViewById(R.id.mainTextview);
        // Set the factory used to create TextViews to switch between.
        mSwitcher.setFactory(mFactory);

                /*
         * Set the in and out animations. Using the fade_in/out animations
         * provided by the framework.
         */
        Animation in = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_out_right);
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);

        slideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_up);
    }

    private void setupPrimaryButtons(View rootView) {
        bFirstButton = (Button) rootView.findViewById(R.id.bFirstOption);
        bSecondButton = (Button) rootView.findViewById(R.id.bSecondOption);
        bThirdButton = (Button) rootView.findViewById(R.id.bThirdOption);
        bFourthButton = (Button) rootView.findViewById(R.id.bFourthOption);
        bFifthButton = (Button) rootView.findViewById(R.id.bFifthOption);
        bSixthButton = (Button) rootView.findViewById(R.id.bSixthOption);

        imgFirstOption = (ImageView) rootView.findViewById(R.id.imgFirstOption);
        imgSecondOption = (ImageView) rootView.findViewById(R.id.imgSecondOption);
        imgThirdOption = (ImageView) rootView.findViewById(R.id.imgThirdOption);
        imgFourthOption = (ImageView) rootView.findViewById(R.id.imgFourthOption);
        imgFifthOption = (ImageView) rootView.findViewById(R.id.imgFifthOption);
        imgSixthOption = (ImageView) rootView.findViewById(R.id.imgSixthOption);

        primaryButtons = new Button[]{bFirstButton, bSecondButton, bThirdButton, bFourthButton, bFifthButton, bSixthButton};
        primaryImages = new ImageView[]{imgFirstOption, imgSecondOption, imgThirdOption, imgFourthOption, imgFifthOption, imgSixthOption};

        PrimaryOption[] options = fetchOptions();
        for (int i = 0; i < primaryButtons.length; i++) {
            primaryButtons[i].setTag(R.string.option_key, options[i]);
            primaryButtons[i].setOnClickListener(this);
            primaryImages[i].setTag(R.string.option_key, options[i]);
            primaryImages[i].setOnClickListener(this);
        }
    }

    private PrimaryOption[] fetchOptions() {
        // TODO: use algo. based on time of day, and user's past experiences with this app
        return new PrimaryOption[]{
                PrimaryOption.FOOD,
                PrimaryOption.LISTEN,
                PrimaryOption.GOOGLE,
                PrimaryOption.FIND,
                PrimaryOption.YOUTUBE,
                PrimaryOption.WEATHER
        };
    }

    private void performSearch(String query) {
        if (mPrimaryOption == PrimaryOption.FIND) {
            startMapsSearch(query);
        } else if (mPrimaryOption == PrimaryOption.LISTEN) {
            dismissKeyboard();
        } else if (mPrimaryOption == PrimaryOption.GOOGLE) {
            startGoogleSearchAnything(query);
        } else if (mPrimaryOption == PrimaryOption.YOUTUBE) {
            startYouTubeSearch(query);
        }
    }

    private void startYouTubeSearch(CharSequence query) {
        if (NetworkManager.isConnected(getActivity())) {
            YouTubeManager manager = YouTubeManager.getInstance(this);
            manager.clearNextPageToken();
            manager.getYouTubeSearch(query.toString());
        } else {
            // No interwebs; display Toast. TODO
        }
        if (lvQueryResults.getAdapter() != null) {
            ((BaseArrayAdapter) lvQueryResults.getAdapter()).clear();
        }
        lvQueryResults
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        lvQueryResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                YouTubeManager.getInstance(MainFragment.this).loadMoreDataFromApi();
            }
        });
        // Intent intent = new Intent(Intent.ACTION_SEARCH);
        // intent.setPackage("com.google_icon.android.youtube");
        // intent.putExtra("search_query", query);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);
    }

    private void startMapsSearch(CharSequence query) {
        String url = "geo:0,0?q=" + query;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void startGoogleSearchAnything(CharSequence query) {
        Intent browserIntent = new Intent(Intent.ACTION_WEB_SEARCH);
        browserIntent.putExtra(SearchManager.QUERY, query.toString());
        startActivity(browserIntent);
    }

    private void startMusicSearchDevice(String query) {
        MusicManager musicLauncher = MusicManager.getInstance(this, this);
        if (!startedMusicSearch) {
            String[] columns = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM};
            int[] mSongListItems = {R.id.tvMusicTitle, R.id.tvMusicDuration, R.id.tvMusicArtist, R.id.tvMusicAlbum};
            MusicCursorAdapter adapter = new MusicCursorAdapter(getActivity(), R.layout.music_row_card, null, columns, mSongListItems);
            lvQueryResults.setAdapter(adapter);
            lvQueryResults.setOnItemClickListener(adapter);
            bringUpListView();
            startedMusicSearch = true;
        }
        musicLauncher.searchMusic(query);
    }

    private void bringUpListView() {
        LinearLayout queryResultsContainer = (LinearLayout) rootView.findViewById(R.id.ll_panel_container);
        queryResultsContainer.setVisibility(View.VISIBLE);

        // TODO: do only on 4.4
        lvQueryResults.setPadding(0, 0, 0, getNavbarHeight());
        lvQueryResults.setClipToPadding(false);

//        LinearLayout searchContainer = (LinearLayout) rootView.findViewById(R.id.ll_search_container);
//        ((ViewGroup) rootView.findViewById(R.id.main_container)).removeView(searchContainer);
//        queryResultsContainer.removeView(searchContainer);
//        queryResultsContainer.addView(searchContainer, 0);
//        etSearchView = (AutoCompleteTextView) queryResultsContainer.findViewById(R.id.mainSearchView);
//        etSearchView.requestFocus();
//        etSearchView.setSelection(etSearchView.length());
//        rootView.findViewById(R.id.ll_panel_container).startAnimation(slideUp);
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearchView.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        PrimaryOption clickedOption = (PrimaryOption) v.getTag(R.string.option_key);
        ((MainActivity) getActivity()).setOnHomePage(false);
        switch (clickedOption) {
            case FOOD:
                mPrimaryOption = PrimaryOption.FOOD;
                setupFoodOptions();
                break;
            case LISTEN:
                mPrimaryOption = PrimaryOption.LISTEN;
                setupListenOptions();
                break;
            case GOOGLE:
                mPrimaryOption = PrimaryOption.GOOGLE;
                setupGoogleOptions();
                break;
            case FIND:
                mPrimaryOption = PrimaryOption.FIND;
                setupFindOptions();
                break;
            case YOUTUBE:
                mPrimaryOption = PrimaryOption.YOUTUBE;
                setupYoutubeOptions();
                break;
            case WEATHER:
                mPrimaryOption = PrimaryOption.WEATHER;
                setupWeatherOptions();
                break;
        }
    }

    private void setupFoodOptions() {
        setupButton();
    }

    private void setupListenOptions() {
        setupButton();
        etSearchView.addTextChangedListener(musicTextWatcher);
    }

    private void setupGoogleOptions() {
        setupButton();
    }

    private void setupFindOptions() {
        setupButton();
        etSearchView.setAdapter(new PlacesAutoCompleteAdapter(
                getActivity(), R.layout.simple_list_item));
        etSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                etSearchView.setText((String) adapterView.getItemAtPosition(position));
            }
        });
    }

    private void setupYoutubeOptions() {
        setupButton();
    }

    private void setupWeatherOptions() {
        setupButton();
    }


    private void setupButton() {
        LinearLayout searchContainer = (LinearLayout) rootView.findViewById(R.id.ll_search_container);
        rootView.findViewById(R.id.scroll_view_main_container).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_panel_container).setVisibility(View.VISIBLE);

        etSearchView.setHint(mPrimaryOption.getMainInfo());
        etSearchView.setFocusableInTouchMode(true);
        etSearchView.requestFocus();

        // Show keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearchView, InputMethodManager.SHOW_IMPLICIT);

        searchContainer.setVisibility(View.VISIBLE);
        searchContainer.startAnimation(slideUp);

        if (mPrimaryOption != PrimaryOption.FIND) {
            etSearchView.setOnItemClickListener(null);
            if (etSearchView.getAdapter() != null) {
                etSearchView.setAdapter((ArrayAdapter<String>) null);
            }
        }
        if (mPrimaryOption != PrimaryOption.LISTEN) {
            etSearchView.removeTextChangedListener(musicTextWatcher);
        }
        etSearchView.setText(""); // Clear out old text
    }

    private int getNavbarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public void onInitialYoutubeQueryFinished(List<YouTubeVideo> videos) {
        dismissKeyboard();
        bringUpListView();
        lvQueryResults.setAdapter(new YouTubeListAdapter(
                getActivity(), videos));
    }

    @Override
    public synchronized void onMoreVideosReceived(List<YouTubeVideo> videos) {
        if (lvQueryResults.getAdapter() != null) {
            ((BaseArrayAdapter) lvQueryResults.getAdapter()).addItems(videos);
        }
    }

    @Override
    public void onLoaderReset() {
        // Clears out the adapter's reference to the Cursor. This prevents memory leaks.
        ((MusicCursorAdapter) lvQueryResults.getAdapter()).swapCursor(null);
    }

    @Override
    public void onLoadFinished(Cursor cursor) {
        MusicCursorAdapter adapter = (MusicCursorAdapter) lvQueryResults.getAdapter();
        if (adapter != null) {
            adapter.setViewBinder(new MusicCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.tvMusicDuration) {
                        ((TextView) view).setText(DateTimeUtils.formatMillis(cursor.getString(columnIndex)));
                        return true;
                    }
                    return false;
                }
            });
            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();
        }
    }
}
