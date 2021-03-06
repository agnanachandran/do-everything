package ca.pluszero.emotive.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ca.pluszero.emotive.R;
import ca.pluszero.emotive.adapters.DrawerListAdapter;
import ca.pluszero.emotive.fragments.MainFragment;
import ca.pluszero.emotive.models.Choice;
import ca.pluszero.emotive.models.DrawerItem;

public class MainActivity extends FragmentActivity {

    public static final String PRESSED_OPTION = "pressed option";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mActionBarTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<DrawerItem> mDrawerItems;
    private boolean onHomePage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Set up image cacher/retriever
        setUpImageLoader();

        int statusBarHeight = getStatusBarHeight();
        int actionBarSize = getActionBarSize();

        // Initialize non-view instance vars
        mActionBarTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        if (isAtLeastKitKatDevice()) {
            mDrawerList.setPadding(15, statusBarHeight + actionBarSize, 0, 4);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {
            // Called when drawer has settled in completely closed state
            public void onDrawerClosed(View view) {
//        		super.onDrawerClosed(view);
                getActionBar().setTitle(mActionBarTitle);
                invalidateOptionsMenu();
            }

            // Called when drawer has settled in completely open state
            public void onDrawerOpened(View drawerView) {
//        		super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set adapter for the list view
        mDrawerItems = new ArrayList<DrawerItem>();
        loadDrawerItems(mDrawerItems);

        mDrawerList.setAdapter(new DrawerListAdapter(this, R.layout.drawer_list_item, mDrawerItems));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getPressedOptionFromWidget();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.FRAGMENT_TAG);
        fragment.dismissProgressBar();
        setUpBackground();
//        getSupportFragmentManager().beginTransaction().attach(getSupportFragmentManager().getFragments().get(0)).commit();
    }

    private void setUpBackground() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String background = preferences.getString("background", "dark_sun_landscape");
        Drawable bgDrawable;
        if (background.equals("dark_sun_landscape")) {
            bgDrawable = getResources().getDrawable(R.drawable.dark_sun_landscape);
        } else if (background.equals("starry_sky")) {
            bgDrawable = getResources().getDrawable(R.drawable.starry_sky_1);
        } else if (background.equals("starry_landscape")) {
            bgDrawable = getResources().getDrawable(R.drawable.starry_landscape);
        } else {
            bgDrawable = getResources().getDrawable(R.drawable.dark_sun_landscape);
        }
        mDrawerLayout.setBackground(bgDrawable);
    }

    private void getPressedOptionFromWidget() {
        if (getIntent().hasExtra(PRESSED_OPTION)) {
            Choice choice = (Choice) getIntent().getSerializableExtra(PRESSED_OPTION);
            MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.FRAGMENT_TAG);
            fragment.clickOption(choice);
            hugeTimerHackToShowKeyboard();
        }
    }

    private void setUpImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
    }

    // TODO: change this if possible.
    private void hugeTimerHackToShowKeyboard() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(findViewById(R.id.mainSearchView), InputMethodManager.SHOW_IMPLICIT);
                    }
                },
                500
        );
    }

    public int getActionBarSize() {
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    private void loadDrawerItems(List<DrawerItem> drawerItems) {
        String[] drawerItemTitles = getResources().getStringArray(R.array.drawer_titles);
        TypedArray icons = getResources().obtainTypedArray(R.array.drawer_icons);
        for (int i = 0; i < drawerItemTitles.length; i++) {
            drawerItems.add(new DrawerItem(drawerItemTitles[i], icons.getDrawable(i)));
        }
        icons.recycle();
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                startSettingsActivity();
                return true;
            case R.id.action_feedback:
                displayFeedback();
                return true;
        }

        // handle other action bar items here

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Called upon calling invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Probably nothing to do here at the moment http://developer.android.com/training/implementing-navigation/nav-drawer.html
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (onHomePage) {
            super.onBackPressed();
        } else {
            // resetUi
            MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.FRAGMENT_TAG);
            fragment.setup();
            fragment.dismissProgressBar();
            onHomePage = true;
        }
        // If on the start screen, back should just do super.onBackPressed(); otherwise, go back to start screen

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private boolean isAtLeastKitKatDevice() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public void setOnHomePage(boolean onHomePage) {
        this.onHomePage = onHomePage;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }

        private void selectItem(int position) {
            switch (position) {
                case 0:
                    startSettingsActivity();
                    break;
                case 1:
                    displayFeedback();
                    break;
                default:
                    break;
            }
            mDrawerList.setItemChecked(position, true);
            setTitle(mDrawerItems.get(position).getDrawerText());
            mDrawerLayout.closeDrawer(mDrawerList);
        }

        public void setTitle(CharSequence title) {
//    		mActionBarTitle = title;
//    		getActionBar().setTitle(mActionBarTitle);
        }

    }

    private void displayFeedback() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.my_email)});
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.feedback_subject));
        i.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }

}
