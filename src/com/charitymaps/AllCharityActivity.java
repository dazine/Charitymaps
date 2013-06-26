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

public class AllCharityActivity extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> charityList;

	// url to get all disasters list
	private static String url_all_charity = "http://www.gowithus.nl/projects/android/v2/get_all_charity.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_CHARITY = "charity";
	private static final String TAG_SID = "sid";
	private static final String TAG_NAME = "name";

	// disasters JSONArray
	JSONArray charity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_charity);

		// Hashmap for ListView
		charityList = new ArrayList<HashMap<String, String>>();

		// Loading disasters in Background Thread
		new LoadAllCharity().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single disaster
		// launching Edit disaster Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// getting values from selected ListItem
				String sid = ((TextView) view.findViewById(R.id.pid)).getText().toString();

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),ViewDisasterActivity.class);
				// sending pid to next activity
				in.putExtra(TAG_SID, sid);
				
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
	class LoadAllCharity extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllCharityActivity.this);
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
			JSONObject json = jParser.makeHttpRequest(url_all_charity, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All Charity: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// disasters found
					// Getting Array of disasters
					charity = json.getJSONArray(TAG_CHARITY);

					// looping through All disasters
					for (int i = 0; i < charity.length(); i++) {
						JSONObject c = charity.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_SID);
						String name = c.getString(TAG_NAME);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_SID, id);
						map.put(TAG_NAME, name);

						// adding HashList to ArrayList
						charityList.add(map);
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
							AllCharityActivity.this, charityList,
							R.layout.list_item, new String[] { TAG_SID,TAG_NAME},
							new int[] { R.id.pid, R.id.name });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}