package com.numicago.android.gasolina.activities.viewloaders;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public interface IUIFinishDelegator {
	
	public void dataReadyToUse(List<?> list, LatLng point);
	
}
