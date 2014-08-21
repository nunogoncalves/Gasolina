package com.numicago.android.gasolina.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.PreferencesActivity;

public class ApplicationSettings {
	
	public static final int MAPS_CIRCLE_COLOR = 0x550099FF;

	private static Context context;
	private static ApplicationSettings settings; 
	private static ConnectivityManager cm;
	private static LocationManager locationManager;
	private static Criteria criteria = new Criteria();
	private static Location lastKnownLocation;
	private static SharedPreferences sharedPref;
	
	private ApplicationSettings(Context context) {
		ApplicationSettings.context = context;
		cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static ApplicationSettings getInstance(Context _context) {
		if (settings == null) {
			settings = new ApplicationSettings(_context);
		}
		return settings;
	}
	
	public boolean isConnectedToInternet() {
		return cm.getActiveNetworkInfo() != null 
				&& cm.getActiveNetworkInfo().isAvailable() 
				&& cm.getActiveNetworkInfo().isConnected(); 
	}

	public boolean isGPSActive() {
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	public static LatLng getGPSCoordinates() {
		LatLng latLng;

	    String provider = locationManager.getBestProvider(criteria, false);
	    lastKnownLocation = locationManager.getLastKnownLocation(provider);

	    if (lastKnownLocation != null) {
	    	latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
	    } else {
//	    	latLng = new LatLng(39.8942694, -8.2760329); //Figueiro
	    	latLng = new LatLng(38.7640897, -9.0992928); //Oriente
	    }
		return latLng;
	}

	public static String getDistanceRadius() {
		String a = sharedPref.getString(
				PreferencesActivity.DISTANCE_RADIUS_KEY,  //selected by user
				context.getString(R.string.distanceArrayDefaultValue)); //default value
		a = a.replaceAll("[^0-9]+", " ");
		return a.trim();
	}
	
	public static String getFavouriteGasType() {
		return sharedPref.getString(
				PreferencesActivity.FAVOURITE_GAS_TYPE,  //selected by user
				context.getString(R.string.gasArrayDefaultValue)); //default value
	}
}
