package com.numicago.android.gasolina.activities.viewloaders;

import android.app.Activity;
import android.view.View;

public abstract class ViewLoader {
	
	protected Activity activity;
	
	public ViewLoader(Activity activity) {
		this.activity = activity;
		instantiateViews();
		fillViews();
	}
	
	protected void showView(View v) {
		v.setVisibility(View.VISIBLE);
	}
	
	protected void hideView(View v) {
		v.setVisibility(View.GONE);
	}
	
	protected boolean isVisible(View v) {
		return v.getVisibility() == View.VISIBLE;
	}
	protected abstract void instantiateViews();
	protected abstract void fillViews();
}
