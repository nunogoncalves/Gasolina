package com.numicago.android.gasolina.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.numicago.android.gasolina.R;

public class PreferencesActivity extends Activity {

	public static final String DISTANCE_RADIUS_KEY = "gasolinaNearMeDistanceRadius";
	public static final String DISTANCE_ORDER_KEY = "gasolinaOrderBy";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
	}
	
	
	public static class SettingsFragment extends PreferenceFragment {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.preferences);
	    }
	}
	
}
