package com.pmapps.bustime.BusList;

import com.pmapps.bustime.Enums.Decker;
import com.pmapps.bustime.Enums.Load;

public class BusItem {
    private final String busNumber;

    private Boolean arrTimesHaveBeenSet;

    private String bus_time_1;
    private Load bus_load_1;
    private Decker bus_decker_1;

    private String bus_time_2;
    private Load bus_load_2;
    private Decker bus_decker_2;

    private String bus_time_3;
    private Load bus_load_3;
    private Decker bus_decker_3;


    // Constructor
    public BusItem(String busNumber) {
        this.busNumber = busNumber;
        this.arrTimesHaveBeenSet = false;
    }

    public Boolean getArrTimesHaveBeenSet() {
        return arrTimesHaveBeenSet;
    }

    public void setArrTimesHaveBeenSet(Boolean arrTimesHaveBeenSet) {
        this.arrTimesHaveBeenSet = arrTimesHaveBeenSet;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getBus_time_1() {
        return bus_time_1;
    }

    public Load getBus_load_1() {
        return bus_load_1;
    }

    public Decker getBus_decker_1() {
        return bus_decker_1;
    }

    public void setBus_time_1(String bus_time_1) {
        this.bus_time_1 = bus_time_1;
    }

    public void setBus_load_1(Load bus_load_1) {
        this.bus_load_1 = bus_load_1;
    }

    public void setBus_decker_1(Decker bus_decker_1) {
        this.bus_decker_1 = bus_decker_1;
    }

    public void setBus_time_2(String bus_time_2) {
        this.bus_time_2 = bus_time_2;
    }

    public void setBus_load_2(Load bus_load_2) {
        this.bus_load_2 = bus_load_2;
    }

    public void setBus_decker_2(Decker bus_decker_2) {
        this.bus_decker_2 = bus_decker_2;
    }

    public void setBus_time_3(String bus_time_3) {
        this.bus_time_3 = bus_time_3;
    }

    public void setBus_load_3(Load bus_load_3) {
        this.bus_load_3 = bus_load_3;
    }

    public void setBus_decker_3(Decker bus_decker_3) {
        this.bus_decker_3 = bus_decker_3;
    }

    public String getBus_time_2() {
        return bus_time_2;
    }

    public Load getBus_load_2() {
        return bus_load_2;
    }

    public Decker getBus_decker_2() {
        return bus_decker_2;
    }

    public String getBus_time_3() {
        return bus_time_3;
    }

    public Load getBus_load_3() {
        return bus_load_3;
    }

    public Decker getBus_decker_3() {
        return bus_decker_3;
    }
}
