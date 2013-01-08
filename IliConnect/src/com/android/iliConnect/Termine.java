package com.android.iliConnect;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.android.iliConnect.models.Notification;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Termine extends ListFragment implements Redrawable, OnCheckedChangeListener {

	private TerminItemAdapter terminAdapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.termine_layout, container, false);

		List<Notification> notifications = getNotificationList();
		terminAdapter = new TerminItemAdapter(MainActivity.currentActivity, R.layout.termin_list_item, notifications, this);
		setListAdapter(terminAdapter);

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

	public void refreshViews() {
		getListView().invalidateViews();

		List<Notification> notifications = getNotificationList();
		terminAdapter = new TerminItemAdapter(MainActivity.currentActivity, R.layout.termin_list_item, notifications, this);
		setListAdapter(terminAdapter);
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		CheckBox checkbox = (CheckBox) buttonView;
		String ref_id = (String) checkbox.getTag();

		Notification selectedNoti = null;
		for (Notification notification : MainActivity.instance.localDataProvider.notifications.Notifications) {
			if (notification.getRef_id().equals(ref_id)) {
				selectedNoti = notification;
				break;
			}
		}

		if (selectedNoti != null) {
			selectedNoti.marked = true;
			MainActivity.instance.localDataProvider.remoteData.save();
		}
	}

	private List<Notification> getNotificationList() {
		List<Notification> notifications = new ArrayList<Notification>();
		for (Notification notification : MainActivity.instance.localDataProvider.notifications.Notifications) {
			if (notification.date != null) {
				Calendar notiDate = new GregorianCalendar();
				notiDate.set(notification.date.getYear(), notification.date.getMonth(), notification.date.getDay());

				Date todayDate = new Date();
				Calendar today = new GregorianCalendar();
				today.set(todayDate.getYear(), todayDate.getMonth(), todayDate.getDay());

				if (notiDate.after(today)) {
					notifications.add(notification);
				}
			}
		}

		return notifications;

	}
}
