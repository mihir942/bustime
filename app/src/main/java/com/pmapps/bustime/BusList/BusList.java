package com.pmapps.bustime.BusList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pmapps.bustime.FavDB.FavBusStop;
import com.pmapps.bustime.FavDB.FavBusStopDatabase;
import com.pmapps.bustime.Logic.MyComparator;
import com.pmapps.bustime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pmapps.bustime.Logic.Logic.deckerForAPIHit;
import static com.pmapps.bustime.Logic.Logic.loadForAPIHit;
import static com.pmapps.bustime.Logic.Logic.timeForAPIHit;

public class BusList extends AppCompatActivity implements RefreshClickInterface {

    // CONSTANTS
    private static final String HEADER_ACCOUNTKEY = "AccountKey";
    private static final String HEADER_JSONFORMAT = "accept";
    private static final String VALUE_ACCOUNTKEY = "nlozX6xbTe2a0U2q5jZplQ==";
    private static final String VALUE_JSONFORMAT = "application/json";
    private static final String LTA_URL = "http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?";

    private static final String TIH_URL_busServiceByBusStop = "https://tih-api.stb.gov.sg/transport/v1/bus_service/bus_stop/";
    private static final String TIH_API_KEY = "07D5qREwcfI7lVAe4CZsavbGjuv1jqcV";

    // Variables
    private BusListAdapter mBusListAdapter;
    private ArrayList<BusItem> mBusList;
    private List<String> listOfBuses;

    private RequestQueue mRequestQueue;
    private String busStopTitle;
    private String busStopCode;
    private String busStopRoad;

    // Views
    private RecyclerView mRecyclerView;
    private ImageButton addToFavButton;

