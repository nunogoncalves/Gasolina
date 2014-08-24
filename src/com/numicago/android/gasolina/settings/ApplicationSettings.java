package com.numicago.android.gasolina.settings;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.App;
import com.numicago.android.gasolina.activities.PreferencesActivity;

public class ApplicationSettings {
	
	public static final int MAPS_CIRCLE_COLOR = 0x550099FF;
	private static Context context;
	private static ApplicationSettings settings; 
	private static ConnectivityManager cm;
	private static LocationManager locationManager;
	private static SharedPreferences sharedPref;
	
	private ApplicationSettings() {
		ApplicationSettings.context = App.getContext();
		cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static ApplicationSettings getInstance() {
		if (settings == null) {
			settings = new ApplicationSettings();
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
	    List<String> providers = locationManager.getProviders(true);

	    Location location;
	    for (int i = providers.size()-1; i >= 0; i--) {
           location = locationManager.getLastKnownLocation(providers.get(i));
            if (location != null) {
                return new LatLng(location.getLatitude(), location.getLongitude()); //Figueiro
            }
	    }
	    
//		LatLng(39.8942694, -8.2760329); //Figueiro
//	    LatLng(38.7640897, -9.0992928); //Oriente
		return new LatLng(38.7640897, -9.0992928);
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
				PreferencesActivity.FAVOURITE_GAS_TYPE_KEY,  //selected by user
				context.getString(R.string.gasArrayDefaultValue)); //default value
	}
	
	public static String getMapStyle() {
		return sharedPref.getString(
				PreferencesActivity.MAP_STYLE_KEY,  //selected by user
				context.getString(R.string.mapStyleArrayDefaultValue)); //default value
	}
}
