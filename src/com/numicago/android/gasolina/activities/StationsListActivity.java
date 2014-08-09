package com.numicago.android.gasolina.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.viewloaders.IUIFinishDelegator;
import com.numicago.android.gasolina.activities.viewloaders.StationsListViewLoader;
import com.numicago.android.gasolina.dataloaders.StationsApiLoader;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.settings.ApplicationSettings;

public class StationsListActivity extends ActionBarActivity implements IUIFinishDelegator {

	private StationsListActivity thisActivity;
	private ApplicationSettings appSettings;
	private StationsListViewLoader homeViewLoader;
	
	private LocationManager locationManager;
	private List<Station> stations;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity = StationsListActivity.this;
		setContentView(R.layout.activity_home);
		
		appSettings = ApplicationSettings.getInstance(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		homeViewLoader = new StationsListViewLoader(this);
		buildLocationListener();
		listenToLocationUpdates();
		homeViewLoader.getListView().setOnItemClickListener(viewStationDetails());
		
	}

	private OnItemClickListener viewStationDetails() {
		OnItemClickListener listener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
				Toast.makeText(thisActivity, "clicked", Toast.LENGTH_LONG).show();
				Station station = (Station) parent.getAdapter().getItem(position);
				Bundle b = new Bundle();
				b.putSerializable("station", station);
				Intent detailsIntent = new Intent(thisActivity, StationDetailsActivity.class);
				detailsIntent.putExtras(b);
				thisActivity.startActivity(detailsIntent);
			}
			
		};
		return listener;
	}

	@SuppressWarnings("unchecked")
	public void dataReadyToUse(List<?> stations) {
		homeViewLoader.toggleVisibility();
		this.stations = (ArrayList<Station>) stations;
		if(stations.size() == 0){
			Toast.makeText(thisActivity, "ZERO STATIOPNS", Toast.LENGTH_LONG).show();
		}
		homeViewLoader.loadStationsList(this.stations);
	}

	public void listenToLocationUpdates() {
		homeViewLoader.toggleVisibility();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}
	
	private LocationListener locationListener;
	
	private void buildLocationListener() {
		homeViewLoader.getListView().setVisibility(View.GONE);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location loc) {
				if (appSettings.isConnectedToInternet()) {
					new StationsApiLoader(thisActivity).fetchStations();
					locationManager.removeUpdates(this);
				}
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}
		};
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent preSettingsIntent = new Intent(thisActivity, PreferencesActivity.class);
			startActivity(preSettingsIntent);
			return super.onOptionsItemSelected(item);
		case R.id.action_refresh_stations_list: 
			listenToLocationUpdates();
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
}