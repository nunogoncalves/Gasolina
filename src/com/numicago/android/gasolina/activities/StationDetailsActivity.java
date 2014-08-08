package com.numicago.android.gasolina.activities;

import android.app.Activity;
import android.os.Bundle;

import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.viewloaders.StationDetailsViewLoader;
import com.numicago.android.gasolina.objects.Station;

public class StationDetailsActivity extends Activity {

	private Station station;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.station_details);
		
		Bundle bundle = getIntent().getExtras();
		station = (Station) bundle.getSerializable("station");
		
		new StationDetailsViewLoader(this, station);
		
	}
}
