package com.numicago.android.gasolina.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.bradcast_receivers.InternetBroadcastReceiver;
import com.numicago.android.gasolina.interfaces.OnInternetConnected;
import com.numicago.android.gasolina.settings.ApplicationSettings;

public class EntranceActivity extends Activity implements OnInternetConnected {
	
	ApplicationSettings appSettings;
	private InternetBroadcastReceiver internetReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appSettings = ApplicationSettings.getInstance();
		
		if (appSettings.isConnectedToInternet()) {
			goToStationsActivity();
		} else {
			setContentView(R.layout.entrance);
			ImageView image = (ImageView) findViewById(R.id.entranteWifiIV);
	        Animation animPulse = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.pulse);
	        image.startAnimation(animPulse);
	        
	        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
	        internetReceiver = new InternetBroadcastReceiver();
	        registerReceiver(internetReceiver, filter);
		}
	}

	private void goToStationsActivity() {
		Intent stationsListActivity = new Intent(this, StationsListActivity.class);
		startActivity(stationsListActivity);
	}
	
	@Override
	public void internetAvailable() {
		unregisterReceiver(internetReceiver);
		goToStationsActivity();
	}
	    
}
