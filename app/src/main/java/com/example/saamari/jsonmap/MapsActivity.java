package com.example.saamari.jsonmap;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private JSONArray kentat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng finland = new LatLng(63, 26);
        mMap.addMarker(new MarkerOptions().position(finland).title("Marker in Finland"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(finland));
        fetchData();
    }
    public void fetchData()
    {
        String url ="http://ptm.fi/materials/golfcourses/golf_courses.json";
        FetchDataTask task = new FetchDataTask();
        task.execute(url);

    }


        class FetchDataTask extends AsyncTask<String, Void, JSONObject> {
            @Override
            protected JSONObject doInBackground(String... urls) {
                HttpURLConnection urlConnection = null;
                JSONObject json = null;
                try {
                    URL url = new URL(urls[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    json = new JSONObject(stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
                return json;
            }

            protected void onPostExecute(JSONObject json) {
                StringBuffer text = new StringBuffer("");
                try {

                    kentat = json.getJSONArray("courses");
                    for (int i = 0; i < kentat.length(); i++) {
                        JSONObject kentta = kentat.getJSONObject(i);
                        text.append("kentta:" + kentta.getString("course") +  "\n");



                    }
                } catch (JSONException e) {
                    Log.e("JSON", "Error getting data.");


                }
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }


            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera. In this case,
             * we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to install
             * it inside the SupportMapFragment. This method will only be triggered once the user has
             * installed Google Play services and returned to the app.
             */



        }
    }




