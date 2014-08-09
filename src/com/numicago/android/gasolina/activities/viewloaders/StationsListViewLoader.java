package com.numicago.android.gasolina.activities.viewloaders;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.adapters.StationItemAdapter;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.settings.ApplicationSettings;

public class StationsListViewLoader extends ViewLoader {
	
	private ListView lv;
	private ImageView gpsGifImage;
	private LinearLayout gpsGifImageViewLayout;
	private LinearLayout stationsListFragmentsContainer;
	private GoogleMap googleMap;
	private MapFragment mapFragment;

	public StationsListViewLoader(Activity activity) {
		super(activity);
	}

	@Override
	protected void instantiateViews() {
		lv = (ListView) activity.findViewById(R.id.stationsMainList);
		initializeMap();
		gpsGifImageViewLayout = (LinearLayout) activity.findViewById(R.id.stationsListGPSLookupLL);
		stationsListFragmentsContainer = (LinearLayout) activity.findViewById(R.id.stationsListFragmentsContainer);
		gpsGifImage = (ImageView) activity.findViewById(R.id.stationsListGpsAnimationImage);
	}

	@Override
	protected void fillViews() {
		gpsGifImage.setBackgroundResource(R.anim.gps_animation);
		AnimationDrawable frameAnimation = (AnimationDrawable) gpsGifImage.getBackground();
		frameAnimation.start();
	}

	public void loadStationsList(List<Station> stations) {
		lv.setAdapter(new StationItemAdapter(activity, stations));
		populateMapWithStations(stations);
	}

	private void initializeMap() {
		mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.stationsListMap);
		
		googleMap = mapFragment.getMap();
		
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		LatLng pointer = ApplicationSettings.getGPSCoordinates();
		MarkerOptions marker = new MarkerOptions();
		marker.position(pointer);
		marker.icon(
				BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointer, 16));
		googleMap.addMarker(marker);
	}
	
	public ListView getListView() {
		return lv;
	}
	
	public void toggleMapVsList() {
		if (isVisible(lv)) {
			showView(mapFragment.getView());
			hideView(lv);
		} else {
			hideView(mapFragment.getView());
			showView(lv);
		}
	}
	
	public void populateMapWithStations(List<Station> stations) {
		googleMap.clear();
		for(Station station : stations) {
			MarkerOptions marker = new MarkerOptions();
			marker.position(station.getLatLongObject());
			marker.title(station.getName());
			
			googleMap.addMarker(marker);
		}
		MarkerOptions marker = new MarkerOptions();
		marker.position(ApplicationSettings.getGPSCoordinates());
		marker.title("EU");
		marker.icon(
				BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		googleMap.addMarker(marker);
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.mainMapRadioButton:
	            if (checked) {
	            	hideView(lv);
	            	showView(mapFragment.getView());
	            }
	            break;
	        case R.id.mainListRadioButton:
	            if (checked) {
	            	showView(lv);
	            	hideView(mapFragment.getView());
	            }
	            break;
	    }
	}
	
	public void showStationsListOrMap() {
		showView(stationsListFragmentsContainer);
		hideView(gpsGifImageViewLayout);
	}

	public void showGpsListeningSignal() {
		hideView(stationsListFragmentsContainer);
		showView(gpsGifImageViewLayout);
		gpsGifImageViewLayout.setGravity(Gravity.CENTER_VERTICAL);
	}

}
