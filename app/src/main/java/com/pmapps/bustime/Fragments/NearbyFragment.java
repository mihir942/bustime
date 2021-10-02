package com.pmapps.bustime.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pmapps.bustime.BusList.BusList;
import com.pmapps.bustime.BusStopList.BusStopClickInterface;
import com.pmapps.bustime.BusStopList.BusStopAdapter;
import com.pmapps.bustime.BusStopList.BusStopItem;
import com.pmapps.bustime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NearbyFragment extends Fragment implements BusStopClickInterface {

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // CONSTANTS
    private static final String JSON_URL = "https://tih-api.stb.gov.sg/transport/v1/bus_stop?location=";
    private static final String API_KEY = "07D5qREwcfI7lVAe4CZsavbGjuv1jqcV";
    private static final String COMMA = "%2C";
    private static final short RADIUS = 50;
    private static final byte LIMIT_STOPS_IN_LIST = 25;
    private static final int MIN_TIME_BTW_LOC_UPDATES = 60000; // 1 MIN = 60 S = 60,000 MS

    // variables to work with
    private BusStopAdapter mBusStopAdapter;
    private ArrayList<BusStopItem> mBusStopList;
    private RequestQueue mRequestQueue;
    private Double latitude, longitude;
    LocationManager locationManager;

    // Views
    private RecyclerView mRecyclerView;
    private ImageButton refreshButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nearby, container, false);

        initiateViews(v);
        initiateDataStuff();

        return v;
    }

    private void initiateViews(@NonNull View v) {
        // refresh button (top right)
        refreshButton = v.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v1 -> updateLocation());

        // RecyclerView initialisation
        mRecyclerView = v.findViewById(R.id.nearbyStops_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation()));
    }

    private void initiateDataStuff() {
        // initialise Bus Stop List
        mBusStopList = new ArrayList<>();

        // Update Location as soon as the Nearby Fragment has been selected
        updateLocation();

        // Initialise Volley request queue
        mRequestQueue = Volley.newRequestQueue(requireActivity());

        // Adapter will grab any data in list and put it into the recyclerview it's attached to
        // RecyclerView <------- Adapter <------- List
        mBusStopAdapter = new BusStopAdapter(requireActivity(), mBusStopList, this);
        mRecyclerView.setAdapter(mBusStopAdapter);
    }

    private void updateLocation() {

        boolean permission_granted = ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!permission_granted) {
            mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        Log.d("PERMISSION RESULT", "permission already granted");

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BTW_LOC_UPDATES, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BTW_LOC_UPDATES, 0, locationListener);
    }

    private void parseJsonAndUpdateData() {

        final String URL = JSON_URL + latitude + COMMA + longitude + "&radius=" + RADIUS + "&apikey=" + API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < LIMIT_STOPS_IN_LIST; i++) {
                    JSONObject bus_stop_hit = jsonArray.getJSONObject(i);
                    String busStopTitle = bus_stop_hit.getString("description");
                    String busStopRoad = bus_stop_hit.getString("roadName");
                    String busStopCode = bus_stop_hit.getString("code");
                    mBusStopList.add(new BusStopItem(busStopTitle, busStopRoad, busStopCode));
                }
                mBusStopAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        mRequestQueue.add(jsonObjectRequest);
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //1. Empty the list
            //2. Notify Data Set Changed
            mBusStopList.clear();
            mBusStopAdapter.notifyDataSetChanged();

            //3. Populate the list
            //4. Notify Data Set Changed
            parseJsonAndUpdateData();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (!result) {
            Log.d("PERMISSION RESULT 2", "permission denied");
            System.exit(0);
        } else {
            Log.d("PERMISSION RESULT 2", "permission accepted");
            updateLocation();
        }
    });

    // BusStopClickInterface: onItemClick
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(requireActivity(), BusList.class);
        String busStopTitle = mBusStopList.get(position).getBusStopTitle();
        String busStopCode = mBusStopList.get(position).getBusStopCode();
        String busStopRoad = mBusStopList.get(position).getBusStopRoad();

        intent.putExtra("BusStopTitle", busStopTitle);
        intent.putExtra("BusStopCode", busStopCode);
        intent.putExtra("BusStopRoad", busStopRoad);
        startActivity(intent);
    }
}