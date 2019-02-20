package wang.patrick.smarter;

import android.app.Activity;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapboxMap mMapboxMap;
    private Double lat;
    private Double lng;
    private String respost;
    private String resaddress;
    private Integer resid;
    private String currentresid;

    private MapView mMapView;
    private Spinner optionSpinner;
    View mView;
    List<Double> latlist = new ArrayList<>();
    List<Double> lnglist = new ArrayList<>();
    List<String> residlist = new ArrayList<>();

    Double TotaldaykWh = 0.0;

    private String restresult;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {



        MapboxAccountManager.start(getActivity().getApplicationContext());
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mMapView = mView.findViewById(R.id.mapquestMapView);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }


    }


    private void addMarker(MapboxMap mapboxMap, Double lat, Double lng, String title) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(lat, lng));
        markerOptions.title(title);
        mapboxMap.addMarker(markerOptions);

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(final MapboxMap mapboxMap) {

        mMapboxMap = mapboxMap;


        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                String result;
                String locresult;
                int i;
                try {
                    result = RestClient.findByResid("");
                    JSONArray jsonArray = new JSONArray(result);
                    for (i = 0; i < jsonArray.length(); i++) {


                        resaddress = jsonArray.getJSONObject(i).getString("resaddress");
                        respost = jsonArray.getJSONObject(i).getString("respostcode");
                        resid = Integer.parseInt(jsonArray.getJSONObject(i).getString("resid"));
                        Log.d("addresss======", resaddress);
                        locresult = WeatherAPI.getLocation(resaddress + " " + respost);
                        residlist.add(resid.toString());

                        JSONObject jsonObject = new JSONObject(locresult);
                        JSONArray jsonlocArray = jsonObject.getJSONArray("results");

                        lat =Double.valueOf(jsonlocArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat"));
                        latlist.add(lat);
                        lng = Double.valueOf(jsonlocArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng"));
                        lnglist.add(lng);


                    }

/*

                    result = WeatherAPI.getLocation("12342.");


                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    lat = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                    lng = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                    result = WeatherAPI.WeatherCall(lat,lng);
                    jsonObject = new JSONObject(result);

                    result =jsonObject.getJSONObject("main").getString("temp");
                    Double temp = Double.parseDouble(result);

                    result = String.valueOf(temp-273.15);

*/


                } catch (Exception e) {
                    e.printStackTrace();
                    result = "NO INFO FOUND";
                }

                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                for (int i = 0; i < latlist.size(); i++) {

                    addMarker(mMapboxMap, latlist.get(i), lnglist.get(i), residlist.get(i));

                }

            }
        }.execute();


        mMapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {


            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_LONG).show();
                optionSpinner = mView.findViewById(R.id.optionSpinner);
                IconFactory iconFactory = IconFactory.getInstance(getActivity());
                Icon redicon = iconFactory.fromResource(R.drawable.red);
                Icon greenicon = iconFactory.fromResource(R.drawable.green);

                currentresid = marker.getTitle();
                if (optionSpinner.getSelectedItem().toString().equals("Daily")) {



                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected String doInBackground(String... params) {

                            try {
                                restresult = RestClient.findDailykWhFromIdDate(currentresid);
                                Log.d("resresult =", restresult);
                                JSONObject jsonObject = new JSONObject(restresult);
                                TotaldaykWh = Double.valueOf(jsonObject.getString("fridge"))+ Double.valueOf(jsonObject.getString("AC"))+Double.valueOf(jsonObject.getString("wash"));

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                            return TotaldaykWh.toString();
                        }

                        @Override
                        protected void onPostExecute(String result) {
                        }
                    }.execute();



                } else {




                    new AsyncTask<String, Void, String>() {
                        @Override
                        protected String doInBackground(String... params) {

                            try {
                                restresult = RestClient.findHourlyUsagefromiddatehour(currentresid);
                                JSONArray jsonArray = new JSONArray(restresult);
                                TotaldaykWh = Double.valueOf(jsonArray.getJSONObject(0).getString("AC usage"))+ Double.valueOf(jsonArray.getJSONObject(0).getString("Washing M usage")) + Double.valueOf(jsonArray.getJSONObject(0).getString("Fridge usage"));

                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                            return TotaldaykWh.toString();
                        }

                        @Override
                        protected void onPostExecute(String result) {
                        }
                    }.execute();

                }

                if (optionSpinner.getSelectedItem().toString().equals("Daily")){
                    if (TotaldaykWh >=21){

                        marker.setIcon(redicon);
                    }
                    else{
                        marker.setIcon(greenicon);
                    }


                }else {
                    if (TotaldaykWh >=1.5){

                        marker.setIcon(redicon);
                    }
                    else{
                        marker.setIcon(greenicon);
                    }

                }



                marker.setSnippet(TotaldaykWh.toString());
                marker.showInfoWindow(mMapboxMap,mMapView);

                return true;
            }
        });


        mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-37.936168, 145.190159), 8));

    }
}
