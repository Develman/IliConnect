package com.android.iliConnect;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.iliConnect.models.DesktopItem;
import com.android.iliConnect.models.Item;

public class DesktopArrayAdapter extends ArrayAdapter<Item> {

	private class DesktopViews {
		TextView title;
		TextView description;
		TextView date;
		TextView type;

		LinearLayout items;
	}

	public DesktopArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	protected List<Item> items;

	public DesktopArrayAdapter(Context context, int textViewResourceId, List<Item> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v = fillListRecursive(null, items.get(position), vi);
		
		
		v.findViewById(R.id.imageButton1).setVisibility(View.VISIBLE);
		v.findViewById(R.id.imageButton1).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Item item = items.get(position);
				MainActivity.instance.showBrowserContent(MainActivity.instance.localDataProvider.auth.url_src+"webdav.php?ref_id="+items.get(position).ref_id);
			}
		});
		//replaceView(v, (LinearLayout)convertView,items.get(position).getType());
		return v;
	}
	
	private View replaceView(View v1, LinearLayout v, String type) {
		int resID = 0;
		String descr = null;
		if (type.equalsIgnoreCase("FILE")) {
			resID = R.drawable.dl;
			descr = "Download";
		} else if (type.equalsIgnoreCase("FOLD")) {
			resID = R.drawable.fold;
			descr = "Ordner";
		} else if (type.equalsIgnoreCase("EXC")) {
			resID = R.drawable.exc;
			descr = "Übung";
		} else if (type.equalsIgnoreCase("TST")) {
			resID = R.drawable.tst;
			descr = "Test";
		} else if (type.equalsIgnoreCase("UNSIGN")) {
			resID = R.drawable.tst; // eigentlich unsign
			descr = "";
		}
		
		v.removeView(v1);
		LinearLayout layout = new LinearLayout(MainActivity.instance);

		layout.setGravity(Gravity.TOP);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(20, 30, 0, 0);
		lp.height = 50;
		lp.width = 50;

//		ImageButton arrow = new ImageButton(MainActivity.instance);
//		arrow.setBackgroundResource(R.drawable.arrow);
		
		
		//LinearLayout layoutArrow = new LinearLayout(MainActivity.instance);
		//layoutArrow.setGravity(Gravity.RIGHT);
		//layoutArrow.addView(arrow);
		
		ImageButton vSub = new ImageButton(MainActivity.instance);

		vSub.setBackgroundResource(resID);
		vSub.setLayoutParams(lp);
		layout.addView(vSub);
		((TextView) v1.findViewById(R.id.itemType)).setText(descr);
		layout.addView(v1);
//		layout.addView(arrow);
		
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		v1.setLayoutParams(lp1);

		if (type.equalsIgnoreCase("UNSIGN")){
				((TextView)v1.findViewById(R.id.itemTitle)).setTextColor(Color.RED);
				((TextView)v1.findViewById(R.id.itemDescription)).setTextColor(Color.RED);
		}
		
		return layout;

	}

	protected LinearLayout fillListRecursive(LinearLayout v, final Item item, LayoutInflater vi) {
		DesktopViews desktopViews = new DesktopViews();
		if (v == null) {
			v = (LinearLayout) vi.inflate(R.layout.item, null);
			desktopViews = new DesktopViews();
			desktopViews.title = (TextView) v.findViewById(R.id.itemTitle);
			desktopViews.description = (TextView) v.findViewById(R.id.itemDescription);
			desktopViews.date = (TextView) v.findViewById(R.id.itemDate);
			desktopViews.type = (TextView) v.findViewById(R.id.itemType);
			
		} else
			desktopViews = (DesktopViews) v.getTag();

		desktopViews.title.setText(item.getTitle());
		desktopViews.description.setText(item.getDescription());
		desktopViews.type.setText(item.getType());

		
		if (item.getType().equalsIgnoreCase("CRS") || item.getType().equalsIgnoreCase("FOLD"))
			desktopViews.type.setVisibility(View.GONE);

		if (item.getClass().equals(DesktopItem.class) && !((DesktopItem) item).getDate().equals(""))
			desktopViews.date.setText(((DesktopItem) item).getDate());
		else
			desktopViews.date.setVisibility(View.GONE);

		v.setTag(item.getRef_id());

		if (item.getItems() != null) {
			for (final Item childItem : item.getItems()) {

				View v1 = fillListRecursive(desktopViews.items, childItem, vi);
				LinearLayout layout = (LinearLayout) v1;

				final String type = childItem.getType();
				layout = (LinearLayout) replaceView(v1, v, type);

				v1 = layout;
				v1.setTag(childItem.getRef_id());
				v.addView(v1);

			}

		}
		final Item parentItem = item;
		v.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String s = v.getTag().toString();

				if (parentItem.getType().equalsIgnoreCase("FOLD") || parentItem.getType().equalsIgnoreCase("CRS")) {
					toggleVisibility(parentItem, v);
				} else if (parentItem.getType().equalsIgnoreCase("FILE") || parentItem.getType().equalsIgnoreCase("UNSIGN")) {
					MainActivity.instance.localDataProvider.openFileOrDownload(s);
				} else {
					MainActivity.instance.showBrowserContent(MainActivity.instance.localDataProvider.auth.url_src + "webdav.php?ref_id=" + s);
				}

			}
		});
		
		toggleVisibility(item, v);

		return v;

	}

	private void toggleVisibility(Item childItem, View v) {
		View vSub;
		ArrayList<Item> childItems = new ArrayList<Item>();
		if (childItem.Item != null)
			childItems = childItem.Item;

		for (Item item : childItems)
			if ((vSub = v.findViewWithTag(item.getRef_id())).getVisibility() == View.VISIBLE)
				vSub.setVisibility(View.GONE);
			else
				vSub.setVisibility(View.VISIBLE);
	}
}
