package com.pmapps.bustime.FavDB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmapps.bustime.BusStopList.BusStopClickInterface;
import com.pmapps.bustime.R;

import java.util.List;

public class FavBusStopAdapter extends RecyclerView.Adapter<FavBusStopAdapter.FavBusStopViewHolder> {

    private final Context mContext;
    private final List<FavBusStop> mFavBusStopList;
    private final BusStopClickInterface mBusStopClickInterface;

    // Constructor
    public FavBusStopAdapter(Context context, List<FavBusStop> favBusStopList, BusStopClickInterface busStopClickInterface) {
        this.mContext = context;
        this.mFavBusStopList = favBusStopList;
        this.mBusStopClickInterface = busStopClickInterface;
    }

    @NonNull
    @Override
    public FavBusStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.bus_stop_item, parent, false);
        return new FavBusStopViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavBusStopViewHolder holder, int position) {
        FavBusStop currentItem = mFavBusStopList.get(position);

        String busStopTitle = currentItem.getBusStopTitle();
        String busStopRoad = currentItem.getBusStopRoad();
        String busStopCode = currentItem.getBusStopCode();

        holder.mBusStopTitle.setText(busStopTitle);
        holder.mBusStopRoad.setText(busStopRoad);
        holder.mBusStopCode.setText(busStopCode);
    }

    @Override
    public int getItemCount() {
        return mFavBusStopList.size();
    }

    // -------------------------------------------------------------------------------------------

    // inner class for ViewHolder
    public class FavBusStopViewHolder extends RecyclerView.ViewHolder {

        public TextView mBusStopTitle;
        public TextView mBusStopRoad;
        public TextView mBusStopCode;

        // Constructor for View Holder
        public FavBusStopViewHolder(@NonNull View itemView) {
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
