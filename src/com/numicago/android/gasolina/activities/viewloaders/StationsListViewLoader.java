package com.numicago.android.gasolina.activities.viewloaders;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.StationDetailsActivity;
import com.numicago.android.gasolina.adapters.StationItemAdapter;
import com.numicago.android.gasolina.enums.EnumStationBrand;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.settings.ApplicationSettings;

public class StationsListViewLoader extends ViewLoader {
	
	private ListView lv;
	private ImageView gpsGifImage;
	private LinearLayout gpsGifImageViewLayout;
	private LinearLayout stationsListFragmentsContainer;
	private GoogleMap googleMap;
	private MapFragment mapFragment;
	private List<Station> stations;

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
		this.stations = stations;
		lv.setAdapter(new StationItemAdapter(activity, stations));
		populateMapWithStations(stations);
	}

	private static final HashMap<String, Integer> radiousVsZoom;
    static
    {
    	radiousVsZoom = new HashMap<String, Integer>();
    	radiousVsZoom.put("1", 14);
    	radiousVsZoom.put("5", 12);
    	radiousVsZoom.put("10", 11);
    	radiousVsZoom.put("15", 11);
    	radiousVsZoom.put("20", 10);
    	radiousVsZoom.put("50", 9);
    }
	
	private void initializeMap() {
		mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.stationsListMap);
		
		googleMap = mapFragment.getMap();
		
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.setMyLocationEnabled(true);
		LatLng pointer = ApplicationSettings.getGPSCoordinates();
		
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				pointer, radiousVsZoom.get(ApplicationSettings.getDistanceRadius())));
		
		googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

	        @Override
	        public void onInfoWindowClick(Marker marker) {
	        	for (Station station : stations) {
					if (station.getName().equals(marker.getTitle())) {
						Bundle b = new Bundle();
						b.putSerializable("station", station);
						Intent detailsIntent = new Intent(activity, StationDetailsActivity.class);
						detailsIntent.putExtras(b);
						activity.startActivity(detailsIntent);
					}
				}
	        	marker.getTitle();
	        }
		});
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
		LatLng pointer = ApplicationSettings.getGPSCoordinates();
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
				pointer, radiousVsZoom.get(ApplicationSettings.getDistanceRadius())));
		for (int i = 0; i < stations.size(); i++) {
			Station station = stations.get(i);
			MarkerOptions marker = new MarkerOptions();
			marker.icon(BitmapDescriptorFactory.fromResource(
					EnumStationBrand.getMarkerDrawableFromApiName(station.getBrandName())));
			marker.position(station.getLatLongObject());
			marker.title(station.getName());
			
			googleMap.addMarker(marker);
			
			
		}
	}
	
	public void onViewTypeClicked(View view) {
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.stationsListListButtonLL:
	            	hideView(lv);
	            	showView(mapFragment.getView());
	            	activity.findViewById(R.id.stationsListListLineView).setBackgroundColor(Color.parseColor("#3399FF"));
	            	activity.findViewById(R.id.stationsListMapLineView).setBackgroundColor(Color.WHITE);
	            break;
	        case R.id.stationsListMapButtonLL:
	            	showView(lv);
	            	hideView(mapFragment.getView());
	            	activity.findViewById(R.id.stationsListListLineView).setBackgroundColor(Color.WHITE);
	            	activity.findViewById(R.id.stationsListMapLineView).setBackgroundColor(Color.parseColor("#3399FF"));
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
