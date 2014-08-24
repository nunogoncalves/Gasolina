package com.numicago.android.gasolina.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.viewloaders.StationDetailsViewLoader;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.settings.ApplicationSettings;

public class StationDetailsActivity extends Activity {

	private Station station;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_details);
		
		Bundle bundle = getIntent().getExtras();
		station = (Station) bundle.getSerializable("station");
		
		setTitle(station.getName());
		new StationDetailsViewLoader(this, station);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ImageView stationDetailNavigateToIV = (ImageView) findViewById(R.id.stationDetailNavigateToIV);
		stationDetailNavigateToIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LatLng myLocation = ApplicationSettings.getGPSCoordinates();
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
					    Uri.parse("http://maps.google.com/maps?saddr="
					    		+ myLocation.latitude
					    		+ "," 
					    		+ myLocation.longitude
					    		+ "&daddr="
					    		+ station.getLatLongObject().latitude 
					    		+ ","
					    		+ station.getLatLongObject().longitude));
					startActivity(intent);
			}
		});
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
