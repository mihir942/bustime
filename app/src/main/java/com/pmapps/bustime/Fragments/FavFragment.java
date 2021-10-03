package com.pmapps.bustime.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pmapps.bustime.BusList.BusList;
import com.pmapps.bustime.BusStopList.BusStopClickInterface;
import com.pmapps.bustime.FavDB.FavBusStop;
import com.pmapps.bustime.FavDB.FavBusStopAdapter;
import com.pmapps.bustime.FavDB.FavBusStopDatabase;
import com.pmapps.bustime.R;

import java.util.ArrayList;
import java.util.List;

public class FavFragment extends Fragment implements BusStopClickInterface {

    public FavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // variables to work with
    private FavBusStopAdapter mFavBusStopAdapter;
    private List<FavBusStop> mFavBusStopList;

    // Views
    private RecyclerView mRecyclerView;

    // Database
    public static FavBusStopDatabase favBusStopDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fav, container, false);

        Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show();

        // Database
        favBusStopDatabase = Room.databaseBuilder(requireActivity(), FavBusStopDatabase.class, "favlistdb").allowMainThreadQueries().build();

        // functions
        initiateViews(v);
        initiateDataStuff();
        populateRecyclerView();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateRecyclerView();
    }

    private void initiateViews(@NonNull View v) {
        mRecyclerView = v.findViewById(R.id.favStops_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), linearLayoutManager.getOrientation()));
    }

    private void initiateDataStuff() {
        // initialise fav Bus Stop List
        mFavBusStopList = new ArrayList<>();

        // Adapter will grab any data in list and put it into the recyclerview it's attached to
        // RecyclerView <------- Adapter <------- List
        mFavBusStopAdapter = new FavBusStopAdapter(requireActivity(), mFavBusStopList, this);
        mRecyclerView.setAdapter(mFavBusStopAdapter);
    }

    private void populateRecyclerView() {
        mFavBusStopList.clear();
        mFavBusStopList.addAll(favBusStopDatabase.busStopDao().selectAll());
        mFavBusStopAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(requireActivity(), BusList.class);
        String busStopTitle = mFavBusStopList.get(position).getBusStopTitle();
        String busStopCode = mFavBusStopList.get(position).getBusStopCode();
        String busStopRoad = mFavBusStopList.get(position).getBusStopRoad();

        intent.putExtra("BusStopTitle", busStopTitle);
        intent.putExtra("BusStopCode", busStopCode);
        intent.putExtra("BusStopRoad", busStopRoad);
        startActivity(intent);
    }
}