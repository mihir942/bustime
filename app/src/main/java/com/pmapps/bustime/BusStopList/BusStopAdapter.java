package com.pmapps.bustime.BusStopList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmapps.bustime.R;

import java.util.ArrayList;

public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.BusStopViewHolder> {

    private final Context mContext;
    private final ArrayList<BusStopItem> mBusStopList;
    private final BusStopClickInterface mBusStopClickInterface;

    // Constructor
    public BusStopAdapter(Context context, ArrayList<BusStopItem> busStopList, BusStopClickInterface busStopClickInterface) {
        this.mContext = context;
        this.mBusStopList = busStopList;
        this.mBusStopClickInterface = busStopClickInterface;
    }

    @NonNull
    @Override
    public BusStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.bus_stop_item, parent, false);
        return new BusStopViewHolder(v);
    }

    // Attach data in list (busStopList) to TextViews etc
    @Override
    public void onBindViewHolder(@NonNull BusStopViewHolder holder, int position) {
        BusStopItem currentItem = mBusStopList.get(position);

        String busStopTitle = currentItem.getBusStopTitle();
        String busStopRoad = currentItem.getBusStopRoad();
        String busStopCode = currentItem.getBusStopCode();

        holder.mBusStopTitle.setText(busStopTitle);
        holder.mBusStopRoad.setText(busStopRoad);
        holder.mBusStopCode.setText(busStopCode);
    }

    @Override
    public int getItemCount() {
        return mBusStopList.size();
    }

    // -------------------------------------------------------------------------------------------

    // Inner class for ViewHolder
    public class BusStopViewHolder extends RecyclerView.ViewHolder {

        public TextView mBusStopTitle;
        public TextView mBusStopRoad;
        public TextView mBusStopCode;


        // Constructor for View Holder
        public BusStopViewHolder(@NonNull View itemView) {
            super(itemView);

            mBusStopTitle = itemView.findViewById(R.id.busStopTitle);
            mBusStopRoad = itemView.findViewById(R.id.busStopRoad);
            mBusStopCode = itemView.findViewById(R.id.busStopCode);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBusStopClickInterface.onItemClick(getAdapterPosition());
                }
            });

        }
    }


}
