package com.jarvis.mapsearchview;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.jarvis.mapsearchview.Geofence.GeofenceTransitionService;
import com.jarvis.mapsearchview.Location.MyLocationUpdatesService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMapFragment extends Fragment {

    static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.locationupdatespendingintent.action" +
                    ".PROCESS_UPDATES";
    private static final String TAG = MyMapFragment.class.getSimpleName();
    public static GoogleMap map;

    public static Marker geoFenceMarker;
    // Draw Geofence circle on GoogleMap
    public static Circle geoFenceLimits;
    public static ArrayList<Circle> geoFenceCircularBoundaryList;
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    /************************************************Markers on Map**************************************************/
    //private Marker geoFenceMarker;
    public static ArrayList<Marker> geoFenceDrawMarkerList;
    JSONParser jp = new JSONParser();
    JSONObject jo;
    ProgressDialog pd;
    private SupportMapFragment mapFragment;
    private Marker markersForGeofenceShops;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());
        //setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.activity_maps, container, false);

        mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container, mapFragment, "MyMapFrag");
        //fragmentTransaction.addToBackStack("MyMapFrag");
        fragmentTransaction.commit();

        return layout;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(locationReceiver);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.d(TAG, "onMapReady()");
                    map = googleMap;
                    map.setOnMapClickListener(
                            new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    Log.d(TAG, "onMapClick(" + latLng + ")");

                                    markerForGeofence(latLng);

                                }
                            }
                    );
                    map.setOnMarkerClickListener(
                            new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    Log.d(TAG, "onMarkerClickListener: " + marker.getPosition());

                                    return false;//because default behaviour should occur
                                    //the default behaviour is for the camera to move to the marker and an info window to appear
                                }
                            });
                }
            });
        }


        getActivity().registerReceiver(locationReceiver, new IntentFilter(ACTION_PROCESS_UPDATES));

    }

    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence(" + latLng + ")");


        String title = latLng.latitude + ", " + latLng.longitude;

        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);

        if (map != null) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = map.addMarker(markerOptions);

        }
        startGeofence(latLng);

    }

    private void startGeofence(LatLng latLng) {
        Log.i(TAG, "startGeofence()");
        if (geoFenceMarker != null) {
            Geofence geofence = createGeofence(geoFenceMarker.getPosition(), GEOFENCE_RADIUS);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(latLng, geofenceRequest);
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    public static final HashMap<LatLng, Double> LATLON_FOR_SHOPS = new HashMap<>();

    private void markersForGeofenceShops() {


        for (Map.Entry<LatLng, Double> ShopEntry : LATLON_FOR_SHOPS.entrySet()) {


            //Log.i(TAG, "markersForGeofenceShops(" + latLng + ")");

            LatLng latLng = new LatLng(ShopEntry.getKey().latitude, ShopEntry.getKey().longitude);
            //String title = latLng.latitude + ", " + latLng.longitude;
            String shopName = ShopEntry.getKey().latitude + ", " + ShopEntry.getKey().longitude + "," + ShopEntry.getValue();
            // Define marker options
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(shopName);

            Log.i(TAG, "startGeofence()");
            //if( geoFenceMarker != null ) {
            Geofence geofence = createGeofence(latLng, GEOFENCE_RADIUS);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(latLng, geofenceRequest);
            //} else {
            //    Log.e(TAG, "Geofence marker is null");
            //}


            if (map != null) {
                // Remove last geoFenceMarker
                //if (geoFenceMarker != null)
                //    geoFenceMarker.remove();

                markersForGeofenceShops = map.addMarker(markerOptions);

                //if ( geoFenceLimits != null )
                //    geoFenceLimits.remove();
/*
                CircleOptions circleOptions = new CircleOptions()
                        .center( latLng )
                        .strokeColor(Color.argb(50, 70,70,70))
                        .fillColor( Color.argb(100, 150,150,150) )
                        .radius( GEOFENCE_RADIUS );
                geoFenceLimits = map.addCircle( circleOptions );
*/
            }
            //editor.putLong(KEY_GEOFENCE_LAT, Double.doubleToRawLongBits(geofenceEntry.getValue().latitude));
            //editor.putLong(KEY_GEOFENCE_LON, Double.doubleToRawLongBits(geofenceEntry.getValue().longitude));

        }


    }

    /********************************************Geofence start ********************************************/

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence Request Id";
    //private static final float GEOFENCE_RADIUS = 500.0f; // in meters

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }


    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null) {
            return geoFencePendingIntent;
        } else {
            Intent intent = new Intent(getActivity(), GeofenceTransitionService.class);
            //Intent intent = new Intent(this, GeofenceTransitionService.class);
            return PendingIntent.getService(getActivity(), GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    private GeofencingClient mGeofencingClient;

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(final LatLng latLng, GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            //LocationServices.GeofencingApi.addGeofences(googleApiClient, request, createGeofencePendingIntent()).setResultCallback(this);
            // mGeofencingClient.requestLocationUpdates(mLocationRequest, getLocationPendingIntent());
            mGeofencingClient.addGeofences(request, createGeofencePendingIntent()).addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    CircleOptions circleOptions = new CircleOptions()
                            .center(latLng)
                            .strokeColor(Color.argb(50, 70, 70, 70))
                            .fillColor(Color.argb(100, 150, 150, 150))
                            .radius(GEOFENCE_RADIUS);
                    geoFenceLimits = map.addCircle(circleOptions);

                }
            });

    }


    /********************************************************************************************************/

    public void OnReceiveItemFromSerachFragUpdateView(String item) {
        new FetchGeofences().execute(currentLat.toString(), currentLon.toString(), userRadiusInKm.toString(), item);
    }


    /*

        private void onSearchForProductShopM() {
            AsyncTaskFetchShopFragment asyncTaskFetchShopFragment = new
                    AsyncTaskFetchShopFragment(new MyMapFragmentCallBack() {
                @Override
                public void onNeededShopFetchedM() {
                    //NowAddMarkerToMap()
                }});
            asyncTaskFetchShopFragment.execute();
        }

    */
    //NowAddMarkerToMap(){
    //
    //
    // };
    public interface MyMapFragmentCallBack {


        void onNeededShopFetchedM();
    }

    private class FetchGeofences extends AsyncTask<String, String, String> {
        String success = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Fetching Data");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {


            List<NameValuePair> nv = new ArrayList<>();
            nv.add(new BasicNameValuePair("currentLat", params[0]));
            nv.add(new BasicNameValuePair("currentLon", params[1]));
            nv.add(new BasicNameValuePair("userRadiusInKm", params[2]));
            nv.add(new BasicNameValuePair("productItem", params[3]));
            //jo = jp.makeHttpRequest("http://192.168.43.11/jsonproject/select.php", nv);
            //jo = jp.makeHttpRequest("http://www.rahulparmar.tk/demo/selectGeofences.php", "POST", nv);
            jo = jp.makeHttpRequest("http://192.168.43.11/demo/selectGeofences.php", "POST", nv);

            if (jo != null) {


                try {

                    JSONArray jsonArray = jo.getJSONArray("fetchedData");
                    ArrayList<String> jsonResultFromPhp = new ArrayList<>();


                    for (int i = 0; i <= jsonArray.length(); i++) {

                        JSONObject Jobj = jsonArray.getJSONObject(i);

                        Double SLat = Jobj.getDouble("ShopLat");
                        Double SLon = Jobj.getDouble("ShopLon");
                        Double SDistance = Jobj.getDouble("Distance");

                        LATLON_FOR_SHOPS.put(new LatLng(SLat, SLon), SDistance);

                        success = "1";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return success;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            pd.dismiss();

            if (s.equals("1")) {

                Toast.makeText(getActivity(), "Data Successfully fetched", Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                MyMapFragment myMapFrag = (MyMapFragment) getFragmentManager().findFragmentByTag("MyMapFrag");
                SearchFragment searchFrag = (SearchFragment) getFragmentManager().findFragmentByTag("MyListViewFrag");

                //if(myMapFrag.isAdded()){
                if (myMapFrag != null) {
                    //ft.hide(searchFrag).commit();
                    ft.show(myMapFrag).commit();
                    //             Toast.makeText(getActivity(), "map show", Toast.LENGTH_SHORT).show();
                }

                markersForGeofenceShops();


            } else {
                Toast.makeText(getActivity(), "Error in fetching data.", Toast.LENGTH_LONG).show();
            }


        }
    }


    Double cLat = 79.345434, cLon = 71.434344;
    //Double currentLat = 22.28504327, currentLon = 70.803855545, userRadiusInKm = 1.0;
    Double currentLat, currentLon, userRadiusInKm = 1.0;


    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt("done");
                if (resultCode == 1) {
                    Double latitude = bundle.getDouble("latitude");
                    Double longitude = bundle.getDouble("longitude");

                    currentLat = latitude;
                    currentLon = longitude;
                    LatLng latLng = new LatLng(latitude, longitude);
                    markerForUsersCurrentLocation(latLng);
                }
            }
        }
    };


    private Marker locationMarker;

    private void markerForUsersCurrentLocation(LatLng latLng) {

        Log.i(TAG, "markerLocation(" + latLng + ")");

        String title = latLng.latitude + ", " + latLng.longitude;

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);

        if (map != null) {
            if (locationMarker != null)
                locationMarker.remove();

            locationMarker = map.addMarker(markerOptions);

            float zoom = 15f;

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);

            map.animateCamera(cameraUpdate);


        }
    }


}
