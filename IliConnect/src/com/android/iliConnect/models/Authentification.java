package com.android.iliConnect.models;

import org.simpleframework.xml.Element;
import android.widget.Toast;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.dataproviders.PersistableObject;

@Element
public class Authentification extends PersistableObject {
	@Element
	public boolean autologin = true;
	@Element (required = false)	
	public String user_id = "Joerg";
	@Element (required = false)
	public String password = "schnee";
	@Element
	public String url_src;
	public String api_src = "IliConnect.php";
	

	@Override
	public void load() {
		try {
			super.deserialize(MainActivity.instance.localDataProvider.localDataFilename);

		} catch (Exception e) {
			Toast t = Toast.makeText(MainActivity.instance, "Fehler beim laden der Authentifizierungsparameter.", Toast.LENGTH_LONG);
			t.show();
		}

	}

	public void setLogin(String user_id, String password, String url_src) {
		this.user_id = user_id;
		this.password = password;
		this.url_src = url_src;
	}

}
