package com.numicago.android.gasolina.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.adapters.DrawerAdapter;
import com.numicago.android.gasolina.fragments.MapAndListFragment;

public class StationsListActivity extends Activity {

	private FragmentManager fragmentManager = getFragmentManager();
	private FragmentTransaction fragTransaction;
	private MapAndListFragment mapAndListFrag;
	
	private DrawerLayout drawerLayout;
	private LinearLayout drawerContainer;
	private ListView drawerListView;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		
		mapAndListFrag = new MapAndListFragment();

		// Passing selected item information to fragment
		Bundle data = new Bundle();
		mapAndListFrag.setArguments(data);
		
		// Creating a fragment transaction
		fragTransaction = fragmentManager.beginTransaction();

		// Adding a fragment to the fragment transaction
		fragTransaction.replace(R.id.content_frame, mapAndListFrag);

		// Committing the transaction
		fragTransaction.commit();
		
		setUpDrawer();
		
	}

	private void setUpDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerContainer = (LinearLayout) findViewById(R.id.drawerContainerLL);
		drawerListView = (ListView) findViewById(R.id.drawer_list);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.abc_ic_menu_moreoverflow_normal_holo_light, R.string.open,
				R.string.close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();

			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}

		};

		// Setting DrawerToggle on DrawerLayout
		drawerLayout.setDrawerListener(drawerToggle);

		drawerListView.setAdapter(new DrawerAdapter(this));

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Setting item click listener for the listview mDrawerList
		drawerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 2) {
					Intent i = new Intent(StationsListActivity.this, PreferencesActivity.class);
					startActivity(i);
				}
				
//				// Creating a fragment object
//				WebViewFragment rFragment = new WebViewFragment();
//
//				// Passing selected item information to fragment
//				Bundle data = new Bundle();
//				data.putInt("position", position);
//				rFragment.setArguments(data);
//				
//				
//				// Getting reference to the FragmentManager
//				FragmentManager fragmentManager = getFragmentManager();
//
//				// Creating a fragment transaction
//				FragmentTransaction ft = fragmentManager.beginTransaction();
//
//				// Adding a fragment to the fragment transaction
//				ft.replace(R.id.content_frame, rFragment);
//
//				// Committing the transaction
//				ft.commit();

				// Closing the drawer
				drawerLayout.closeDrawer(drawerContainer);

			}
		});
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		if(item.getItemId() == R.id.action_refresh_stations_list) {
			mapAndListFrag.listenToLocationUpdates();
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerContainer);
//
//		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_actions, menu);
		return true;
	}
}
