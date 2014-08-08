package com.numicago.android.gasolina.activities.viewloaders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.utils.ImageLoader;

public class StationListItemViewLoader {

	private Activity activity;
	private View itemView;
	private LayoutInflater inflater;
	private Station station;
	
	public StationListItemViewLoader(
			Activity activity,
			Station station) {
		this.activity = activity;
		this.station = station;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		instantiateViews();
		fillViews();
	}
	
	@SuppressLint("InflateParams")
	public View getItemView() {
		if(itemView == null) {
        	itemView = inflater.inflate(R.layout.station_list_item, null);
    	}
		return itemView;
	}

	ImageView stationItemBrandNameIV;
	TextView gasNameTV;
	TextView stationItemLatLngTV;
	TextView stationItembrandNameTV;
	TextView stationItemUpdatedAtTV;
	
	protected void instantiateViews() {
		
		View vi = getItemView();
		
		stationItemBrandNameIV = (ImageView) vi.findViewById(R.id.stationItemBrandNameIV);
		gasNameTV= (TextView)vi.findViewById(R.id.stationItemNameTV);
		stationItemLatLngTV= (TextView)vi.findViewById(R.id.stationItemDistanceTV);
		stationItembrandNameTV= (TextView)vi.findViewById(R.id.stationItembrandNameTV);
		stationItemUpdatedAtTV= (TextView)vi.findViewById(R.id.stationItemUpdatedAtTV);
	}

	private String brandBaseImageUrl = "https://dl.dropboxusercontent.com/u/2001692/applications/Gasolina/";
	
	protected void fillViews() {
		String brandName = station.getFormattedBrandName();
		if(brandName != null && brandName.length() > 0) {
			new ImageLoader(activity).DisplayImage(brandBaseImageUrl + brandName + ".png", 
					activity, stationItemBrandNameIV, true, brandName);
		}
		
		gasNameTV.setText(station.getName());
        
		stationItemLatLngTV.setText(station.getDistance());
		
		stationItembrandNameTV.setText(station.getBrandName());

		stationItemUpdatedAtTV.setText(station.getUpdatedAt());
	}

}
