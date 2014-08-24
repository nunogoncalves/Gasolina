package com.numicago.android.gasolina.activities;

import java.util.List;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.viewloaders.IUIFinishDelegator;
import com.numicago.android.gasolina.adapters.DrawerAdapter;
import com.numicago.android.gasolina.dataloaders.StationsApiLoader;
import com.numicago.android.gasolina.fragments.MapAndListFragment;

public class StationsListActivity extends ActionBarActivity implements IUIFinishDelegator {

	public MenuItem refreshMenuItem;

	private FragmentManager fragmentManager = getFragmentManager();
	private FragmentTransaction fragTransaction;
	private MapAndListFragment mapAndListFrag;
	
	private DrawerLayout drawerLayout;
	private LinearLayout drawerContainer;
	private ListView drawerListView;
	private ActionBarDrawerToggle drawerToggle;
	private StationsListActivity thisActivity;
	
	private EditText searchText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_home);
		
		thisActivity = StationsListActivity.this;
		
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
				R.drawable.ic_drawer, R.string.open,
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

		drawerLayout.setDrawerListener(drawerToggle);

		drawerListView.setAdapter(new DrawerAdapter(this));

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("");

		searchText = (EditText) findViewById(R.id.drawerSearchET);
		
		Button searchButton = (Button) findViewById(R.id.drawerSearchButton);
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean geocoderOk = new StationsApiLoader(thisActivity)
					.searchStationsAroundLocation(
							searchText.getText().toString() + ", Portugal");
				if (geocoderOk) {
					drawerLayout.closeDrawer(drawerContainer);
					refreshMenuItem.setActionView(R.layout.action_profress);
					refreshMenuItem.expandActionView();
					getActionBar().setTitle(searchText.getText().toString());
				} else {
					Toast.makeText(thisActivity, "Há um problema a obter a localização", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		// Setting item click listener for the listview mDrawerList
		drawerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 2) {
					Intent i = new Intent(StationsListActivity.this, PreferencesActivity.class);
					startActivity(i);
				}
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
		
		switch (item.getItemId()) {
//		case R.id.action_search_stations_list:
//			openSearchModal();
//			break;
		case R.id.action_refresh_stations_list:
			getActionBar().setTitle("");
			refreshMenuItem.setActionView(R.layout.action_profress);
			refreshMenuItem.expandActionView();
			mapAndListFrag.listenToLocationUpdates();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerContainer);
//
		refreshMenuItem.setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.main_activity_actions, menu);
	    refreshMenuItem = menu.findItem(R.id.action_refresh_stations_list);
	    refreshMenuItem.setVisible(false);
	    return super.onCreateOptionsMenu(menu);
	}

	public void dataReadyToUse(List<?> stations, LatLng point) {
		mapAndListFrag.dataReadyToUse(stations, point);
		refreshMenuItem.setVisible(true);
		refreshMenuItem.collapseActionView();
		refreshMenuItem.setActionView(null);
	}
	
//	public void openSearchModal() {
//		final Dialog dialog = new Dialog(thisActivity);
//		dialog.setTitle("Indique o local que deseja procurar");
//		dialog.setContentView(R.layout.search_modal);
//		dialog.findViewById(R.id.searchDialogOkButton).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String location = ((EditText) dialog
//						.findViewById(R.id.searchModalLocationET)).getText().toString();
//				new StationsApiLoader(thisActivity).searchStationsAroundLocation(location + ", Portugal");
//				dialog.dismiss();
//			}
//		});
//		
//		dialog.findViewById(R.id.searchDialogCancelButton).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		dialog.show();
//	}

}
