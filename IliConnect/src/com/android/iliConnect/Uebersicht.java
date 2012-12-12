package com.android.iliConnect;

import java.util.ArrayList;
import java.util.List;

import android.app.LauncherActivity.ListItem;
import android.app.ListFragment;
import android.app.Notification;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnDrawListener;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Uebersicht extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.uebersicht_layout, container, false);
		ListView notesLV = (ListView) v.findViewById(R.id.listViewNotifications);
		ArrayList<ListViewItem> noteItems = new ArrayList<ListViewItem>();

		for (com.android.iliConnect.models.Notification note : MainActivity.localDataProvider.notifications.Notifications) {
			ListViewItem lvI = new ListViewItem(note.title,note.description,note.getDate());
			noteItems.add(lvI);
		}
		NoteArrayAdapter noteAdapter = new NoteArrayAdapter(MainActivity.currentActivity, R.layout.noteitem, noteItems.toArray(new ListViewItem[noteItems.size()]));

		notesLV.setAdapter(noteAdapter);

		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
}