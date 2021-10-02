package com.pmapps.bustime.BusList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pmapps.bustime.R;

import java.util.ArrayList;

import static com.pmapps.bustime.Logic.Logic.colorForLoad;
import static com.pmapps.bustime.Logic.Logic.imgForDecker;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.BusListViewHolder> {

    private final Context mContext;
    private final ArrayList<BusItem> mBusList;
    private final RefreshClickInterface mRefreshClickInterface;

    public BusListAdapter(Context context, ArrayList<BusItem> mBusList, RefreshClickInterface refreshClickInterface) {
        this.mContext = context;
        this.mBusList = mBusList;
        this.mRefreshClickInterface = refreshClickInterface;
    }

    // Inner class for View Holder
    public class BusListViewHolder extends RecyclerView.ViewHolder {

        public TextView mBusNumber;
        public View mSepLineView;
        public ImageButton mTimeRefreshButton;

        public ConstraintLayout mConstraintLayout1;
        public TextView mBusTime1;
        public View mBusLoad1;
        public ImageView mBusDecker1;

        public ConstraintLayout mConstraintLayout2;
        public TextView mBusTime2;
        public View mBusLoad2;
        public ImageView mBusDecker2;

        public ConstraintLayout mConstraintLayout3;
        public TextView mBusTime3;
        public View mBusLoad3;
        public ImageView mBusDecker3;


        // Constructor for View Holder
        public BusListViewHolder(@NonNull View itemView) {
            super(itemView);

            mBusNumber = itemView.findViewById(R.id.busNumber_TextView);
            mSepLineView = itemView.findViewById(R.id.sepLineView);
            mTimeRefreshButton = itemView.findViewById(R.id.timeRefreshButton);

            mConstraintLayout1 = itemView.findViewById(R.id.constraintLayout_bus1);
            mBusTime1 = itemView.findViewById(R.id.busTime1_TextView);
            mBusLoad1 = itemView.findViewById(R.id.busLoad1_View);
            mBusDecker1 = itemView.findViewById(R.id.busDecker1_ImageView);

            mConstraintLayout2 = itemView.findViewById(R.id.constraintLayout_bus2);
            mBusTime2 = itemView.findViewById(R.id.busTime2_TextView);
            mBusLoad2 = itemView.findViewById(R.id.busLoad2_View);
            mBusDecker2 = itemView.findViewById(R.id.busDecker2_ImageView);

            mConstraintLayout3 = itemView.findViewById(R.id.constraintLayout_bus3);
            mBusTime3 = itemView.findViewById(R.id.busTime3_TextView);
            mBusLoad3 = itemView.findViewById(R.id.busLoad3_View);
            mBusDecker3 = itemView.findViewById(R.id.busDecker3_ImageView);

            mTimeRefreshButton.setOnClickListener(v -> mRefreshClickInterface.onRefreshClick(getAdapterPosition(), mTimeRefreshButton));
        }
    }

    @NonNull
    @Override
    public BusListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.bus_item, parent, false);
        return new BusListViewHolder(v);

    }

    // Attach data in list (busList) to TextViews etc
    @Override
    public void onBindViewHolder(@NonNull BusListViewHolder holder, int position) {

        int oPosition = holder.getAdapterPosition();

        BusItem busItem = mBusList.get(oPosition);
        String busNumber = busItem.getBusNumber();
        holder.mBusNumber.setText(busNumber);

        // hasArrTime can be true, null, false

        String time1 = busItem.getBus_time_1();
        String time2 = busItem.getBus_time_2();
        String time3 = busItem.getBus_time_3();

        holder.mBusTime1.setText(time1);
        holder.mBusTime2.setText(time2);
        holder.mBusTime3.setText(time3);

        Log.d("BUS", oPosition + " " + busNumber);

        if (!busItem.getArrTimesHaveBeenSet()) {
            holder.mConstraintLayout1.setVisibility(View.INVISIBLE);
            holder.mBusDecker1.setVisibility(View.INVISIBLE);
            holder.mConstraintLayout2.setVisibility(View.INVISIBLE);
            holder.mBusDecker2.setVisibility(View.INVISIBLE);
            holder.mConstraintLayout3.setVisibility(View.INVISIBLE);
            holder.mBusDecker3.setVisibility(View.INVISIBLE);
            return;
        }

        if (time1.equals("--")) {
            holder.mConstraintLayout1.setVisibility(View.VISIBLE);
            holder.mBusTime1.setVisibility(View.VISIBLE);
            holder.mBusLoad1.setVisibility(View.INVISIBLE);
            holder.mBusDecker1.setVisibility(View.INVISIBLE);
        } else {
            holder.mConstraintLayout1.setVisibility(View.VISIBLE);
            holder.mBusTime1.setVisibility(View.VISIBLE);
            holder.mBusLoad1.setBackgroundColor(colorForLoad(busItem.getBus_load_1()));
            holder.mBusDecker1.setImageDrawable(imgForDecker(mContext, busItem.getBus_decker_1()));
            holder.mBusLoad1.setVisibility(View.VISIBLE);
            holder.mBusDecker1.setVisibility(View.VISIBLE);
        }

        if (time2.equals("--")) {
            holder.mConstraintLayout2.setVisibility(View.VISIBLE);
            holder.mBusTime2.setVisibility(View.VISIBLE);
            holder.mBusLoad2.setVisibility(View.INVISIBLE);
            holder.mBusDecker2.setVisibility(View.INVISIBLE);
        } else {
            holder.mConstraintLayout2.setVisibility(View.VISIBLE);
            holder.mBusTime2.setVisibility(View.VISIBLE);
            holder.mBusLoad2.setBackgroundColor(colorForLoad(busItem.getBus_load_2()));
            holder.mBusDecker2.setImageDrawable(imgForDecker(mContext, busItem.getBus_decker_2()));
            holder.mBusLoad2.setVisibility(View.VISIBLE);
            holder.mBusDecker2.setVisibility(View.VISIBLE);
        }

        if (time3.equals("--")) {
            holder.mConstraintLayout3.setVisibility(View.VISIBLE);
            holder.mBusTime3.setVisibility(View.VISIBLE);
            holder.mBusLoad3.setVisibility(View.INVISIBLE);
            holder.mBusDecker3.setVisibility(View.INVISIBLE);
        } else {
            holder.mConstraintLayout3.setVisibility(View.VISIBLE);
            holder.mBusTime3.setVisibility(View.VISIBLE);
            holder.mBusLoad3.setBackgroundColor(colorForLoad(busItem.getBus_load_3()));
            holder.mBusDecker3.setImageDrawable(imgForDecker(mContext, busItem.getBus_decker_3()));
            holder.mBusLoad3.setVisibility(View.VISIBLE);
            holder.mBusDecker3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mBusList.size();
    }

    // -------------------------------------------------------------------------------------------

}
