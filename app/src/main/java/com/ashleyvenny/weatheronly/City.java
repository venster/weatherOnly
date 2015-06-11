package com.ashleyvenny.weatheronly;

/**
 * Created by ashleyvo on 6/8/15.
 */
public class City {

    private double lat;
    private double lon;
    private String name;
    private int id;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
}


