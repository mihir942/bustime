package com.pmapps.bustime.FavDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavBusStop.class}, version = 1, exportSchema = false)
public abstract class FavBusStopDatabase extends RoomDatabase {
    public abstract BusStopDao busStopDao();
}