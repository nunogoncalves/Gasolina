package com.numicago.android.gasolina.activities.viewloaders;

import java.util.List;

import android.app.Activity;
import android.widget.ListView;

import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.adapters.StationItemAdapter;
import com.numicago.android.gasolina.objects.Station;

public class StationsListViewLoader extends ViewLoader {

	ListView lv;
	StationItemAdapter adapter;

	public StationsListViewLoader(Activity activity) {
		super(activity);
		lv = (ListView) activity.findViewById(R.id.stationsMainList);
	}

	@Override
	protected void instantiateViews() {
	}

	@Override
	protected void fillViews() {
	}

	public void loadStationsList(List<Station> stations) {
		lv.setAdapter(new StationItemAdapter(activity, stations));
	}

	public ListView getListView() {
		return lv;
	}

}
