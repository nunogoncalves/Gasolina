package com.numicago.android.gasolina.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.StationDetailsActivity;
import com.numicago.android.gasolina.activities.viewloaders.IUIFinishDelegator;
import com.numicago.android.gasolina.adapters.StationItemAdapter;
import com.numicago.android.gasolina.dataloaders.StationsApiLoader;
import com.numicago.android.gasolina.enums.EnumStationBrand;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.settings.ApplicationSettings;

public class MapAndListFragment extends Fragment implements IUIFinishDelegator, View.OnClickListener {

	private View fragmentView;
	private MapFragment mapFragment;
	private GoogleMap googleMap;
	private ListView lv;
	private ImageView gpsGifImage;
	private LinearLayout gpsGifImageViewLayout;
	private LinearLayout stationsListFragmentsContainer;
	private List<Station> stations;
	
	private LocationManager locationManager;
	
	private boolean listeningToGps;
	private ApplicationSettings appSettings;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.map_and_list_fragment, container, false);
		appSettings = ApplicationSettings.getInstance(getActivity());
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		instantiateViews();
		fillViews();
		buildLocationListener();
		listenToLocationUpdates();
		return fragmentView;
	}
	
	private void instantiateViews() {
		lv = (ListView) fragmentView.findViewById(R.id.stationsMainList);
		initializeMap();
		gpsGifImageViewLayout = (LinearLayout) fragmentView.findViewById(R.id.stationsListGPSLookupLL);
		stationsListFragmentsContainer = (LinearLayout) fragmentView.findViewById(R.id.stationsListFragmentsContainer);
		
		gpsGifImage = (ImageView) fragmentView.findViewById(R.id.stationsListGpsAnimationImage);
		
		fragmentView.findViewById(R.id.stationsListListButtonLL).setOnClickListener(this);
		fragmentView.findViewById(R.id.stationsListMapButtonLL).setOnClickListener(this);
	}
	
	protected void fillViews() {
		gpsGifImage.setBackgroundResource(R.anim.gps_animation);
		AnimationDrawable frameAnimation = (AnimationDrawable) gpsGifImage.getBackground();
		frameAnimation.start();
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
		mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.stationsListMap);
		
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
						Intent detailsIntent = new Intent(getActivity(), StationDetailsActivity.class);
						detailsIntent.putExtras(b);
						startActivity(detailsIntent);
					}
				}
	        	marker.getTitle();
	        }
		});
	}

	public void listenToLocationUpdates() {
		if (!listeningToGps) {
			listeningToGps = true;
			showGpsListeningSignal();
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}
	}
	
	private LocationListener locationListener;
	
	private void buildLocationListener() {
		lv.setVisibility(View.GONE);
		locationListener = new LocationListener() {
			public void onLocationChanged(Location loc) {
				if (appSettings.isConnectedToInternet()) {
					new StationsApiLoader(MapAndListFragment.this).fetchStations();
					locationManager.removeUpdates(this);
				}
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}
		};
	}
	
	public void showStationsListOrMap() {
		stationsListFragmentsContainer.setVisibility(View.VISIBLE);
		gpsGifImageViewLayout.setVisibility(View.GONE);
	}

	public void showGpsListeningSignal() {
		stationsListFragmentsContainer.setVisibility(View.GONE);
		gpsGifImageViewLayout.setVisibility(View.VISIBLE);
		gpsGifImageViewLayout.setGravity(Gravity.CENTER_VERTICAL);
	}

	public void loadStationsList(List<Station> stations) {
		this.stations = stations;
		lv.setAdapter(new StationItemAdapter(getActivity(), stations));
		populateMapWithStations(stations);
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
		
		CircleOptions co = new CircleOptions();
		co.center(pointer)
		  .fillColor(ApplicationSettings.MAPS_CIRCLE_COLOR)
		  .strokeColor(Color.BLUE)
		  .strokeWidth(1)
		  .radius(Integer.parseInt(ApplicationSettings.getDistanceRadius()) * 1000);
		googleMap.addCircle(co);
	}
	
	@SuppressWarnings("unchecked")
	public void dataReadyToUse(List<?> stations) {
		listeningToGps = false;
		showStationsListOrMap();
		this.stations = (ArrayList<Station>) stations;
		if(stations.size() == 0){
			Toast.makeText(getActivity(), "ZERO STATIOPNS", Toast.LENGTH_LONG).show();
		}
		loadStationsList(this.stations);
	}
	
	private void hideView(View view) {
		view.setVisibility(View.GONE);
	}
	
	private void showView(View view) {
		view.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		// Check which radio button was clicked
	    switch(v.getId()) {
	        case R.id.stationsListListButtonLL:
	            	hideView(lv);
	            	showView(mapFragment.getView());
	            	fragmentView.findViewById(R.id.stationsListListLineView).setBackgroundColor(Color.parseColor("#3399FF"));
	            	fragmentView.findViewById(R.id.stationsListMapLineView).setBackgroundColor(Color.WHITE);
	            break;
	        case R.id.stationsListMapButtonLL:
	            	showView(lv);
	            	hideView(mapFragment.getView());
	            	fragmentView.findViewById(R.id.stationsListListLineView).setBackgroundColor(Color.WHITE);
	            	fragmentView.findViewById(R.id.stationsListMapLineView).setBackgroundColor(Color.parseColor("#3399FF"));
	            break;
	    }
	}
}