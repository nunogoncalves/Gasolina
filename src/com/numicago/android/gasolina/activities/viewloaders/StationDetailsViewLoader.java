package com.numicago.android.gasolina.activities.viewloaders;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.utils.ImageLoader;

public class StationDetailsViewLoader {

	private Activity activity;
	private Station station;
	private String brandBaseImageUrl = "https://dl.dropboxusercontent.com/u/2001692/applications/Gasolina/";
	private GoogleMap googleMap;
	
	public StationDetailsViewLoader(Activity activity, Station station) {
		this.activity = activity;
		this.station = station;
		instantiateViews();
		fillViews();
	}

	ImageView stationDetailsBrandImage;
	TextView stationDetailsBrandNameTV;
	TextView stationDetailsNameTV;
	TextView stationDetailsDistanceTV;
	TextView stationItemUpdatedAtTV;
	TextView stationItemPrice98TV;
	TextView stationItemPrice95TV;
	TextView stationItemPriceDieselTV;
	TextView stationItemPriceGPLTV;
	TextView stationDetailsUpdatedTV;
	
	protected void instantiateViews() {
		stationDetailsBrandImage = (ImageView) activity.findViewById(R.id.stationDetailsBrandIV);
		stationDetailsNameTV = (TextView) activity.findViewById(R.id.stationDetailsNameTV);
		stationDetailsBrandNameTV = (TextView) activity.findViewById(R.id.stationDetailsBrandNameTV);
		stationItemPrice98TV = (TextView) activity.findViewById(R.id.stationDetails98TV);
		stationItemPrice95TV = (TextView) activity.findViewById(R.id.stationDetails95TV);
		stationItemPriceDieselTV = (TextView) activity.findViewById(R.id.stationDetailsDieselTV);
		stationItemPriceGPLTV = (TextView) activity.findViewById(R.id.stationDetailsGasTV);
		stationDetailsDistanceTV = (TextView) activity.findViewById(R.id.stationDetailsDistanceTV);
		stationItemUpdatedAtTV = (TextView) activity.findViewById(R.id.stationItemUpdatedAtTV);
		stationDetailsUpdatedTV = (TextView) activity.findViewById(R.id.stationDetailsUpdatedTV);
	}
	
	protected void fillViews() {
		new ImageLoader(activity).DisplayImage(
				brandBaseImageUrl + station.getFormattedBrandName() + ".png", 
				activity,
				stationDetailsBrandImage,
				true,
				station.getBrandName());
		
		stationDetailsBrandNameTV.setText(station.getBrandName());
		stationDetailsNameTV.setText(station.getName());
		stationDetailsDistanceTV.setText(station.getDistance());
		
		stationDetailsUpdatedTV.setText("Actualizado: " + station.getUpdatedAt());
		
		stationItemPrice98TV.setText("€ " + station.getEight_price() + "");
		stationItemPrice95TV.setText("€ " + station.getFive_price() + "");
		stationItemPriceDieselTV.setText("€ " + station.getDiesel_price() + "");
		stationItemPriceGPLTV.setText("€ " + station.getGpl_price() + "");
		
		setMaps();
	}
	
	private void setMaps() {
		googleMap = ((MapFragment) activity.getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		
		final LatLng pointer = station.getLatLongObject();
		MarkerOptions marker = new MarkerOptions();
		marker.position(pointer);
		marker.title(station.getName());
		marker.draggable(true);
		googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		
		googleMap.addMarker(marker);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointer, 16));
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				return false;
			}
		});
	}

}
