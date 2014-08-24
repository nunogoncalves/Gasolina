package com.numicago.android.gasolina.activities;

import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.numicago.android.gasolina.R;

public class PreferencesActivity extends Activity {

	public static final String DISTANCE_RADIUS_KEY = "gasolinaNearMeDistanceRadius";
	public static final String DISTANCE_ORDER_KEY = "gasolinaOrderBy";
	public static final String FAVOURITE_GAS_TYPE_KEY = "gasolinaFavouriteGasType";
	public static final String MAP_STYLE_KEY = "gasolinaMapStyle";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
	}
	
	public static class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	    
		private SharedPreferences prefs;
		private Activity activity;
		private String packName;
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        addPreferencesFromResource(R.xml.preferences);
	        activity = getActivity();
	        packName = activity.getPackageName();
			prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
	        prefs.registerOnSharedPreferenceChangeListener(this);
	        
	        Map<String,?> keys = prefs.getAll();

			for(Map.Entry<String,?> entry : keys.entrySet()) {
				setKeySummary(entry.getKey());            
			 }
	    }

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (prefs.contains(key)) {
				setKeySummary(key);
			}
		}

		private void setKeySummary(String key) {
			String id = prefs.getString(key, "");
			int resId = activity.getResources().getIdentifier(id, "string", packName);
			Preference pref = findPreference(key);
			if (resId != 0) {
				pref.setSummary(activity.getString(resId));
			}
		}
	}
}
