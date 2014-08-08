package com.numicago.android.gasolina.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.numicago.android.gasolina.activities.viewloaders.StationListItemViewLoader;
import com.numicago.android.gasolina.objects.Station;

public class StationItemAdapter extends BaseAdapter {

	private List<Station> stations;
	private Activity activity;
	private StationListItemViewLoader itemLoader;
	
	public StationItemAdapter(Activity activity, List<Station> stationsList) {
		stations = stationsList;
		this.activity = activity;
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

	@Override
	public View getView(int index, View listItemView, ViewGroup arg2) {
		itemLoader = new StationListItemViewLoader(activity, getItem(index));
        return itemLoader.getItemView();
	}
	
}
