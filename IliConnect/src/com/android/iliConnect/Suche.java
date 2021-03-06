package com.android.iliConnect;

import static android.content.Context.CONNECTIVITY_SERVICE;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.android.iliConnect.Exceptions.NetworkException;
import com.android.iliConnect.dataproviders.RemoteDataProvider;
import com.android.iliConnect.models.Item;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Suche extends ListFragment implements Redrawable {

	private ArrayList<Item> searchResult = new ArrayList<Item>();
	private ArrayList<Item> existingCourses = new ArrayList<Item>();
	private EditText etSearch;
	private ListAdapter fileList;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		List<Item> courses = new ArrayList<Item>();
		LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.suche_layout, container, false);

		if (MainActivity.instance.localDataProvider.results.Item != null) {
			// nur die Kurse als Suchergebnis anzeigen, in denen man noch nicht angemeldet ist.
			courses = this.getSearchResults();
		}

		fileList = new SearchArrayAdapter(getActivity(), R.layout.item, courses);
		setListAdapter(fileList);

		if (container == null) {
			return null;
		}

		etSearch = (EditText) mLinearLayout.findViewById(R.id.editText1);
		etSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void afterTextChanged(Editable s) {
				if (s.length() >= 1) {
					try {
						updateResults(s.toString());
					} catch (NetworkException e) {
						MainActivity.instance.showToast(e.getMessage());
						e.printStackTrace();
					}
				}

			}
		});

		return mLinearLayout;

	}

	private void updateResults(String s) throws NetworkException {
		boolean wlanOnly = MainActivity.instance.localDataProvider.settings.sync_wlanonly;

		ConnectivityManager connManager = (ConnectivityManager) MainActivity.instance.getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("searchfor", s));

		((ProgressBar) MainTabView.instance.findViewById(R.id.progressBar1)).setVisibility(View.VISIBLE);
		;

		if (wlanOnly) {
			// wenn Wlan-Only gesetzt ist, muss eine Wlan-Verbindung vorhanden sein
			if (wifi == null || !wifi.isConnected()) {
				throw new NetworkException("Es wird eine Wlan-Verbindung benötigt.");
			}
			RemoteDataProvider rP = new RemoteDataProvider(nameValuePairs);
			rP.execute(new String[] { MainActivity.instance.localDataProvider.remoteData.getSyncUrl() + "?action=search", MainActivity.instance.localDataProvider.searchDataFileName });
		} else {
			// falls keine Internetverdndiung besteht, Fehlermeldung schmeißen
			if ((mobile == null || !mobile.isConnected()) && (wifi == null || !wifi.isConnected())) {
				throw new NetworkException("Es wird eine Internetverbindung benötigt.");
			} else {
				RemoteDataProvider rP = new RemoteDataProvider(nameValuePairs);
				rP.execute(new String[] { MainActivity.instance.localDataProvider.remoteData.getSyncUrl() + "?action=search", MainActivity.instance.localDataProvider.searchDataFileName });
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void refreshViews() {

		// nur die Kurse als Suchergebnis anzeigen, in denen man noch nicht angemeldet ist.
		List<Item> courses = this.getSearchResults();

		fileList = new SearchArrayAdapter(getActivity(), R.layout.item, courses);
		getListView().invalidateViews();
		setListAdapter(fileList);

		((ProgressBar) MainTabView.instance.findViewById(R.id.progressBar1)).setVisibility(View.INVISIBLE);
	}

	private List<Item> getSearchResults() {
		ArrayList<Item> courses = new ArrayList<Item>();

		existingCourses = MainActivity.instance.localDataProvider.desktopItems.DesktopItem;
		searchResult = MainActivity.instance.localDataProvider.results.Item;

		if (searchResult != null) {
			for (Item item : searchResult) {
				if (!existingCourses.contains(item)) {
					courses.add(item);
				}
			}
		}

		return courses;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
}