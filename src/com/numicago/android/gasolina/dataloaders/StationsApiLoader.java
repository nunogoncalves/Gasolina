package com.numicago.android.gasolina.dataloaders;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.numicago.android.gasolina.R;
import com.numicago.android.gasolina.activities.App;
import com.numicago.android.gasolina.activities.viewloaders.IUIFinishDelegator;
import com.numicago.android.gasolina.objects.Station;
import com.numicago.android.gasolina.settings.ApplicationSettings;
import com.numicago.android.gasolina.utils.Utils;

public class StationsApiLoader extends AsyncTask<String, Void, List<Station>> {

	private double lat = 0.0;
	private double lng = 0.0;
	private String baseWebUrl;
	private String locationUrl;
	private String requestFormat = "&format=json";
	private DecimalFormat latLongFormat = new DecimalFormat("#.######");
		
	IUIFinishDelegator uiDelegator;
	
	public StationsApiLoader(IUIFinishDelegator uiDelegator) {
		super();
		this.uiDelegator = uiDelegator;
		baseWebUrl = App.getContext().getString(R.string.apiStationsBaseUrl);
	}
	
	public void fetchStations() {
		execute(); //will call doInBackground
	}
	
	private String rebuildLocationUrl() {
		LatLng latLng = ApplicationSettings.getGPSCoordinates();
		lat = latLng.latitude;
		lng = latLng.longitude;
		locationUrl = "?lat=" + latLongFormat.format(lat) + "&long=" + latLongFormat.format(lng);
		
		String maxDistance = "&max_distance=" + ApplicationSettings.getDistanceRadius() ;

		String url = baseWebUrl + locationUrl + maxDistance + requestFormat;
		url = url.replace(",", ".");
		return url;
	}
	
	@Override
	protected List<Station> doInBackground(String... params) {

		String url = rebuildLocationUrl();
		HttpGet get = new HttpGet(url);
		get.addHeader(BasicScheme.authenticate(
				new UsernamePasswordCredentials(
						App.getContext().getString(R.string.apiBasicAuthUser), 
						App.getContext().getString(R.string.apiBasicAuthPass)),
				"UTF-8", false));

		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		List<Station> stations = new ArrayList<Station>();
		
		try {
			response = client.execute(get);
			InputStream content = response.getEntity().getContent();
			String convertStreamToString = Utils.convertStreamToString(content);
			JSONObject stationsJSON = new JSONObject(convertStreamToString);
			JSONArray jsonArray = stationsJSON.getJSONArray("stations");
			Gson gson = new Gson();
			for (int i = 0; i < jsonArray.length(); i++) {
				String stationString = new JSONObject(jsonArray.get(i).toString()).toString();
				Station station = gson.fromJson(stationString, Station.class);
				stations.add(station);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return stations;
	}
	
	@Override
	protected void onPostExecute(List<Station> stations) {
		uiDelegator.dataReadyToUse(stations);
		super.onPostExecute(stations);
	}
}
