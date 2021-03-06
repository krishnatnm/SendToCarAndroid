package com.jvanier.android.sendtocar.controllers;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jvanier.android.sendtocar.R;
import com.jvanier.android.sendtocar.common.Log;
import com.jvanier.android.sendtocar.controllers.commands.Command;
import com.jvanier.android.sendtocar.controllers.commands.SendDebugLogToDeveloper;
import com.jvanier.android.sendtocar.controllers.commands.ShowAppInGooglePlay;
import com.jvanier.android.sendtocar.controllers.commands.ShowHelp;
import com.jvanier.android.sendtocar.controllers.commands.ShowTutorial;
import com.jvanier.android.sendtocar.controllers.commands.ToggleDebugMode;
import com.jvanier.android.sendtocar.controllers.commands.WriteEmailToDeveloper;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	final private class NavigationItem {
		public final int stringId;
		public final int drawableId;
		public final Command handler;
		public final Command longPressHandler;

		public NavigationItem(int stringId, int drawableId, Command handler) {
			this.stringId = stringId;
			this.drawableId = drawableId;
			this.handler = handler;
			this.longPressHandler = null;
		}
		public NavigationItem(int stringId, int drawableId, Command handler, Command longPressHandler) {
			this.stringId = stringId;
			this.drawableId = drawableId;
			this.handler = handler;
			this.longPressHandler = longPressHandler;
		}
	}

	/**
	 * Per the design guidelines, you should show the drawer on launch once.
	 * This shared preference tracks this.
	 */
	private static final String PREF_USER_SHOWN_DRAWER = "navigation_drawer_shown";

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;
	private View mFragmentContainerView;

	private boolean mFromSavedInstanceState;
	private boolean mUserShownDrawer;

	private ArrayList<NavigationItem> itemsList;

	private ArrayAdapter<NavigationItem> mAdapter;

	public NavigationDrawerFragment() {
	}
	
	private void setupItems() {
		itemsList = new ArrayList<>();

		itemsList.add(new NavigationItem(R.string.listTutorialTitle, R.drawable.ic_navigation_lightbulb, new ShowTutorial()));
		itemsList.add(new NavigationItem(R.string.listHelpTitle, R.drawable.ic_navigation_help, new ShowHelp()));
		itemsList.add(new NavigationItem(R.string.listRateTitle, R.drawable.ic_navigation_star, new ShowAppInGooglePlay()));
		itemsList.add(new NavigationItem(R.string.listEmailTitle, R.drawable.ic_navigation_email, new WriteEmailToDeveloper(), new ToggleDebugMode()));

		if(Log.hasLogFile()) {
			itemsList.add(new NavigationItem(R.string.listSendLog, R.drawable.ic_navigation_email, new SendDebugLogToDeveloper(), new ToggleDebugMode()));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserShownDrawer = sp.getBoolean(PREF_USER_SHOWN_DRAWER, false);

		if(savedInstanceState != null) {
			mFromSavedInstanceState = true;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mDrawerListView = (ListView) inflater.inflate(R.layout.navigation_drawer_fragment, container, false);
		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
			}
		});
		mDrawerListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				return longPressItem(position);
			}
		});

		setupItems();

		mAdapter = new ArrayAdapter<NavigationItem>(getActionBar().getThemedContext(), R.layout.navigation_item,
				android.R.id.text1, itemsList) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);

				NavigationItem item = getItem(position);
				TextView tv = (TextView) v.findViewById(android.R.id.text1);
				tv.setText(item.stringId);
				tv.setCompoundDrawablesWithIntrinsicBounds(item.drawableId, 0, 0, 0);

				return v;
			}
		};
		
		mDrawerListView.setAdapter(mAdapter);

		return mDrawerListView;
	}
	
	private void updateAdapter() {
		mAdapter.clear();
		for(NavigationItem item : itemsList) {
			mAdapter.add(item);
		}
		mAdapter.notifyDataSetChanged();
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 *
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(int fragmentId, DrawerLayout drawerLayout) {
		mFragmentContainerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		);

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if(!mUserShownDrawer && !mFromSavedInstanceState) {
			mDrawerLayout.openDrawer(mFragmentContainerView);

			// The user manually opened the drawer; store this flag to prevent
			// auto-showing
			// the navigation drawer automatically in the future.
			mUserShownDrawer = true;
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
			sp.edit().putBoolean(PREF_USER_SHOWN_DRAWER, true).commit();
		}

		// Defer code dependent on restoration of previous instance state.
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Let the drawer toggle component handle the hamburger button
		if(mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) getActivity()).getSupportActionBar();
	}

	public void handleMenuKey() {
		if(mDrawerLayout.isDrawerOpen(mFragmentContainerView)) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		} else {
			mDrawerLayout.openDrawer(mFragmentContainerView);
		}
	}

	protected void selectItem(int position) {
		if(mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(mFragmentContainerView);
		}
		Command handler = itemsList.get(position).handler;
		if(handler != null) {
			handler.perfrom(getActivity());
		}
	}

	protected boolean longPressItem(int position) {
		Command handler = itemsList.get(position).longPressHandler;
		if(handler != null) {
			handler.perfrom(getActivity());
			
			// Hack to refresh the menu. We know that it might change in a handler
			setupItems();
			updateAdapter();
			
			return true;
		} else {
			return false;
		}
	}
}
