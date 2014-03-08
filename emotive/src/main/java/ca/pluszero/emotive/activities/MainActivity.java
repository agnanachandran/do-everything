package ca.pluszero.emotive.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import ca.pluszero.emotive.R;
import ca.pluszero.emotive.adapters.DrawerListAdapter;
import ca.pluszero.emotive.fragments.MainSectionFragment;
import ca.pluszero.emotive.models.DrawerItem;

public class MainActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mActionBarTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<DrawerItem> mDrawerItems;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        // Set up image cacher/retriever
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

        // Initialize non-view instance vars
        mActionBarTitle = mDrawerTitle = getTitle();
        
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START); 
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
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
        
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private void loadDrawerItems(List<DrawerItem> drawerItems) {
        String[] drawerItemTitles = getResources().getStringArray(R.array.drawer_titles);
        TypedArray icons = getResources().obtainTypedArray(R.array.drawer_icons);
        for(int i = 0; i < drawerItemTitles.length; i++) {
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
    	// TODO: Probably nothing to do here at the moment http://developer.android.com/training/implementing-navigation/nav-drawer.html
    	return super.onPrepareOptionsMenu(menu);
    }
    
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new MainSectionFragment();
            Bundle args = new Bundle();
            args.putInt(MainSectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_new_section).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
    	
    	@SuppressWarnings("rawtypes")
		@Override
    	public void onItemClick(AdapterView parent, View view, int position, long id)
    	{
    		selectItem(position);
    	}

    	private void selectItem(int position)
    	{
    		switch (position) {
    		case 0:
    			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
    			MainActivity.this.startActivity(intent);
    			break;
    		case 1:
    			Intent i = new Intent(Intent.ACTION_SEND);
    			i.setType("message/rfc822");
    			i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getResources().getString(R.string.my_email)});
    			i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.feedback_subject));
    			i.putExtra(Intent.EXTRA_TEXT   , "");
    			try {
    			    startActivity(Intent.createChooser(i, "Send mail..."));
    			} catch (android.content.ActivityNotFoundException ex) {
    			    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
    			}
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


}
