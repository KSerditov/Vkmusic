package ru.kserditov.vkmusic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class GetJson extends AsyncTask<String, Void, String> {
	@Override
	protected String doInBackground(String... urls) {
		// params comes from the execute() call: params[0] is the url.
		return getAudioJson(urls[0]);
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		// textView.setText(result);
	}

	public String getAudioJson(String searchAudioUrl) {
		String jsonResponse = "";

		Log.i("UTILS", "Creating HTTP client");

		HttpClient httpClient = new DefaultHttpClient();

		Log.i("UTILS", "Search request: " + searchAudioUrl);

		HttpGet httpGet = new HttpGet(searchAudioUrl);

		try {

			HttpResponse httpResponse = httpClient.execute(httpGet);

			StatusLine statusLine = httpResponse.getStatusLine();

			Log.i("UTILS", "Status Line: " + statusLine.getStatusCode());

			if (statusLine.getStatusCode() == 200) {

				HttpEntity httpEntity = httpResponse.getEntity();
				InputStreamReader inputStreamReader = new InputStreamReader(
						httpEntity.getContent());
				BufferedReader br = new BufferedReader(inputStreamReader);
				StringBuilder sb = new StringBuilder();
				String read = br.readLine();

				while (read != null) {
					sb.append(read);
					read = br.readLine();
				}

				jsonResponse = sb.toString();

				Log.i("UTILS", "JSON: " + jsonResponse);

				JSONObject jObject = new JSONObject(jsonResponse);
				JSONObject obj2 = (JSONObject) jObject.get("response");
				JSONArray jArray = (JSONArray) obj2.get("items");

				for (int i = 0; i < jArray.length(); i++) {
					try {
						JSONObject oneObject = jArray.getJSONObject(i);
						// Pulling items from the array
						// String oneObjectsItem = oneObject.getString("id");
						// String oneObjectsItem2 =
						// oneObject.getString("duration");
						Log.i("ERROR", oneObject.getString("id"));
						Log.i("ERROR", oneObject.getString("artist"));
						Log.i("ERROR", oneObject.getString("title"));
						Log.i("ERROR", oneObject.getString("duration"));
						Log.i("ERROR", oneObject.getString("url"));
					} catch (JSONException e) {
						Log.e("ERROR", e.getLocalizedMessage());
					}
				}

			}
		} catch (IOException e) {
			Log.e("ERROR", e.getLocalizedMessage());
		} catch (JSONException e) {
			Log.e("ERROR", e.getLocalizedMessage());
		}

		return jsonResponse;
	}
}
