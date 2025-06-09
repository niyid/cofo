package com.vasworks.cofo.android;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.vasworks.android.util.LinkedHashMapAdapter;
import com.vasworks.xmlrpc.XMLRPCClient;
import com.vasworks.xmlrpc.XMLRPCException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {
	public static final String LOG_TAG = "MainActivity";

	private ProgressDialog progressDialog;
	
	private final String url = "http://192.168.43.99:8069", db = "bayelsa_gov", password = "passw0rd123";
	
	private int userId = 1;
	
	private List<Double[]> polygonVertices;

	private LocationManager locationManager;
	
	private MyLocationListener myLocationListener = new MyLocationListener();

	private float MEAN_DISTANCE_FOR_UPDATES = 0.5f;
	
	private Location currentLocation;
	
	private Button doneButton;
	
	private Button clearButton;
	
	private MapView mapView;
	
	private Spinner schedule;
	
	private GoogleMap map;
	
	private Integer selectedScheduleId;
	
	private LinkedHashMap<Integer, String> scheduleMapData;
	
	@Override
	public void onResume() {
		mapView.onResume();
	    super.onResume();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loadSchedule(savedInstanceState);

		progressDialog = new ProgressDialog(this);
		
		polygonVertices = new ArrayList<Double[]>();
		
		schedule = (Spinner) findViewById(R.id.schedule);
		
		clearButton = (Button) findViewById(R.id.btnClear);
		
		doneButton = (Button) findViewById(R.id.btnDone);
		
		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(selectedScheduleId == null) {
					displayAlert("'Schedule' is required.", "Validation Error", false);
				} else if(polygonVertices.size() <= 2) {
					displayAlert("More survey points required.", "Validation Error", false);
				} else if(polygonVertices.size() > 2) {
					Double[] d = polygonVertices.get(0);
					Double[] lastVertex = polygonVertices.get(polygonVertices.size() - 1);
					map.addPolyline(new PolylineOptions()
							    .add(new LatLng(lastVertex[0], lastVertex[1]), new LatLng(d[0], d[1]))
							    .width(5)
							    .color(Color.RED));
					polygonVertices.add(d);			
					new SaveSurveyTask().execute();
				}
			}
		});
		
		currentLocation = getLastKnownLocation();
		
		locationManager.requestLocationUpdates(

		LocationManager.GPS_PROVIDER, 0, MEAN_DISTANCE_FOR_UPDATES,	myLocationListener);
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Log.i(LOG_TAG, "GPS not enabled.");
			showGPSDisabledAlertToUser();
		}
		
		mapView = (MapView) findViewById(R.id.surveyMap);
		mapView.onCreate(savedInstanceState);
 
		map = mapView.getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setAllGesturesEnabled(true);

		MapsInitializer.initialize(this);
		
		Log.i(LOG_TAG, "currentLocation=" + currentLocation);
		LatLng loc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, 14);
		map.animateCamera(cameraUpdate);
		map.moveCamera(cameraUpdate);
	}
	
	private Location getLastKnownLocation() {
	    locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
	    List<String> providers = locationManager.getProviders(true);
	    Location bestLocation = null;
	    for (String provider : providers) {
	        Location l = locationManager.getLastKnownLocation(provider);
	        if (l == null) {
	            continue;
	        }
	        if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
	            // Found best last known location: %s", l);
	            bestLocation = l;
	        }
	    }
	    return bestLocation;
	}
	
	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?").setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(callGPSSettingIntent);
					}
				});
		alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	private void displayAlert(CharSequence charSequence, CharSequence charSequence2, final boolean exit) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this).setTitle(charSequence2).setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						if (exit) {
							finish();
						} else {
							dialog.cancel();
						}
					}
				});
		alertDialog.setMessage(charSequence).show();
	}
	
	private String sendSurvey() {		
		XMLRPCClient models;
		String msg = null;
		try {
			models = new XMLRPCClient(new URL(String.format("%s/xmlrpc/2/object", url)));
			Object val = models.call("execute_kw", db, userId, password, "lis.cofo", "import_polygon", new Object[]{selectedScheduleId, serializeVertices(polygonVertices)});
			Log.i(LOG_TAG, "Return val=" + val);
			polygonVertices = new ArrayList<Double[]>();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			msg = e.getMessage();
		} catch (XMLRPCException e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		
		return msg;
	}
	
	private void clearMap() {
		map.clear();
		polygonVertices.clear();
		clearButton.setEnabled(false);
		doneButton.setEnabled(false);
	}

	private String serializeVertices(List<Double[]> vertices) {
		StringBuilder b = new StringBuilder("((");
		
		int count = 0;
		for(Double[] point : vertices) {
			if(count > 0) {
				b.append(", ");
			}
			b.append(point[0]);
			b.append(" ");
			b.append(point[1]);
			count++;
		}
		b.append("))");
		
		return b.toString();
	}
	
	public void markVertex(View v) {
		Double[] d = new Double[2];
		d[0] = currentLocation.getLatitude();
		d[1] = currentLocation.getLongitude();
		
		if(polygonVertices.size() > 0) {
			Double[] lastVertex = polygonVertices.get(polygonVertices.size() - 1);
			map.addPolyline(new PolylineOptions()
				     .add(new LatLng(lastVertex[0], lastVertex[1]), new LatLng(d[0], d[1]))
				     .width(5)
				     .color(Color.RED));
			map.addMarker(new MarkerOptions().position(new LatLng(d[0], d[1])).icon(BitmapDescriptorFactory.fromResource(R.drawable.dot_red)));
		} else {
			map.addMarker(new MarkerOptions().position(new LatLng(d[0], d[1])).title("Start"));
		}
		
		polygonVertices.add(d);
		if(polygonVertices.size() > 1) {
			doneButton.setEnabled(true);
		}
		clearButton.setEnabled(true);
	}
	
	public void clearVertices(View v) {
		clearMap();
	}
	
	private void loadSchedule(Bundle savedInstanceState) {
		if(savedInstanceState == null || scheduleMapData == null) {
			scheduleMapData = new LinkedHashMap<Integer, String>();
		    scheduleMapData.put(11, "Schedule Item 11");
		    scheduleMapData.put(12, "Schedule Item 12");
		    scheduleMapData.put(13, "Schedule Item 13");
		    scheduleMapData.put(14, "Schedule Item 14");
		    scheduleMapData.put(15, "Schedule Item 15");
		    scheduleMapData.put(16, "Schedule Item 16");
		    scheduleMapData.put(17, "Schedule Item 17");
		    scheduleMapData.put(18, "Schedule Item 18");
		    scheduleMapData.put(19, "Schedule Item 19");
		    scheduleMapData.put(20, "Schedule Item 20");
		}

	    LinkedHashMapAdapter<Integer, String> adapter = new LinkedHashMapAdapter<Integer, String>(this, android.R.layout.simple_spinner_item, scheduleMapData);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    schedule = (Spinner) findViewById(R.id.schedule);
	    schedule.setAdapter(adapter);
	    schedule.setOnItemSelectedListener(this);		
	}

	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
			currentLocation = location;
		}

		public void onStatusChanged(String s, int i, Bundle b) {
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(MainActivity.this, "GPS turned off.", Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(MainActivity.this, "GPS turned on.",	Toast.LENGTH_LONG).show();
		}
	}

	private class SaveSurveyTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog.setMessage("Working...");
			progressDialog.show();

		}

		@Override
		protected String doInBackground(Void... params) {
			Log.d(LOG_TAG, "doInBackground()");
			return sendSurvey();
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			if(result != null) {
				displayAlert(result, "Exception", false);
			} else {
				clearMap();
				displayAlert("Survey data saved.", "Success", false);
				scheduleMapData.remove(selectedScheduleId);
			}
		}

	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putSerializable("schedule", scheduleMapData);
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	@SuppressWarnings("unchecked")
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		scheduleMapData = (LinkedHashMap<Integer, String>) savedInstanceState.get("schedule");
		selectedScheduleId = (Integer) schedule.getItemAtPosition(0);
	    super.onRestoreInstanceState(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    	schedule.setSelection(position);
    	selectedScheduleId = ((Entry<Integer, String>) schedule.getSelectedItem()).getKey();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
