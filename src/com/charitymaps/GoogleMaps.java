package com.charitymaps;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMaps extends Activity {
    private GoogleMap googleMap;
    private int mapType = GoogleMap.MAP_TYPE_NORMAL;
    
    private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	private static final String TAG_LAT = "lat";
	private static final String TAG_LONG = "long";
	
	private JSONArray places;
	private HashMap<String, String> mapPlaceToId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment =  (MapFragment) fragmentManager.findFragmentById(R.id.map);
        googleMap = mapFragment.getMap();

        /////////////////////// STANDAARD MARKERS ///////////////////////
        LatLng sfLatLng = new LatLng(37.7750, -122.4183);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions()
                .position(sfLatLng)
                .title("San Francisco")
                .snippet("Population: 776733")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        LatLng sLatLng = new LatLng(37.857236, -122.486916);
        googleMap.addMarker(new MarkerOptions()
                .position(sLatLng)
                .title("Sausalito")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        
        LatLng cameraLatLng = new LatLng(0, 0);
        
        /////////////////////// DATABASE MARKERS ///////////////////////
        /*LatLng cameraLatLng = new LatLng(0, 0);
        try {
			cameraLatLng = new LatLng(Double.parseDouble(places.getJSONObject(0).getString(TAG_LAT)),
			Double.parseDouble(places.getJSONObject(0).getString(TAG_LONG)));
			
			for (int i = 0; i < places.length(); i++) {
				JSONObject c = places.getJSONObject(i);
				String title = c.getString(TAG_NAME);
				String lat = c.getString(TAG_LAT);
				String lon = c.getString(TAG_LONG);
				String mid = c.getString(TAG_PID);
				LatLng loc = new LatLng(0, 0);
				try {
					loc = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
				} catch (NumberFormatException nfe) {
					continue;
				}
				mapPlaceToId.put(title, mid);
				googleMap
						.addMarker(new MarkerOptions()
								.position(loc)
								.title(title)
								.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		*/
        
        
        
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);


        //LatLng cameraLatLng = sfLatLng;
        float cameraZoom = 10;

        if(savedInstanceState != null){
            mapType = savedInstanceState.getInt("map_type", GoogleMap.MAP_TYPE_NORMAL);

            double savedLat = savedInstanceState.getDouble("lat");
            double savedLng = savedInstanceState.getDouble("lng");
            cameraLatLng = new LatLng(savedLat, savedLng);

            cameraZoom = savedInstanceState.getFloat("zoom", 10);
        }

        googleMap.setMapType(mapType);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraLatLng, cameraZoom));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.normal_map:
                mapType = GoogleMap.MAP_TYPE_NORMAL;
                break;

            case R.id.satellite_map:
                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                break;

            case R.id.terrain_map:
                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                break;

            case R.id.hybrid_map:
                mapType = GoogleMap.MAP_TYPE_HYBRID;
                break;
        }

        googleMap.setMapType(mapType);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the map type so when we change orientation, the mape type can be restored
        LatLng cameraLatLng = googleMap.getCameraPosition().target;
        float cameraZoom = googleMap.getCameraPosition().zoom;
        outState.putInt("map_type", mapType);
        outState.putDouble("lat", cameraLatLng.latitude);
        outState.putDouble("lng", cameraLatLng.longitude);
        outState.putFloat("zoom", cameraZoom);
    }
}