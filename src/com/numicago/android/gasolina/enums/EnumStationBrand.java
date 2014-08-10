package com.numicago.android.gasolina.enums;

import com.numicago.android.gasolina.R;

public enum EnumStationBrand {
	
	GALP("GALP", "galp", R.drawable.galp, R.drawable.galp_pointer),
	CEPSA("CEPSA", "cepsa", R.drawable.cepsa, R.drawable.galp_pointer),
	BP("BP", "bp", R.drawable.bp, R.drawable.bp_pointer),
	INTERMARCHE("INTERMARCHÉ", "intermarche", R.drawable.intermarche, R.drawable.intermarche_pointer),
	JUMBO("JUMBO", "jumbo", R.drawable.jumbo, R.drawable.jumbo_pointer),
	REDE_ENERGIA("REDE ENERGIA", "rede_energia", R.drawable.rede_energia, R.drawable.rede_energia_pointer),
	REPSOL("REPSOL", "repsol", R.drawable.repsol, R.drawable.repsol_pointer),
	
	GENERIC("", "", R.drawable.station_placeholder, R.drawable.station_placeholder_pointer);
	
	private String apiName;
	private String appName;
	private int iconDrawable;
	private int markerDrawable;

	private EnumStationBrand(
			String apiName, 
			String appName, 
			int iconDrawable, 
			int markerDrawable) {
		
		this.apiName = apiName;
		this.appName = appName;
		this.iconDrawable = iconDrawable;
		this.markerDrawable = markerDrawable;
	}
	
	public String getApiName() {
		return apiName;
	}

	public String getAppName() {
		return appName;
	}

	public int getIconDrawable() {
		return iconDrawable;
	}

	public int getMarkerDrawable() {
		return markerDrawable;
	}

	public static EnumStationBrand getStaionEnumFromName(String apiName) {
		for (EnumStationBrand enumBrand : EnumStationBrand.values()) {
			if(enumBrand.apiName.equals(apiName)) {
				return enumBrand;
			}
		}
		return GENERIC;
	}
	
	public static String getAppNameFromApiName(String apiName) {
		EnumStationBrand enumStation = getStaionEnumFromName(apiName);
		return enumStation.appName;
	}
	
	public static int getIconDrawableFromApiName(String apiName) {
		EnumStationBrand enumStation = getStaionEnumFromName(apiName);
		return enumStation.iconDrawable;
	}
	
	public static int getMarkerDrawableFromApiName(String apiName) {
		EnumStationBrand enumStation = getStaionEnumFromName(apiName);
		return enumStation.markerDrawable;
	}
	
}