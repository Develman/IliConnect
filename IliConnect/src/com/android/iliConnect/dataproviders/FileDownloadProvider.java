package com.android.iliConnect.dataproviders;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.android.iliConnect.MainActivity;
import com.android.iliConnect.models.CourseData;
import com.android.iliConnect.ssl.HttpsClient;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;

public class FileDownloadProvider extends AsyncTask<String, Integer, String> {
	
	ProgressDialog progressDialog;
	
	public FileDownloadProvider(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	@Override
	protected String doInBackground(String... sUrl) {

		try {
			String url = sUrl[0];
			String filePath = sUrl[1];
			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			
			// mache aus http einen httpsClient
			HttpClient httpsClient = HttpsClient.createHttpsClient(httpClient);
		
			// erst Post gegenüber Login durchführen
			HttpPost post = new HttpPost(MainActivity.instance.localDataProvider.auth.url_src + "login.php");

			List<NameValuePair> postFields = new ArrayList<NameValuePair>(2);

			// Set the post fields
			postFields.add(new BasicNameValuePair("username", MainActivity.instance.localDataProvider.auth.user_id));
			postFields.add(new BasicNameValuePair("password", MainActivity.instance.localDataProvider.auth.password));
			post.setEntity(new UrlEncodedFormEntity(postFields, HTTP.UTF_8));

			HttpResponse response = null;
			response = httpsClient.execute(post);

			// als zweites Datei per Get laden
			HttpGet get = new HttpGet(url);
			response = httpsClient.execute(get);

			HttpEntity entity = response.getEntity();
			InputStream in = entity.getContent();

			// Save the file to SD
			/*File path = Environment.getExternalStoragePublicDirectory(filePath);
			path.mkdirs();*/
			File file = new File(filePath);
			FileOutputStream fos = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int len1 = 0;

			while ((len1 = in.read(buffer)) > 0) {
				fos.write(buffer, 0, len1);
			}

			fos.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		progressDialog.setProgress(progress[0]);
	}

}