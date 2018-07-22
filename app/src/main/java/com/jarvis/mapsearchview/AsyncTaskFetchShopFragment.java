package com.jarvis.mapsearchview;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncTaskFetchShopFragment {}

//extends AsyncTask<String, String, String> {

/*
    private SearchFragment.SearchFragmentCallBack mSearchFragmentCallBack;


    public AsyncTaskFetchShopFragment(SearchFragment.SearchFragmentCallBack callBack) {
        mSearchFragmentCallBack = callBack;


    }

    public static final HashMap<LatLng, Double> LATLON_FOR_SHOPS = new HashMap<>();




    JSONParser jp = new JSONParser();
    JSONObject jo;
    ProgressDialog pd;

    Double cLat = 79.345434, cLon = 71.434344;
    Double currentLat = 22.28504327, currentLon = 70.803855545;


    String success = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Fetching Data");
        pd.setCancelable(false);
        pd.show();

    }

    @Override
    protected String doInBackground(String... params) {

        List<NameValuePair> nv = new ArrayList<>();
        nv.add(new BasicNameValuePair("currentLat", params[0]));
        nv.add(new BasicNameValuePair("currentLon", params[1]));
        nv.add(new BasicNameValuePair("productItem", params[2]));
        //jo = jp.makeHttpRequest("http://192.168.43.11/jsonproject/select.php", nv);
        jo = jp.makeHttpRequest("http://www.rahulparmar.tk/demo/selectGeofences.php","POST", nv);

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
                    //           Toast.makeText(MainActivity.this, "HashMap Created", Toast.LENGTH_SHORT).show();
                    success="1";
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
            Toast.makeText(, "Data Successfully fetched", Toast.LENGTH_SHORT).show();
        } else { Toast.makeText(this, "Error in fetching data.", Toast.LENGTH_LONG).show();
        }

        mSearchFragmentCallBack.onNeededShopFetched();
    }
}}

*/