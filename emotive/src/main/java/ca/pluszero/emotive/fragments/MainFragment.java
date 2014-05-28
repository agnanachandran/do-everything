package ca.pluszero.emotive.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.youtube.player.YouTubeIntents;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ca.pluszero.emotive.R;
import ca.pluszero.emotive.activities.MainActivity;
import ca.pluszero.emotive.adapters.BaseArrayAdapter;
import ca.pluszero.emotive.adapters.MusicCursorAdapter;
import ca.pluszero.emotive.adapters.PlacesAutoCompleteAdapter;
import ca.pluszero.emotive.adapters.YelpListAdapter;
import ca.pluszero.emotive.adapters.YouTubeListAdapter;
import ca.pluszero.emotive.database.ChoiceDataSource;
import ca.pluszero.emotive.listeners.EndlessScrollListener;
import ca.pluszero.emotive.managers.MusicManager;
import ca.pluszero.emotive.managers.NetworkManager;
import ca.pluszero.emotive.managers.PlaceDetailsManager;
import ca.pluszero.emotive.managers.WeatherManager;
import ca.pluszero.emotive.managers.YelpManager;
import ca.pluszero.emotive.managers.YouTubeManager;
import ca.pluszero.emotive.models.Choice;
import ca.pluszero.emotive.models.Forecast;
import ca.pluszero.emotive.models.Place;
import ca.pluszero.emotive.models.PlaceDetails;
import ca.pluszero.emotive.models.YelpData;
import ca.pluszero.emotive.models.YouTubeVideo;
import ca.pluszero.emotive.utils.DateTimeUtils;
import ca.pluszero.emotive.utils.ScreenUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class MainFragment extends Fragment implements View.OnClickListener, YouTubeManager.OnFinishedListener, MusicManager.IMusicLoadedListener, PlaceDetailsManager.OnFinishedListener, WeatherManager.OnFinishedListener, YelpManager.OnYelpFinishedListener {

    public static final String FRAGMENT_TAG = "main_fragment";
    private static final String DEGREE_SYMBOL = "°";
    private Location currentLocation;
    private boolean startedFoodSearch;

    Criteria crit = new Criteria();
    {
        crit.setAccuracy(Criteria.ACCURACY_LOW);
        crit.setPowerRequirement(Criteria.NO_REQUIREMENT);
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            currentLocation = location;
            if (mPrimaryOption == Choice.WEATHER) {
                displayWeather();
            } else if (mPrimaryOption == Choice.FOOD) {
                new YelpManager(MainFragment.this).query(currentQuery, currentLocation);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private SmoothProgressBar progressBar;
    private Button bFirstButton;
    private Button bSecondButton;
    private Button bThirdButton;
    private Button bFourthButton;
    private Button bFifthButton;
    private Button bSixthButton;
    private ImageView imgFirstOption;
    private ImageView imgSecondOption;
    private ImageView imgThirdOption;
    private ImageView imgFourthOption;
    private ImageView imgFifthOption;
    private ImageView imgSixthOption;
    private Button[] primaryButtons;
    private ImageView[] primaryImages;
    private ListView lvQueryResults;
    private Choice mPrimaryOption;
    private AutoCompleteTextView etSearchView;
    private View rootView;
    private TextSwitcher mSwitcher;
    private Animation slideUp;
    private Place place;
    private String currentQuery;
    private MusicCursorAdapter musicCursorAdapter;
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
    private TextWatcher clearSearchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            View searchClear = rootView.findViewById(R.id.imgSearchClear);
            if (s.length() > 0) {
                searchClear.setVisibility(View.VISIBLE);
            } else {
                searchClear.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {  }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        rootView.findViewById(R.id.outermost_main_container).setPadding(0, ((MainActivity) getActivity()).getStatusBarHeight()
                + ((MainActivity) getActivity()).getActionBarSize() + 30, 0, 0);

        lvQueryResults = (ListView) rootView.findViewById(R.id.lvQueryResults);
        progressBar = (SmoothProgressBar) rootView.findViewById(R.id.progress_bar);

        if (isKitKatDevice()) {
            lvQueryResults.setPadding(0, 0, 0, ScreenUtils.getNavbarHeight(getResources()));
            lvQueryResults.setClipToPadding(false);
        }
        etSearchView = (AutoCompleteTextView) rootView.findViewById(R.id.mainSearchView);
        setupAnimations();

        setup();
        setupPrimaryButtons(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    public void setup() {
        dismissProgressBar();
        mPrimaryOption = null;
        mSwitcher.setText(DateTimeUtils.getGreetingBasedOnTimeOfDay() + ",\n What do you want to do?");
        showPanel(false);
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(locationListener);

        etSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = v.getText().toString();

                    performSearch(query);
                    return true;
                }
                return false;
            }

        });
        lvQueryResults.setOnItemClickListener(null);
        lvQueryResults.setOnScrollListener(null);
        lvQueryResults.setVisibility(View.GONE);
        rootView.findViewById(R.id.imgSearchClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearchView.setText("");
            }
        });
        if (lvQueryResults.getAdapter() != null) {
            if (lvQueryResults.getAdapter() instanceof BaseArrayAdapter) {
                ((BaseArrayAdapter) lvQueryResults.getAdapter()).clear();
            }
        }
        lvQueryResults.setAdapter(null);

    }

    private void showPanel(boolean shouldShowPanel) {
        if (shouldShowPanel) {
            rootView.findViewById(R.id.scroll_view_main_container).setVisibility(View.GONE);
            rootView.findViewById(R.id.ll_panel_container).setVisibility(View.VISIBLE);
        } else {
            rootView.findViewById(R.id.scroll_view_main_container).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.ll_panel_container).setVisibility(View.GONE);
            rootView.findViewById(R.id.weather_container).setVisibility(View.GONE);
        }
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

        List<Choice> choices = fetchChoices();
        for (int i = 0; i < primaryButtons.length; i++) {
            Choice choice = choices.get(i);
            primaryButtons[i].setText(choice.getTitle());
            primaryButtons[i].setTag(R.string.option_key, choice);
            primaryButtons[i].setOnClickListener(this);

            primaryImages[i].setImageDrawable(getResources().getDrawable(choice.getSelector()));
            primaryImages[i].setTag(R.string.option_key, choice);
            primaryImages[i].setOnClickListener(this);
        }
    }

    private List<Choice> fetchChoices() {
        // TODO: use algo. based on time of day, and user's past experiences with this app
//        ChoiceDataSource dataSource = new ChoiceDataSource(getActivity());
//        try {
//            dataSource.open();
//            List<Choice> choices = dataSource.getAllChoices();
//            Collections.sort(choices, new Comparator<Choice>() {
//                @Override
//                public int compare(Choice lhs, Choice rhs) {
//                    return rhs.getTimesTapped() - lhs.getTimesTapped();
//                }
//            });
//            return Collections.unmodifiableList(choices);
//        } catch (SQLException e) {
//            Log.e(MainFragment.class.getName(), "SQLException: " + e.getMessage());
        List<Choice> choices = new ArrayList<Choice>();
        choices.add(Choice.FOOD);
        choices.add(Choice.LISTEN);
        choices.add(Choice.GOOGLE);
        choices.add(Choice.FIND);
        choices.add(Choice.YOUTUBE);
        choices.add(Choice.WEATHER);
        return choices;
//        }

    }

    private void performSearch(String query) {
        currentQuery = query;
        if (mPrimaryOption == Choice.FOOD) {
            startFoodSearch(query);
        } else if (mPrimaryOption == Choice.FIND) {
            startMapsSearch(query);
        } else if (mPrimaryOption == Choice.LISTEN) {
            startMusicSearch(query);
        } else if (mPrimaryOption == Choice.GOOGLE) {
            startGoogleSearchAnything(query);
        } else if (mPrimaryOption == Choice.YOUTUBE) {
            startYouTubeSearch(query);
        } else if (mPrimaryOption == Choice.WEATHER) {
            startWeatherSearch();
        }
    }

    private void startMusicSearch(String query) {
        boolean shouldOpenGrooveshark = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("grooveshark_checkbox", false);
        if (shouldOpenGrooveshark && query.matches("[A-Za-z0-9 ]+ gs")) {
            Intent grooveSharkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://html5.grooveshark.com/#!/search/" + query.substring(0, query.lastIndexOf(" gs")).replaceAll(" ", "%20")));
            startActivity(grooveSharkIntent);
        }
        dismissKeyboard();
    }

    private void startFoodSearch(String query) {
        dismissKeyboard();
        showProgressBar();
        if (currentLocation == null) {
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(lm.getBestProvider(crit, true), 500, 10, locationListener);
        } else {
            new YelpManager(this).query(query, currentLocation);
        }
    }

    private void displayWeather() {
        double longitude = currentLocation.getLongitude();
        double latitude = currentLocation.getLatitude();
        new WeatherManager(this).getWeatherQuery(new PlaceDetails(String.valueOf(latitude), String.valueOf(longitude)));
    }

    private void showProgressBar() {
        if (!getActivity().isFinishing()) {
            if (mPrimaryOption == Choice.YOUTUBE) {
                progressBar.setSmoothProgressDrawableColors(getResources().getIntArray(R.array.youtube_colors));
            } else if (mPrimaryOption == Choice.WEATHER) {
                progressBar.setSmoothProgressDrawableColors(getResources().getIntArray(R.array.weather_colors));
            } else if (mPrimaryOption == Choice.FOOD) {
                progressBar.setSmoothProgressDrawableColors(getResources().getIntArray(R.array.food_colors));
            }
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.progressiveStop();
                progressBar.setVisibility(View.VISIBLE);
                progressBar.progressiveStart();
            }
        }
    }

    public void dismissProgressBar() {
        progressBar.progressiveStop();
        progressBar.setVisibility(View.GONE);
    }

    private void startWeatherSearch() {
        dismissKeyboard();
        PlaceDetailsManager placeManager = new PlaceDetailsManager(this);
        if (NetworkManager.isConnected(getActivity())) {
            if (place != null) {
                showProgressBar();
                placeManager.getPlaceDetailsQuery(place.getReference());
            } else {
                Toast.makeText(MainFragment.this.getActivity(), "Please type a query and select an item from the dropdown.", Toast.LENGTH_LONG).show();
            }
        } else {
            displayNetworkConnectionToast();
        }
    }

    private void startYouTubeSearch(CharSequence query) {
        final YouTubeManager manager = new YouTubeManager(this);
        if (NetworkManager.isConnected(getActivity())) {
            showProgressBar();
            manager.clearNextPageToken();
            manager.getYouTubeSearch(query.toString());
        } else {
            displayNetworkConnectionToast();
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
                manager.loadMoreDataFromApi();
                showProgressBar();
            }
        });
        // Intent intent = new Intent(Intent.ACTION_SEARCH);
        // intent.setPackage("com.google_icon.android.youtube");
        // intent.putExtra("search_query", query);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(intent);
    }

    private void displayNetworkConnectionToast() {
        Toast.makeText(getActivity(), "Please make sure you are connected to the internet.", Toast.LENGTH_LONG).show();
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
        MusicManager musicLauncher = new MusicManager(this, this);
        String[] columns = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM};
        int[] mSongListItems = {R.id.tvMusicTitle, R.id.tvMusicDuration, R.id.tvMusicArtist, R.id.tvMusicAlbum};
        musicCursorAdapter = new MusicCursorAdapter(getActivity(), R.layout.music_row_card, null, columns, mSongListItems);
        bringUpListView();
        lvQueryResults.setAdapter(musicCursorAdapter);
        lvQueryResults.setOnItemClickListener(musicCursorAdapter);
        musicLauncher.searchMusic(query);
    }

    private void bringUpListView() {
        LinearLayout resultsContainer = (LinearLayout) rootView.findViewById(R.id.ll_panel_container);
        resultsContainer.setVisibility(View.VISIBLE);
        lvQueryResults.setVisibility(View.VISIBLE);
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearchView.getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        Choice clickedOption = (Choice) v.getTag(R.string.option_key);
        clickOption(clickedOption);
    }

    public void clickOption(Choice clickedOption) {
        ((MainActivity) getActivity()).setOnHomePage(false);
        switch (clickedOption) {
            case FOOD:
                mPrimaryOption = Choice.FOOD;
                setupFoodOptions();
                break;
            case LISTEN:
                mPrimaryOption = Choice.LISTEN;
                setupListenOptions();
                break;
            case GOOGLE:
                mPrimaryOption = Choice.GOOGLE;
                setupGoogleOptions();
                break;
            case FIND:
                mPrimaryOption = Choice.FIND;
                setupFindOptions();
                break;
            case YOUTUBE:
                mPrimaryOption = Choice.YOUTUBE;
                setupYoutubeOptions();
                break;
            case WEATHER:
                mPrimaryOption = Choice.WEATHER;
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
        etSearchView.setText("");
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
                setTextAndCursorOfSearchEditText(adapterView, position);
            }
        });
    }

    private void setupYoutubeOptions() {
        setupButton();
    }

    private void setupWeatherOptions() {
        setupButton();
        etSearchView.setAdapter(new PlacesAutoCompleteAdapter(
                getActivity(), R.layout.simple_list_item, true));
        etSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                setTextAndCursorOfSearchEditText(adapterView, position);
                place = ((PlacesAutoCompleteAdapter) etSearchView.getAdapter()).getItemForPosition(position);
            }
        });

        if (NetworkManager.isConnected(getActivity())) {
            if (currentLocation != null) {
                displayWeather();
            } else {
                showProgressBar();
                LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                lm.requestLocationUpdates(lm.getBestProvider(crit, true), 500, 10, locationListener);
            }
        }
    }

    private void setupButton() {
        LinearLayout searchContainer = (LinearLayout) rootView.findViewById(R.id.ll_search_container);
        showPanel(true);

        currentQuery = null;
        ChoiceDataSource dataSource = new ChoiceDataSource(getActivity());
        try {
            dataSource.open();
            dataSource.updateChoice(mPrimaryOption);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        etSearchView.setHint(mPrimaryOption.getMainInfo());
        etSearchView.setFocusableInTouchMode(true);
        etSearchView.requestFocus();

        searchContainer.setVisibility(View.VISIBLE);
        searchContainer.startAnimation(slideUp);

        lvQueryResults.setAdapter(null);

        if (mPrimaryOption != Choice.FIND) {
            etSearchView.setOnItemClickListener(null);
            if (etSearchView.getAdapter() != null) {
                etSearchView.setAdapter((ArrayAdapter<String>) null);
            }
        }
        etSearchView.removeTextChangedListener(musicTextWatcher);
        etSearchView.addTextChangedListener(clearSearchTextWatcher);
        etSearchView.setText(""); // Clear out old text

        // Show keyboard if the weather option is not chosen
        if (mPrimaryOption != Choice.WEATHER) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearchView, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onInitialYoutubeQueryFinished(List<YouTubeVideo> videos) {
        if (isAdded()) {
            dismissKeyboard();
            dismissProgressBar();
            if (mPrimaryOption == Choice.YOUTUBE) {
                bringUpListView();
                lvQueryResults.setAdapter(new YouTubeListAdapter(
                        getActivity(), videos));
            }
        }
    }

    @Override
    public void onMoreVideosReceived(List<YouTubeVideo> videos) {
        if (isAdded()) {
            if (lvQueryResults.getAdapter() != null) {
                ((BaseArrayAdapter) lvQueryResults.getAdapter()).addItems(videos);
                dismissProgressBar();
            }
        }
    }

    @Override
    public void onLoaderReset() {
        // Clears out the adapter's reference to the Cursor. This prevents memory leaks.
        MusicCursorAdapter adapter = (MusicCursorAdapter) lvQueryResults.getAdapter();
        if (adapter != null) {
            adapter.swapCursor(null);
        }
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

    private void setTextAndCursorOfSearchEditText(AdapterView<?> adapterView, int position) {
        String selectionText = (String) adapterView.getItemAtPosition(position);
        etSearchView.setText(selectionText);
        etSearchView.setSelection(selectionText.length());
    }

    @Override
    public void onPlaceDetailsQueryFinished(PlaceDetails placeDetails) {
        new WeatherManager(this).getWeatherQuery(placeDetails);
    }

    // TODO: Refactor into WeatherViewFormatter
    @Override
    public void onWeatherQueryFinished(Forecast weatherData) {
        dismissProgressBar();
        if (mPrimaryOption != Choice.WEATHER) {
            return;
        }
        View weatherContainer = rootView.findViewById(R.id.weather_container);
        weatherContainer.setVisibility(View.VISIBLE);

        if (isKitKatDevice()) {
            weatherContainer.setPadding(0, 0, 0, ScreenUtils.getNavbarHeight(getResources()));
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean showFahrenheit = preferences.getBoolean("fahrenheit_checkbox", false);
        boolean showMiles = preferences.getBoolean("miles_checkbox", false);

        ((TextView) rootView.findViewById(R.id.weather_temp)).setText((showFahrenheit ? weatherData.getTemperatureInFahrenheit() : weatherData.getTemperatureInCelsius()) + DEGREE_SYMBOL);
//        ((TextView) rootView.findViewById(R.id.weather_feels_like)).setText(getString(R.string.feels_like, String.valueOf(showFahrenheit ? weatherData.getApparentTemperatureInFahrenheit() : weatherData.getApparentTemperatureInCelsius())));
        ((TextView) rootView.findViewById(R.id.weather_status)).setText(weatherData.getSummary());
        ((TextView) rootView.findViewById(R.id.weather_humidity)).setText(getString(R.string.humidity, String.valueOf(weatherData.getHumidity())));
        ((TextView) rootView.findViewById(R.id.weather_wind_speed)).setText(getString(R.string.wind_speed, weatherData.getFormattedWindSpeed(showMiles), showMiles ? "mi/h" : "km/h"));

        setupCityCountryWeatherInfo();

        Drawable weatherIcon = getResources().getDrawable(weatherData.getIcon().getDrawableId());
        ((ImageView) rootView.findViewById(R.id.weather_now_icon)).setImageDrawable(weatherIcon);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewGroup weatherHourlyContainer = (ViewGroup) rootView.findViewById(R.id.weather_hourly_container);
        weatherHourlyContainer.removeAllViews();

        for (Forecast.FutureWeather weather : weatherData.getHourlyWeatherList()) {
            LinearLayout weatherHourlyCardContainer = (LinearLayout) inflater.inflate(R.layout.weather_future_card_container, weatherHourlyContainer, false);
            weatherHourlyContainer.addView(weatherHourlyCardContainer);
            ((TextView) weatherHourlyCardContainer.findViewById(R.id.weather_card_time)).setText(weather.getHourAsString());
            int hourlyWeatherIconId = weather.getIcon().getDrawableId();
            ((ImageView) weatherHourlyCardContainer.findViewById(R.id.weather_card_icon)).setImageDrawable(
                    getResources().getDrawable(hourlyWeatherIconId));
            ((TextView) weatherHourlyCardContainer.findViewById(R.id.weather_card_temp)).setText((showFahrenheit ? weather.getTemp().getValueInFahrenheit() : weather.getTemp().toCelsius()) + DEGREE_SYMBOL);
        }

        ViewGroup weatherDailyContainer = (ViewGroup) rootView.findViewById(R.id.weather_daily_container);
        weatherDailyContainer.removeAllViews();

        for (Forecast.FutureWeather weather : weatherData.getDailyWeatherList()) {
            LinearLayout weatherDailyCardContainer = (LinearLayout) inflater.inflate(R.layout.weather_future_card_container, weatherHourlyContainer, false);
            weatherDailyContainer.addView(weatherDailyCardContainer);
            ((TextView) weatherDailyCardContainer.findViewById(R.id.weather_card_time)).setText(weather.getDayAsString());
            int hourlyWeatherIconId = weather.getIcon().getDrawableId();
            ((ImageView) weatherDailyCardContainer.findViewById(R.id.weather_card_icon)).setImageDrawable(
                    getResources().getDrawable(hourlyWeatherIconId));
            ((TextView) weatherDailyCardContainer.findViewById(R.id.weather_card_temp)).setText((showFahrenheit ? weather.getTemp().getValueInFahrenheit() : weather.getTemp().toCelsius()) + DEGREE_SYMBOL);
        }
    }

    private boolean isKitKatDevice() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
    }

    private void setupCityCountryWeatherInfo() {
        TextView tvCountryName = (TextView) rootView.findViewById(R.id.weather_country);

        String cityName = "";
        String countryName = "";

        if (currentQuery != null && !currentQuery.isEmpty() && currentQuery.contains(", ")) {
            String[] querySplit = currentQuery.split(", ");
            cityName = querySplit[0];
            countryName = currentQuery.substring(currentQuery.indexOf(',') + 2);
            currentQuery = null;
        } else {
            if (currentQuery == null) {
                cityName = "My Location";
            } else {
                cityName = currentQuery;
            }
            tvCountryName.setVisibility(View.GONE);
        }
        ((TextView) rootView.findViewById(R.id.weather_city)).setText(cityName);
        if (!countryName.isEmpty()) {
            tvCountryName.setText(countryName);
            tvCountryName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onYelpDataRetrieved(List<YelpData> datas) {
        dismissProgressBar();
        if (mPrimaryOption == Choice.FOOD) {
            bringUpListView();
            lvQueryResults.setAdapter(new YelpListAdapter(getActivity(), datas));
        }
    }
}
