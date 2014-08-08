package com.numicago.android.gasolina.activities.viewloaders;

import android.app.Activity;

public abstract class ViewLoader {
	
	protected Activity activity;
	
	public ViewLoader(Activity activity) {
		this.activity = activity;
		instantiateViews();
		fillViews();
	}
	
	protected abstract void instantiateViews();
	protected abstract void fillViews();
}
