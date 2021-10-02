package com.pmapps.bustime.FavDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BusStopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavBusStop(FavBusStop favBusStop);

    @Query("SELECT * FROM fav_list ORDER BY id ASC")
    List<FavBusStop> selectAll();

    @Query("DELETE FROM fav_list WHERE busStopCode= :code")
    void deleteBusStopWithCode(String code);

    @Query("SELECT COUNT() FROM fav_list WHERE busStopCode = :code")
    int exists(String code);

}
