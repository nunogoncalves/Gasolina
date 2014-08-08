package com.numicago.android.gasolina.objects;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.numicago.android.gasolina.settings.ApplicationSettings;

public class Station implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String _id;
	private String name;
	private String brand_name;
	private float[] latlong;
	private float diesel_price;
	private float eight_price;
	private float five_price;
	private float gpl_price;
	private String updated_at;
	
	/*
		"_id": "53bc7f1a9872b0f14f000789",
		"_keywords": [
		"kqfunjab",
		"ourem",
		"outro",
		"repsol",
		"valinho"
		],
		"address": null,
		"brand_name": "REPSOL",
		"created_at": "2014-07-09T00:30:34+01:00",
		"deleted_at": null,
		"diesel_price": 1.399,
		"eight_price": null,
		"five_price": 1.659,
		"geo_near_distance": 0.005935301854161178,
		"gpl_price": null,
		"internal_reference": "kqfunjab",
		"latlong": [
		39.591014,
		-8.649793
		],
		"location": "Ourém",
		"name": "E.S. VALINHO",
		"reference": 164845,
		"search": null,
		"slug": "repsol-e-s-valinho-kqfunjab",
		"source_updated_at": null,
		"type": "Outro",
		"updated_at": "2014-07-26T12:04:10+01:00"
	 * */
	public Station(String _id, String name) {
		this._id = _id;
		this.name = name;
	}
	
	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float[] getLatLong() {
		return latlong;
	}
	
	public LatLng getLatLongObject() {
		return new LatLng(getLatLong()[0], getLatLong()[1]);
	}
	
	public String getBrandName() {
		return this.brand_name;
	}
	
	public void setBrandName(String brand_name) {
		this.brand_name = brand_name;
	}
	
	public void setLatLong(float[] latLong) {
		this.latlong = latLong;
	}
	
	public float getDiesel_price() {
		return diesel_price;
	}

	public void setDiesel_price(float diesel_price) {
		this.diesel_price = diesel_price;
	}

	public float getEight_price() {
		return eight_price;
	}

	public void setEight_price(float eight_price) {
		this.eight_price = eight_price;
	}

	public float getFive_price() {
		return five_price;
	}

	public void setFive_price(float five_price) {
		this.five_price = five_price;
	}

	public float getGpl_price() {
		return gpl_price;
	}

	public void setGpl_price(float gpl_price) {
		this.gpl_price = gpl_price;
	}

	public String getDistance() {
		return mOrKm(distance());  
	}
	
	public void set_Updated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
	public String get_updated_at() {
		return updated_at;
	}
	
	public String getUpdatedAt() {
		SimpleDateFormat parseFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
		SimpleDateFormat displayFormatter = new SimpleDateFormat("dd 'de' MMMM 'às' HH:mm", Locale.getDefault());
		
		try {
			return displayFormatter.format(parseFormatter.parse(updated_at));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return "ooops";
		}
	}
	
	public String mOrKm(float distance) {
		DecimalFormat df;
		if (distance > 1000) {
			df = new DecimalFormat("#.##");
			return df.format(distance / 1000) + " Km";
		} else {
			df = new DecimalFormat("#");
			return df.format(distance) + " m";
		}
	}
	
	public float distance() {
		LatLng currentLocation = ApplicationSettings.getGPSCoordinates();
		
		float[] results = new float[2];
		
		Location.distanceBetween(
				currentLocation.latitude,
				currentLocation.longitude,
				getLatLongObject().latitude, 
				getLatLongObject().longitude, results);
		
		return results[0];
	}

	private static final HashMap<String, String> namesHash;
    static
    {
    	namesHash = new HashMap<String, String>();
    	namesHash.put("GALP", "galp");
    	namesHash.put("CEPSA", "cepsa");
    	namesHash.put("BP", "bp");
    	namesHash.put("INTERMARCHÉ", "intermarche");
    	namesHash.put("JUMBO", "jumbo");
    	namesHash.put("REDE ENERGIA", "rede_energia");
    	namesHash.put("REPSOL", "repsol");
    }

	public String getFormattedBrandName() {
		String formatted = Station.namesHash.get(brand_name);
		if(formatted != null) {
			return formatted;
		}
		return "";
	}
	
}
