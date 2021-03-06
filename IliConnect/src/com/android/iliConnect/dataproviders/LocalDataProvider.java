package com.android.iliConnect.dataproviders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.xmlpull.v1.XmlPullParser;

import android.content.res.XmlResourceParser;
import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.MainTabView;
import com.android.iliConnect.MessageBuilder;
import com.android.iliConnect.R;
import com.android.iliConnect.models.Authentification;
import com.android.iliConnect.models.Current;
import com.android.iliConnect.models.Desktop;
import com.android.iliConnect.models.Item;
import com.android.iliConnect.models.LocalData;
import com.android.iliConnect.models.Notification;
import com.android.iliConnect.models.Notifications;
import com.android.iliConnect.models.RemoteData;
import com.android.iliConnect.models.Results;
import com.android.iliConnect.models.Settings;
import com.android.iliConnect.models.modification.AppData;

public class LocalDataProvider {

	private static LocalDataProvider instance;

	public Settings settings = new Settings();
	public Notifications notifications = new Notifications();

	public LocalData localdata = new LocalData();
	public RemoteData remoteData = new RemoteData();
	public AppData appData = new AppData();

	public Desktop desktopItems = new Desktop();
	public Authentification auth = new Authentification();;
	public Results results = new Results();

	public String appDataFileName = "AppData.xml";
	public String searchDataFileName = "SearchData.xml";
	public String remoteDataFileName = "RemoteData.xml";
	public String localDataFilename = "LocalData.xml";

	public static boolean isAvaiable = false;
	public boolean isUpdating = false;
	public ReentrantLock syncObject;

	public static LocalDataProvider getInstance() {
		if (instance == null) {
			instance = new LocalDataProvider();

		}
		return instance;
	}

	public void init(int xmlRes) {
		syncObject = new ReentrantLock();

		String dataFile = "";
		if (xmlRes == R.xml.config) {
			dataFile = localDataFilename;
		} else {
			dataFile = appDataFileName;
		}

		File config = new File(MainActivity.instance.getFilesDir() + "/" + dataFile);
		if (!config.exists()) {
			XmlResourceParser xpp = MainActivity.currentActivity.getResources().getXml(xmlRes);
			StringBuffer stringBuffer = new StringBuffer();
			try {
				xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {

					if (eventType == XmlPullParser.START_TAG) {
						stringBuffer.append("<" + xpp.getName() + ">");
					} else if (eventType == XmlPullParser.END_TAG) {
						stringBuffer.append("</" + xpp.getName() + ">");
					} else if (eventType == XmlPullParser.TEXT) {
						stringBuffer.append(xpp.getText());
					}
					eventType = xpp.next();
				}
				OutputStream output = new FileOutputStream(MainActivity.instance.getFilesDir() + "/" + dataFile);
				output.write(stringBuffer.toString().getBytes());

			} catch (Exception e) {
				Toast t = Toast.makeText(MainActivity.instance, "Fehler beim Erstellen der lokalen Konfigurationsdatei.", Toast.LENGTH_LONG);
				t.show();
			}

		}

	}

	public boolean updateLocalData() {
		try {

			remoteData.load();
			appData.load();

			if (remoteData.Current != null) {
				Current current = remoteData.Current;
				if (current.Desktop != null && current.Desktop.DesktopItem != null) {
					desktopItems.DesktopItem = current.Desktop.DesktopItem;
				} else {
					ArrayList<Item> items = new ArrayList<Item>();
					desktopItems.DesktopItem = items;
				}

				if (current.Notifications != null) {
					notifications.Notifications = remoteData.Current.Notifications;
				} else {
					ArrayList<Notification> notis = new ArrayList<Notification>();
					notifications.Notifications = notis;
				}

			}

			if (new File(MainActivity.instance.getFilesDir() + "/" + appDataFileName).exists()) {
				appData.load();
			}

			isUpdating = false;

			if (new File(MainActivity.instance.getFilesDir() + "/" + searchDataFileName).exists())
				results.load();

			synchronized (MainActivity.syncObject) {
				MainActivity.syncObject.notifyAll();
			}

			if (MainTabView.getInstance() != null)
				MainTabView.getInstance().update();

		} catch (Exception e) {

			MessageBuilder.exception_message(MainTabView.instance, "Es ist ein Fehler während der Synchronisation aufgetreten.");
			// Datei löschen, falls beim Erzeugen ein Fehler aufgetreten ist.
			File remoteDataFile = new File(MainActivity.instance.getFilesDir() + "/" + MainActivity.instance.localDataProvider.remoteDataFileName);
			remoteDataFile.delete();
			//MainActivity.instance.logout();

			return false;
		}
		isAvaiable = true;
		return true;

	}

	public void deleteAuthentication() {

		MainActivity.instance.localDataProvider.auth.setLogin(false, "", "", "http://swe.k3mp.de/ilias/");
		MainActivity.instance.localDataProvider.localdata.save();
	}

}
