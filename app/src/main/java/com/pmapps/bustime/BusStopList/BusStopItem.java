package com.pmapps.bustime.BusStopList;

public class BusStopItem {
    private final String busStopTitle;
    private final String busStopRoad;
    private final String busStopCode;

    public BusStopItem(String busStopTitle, String busStopRoad, String busStopCode) {
        this.busStopTitle = busStopTitle;
        this.busStopRoad = busStopRoad;
        this.busStopCode = busStopCode;
    }

    public String getBusStopTitle() {
        return busStopTitle;
    }

    public String getBusStopRoad() {
        return busStopRoad;
    }

    public String getBusStopCode() {
        return busStopCode;
    }
}
