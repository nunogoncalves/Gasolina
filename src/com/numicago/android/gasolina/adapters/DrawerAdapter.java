package com.numicago.android.gasolina.adapters;

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

public class DrawerAdapter extends BaseAdapter {

	private Activity activity;
	private String[] drawerEntries;
	private LayoutInflater inflater;
	
	public DrawerAdapter(Activity activity) {
		this.activity = activity;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		drawerEntries = activity.getResources().getStringArray(R.array.drawerEntries);
	}
	
	@Override
	public int getCount() {
		return drawerEntries.length;
	}

	@Override
	public String getItem(int position) {
		return drawerEntries[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private static final int[] icons = {R.drawable.closest, R.drawable.favourite, R.drawable.settings};
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.drawer_item, null);
        }
		
		TextView itemTV = (TextView) convertView.findViewById(R.id.drawerItemTV);
		itemTV.setText(getItem(position));

		ImageView drawerItemIV = (ImageView) convertView.findViewById(R.id.drawerItemIV);
		drawerItemIV.setImageDrawable(activity.getResources().getDrawable(icons[position]));
		
		return convertView;
	}

}
