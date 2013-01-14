package com.android.iliConnect;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.iliConnect.Exceptions.CoursePasswordException;
import com.android.iliConnect.Exceptions.JoinCourseException;
import com.android.iliConnect.Exceptions.NetworkException;
import com.android.iliConnect.dataproviders.LocalCourseProvider;
import com.android.iliConnect.message.QROnClickListener;
import com.android.iliConnect.models.Item;

public class SearchArrayAdapter extends DesktopDetailArrayAdapter implements QROnClickListener {
	
	
	private class SearchView {
		TextView title;
		TextView description;
		TextView owner;
	}

	public SearchArrayAdapter(Context context, int textViewResourceId, List<Item> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Item item = items.get(position);
			
		View v = convertView;
		LayoutInflater vi = (LayoutInflater) MainActivity.instance.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		v = (LinearLayout) vi.inflate(R.layout.item, null);
		
		SearchView searchView = new SearchView();
		
		searchView.title = (TextView) v.findViewById(R.id.itemTitle);
		searchView.description = (TextView) v.findViewById(R.id.itemDescription);
		searchView.owner = (TextView) v.findViewById(R.id.itemOwner);
		
		searchView.title.setText(item.getTitle());
		searchView.description.setText(item.getDescription());
		searchView.owner.setText(item.getOwner());
		
		// Falls Description leer ist, keine Leerzeile anzeigen
		if(searchView.description.getText().equals("")) {
			v.findViewById(R.id.itemDescription).setVisibility(View.GONE);
		}
		
		if(searchView.owner.getText().equals("")) {
			v.findViewById(R.id.itemOwner).setVisibility(View.GONE);
		}
		
		// Date und Type in der Ergebnislist ausblenden
		v.findViewById(R.id.itemDate).setVisibility(View.GONE);
		v.findViewById(R.id.itemType).setVisibility(View.GONE);
		
		final SearchArrayAdapter instance = (SearchArrayAdapter) this;
		v.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				v.setBackgroundColor(R.color.darkmint);
				Item item = items.get(position);
				MessageBuilder.course_login(MainTabView.instance, item.title, item.ref_id, instance);		
			}
		});
		return v;
	}
	
	
	private void joinCourse(String ref_id, String password) {
		LocalCourseProvider courseProv = new LocalCourseProvider();
		try {
			try {
				String result = courseProv.joinCourse(ref_id, password);
				if(result != null && result.contains("PASSWORD_NEEDED")) {
					// Passwortabfrage einblenden
					MessageBuilder.course_password(MainTabView.instance, ref_id, this);
				}
			} catch (NetworkException e) {
				// TODO Fehlermeldung anzeigen!!
				e.printStackTrace();
			}
		} catch (JoinCourseException e) {
			e.printStackTrace();
		} catch (CoursePasswordException e) {
			MessageBuilder.course_password(MainTabView.instance, ref_id, this);
			e.printStackTrace();
		}
		
	}


	public void onClickCoursePassword(String ref_id, String password) {
		// nach Passworteingabe joinCourse() erneut aufrufen
		this.joinCourse(ref_id, password);	
	}


	public void onClickSignOnCourse(String ref_id, String password) {
		this.joinCourse(ref_id, null);
	}

}
