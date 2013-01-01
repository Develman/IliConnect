package com.android.iliConnect;

import java.util.ArrayList;
import java.util.List;

import com.android.iliConnect.models.Desktop;
import com.android.iliConnect.models.DesktopItem;
import com.android.iliConnect.models.Item;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DesktopArrayAdapter extends ArrayAdapter<DesktopItem> {

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

	private List<DesktopItem> items;

	public DesktopArrayAdapter(Context context, int textViewResourceId, List<DesktopItem> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		v = fillListRecursive(null, items.get(position), vi);

		return v;
	}

	private View replaceView(View v1, LinearLayout v, int resID, String descr) {
		v.removeView(v1);
		LinearLayout layout = new LinearLayout(MainActivity.instance);
		
		//if (descr.equalsIgnoreCase("Ordner"))
		//	layout.setGravity(Gravity.TOP);
		//else
		layout.setGravity(Gravity.TOP);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(20, 30, 0, 0);
		
		
		ImageButton vSub = new ImageButton(MainActivity.instance);

		vSub.setBackgroundResource(resID);
		vSub.setLayoutParams(lp);
		layout.addView(vSub);
		((TextView) v1.findViewById(R.id.itemType)).setText(descr);
		layout.addView(v1);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		v1.setLayoutParams(lp1);
		
		return layout;

	}

	public LinearLayout fillListRecursive(LinearLayout v, final Item item, LayoutInflater vi) {
		DesktopViews desktopViews = new DesktopViews();
		if (v == null) {
			v = (LinearLayout) vi.inflate(R.layout.item, null);
			desktopViews = new DesktopViews();
			desktopViews.title = (TextView) v.findViewById(R.id.itemTitle);
			desktopViews.description = (TextView) v.findViewById(R.id.itemDescription);
			desktopViews.date = (TextView) v.findViewById(R.id.itemDate);
			desktopViews.type = (TextView) v.findViewById(R.id.itemType);
			v.setTag(desktopViews);
		} else
			desktopViews = (DesktopViews) v.getTag();

		desktopViews.title.setText(item.getTitle());
		desktopViews.description.setText(item.getDescription());
		desktopViews.type.setText(item.getType());
		
		if(item.getType().equalsIgnoreCase("CRS") || item.getType().equalsIgnoreCase("FOLD"))
			desktopViews.type.setVisibility(View.GONE);
		
		if (item.getClass().equals(DesktopItem.class))
			desktopViews.date.setText(((DesktopItem) item).getDate());
		else
			desktopViews.date.setVisibility(View.GONE);

		
		
		v.setTag(item.getRef_id());

		if (item.getItems() != null) {
			for (final Item childItem : item.getItems()) {

				View v1 = fillListRecursive(desktopViews.items, childItem, vi);
				LinearLayout layout = (LinearLayout) v1;

				final String type = childItem.getType();
				if (type.equalsIgnoreCase("FILE")) {
					layout = (LinearLayout) replaceView(v1, v, R.drawable.dl, "Download");
				} else if (type.equalsIgnoreCase("FOLD")) {
					layout = (LinearLayout) replaceView(v1, v, R.drawable.fold, "Ordner");
				} else if (type.equalsIgnoreCase("EXC")) {
					layout = (LinearLayout) replaceView(v1, v, R.drawable.exc, "�bung");
				} else if (type.equalsIgnoreCase("TST")) {
					layout = (LinearLayout) replaceView(v1, v, R.drawable.tst, "Test");
				}
				v1 = layout;
				v1.setTag(childItem.getRef_id());
				v.addView(v1);

			}

		}
		final Item parentItem = item;
		v.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String s = v.getTag().toString();

				if(parentItem.getType().equalsIgnoreCase("fold") || parentItem.getType().equalsIgnoreCase("crs")){
					toggleVisibility(parentItem, v);
				}	else if (parentItem.getType().equalsIgnoreCase("file")) {
					MainActivity.instance.localDataProvider.openFileOrDownload(s);
				} else {
					MainActivity.instance.showBrowserContent(MainActivity.instance.localDataProvider.auth.url_src + "webdav.php?ref_id=" + s);
				}


				
			}
		});
		
		return v;

	}

	private void toggleVisibility(Item childItem, View v) {
		View vSub;
		ArrayList<Item> childItems = new ArrayList<Item>();
		if (childItem.Item != null)
			childItems = childItem.Item;
		else if(childItem.getClass().equals(DesktopItem.class) && ((DesktopItem)childItem).Items != null)
			childItems = ((DesktopItem)childItem).Items;
			
		
			
			for (Item item : childItems)
				if ((vSub = v.findViewWithTag(item.getRef_id())).getVisibility() == View.VISIBLE)
					vSub.setVisibility(View.GONE);
				else
					vSub.setVisibility(View.VISIBLE);
	}
}