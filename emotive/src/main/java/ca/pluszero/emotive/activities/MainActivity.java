package ca.pluszero.emotive.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import ca.pluszero.emotive.R;
import ca.pluszero.emotive.adapters.DrawerListAdapter;
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

        int statusBarHeight = getStatusBarHeight();
        int actionBarSize = getActionBarSize();

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setPadding(15, statusBarHeight + actionBarSize, 0, 4);

        findViewById(R.id.outermost_main_container).setPadding(0, statusBarHeight + actionBarSize + 30, 0, 0);
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

    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
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

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
