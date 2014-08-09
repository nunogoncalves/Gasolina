package com.numicago.android.gasolina.adapters;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.utils.ImageLoader;

public class StationItemAdapter extends BaseAdapter {

	private List<Station> stations;
	private Activity activity;
	private LayoutInflater inflater;
	
	public StationItemAdapter(Activity activity, List<Station> stationsList) {
		stations = stationsList;
		this.activity = activity;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return stations.size();
	}

	@Override
	public Station getItem(int index) {
		return stations.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	ImageView stationItemBrandNameIV;
	TextView gasNameTV;
	TextView stationItemLatLngTV;
	TextView stationItembrandNameTV;
	TextView stationItemUpdatedAtTV;
	
	private void instantiateViews(View vi) {
		stationItemBrandNameIV = (ImageView) vi.findViewById(R.id.stationItemBrandNameIV);
		gasNameTV= (TextView)vi.findViewById(R.id.stationItemNameTV);
		stationItemLatLngTV= (TextView)vi.findViewById(R.id.stationItemDistanceTV);
		stationItembrandNameTV= (TextView)vi.findViewById(R.id.stationItembrandNameTV);
		stationItemUpdatedAtTV= (TextView)vi.findViewById(R.id.stationItemUpdatedAtTV);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		View vi = null;
		if (vi == null) {
			vi = inflater.inflate(R.layout.station_list_item, null);
		}
		
		instantiateViews(vi);
		fillViews(getItem(index));
		
		return vi;
	}
	
	private static final HashMap<String, Integer> namesHash;
    static
    {
    	namesHash = new HashMap<String, Integer>();
    	namesHash.put("galp", R.drawable.galp);
    	namesHash.put("cepsa", R.drawable.cepsa);
    	namesHash.put("bp", R.drawable.bp);
    	namesHash.put("intermarche", R.drawable.intermarche);
    	namesHash.put("jumbo", R.drawable.jumbo);
    	namesHash.put("rede_energia", R.drawable.rede_energia);
    	namesHash.put("repsol", R.drawable.repsol);
    }
	
	private void fillViews(Station station) {
		String brandName = station.getFormattedBrandName();
		if(brandName != null && brandName.length() > 0) {
			Integer drawable = namesHash.get(brandName);
			if (drawable == null) {
				drawable = R.drawable.station_placeholder;
			}
			
			stationItemBrandNameIV.setImageDrawable(activity.getResources().getDrawable(drawable));
		}
		
		gasNameTV.setText(station.getName());
		stationItemLatLngTV.setText(station.getDistance());
		stationItembrandNameTV.setText(station.getBrandName());
		stationItemUpdatedAtTV.setText(station.getUpdatedAt());
	}
	
}
