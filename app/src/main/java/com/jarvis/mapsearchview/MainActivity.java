package com.jarvis.mapsearchview;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.jarvis.mapsearchview.Location.MyLocationUpdatesService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchFragment.OnItemSelectedListener {


    JSONParser jp = new JSONParser();
    JSONObject jo;
    ProgressDialog pd;

    Double cLat = 79.345434, cLon = 71.434344;
    Double currentLat = 22.28504327, currentLon = 70.803855545;

    @Override
    public void callonItemSelectedListener(String item) {
        //this.onBackPressed();
    //super.onBackPressed();
        //    MyMapFragment mMyMapFragment = new MyMapFragment();
        //      Bundle arguments = new Bundle();
        MyMapFragment updateMap = (MyMapFragment) getSupportFragmentManager().findFragmentByTag("MyMapFrag");
        if (updateMap != null) {

            updateMap.OnReceiveItemFromSerachFragUpdateView(item);
        } else {
            Toast.makeText(this, "Unable to update", Toast.LENGTH_SHORT).show();
        }
//        new FetchGeofences().execute(currentLat.toString(), currentLon.toString(), item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        textLat = (TextView) findViewById(R.id.lat);
        textLong = (TextView) findViewById(R.id.lon);


        displayView(0);
        Fragment mMapFragment = new MyMapFragment();
        //FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.add(R.id.fragment_container, mMapFragment, "MyMapFrag");
            //transaction.addToBackStack("MyMapFrag");
            // Commit the transaction
            transaction.commit();

            //fragmentManager.beginTransaction().replace(R.id.content_frame, mMapFragment).commit();
            //fragmentManager.beginTransaction().replace(R.id.map, mMapFragment).commit();
            //fragmentManager.beginTransaction().add(R.id.map, mMapFragment).commit();
        }

        startService(new Intent(this, MyLocationUpdatesService.class));

    }

    static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.locationupdatespendingintent.action" +
                    ".PROCESS_UPDATES";

    private TextView textLat, textLong;
    public BroadcastReceiver receiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if (ACTION_PROCESS_UPDATES.equals(action)) {

                    Toast.makeText(MainActivity.this, "received to receiver",Toast.LENGTH_SHORT).show();
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        int resultCode = bundle.getInt("done");
                        if (resultCode == 1) {
                            Double latitude = bundle.getDouble("latitude");

                            Double longitude = bundle.getDouble("longitude");

                            textLat.setText(latitude.toString());
                            textLong.setText(longitude.toString());
                        }
                    }
                }
            }
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver, new IntentFilter(ACTION_PROCESS_UPDATES));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Resume",Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Pause",Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



























    private Fragment fragment = null;
    private FragmentManager fragmentManager;

    private void displayView(int position) {
        fragment = null;
        String fragmentTags = "";
        switch (position) {
            case 0:
                fragment = new SearchFragment();

                break;

            default:
                break;
        }

        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment, "MyListViewFrag")
                    //.addToBackStack("MyListViewFrag")
                    .commit();
        }
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "ActionSearch", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
