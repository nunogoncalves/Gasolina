package com.numicago.android.gasolina.activities.viewloaders;

import java.util.List;

import android.app.Activity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.adapters.StationItemAdapter;
import com.numicago.android.gasolina.objects.Station;

public class StationsListViewLoader extends ViewLoader {

	private ListView lv;
	private LinearLayout gpsGifImageViewLayout; 

	public StationsListViewLoader(Activity activity) {
		super(activity);
		lv = (ListView) activity.findViewById(R.id.stationsMainList);
	}

	@Override
	protected void instantiateViews() {
		gpsGifImageViewLayout = (LinearLayout) activity.findViewById(R.id.stationsListGPSLookupLL);
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
	
	
	public void toggleVisibility() {
		if (!isVisible(gpsGifImageViewLayout)) {
			showView(gpsGifImageViewLayout);
			hideView(lv);
			((LinearLayout) gpsGifImageViewLayout.getParent()).setGravity(Gravity.CENTER_VERTICAL);
		} else {
			hideView(gpsGifImageViewLayout);
			showView(lv);
			((LinearLayout) gpsGifImageViewLayout.getParent()).setGravity(Gravity.TOP);
		}
	}

}
