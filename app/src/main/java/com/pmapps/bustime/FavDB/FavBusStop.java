package com.pmapps.bustime.FavDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_list")
public class FavBusStop {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "busStopTitle")
    private String busStopTitle;

    @ColumnInfo(name = "busStopCode")
    private String busStopCode;

    @ColumnInfo(name = "busStopRoad")
    private String busStopRoad;

    public FavBusStop(String busStopTitle, String busStopCode, String busStopRoad) {
        this.busStopTitle = busStopTitle;
        this.busStopCode = busStopCode;
        this.busStopRoad = busStopRoad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusStopTitle() {
        return busStopTitle;
    }

    public void setBusStopTitle(String busStopTitle) {
        this.busStopTitle = busStopTitle;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getBusStopRoad() {
        return busStopRoad;
    }

    public void setBusStopRoad(String busStopRoad) {
        this.busStopRoad = busStopRoad;
    }
}
