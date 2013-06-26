package com.charitymaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.charitymaps.R;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AllDisastersActivity extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> disastersList;

	// url to get all disasters list
	private static String url_all_disasters = "http://www.gowithus.nl/projects/android/v2/get_all_disasters.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_DISASTERS = "disasters";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	private static final String TAG_LOCATION = "location";

	// disasters JSONArray
	JSONArray disasters = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_disasters);

		// Hashmap for ListView
		disastersList = new ArrayList<HashMap<String, String>>();

		// Loading disasters in Background Thread
		new LoadAllDisasters().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single disaster
		// launching Edit disaster Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String pid = ((TextView) view.findViewById(R.id.pid)).getText()
						.toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						ViewDisasterActivity.class);
				// sending pid to next activity
				in.putExtra(TAG_PID, pid);

				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});

	}

	// Response from Edit disaster Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// if result code 100 is received
			// means user edited/deleted disaster
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	/**
	 * Background Async Task to Load all disaster by making HTTP Request
	 * */
	class LoadAllDisasters extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllDisastersActivity.this);
			pDialog.setMessage("Loading, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All disaster from url
		 * */
		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_disasters, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("All Disasters: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// disasters found
					// Getting Array of disasters
					disasters = json.getJSONArray(TAG_DISASTERS);

					// looping through All disasters
					for (int i = 0; i < disasters.length(); i++) {
						JSONObject c = disasters.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_PID);
						String name = c.getString(TAG_NAME);
						String location = c.getString(TAG_LOCATION);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_PID, id);
						map.put(TAG_NAME, name);
						map.put(TAG_LOCATION, location);

						// adding HashList to ArrayList
						disastersList.add(map);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all disaster
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							AllDisastersActivity.this, disastersList,
							R.layout.list_item, new String[] { TAG_PID,
									TAG_NAME, TAG_LOCATION },
							new int[] { R.id.pid, R.id.name,
									R.id.databaseLocation });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}