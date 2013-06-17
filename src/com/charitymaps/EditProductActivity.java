package com.charitymaps;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.charitymaps.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class EditProductActivity extends Activity {

	TextView txtName;
	TextView txtLocation;
	TextView txtDesc;
	TextView txtCreatedAt;
	Button btnSave;
	Button btnDelete;

	String pid;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single disaster url
	private static final String url_disaster_detials = "http://www.gowithus.nl/projects/android/v2/get_disaster_details.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_DISASTER = "disaster";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_DESCRIPTION = "description";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_disaster);

		// BELANGRIJK!!
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		}
		
		// getting disaster details from intent
		Intent i = getIntent();
		
		// getting disaster id (pid) from intent
		pid = i.getStringExtra(TAG_PID);

		// Getting complete disaster details in background thread
		new GetDisasterDetails().execute();

	}

	/**
	 * Background Async Task to Get complete disaster details
	 * */
	class GetDisasterDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditProductActivity.this);
			pDialog.setMessage("Loading, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting disaster details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					int success;
					try {
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("pid", pid));

						// getting disaster details by making HTTP request
						// Note that disaster details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_disaster_detials, "GET", params);

						// check your log for json response
						Log.d("Single Disaster Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received disaster details
							JSONArray disasterObj = json
									.getJSONArray(TAG_DISASTER); // JSON Array
							
							// get first disaster object from JSON Array
							JSONObject disaster = disasterObj.getJSONObject(0);

							// disaster with this pid found
							// Edit Text
							txtName = (TextView) findViewById(R.id.inputName);
							txtLocation = (TextView) findViewById(R.id.inputLocation);
							txtDesc = (TextView) findViewById(R.id.inputDesc);

							// display disaster data in EditText
							txtName.setText(disaster.getString(TAG_NAME));
							txtLocation.setText(disaster.getString(TAG_LOCATION));
							txtDesc.setText(disaster.getString(TAG_DESCRIPTION));

						}else{
							// disaster with pid not found
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}


		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
		}

	}
}
