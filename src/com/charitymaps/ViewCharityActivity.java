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

public class ViewCharityActivity extends Activity {

	TextView txtName;
	TextView txtWebsite;
	TextView txtDesc;
	TextView txtCreatedAt;

	String sid;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single charity url
	private static final String url_charity_detials = "http://www.gowithus.nl/projects/android/v2/get_charity_details.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_CHARITY = "charity";
	private static final String TAG_SID = "sid";
	private static final String TAG_NAME = "name";
	private static final String TAG_WEBSITE = "website";
	private static final String TAG_DESCRIPTION = "description";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_charity);

		// BELANGRIJK!!
		if (android.os.Build.VERSION.SDK_INT > 9) {
		      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		      StrictMode.setThreadPolicy(policy);
		}
		
		// getting charity details from intent
		Intent i = getIntent();
		
		// getting charity id (sid) from intent
		sid = i.getStringExtra(TAG_SID);

		// Getting complete charity details in background thread
		new GetCharityDetails().execute();

	}

	/**
	 * Background Async Task to Get complete charity details
	 * */
	class GetCharityDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ViewCharityActivity.this);
			pDialog.setMessage("Loading, please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting charity details in background thread
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
						params.add(new BasicNameValuePair("sid", sid));

						// getting charity details by making HTTP request
						// Note that charity details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(url_charity_detials, "GET", params);

						// check your log for json response
						Log.d("Single charity Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received charity details
							JSONArray charityObj = json.getJSONArray(TAG_CHARITY); // JSON Array
							
							// get first charity object from JSON Array
							JSONObject charity = charityObj.getJSONObject(0);

							// charity with this pid found
							// Edit Text
							txtName = (TextView) findViewById(R.id.inputName);
							txtWebsite = (TextView) findViewById(R.id.inputLocation);
							txtDesc = (TextView) findViewById(R.id.inputDesc);

							// display charity data in EditText
							txtName.setText(charity.getString(TAG_NAME));
							txtWebsite.setText(charity.getString(TAG_WEBSITE));
							txtDesc.setText(charity.getString(TAG_DESCRIPTION));

						}else{
							// charity with pid not found
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
