package com.ashleyvenny.weatheronly;

/**
 * Created by ashleyvo on 6/8/15.
 */
public class WeatherOfDay {

    private Integer id;
    private String mainWeather;
    private String descrip;
    private String icon;

    private final String URL_FOR_ICON ="http://openweathermap.org/img/w/";
    private final String URL_FOR_ICON2=".png";
    private String icon_url;

    public Integer getId() {
        return id;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getIcon() {
        return icon;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setIcon_url(String i) {
        icon_url=URL_FOR_ICON+i+URL_FOR_ICON2;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMainWeather(String mainWeather) {
        this.mainWeather = mainWeather;
    }
}
