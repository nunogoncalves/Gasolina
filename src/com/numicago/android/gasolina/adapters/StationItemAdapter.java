package com.numicago.android.gasolina.adapters;

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
import com.numicago.android.gasolina.enums.EnumStationBrand;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.settings.ApplicationSettings;

public class StationItemAdapter extends BaseAdapter {

	private List<Station> stations;
	private Activity activity;
	private LayoutInflater inflater;
	private String favouriteGas;
	private String noFavouriteGas;
	
	public StationItemAdapter(Activity activity, List<Station> stationsList) {
		stations = stationsList;
		this.activity = activity;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		favouriteGas = ApplicationSettings.getFavouriteGasType();
		noFavouriteGas = activity.getString(R.string.gasArrayDefaultValue);
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
	TextView stationListItemPriceTV;
	
	private void instantiateViews(View vi) {
		stationItemBrandNameIV = (ImageView) vi.findViewById(R.id.stationItemBrandNameIV);
		gasNameTV = (TextView)vi.findViewById(R.id.stationItemNameTV);
		stationItemLatLngTV = (TextView)vi.findViewById(R.id.stationItemDistanceTV);
		stationItembrandNameTV = (TextView)vi.findViewById(R.id.stationItembrandNameTV);
		stationItemUpdatedAtTV = (TextView)vi.findViewById(R.id.stationItemUpdatedAtTV);
		stationListItemPriceTV = (TextView)vi.findViewById(R.id.stationListItemPrice);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (vi == null) {
			vi = inflater.inflate(R.layout.station_list_item, null);
		}
		
		instantiateViews(vi);
		fillViews(getItem(index));
		
		return vi;
	}
	
	private void fillViews(Station station) {
		Integer drawable = EnumStationBrand.getIconDrawableFromApiName(station.getBrandName());
			
		stationItemBrandNameIV.setImageDrawable(activity.getResources().getDrawable(drawable));
		
		gasNameTV.setText(station.getName());
		stationItemLatLngTV.setText(station.getDistance());
		stationItembrandNameTV.setText(station.getBrandName());
		stationItemUpdatedAtTV.setText(station.getUpdatedAt());
		
		if(!noFavouriteGas.equals(favouriteGas)) {
			stationListItemPriceTV.setVisibility(View.VISIBLE);
			String pack = activity.getPackageName();
			String id = favouriteGas;
			int resId = activity.getResources().getIdentifier(id, "string", pack);
			stationListItemPriceTV.setText("Preço " + activity.getString(resId) + " €" + station.getGasPrice(favouriteGas));
		}
	}
	
}