    // Database
    public static FavBusStopDatabase favBusStopDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_list);

        // Database
        favBusStopDatabase = Room.databaseBuilder(getApplicationContext(), FavBusStopDatabase.class, "favlistdb").allowMainThreadQueries().build();

        // functions
        fetchDataFromIntent();
        initiateViews();
        initiateDataStuff();
        getBusNumbers();
    }

    private void fetchDataFromIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            busStopTitle = bundle.getString("BusStopTitle");
            busStopCode = bundle.getString("BusStopCode");
            busStopRoad = bundle.getString("BusStopRoad");
        }
    }

    private void initiateViews() {
        // Views
        TextView busStopTitle_TextView = findViewById(R.id.busStopTitle_TextView);
        busStopTitle_TextView.setText(busStopTitle);
        addToFavButton = findViewById(R.id.addToFavButton);
        addToFavButton.setOnClickListener(v -> addToFavourites());


        if (inFavourites())
            addToFavButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favourite, null));
        else
            addToFavButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_outlined, null));

        // RecyclerView initialisation
        mRecyclerView = findViewById(R.id.busList_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation()));
    }

    private void initiateDataStuff() {
        // initialise Bus List
        mBusList = new ArrayList<>();
        listOfBuses = new ArrayList<>();

        // Initialise Volley request queue
        mRequestQueue = Volley.newRequestQueue(this);

        // Adapter will grab any data in list and put it into the recyclerview it's attached to
        // RecyclerView <------- Adapter <------- List
        mBusListAdapter = new BusListAdapter(this, mBusList, this);
        mRecyclerView.setAdapter(mBusListAdapter);
    }

    private void getBusNumbers() {
        // get list of buses from TIH
        final String URL_1 = TIH_URL_busServiceByBusStop + busStopCode + "?apikey=" + TIH_API_KEY;
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(URL_1, response -> {

            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject busHit = jsonArray.getJSONObject(i);
                    String busNumber = busHit.getString("number");
                    if (!listOfBuses.contains(busNumber)) {
                        listOfBuses.add(busNumber);
                        BusItem busItem = new BusItem(busNumber);
                        mBusList.add(busItem);
                    }
                }

                // Sort (it works!)
                Collections.sort(mBusList, new MyComparator());
                for (BusItem e : mBusList) {
                    Log.d("BUS", e.getBusNumber());
                }

                mBusListAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                // will never catch an exception unless there's literally no buses at the bus stop.
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        mRequestQueue.add(jsonObjectRequest1);
    }

    // get bus timings
    @Override
    public void onRefreshClick(int position, ImageButton refreshButton) {

        Animation rotateAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        refreshButton.startAnimation(rotateAnim);

        BusItem busItem = mBusList.get(position);
        String busNumber = busItem.getBusNumber();
        final String URL_2 = LTA_URL + "BusStopCode=" + busStopCode + "&ServiceNo=" + busNumber;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_2, response -> {
            try {

                JSONArray jsonArray = response.getJSONArray("Services");
                JSONObject busHit = jsonArray.getJSONObject(0);

                //nextbus1
                JSONObject nextBus1 = busHit.getJSONObject("NextBus");
                String EstimatedArrival1 = nextBus1.getString("EstimatedArrival");
                String Load1 = nextBus1.getString("Load");
                String Type1 = nextBus1.getString("Type");
                busItem.setBus_time_1(timeForAPIHit(EstimatedArrival1));
                busItem.setBus_load_1(loadForAPIHit(Load1));
                busItem.setBus_decker_1(deckerForAPIHit(Type1));

                try {
                    //nextbus2
                    JSONObject nextBus2 = busHit.getJSONObject("NextBus2");
                    String EstimatedArrival2 = nextBus2.getString("EstimatedArrival");
                    String Load2 = nextBus2.getString("Load");
                    String Type2 = nextBus2.getString("Type");
                    busItem.setBus_time_2(timeForAPIHit(EstimatedArrival2));
                    busItem.setBus_load_2(loadForAPIHit(Load2));
                    busItem.setBus_decker_2(deckerForAPIHit(Type2));

                    try {
                        //nextbus3
                        JSONObject nextBus3 = busHit.getJSONObject("NextBus3");
                        String EstimatedArrival3 = nextBus3.getString("EstimatedArrival");
                        String Load3 = nextBus3.getString("Load");
                        String Type3 = nextBus3.getString("Type");
                        busItem.setBus_time_3(timeForAPIHit(EstimatedArrival3));
                        busItem.setBus_load_3(loadForAPIHit(Load3));
                        busItem.setBus_decker_3(deckerForAPIHit(Type3));

                        busItem.setArrTimesHaveBeenSet(true);
                        mBusListAdapter.notifyItemChanged(position);

                    } catch (StringIndexOutOfBoundsException e) {
                        busItem.setBus_time_3("--");
                        busItem.setArrTimesHaveBeenSet(true);
                        mBusListAdapter.notifyItemChanged(position);
                    }


                } catch (StringIndexOutOfBoundsException e) {
                    busItem.setBus_time_2("--");
                    busItem.setBus_time_3("--");
                    busItem.setArrTimesHaveBeenSet(true);
                    mBusListAdapter.notifyItemChanged(position);
                }

            } catch (JSONException e) {
                busItem.setBus_time_1("--");
                busItem.setBus_time_2("--");
                busItem.setBus_time_3("--");
                busItem.setArrTimesHaveBeenSet(true);
                mBusListAdapter.notifyItemChanged(position);
            }

            //refreshButton.clearAnimation();

        }, Throwable::printStackTrace) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(HEADER_ACCOUNTKEY, VALUE_ACCOUNTKEY);
                headers.put(HEADER_JSONFORMAT, VALUE_JSONFORMAT);
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        mRequestQueue.add(jsonObjectRequest);

    }


    // deals with favourites
    private void addToFavourites() {
        Drawable drawable = addToFavButton.getDrawable();

        Drawable outline = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_heart_outlined, null);
        Drawable filled = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favourite, null);

        if (inFavourites()) {

            //delete from database
            favBusStopDatabase.busStopDao().deleteBusStopWithCode(busStopCode);
            addToFavButton.setImageDrawable(outline);
        } else {

            //insert into database
            FavBusStop favBusStop = new FavBusStop(busStopTitle, busStopCode, busStopRoad);
            favBusStopDatabase.busStopDao().addFavBusStop(favBusStop);
            addToFavButton.setImageDrawable(filled);
        }
    }

    private Boolean inFavourites() {
        return favBusStopDatabase.busStopDao().exists(busStopCode) > 0;
    }

}